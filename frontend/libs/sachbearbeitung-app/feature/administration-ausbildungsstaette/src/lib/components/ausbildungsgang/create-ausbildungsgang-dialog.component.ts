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
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

import {
  AbschlussSlim,
  AusbildungsgangCreate,
  AusbildungsstaetteSlim,
} from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type CreateAbschlussData = {
  ausbildungsstaetten: AusbildungsstaetteSlim[];
  abschluesse: AbschlussSlim[];
};

@Component({
  selector: 'dv-create-ausbildungsgang-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    TranslatedPropertyPipe,
  ],
  templateUrl: './create-ausbildungsgang-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateAusbildungsgangDialogComponent {
  private dialogRef =
    inject<MatDialogRef<CreateAbschlussData, AusbildungsgangCreate>>(
      MatDialogRef,
    );
  private formBuilder = inject(NonNullableFormBuilder);
  translate = inject(TranslateService);

  dialogData = inject<CreateAbschlussData>(MAT_DIALOG_DATA);
  form = this.formBuilder.group({
    ausbildungsstaette: [
      <AusbildungsstaetteSlim | null>null,
      Validators.required,
    ],
    abschluss: [<AbschlussSlim | null>null, Validators.required],
  });
  formValueChangedSig = toSignal(this.form.valueChanges);
  ausbildungsgangNameSig = computed(() => {
    const { ausbildungsstaette, abschluss } = this.formValueChangedSig() ?? {};
    if (!ausbildungsstaette || !abschluss) {
      return {
        nameDe: '',
        nameFr: '',
      };
    }
    return {
      nameDe: `${ausbildungsstaette.nameDe} - ${abschluss.bezeichnungDe}`,
      nameFr: `${ausbildungsstaette.nameFr} - ${abschluss.bezeichnungFr}`,
    };
  });

  static open(dialog: MatDialog, data: CreateAbschlussData) {
    return dialog.open<
      CreateAusbildungsgangDialogComponent,
      CreateAbschlussData,
      AusbildungsgangCreate
    >(CreateAusbildungsgangDialogComponent, { data });
  }

  confirm() {
    if (this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form);
    this.dialogRef.close({
      abschlussId: values.abschluss.id,
      ausbildungsstaetteId: values.ausbildungsstaette.id,
    });
  }

  cancel() {
    this.dialogRef.close();
  }
}
