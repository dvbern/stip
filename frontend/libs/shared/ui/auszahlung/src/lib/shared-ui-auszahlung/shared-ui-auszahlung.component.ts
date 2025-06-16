import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  effect,
  inject,
  input,
  output,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedModelAuszahlung } from '@dv/shared/model/auszahlung';
import { AuszahlungUpdate, MASK_IBAN } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { ibanValidator } from '@dv/shared/util/validator-iban';

@Component({
  selector: 'dv-shared-ui-auszahlung',
  imports: [
    FormsModule,
    RouterLink,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    TranslatePipe,
    MaskitoDirective,
    SharedUiFormAddressComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedUiFormSaveComponent,
    SharedUiFormMessageErrorDirective,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
    SharedUiInfoDialogDirective,
  ],
  templateUrl: './shared-ui-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiAuszahlungComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);

  auszahlungViewSig = input.required<SharedModelAuszahlung>();
  saveTriggered = output<AuszahlungUpdate>();
  continueTriggered = output<void>();
  formIsUnsaved = output<boolean>();

  MASK_IBAN = MASK_IBAN;

  form = this.formBuilder.group({
    auszahlungAnSozialdienst: [false],
    zahlungsverbindung: this.formBuilder.group(
      {
        nachname: [<string | undefined>undefined, [Validators.required]],
        vorname: [<string | undefined>undefined, [Validators.required]],
        adresse: SharedUiFormAddressComponent.buildAddressFormGroup(
          this.formBuilder,
          Validators.required,
        ),
        iban: [
          <string | undefined>undefined,
          [Validators.required, ibanValidator()],
        ],
      },
      { validators: [Validators.required] },
    ),
  });
  auszahlungAnSozialdienstChangedSig = toSignal(
    this.form.controls.auszahlungAnSozialdienst.valueChanges,
  );

  private invalidFormularControlsSig = computed(() => {
    return this.auszahlungViewSig().invalidFormularControls;
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.invalidFormularControlsSig,
      this.form,
    );

    effect(() => {
      const auszahlung = this.auszahlungViewSig().auszahlung?.value;
      if (isDefined(auszahlung)) {
        this.form.patchValue(
          {
            ...auszahlung,
            zahlungsverbindung: {
              ...auszahlung.zahlungsverbindung,
              iban: auszahlung.zahlungsverbindung?.iban?.substring(2), // Land-Prefix loeschen
            },
          },
          { emitEvent: false },
        );

        if (auszahlung.zahlungsverbindung) {
          SharedUiFormAddressComponent.patchForm(
            this.form.controls.zahlungsverbindung.controls.adresse,
            auszahlung.zahlungsverbindung.adresse,
          );
        }
      }
    });

    effect(() => {
      const auszahlungAnSozialdienst =
        this.auszahlungAnSozialdienstChangedSig();
      this.formUtils.setDisabledState(
        this.form.controls.zahlungsverbindung,
        !!auszahlungAnSozialdienst,
        true,
      );
    });
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { auszahlung } = this.buildUpdatedGesuchFromForm();
    if (this.form.valid) {
      this.saveTriggered.emit(auszahlung);
      this.form.markAsPristine();
    }
  }

  private buildUpdatedGesuchFromForm() {
    const auszahlung = this.auszahlungViewSig().auszahlung?.value;
    const formData = this.form.getRawValue();
    const zahlungsverbindungData = convertTempFormToRealValues(
      this.form.controls.zahlungsverbindung,
      ['nachname', 'vorname', 'iban', 'adresse'],
    );
    const addressData = SharedUiFormAddressComponent.getRealValues(
      this.form.controls.zahlungsverbindung.controls.adresse,
    );
    return {
      auszahlung: {
        ...auszahlung,
        ...formData,
        isDelegated: undefined,
        zahlungsverbindung: !formData.auszahlungAnSozialdienst
          ? {
              ...auszahlung?.zahlungsverbindung,
              ...zahlungsverbindungData,
              adresse: {
                ...addressData,
              },
              iban: 'CH' + zahlungsverbindungData.iban,
            }
          : undefined,
      },
    };
  }
}
