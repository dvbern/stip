import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
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
import { Store } from '@ngrx/store';
import { subYears } from 'date-fns';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { selectLanguage } from '@dv/shared/data-access/language';
import { Anrede, DelegierungStatus, Sprache } from '@dv/shared/model/gesuch';
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
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';
import { isPending } from '@dv/shared/util/remote-data';
import {
  MEDIUM_AGE_GESUCHSSTELLER,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';
import { SozialdienstAppTranslationKey } from '@dv/sozialdienst-app/assets/i18n';
import { DelegationStore } from '@dv/sozialdienst-app/data-access/delegation';
import { SozialdienstAppUiAdvTranslocoDirective } from '@dv/sozialdienst-app/ui/adv-transloco-directive';

export interface DelegierungDialogData {
  delegierungId: string;
}

@Component({
  selector: 'dv-sozialdienst-app-feature-delegierung-dialog',
  imports: [
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
    SharedUiLoadingComponent,
    SozialdienstAppUiAdvTranslocoDirective,
  ],
  providers: [
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
  templateUrl: './sozialdienst-app-feature-delegierung-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DelegierungDialogComponent implements OnDestroy {
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
  delegierungSig = computed(() => this.delegationStore.delegierung().data);

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

  saveLabelKey: SozialdienstAppTranslationKey = `sozialdienst-app.delegierung-dialog.sozAdmin.delegierung.${
    this.delegierungSig()?.delegierterMitarbeiter ? 'changeZuweisung' : 'accept'
  }`;

  sozMitarbeiterChangedSig = toSignal(
    this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.valueChanges,
  );

  matDiaogBackdropClickedSig = toSignal(this.dialogRef.backdropClick());

  showUnsavedChangesErrorSig = computed(() => {
    this.matDiaogBackdropClickedSig();

    return (
      isDefined(
        this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.value,
      ) !== isDefined(this.delegierungSig()?.delegierterMitarbeiter?.id)
    );
  });

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      this.languageSig(),
    );
  }

  constructor() {
    this.delegationStore.loadSozialdienstBenutzerList$();
    this.delegationStore.loadDelegierung$(this.dialogData);

    effect(() => {
      const sozMitarbeiterId = this.sozMitarbeiterChangedSig();

      if (
        sozMitarbeiterId !== this.delegierungSig()?.delegierterMitarbeiter?.id
      ) {
        this.dialogRef.disableClose = true;
      } else {
        this.dialogRef.disableClose = false;
      }
    });

    effect(() => {
      const delegierung = this.delegierungSig();
      if (!delegierung) {
        return;
      }

      this.form.patchValue({
        fallNummer: delegierung.fallNummer,
        anrede: delegierung.persoenlicheAngaben?.anrede,
        nachname: delegierung.persoenlicheAngaben?.nachname,
        vorname: delegierung.persoenlicheAngaben?.vorname,
        geburtsdatum: parseBackendLocalDateAndPrint(
          delegierung.persoenlicheAngaben?.geburtsdatum,
          this.languageSig(),
        ),
        email: delegierung.persoenlicheAngaben?.email,
        sprache: delegierung.persoenlicheAngaben?.sprache,
      });

      if (
        !(['AKZEPTIERT', 'EINGEREICHT'] as DelegierungStatus[]).includes(
          delegierung.status,
        )
      ) {
        this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.disable();
      }

      if (delegierung.persoenlicheAngaben?.adresse) {
        SharedUiFormAddressComponent.patchForm(
          this.form.controls.adresse,
          delegierung.persoenlicheAngaben.adresse,
        );
      }

      if (delegierung.delegierterMitarbeiter?.id) {
        this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.patchValue(
          delegierung.delegierterMitarbeiter.id,
        );
      }
    });
  }

  ngOnDestroy() {
    this.delegationStore.resetDelegierenState();
  }

  changeSozMitarbeiter() {
    const mitarbeiterId =
      this.zuweisungSozMitarbeiterForm.controls.sozMitarbeiter.value;

    const delegierungId = this.delegierungSig()?.id;

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
    const delegierungId = this.delegierungSig()?.id;
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

  removeDelegation() {
    const delegierungId = this.delegierungSig()?.id;
    if (delegierungId) {
      SharedUiConfirmDialogComponent.open<
        SharedTranslationKey | SozialdienstAppTranslationKey
      >(this.dialog, {
        title:
          'sozialdienst-app.delegierung-dialog.sozAdmin.delegierung.remove',
        message:
          'sozialdienst-app.delegierung-dialog.sozAdmin.delegierung.remove.message',
      })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.delegationStore.delegierungAufloesen$({
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
