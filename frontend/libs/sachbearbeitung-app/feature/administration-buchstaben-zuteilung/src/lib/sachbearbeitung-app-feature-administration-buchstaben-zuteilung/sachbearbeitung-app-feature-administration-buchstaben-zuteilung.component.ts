import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  inject,
  signal,
  viewChild,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MaskitoDirective } from '@maskito/angular';
import { MaskitoOptions } from '@maskito/core';
import { TranslatePipe } from '@ngx-translate/core';

import { SachbearbeiterStore } from '@dv/sachbearbeitung-app/data-access/sachbearbeiter';
import { BuchstabenZuordnung } from '@dv/sachbearbeitung-app/model/administration';
import {
  cleanupZuweisung,
  removeDuplicates,
  sortZuweisung,
} from '@dv/sachbearbeitung-app/util-fn/sachbearbeiter-helper';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import {
  SharedUiFormFieldDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import {
  SharedUtilFormService,
  provideMaterialDefaultOptions,
} from '@dv/shared/util/form';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

const CHAR = '[a-z]{1,3} ?';
const RANGE = `${CHAR}- ?(${CHAR})?`;
@Component({
  selector:
    'dv-sachbearbeitung-app-feature-administration-buchstaben-zuteilung',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatTooltipModule,
    MatCheckboxModule,
    TranslatePipe,
    SharedUiFormSaveComponent,
    SharedUiFormFieldDirective,
    SharedUiRdIsPendingPipe,
    SharedUiMaxLengthDirective,
    SharedUiClearButtonComponent,
  ],
  providers: [
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
    paginatorTranslationProvider(),
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-buchstaben-zuteilung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent
  implements AfterViewInit
{
  private elementRef = inject(ElementRef);
  private formBuilder = inject(FormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private sortSig = viewChild(MatSort);
  private paginatorSig = viewChild(MatPaginator);
  store = inject(SachbearbeiterStore);
  filterChangedSig = signal<string | null>(null);
  displayedColumns = ['fullName', 'buchstabenDe', 'buchstabenFr'] as const;
  zuweisungInputMask: MaskitoOptions = {
    mask: new RegExp(`^(${RANGE}|${CHAR})(, ?(${RANGE}|${CHAR})?)*$`, 'i'),
    postprocessors: [
      ({ selection, value }) => {
        return {
          selection,
          value: removeDuplicates(value.toUpperCase()),
        };
      },
    ],
  };

  formSig = computed(() => {
    const zuweisungen = this.store.zuweisungenViewSig();
    const createFormGroup = (z?: BuchstabenZuordnung) =>
      this.formBuilder.group({
        enabledDe: [z?.enabledDe],
        buchstabenDe: [z?.buchstabenDe],
        enabledFr: [z?.enabledFr],
        buchstabenFr: [z?.buchstabenFr],
      });
    const form = this.formBuilder.group(
      zuweisungen.reduce(
        (acc, u) => ({
          ...acc,
          [u.benutzerId]: createFormGroup(u),
        }),
        {} as Record<string, ReturnType<typeof createFormGroup>>,
      ),
    );
    return form;
  });
  zuweisungDataSourceSig = computed(() => {
    const initialized = this.afterViewInitSig();
    if (!initialized) {
      return new MatTableDataSource([]);
    }
    const filter = this.filterChangedSig();
    const zuweisungen = this.store.zuweisungenViewSig();

    const datasource = new MatTableDataSource(zuweisungen);
    datasource.filterPredicate = (data, filter) => {
      return data.fullName.toLocaleLowerCase().includes(filter);
    };
    datasource.sort = this.sortSig() ?? null;
    datasource.paginator = this.paginatorSig() ?? null;
    if (filter) {
      datasource.filter = filter.trim().toLocaleLowerCase();
    }
    return datasource;
  });

  private afterViewInitSig = signal(false);

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
  }

  ngAfterViewInit() {
    this.afterViewInitSig.set(true);
  }

  handleSave() {
    const form = this.formSig();
    form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const formValues = this.buildUpdatedGesuchFromForm();
    if (form.valid && formValues) {
      this.store.saveSachbearbeiterZuweisung$(formValues);
      form.markAsPristine();
    }
  }

  shouldHandleLanguage(enabled: boolean, id: string, language: 'De' | 'Fr') {
    const control =
      this.formSig().controls[id]?.controls[`buchstaben${language}`];
    if (!control) {
      return;
    }
    this.formUtils.setRequired(control, enabled);
    if (!enabled) {
      control.setValue(undefined);
    }
  }

  normalizeZuweisung(id: string, language: 'De' | 'Fr') {
    const form = this.formSig();
    const control = form.controls[id]?.controls[`buchstaben${language}`];
    const enabledControl = form.controls[id]?.controls[`enabled${language}`];
    const value = control?.value;
    if (!value) {
      control.reset(undefined, { onlySelf: true });
      this.formUtils.setRequired(control, false);
      enabledControl?.setValue(false, { emitEvent: false });
      return;
    }
    if (!control || !enabledControl) {
      return;
    }
    control.setValue(sortZuweisung(cleanupZuweisung(value)));
    enabledControl.setValue(true);
  }

  private buildUpdatedGesuchFromForm() {
    const form = this.formSig();
    return Object.entries(form.controls).map(([benutzerId, group]) => {
      const { buchstabenDe, buchstabenFr } = group.getRawValue();
      return {
        sachbearbeiter: benutzerId,
        zuordnung: {
          buchstabenDe: buchstabenDe ?? undefined,
          buchstabenFr: buchstabenFr ?? undefined,
        },
      };
    });
  }
}
