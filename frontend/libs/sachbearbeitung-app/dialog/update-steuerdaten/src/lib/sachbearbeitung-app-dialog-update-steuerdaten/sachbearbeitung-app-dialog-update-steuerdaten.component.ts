import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type UpdateSteuerdatenDialogResult = {
  token: string;
};

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-update-steuerdaten',
  standalone: true,
  imports: [
    CommonModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslatePipe,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './sachbearbeitung-app-dialog-update-steuerdaten.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogUpdateSteuerdatenComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogRef =
    inject<
      MatDialogRef<
        SachbearbeitungAppDialogUpdateSteuerdatenComponent,
        UpdateSteuerdatenDialogResult
      >
    >(MatDialogRef);

  form = this.formBuilder.group({
    token: this.formBuilder.control(<string | undefined>undefined, [
      Validators.required,
    ]),
  });

  static open(dialog: MatDialog) {
    return dialog.open<
      SachbearbeitungAppDialogUpdateSteuerdatenComponent,
      undefined,
      UpdateSteuerdatenDialogResult
    >(SachbearbeitungAppDialogUpdateSteuerdatenComponent);
  }

  cancel() {
    this.dialogRef.close();
  }

  handleSave() {
    if (!this.form.valid) {
      return;
    }

    const { token } = convertTempFormToRealValues(this.form);

    this.dialogRef.close({
      token,
    });
  }
}
