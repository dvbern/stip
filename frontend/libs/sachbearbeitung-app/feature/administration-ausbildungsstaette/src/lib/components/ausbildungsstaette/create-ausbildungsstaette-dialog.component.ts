import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
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
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import {
  AbschlussSlim,
  AusbildungsstaetteCreate,
  AusbildungsstaetteSlim,
} from '@dv/shared/model/gesuch';
import { KnownLanguage } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';

type CreateAbschlussData = {
  ausbildungsstaetten: AusbildungsstaetteSlim[];
  abschluesse: AbschlussSlim[];
};

type SchulType = 'CT' | 'BUR';

@Component({
  selector: 'dv-create-ausbildungsstaette-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './create-ausbildungsstaette-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateAusbildungsstaetteDialogComponent {
  private dialogRef =
    inject<MatDialogRef<CreateAbschlussData, AusbildungsstaetteCreate>>(
      MatDialogRef,
    );
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  translate = inject(TranslateService);

  dialogData = inject<CreateAbschlussData>(MAT_DIALOG_DATA);
  form = this.formBuilder.group({
    schule: [<SchulType | undefined>undefined, Validators.required],
    nameDe: [
      <string | null>null,
      [
        Validators.required,
        uniqueAusbildungsstaetteValidator(
          this.dialogData.ausbildungsstaetten,
          'nameDe',
        ),
      ],
    ],
    nameFr: [
      <string | null>null,
      [
        Validators.required,
        uniqueAusbildungsstaetteValidator(
          this.dialogData.ausbildungsstaetten,
          'nameFr',
        ),
      ],
    ],
    burNo: [<string | undefined>undefined],
  });
  schulen = [
    {
      value: 'CT',
      label:
        'sachbearbeitung-app.feature.administration.ausbildungsstaette.schule.CT',
    },
    {
      value: 'BUR',
      label:
        'sachbearbeitung-app.feature.administration.ausbildungsstaette.schule.BUR',
    },
  ] satisfies { value: SchulType; label: SachbearbeitungAppTranslationKey }[];

  private schuleChangedSig = toSignal(this.form.controls.schule.valueChanges);

  static open(dialog: MatDialog, data: CreateAbschlussData) {
    return dialog.open<
      CreateAusbildungsstaetteDialogComponent,
      CreateAbschlussData,
      AusbildungsstaetteCreate
    >(CreateAusbildungsstaetteDialogComponent, { data });
  }

  constructor() {
    effect(() => {
      const schule = this.schuleChangedSig();

      this.formUtils.setRequired(this.form.controls.burNo, schule === 'BUR');
    });
  }

  confirm() {
    if (this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form, ['nameDe', 'nameFr']);
    this.dialogRef.close({
      nameDe: values.nameDe,
      nameFr: values.nameFr,
      burNo: values.burNo,
    });
  }

  cancel() {
    this.dialogRef.close();
  }
}

const uniqueAusbildungsstaetteValidator = (
  ausbildungsstaetten: AusbildungsstaetteSlim[],
  property: `name${Capitalize<KnownLanguage>}`,
): ValidatorFn => {
  return (control: AbstractControl<string | null>) => {
    const currentValue = control.value;

    if (!currentValue) {
      return null;
    }

    const duplicateExists = ausbildungsstaetten.some(
      (entry) => entry[property] === currentValue,
    );

    return duplicateExists ? { notUniqueAusbildungsstaette: true } : null;
  };
};
