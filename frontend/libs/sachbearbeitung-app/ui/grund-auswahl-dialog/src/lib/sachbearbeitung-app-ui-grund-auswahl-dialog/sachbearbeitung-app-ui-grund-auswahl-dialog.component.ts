import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
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
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';

import { AblehnungGrundStore } from '@dv/shared/global/ablehnung-grund';
import { SharedUiFormMessageErrorDirective } from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

export interface GrundAuswahlDialogData {
  titleKey: string;
  labelKey: string;
  messageKey: string;
  confirmKey: string;
}

export interface GrundAuswahlDialogResult {
  entityId: string;
}

@Component({
  selector: 'dv-sachbearbeitung-app-ui-grund-auswahl-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatSelectModule,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './sachbearbeitung-app-ui-grund-auswahl-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppUiGrundAuswahlDialogComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        SachbearbeitungAppUiGrundAuswahlDialogComponent,
        GrundAuswahlDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  dialogData = inject<GrundAuswahlDialogData>(MAT_DIALOG_DATA);
  store = inject(AblehnungGrundStore);

  form = this.formBuilder.group({
    grundId: [<string | undefined>undefined, [Validators.required]],
  });

  static open(dialog: MatDialog, data: GrundAuswahlDialogData) {
    return dialog.open<
      SachbearbeitungAppUiGrundAuswahlDialogComponent,
      GrundAuswahlDialogData,
      GrundAuswahlDialogResult
    >(SachbearbeitungAppUiGrundAuswahlDialogComponent, { data });
  }

  confirm() {
    if (!this.form.valid) {
      return;
    }

    const { grundId } = convertTempFormToRealValues(this.form, ['grundId']);
    this.dialogRef.close({ entityId: grundId });
  }

  cancel() {
    this.dialogRef.close();
  }
}
