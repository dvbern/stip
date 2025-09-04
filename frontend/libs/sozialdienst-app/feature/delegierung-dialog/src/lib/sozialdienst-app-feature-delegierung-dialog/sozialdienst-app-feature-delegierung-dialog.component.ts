import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
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
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { subYears } from 'date-fns';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { selectLanguage } from '@dv/shared/data-access/language';
import { Anrede, FallWithDelegierung, Sprache } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { isPending } from '@dv/shared/util/remote-data';
import {
  MEDIUM_AGE_GESUCHSSTELLER,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';
import { SozialdienstAppTranslationKey } from '@dv/sozialdienst-app/assets/i18n';
import { DelegationStore } from '@dv/sozialdienst-app/data-access/delegation';

export interface DelegierungDialogData {
  fall: FallWithDelegierung;
}

@Component({
  selector: 'dv-sozialdienst-app-feature-delegierung-dialog',
  imports: [
    CommonModule,
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    SharedUiHasRolesDirective,
    ReactiveFormsModule,
    MatFormFieldModule,
    SharedUiFormReadonlyDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedUiFormAddressComponent,
    SharedUiFormSaveComponent,
  ],
  templateUrl: './sozialdienst-app-feature-delegierung-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DelegierungDialogComponent implements OnInit, OnDestroy {
  private dialog = inject(MatDialog);
  private dialogRef =
    inject<MatDialogRef<DelegierungDialogComponent, boolean>>(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private store = inject(Store);
  delegationStore = inject(DelegationStore);
  dialogData = inject<DelegierungDialogData>(MAT_DIALOG_DATA);

  readonly anredeValues = Object.values(Anrede);
  readonly spracheValues = Object.values(Sprache);

  isPending = isPending;

  languageSig = this.store.selectSignal(selectLanguage);

  static open(dialog: MatDialog, data: DelegierungDialogData) {
    return dialog.open<
      DelegierungDialogComponent,
      DelegierungDialogData,
      boolean
    >(DelegierungDialogComponent, {
      data,
    });
  }

  form = this.formBuilder.group({
    fallNummer: [''],
    anrede: [''],
    nachname: [''],
    vorname: [''],
    adresse: SharedUiFormAddressComponent.buildAddressFormGroup(
      this.formBuilder,
    ),
    geburtsdatum: [''],
    email: [''],
    sprache: [''],
  });

  zuweisungSozMitarbeiterForm = this.formBuilder.group({
    sozMitarbeiter: [<string | undefined>undefined, [Validators.required]],
  });

  sozMitarbeiterChangedSig = toSignal(
    this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.valueChanges,
  );

  matDiaogBackdropClickedSig = toSignal(this.dialogRef.backdropClick());

  showUnsavedChangesErrorSig = computed(() => {
    this.matDiaogBackdropClickedSig();

    if (
      isDefined(
        this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.value,
      ) !==
      isDefined(this.dialogData.fall.delegierung.delegierterMitarbeiter?.id)
    ) {
      return true;
    } else {
      return false;
    }
  });

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      this.languageSig(),
    );
  }

  constructor() {
    effect(() => {
      const sozMitarbeiterId = this.sozMitarbeiterChangedSig();

      if (
        sozMitarbeiterId !==
        this.dialogData.fall.delegierung.delegierterMitarbeiter?.id
      ) {
        this.dialogRef.disableClose = true;
      } else {
        this.dialogRef.disableClose = false;
      }
    });
  }

  ngOnInit() {
    this.delegationStore.loadSozialdienstBenutzerList$();
    this.form.patchValue({
      fallNummer: this.dialogData.fall.fallNummer,
      anrede: this.dialogData.fall.delegierung.persoenlicheAngaben?.anrede,
      nachname: this.dialogData.fall.delegierung.persoenlicheAngaben?.nachname,
      vorname: this.dialogData.fall.delegierung.persoenlicheAngaben?.vorname,
      geburtsdatum: parseBackendLocalDateAndPrint(
        this.dialogData.fall.delegierung.persoenlicheAngaben?.geburtsdatum,
        this.languageSig(),
      ),
      email: this.dialogData.fall.delegierung.persoenlicheAngaben?.email,
      sprache: this.dialogData.fall.delegierung.persoenlicheAngaben?.sprache,
    });
    if (this.dialogData.fall.delegierung.persoenlicheAngaben?.adresse) {
      SharedUiFormAddressComponent.patchForm(
        this.form.controls.adresse,
        this.dialogData.fall.delegierung.persoenlicheAngaben.adresse,
      );
    }
    if (this.dialogData.fall.delegierung.delegierterMitarbeiter?.id) {
      this.zuweisungSozMitarbeiterForm.patchValue({
        sozMitarbeiter:
          this.dialogData.fall.delegierung.delegierterMitarbeiter.id,
      });
    }
  }

  ngOnDestroy() {
    this.delegationStore.resetDelegierenState();
  }

  changeSozMitarbeiter() {
    const mitarbeiterId =
      this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.value;

    const delegierungId = this.dialogData.fall.delegierung.id;

    if (mitarbeiterId && delegierungId) {
      this.delegationStore.delegierterMitarbeiterAendern$({
        req: {
          delegierterMitarbeiterAendern: {
            mitarbeiterId,
          },
          delegierungId,
        },
        onSuccess: () => {
          this.dialogRef.close(true);
        },
      });
    }
  }

  rejectDelegation() {
    const delegierungId = this.dialogData.fall.delegierung.id;
    if (delegierungId) {
      SharedUiConfirmDialogComponent.open<
        SharedTranslationKey | SozialdienstAppTranslationKey
      >(this.dialog, {
        title:
          'sozialdienst-app.delegierung-dialog.sozAdmin.delegierung.reject',
        message:
          'sozialdienst-app.delegierung-dialog.sozAdmin.delegierung.reject.message',
      })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.delegationStore.delegierungAblehnen$({
              delegierungId,
              onSuccess: () => {
                this.dialogRef.close(true);
              },
            });
          }
        });
    }
  }

  cancel() {
    this.dialogRef.disableClose = false;
    this.dialogRef.close();
  }
}
