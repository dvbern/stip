import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatDatepicker,
  MatDatepickerModule,
} from '@angular/material/datepicker';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';
import { endOfMonth, startOfMonth } from 'date-fns';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { CreateAenderungsantragRequest } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { provideMonthYearSachbearbeiterDateAdapter } from '@dv/shared/util/date-adapter';
import {
  convertTempFormToRealValues,
  provideMaterialDefaultOptions,
} from '@dv/shared/util/form';
import { toBackendLocalDate } from '@dv/shared/util/validator-date';

type GesuchAenderungData = {
  minDate: Date;
  maxDate: Date;
};

@Component({
  selector: 'dv-shared-ui-aenderung-melden-dialog',
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
  ],
  templateUrl: './shared-ui-aenderung-melden-dialog.component.html',
  styleUrl: './shared-ui-aenderung-melden-dialog.component.scss',
  providers: [
    provideMonthYearSachbearbeiterDateAdapter(),
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiAenderungMeldenDialogComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  dialogData = inject<GesuchAenderungData>(MAT_DIALOG_DATA);
  private dialogRef: MatDialogRef<
    SharedUiAenderungMeldenDialogComponent,
    CreateAenderungsantragRequest | null
  > = inject(MatDialogRef);

  config = inject(SharedModelCompileTimeConfig);

  form = this.formBuilder.group({
    gueltigAb: [<Date | null>null, Validators.required],
    gueltigBis: [<Date | null>null],
    kommentar: [<string | null>null, Validators.required],
  });

  static open(dialog: MatDialog, data: GesuchAenderungData) {
    return dialog.open<
      SharedUiAenderungMeldenDialogComponent,
      GesuchAenderungData,
      CreateAenderungsantragRequest
    >(SharedUiAenderungMeldenDialogComponent, { data });
  }

  monthSelectedGueltigAb(event: Date, picker: MatDatepicker<Date>) {
    if (this.config.isGesuchApp) return;

    this.form.get('gueltigAb')?.setValue(startOfMonth(event));
    picker.close();
  }

  monthSelectedGueltigBis(event: Date, picker: MatDatepicker<Date>) {
    if (this.config.isGesuchApp) return;

    this.form.get('gueltigBis')?.setValue(endOfMonth(event));
    picker.close();
  }

  confirm() {
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }
    const aenderungsAntrag = convertTempFormToRealValues(this.form, [
      'gueltigAb',
      'kommentar',
    ]);

    if (this.config.isSachbearbeitungApp) {
      aenderungsAntrag.gueltigAb = startOfMonth(aenderungsAntrag.gueltigAb);
      aenderungsAntrag.gueltigBis = aenderungsAntrag.gueltigBis
        ? endOfMonth(aenderungsAntrag.gueltigBis)
        : null;
    }

    return this.dialogRef.close({
      start: toBackendLocalDate(aenderungsAntrag.gueltigAb),
      end: aenderungsAntrag.gueltigBis
        ? toBackendLocalDate(aenderungsAntrag.gueltigBis)
        : undefined,
      comment: aenderungsAntrag.kommentar,
    });
  }

  cancel() {
    return this.dialogRef.close(null);
  }
}
