import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { subYears } from 'date-fns';

import { selectLanguage } from '@dv/shared/data-access/language';
import {
  SharedDataAccessStammdatenApiEvents,
  selectLaender,
} from '@dv/shared/data-access/stammdaten';
import { Adresse, Anrede } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
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

export interface DelegierenDialogResult {
  anrede: Anrede;
  nachname: string;
  vorname: string;
  geburtsdatum: string;
  adresse: Adresse;
}

@Component({
  selector: 'dv-gesuch-app-feature-delegieren-dialog',
  imports: [
    CommonModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    ReactiveFormsModule,
    SharedUiFormAddressComponent,
  ],
  templateUrl: './gesuch-app-feature-delegieren-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureDelegierenDialogComponent implements OnInit {
  private dialogRef =
    inject<
      MatDialogRef<
        GesuchAppFeatureDelegierenDialogComponent,
        DelegierenDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private store = inject(Store);

  readonly anredeValues = Object.values(Anrede);

  languageSig = this.store.selectSignal(selectLanguage);
  laenderSig = this.store.selectSignal(selectLaender);

  static open(
    dialog: MatDialog,
  ): MatDialogRef<
    GesuchAppFeatureDelegierenDialogComponent,
    DelegierenDialogResult
  > {
    return dialog.open<
      GesuchAppFeatureDelegierenDialogComponent,
      DelegierenDialogResult
    >(GesuchAppFeatureDelegierenDialogComponent);
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
  });

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      this.languageSig(),
    );
  }

  ngOnInit() {
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
  }

  confirm() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      const values = this.form.getRawValue();
      const adresseValues = SharedUiFormAddressComponent.getRealValues(
        this.form.controls.adresse,
      );

      this.dialogRef.close({
        ...values,
        geburtsdatum: parseStringAndPrintForBackendLocalDate(
          values.geburtsdatum,
          this.languageSig(),
          subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
        )!,
        adresse: {
          ...adresseValues,
        },
      });
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
