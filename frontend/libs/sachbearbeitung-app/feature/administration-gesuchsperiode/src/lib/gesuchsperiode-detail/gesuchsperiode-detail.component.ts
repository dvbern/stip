import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  effect,
  inject,
  input,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatDatepicker,
  MatDatepickerApply,
  MatDatepickerInput,
  MatDatepickerToggle,
} from '@angular/material/datepicker';
import {
  MatError,
  MatFormFieldModule,
  MatHint,
} from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { MaskitoModule } from '@maskito/angular';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoYear } from '@dv/shared/util/maskito-util';

import { PublishComponent } from '../publish/publish.component';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    MaskitoModule,
    MatError,
    MatFormFieldModule,
    MatHint,
    MatInput,
    TranslateModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiFormReadonlyDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    PublishComponent,
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatDatepickerApply,
  ],
  templateUrl: './gesuchsperiode-detail.component.html',
  providers: [provideDvDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeDetailComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);
  store = inject(GesuchsperiodeStore);
  router = inject(Router);
  route = inject(ActivatedRoute);
  maskitoYear = maskitoYear;
  idSig = input.required<string | undefined>({ alias: 'id' });

  form = this.formBuilder.group({
    bezeichnungDe: [<string | null>null, [Validators.required]],
    bezeichnungFr: [<string | null>null, [Validators.required]],
    fiskaljahr: [<string | null>null, [Validators.required]],
    gesuchsjahr: [<string | null>null, [Validators.required]],
    gesuchsperiodeStart: [<string | null>null, [Validators.required]],
    gesuchsperiodeStopp: [<string | null>null, [Validators.required]],
    aufschaltterminStart: [<string | null>null, [Validators.required]],
    aufschaltterminStopp: [<string | null>null, [Validators.required]],
    einreichefristNormal: [<string | null>null, [Validators.required]],
    einreichefristReduziert: [<string | null>null, [Validators.required]],
    ausbKosten_SekII: [<string | null>null, [Validators.required]],
    ausbKosten_Tertiaer: [<string | null>null, [Validators.required]],
    freibetrag_vermoegen: [<string | null>null, [Validators.required]],
    freibetrag_erwerbseinkommen: [<string | null>null, [Validators.required]],
    einkommensfreibetrag: [<string | null>null, [Validators.required]],
    elternbeteiligungssatz: [<string | null>null, [Validators.required]],
    vermoegensfreibetrag: [<string | null>null, [Validators.required]],
    vermogenSatzAngerechnet: [<string | null>null, [Validators.required]],
    integrationszulage: [<string | null>null, [Validators.required]],
    limite_EkFreibetrag_Integrationszulag: [
      <string | null>null,
      [Validators.required],
    ],
    stipLimite_Minimalstipendium: [<string | null>null, [Validators.required]],
    person_1: [<string | null>null, [Validators.required]],
    personen_2: [<string | null>null, [Validators.required]],
    personen_3: [<string | null>null, [Validators.required]],
    personen_4: [<string | null>null, [Validators.required]],
    personen_5: [<string | null>null, [Validators.required]],
    personen_6: [<string | null>null, [Validators.required]],
    personen_7: [<string | null>null, [Validators.required]],
    proWeiterePerson: [<string | null>null, [Validators.required]],
    kinder_00_18: [<string | null>null, [Validators.required]],
    jugendliche_erwachsene_19_25: [<string | null>null, [Validators.required]],
    erwachsene_26_99: [<string | null>null, [Validators.required]],
    wohnkosten_fam_1pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_2pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_3pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_4pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_5pluspers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_1pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_2pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_3pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_4pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_5pluspers: [
      <string | null>null,
      [Validators.required],
    ],
  });

  private numberConverter = this.formUtils.createNumberConverter(this.form, [
    'fiskaljahr',
    'gesuchsjahr',
    'ausbKosten_SekII',
    'ausbKosten_Tertiaer',
    'freibetrag_vermoegen',
    'freibetrag_erwerbseinkommen',
    'elternbeteiligungssatz',
    'einkommensfreibetrag',
    'vermoegensfreibetrag',
    'vermogenSatzAngerechnet',
    'integrationszulage',
    'limite_EkFreibetrag_Integrationszulag',
    'stipLimite_Minimalstipendium',
    'person_1',
    'personen_2',
    'personen_3',
    'personen_4',
    'personen_5',
    'personen_6',
    'personen_7',
    'proWeiterePerson',
    'kinder_00_18',
    'jugendliche_erwachsene_19_25',
    'erwachsene_26_99',
    'wohnkosten_fam_1pers',
    'wohnkosten_fam_2pers',
    'wohnkosten_fam_3pers',
    'wohnkosten_fam_4pers',
    'wohnkosten_fam_5pluspers',
    'wohnkosten_persoenlich_1pers',
    'wohnkosten_persoenlich_2pers',
    'wohnkosten_persoenlich_3pers',
    'wohnkosten_persoenlich_4pers',
    'wohnkosten_persoenlich_5pluspers',
  ]);

  constructor() {
    effect(
      () => {
        const id = this.idSig();
        if (id) {
          this.store.loadGesuchsperiode$(id);
        }
      },
      { allowSignalWrites: true },
    );
    effect(() => {
      const gesuchsperiode = this.store.currentGesuchsperiodeViewSig();
      if (!gesuchsperiode) {
        return;
      }
      this.form.patchValue({
        ...gesuchsperiode,
        ...this.numberConverter.toString(gesuchsperiode),
      });
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (this.form.invalid) {
      return;
    }
    const value = convertTempFormToRealValues(this.form);
    this.store.saveGesuchsperiode$({
      gesuchsperiodeId: this.idSig(),
      gesuchsperiodenDaten: {
        ...value,
        ...this.numberConverter.toNumber(),
      },
      onAfterSave: (gesuchsjahr) => {
        this.router.navigate(['..', gesuchsjahr.id], {
          relativeTo: this.route,
        });
      },
    });
  }
}
