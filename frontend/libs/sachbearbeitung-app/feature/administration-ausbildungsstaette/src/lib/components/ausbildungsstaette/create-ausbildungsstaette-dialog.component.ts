import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
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
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import {
  AbschlussSlim,
  AusbildungsstaetteCreate,
  AusbildungsstaetteSlim,
} from '@dv/shared/model/gesuch';
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
    nameDe: [<string | null>null, Validators.required],
    nameFr: [<string | null>null, Validators.required],
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
