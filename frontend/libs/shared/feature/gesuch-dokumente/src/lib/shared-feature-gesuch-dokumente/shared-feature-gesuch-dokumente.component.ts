import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  SharedModelGesuchDokument,
  SharedModelTableCustomDokument,
  SharedModelTableDokument,
} from '@dv/shared/model/dokument';
import { Dokumentstatus } from '@dv/shared/model/gesuch';
import { DOKUMENTE } from '@dv/shared/model/gesuch-form';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiIfGesuchstellerDirective,
  SharedUiIfSachbearbeiterDirective,
} from '@dv/shared/ui/if-app-type';
import {
  RejectDokument,
  SharedUiRejectDokumentComponent,
} from '@dv/shared/ui/reject-dokument';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  getLatestGesuchIdFromGesuch$,
  getLatestTrancheIdFromGesuch$,
} from '@dv/shared/util/gesuch';

import { AdditionalDokumenteComponent } from './components/additional-dokumente/additional-dokumente.component';
import { CreateCustomDokumentDialogComponent } from './components/create-custom-dokument-dialog/create-custom-dokument-dialog.component';
import { CustomDokumenteComponent } from './components/custom-dokumente/custom-dokumente.component';
import { RequiredDokumenteComponent } from './components/required-dokumente/required-dokumente.component';

