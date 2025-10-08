import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  inject,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslocoPipe } from '@jsverse/transloco';

import { PermissionStore } from '@dv/shared/global/permission';
import { GesuchNotiz, GesuchNotizTyp } from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

export type NotizDialogData = {
  notizTyp: GesuchNotizTyp;
  notiz?: GesuchNotiz;
  readonly?: boolean;
};

export type NotizDialogResult = {
  id: string | null | undefined;
  betreff: string;
  text: string;
  antwort?: string;
  pendenzAbgschlossen?: boolean;
};

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslocoPipe,
    MatInputModule,
    MatCheckboxModule,
    MatFormFieldModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedUiFormReadonlyDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-infos-notizen-detail-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent,
        NotizDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  @HostBinding('class') defaultClasses = 'd-flex flex-column gap-2 p-5';

  public permissionStore = inject(PermissionStore);
  public dialogData = inject<NotizDialogData>(MAT_DIALOG_DATA);
  public form = this.formBuilder.group({
    betreff: [<string | null>null, [Validators.required]],
    text: [<string | null>null, [Validators.required]],
    antwort: [<string | null>null],
    pendenzAbgschlossen: [<boolean | null>null],
  });

  public isJurNotiz = this.dialogData.notizTyp === 'JURISTISCHE_NOTIZ';
  public userIsJurist = this.permissionStore.rolesMapSig().V0_Jurist;
  public userIsSachbearbeiter =
    this.permissionStore.rolesMapSig().V0_Sachbearbeiter;

  constructor() {
    this.form.patchValue({
      betreff: this.dialogData.notiz?.betreff,
      text: this.dialogData.notiz?.text,
      antwort: this.dialogData.notiz?.antwort,
      pendenzAbgschlossen: this.dialogData.notiz?.pendenzAbgschlossen,
    });

    switch (this.dialogData.notizTyp) {
      case 'PENDENZ_NOTIZ':
      case 'GESUCH_NOTIZ': {
        this.form.controls.antwort.disable();

        if (!this.userIsSachbearbeiter) {
          this.form.controls.betreff.disable();
          this.form.controls.text.disable();
          this.form.controls.pendenzAbgschlossen.disable();
        }
        break;
      }
      case 'JURISTISCHE_NOTIZ': {
        if (this.dialogData.notiz?.id) {
          this.form.controls.betreff.disable();
          this.form.controls.text.disable();
        }
        if (!this.userIsJurist) {
          this.form.controls.antwort.disable();
        } else {
          if (this.dialogData.notiz?.antwort || !this.dialogData.notiz?.id) {
            this.form.controls.antwort.disable();
          } else {
            this.form.controls.antwort.setValidators([Validators.required]);
            this.form.controls.antwort.updateValueAndValidity();
          }
        }
        break;
      }
      default:
        assertUnreachable(this.dialogData.notizTyp);
    }
  }

  static open(dialog: MatDialog, data: NotizDialogData) {
    return dialog
      .open<
        SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent,
        NotizDialogData,
        NotizDialogResult
      >(SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent, {
        data,
        width: '800px',
      })
      .afterClosed();
  }

  cancel() {
    this.dialogRef.close();
  }

  handleSave() {
    if (!this.form.valid) {
      return;
    }

    const notizDaten = convertTempFormToRealValues(this.form);

    this.dialogRef.close({
      id: this.dialogData.notiz?.id,
      text: notizDaten.text,
      betreff: notizDaten.betreff,
      antwort: notizDaten.antwort,
      pendenzAbgschlossen: notizDaten.pendenzAbgschlossen,
    });
  }
}
