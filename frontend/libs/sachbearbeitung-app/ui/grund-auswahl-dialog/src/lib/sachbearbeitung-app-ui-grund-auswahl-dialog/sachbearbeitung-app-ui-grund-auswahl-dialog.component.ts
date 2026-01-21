import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  signal,
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
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';

import { AblehnungGrundStore } from '@dv/shared/global/ablehnung-grund';
import {
  Kanton,
  StipDecision,
  StipDecisionText,
} from '@dv/shared/model/gesuch';
import { SharedUiFileUploadComponent } from '@dv/shared/ui/file-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';

export interface GrundAuswahlDialogData {
  titleKey: string;
  labelKey: string;
  messageKey: string;
  confirmKey: string;
  allowedTypes?: string[];
}

export type GrundAuswahlDialogResult =
  | {
      type: 'grund';
      entityId: string;
      kanton?: Kanton;
    }
  | {
      type: 'manuell';
      kommentar?: string;
      verfuegungUpload: File;
    };

const GRUND_MANUELL = { id: undefined, stipDecision: 'MANUELL' as const };

@Component({
  selector: 'dv-sachbearbeitung-app-ui-grund-auswahl-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDividerModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFileUploadComponent,
    SharedUiMaxLengthDirective,
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
  readonly kantone = Object.values(Kanton);
  selectedFileSig = signal<File | null>(null);
  dialogData = inject<GrundAuswahlDialogData>(MAT_DIALOG_DATA);
  store = inject(AblehnungGrundStore);
  grundManuell = GRUND_MANUELL;

  allAblehnungsGruendeSig = computed(() => {
    const ablehnungsGruende = this.store.gruende();
    return [...(ablehnungsGruende.data ?? []), GRUND_MANUELL];
  });
  form = this.formBuilder.group({
    fileUpload: [<File | undefined>undefined],
    kommentar: [<string | undefined>undefined],
    grund: [
      <StipDecisionText | typeof GRUND_MANUELL | undefined>undefined,
      [Validators.required],
    ],
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

    effect(() => {
      const showVerfuegungUpload = this.showVerfuegungUploadSig();
      this.formUtils.setRequired(
        this.form.controls.fileUpload,
        showVerfuegungUpload,
      );
      [this.form.controls.fileUpload, this.form.controls.kommentar].forEach(
        (control) => {
          this.formUtils.setDisabledState(control, !showVerfuegungUpload, true);
        },
      );
    });
  }

  readonly grundSig = toSignal(this.form.controls.grund.valueChanges);

  readonly showKantonFieldSig = computed(
    () =>
      this.grundSig()?.stipDecision === StipDecision.KEIN_WOHNSITZ_KANTON_BE,
  );

  readonly showVerfuegungUploadSig = computed(
    () => this.grundSig()?.stipDecision === 'MANUELL',
  );

  updateFileList(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = input.files;

    if (files && files.length > 0) {
      this.selectedFileSig.set(files[0]);
    } else {
      this.selectedFileSig.set(null);
    }
    this.form.controls.fileUpload.markAsTouched();
  }

  confirm() {
    if (!this.form.valid) {
      return;
    }

    const verfuegungUpload = this.selectedFileSig();
    if (this.showVerfuegungUploadSig()) {
      if (!verfuegungUpload) {
        return;
      }
    }

    const { grund, kanton, kommentar } = convertTempFormToRealValues(
      this.form,
      ['grund'],
    );
    if (grund.id) {
      return this.dialogRef.close({
        type: 'grund',
        entityId: grund.id,
        kanton,
      });
    } else if (verfuegungUpload) {
      return this.dialogRef.close({
        type: 'manuell',
        kommentar: kommentar || undefined,
        verfuegungUpload,
      });
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
