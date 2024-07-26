import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  computed,
  effect,
  inject,
} from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoDirective } from '@maskito/angular';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { subYears } from 'date-fns';
import { Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormPartner } from '@dv/shared/event/gesuch-form-partner';
import {
  DokumentTyp,
  Land,
  MASK_SOZIALVERSICHERUNGSNUMMER,
  PartnerUpdate,
} from '@dv/shared/model/gesuch';
import { PARTNER, isStepDisabled } from '@dv/shared/model/gesuch-form';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiFormCountryComponent } from '@dv/shared/ui/form-country';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUtilCountriesService } from '@dv/shared/util/countries';
import { SharedUtilFormService } from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';
import { sharedUtilValidatorAhv } from '@dv/shared/util/validator-ahv';
import {
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
  parseStringAndPrintForBackendLocalDate,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormPartnerView } from './shared-feature-gesuch-form-partner.selector';

const MAX_AGE_ADULT = 130;
const MIN_AGE_ADULT = 10;
const MEDIUM_AGE_ADULT = 30;

@Component({
  selector: 'dv-shared-feature-gesuch-form-partner',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    SharedUiFormFieldDirective,
    SharedUiFormCountryComponent,
    SharedUiFormAddressComponent,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MaskitoDirective,
    NgbInputDatepicker,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
    MatCheckboxModule,
    SharedUiLoadingComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiFormReadonlyDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-partner.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormPartnerComponent implements OnInit {
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private countriesService = inject(SharedUtilCountriesService);

  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;
  readonly maskitoNumber = maskitoNumber;

  readonly Land = Land;

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormPartnerView);
  gotReenabled$ = new Subject<object>();
  laenderSig = computed(() => this.viewSig().laender);
  translatedLaender$ = toObservable(this.laenderSig).pipe(
    switchMap((laender) => this.countriesService.getCountryList(laender)),
  );

  form = this.formBuilder.group({
    sozialversicherungsnummer: ['', []],
    nachname: ['', [Validators.required]],
    vorname: ['', [Validators.required]],
    adresse: SharedUiFormAddressComponent.buildAddressFormGroup(
      this.formBuilder,
    ),
    geburtsdatum: [
      '',
      [
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'date'),
        minDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MAX_AGE_ADULT),
          'date',
        ),
        maxDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MIN_AGE_ADULT),
          'date',
        ),
      ],
    ],
    ausbildungMitEinkommenOderErwerbstaetig: [false, [Validators.required]],
    jahreseinkommen: [<string | undefined>undefined, [Validators.required]],
    fahrkosten: [<string | undefined>undefined, [Validators.required]],
    verpflegungskosten: [<string | undefined>undefined, [Validators.required]],
  });

  ausbildungMitEinkommenOderErwerbstaetigSig = toSignal(
    this.form.controls.ausbildungMitEinkommenOderErwerbstaetig.valueChanges,
  );

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  jahreseinkommenSig = toSignal(
    this.form.controls.jahreseinkommen.valueChanges,
  );

  fahrkostenSig = toSignal(this.form.controls.fahrkosten.valueChanges);

  jahreseinkommenDocumentSig = this.createUploadOptionsSig(() => {
    const jahreseinkommen = fromFormatedNumber(
      this.jahreseinkommenSig() ?? '0',
    );

    return jahreseinkommen > 0
      ? DokumentTyp.PARTNER_AUSBILDUNG_LOHNABRECHNUNG
      : null;
  });

  fahrkostenDocumentSig = this.createUploadOptionsSig(() => {
    const fahrkosten = fromFormatedNumber(this.fahrkostenSig() ?? '0');

    return fahrkosten > 0 ? DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT : null;
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    effect(
      () => {
        const { gesuchFormular } = this.viewSig();
        const svValidators = [
          Validators.required,
          sharedUtilValidatorAhv('partner', gesuchFormular),
        ];
        this.form.controls.sozialversicherungsnummer.clearValidators();
        this.form.controls.sozialversicherungsnummer.addValidators(
          svValidators,
        );

        if (gesuchFormular?.partner) {
          const partner = gesuchFormular.partner;
          const partnerForForm = {
            ...partner,
            geburtsdatum: partner.geburtsdatum.toString(),
          };
          this.form.patchValue({
            ...partnerForForm,
            geburtsdatum: parseBackendLocalDateAndPrint(
              partner.geburtsdatum,
              this.languageSig(),
            ),
            jahreseinkommen: partnerForForm.jahreseinkommen?.toString(),
            fahrkosten: partnerForForm.fahrkosten?.toString(),
            verpflegungskosten: partnerForForm.verpflegungskosten?.toString(),
          });
          SharedUiFormAddressComponent.patchForm(
            this.form.controls.adresse,
            partnerForForm.adresse,
          );
        }
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        const { gesuch, gesuchFormular } = this.viewSig();
        if (
          gesuch &&
          gesuchFormular &&
          isStepDisabled(PARTNER, gesuchFormular)
        ) {
          this.store.dispatch(
            SharedEventGesuchFormPartner.nextStepTriggered({
              gesuchId: gesuch.id,
              trancheId: gesuch.gesuchTrancheToWorkWith.id,
              gesuchFormular,
              origin: PARTNER,
            }),
          );
        }
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        this.gotReenabledSig();
        const noAusbildungMitEinkommenOderErwerbstaetigkeit =
          !this.ausbildungMitEinkommenOderErwerbstaetigSig();
        if (this.viewSig().readonly) {
          Object.values(this.form.controls).forEach((control) =>
            control.disable(),
          );
        } else {
          this.formUtils.setDisabledState(
            this.form.controls.jahreseinkommen,
            noAusbildungMitEinkommenOderErwerbstaetigkeit,
            true,
          );
          this.formUtils.setDisabledState(
            this.form.controls.fahrkosten,
            noAusbildungMitEinkommenOderErwerbstaetigkeit,
            true,
          );
          this.formUtils.setDisabledState(
            this.form.controls.verpflegungskosten,
            noAusbildungMitEinkommenOderErwerbstaetigkeit,
            true,
          );
        }
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormPartner.init());
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
  }

  handleSaveAndContinue() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();
    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormPartner.nextStepTriggered({
          origin: PARTNER,
          gesuchId,
          trancheId,
          gesuchFormular,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormPartner.nextTriggered({
          id: gesuch.id,
          origin: PARTNER,
        }),
      );
    }
  }

  handleSaveAndBack() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();
    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormPartner.prevStepTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: PARTNER,
        }),
      );
      this.form.markAsPristine();
    }
  }

  trackByIndex(index: number) {
    return index;
  }

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_ADULT),
      this.languageSig(),
    );
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = this.form.getRawValue();
    const partner: PartnerUpdate = {
      ...gesuchFormular?.partner,
      ...formValues,
      adresse: {
        id: gesuchFormular?.partner?.adresse?.id,
        ...SharedUiFormAddressComponent.getRealValues(
          this.form.controls.adresse,
        ),
      },
      // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
      geburtsdatum: parseStringAndPrintForBackendLocalDate(
        formValues.geburtsdatum,
        this.languageSig(),
        subYears(new Date(), MEDIUM_AGE_ADULT),
      )!,
      jahreseinkommen: fromFormatedNumber(formValues.jahreseinkommen),
      fahrkosten: fromFormatedNumber(formValues.fahrkosten),
      verpflegungskosten: fromFormatedNumber(formValues.verpflegungskosten),
    };
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        partner,
      },
    };
  }
}
