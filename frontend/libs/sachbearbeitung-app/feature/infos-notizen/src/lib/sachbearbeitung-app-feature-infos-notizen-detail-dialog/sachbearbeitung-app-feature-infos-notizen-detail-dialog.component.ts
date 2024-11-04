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

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizUpdate,
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
import { parseBackendLocalDateAndPrint } from '@dv/shared/util/validator-date';

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
        GesuchNotizUpdate | GesuchNotizCreate
      >
    >(MatDialogRef);
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);

  @HostBinding('class') defaultClasses = 'd-flex flex-column gap-2 p-5';
  dialogData = inject<GesuchNotiz | undefined>(MAT_DIALOG_DATA);
  form = this.formBuilder.group({
    datum: [<string | undefined>undefined],
    user: [<string | null>null],
    betreff: [<string | null>null, [Validators.required]],
    text: [<string | null>null, [Validators.required]],
  });
  notizIdSig = input.required<string>({ alias: 'notizId' });
  languageSig = this.store.selectSignal(selectLanguage);

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    this.form.patchValue({
      datum: parseBackendLocalDateAndPrint(
        this.dialogData?.timestampErstellt,
        this.languageSig(),
      ),
      betreff: this.dialogData?.betreff,
      text: this.dialogData?.text,
      user: this.dialogData?.userErstellt,
    });
  }

  static open(dialog: MatDialog, data?: GesuchNotiz) {
    return dialog
      .open<
        SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent,
        GesuchNotiz,
        GesuchNotizUpdate | GesuchNotizCreate
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
      id: this.dialogData?.id,
      text: notizDaten.text,
      betreff: notizDaten.betreff,
    });
  }
}
