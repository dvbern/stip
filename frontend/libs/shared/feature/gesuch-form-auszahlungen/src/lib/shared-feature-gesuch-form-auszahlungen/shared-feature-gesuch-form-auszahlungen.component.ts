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
  AbstractControl,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoModule } from '@maskito/angular';
import { NgbAlert } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { extractIBAN } from 'ibantools';
import { distinctUntilChanged, shareReplay } from 'rxjs';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import {
  ElternUpdate,
  Kontoinhaber,
  MASK_IBAN,
  PersonInAusbildungUpdate,
  SharedModelGesuchFormular,
} from '@dv/shared/model/gesuch';
import { AUSZAHLUNGEN } from '@dv/shared/model/gesuch-form';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { calculateElternSituationGesuch } from '@dv/shared/util-fn/gesuch-util';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { selectSharedFeatureGesuchFormAuszahlungenView } from './shared-feature-gesuch-form-auszahlungen.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-auszahlungen',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiProgressBarComponent,
    TranslateModule,
    MaskitoModule,
    SharedUiFormAddressComponent,
    NgbAlert,
    GesuchAppUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-auszahlungen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormAuszahlungenComponent implements OnInit {
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private fb = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);

  MASK_IBAN = MASK_IBAN;
  step = AUSZAHLUNGEN;

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
    return this.view().laender;
  });
  languageSig = this.store.selectSignal(selectLanguage);

  view = this.store.selectSignal(selectSharedFeatureGesuchFormAuszahlungenView);

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    const kontoinhaberinChangesSig = toSignal(
      this.form.controls.kontoinhaber.valueChanges.pipe(
        distinctUntilChanged(),
        shareReplay({ bufferSize: 1, refCount: true }),
      ),
    );

    effect(
      () => {
        const { gesuchFormular } = this.view();
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
          }
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const kontoinhaberin = kontoinhaberinChangesSig();
        if (kontoinhaberin === undefined) {
          return;
        }
        const { gesuchFormular } = this.view();
        this.form.reset({
          kontoinhaber: kontoinhaberin,
        });
        this.handleKontoinhaberinChanged(kontoinhaberin, gesuchFormular);
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const { readonly } = this.view();
        if (readonly) {
          Object.values(this.form.controls).forEach((control) =>
            control.disable(),
          );
        }
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
          origin: AUSZAHLUNGEN,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.view();
    if (gesuch?.id && gesuch?.gesuchTrancheToWorkWith.id) {
      this.store.dispatch(
        SharedEventGesuchFormAuszahlung.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: AUSZAHLUNGEN,
        }),
      );
    }
  }

  trackByIndex(index: number) {
    return index;
  }

  private handleKontoinhaberinChanged(
    kontoinhaberin: Kontoinhaber,
    gesuchFormular: SharedModelGesuchFormular | null,
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
        this.enableNameAndAdresse();
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
    const { gesuch, gesuchFormular } = this.view();
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

export function ibanValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (control.value === null || control.value === '') {
      return null;
    }
    const extractIBANResult = extractIBAN('CH' + control.value);
    if (!extractIBANResult.valid || extractIBANResult.countryCode !== 'CH') {
      return { invalidIBAN: true };
    }
    // Check IBAN is not a QR-IBAN, a QR based one, has in the first 5 digits a value between 30000 and 31999
    if (
      +extractIBANResult.iban.substring(4, 9) >= 30000 &&
      +extractIBANResult.iban.substring(4, 9) <= 31999
    ) {
      return { qrIBAN: true };
    }
    return null;
  };
}
