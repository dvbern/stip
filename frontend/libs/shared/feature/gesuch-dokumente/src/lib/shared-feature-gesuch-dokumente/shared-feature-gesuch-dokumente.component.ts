import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedModelTableDokument } from '@dv/shared/model/dokument';
import {
  DokumentTyp,
  Dokumentstatus,
  GesuchDokument,
} from '@dv/shared/model/gesuch';
import {
  DOKUMENTE,
  SharedModelGesuchFormStep,
  getFormStepByDocumentType,
} from '@dv/shared/model/gesuch-form';
import {
  DOKUMENT_TYP_TO_DOCUMENT_OPTIONS,
  SharedPatternDocumentUploadComponent,
  createDocumentOptions,
} from '@dv/shared/pattern/document-upload';
import { detailExpand } from '@dv/shared/ui/animations';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import {
  SharedUiIfGesuchstellerDirective,
  SharedUiIfSachbearbeiterDirective,
} from '@dv/shared/ui/if-app-type';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiPrefixAppTypePipe } from '@dv/shared/ui/prefix-app-type';
import {
  RejectDokument,
  SharedUiRejectDokumentComponent,
} from '@dv/shared/ui/reject-dokument';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { getLatestTrancheIdFromGesuch$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';

@Component({
  selector: 'dv-shared-feature-gesuch-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    SharedUiLoadingComponent,
    TranslatePipe,
    MatTableModule,
    SharedUiStepFormButtonsComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiIconBadgeComponent,
    SharedUiIfGesuchstellerDirective,
    SharedUiIfSachbearbeiterDirective,
    SharedUiPrefixAppTypePipe,
    TypeSafeMatCellDefDirective,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  animations: [detailExpand],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {
  private store = inject(Store);
  private stepManager = inject(SharedUtilGesuchFormStepManagerService);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  public dokumentsStore = inject(DokumentsStore);

  detailColumns = ['kommentar'];
  displayedColumns = [
    'expander',
    'documentName',
    'formStep',
    'status',
    'actions',
  ];

  DokumentStatus = Dokumentstatus;
  expandedRowId: string | null = null;

  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  stepViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);

  canSendMissingDocumentsSig = computed(() => {
    const hasAbgelehnteDokuments =
      this.dokumentsStore.hasAbgelehnteDokumentsSig();
    const isInCorrectState =
      this.gesuchViewSig().gesuch?.gesuchStatus === 'IN_BEARBEITUNG_SB';

    return hasAbgelehnteDokuments && isInCorrectState;
  });

  dokumenteDataSourceSig = computed(() => {
    const { dokuments, requiredDocumentTypes } =
      this.dokumentsStore.dokumenteViewSig();

    const { readonly, trancheSetting, gesuchId } = this.gesuchViewSig();

    const trancheId = this.gesuchViewSig().trancheId;

    const allowTypes = this.gesuchViewSig().allowTypes;
    const stepsFlow = this.stepViewSig().stepsFlow;

    if (!trancheId || !allowTypes || !gesuchId) {
      return new MatTableDataSource<
        SharedModelTableDokument & {
          formStep: SharedModelGesuchFormStep & {
            routes: string[];
          };
        }
      >([]);
    }

    const uploadedDocuments: SharedModelTableDokument[] = dokuments.map(
      (gesuchDokument) => {
        const dokumentTyp = gesuchDokument.dokumentTyp;

        if (!dokumentTyp) {
          throw new Error('Document type is missing');
        }

        const documentOptions = createDocumentOptions({
          trancheId,
          allowTypes,
          dokumentTyp,
          gesuchDokument,
          initialDocuments: gesuchDokument.dokumente,
          readonly,
        });

        const formStep = getFormStepByDocumentType(dokumentTyp);

        return {
          dokumentTyp,
          gesuchDokument,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          documentOptions,
        };
      },
    );

    const missingDocuments: SharedModelTableDokument[] =
      requiredDocumentTypes.map((dokumentTyp) => {
        const formStep = getFormStepByDocumentType(dokumentTyp);

        const documentOptions = createDocumentOptions({
          trancheId,
          allowTypes,
          dokumentTyp,
          initialDocuments: [],
          readonly,
        });

        return {
          dokumentTyp,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          documentOptions,
        };
      });

    return new MatTableDataSource<
      SharedModelTableDokument & {
        formStep: SharedModelGesuchFormStep & {
          routes: string[];
        };
      }
    >(
      [...uploadedDocuments, ...missingDocuments]
        .sort((a, b) =>
          this.stepManager.compareStepsByFlow(
            stepsFlow,
            a.formStep,
            b.formStep,
            () => a.dokumentTyp.localeCompare(b.dokumentTyp),
          ),
        )
        .map((dokument) => ({
          ...dokument,
          formStep: {
            ...dokument.formStep,
            routes: [
              '/',
              'gesuch',
              ...dokument.formStep.route.split('/'),
              gesuchId,
              ...(trancheSetting?.routesSuffix ?? []),
            ],
          },
        })),
    );
  });

  constructor() {
    getLatestTrancheIdFromGesuch$(this.gesuchViewSig)
      .pipe(takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.dokumentsStore.getDokumenteAndRequired$({
          gesuchTrancheId,
          ignoreCache: true,
        });
      });

    this.store.dispatch(SharedEventGesuchDokumente.init());
  }

  trackByFn(_index: number, item: SharedModelTableDokument) {
    return item.dokumentTyp;
  }

  dokumentAkzeptieren(document: SharedModelTableDokument) {
    const gesuchTrancheId = this.gesuchViewSig().trancheId;

    if (!document?.gesuchDokument?.id || !gesuchTrancheId) return;

    this.dokumentsStore.gesuchDokumentAkzeptieren$({
      gesuchDokumentId: document.gesuchDokument.id,
      afterSuccess: () => {
        this.dokumentsStore.getDokumenteAndRequired$({ gesuchTrancheId });
      },
    });
  }

  dokumentAblehnen(document: SharedModelTableDokument) {
    const { trancheId: gesuchTrancheId } = this.gesuchViewSig();

    if (!gesuchTrancheId) return;

    const dialogRef = this.dialog.open<
      SharedUiRejectDokumentComponent,
      GesuchDokument,
      RejectDokument
    >(SharedUiRejectDokumentComponent, {
      data: document.gesuchDokument,
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentsStore.gesuchDokumentAblehnen$({
            gesuchTrancheId: gesuchTrancheId,
            kommentar: result.kommentar,
            gesuchDokumentId: result.id,
            dokumentTyp: document.dokumentTyp as DokumentTyp,
            afterSuccess: () => {
              this.dokumentsStore.getDokumenteAndRequired$({ gesuchTrancheId });
            },
          });
        }
      });
  }

  expandRow(dokument: SharedModelTableDokument) {
    if (this.expandedRowId === dokument.dokumentTyp) {
      this.expandedRowId = null;
    } else {
      this.expandedRowId = dokument.dokumentTyp;
      this.getGesuchDokumentKommentare(dokument);
    }
  }

  getGesuchDokumentKommentare(dokument: SharedModelTableDokument) {
    const { trancheId } = this.gesuchViewSig();
    if (!trancheId) return;

    this.dokumentsStore.getGesuchDokumentKommentare$({
      dokumentTyp: dokument.dokumentTyp as DokumentTyp,
      gesuchTrancheId: trancheId,
    });
  }

  fehlendeDokumenteEinreichen() {
    const { gesuchId, trancheId } = this.gesuchViewSig();

    if (gesuchId && trancheId) {
      this.dokumentsStore.fehlendeDokumenteEinreichen$({
        trancheId,
        onSuccess: () => {
          // Reload gesuch because the status has changed
          this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
        },
      });
    }
  }

  fehlendeDokumenteUebermitteln() {
    const { trancheId } = this.gesuchViewSig();

    if (trancheId) {
      this.dokumentsStore.fehlendeDokumenteUebermitteln$({
        trancheId,
        onSuccess: () => {
          // Reload gesuch because the status has changed
          this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
          // Also load the required documents again
          this.dokumentsStore.getRequiredDocumentTypes$(trancheId);
        },
      });
    }
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
