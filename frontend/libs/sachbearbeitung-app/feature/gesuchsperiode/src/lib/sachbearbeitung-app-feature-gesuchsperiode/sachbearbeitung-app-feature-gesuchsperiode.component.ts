import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
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
import { MatInput, MatLabel } from '@angular/material/input';
import { MaskitoModule } from '@maskito/angular';
import { TranslateModule } from '@ngx-translate/core';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuchsperiode',
  standalone: true,
  imports: [
    CommonModule,
    MaskitoModule,
    MatError,
    MatFormFieldModule,
    MatHint,
    MatInput,
    MatLabel,
    TranslateModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    GesuchAppUiStepFormButtonsComponent,
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatDatepickerApply,
  ],
  templateUrl: './sachbearbeitung-app-feature-gesuchsperiode.component.html',
  styleUrl: './sachbearbeitung-app-feature-gesuchsperiode.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchsperiodeComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  form = this.formBuilder.group({
    bezeichnung: [<string | null>null, [Validators.required]],
    fiskaljahr: [<string | null>null, [Validators.required]],
    gesuchsperiodeStart: [<number | null>null, [Validators.required]],
    gesuchsperiodeStopp: [<number | null>null, [Validators.required]],
    aufschaltterminStart: [<number | null>null, [Validators.required]],
    aufschaltterminStopp: [<number | null>null, [Validators.required]],
    einreichefristNormal: [<number | null>null, [Validators.required]],
    einreichefristReduziert: [<number | null>null, [Validators.required]],
    ausbKosten_SekII: [<number | null>null, [Validators.required]],
    ausbKosten_Tertiaer: [<number | null>null, [Validators.required]],
    b_Einkommenfreibetrag: [<number | null>null, [Validators.required]],
    b_VermogenSatzAngerechnet: [<number | null>null, [Validators.required]],
    b_Verpf_Auswaerts_Tagessatz: [<number | null>null, [Validators.required]],
    elternbeteiligungssatz: [<number | null>null, [Validators.required]],
    f_Einkommensfreibetrag: [<number | null>null, [Validators.required]],
    f_Vermoegensfreibetrag: [<number | null>null, [Validators.required]],
    f_VermogenSatzAngerechnet: [<number | null>null, [Validators.required]],
    integrationszulage: [<number | null>null, [Validators.required]],
    limite_EkFreibetrag_Integrationszulag: [
      <number | null>null,
      [Validators.required],
    ],
    stipLimite_Minimalstipendium: [<number | null>null, [Validators.required]],
    person_1: [<number | null>null, [Validators.required]],
    person_2: [<number | null>null, [Validators.required]],
    person_3: [<number | null>null, [Validators.required]],
    person_4: [<number | null>null, [Validators.required]],
    person_5: [<number | null>null, [Validators.required]],
    person_6: [<number | null>null, [Validators.required]],
    person_7: [<number | null>null, [Validators.required]],
    ppP_8: [<number | null>null, [Validators.required]],
    _00_18: [<number | null>null, [Validators.required]],
    _19_25: [<number | null>null, [Validators.required]],
    _26_99: [<number | null>null, [Validators.required]],
    bB_1Pers: [<number | null>null, [Validators.required]],
    bB_2Pers: [<number | null>null, [Validators.required]],
    bB_3Pers: [<number | null>null, [Validators.required]],
    bB_4Pers: [<number | null>null, [Validators.required]],
    bB_5Pers: [<number | null>null, [Validators.required]],
    fB_1Pers: [<number | null>null, [Validators.required]],
    fB_2Pers: [<number | null>null, [Validators.required]],
    fB_3Pers: [<number | null>null, [Validators.required]],
    fB_4Pers: [<number | null>null, [Validators.required]],
    fB_5Pers: [<number | null>null, [Validators.required]],
  });

  startDate = new Date();
}
