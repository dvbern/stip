import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
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
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedModelTableDokument } from '@dv/shared/model/dokument';
import { GesuchDokument } from '@dv/shared/model/gesuch';
import {
  DOKUMENTE,
  getFormStepByDocumentType,
} from '@dv/shared/model/gesuch-form';
import {
  DOKUMENT_TYP_TO_DOCUMENT_OPTIONS,
  SharedPatternDocumentUploadComponent,
  createDocumentOptions,
} from '@dv/shared/pattern/document-upload';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiPrefixAppTypePipe } from '@dv/shared/ui/prefix-app-type';
import {
  RejectDokument,
  SharedUiRejectDokumentComponent,
} from '@dv/shared/ui/reject-dokument';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { getLatestGesuchIdFromGesuch$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';

@Component({
  selector: 'dv-shared-feature-gesuch-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    SharedUiLoadingComponent,
    TranslateModule,
    MatTableModule,
    SharedUiStepFormButtonsComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiBadgeComponent,
    SharedUiIconBadgeComponent,
    SharedUiIfSachbearbeiterDirective,
    SharedUiPrefixAppTypePipe,
    TypeSafeMatCellDefDirective,
    MatExpansionPanel,
  ],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  animations: [
    trigger('detailExpand', [
      state('collapsed,void', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)'),
      ),
    ]),
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {
  private store = inject(Store);
  private stepManager = inject(SharedUtilGesuchFormStepManagerService);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  public dokumentsStore = inject(DokumentsStore);

  detailColumns = ['expandedDetail'];

  displayedColumns = [
    'expander',
    'documentName',
    'formStep',
    'status',
    'actions',
  ];

  expandedRowId: string | null = null;

  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  stepViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  canSendMissingDocumentsSig = computed(() => {
    const hasAbgelehnteDokuments =
      this.dokumentsStore.hasAbgelehnteDokumentsSig();
    const isInCorrectState =
      this.gesuchViewSig().gesuch?.gesuchStatus !== 'FEHLENDE_DOKUMENTE';

    return hasAbgelehnteDokuments && isInCorrectState;
  });

  dokumenteDataSourceSig = computed(() => {
    const documents = this.dokumentsStore.dokumenteViewSig().dokuments;
    const requiredDocumentTypes =
      this.dokumentsStore.dokumenteViewSig().requiredDocumentTypes;
    const gesuchId = this.gesuchViewSig().gesuchId;
    const readonly = this.gesuchViewSig().readonly;
    const allowTypes = this.gesuchViewSig().allowTypes;
    const stepsFlow = this.stepViewSig().stepsFlow;

    if (!gesuchId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableDokument>([]);
    }

    const uploadedDocuments: SharedModelTableDokument[] = documents.map(
      (document) => {
        const dokumentTyp = document.dokumentTyp;

        if (!dokumentTyp) {
          throw new Error('Document type is missing');
        }

        const documentOptions = createDocumentOptions({
          gesuchId,
          allowTypes,
          dokumentTyp,
          initialDocuments: document.dokumente,
          readonly,
        });

        const formStep = getFormStepByDocumentType(dokumentTyp);

        return {
          dokumentTyp,
          gesuchDokument: document,
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
          gesuchId,
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

    return new MatTableDataSource<SharedModelTableDokument>(
      [...uploadedDocuments, ...missingDocuments].sort((a, b) =>
        this.stepManager.compareStepsByFlow(
          stepsFlow,
          a.formStep,
          b.formStep,
          () => a.dokumentTyp.localeCompare(b.dokumentTyp),
        ),
      ),
    );
  });

  trackByFn(_index: number, item: SharedModelTableDokument) {
    return item?.gesuchDokument?.id ?? item.dokumentTyp;
  }

  constructor() {
    getLatestGesuchIdFromGesuch$(this.gesuchViewSig)
      .pipe(takeUntilDestroyed())
      .subscribe((gesuchId) => {
        this.dokumentsStore.getDokumenteAndRequired$(gesuchId);
      });

    this.store.dispatch(SharedEventGesuchDokumente.init());
  }

  dokumentAkzeptieren(document: SharedModelTableDokument) {
    const gesuchId = this.gesuchViewSig().gesuchId;

    if (!document?.gesuchDokument?.id || !gesuchId) return;

    this.dokumentsStore.gesuchDokumentAkzeptieren$({
      gesuchDokumentId: document.gesuchDokument.id,
      gesuchId,
    });
  }

  dokumentAblehnen(document: SharedModelTableDokument) {
    const { gesuchId } = this.gesuchViewSig();

    if (!gesuchId) return;

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
            gesuchDokumentId: result.id,
            gesuchId,
            kommentar: result.kommentar,
          });
        }
      });
  }

  expandRow(dokument: SharedModelTableDokument) {
    if (this.expandedRowId === dokument.dokumentTyp) {
      this.expandedRowId = null;
    } else {
      this.expandedRowId = dokument.dokumentTyp;
    }
  }

  fehlendeDokumenteUebermitteln() {
    const { gesuchId } = this.gesuchViewSig();

    if (gesuchId) {
      this.dokumentsStore.fehlendeDokumenteUebermitteln$({
        gesuchId,
        onSuccess: () => {
          // Reload gesuch because the status has changed
          this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
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
