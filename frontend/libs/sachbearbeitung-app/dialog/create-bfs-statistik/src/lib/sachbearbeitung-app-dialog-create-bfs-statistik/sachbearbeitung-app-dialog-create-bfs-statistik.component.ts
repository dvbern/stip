import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MaskitoDirective } from '@maskito/angular';

import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { maskitoYear } from '@dv/shared/util/maskito-util';

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-create-bfs-statistik',
  imports: [
    ReactiveFormsModule,
    MaskitoDirective,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-dialog-create-bfs-statistik.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogCreateBfsStatistikComponent {
  private dialogRef =
    inject<
      MatDialogRef<SachbearbeitungAppDialogCreateBfsStatistikComponent, number>
    >(MatDialogRef);
  maskitoYear = maskitoYear({ max: new Date().getFullYear() });

  form = new FormGroup({
    year: new FormControl(<string | undefined>undefined, [Validators.required]),
  });

  static open(
    dialog: MatDialog,
  ): MatDialogRef<SachbearbeitungAppDialogCreateBfsStatistikComponent, number> {
    return dialog.open(SachbearbeitungAppDialogCreateBfsStatistikComponent);
  }

  cancel() {
    this.dialogRef.close();
  }

  confirm() {
    const value = +(this.form.controls.year.value ?? 0);

    if (this.form.invalid || !value) {
      return;
    }

    this.dialogRef.close(value);
  }
}
