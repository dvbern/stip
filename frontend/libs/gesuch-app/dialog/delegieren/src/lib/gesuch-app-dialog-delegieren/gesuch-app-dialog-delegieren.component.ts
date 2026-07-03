import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { subYears } from 'date-fns';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDialogNutzungsbedingungenComponent } from '@dv/shared/dialog/nutzungsbedingungen';
import {
  Anrede,
  DelegierungCreate,
  PATTERN_EMAIL,
  Sprache,
} from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  convertTempFormToRealValues,
  provideMaterialDefaultOptions,
} from '@dv/shared/util/form';
import {
  MAX_AGE_GESUCHSSTELLER,
  MEDIUM_AGE_GESUCHSSTELLER,
  MIN_AGE_GESUCHSSTELLER,
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onDateInputBlur,
  parseStringAndPrintForBackendLocalDate,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';

export type DelegierenDialogResult = DelegierungCreate;

@Component({
  selector: 'dv-gesuch-app-dialog-delegieren',
  imports: [
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MatCheckboxModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    ReactiveFormsModule,
    SharedUiFormAddressComponent,
  ],
  templateUrl: './gesuch-app-dialog-delegieren.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
})
export class GesuchAppDialogDelegierenComponent {
  private dialog = inject(MatDialog);
  private dialogRef =
    inject<
      MatDialogRef<GesuchAppDialogDelegierenComponent, DelegierenDialogResult>
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private store = inject(Store);

  readonly anredeValues = Object.values(Anrede);
  readonly spracheValues = Object.values(Sprache);

  languageSig = this.store.selectSignal(selectLanguage);

  static open(
    dialog: MatDialog,
  ): MatDialogRef<GesuchAppDialogDelegierenComponent, DelegierenDialogResult> {
    return dialog.open<
      GesuchAppDialogDelegierenComponent,
      DelegierenDialogResult
    >(GesuchAppDialogDelegierenComponent);
  }

  form = this.formBuilder.group({
    anrede: this.formBuilder.control<Anrede>('' as Anrede, {
      validators: Validators.required,
    }),
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
          subYears(new Date(), MAX_AGE_GESUCHSSTELLER),
          'date',
        ),

        maxDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MIN_AGE_GESUCHSSTELLER),
          'date',
        ),
      ],
    ],
    email: ['', [Validators.required, Validators.pattern(PATTERN_EMAIL)]],
    sprache: [<Sprache | undefined>undefined, [Validators.required]],
    nutzungsbedingungenAkzeptiert: [false, [Validators.requiredTrue]],
  });

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      this.languageSig(),
    );
  }

  confirm() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      const values = convertTempFormToRealValues(this.form);
      const adresseValues = SharedUiFormAddressComponent.getRealValues(
        this.form.controls.adresse,
      );
      const geburtsdatum = parseStringAndPrintForBackendLocalDate(
        values.geburtsdatum,
        this.languageSig(),
        subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      );

      if (!geburtsdatum) {
        return;
      }

      this.dialogRef.close({
        ...values,
        geburtsdatum,
        adresse: {
          ...adresseValues,
        },
      });
    }
  }

  showNutzungsbedingungen() {
    SharedDialogNutzungsbedingungenComponent.open(this.dialog, true)
      .afterClosed()
      .subscribe();
  }

  cancel() {
    this.dialogRef.close();
  }
}
