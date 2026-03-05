import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  input,
  output,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { SharedDialogEditDokumentenNachfristComponent } from '@dv/shared/dialog/edit-dokumenten-nachfrist';
import {
  SharedModelTableDokument,
  SharedModelTableRequiredDokument,
} from '@dv/shared/model/dokument';
import {
  DokumentTyp,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
  GesuchDokumentRef,
  GesuchTrancheStatus,
  Gesuchstatus,
  TrancheSetting,
} from '@dv/shared/model/gesuch';
import {
  GesuchFormStep,
  getFormStepByDocumentType,
} from '@dv/shared/model/gesuch-form';
import { PermissionMap } from '@dv/shared/model/permission-state';
import { assertUnreachable } from '@dv/shared/model/type-util';
import {
  DOKUMENT_TYP_TO_DOCUMENT_OPTIONS,
  SharedPatternDocumentUploadComponent,
  createGesuchDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { detailExpand } from '@dv/shared/ui/animations';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { RemoteData, isPending } from '@dv/shared/util/remote-data';

import { DokumentStatusActionsComponent } from '../dokument-status-actions/dokument-status-actions.component';

const interactionMapGesuch: Partial<Record<Gesuchstatus, boolean | undefined>> =
  {
    FEHLENDE_DOKUMENTE: true,
    BEREIT_FUER_BEARBEITUNG: false,
    IN_BEARBEITUNG_SB: true,
  };

const interactionMapAenderung: Partial<
  Record<GesuchTrancheStatus, boolean | undefined>
> = {
  FEHLENDE_DOKUMENTE: true,
};

type ExpandedRow =
  | {
      type: 'none';
    }
  | {
      type: 'id';
      id: string;
    }
  | {
      type: 'ref';
      dokumentTyp: DokumentTyp;
      entryId: string | undefined;
    };

@Component({
  selector: 'dv-required-dokumente',
  imports: [
    CommonModule,
    RouterLink,
    MatTableModule,
    MatTooltipModule,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    SharedPatternDocumentUploadComponent,
    DokumentStatusActionsComponent,
    SharedUiLoadingComponent,
    SharedUiIfSachbearbeiterDirective,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './required-dokumente.component.html',
  styleUrl: './required-dokumente.component.scss',
  animations: [detailExpand],
  providers: [provideDvDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RequiredDokumenteComponent {
  private stepManager = inject(SharedUtilGesuchFormStepManagerService);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  dokumentStore = inject(DokumentsStore);
  dokumenteViewSig = input.required<{
    gesuchId: string | undefined;
    nachfrist: string | undefined;
    permissions: PermissionMap;
    trancheId: string | undefined;
    trancheSetting: TrancheSetting | undefined;
    canApproveDecline: boolean;
    isSachbearbeitungApp: boolean;
    allowTypes: string | undefined;
    stepsFlow: GesuchFormStep[];
    dokuments: GesuchDokument[];
    kommentare: RemoteData<GesuchDokumentKommentar[]>;
    requiredDocumentTypes: DokumentTyp[];
    requiredDocumentRefs: GesuchDokumentRef[];
    readonly: boolean;
    loading: boolean;
    gesuchStatus?: Gesuchstatus;
    trancheStatus?: GesuchTrancheStatus;
  }>();
  getGesuchDokumentKommentare = output<SharedModelTableRequiredDokument>();
  dokumentAkzeptieren = output<SharedModelTableDokument>();
  dokumentAblehnen = output<SharedModelTableDokument>();
  reloadGesuch = output<unknown>();

  detailColumns = ['kommentar'];
  displayedColumns = [
    'expander',
    'documentName',
    'formStep',
    'status',
    'actions',
  ];

  DokumentStatus = Dokumentstatus;

  expandedRowSig = signal<ExpandedRow>({ type: 'none' });

  canEditNachfristSig = computed(() => {
    const { gesuchStatus, trancheSetting, trancheStatus } =
      this.dokumenteViewSig();
    const gesuchUrlTyp = trancheSetting?.gesuchUrlTyp;

    if (!gesuchUrlTyp || !gesuchStatus || !trancheStatus) {
      return undefined;
    }

    if (gesuchUrlTyp === 'AENDERUNG') {
      return interactionMapAenderung[trancheStatus as GesuchTrancheStatus];
    }

    return interactionMapGesuch[gesuchStatus];
  });

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      permissions,
      trancheId,
      trancheSetting,
      allowTypes,
      stepsFlow,
      dokuments,
      kommentare,
      requiredDocumentTypes,
      requiredDocumentRefs,
    } = this.dokumenteViewSig();

    if (!trancheId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableRequiredDokument>([]);
    }

    const expandedRow = this.expandedRowSig();

    const uploadedDocuments: SharedModelTableRequiredDokument[] = dokuments.map(
      (gesuchDokument) => {
        const dokumentTyp = gesuchDokument.dokumentTyp;

        if (!dokumentTyp) {
          throw new Error('Document type is missing');
        }

        const dokumentOptions = createGesuchDokumentOptions({
          trancheId,
          entryId: gesuchDokument.entryId,
          permissions,
          allowTypes,
          dokumentTyp,
          gesuchDokument,
          initialDocuments: gesuchDokument.dokumente,
        });

        const formStep = getFormStepByDocumentType(dokumentTyp);

        return {
          dokumentTyp,
          entryId: gesuchDokument.entryId,
          isExpanded: isExpanded(expandedRow, {
            id: gesuchDokument.id,
            dokumentTyp: dokumentTyp,
            entryId: gesuchDokument.entryId,
          }),
          gesuchDokument,
          kommentare: [],
          kommentarePending: false,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          dokumentOptions,
        };
      },
    );

    const missingDocuments: SharedModelTableRequiredDokument[] = [
      ...requiredDocumentTypes.map((dokumentTyp) => ({
        dokumentTyp,
        entryId: undefined,
      })),
      ...requiredDocumentRefs,
    ].map(({ dokumentTyp, entryId }) => {
      const formStep = getFormStepByDocumentType(dokumentTyp);

      const dokumentOptions = createGesuchDokumentOptions({
        trancheId,
        entryId,
        permissions,
        allowTypes,
        dokumentTyp,
        initialDocuments: [],
      });

      return {
        formStep,
        dokumentTyp,
        entryId,
        isExpanded: isExpanded(expandedRow, { dokumentTyp, entryId }),
        kommentare: [],
        kommentarePending: false,
        titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
        dokumentOptions,
      };
    });

    return new MatTableDataSource<SharedModelTableRequiredDokument>(
      [...uploadedDocuments, ...missingDocuments]
        .sort((a, b) =>
          this.stepManager.compareStepsByFlow(
            stepsFlow,
            a.formStep,
            b.formStep,
            () => {
              const compared = a.dokumentTyp.localeCompare(b.dokumentTyp);
              return compared === 0
                ? (a.gesuchDokument?.entryId?.localeCompare(
                    b.gesuchDokument?.entryId ?? '',
                  ) ?? 0)
                : compared;
            },
          ),
        )
        .map((dokument) => ({
          ...dokument,
          isExpanded: isExpanded(expandedRow, {
            id: dokument.gesuchDokument?.id,
            dokumentTyp: dokument.dokumentTyp,
            entryId: dokument.entryId,
          }),
          kommentarePending: isPending(kommentare),
          kommentare:
            kommentare.data?.filter(
              (k) => k.gesuchDokumentId === dokument.gesuchDokument?.id,
            ) ?? [],
          formStep: {
            ...dokument.formStep,
            routes: gesuchId
              ? [
                  '/',
                  'gesuch',
                  ...dokument.formStep.route.split('/'),
                  gesuchId,
                  ...(trancheSetting?.routesSuffix ?? []),
                ]
              : undefined,
          },
        })),
    );
  });

  constructor() {
    effect(() => {
      const el = this.dokumentStore.expandedComponentList();

      if (el !== 'required') {
        this.expandedRowSig.set({ type: 'none' });
      }
    });
  }

  trackByFn(_index: number, item: SharedModelTableRequiredDokument) {
    return item.dokumentTyp;
  }

  editNachfrist(gesuchId: string, nachfrist: string) {
    SharedDialogEditDokumentenNachfristComponent.open(this.dialog, nachfrist)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentStore.editNachfrist$({
            gesuchId,
            newNachfrist: result.newNachfrist,
            onSuccess: () => {
              this.reloadGesuch.emit({});
            },
          });
        }
      });
  }

  expandRow(dokument: SharedModelTableRequiredDokument) {
    const identifier: ExpandedRow = dokument.gesuchDokument?.id
      ? { type: 'id', id: dokument.gesuchDokument.id }
      : {
          type: 'ref',
          dokumentTyp: dokument.dokumentTyp,
          entryId: dokument.entryId,
        };

    if (JSON.stringify(this.expandedRowSig()) === JSON.stringify(identifier)) {
      this.expandedRowSig.set({ type: 'none' });
    } else {
      this.dokumentStore.setExpandedList('required');
      this.expandedRowSig.set(identifier);
      this.getGesuchDokumentKommentare.emit(dokument);
    }
  }
}

const isExpanded = (
  expandedRow: ExpandedRow,
  ref: { id?: string; dokumentTyp: DokumentTyp; entryId: string | undefined },
) => {
  switch (expandedRow.type) {
    case 'none':
      return false;
    case 'id':
      return ref.id === expandedRow.id;
    case 'ref':
      return (
        ref.dokumentTyp === expandedRow.dokumentTyp &&
        ref.entryId === expandedRow.entryId
      );
    default:
      assertUnreachable(expandedRow);
  }
};
