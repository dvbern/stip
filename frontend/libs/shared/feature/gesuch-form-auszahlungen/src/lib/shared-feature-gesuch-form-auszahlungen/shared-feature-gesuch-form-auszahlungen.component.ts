import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  computed,
  effect,
  inject,
  untracked,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { Subject, distinctUntilChanged, shareReplay } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import {
  DokumentTyp,
  ElternUpdate,
  GesuchFormularType,
  Kontoinhaber,
  MASK_IBAN,
  PersonInAusbildungUpdate,
} from '@dv/shared/model/gesuch';
import { AUSZAHLUNG } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
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
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { ibanValidator } from '@dv/shared/util/validator-iban';
import { calculateElternSituationGesuch } from '@dv/shared/util-fn/gesuch-util';

import { selectSharedFeatureGesuchFormAuszahlungenView } from './shared-feature-gesuch-form-auszahlungen.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-auszahlungen',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiProgressBarComponent,
    TranslatePipe,
    MaskitoDirective,
    SharedUiInfoContainerComponent,
    SharedUiFormAddressComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
    SharedUiInfoDialogDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-auszahlungen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormAuszahlungenComponent implements OnInit {
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private fb = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);

  MASK_IBAN = MASK_IBAN;
  step = AUSZAHLUNG;

  form = this.fb.group({
    adresseId: [<string | undefined>undefined, []],
    kontoinhaber: [
      <Kontoinhaber | undefined>undefined,
      {
        validators: Validators.required,
      },
    ],
    nachname: [<string | undefined>undefined, [Validators.required]],
    vorname: [<string | undefined>undefined, [Validators.required]],
    adresse: SharedUiFormAddressComponent.buildAddressFormGroup(this.fb),
    iban: [
      <string | undefined>undefined,
      [Validators.required, ibanValidator()],
    ],
  });

  laenderSig = computed(() => {
    return this.viewSig().laender;
  });
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormAuszahlungenView,
  );
  gotReenabled$ = new Subject<object>();

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  kontoinhaberChangesSig = toSignal(
    this.form.controls.kontoinhaber.valueChanges,
  );

  abtretungserklaerungDocumentSig = this.createUploadOptionsSig(() => {
    const kontoinhaberChanged = this.kontoinhaberChangesSig();
    const { gesuchFormular } = this.viewSig();
    const kontoinhaber =
      kontoinhaberChanged ?? gesuchFormular?.auszahlung?.kontoinhaber;

    if (
      kontoinhaber === Kontoinhaber.SOZIALDIENST_INSTITUTION ||
      kontoinhaber === Kontoinhaber.ANDERE
    ) {
      return DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG;
    }

    return null;
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    const kontoinhaberinChangesSig = toSignal(
      this.form.controls.kontoinhaber.valueChanges.pipe(
        distinctUntilChanged(),
        shareReplay({ bufferSize: 1, refCount: true }),
      ),
    );

    effect(
      () => {
        const { gesuchFormular } = this.viewSig();
        if (isDefined(gesuchFormular)) {
          const initalValue = gesuchFormular.auszahlung;
          this.form.patchValue(
            {
              ...initalValue,
              iban: initalValue?.iban?.substring(2), // Land-Prefix loeschen
            },
            { emitEvent: false },
          );
          if (initalValue) {
            this.handleKontoinhaberinChanged(
              initalValue?.kontoinhaber,
              gesuchFormular,
            );
            SharedUiFormAddressComponent.patchForm(
              this.form.controls.adresse,
              initalValue.adresse,
            );
          }
        }
      },
      { allowSignalWrites: true },
    );

    effect(() => {
      this.gotReenabledSig();
      const { gesuchFormular } = untracked(this.viewSig);
      const kontoinhaberin = this.form.controls.kontoinhaber.value;
      if (kontoinhaberin) {
        this.handleKontoinhaberinChanged(kontoinhaberin, gesuchFormular);
      }
    });

    effect(
      () => {
        const kontoinhaberin = kontoinhaberinChangesSig();
        if (kontoinhaberin === undefined) {
          return;
        }
        const { gesuchFormular } = this.viewSig();
        this.form.reset({
          kontoinhaber: kontoinhaberin,
        });
        this.handleKontoinhaberinChanged(kontoinhaberin, gesuchFormular);
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAuszahlung.init());
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();
    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormAuszahlung.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: AUSZAHLUNG,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id && gesuch?.gesuchTrancheToWorkWith.id) {
      this.store.dispatch(
        SharedEventGesuchFormAuszahlung.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: AUSZAHLUNG,
        }),
      );
    }
  }

  trackByIndex(index: number) {
    return index;
  }

  private handleKontoinhaberinChanged(
    kontoinhaberin: Kontoinhaber,
    gesuchFormular: GesuchFormularType | null,
  ): void {
    switch (kontoinhaberin) {
      case Kontoinhaber.GESUCHSTELLER:
        this.setValuesFrom(gesuchFormular?.personInAusbildung);
        this.disableNameAndAdresse();
        break;
      case Kontoinhaber.VATER: {
        this.setValuesFrom(
          calculateElternSituationGesuch(gesuchFormular).vater,
        );
        this.disableNameAndAdresse();
        break;
      }
      case Kontoinhaber.MUTTER:
        this.setValuesFrom(
          calculateElternSituationGesuch(gesuchFormular).mutter,
        );
        this.disableNameAndAdresse();
        break;
      case Kontoinhaber.ANDERE:
      case Kontoinhaber.SOZIALDIENST_INSTITUTION:
      default:
        if (!this.viewSig().readonly) {
          this.enableNameAndAdresse();
        }
        break;
    }
  }

  private setValuesFrom(
    valuesFrom: PersonInAusbildungUpdate | ElternUpdate | undefined,
  ): void {
    if (valuesFrom) {
      this.form.patchValue(
        {
          ...valuesFrom,
          adresseId: valuesFrom.adresse?.id,
        },
        { emitEvent: false },
      );
      SharedUiFormAddressComponent.patchForm(
        this.form.controls.adresse,
        valuesFrom.adresse,
      );
    } else {
      this.form.reset(undefined, { emitEvent: false });
    }
  }

  private disableNameAndAdresse(): void {
    this.form.controls.nachname.disable();
    this.form.controls.vorname.disable();
    this.form.controls.adresse.disable();
  }

  handleKontoinhaberinChangedByUser(): void {
    this.form.controls.nachname.reset('');
    this.form.controls.vorname.reset('');
    this.form.controls.adresse.reset({});
    this.form.controls.iban.reset('');
  }

  private enableNameAndAdresse(): void {
    this.form.controls.nachname.enable();
    this.form.controls.vorname.enable();
    this.form.controls.adresse.enable();
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const auszahlung = gesuchFormular?.auszahlung;
    const formularData = convertTempFormToRealValues(this.form, [
      'iban',
      'kontoinhaber',
      'nachname',
      'vorname',
    ]);
    const addressData = SharedUiFormAddressComponent.getRealValues(
      this.form.controls['adresse'],
    );
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        auszahlung: {
          ...auszahlung,
          ...formularData,
          adresseId: undefined,
          adresse: {
            ...addressData,
            id: formularData.adresseId,
          },
          /** Or only send id?
           * @example
          adresse: (formularData.adresseId
            ? {
                id: formularData.adresseId,
              }
            : addressData) as any,
           */
          iban: 'CH' + this.form.getRawValue().iban,
        },
      },
    };
  }
}
