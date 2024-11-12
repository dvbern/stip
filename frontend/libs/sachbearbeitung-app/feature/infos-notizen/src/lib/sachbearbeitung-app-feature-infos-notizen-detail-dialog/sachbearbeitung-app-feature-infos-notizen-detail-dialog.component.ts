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
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { selectLanguage } from '@dv/shared/data-access/language';
import {
  GesuchNotiz,
  JuristischeAbklaerungNotiz,
} from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type SBNotiz = {
  notizTyp: 'GESUCH_NOTIZ';
  notiz?: GesuchNotiz;
};

type JurNotiz = {
  notizTyp: 'JURISTISCHE_NOTIZ';
  notiz?: JuristischeAbklaerungNotiz;
};

export type NotizDialogData = SBNotiz | JurNotiz;

export type NotizDialogResult = {
  id: string | null | undefined;
  betreff: string;
  text: string;
  antwort?: string;
};

const isJurNotiz = (data: NotizDialogData): data is JurNotiz => {
  return data.notizTyp === 'JURISTISCHE_NOTIZ';
};

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslateModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    MatInputModule,
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
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);

  @HostBinding('class') defaultClasses = 'd-flex flex-column gap-2 p-5';
  dialogData = inject<NotizDialogData>(MAT_DIALOG_DATA);
  form = this.formBuilder.group({
    betreff: [<string | null>null, [Validators.required]],
    text: [<string | null>null, [Validators.required]],
    antwort: [<string | null>null],
  });

  languageSig = this.store.selectSignal(selectLanguage);
  public isJurNotizFn = isJurNotiz;

  constructor() {
    if (isJurNotiz(this.dialogData)) {
      this.form.patchValue({
        betreff: this.dialogData.notiz?.betreff,
        text: this.dialogData.notiz?.text,
        antwort: this.dialogData.notiz?.antwort,
      });
    } else {
      this.form.patchValue({
        betreff: this.dialogData.notiz?.betreff,
        text: this.dialogData.notiz?.text,
      });
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
    });
  }
}
