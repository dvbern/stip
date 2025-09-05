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
import { TranslocoPipe, TranslocoService } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { MaskitoOptions } from '@maskito/core';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import {
  AbschlussSlim,
  AusbildungsstaetteCreate,
  AusbildungsstaetteNummerTyp,
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

@Component({
  selector: 'dv-create-ausbildungsstaette-dialog',
  imports: [
    CommonModule,
    MaskitoDirective,
    ReactiveFormsModule,
    TranslocoPipe,
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
  translate = inject(TranslocoService);

  dialogData = inject<CreateAbschlussData>(MAT_DIALOG_DATA);
  alphanumericMask: MaskitoOptions = {
    mask: /^[a-zA-Z\d]+$/,
  };
  form = this.formBuilder.group({
    schuleNummerTyp: [
      <AusbildungsstaetteNummerTyp | undefined>undefined,
      Validators.required,
    ],
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
    nummer: [<string | undefined>undefined],
  });

  schuleNummerTypChangedSig = toSignal(
    this.form.controls.schuleNummerTyp.valueChanges,
  );

  schuleNummerTypes = [
    {
      value: 'CH_SHIS',
      label:
        'sachbearbeitung-app.feature.administration.ausbildungsstaette.schule.CH_SHIS',
    },
    {
      value: 'BUR_NO',
      label:
        'sachbearbeitung-app.feature.administration.ausbildungsstaette.schule.BUR_NO',
    },
    {
      value: 'CT_NO',
      label:
        'sachbearbeitung-app.feature.administration.ausbildungsstaette.schule.CT_NO',
    },
    {
      value: 'OHNE_NO',
      label:
        'sachbearbeitung-app.feature.administration.ausbildungsstaette.schule.OHNE_NO',
    },
  ] satisfies {
    value: AusbildungsstaetteNummerTyp;
    label: SachbearbeitungAppTranslationKey;
  }[];

  static open(dialog: MatDialog, data: CreateAbschlussData) {
    return dialog.open<
      CreateAusbildungsstaetteDialogComponent,
      CreateAbschlussData,
      AusbildungsstaetteCreate
    >(CreateAusbildungsstaetteDialogComponent, { data });
  }

  constructor() {
    effect(() => {
      const schuleNummerTyp = this.schuleNummerTypChangedSig();

      if (schuleNummerTyp === 'OHNE_NO') {
        this.form.controls.nummer.setValue(undefined);
        this.form.controls.nummer.clearValidators();
      } else {
        this.form.controls.nummer.setValidators([Validators.required]);
      }
      this.form.controls.nummer.updateValueAndValidity();
    });
  }

  confirm() {
    if (this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form, [
      'nameDe',
      'nameFr',
      'schuleNummerTyp',
    ]);
    this.dialogRef.close({
      nameDe: values.nameDe,
      nameFr: values.nameFr,
      nummerTyp: values.schuleNummerTyp,
      nummer: values.nummer,
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
      (entry) => entry.aktiv && entry[property] === currentValue,
    );

    return duplicateExists ? { notUniqueAusbildungsstaette: true } : null;
  };
};
