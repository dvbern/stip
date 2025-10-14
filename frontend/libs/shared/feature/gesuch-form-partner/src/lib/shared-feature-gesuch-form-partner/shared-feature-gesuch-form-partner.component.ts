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
import { toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { subYears } from 'date-fns';
import { Subject } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormPartner } from '@dv/shared/event/gesuch-form-partner';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  AusbildungsPensum,
  MASK_SOZIALVERSICHERUNGSNUMMER,
  PartnerUpdate,
} from '@dv/shared/model/gesuch';
import { PARTNER, isStepDisabled } from '@dv/shared/model/gesuch-form';
import { preparePermissions } from '@dv/shared/model/permission-state';
import { MAX_EINKOMMEN } from '@dv/shared/model/ui-constants';
import { SharedPatternDocumentUploadComponent } from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import { SharedUtilFormService } from '@dv/shared/util/form';
import { maskitoMaxNumber, maskitoNumber } from '@dv/shared/util/maskito-util';
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
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslocoPipe,
    SharedUiFormFieldDirective,
    SharedUiFormAddressComponent,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MaskitoDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
    MatCheckboxModule,
    SharedUiLoadingComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiInfoDialogDirective,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-partner.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormPartnerComponent implements OnInit {
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private permissionStore = inject(PermissionStore);
  private einreichenStore = inject(EinreichenStore);
  private appType = inject(SharedModelCompileTimeConfig).appType;
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);

  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;
  readonly maskitoNumber = maskitoNumber;
  readonly maskitoMaxNumber = maskitoMaxNumber(MAX_EINKOMMEN);
  readonly ausbildungspensumValues = Object.values(AusbildungsPensum);

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormPartnerView);
  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);
  gotReenabled$ = new Subject<object>();

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
    inAusbildung: [<boolean | undefined>undefined],
    ausbildungspensum: [<AusbildungsPensum | undefined>undefined],
  });

  inAusbildungChangedSig = toSignal(
    this.form.controls.inAusbildung.valueChanges,
  );
  showAusbildungspensumSig = computed(() => {
    return this.inAusbildungChangedSig() === true;
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    effect(() => {
      const { gesuchFormular } = this.viewSig();
      const svValidators = [
        Validators.required,
        sharedUtilValidatorAhv('partner', gesuchFormular),
      ];
      this.form.controls.sozialversicherungsnummer.clearValidators();
      this.form.controls.sozialversicherungsnummer.addValidators(svValidators);

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
        });
        SharedUiFormAddressComponent.patchForm(
          this.form.controls.adresse,
          partnerForForm.adresse,
        );
      }
    });
    effect(() => {
      const { gesuch, gesuchFormular } = this.viewSig();
      const rolesMap = this.permissionStore.rolesMapSig();
      const { trancheTyp } = this.cacheSig();
      const { permissions } = preparePermissions(
        trancheTyp,
        gesuch,
        this.appType,
        rolesMap,
      );
      if (
        gesuch &&
        gesuchFormular &&
        isStepDisabled(PARTNER, gesuch, permissions)
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
    });
  }

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormPartner.init());
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
