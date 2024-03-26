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
import { DateAdapter } from '@angular/material/core';
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
import { provideDateFnsAdapter } from '@angular/material-date-fns-adapter';
import { MaskitoModule } from '@maskito/angular';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { fromFormatedNumber } from '@dv/shared/util/maskito-util';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuchsperiode-detail',
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
  templateUrl: './gesuchsperiode-detail.component.html',
  providers: [provideDvDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeDetailComponent implements OnInit {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);
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
    b_Einkommenfreibetrag: [<string | null>null, [Validators.required]],
    b_VermogenSatzAngerechnet: [<string | null>null, [Validators.required]],
    b_Verpf_Auswaerts_Tagessatz: [<string | null>null, [Validators.required]],
    elternbeteiligungssatz: [<string | null>null, [Validators.required]],
    f_Einkommensfreibetrag: [<string | null>null, [Validators.required]],
    f_Vermoegensfreibetrag: [<string | null>null, [Validators.required]],
    f_VermogenSatzAngerechnet: [<string | null>null, [Validators.required]],
    integrationszulage: [<string | null>null, [Validators.required]],
    limite_EkFreibetrag_Integrationszulag: [
      <string | null>null,
      [Validators.required],
    ],
    stipLimite_Minimalstipendium: [<string | null>null, [Validators.required]],
    person_1: [<string | null>null, [Validators.required]],
    person_2: [<string | null>null, [Validators.required]],
    person_3: [<string | null>null, [Validators.required]],
    person_4: [<string | null>null, [Validators.required]],
    person_5: [<string | null>null, [Validators.required]],
    person_6: [<string | null>null, [Validators.required]],
    person_7: [<string | null>null, [Validators.required]],
    ppP_8: [<string | null>null, [Validators.required]],
    _00_18: [<string | null>null, [Validators.required]],
    _19_25: [<string | null>null, [Validators.required]],
    _26_99: [<string | null>null, [Validators.required]],
    bB_1Pers: [<string | null>null, [Validators.required]],
    bB_2Pers: [<string | null>null, [Validators.required]],
    bB_3Pers: [<string | null>null, [Validators.required]],
    bB_4Pers: [<string | null>null, [Validators.required]],
    bB_5Pers: [<string | null>null, [Validators.required]],
    fB_1Pers: [<string | null>null, [Validators.required]],
    fB_2Pers: [<string | null>null, [Validators.required]],
    fB_3Pers: [<string | null>null, [Validators.required]],
    fB_4Pers: [<string | null>null, [Validators.required]],
    fB_5Pers: [<string | null>null, [Validators.required]],
  });

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
    const value = convertTempFormToRealValues(this.form, 'all');
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