@Component({
  selector: 'dv-shared-feature-gesuch-dokumente',
  imports: [
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    RequiredDokumenteComponent,
    AdditionalDokumenteComponent,
    CustomDokumenteComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiIfGesuchstellerDirective,
    SharedUiIfSachbearbeiterDirective,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private config = inject(SharedModelCompileTimeConfig);
  private destroyRef = inject(DestroyRef);
  public dokumentsStore = inject(DokumentsStore);

  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  stepViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  additionalDokumenteViewSig = computed(() => {
    const { allowTypes, gesuchId, permissions, trancheId, readonly } =
      this.gesuchViewSig();
    const { dokuments, requiredDocumentTypes, loading } =
      this.dokumentsStore.additionalDokumenteViewSig();

    return {
      gesuchId,
      trancheId,
      allowTypes,
      unterschriftenblaetter: dokuments,
      permissions,
      readonly,
      loading,
      showList: dokuments.length > 0 || requiredDocumentTypes.length > 0,
      requiredDocumentTypes,
    };
  });

  standardDokumenteViewSig = computed(() => {
    const {
      allowTypes,
      gesuchId,
      permissions,
      trancheSetting,
      trancheId,
      readonly,
      config: { isSachbearbeitungApp },
      gesuch,
    } = this.gesuchViewSig();
    const { dokuments, requiredDocumentTypes, loading } =
      this.dokumentsStore.dokumenteViewSig();
    const stepsFlow = this.stepViewSig().stepsFlow;

    const kommentare = this.dokumentsStore.kommentareViewSig();

    return {
      gesuchId,
      nachfrist: gesuch?.nachfristDokumente,
      trancheId,
      permissions,
      trancheSetting: trancheSetting ?? undefined,
      canApproveDecline: !!permissions.canApprove,
      isSachbearbeitungApp,
      allowTypes,
      stepsFlow,
      dokuments,
      kommentare,
      requiredDocumentTypes,
      readonly,
      loading,
      gesuchStatus: gesuch?.gesuchStatus,
    };
  });

  customDokumenteViewSig = computed(() => {
    const {
      allowTypes,
      gesuchId,
      permissions,
      trancheSetting,
      trancheId,
      readonly,
      gesuch,
      config: { isSachbearbeitungApp },
    } = this.gesuchViewSig();

    const { dokuments, requiredDocumentTypes, loading } =
      this.dokumentsStore.customDokumenteViewSig();

    const kommentare = this.dokumentsStore.kommentareViewSig();

    return {
      gesuchId,
      trancheId,
      permissions,
      trancheSetting: trancheSetting ?? undefined,
      canApproveDecline: !!permissions.canApprove,
      isSachbearbeitungApp,
      allowTypes,
      dokuments,
      kommentare,
      requiredDocumentTypes,
      readonly,
      loading,
      gesuchStatus: gesuch?.gesuchStatus,
      showList:
        dokuments.length > 0 ||
        requiredDocumentTypes.length > 0 ||
        isSachbearbeitungApp,
    };
  });

  DokumentStatus = Dokumentstatus;

  // show button to inform the GS that documents are missing (or declined)
  // if true, the button is shown
  canSBSendMissingDocumentsSig = computed(() => {
    const hasDokumenteToUebermitteln =
      !!this.dokumentsStore.dokumenteCanFlagsSig()
        .sbCanFehlendeDokumenteUebermitteln;
    const { permissions } = this.gesuchViewSig();

    return hasDokumenteToUebermitteln && permissions.canWrite;
  });

  canGSSendMissingDocumentsSig = computed(() => {
    return !!this.dokumentsStore.dokumenteCanFlagsSig()
      .gsCanDokumenteUebermitteln;
  });

  canCreateCustomDokumentTypSig = computed(() => {
    const { permissions } = this.gesuchViewSig();

    return !!permissions.canWrite;
  });

  // set the gesuch status to from "WARTEN_AUF_UNTERSCHRIFTENBLATT" to "VERSANDBEREIT"
  canSetToAdditionalDokumenteErhaltenSig = computed(() => {
    const { permissions } = this.gesuchViewSig();
    const { unterschriftenblaetter, requiredDocumentTypes } =
      this.additionalDokumenteViewSig();

    const hasUnterschriftenblatt =
      requiredDocumentTypes.length === 0 && unterschriftenblaetter.length > 0;

    return permissions.canUploadUnterschriftenblatt && hasUnterschriftenblatt;
  });

  constructor() {
    getLatestGesuchIdFromGesuch$(this.gesuchViewSig)
      .pipe(
        takeUntilDestroyed(),
        filter(() => this.config.isSachbearbeitungApp),
      )
      .subscribe((gesuchId) => {
        this.dokumentsStore.getAdditionalDokumente$({
          gesuchId,
        });
      });
    getLatestTrancheIdFromGesuch$(this.gesuchViewSig)
      .pipe(takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
          gesuchTrancheId,
          ignoreCache: true,
        });
      });

    effect(() => {
      if (
        this.config.isSachbearbeitungApp &&
        this.dokumentsStore.dokumenteCanFlagsSig().sbCanBearbeitungAbschliessen
      ) {
        this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
      }
    });

    this.store.dispatch(SharedEventGesuchDokumente.init());
  }

  dokumentAkzeptieren(dokument: SharedModelTableDokument) {
    const gesuchTrancheId = this.gesuchViewSig().trancheId;

    if (!dokument?.gesuchDokument?.id || !gesuchTrancheId) return;

    this.dokumentsStore.gesuchDokumentAkzeptieren$({
      gesuchDokumentId: dokument?.gesuchDokument.id,
      onSuccess: () => {
        this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
          gesuchTrancheId,
        });
      },
    });
  }

  dokumentAblehnen(document: SharedModelTableDokument) {
    const { trancheId: gesuchTrancheId } = this.gesuchViewSig();
    const gesuchDokumentId =
      document.dokumentOptions.dokument.gesuchDokument?.id;
    if (!gesuchTrancheId || !gesuchDokumentId) return;

    const dialogRef = this.dialog.open<
      SharedUiRejectDokumentComponent,
      SharedModelGesuchDokument,
      RejectDokument
    >(SharedUiRejectDokumentComponent);

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentsStore.gesuchDokumentAblehnen$({
            gesuchTrancheId: gesuchTrancheId,
            kommentar: result.kommentar,
            gesuchDokumentId,
            onSuccess: () => {
              this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
                gesuchTrancheId,
              });
            },
          });
        }
      });
  }

  getGesuchDokumentKommentare(dokument: SharedModelTableDokument) {
    const { trancheId } = this.gesuchViewSig();
    const gesuchDokumentId = dokument.gesuchDokument?.id;
    if (!trancheId || !gesuchDokumentId) return;

    this.dokumentsStore.getGesuchDokumentKommentare$({
      gesuchDokumentId: gesuchDokumentId,
      gesuchTrancheId: trancheId,
    });
  }

  deleteCustomDokumentTyp(dokument: SharedModelTableCustomDokument) {
    const { gesuchId, trancheId } = this.gesuchViewSig();

    if (!gesuchId || !trancheId) return;

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.dokumente.deleteCustomDokumentTyp.title',
      message: 'shared.dokumente.deleteCustomDokumentTyp.message',
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentsStore.deleteCustomDokumentTyp$({
            customDokumentTypId: dokument.dokumentTyp.id,
            onSuccess: () => {
              this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
                gesuchTrancheId: trancheId,
              });
            },
          });
        }
      });
  }

  fehlendeDokumenteEinreichen() {
    const { trancheId, trancheSetting } = this.gesuchViewSig();

    if (trancheId && trancheSetting) {
      this.dokumentsStore.fehlendeDokumenteEinreichen$({
        trancheId,
        tranchenTyp: trancheSetting.type,
        onSuccess: () => {
          // Reload gesuch because the status has changed
          this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
          // Also load the required documents again
          this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
            gesuchTrancheId: trancheId,
          });
        },
      });
    }
  }

  fehlendeDokumenteUebermitteln() {
    const { trancheId, trancheSetting } = this.gesuchViewSig();

    if (trancheId && trancheSetting) {
      this.dokumentsStore.fehlendeDokumenteUebermitteln$({
        trancheId,
        trancheTyp: trancheSetting.type,
        onSuccess: () => {
          // Reload gesuch because the status has changed
          this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
          // Also load the required documents again
          this.dokumentsStore.getDocumentsToUpload$({
            gesuchTrancheId: trancheId,
          });
        },
      });
    }
  }

  setToAdditionalDokumenteErhalten() {
    const { trancheId: gesuchTrancheId } = this.gesuchViewSig();
    if (!gesuchTrancheId) return;

    this.dokumentsStore.setToAdditionalDokumenteErhalten$({
      gesuchTrancheId,
      onSuccess: () => {
        this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
      },
    });
  }

  createCustomDokumentTyp() {
    CreateCustomDokumentDialogComponent.open(this.dialog)
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          const { trancheId } = this.gesuchViewSig();
          if (trancheId) {
            this.dokumentsStore.createCustomDokumentTyp$({
              trancheId,
              type: result.name,
              description: result.kommentar,
              onSuccess: () => {
                this.dokumentsStore.getGesuchDokumenteAndDocumentsToUpload$({
                  gesuchTrancheId: trancheId,
                });
              },
            });
          }
        }
      });
  }

  reloadGesuch() {
    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
  }

  handleContinue() {
    const { gesuchId } = this.gesuchViewSig();
    if (gesuchId) {
      this.store.dispatch(
        SharedEventGesuchDokumente.nextTriggered({
          id: gesuchId,
          origin: DOKUMENTE,
        }),
      );
    }
  }
}
