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

import { PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export interface ReplaceSozialdienstAdminDialogResult {
  nachname: string;
  vorname: string;
  email: string;
}

@Component({
    imports: [
        CommonModule,
        TranslatePipe,
        MatFormFieldModule,
        MatInputModule,
        SharedUiFormFieldDirective,
        SharedUiFormMessageErrorDirective,
        SharedUiMaxLengthDirective,
        ReactiveFormsModule,
    ],
    templateUrl: './replace-sozialdienst-admin-dialog.component.html',
    styleUrls: ['./replace-sozialdienst-admin-dialog.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReplaceSozialdienstAdminDialogComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        ReplaceSozialdienstAdminDialogComponent,
        ReplaceSozialdienstAdminDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  form = this.formBuilder.group({
    nachname: [<string | null>null, [Validators.required]],
    vorname: [<string | null>null, [Validators.required]],
    email: [
      <string | null>null,
      [Validators.required, Validators.pattern(PATTERN_EMAIL)],
    ],
  });

  static open(dialog: MatDialog) {
    return dialog.open<
      ReplaceSozialdienstAdminDialogComponent,
      ReplaceSozialdienstAdminDialogResult
    >(ReplaceSozialdienstAdminDialogComponent);
  }

  confirm() {
    this.form.markAllAsTouched();
    const { nachname, vorname, email } = this.form.value;
    if (!nachname || !vorname || !email) {
      return;
    }
    this.dialogRef.close({ nachname, vorname, email });
  }

  cancel() {
    this.dialogRef.close();
  }
}
