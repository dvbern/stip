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
export class GesuchsperiodeDetailComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);
  id = input.required<string>();

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

  store = inject(GesuchsperiodeStore);

  constructor() {
    effect(
      () => {
        this.store.loadGesuchsperiode$(this.id());
      },
      { allowSignalWrites: true },
    );
    effect(() => {
      const gesuchsperiode = this.store.currentGesuchsperiode?.().data;
      if (!gesuchsperiode) {
        return;
      }
      this.form.patchValue({
        ...gesuchsperiode,
        ausbKosten_SekII: gesuchsperiode.ausbKosten_SekII.toString(),
        ausbKosten_Tertiaer: gesuchsperiode.ausbKosten_Tertiaer.toString(),
        freibetrag_vermoegen: gesuchsperiode.freibetrag_vermoegen.toString(),
        freibetrag_erwerbseinkommen:
          gesuchsperiode.freibetrag_erwerbseinkommen.toString(),
        elternbeteiligungssatz:
          gesuchsperiode.elternbeteiligungssatz.toString(),
        einkommensfreibetrag: gesuchsperiode.einkommensfreibetrag.toString(),
        vermoegensfreibetrag: gesuchsperiode.vermoegensfreibetrag?.toString(),
        vermogenSatzAngerechnet:
          gesuchsperiode.vermogenSatzAngerechnet?.toString(),
        integrationszulage: gesuchsperiode.integrationszulage.toString(),
        limite_EkFreibetrag_Integrationszulag:
          gesuchsperiode.limite_EkFreibetrag_Integrationszulag.toString(),
        stipLimite_Minimalstipendium:
          gesuchsperiode.stipLimite_Minimalstipendium.toString(),
        person_1: gesuchsperiode.person_1.toString(),
        personen_2: gesuchsperiode.personen_2.toString(),
        personen_3: gesuchsperiode.personen_3.toString(),
        personen_4: gesuchsperiode.personen_4.toString(),
        personen_5: gesuchsperiode.personen_5.toString(),
        personen_6: gesuchsperiode.personen_6.toString(),
        personen_7: gesuchsperiode.personen_7.toString(),
        proWeiterePerson: gesuchsperiode.proWeiterePerson.toString(),
        kinder_00_18: gesuchsperiode.kinder_00_18.toString(),
        jugendliche_erwachsene_19_25:
          gesuchsperiode.jugendliche_erwachsene_19_25.toString(),
        erwachsene_26_99: gesuchsperiode.erwachsene_26_99.toString(),
        wohnkosten_fam_1pers: gesuchsperiode.wohnkosten_fam_1pers.toString(),
        wohnkosten_fam_2pers: gesuchsperiode.wohnkosten_fam_2pers.toString(),
        wohnkosten_fam_3pers: gesuchsperiode.wohnkosten_fam_3pers.toString(),
        wohnkosten_fam_4pers: gesuchsperiode.wohnkosten_fam_4pers.toString(),
        wohnkosten_fam_5pluspers:
          gesuchsperiode.wohnkosten_fam_5pluspers.toString(),
        wohnkosten_persoenlich_1pers:
          gesuchsperiode.wohnkosten_persoenlich_1pers.toString(),
        wohnkosten_persoenlich_2pers:
          gesuchsperiode.wohnkosten_persoenlich_2pers.toString(),
        wohnkosten_persoenlich_3pers:
          gesuchsperiode.wohnkosten_persoenlich_3pers.toString(),
        wohnkosten_persoenlich_4pers:
          gesuchsperiode.wohnkosten_persoenlich_4pers.toString(),
        wohnkosten_persoenlich_5pluspers:
          gesuchsperiode.wohnkosten_persoenlich_5pluspers.toString(),
      });
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (!this.form.valid) {
      return;
    }
    const value = convertTempFormToRealValues(this.form, 'all');
    this.store.saveGesuchsperiode$({
      gesuchsperiodeId: this.id(),
      gesuchsperiodenDaten: {
        ...value,
        ausbKosten_SekII: fromFormatedNumber(value.ausbKosten_SekII),
        ausbKosten_Tertiaer: fromFormatedNumber(value.ausbKosten_Tertiaer),
        freibetrag_vermoegen: fromFormatedNumber(value.freibetrag_vermoegen),
        freibetrag_erwerbseinkommen: fromFormatedNumber(
          value.freibetrag_erwerbseinkommen,
        ),
        elternbeteiligungssatz: fromFormatedNumber(
          value.elternbeteiligungssatz,
        ),
        einkommensfreibetrag: fromFormatedNumber(value.einkommensfreibetrag),
        vermoegensfreibetrag: fromFormatedNumber(value.vermoegensfreibetrag),
        vermogenSatzAngerechnet: fromFormatedNumber(
          value.vermogenSatzAngerechnet,
        ),
        integrationszulage: fromFormatedNumber(value.integrationszulage),
        limite_EkFreibetrag_Integrationszulag: fromFormatedNumber(
          value.limite_EkFreibetrag_Integrationszulag,
        ),
        stipLimite_Minimalstipendium: fromFormatedNumber(
          value.stipLimite_Minimalstipendium,
        ),
        person_1: fromFormatedNumber(value.person_1),
        personen_2: fromFormatedNumber(value.personen_2),
        personen_3: fromFormatedNumber(value.personen_3),
        personen_4: fromFormatedNumber(value.personen_4),
        personen_5: fromFormatedNumber(value.personen_5),
        personen_6: fromFormatedNumber(value.personen_6),
        personen_7: fromFormatedNumber(value.personen_7),
        proWeiterePerson: fromFormatedNumber(value.proWeiterePerson),
        kinder_00_18: fromFormatedNumber(value.kinder_00_18),
        jugendliche_erwachsene_19_25: fromFormatedNumber(
          value.jugendliche_erwachsene_19_25,
        ),
        erwachsene_26_99: fromFormatedNumber(
          value.jugendliche_erwachsene_19_25,
        ),
        wohnkosten_fam_1pers: fromFormatedNumber(value.wohnkosten_fam_1pers),
        wohnkosten_fam_2pers: fromFormatedNumber(value.wohnkosten_fam_2pers),
        wohnkosten_fam_3pers: fromFormatedNumber(value.wohnkosten_fam_3pers),
        wohnkosten_fam_4pers: fromFormatedNumber(value.wohnkosten_fam_4pers),
        wohnkosten_fam_5pluspers: fromFormatedNumber(
          value.wohnkosten_fam_5pluspers,
        ),
        wohnkosten_persoenlich_1pers: fromFormatedNumber(
          value.wohnkosten_persoenlich_1pers,
        ),
        wohnkosten_persoenlich_2pers: fromFormatedNumber(
          value.wohnkosten_persoenlich_2pers,
        ),
        wohnkosten_persoenlich_3pers: fromFormatedNumber(
          value.wohnkosten_persoenlich_3pers,
        ),
        wohnkosten_persoenlich_4pers: fromFormatedNumber(
          value.wohnkosten_persoenlich_4pers,
        ),
        wohnkosten_persoenlich_5pluspers: fromFormatedNumber(
          value.wohnkosten_persoenlich_5pluspers,
        ),
      },
    });
  }
}
