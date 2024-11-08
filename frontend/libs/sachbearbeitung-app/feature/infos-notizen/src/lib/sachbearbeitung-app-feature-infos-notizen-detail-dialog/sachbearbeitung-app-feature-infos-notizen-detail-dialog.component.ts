import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  inject,
  input,
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
import { MatCell } from '@angular/material/table';
import { RouterLink } from '@angular/router';
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
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type SBNotiz = {
  typ: 'GESUCH_NOTIZ';
  notiz?: GesuchNotiz;
};

type JurNotiz = {
  typ: 'JURISTISCHE_NOTIZ';
  notiz?: JuristischeAbklaerungNotiz;
};

export type NotizDialogData = SBNotiz | JurNotiz;

export type NotizDialogResult = {
  id: string | null | undefined;
  betreff: string;
  text: string;
  antwort?: string;
};

export const isJurNotiz = (notiz: NotizDialogData): notiz is JurNotiz => {
  return notiz.typ === 'JURISTISCHE_NOTIZ';
};

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslateModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiFormSaveComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    MatInputModule,
    MatCell,
    TypeSafeMatCellDefDirective,
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
  notizIdSig = input.required<string>({ alias: 'notizId' });
  languageSig = this.store.selectSignal(selectLanguage);

  constructor() {
    // this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    if (isJurNotiz(this.dialogData)) {
      // to be implemented
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
