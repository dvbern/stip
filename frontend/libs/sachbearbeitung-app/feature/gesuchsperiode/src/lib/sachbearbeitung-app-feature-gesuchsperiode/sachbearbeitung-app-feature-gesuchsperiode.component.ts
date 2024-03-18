import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  effect,
  inject,
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
import { MaskitoModule } from '@maskito/angular';
import { TranslateModule } from '@ngx-translate/core';
import { elementAt, from } from 'rxjs';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUtilFormService } from '@dv/shared/util/form';
import { fromFormatedNumber } from '@dv/shared/util/maskito-util';

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
export class SachbearbeitungAppFeatureGesuchsperiodeComponent
  implements OnInit
{
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);
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

  store = inject(GesuchsperiodeStore);

  ngOnInit(): void {
    this.store.loadGesuchsperiode();
  }

  constructor() {
    effect(() => {
      const gesuchsperiode = this.store.currentGesuchsperiode?.();
      if (!gesuchsperiode) {
        return;
      }
      this.form.patchValue(gesuchsperiode as any);
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (!this.form.valid) {
      return;
    }
    console.log(this.form.value);
    const value = this.form.value;
    this.store.saveGesuchsperiode({
      ...value,
      ausbKosten_SekII: fromFormatedNumber(value.ausbKosten_SekII),
      ausbKosten_Tertiaer: fromFormatedNumber(value.ausbKosten_Tertiaer),
      b_Einkommenfreibetrag: fromFormatedNumber(value.b_Einkommenfreibetrag),
      b_VermogenSatzAngerechnet: fromFormatedNumber(
        value.b_VermogenSatzAngerechnet,
      ),
      b_Verpf_Auswaerts_Tagessatz: fromFormatedNumber(
        value.b_Verpf_Auswaerts_Tagessatz,
      ),
      elternbeteiligungssatz: fromFormatedNumber(value.elternbeteiligungssatz),
      f_Einkommensfreibetrag: fromFormatedNumber(value.f_Einkommensfreibetrag),
      f_Vermoegensfreibetrag: fromFormatedNumber(value.f_Vermoegensfreibetrag),
      f_VermogenSatzAngerechnet: fromFormatedNumber(
        value.f_VermogenSatzAngerechnet,
      ),
      integrationszulage: fromFormatedNumber(value.integrationszulage),
      limite_EkFreibetrag_Integrationszulag: fromFormatedNumber(
        value.limite_EkFreibetrag_Integrationszulag,
      ),
      stipLimite_Minimalstipendium: fromFormatedNumber(
        value.stipLimite_Minimalstipendium,
      ),
      person_1: fromFormatedNumber(value.person_1),
      person_2: fromFormatedNumber(value.person_2),
      person_3: fromFormatedNumber(value.person_3),
      person_4: fromFormatedNumber(value.person_4),
      person_5: fromFormatedNumber(value.person_5),
      person_6: fromFormatedNumber(value.person_6),
      person_7: fromFormatedNumber(value.person_7),
      ppP_8: fromFormatedNumber(value.ppP_8),
      _00_18: fromFormatedNumber(value._00_18),
      _19_25: fromFormatedNumber(value._19_25),
      _26_99: fromFormatedNumber(value._19_25),
      bB_1Pers: fromFormatedNumber(value.bB_1Pers),
      bB_2Pers: fromFormatedNumber(value.bB_2Pers),
      bB_3Pers: fromFormatedNumber(value.bB_3Pers),
      bB_4Pers: fromFormatedNumber(value.bB_4Pers),
      bB_5Pers: fromFormatedNumber(value.bB_5Pers),
      fB_1Pers: fromFormatedNumber(value.fB_1Pers),
      fB_2Pers: fromFormatedNumber(value.fB_2Pers),
      fB_3Pers: fromFormatedNumber(value.fB_3Pers),
      fB_4Pers: fromFormatedNumber(value.fB_4Pers),
      fB_5Pers: fromFormatedNumber(value.fB_5Pers),
    });
  }
}
