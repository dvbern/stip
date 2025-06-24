import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
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
import {
  Kanton,
  StipDecision,
  StipDecisionText,
} from '@dv/shared/model/gesuch';
import { SharedUiFormMessageErrorDirective } from '@dv/shared/ui/form';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';

export interface GrundAuswahlDialogData {
  titleKey: string;
  labelKey: string;
  messageKey: string;
  confirmKey: string;
}

export interface GrundAuswahlDialogResult {
  entityId: string;
  kanton?: Kanton;
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
  private formUtils = inject(SharedUtilFormService);
  dialogData = inject<GrundAuswahlDialogData>(MAT_DIALOG_DATA);
  store = inject(AblehnungGrundStore);
  readonly kantone = Object.values(Kanton);

  form = this.formBuilder.group({
    grund: [<StipDecisionText | undefined>undefined, [Validators.required]],
    kanton: [<Kanton | undefined>undefined, [Validators.required]],
  });

  static open(dialog: MatDialog, data: GrundAuswahlDialogData) {
    return dialog.open<
      SachbearbeitungAppUiGrundAuswahlDialogComponent,
      GrundAuswahlDialogData,
      GrundAuswahlDialogResult
    >(SachbearbeitungAppUiGrundAuswahlDialogComponent, { data });
  }
  constructor() {
    effect(() => {
      const showKanton = this.showKantonFieldSig();
      this.formUtils.setDisabledState(
        this.form.controls.kanton,
        !showKanton,
        true,
      );
    });
  }

  readonly grundSig = toSignal(this.form.controls.grund.valueChanges);

  readonly showKantonFieldSig = computed(
    () =>
      this.grundSig()?.stipDecision === StipDecision.KEIN_WOHNSITZ_KANTON_BE,
  );

  confirm() {
    if (!this.form.valid) {
      return;
    }

    const { grund, kanton } = convertTempFormToRealValues(this.form, ['grund']);
    this.dialogRef.close({ entityId: grund.id, kanton });
  }

  cancel() {
    this.dialogRef.close();
  }
}
