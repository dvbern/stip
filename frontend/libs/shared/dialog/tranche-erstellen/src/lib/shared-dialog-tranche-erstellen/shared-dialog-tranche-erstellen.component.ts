import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
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
import { MatInputModule } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';
import { addDays } from 'date-fns';

import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { CreateAenderungsantragRequest } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  convertTempFormToRealValues,
  provideMaterialDefaultOptions,
} from '@dv/shared/util/form';
import { toBackendLocalDate } from '@dv/shared/util/validator-date';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

type GesuchTrancheErstellenData = {
  forAenderung: boolean;
  gesuchId: string;
  minDate: Date;
  maxDate: Date;
};

@Component({
  selector: 'dv-shared-dialog-tranche-erstellen',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedUiFormSaveComponent,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './shared-dialog-tranche-erstellen.component.html',
  styleUrl: './shared-dialog-tranche-erstellen.component.scss',
  providers: [
    provideDvDateAdapter(),
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogTrancheErstellenComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogRef: MatDialogRef<SharedDialogTrancheErstellenComponent> =
    inject(MatDialogRef);
  dialogData = inject<GesuchTrancheErstellenData>(MAT_DIALOG_DATA);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  config = inject(SharedModelCompileTimeConfig);

  form = this.formBuilder.group({
    gueltigAb: [<Date | null>null, Validators.required],
    gueltigBis: [<Date | null>null],
    kommentar: [<string | null>null, Validators.required],
  });

  private gueltigAbChangedSig = toSignal(
    this.form.controls.gueltigAb.valueChanges,
  );
  oneDayAfterGueltigAbSig = computed(() => {
    const gueltigAb = this.gueltigAbChangedSig();
    if (!gueltigAb) {
      return null;
    }
    return addDays(gueltigAb, 1);
  });

  static open(dialog: MatDialog, data: GesuchTrancheErstellenData) {
    return dialog.open<
      SharedDialogTrancheErstellenComponent,
      GesuchTrancheErstellenData,
      CreateAenderungsantragRequest
    >(SharedDialogTrancheErstellenComponent, { data });
  }

  confirm() {
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }
    const createTranche = convertTempFormToRealValues(this.form, [
      'gueltigAb',
      'kommentar',
    ]);

    const createTrancheData = {
      start: toBackendLocalDate(createTranche.gueltigAb),
      end: createTranche.gueltigBis
        ? toBackendLocalDate(createTranche.gueltigBis)
        : undefined,
      comment: createTranche.kommentar,
    };

    if (this.dialogData.forAenderung) {
      return this.dialogRef.close(createTrancheData);
    }

    this.gesuchAenderungStore.createGesuchTrancheCopy$({
      gesuchId: this.dialogData.gesuchId,
      createGesuchTrancheRequest: createTrancheData,
      onSuccess: () => {
        this.dialogRef.close(null);
      },
      onFailure: (error) => {
        const parsedError = sharedUtilFnErrorTransformer(error);

        if (
          parsedError.type === 'validationError' &&
          parsedError.validationErrors.some(
            (e) =>
              e.messageTemplate ===
              '{jakarta.validation.constraints.gesuchTranche.daterangeTooShort.message}',
          )
        ) {
          this.form.setErrors({
            dateRangeTooShort: true,
          });
        }
      },
    });
  }

  cancel() {
    return this.dialogRef.close(null);
  }
}
