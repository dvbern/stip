import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  effect,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { Subject } from 'rxjs';

import { SharedEventGesuchFormDarlehen } from '@dv/shared/event/gesuch-form-darlehen';
import { DokumentTyp } from '@dv/shared/model/gesuch';
import { DARLEHEN } from '@dv/shared/model/gesuch-form';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';

import { selectSharedFeatureGesuchFormDarlehenView } from './shared-feature-darlehen.selector';

@Component({
  selector: 'dv-shared-feature-darlehen',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatRadioModule,
    MaskitoDirective,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedUiZuvorHintDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiFormMessageErrorDirective,
    SharedUiTranslateChangePipe,
    SharedUiFormZuvorHintComponent,
    SharedUiFormReadonlyDirective,
    SharedUiStepFormButtonsComponent,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenComponent implements OnInit {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormDarlehenView);
  maskitoNumber = maskitoNumber;
  gotReenabled$ = new Subject<object>();

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);
  private gotReenabledSig = toSignal(this.gotReenabled$);

  private atLeastOneCheckboxChecked: ValidatorFn = (
    control: AbstractControl,
  ) => {
    const checked = Object.values(control.value).some((v) => v === true);
    return checked ? null : { atLeastOneCheckboxChecked: true };
  };

  form = this.formBuilder.group({
    willDarlehen: [<boolean | undefined>undefined, [Validators.required]],
    betragDarlehen: [<string | null>null, [Validators.required]],
    betragBezogenKanton: [<string | null>null, [Validators.required]],
    schulden: [<string | null>null, [Validators.required]],
    anzahlBetreibungen: [<number | null>null, [Validators.required]],
    gruende: this.formBuilder.group(
      {
        grundNichtBerechtigt: [<boolean | undefined>undefined],
        grundAusbildungZwoelfJahre: [<boolean | undefined>undefined],
        grundHoheGebuehren: [<boolean | undefined>undefined],
        grundAnschaffungenFuerAusbildung: [<boolean | undefined>undefined],
        grundZweitausbildung: [<boolean | undefined>undefined],
      },
      { validators: [this.atLeastOneCheckboxChecked] },
    ),
  });

  willDarlehenChangedSig = toSignal(
    this.form.controls.willDarlehen.valueChanges,
  );
  grundNichtBerechtigtChangedSig = toSignal(
    this.form.controls.gruende.controls.grundNichtBerechtigt.valueChanges,
  );
  grundHoheGebuehrenChangedSig = toSignal(
    this.form.controls.gruende.controls.grundHoheGebuehren.valueChanges,
  );
  grundAnschaffungenFuerAusbildungChangedSig = toSignal(
    this.form.controls.gruende.controls.grundAnschaffungenFuerAusbildung
      .valueChanges,
  );

  anzahlBetreibungenDocSig = this.createUploadOptionsSig(() => {
    const willDarlehen = this.willDarlehenChangedSig() ?? false;
    return willDarlehen ? DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG : null;
  });
  grundNichtBerechtigtDocSig = this.createUploadOptionsSig(() => {
    const isGrundNichtBerechtigt = this.grundNichtBerechtigtChangedSig();
    return isGrundNichtBerechtigt
      ? DokumentTyp.DARLEHEN_AUFSTELLUNG_KOSTEN_ELTERN
      : null;
  });
  grundHoheGebuehrenDocSig = this.createUploadOptionsSig(() => {
    const isGrundHoheGebuehren = this.grundHoheGebuehrenChangedSig();
    return isGrundHoheGebuehren
      ? DokumentTyp.DARLEHEN_KOPIE_SCHULGELDRECHNUNG
      : null;
  });
  grundAnschaffungenFuerAusbildungDocSig = this.createUploadOptionsSig(() => {
    const isGrundAnschaffungenFuerAusbildung =
      this.grundAnschaffungenFuerAusbildungChangedSig();
    return isGrundAnschaffungenFuerAusbildung
      ? DokumentTyp.DARLEHEN_BELEGE_ANSCHAFFUNGEN
      : null;
  });

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormDarlehen.init());
  }

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);

    // set disabled state
    effect(
      () => {
        this.gotReenabledSig();
        const willDarlehen = this.willDarlehenChangedSig() ?? false;

        this.formUtils.setDisabledState(
          this.form.controls.betragDarlehen,
          !willDarlehen,
          true,
        );
        this.formUtils.setDisabledState(
          this.form.controls.betragBezogenKanton,
          !willDarlehen,
          true,
        );
        this.formUtils.setDisabledState(
          this.form.controls.schulden,
          !willDarlehen,
          true,
        );
        this.formUtils.setDisabledState(
          this.form.controls.anzahlBetreibungen,
          !willDarlehen,
          true,
        );
        this.formUtils.setDisabledState(
          this.form.controls.gruende,
          !willDarlehen,
          true,
        );
      },
      { allowSignalWrites: true },
    );

    // fill form
    effect(
      () => {
        const { darlehen } = this.viewSig();
        if (darlehen) {
          this.form.patchValue({
            willDarlehen: darlehen.willDarlehen,
            betragDarlehen: darlehen.betragDarlehen?.toString(),
            betragBezogenKanton: darlehen.betragBezogenKanton?.toString(),
            schulden: darlehen.schulden?.toString(),
            anzahlBetreibungen: darlehen.anzahlBetreibungen,
            gruende: {
              grundNichtBerechtigt: darlehen.grundNichtBerechtigt,
              grundAusbildungZwoelfJahre: darlehen.grundAusbildungZwoelfJahre,
              grundHoheGebuehren: darlehen.grundHoheGebuehren,
              grundAnschaffungenFuerAusbildung:
                darlehen.grundAnschaffungenFuerAusbildung,
              grundZweitausbildung: darlehen.grundZweitausbildung,
            },
          });
        } else {
          this.form.reset();
        }
      },
      { allowSignalWrites: true },
    );
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'willDarlehen',
      'betragDarlehen',
      'betragBezogenKanton',
      'schulden',
      'anzahlBetreibungen',
    ]);

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        darlehen: {
          willDarlehen: formValues.willDarlehen,
          betragDarlehen: fromFormatedNumber(formValues.betragDarlehen),
          betragBezogenKanton: fromFormatedNumber(
            formValues.betragBezogenKanton,
          ),
          schulden: fromFormatedNumber(formValues.schulden),
          anzahlBetreibungen: formValues.anzahlBetreibungen,
          grundNichtBerechtigt: formValues.gruende.grundNichtBerechtigt,
          grundAusbildungZwoelfJahre:
            formValues.gruende.grundAusbildungZwoelfJahre,
          grundHoheGebuehren: formValues.gruende.grundHoheGebuehren,
          grundAnschaffungenFuerAusbildung:
            formValues.gruende.grundAnschaffungenFuerAusbildung,
          grundZweitausbildung: formValues.gruende.grundZweitausbildung,
        },
      },
    };
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);

    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();

    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormDarlehen.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: DARLEHEN,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormDarlehen.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: DARLEHEN,
        }),
      );
    }
  }
}
