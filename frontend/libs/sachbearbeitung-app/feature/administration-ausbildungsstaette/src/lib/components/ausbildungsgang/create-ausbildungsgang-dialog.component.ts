import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
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
  AusbildungsgangSlim,
  AusbildungsstaetteSlim,
} from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { LookupType } from '@dv/shared/model/select-search';
import { SharedUiSelectSearchComponent } from '@dv/shared/ui/select-search';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type CreateAbschlussData = {
  existingAusbildungsgaenge: AusbildungsgangSlim[];
  ausbildungsstaetten: (AusbildungsstaetteSlim & LookupType)[];
  abschluesse: (AbschlussSlim & LookupType)[];
  language: Language;
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
    MatAutocompleteModule,
    TranslatedPropertyPipe,
    SharedUiSelectSearchComponent,
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
  form = this.formBuilder.group(
    {
      ausbildungsstaetteId: [
        <string | undefined>undefined,
        [Validators.required],
      ],
      abschlussId: [<string | undefined>undefined, [Validators.required]],
    },
    {
      validators: validateUniqueCombination(
        this.dialogData.existingAusbildungsgaenge,
      ),
    },
  );
  formValueChangedSig = toSignal(this.form.valueChanges);
  ausbildungsstaetteIdChangedSig = toSignal(
    this.form.controls.ausbildungsstaetteId.valueChanges,
  );
  abschlussIdChangedSig = toSignal(this.form.controls.abschlussId.valueChanges);
  selectedAusbildungsstaetteSig = computed(() => {
    const ausbildungsstaetteId = this.ausbildungsstaetteIdChangedSig();
    const ausbildungsstaette = this.dialogData.ausbildungsstaetten.find(
      (a) => a.id === ausbildungsstaetteId,
    );

    return ausbildungsstaette;
  });
  selectedAbschlussSig = computed(() => {
    const abschlussId = this.abschlussIdChangedSig();
    const abschluss = this.dialogData.abschluesse.find(
      (a) => a.id === abschlussId,
    );

    return abschluss;
  });
  ausbildungsgangNameSig = computed(() => {
    const ausbildungsstaette = this.selectedAusbildungsstaetteSig();
    const abschluss = this.selectedAbschlussSig();
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
      abschlussId: values.abschlussId,
      ausbildungsstaetteId: values.ausbildungsstaetteId,
    });
  }

  cancel() {
    this.dialogRef.close();
  }
}

const validateUniqueCombination =
  (existingAusbildungsgaenge: AusbildungsgangSlim[]): ValidatorFn =>
  (
    formGroup: AbstractControl<{
      ausbildungsstaetteId: string | undefined;
      abschlussId: string | null;
    }>,
  ) => {
    const abschlussId = formGroup.value.abschlussId;
    const ausbildungsstaetteId = formGroup.value.ausbildungsstaetteId;

    if (!ausbildungsstaetteId || !abschlussId) {
      return null;
    }

    if (
      existingAusbildungsgaenge.some((ausbildugnsgang) => {
        return (
          ausbildugnsgang.aktiv &&
          ausbildugnsgang.abschlussId === abschlussId &&
          ausbildugnsgang.ausbildungsstaetteId === ausbildungsstaetteId
        );
      })
    ) {
      return { conflict: true };
    }

    return null;
  };
