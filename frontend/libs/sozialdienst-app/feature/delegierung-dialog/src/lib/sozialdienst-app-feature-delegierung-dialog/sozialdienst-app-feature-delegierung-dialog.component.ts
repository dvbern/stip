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
import { selectLaender } from '@dv/shared/data-access/stammdaten';
import { Anrede, FallWithDelegierung } from '@dv/shared/model/gesuch';
import { compareById } from '@dv/shared/model/type-util';
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
  parseBackendLocalDateAndPrint,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';
import { DelegationStore } from '@dv/sozialdienst-app/data-access/delegation';

export interface DelegierungDialogData {
  fall: FallWithDelegierung;
}

export interface DelegierungDialogResult {
  sozialdienstMitarbeiterId: string;
  fallId: string;
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
  delegationStore = inject(DelegationStore);
  dialogData = inject<DelegierungDialogData>(MAT_DIALOG_DATA);

  readonly anredeValues = Object.values(Anrede);

  compareById = compareById;

  languageSig = this.store.selectSignal(selectLanguage);
  laenderSig = this.store.selectSignal(selectLaender);

  static open(dialog: MatDialog, data: DelegierungDialogData) {
    return dialog.open<
      DelegierungDialogComponent,
      DelegierungDialogData,
      DelegierungDialogResult
    >(DelegierungDialogComponent, {
      data,
    });
  }

  form = this.formBuilder.group({
    fallNummer: ['', [Validators.required]],
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

  zuweisungSozMitarbeiterForm = this.formBuilder.group({
    sozMitarbeiter: [<string | undefined>undefined, [Validators.required]],
  });

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      this.languageSig(),
    );
  }

  ngOnInit() {
    this.delegationStore.loadSozialdienstBenutzerList$();
    this.form.patchValue({
      fallNummer: this.dialogData.fall.fallNummer,
      anrede: this.dialogData.fall.delegierung.persoenlicheAngaben?.anrede,
      nachname: this.dialogData.fall.delegierung.persoenlicheAngaben?.nachname,
      vorname: this.dialogData.fall.delegierung.persoenlicheAngaben?.vorname,
      geburtsdatum: parseBackendLocalDateAndPrint(
        this.dialogData.fall.delegierung.persoenlicheAngaben?.geburtsdatum,
        this.languageSig(),
      ),
    });
    if (this.dialogData.fall.delegierung.persoenlicheAngaben?.adresse) {
      SharedUiFormAddressComponent.patchForm(
        this.form.controls.adresse,
        this.dialogData.fall.delegierung.persoenlicheAngaben.adresse,
      );
    }
  }

  changeSozMitarbeiter() {
    const mitarbeiterId =
      this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.value;

    if (mitarbeiterId) {
      // this.delegationStore
      //   .delegierterMitarbeiterAendern$({
      //     delegierterMitarbeiterAendern: {
      //       mitarbeiterId,
      //     },
      //     delegierungId: this.dialogData.fall.delegierung.id,
      //   })
      //   .subscribe((result) => {
      //     // if (result) {
      //     //   this.dialogRef.close({
      //     //     sozialdienstMitarbeiterId: sozMitarbeiterId,
      //     //     fallId: this.dialogData.fall.id,
      //     //   });
      //     // }
      //   });
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
