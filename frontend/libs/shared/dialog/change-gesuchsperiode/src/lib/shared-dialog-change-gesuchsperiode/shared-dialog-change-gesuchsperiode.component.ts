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
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { ChangeGesuchsperiodeStore } from '@dv/shared/data-access/change-gesuchsperiode';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  GesuchFormularType,
  TrancheSetting,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import { capitalized } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import {
  sharedUtilFnErrorTransformer,
  transformValidationReportToFormSteps,
} from '@dv/shared/util-fn/error-transformer';

export type ChangeGesuchsperiodeDialogData = {
  gesuchTrancheId: string;
  gesuchId: string;
  trancheSetting: TrancheSetting;
  gesuchFormular: GesuchFormularType;
};

export type ChangeGesuchsperiodeDialogResult = {
  gesuchsperiodeId: string;
};

@Component({
  selector: 'dv-shared-dialog-change-gesuchsperiode',
  imports: [
    TranslocoPipe,
    RouterLink,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatDatepickerModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiRdIsPendingPipe,
    SharedUiInfoContainerComponent,
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
        ChangeGesuchsperiodeDialogResult
      >
    >(MatDialogRef);
  dialogData = inject<ChangeGesuchsperiodeDialogData>(MAT_DIALOG_DATA);
  changeGesuchsperiodeStore = inject(ChangeGesuchsperiodeStore);
  languageSig = this.store.selectSignal(selectLanguage);

  gesuchsperiodenViewSig = computed(() => {
    const periods =
      this.changeGesuchsperiodeStore.assignableGesuchsperioden().data;
    const lang = this.languageSig();

    return periods?.map((p) => ({
      id: p.id,
      displayName: p[`bezeichnung${capitalized(lang)}`],
    }));
  });

  changeGesuchsperiodeErrorSig = computed(() => {
    const parsedError = sharedUtilFnErrorTransformer(
      this.changeGesuchsperiodeStore.changeGesuchsperiode()?.error,
    );

    if (parsedError.type !== 'validationError') {
      return undefined;
    }

    // Fix: Ensure hasDocuments is undefined, not null
    const parsedErrorFixed: ValidationReport = {
      ...parsedError,
      hasDocuments: parsedError.hasDocuments ?? undefined,
    };

    return transformValidationReportToFormSteps(
      this.dialogData.gesuchId,
      this.dialogData.trancheSetting.routesSuffix,
      parsedErrorFixed,
      this.dialogData.gesuchFormular,
    );
  });

  static open(dialog: MatDialog, data: ChangeGesuchsperiodeDialogData) {
    return dialog.open<
      SharedDialogChangeGesuchsperiodeComponent,
      ChangeGesuchsperiodeDialogData,
      ChangeGesuchsperiodeDialogResult
    >(SharedDialogChangeGesuchsperiodeComponent, {
      data,
    });
  }

  form = this.formBuilder.group({
    gesuchsperiodeId: [<string | undefined>undefined, [Validators.required]],
  });

  constructor() {
    this.changeGesuchsperiodeStore.resetErrors();
    this.changeGesuchsperiodeStore.getAllAssignableGesuchsperiode$({
      gesuchId: this.dialogData.gesuchId,
    });
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
        this.dialogRef.close({ gesuchsperiodeId });
      },
    });
  }

  cancel() {
    return this.dialogRef.close();
  }
}
