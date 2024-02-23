import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  ViewChild,
  computed,
  inject,
  signal,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MaskitoModule } from '@maskito/angular';
import { MaskitoOptions } from '@maskito/core';
import { TranslateModule } from '@ngx-translate/core';

import { SachbearbeiterStore } from '@dv/sachbearbeitung-app/data-access/sachbearbeiter';
import { BuchstabenZuordnung } from '@dv/sachbearbeitung-app/model/administration';
import {
  cleanupZuweisung,
  removeDuplicates,
  sortZuweisung,
} from '@dv/sachbearbeitung-app/util-fn/sachbearbeiter-helper';
import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUtilFormService } from '@dv/shared/util/form';
import { createFilterableColumns } from '@dv/shared/util-fn/table-helper';

import {
  selectSachbearbeitungAppBuchstabenZuteilung,
  selectSachbearbeitungAppBuchstabenZuteilungState,
} from './sachbearbeitung-app-feature-administration-buchstaben-zuteilung.selector';

const CHAR = '[a-z]{1,3} ?';
const RANGE = `${CHAR}- ?(${CHAR})?`;
@Component({
  selector:
    'dv-sachbearbeitung-app-feature-administration-buchstaben-zuteilung',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaskitoModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatCheckboxModule,
    TranslateModule,
    SharedUiLoadingComponent,
    SharedUiFormSaveComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  providers: [
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-buchstaben-zuteilung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent
  implements AfterViewInit
{
  @ViewChild(MatSort) sort!: MatSort;
  private elementRef = inject(ElementRef);
  private formBuilder = inject(FormBuilder);
  private formUtils = inject(SharedUtilFormService);
  zuteilungSig = selectSachbearbeitungAppBuchstabenZuteilung();
  stateSig = selectSachbearbeitungAppBuchstabenZuteilungState();
  store = inject(SachbearbeiterStore);
  filterChangedSig = signal<string | null>(null);
  displayedColumns = [
    'fullName',
    'enabledDe',
    'buchstabenDe',
    'enabledFr',
    'buchstabenFr',
  ] as const;
  displayedColumnFilters = createFilterableColumns(this.displayedColumns, [
    'fullName',
  ]);
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
    const zuweisungen = this.zuteilungSig();
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
    const zuweisungen = this.zuteilungSig();

    const datasource = new MatTableDataSource(zuweisungen);
    datasource.filterPredicate = (data, filter) => {
      return data.fullName.toLocaleLowerCase().includes(filter);
    };
    datasource.sort = this.sort;
    if (filter) {
      datasource.filter = filter.trim().toLocaleLowerCase();
    }
    return datasource;
  });

  private afterViewInitSig = signal(false);

  ngAfterViewInit() {
    this.afterViewInitSig.set(true);
  }

  handleSave() {
    const form = this.formSig();
    form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const formValues = this.buildUpdatedGesuchFromForm();
    if (form.valid && formValues) {
      this.store.saveSachbearbeiterZuweisung(formValues);
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
      control.reset();
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
        benutzerId,
        sachbearbeiterZuordnungStammdaten: {
          buchstabenDe: buchstabenDe ?? undefined,
          buchstabenFr: buchstabenFr ?? undefined,
        },
      };
    });
  }
}
