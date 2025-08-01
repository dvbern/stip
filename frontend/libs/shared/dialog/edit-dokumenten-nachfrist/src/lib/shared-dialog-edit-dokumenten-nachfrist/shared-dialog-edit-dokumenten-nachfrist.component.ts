import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';
import { startOfDay } from 'date-fns';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { normalizeDateForUTC } from '@dv/shared/util/validator-date';

type NachfristData = {
  nachfrist: string;
};
type EditNachfristResult = {
  newNachfrist: string;
};

@Component({
  selector: 'dv-shared-dialog-edit-dokumenten-nachfrist',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    SharedUiMaxLengthDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  providers: [provideDvDateAdapter()],
  templateUrl: './shared-dialog-edit-dokumenten-nachfrist.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogEditDokumentenNachfristComponent {
  private dialogRef = inject(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogData = inject<NachfristData>(MAT_DIALOG_DATA);

  minDate = startOfDay(new Date());
  form = this.formBuilder.group({
    newNachfrist: [this.dialogData.nachfrist, [Validators.required]],
  });

  static open(dialog: MatDialog, nachfrist: string) {
    return dialog
      .open<
        SharedDialogEditDokumentenNachfristComponent,
        NachfristData,
        EditNachfristResult
      >(SharedDialogEditDokumentenNachfristComponent, { data: { nachfrist } })
      .afterClosed();
  }

  cancel() {
    this.dialogRef.close();
  }

  handleSave() {
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    const formValues = convertTempFormToRealValues(this.form);
    this.dialogRef.close({
      newNachfrist: normalizeDateForUTC(formValues.newNachfrist),
    });
  }
}
