import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
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
  GesuchDokumentEntry,
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
  RowExpansionOverrides,
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
  isExpanded,
  toggleRowFn,
} from '@dv/shared/ui/table-helper';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';

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
    entrys: GesuchDokumentEntry[];
    requiredDocumentTypes: DokumentTyp[];
    requiredDocumentRefs: GesuchDokumentRef[];
    readonly: boolean;
    loading: boolean;
    gesuchStatus?: Gesuchstatus;
    trancheStatus?: GesuchTrancheStatus;
  }>();
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

  rowExpansionOverrides = signal<RowExpansionOverrides>({});

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
      entrys,
      requiredDocumentTypes,
      requiredDocumentRefs,
    } = this.dokumenteViewSig();

    if (!trancheId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableRequiredDokument>([]);
    }

    const overrides = this.rowExpansionOverrides();

    const uploadedDocuments: SharedModelTableRequiredDokument[] = dokuments.map(
      (gesuchDokument) => {
        const dokumentTyp = gesuchDokument.dokumentTyp;

        if (!dokumentTyp) {
          throw new Error('Document type is missing');
        }

        const entryId = gesuchDokument.entryId;
        const dokumentOptions = createGesuchDokumentOptions({
          trancheId,
          entryId,
          permissions,
          allowTypes,
          dokumentTyp,
          gesuchDokument,
          initialDocuments: gesuchDokument.dokumente,
        });

        const formStep = getFormStepByDocumentType(dokumentTyp);

        return {
          dokumentTyp,
          entryId,
          isExpanded: true,
          gesuchDokument,
          formStep,
          entryName: entrys.find(
            (e) =>
              e.dokumentTyps.includes(dokumentTyp) && e.entryId === entryId,
          )?.name,
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
        entryName: entrys.find(
          (e) => e.dokumentTyps.includes(dokumentTyp) && e.entryId === entryId,
        )?.name,
        isExpanded: true,
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
              return getEntryName(a).localeCompare(getEntryName(b));
            },
          ),
        )
        .map((dokument) => ({
          ...dokument,
          isExpanded: isExpanded(dokument.gesuchDokument, overrides),
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

  trackByFn(_index: number, item: SharedModelTableRequiredDokument) {
    return getEntryName(item);
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

  toggleRow(tableDok: SharedModelTableRequiredDokument) {
    toggleRowFn(tableDok.gesuchDokument, this.rowExpansionOverrides);
  }
}

const getEntryName = (item: {
  dokumentTyp: DokumentTyp;
  entryName?: string;
}) => {
  return `${item.dokumentTyp}_${item.entryName ?? ''}`;
};
