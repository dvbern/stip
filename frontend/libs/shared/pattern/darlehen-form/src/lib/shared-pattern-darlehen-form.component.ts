import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  effect,
  inject,
  output,
  signal,
  viewChildren,
} from '@angular/core';
import {
  outputFromObservable,
  toObservable,
  toSignal,
} from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { merge } from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DarlehenDokumentType,
  DarlehenGrund,
  FreiwilligDarlehenUpdateGs,
  FreiwilligDarlehenUpdateSb,
} from '@dv/shared/model/gesuch';
import { getDarlehenPermissions } from '@dv/shared/model/permission-state';
import {
  SharedPatternDocumentUploadComponent,
  createDarlehenUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import {
  SharedUiIfGesuchstellerDirective,
  SharedUiIfSachbearbeiterDirective,
} from '@dv/shared/ui/if-app-type';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';

@Component({
  selector: 'dv-shared-pattern-darlehen-form',
  imports: [
    CommonModule,
    TranslocoPipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatRadioModule,
    MaskitoDirective,
    RouterLink,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiFormMessageErrorDirective,
    SharedUiIfSachbearbeiterDirective,
    SharedUiIfGesuchstellerDirective,
    SharedUiMaxLengthDirective,
    SharedUiFormReadonlyDirective,
  ],
  templateUrl: './shared-pattern-darlehen-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDarlehenFormComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private formService = inject(SharedUtilFormService);
  private dialog = inject(MatDialog);
  private elementRef = inject(ElementRef);
  private compileTimeConfig = inject(SharedModelCompileTimeConfig);
  private permissionStore = inject(PermissionStore);
  private gesuchDashboardStore = inject(DashboardStore, { optional: true });

  private store = inject(Store);
  private config = this.store.selectSignal(selectSharedDataAccessConfigsView);

  private documentUploadsSig = viewChildren(
    SharedPatternDocumentUploadComponent,
  );

  darlehenStore = inject(DarlehenStore);
  darlehenSig = this.darlehenStore.cachedDarlehen.data;

  maskitoNumber = maskitoNumber;

  darlehenPermissionsSig = computed(() => {
    const delegierung =
      this.gesuchDashboardStore?.dashboardViewSig()?.delegierung;

    return getDarlehenPermissions(
      this.darlehenSig()?.status,
      this.compileTimeConfig.appType,
      this.permissionStore.rolesMapSig(),
      delegierung,
    ).permissions;
  });

  private createUploadOptionsSig = createDarlehenUploadOptionsFactory({
    darlehen: this.darlehenSig,
    allowTypes: this.config().deploymentConfig?.allowedMimeTypes?.join(','),
    permissions: this.darlehenPermissionsSig,
  });

  private atLeastOneCheckboxChecked: ValidatorFn = (
    control: AbstractControl,
  ) => {
    const checked = Object.values(control.value).some((v) => v === true);
    return checked ? null : { atLeastOneCheckboxChecked: true };
  };

  isAllDocumentsUploadedSig = computed<boolean>(() => {
    const dokumentUploads = this.documentUploadsSig();
    return dokumentUploads.every((upload) => {
      return upload.hasEntriesSig();
    });
  });

  formSb = this.formBuilder.group({
    gewaehren: [<boolean | null>null, [Validators.required]],
    betrag: [<string | null>null, [Validators.required]],
    kommentar: [<string | null>null, [Validators.required]],
  });

  formGs = this.formBuilder.group({
    betragGewuenscht: [<string | null>null, [Validators.required]],
    schulden: [<string | null>null, [Validators.required]],
    anzahlBetreibungen: [<number | null>null, [Validators.required]],
    gruende: this.formBuilder.group(
      {
        ANSCHAFFUNGEN_FUER_AUSBILDUNG: [<boolean | undefined>undefined],
        AUSBILDUNG_ZWOELF_JAHRE: [<boolean | undefined>undefined],
        HOHE_GEBUEHREN: [<boolean | undefined>undefined],
        NICHT_BERECHTIGT: [<boolean | undefined>undefined],
        ZWEITAUSBILDUNG: [<boolean | undefined>undefined],
      } satisfies Record<DarlehenGrund, [boolean | undefined]>,
      { validators: [this.atLeastOneCheckboxChecked] },
    ),
  });

  gewaehrenChangedSig = toSignal(this.formSb.controls.gewaehren.valueChanges);
  showBetragFieldSig = computed(() => {
    const gewaehren = this.gewaehrenChangedSig();

    return !!gewaehren;
  });

  anzahlBetreibungenChangedSig = toSignal(
    this.formGs.controls.anzahlBetreibungen.valueChanges,
  );

  grundNichtBerechtigtChangedSig = toSignal(
    this.formGs.controls.gruende.controls.NICHT_BERECHTIGT.valueChanges,
  );
  grundHoheGebuehrenChangedSig = toSignal(
    this.formGs.controls.gruende.controls.HOHE_GEBUEHREN.valueChanges,
  );
  grundAnschaffungenFuerAusbildungChangedSig = toSignal(
    this.formGs.controls.gruende.controls.ANSCHAFFUNGEN_FUER_AUSBILDUNG
      .valueChanges,
  );

  anzahlBetreibungenDocSig = this.createUploadOptionsSig(() => {
    // trigger recomputation for darlehen.state change that will disable the upload
    this.darlehenSig();
    return DarlehenDokumentType.BETREIBUNGS_AUSZUG;
  });

  grundNichtBerechtigtDocSig = this.createUploadOptionsSig(() => {
    const isGrundNichtBerechtigt = this.grundNichtBerechtigtChangedSig();
    return isGrundNichtBerechtigt
      ? DarlehenDokumentType.AUFSTELLUNG_KOSTEN_ELTERN
      : null;
  });
  grundHoheGebuehrenDocSig = this.createUploadOptionsSig(() => {
    const isGrundHoheGebuehren = this.grundHoheGebuehrenChangedSig();
    return isGrundHoheGebuehren
      ? DarlehenDokumentType.KOPIE_SCHULGELDRECHNUNG
      : null;
  });
  grundAnschaffungenFuerAusbildungDocSig = this.createUploadOptionsSig(() => {
    const isGrundAnschaffungenFuerAusbildung =
      this.grundAnschaffungenFuerAusbildungChangedSig();
    return isGrundAnschaffungenFuerAusbildung
      ? DarlehenDokumentType.BELEGE_ANSCHAFFUNGEN
      : null;
  });

  gsFormSavedSig = signal(false);
  gsShowMissingDocsErrorSig = signal(false);
  sbFormSavedSig = signal(false);
  formIsUnsavedSig = outputFromObservable(
    merge(
      observeUnsavedChanges(this.formGs, toObservable(this.gsFormSavedSig)),
      observeUnsavedChanges(this.formSb, toObservable(this.sbFormSavedSig)),
    ),
  );
  darlehenUpdatedSig = output<void>();
  darlehenDeletedSig = output<void>();

  constructor() {
    // patch form value
    effect(() => {
      const darlehen = this.darlehenSig();
      if (!darlehen) {
        return;
      }
      this.formSb.patchValue({
        gewaehren: darlehen.gewaehren,
        betrag: darlehen.betrag?.toString(),
        kommentar: darlehen.kommentar,
      });

      this.formGs.patchValue({
        betragGewuenscht: darlehen.betragGewuenscht?.toString(),
        schulden: darlehen.schulden?.toString(),
        anzahlBetreibungen: darlehen.anzahlBetreibungen,
        gruende: Object.fromEntries(
          Object.values(DarlehenGrund).map((grund) => [
            grund,
            darlehen.gruende?.includes(grund) ?? false,
          ]),
        ),
      });
    });

    effect(() => {
      const gewaehren = this.showBetragFieldSig();
      this.formService.setDisabledState(
        this.formSb.controls.betrag,
        !gewaehren,
      );
    });
  }

  private buildUpdatedGsFrom() {
    const realValues = convertTempFormToRealValues(this.formGs, [
      'betragGewuenscht',
      'schulden',
      'anzahlBetreibungen',
    ]);

    const darlehen: FreiwilligDarlehenUpdateGs = {
      betragGewuenscht: fromFormatedNumber(realValues.betragGewuenscht),
      schulden: fromFormatedNumber(realValues.schulden),
      anzahlBetreibungen: realValues.anzahlBetreibungen,
      gruende: Object.entries(this.formGs.controls.gruende.value)
        .filter(([, value]) => value === true)
        .map(([key]) => key as DarlehenGrund),
    };

    return darlehen;
  }

  // Gesuchsteller Actions
  darlehenUpdateAndEingebenGs(): void {
    this.formGs.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const darlehen = this.darlehenSig();
    this.gsShowMissingDocsErrorSig.set(true);

    if (!darlehen || this.formGs.invalid || !this.isAllDocumentsUploadedSig()) {
      return;
    }

    const updatedDarlehen = this.buildUpdatedGsFrom();

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.eingeben.dialog.title',
      message: 'shared.form.darlehen.eingeben.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.eingeben.dialog.confirm',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenUpdateAndEingebenGs$({
            data: {
              darlehenId: darlehen.id,
              freiwilligDarlehenUpdateGs: updatedDarlehen,
            },
            onSuccess: () => {
              this.gsFormSavedSig.set(true);
              this.gsShowMissingDocsErrorSig.set(false);
              this.formGs.markAsPristine();
            },
          });
        }
      });
  }

  darlehenDeleteGs(): void {
    {
      const darlehen = this.darlehenSig();

      if (!darlehen) {
        return;
      }

      SharedUiConfirmDialogComponent.open(this.dialog, {
        title: 'shared.form.darlehen.delete.dialog.title',
        message: 'shared.form.darlehen.delete.dialog.message',
        cancelText: 'shared.cancel',
        confirmText: 'shared.form.darlehen.delete',
      })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.darlehenStore.darlehenDeleteGs$({
              data: { darlehenId: darlehen.id },
              onSuccess: () => {
                this.gsFormSavedSig.set(true);
                this.formGs.markAsPristine();
                this.darlehenDeletedSig.emit();
              },
            });
          }
        });
    }
  }

  // Sachbearbeiter Actions

  private buildUpdatedSbFrom(): FreiwilligDarlehenUpdateSb {
    const realValues = convertTempFormToRealValues(this.formSb, [
      'betrag',
      'kommentar',
      'gewaehren',
    ]);
    return {
      betrag: fromFormatedNumber(realValues.betrag),
      kommentar: realValues.kommentar,
      gewaehren: realValues.gewaehren,
    };
  }

  darlehenUpdateAndFreigeben(): void {
    this.formSb.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const darlehen = this.darlehenSig();

    if (this.formSb.invalid || !darlehen) {
      return;
    }

    const updatedDarlehen = this.buildUpdatedSbFrom();

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.freigeben.dialog.title',
      message: 'shared.form.darlehen.freigeben.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.freigeben',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenUpdateAndFreigebenSb$({
            data: {
              darlehenId: darlehen.id,
              freiwilligDarlehenUpdateSb: updatedDarlehen,
            },
            onSuccess: () => {
              this.sbFormSavedSig.set(true);
              this.formSb.markAsPristine();
            },
          });
        }
      });
  }

  darlehenUpdateAndZurueckweisen(): void {
    const darlehen = this.darlehenSig();

    if (!darlehen) {
      return;
    }

    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: darlehen.id,
      titleKey: 'shared.form.darlehen.zurueckweisen.dialog.title',
      messageKey: 'shared.form.darlehen.zurueckweisen.dialog.message',
      placeholderKey: 'shared.form.darlehen.zurueckweisen.dialog.placeholder',
      confirmKey: 'shared.form.darlehen.zurueckweisen',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.formSb.reset();
          this.darlehenStore.darlehenZurueckweisen$({
            darlehenId: darlehen.id,
            kommentar: { text: result.kommentar },
          });
        }
      });
  }

  // Freigabestelle Actions
  darlehenUpdateAndAbschliessen(): void {
    this.formSb.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const darlehen = this.darlehenSig();

    if (this.formSb.invalid || !darlehen) {
      return;
    }

    const updatedDarlehen = this.buildUpdatedSbFrom();

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.abschliessen.dialog.title',
      message: 'shared.form.darlehen.abschliessen.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.abschliessen.dialog.confirm',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenUpdateAndAbschliessenSb$({
            data: {
              darlehenId: darlehen.id,
              freiwilligDarlehenUpdateSb: updatedDarlehen,
            },
            onSuccess: () => {
              this.sbFormSavedSig.set(true);
              this.darlehenUpdatedSig.emit();
              this.formSb.markAsPristine();
            },
          });
        }
      });
  }
}
