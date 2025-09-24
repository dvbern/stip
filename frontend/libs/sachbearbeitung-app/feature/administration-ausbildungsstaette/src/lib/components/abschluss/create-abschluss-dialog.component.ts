import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
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
import { TranslocoPipe } from '@jsverse/transloco';

import {
  AbschlussSlim,
  Bildungsrichtung,
  BrueckenangebotCreate,
} from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

const invalidBildungsrichtungen: Bildungsrichtung[] = ['OBLIGATORISCHE_SCHULE'];

@Component({
  selector: 'dv-create-abschluss-dialog',
  imports: [
    ReactiveFormsModule,
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './create-abschluss-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateAbschlussDialogComponent {
  private dialogRef =
    inject<MatDialogRef<void, BrueckenangebotCreate>>(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  dialogData = inject<{ abschluesse: AbschlussSlim[] }>(MAT_DIALOG_DATA);

  bildungsrichtungen = Object.values(Bildungsrichtung).filter(
    (richtung) => !invalidBildungsrichtungen.includes(richtung),
  );
  form = this.formBuilder.group({
    bildungsrichtung: [<Bildungsrichtung | null>null, Validators.required],
    bezeichnungDe: [<string | null>null, [Validators.required]],
    bezeichnungFr: [<string | null>null, [Validators.required]],
  });

  static open(dialog: MatDialog, data: { abschluesse: AbschlussSlim[] }) {
    return dialog.open<
      CreateAbschlussDialogComponent,
      { abschluesse: AbschlussSlim[] },
      BrueckenangebotCreate
    >(CreateAbschlussDialogComponent, { data });
  }

  constructor() {
    // Add uniqueness validators that depend on bildungsrichtung and existing entries
    this.form.controls.bezeichnungDe.addValidators(
      uniqueAbschlussBezeichnungValidator(
        () => this.form.controls.bildungsrichtung.value,
        this.dialogData.abschluesse,
        'bezeichnungDe',
      ),
    );
    this.form.controls.bezeichnungFr.addValidators(
      uniqueAbschlussBezeichnungValidator(
        () => this.form.controls.bildungsrichtung.value,
        this.dialogData.abschluesse,
        'bezeichnungFr',
      ),
    );

    // When bildungsrichtung changes, revalidate name fields that depend on it
    this.form.controls.bildungsrichtung.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => {
        this.form.controls.bezeichnungDe.updateValueAndValidity();
        this.form.controls.bezeichnungFr.updateValueAndValidity();
      });
  }

  confirm() {
    if (this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form);
    this.dialogRef.close(values);
  }

  cancel() {
    this.dialogRef.close();
  }
}

function uniqueAbschlussBezeichnungValidator(
  getBildungsrichtung: () => Bildungsrichtung | null,
  existing: Array<{
    aktiv: boolean;
    bildungsrichtung: Bildungsrichtung;
    bezeichnungDe: string;
    bezeichnungFr: string;
  }>,
  property: 'bezeichnungDe' | 'bezeichnungFr',
) {
  return (control: AbstractControl<string | null>) => {
    const currentValue = control.value;
    if (!currentValue) return null;

    const br = getBildungsrichtung();
    if (!br) return null;

    const duplicateExists = existing.some(
      (e) => e.bildungsrichtung === br && e[property] === currentValue,
    );
    return duplicateExists ? { notUniqueAbschluss: true } : null;
  };
}
