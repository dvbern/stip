import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
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
import { MatSelectModule } from '@angular/material/select';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { ChangeGesuchsperiodeStore } from '@dv/shared/data-access/change-gesuchsperiode';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';

@Component({
  selector: 'dv-shared-dialog-change-gesuchsperiode',
  imports: [
    CommonModule,
    TranslatePipe,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatDatepickerModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './shared-dialog-change-gesuchsperiode.component.html',
  styleUrl: './shared-dialog-change-gesuchsperiode.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogChangeGesuchsperiodeComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogRef =
    inject<
      MatDialogRef<
        SharedDialogChangeGesuchsperiodeComponent,
        { gesuchTrancheId: string }
      >
    >(MatDialogRef);
  private globalNotificationStore = inject(GlobalNotificationStore);
  dialogData = inject<{ gesuchTrancheId: string }>(MAT_DIALOG_DATA);
  changeGesuchsperiodeStore = inject(ChangeGesuchsperiodeStore);
  languageSig = this.store.selectSignal(selectLanguage);

  gesuchsperiodenViewSig = computed(() => {
    const p = this.changeGesuchsperiodeStore.assignableGesuchsperioden().data;
    const lang = this.languageSig();

    return p?.map((period) => ({
      id: period.id,
      displayName: lang === 'de' ? period.bezeichnungDe : period.bezeichnungFr,
    }));
  });

  static open(dialog: MatDialog, data: { gesuchTrancheId: string }) {
    return dialog.open<
      SharedDialogChangeGesuchsperiodeComponent,
      { gesuchTrancheId: string }
    >(SharedDialogChangeGesuchsperiodeComponent, {
      data,
    });
  }

  form = this.formBuilder.group({
    gesuchsperiodeId: [<string | undefined>undefined, [Validators.required]],
  });

  constructor() {
    this.changeGesuchsperiodeStore.getAllAssignableGesuchsperiode$();
  }

  confirm() {
    const { gesuchsperiodeId } = this.form.value;
    const gesuchTrancheId = this.dialogData.gesuchTrancheId;

    if (!gesuchsperiodeId || !gesuchTrancheId) {
      return;
    }

    this.changeGesuchsperiodeStore.setGesuchsperiodeForGesuch$({
      gesuchsperiodeId,
      gesuchTrancheId,
      onSuccess: () => {
        this.globalNotificationStore.createSuccessNotification({
          messageKey: 'shared.dialog.change-gesuchsperiode.success',
        });
        this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
        this.dialogRef.close({ gesuchTrancheId });
      },
    });
  }

  cancel() {
    return this.dialogRef.close();
  }
}
