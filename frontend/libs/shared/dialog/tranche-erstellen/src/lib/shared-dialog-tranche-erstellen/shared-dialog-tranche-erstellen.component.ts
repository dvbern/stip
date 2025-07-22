import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
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
// eslint-disable-next-line @nx/enforce-module-boundaries
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { addDays } from 'date-fns';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { CreateAenderungsantragRequest } from '@dv/shared/model/gesuch';
import { assertUnreachable } from '@dv/shared/model/type-util';
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
  type: 'createTranche' | 'createAenderung' | 'updateAenderungVonBis';
  id: string;
  minDate: Date;
  maxDate: Date;
  currentGueligAb?: Date;
  currentGueligBis?: Date;
};

const titleKeysByTypeMap = {
  createTranche: {
    title: 'shared.dialog.gesuch.tranche.create.title',
    text: 'shared.dialog.gesuch.tranche.create.text',
  },
  createAenderung: {
    title: 'shared.dialog.gesuch-aenderung.create.title',
    text: 'shared.dialog.gesuch-aenderung.create.text',
  },
  updateAenderungVonBis: {
    title: 'shared.dialog.gesuch-aenderung.update-von-bis.title',
    text: 'shared.dialog.gesuch-aenderung.update-von-bis.text',
  },
} satisfies Record<GesuchTrancheErstellenData['type'], unknown>;

@Component({
  selector: 'dv-shared-dialog-tranche-erstellen',
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
  store = inject(Store);
  config = inject(SharedModelCompileTimeConfig);

  titleKeys = titleKeysByTypeMap[this.dialogData.type];
  form = this.formBuilder.group({
    gueltigAb: [
      <Date | undefined>this.dialogData.currentGueligAb,
      Validators.required,
    ],
    gueltigBis: [<Date | undefined>this.dialogData.currentGueligBis],
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

    const servicePayload = {
      onSuccess: () => {
        this.dialogRef.close(null);
      },
      onFailure: (error: unknown) => {
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
    } as const;

    switch (this.dialogData.type) {
      case 'createTranche': {
        this.gesuchAenderungStore.createGesuchTrancheCopy$({
          createGesuchTrancheRequest: createTrancheData,
          gesuchId: this.dialogData.id,
          ...servicePayload,
        });
        break;
      }
      case 'createAenderung': {
        this.gesuchAenderungStore.createGesuchAenderung$({
          createAenderungsantragRequest: createTrancheData,
          gesuchId: this.dialogData.id,
          ...servicePayload,
        });
        break;
      }
      case 'updateAenderungVonBis': {
        this.gesuchAenderungStore.updateAenderungVonBis$({
          aenderungId: this.dialogData.id,
          patchAenderungsInfoRequest: createTrancheData,
          onSuccess: () => {
            this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
            servicePayload.onSuccess();
          },
          onFailure: servicePayload.onFailure,
        });
        break;
      }
      default: {
        assertUnreachable(this.dialogData.type);
      }
    }
  }

  cancel() {
    return this.dialogRef.close(null);
  }
}
