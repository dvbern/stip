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
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
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
import { Anrede, FallWithDelegierung } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
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
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';

export interface DelegierungDialogData {
  fall: FallWithDelegierung;
}

@Component({
  selector: 'dv-sozialdienst-app-feature-delegierung-dialog',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    SharedUiFormReadonlyDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedUiFormAddressComponent,
  ],
  templateUrl: './sozialdienst-app-feature-delegierung-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DelegierungDialogComponent implements OnInit {
  private dialogRef = inject(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private store = inject(Store);
  dialogData = inject<DelegierungDialogData>(MAT_DIALOG_DATA);

  readonly anredeValues = Object.values(Anrede);

  languageSig = this.store.selectSignal(selectLanguage);
  laenderSig = this.store.selectSignal(selectLaender);

  static open(dialog: MatDialog, data: DelegierungDialogData) {
    return dialog.open<DelegierungDialogComponent, DelegierungDialogData>(
      DelegierungDialogComponent,
      {
        data,
        width: '700px',
      },
    );
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
    // todo: implement
  }

  cancel() {
    this.dialogRef.close();
  }
}
