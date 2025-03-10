import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  output,
  signal,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedModelTableDokument,
  SharedModelTableRequiredDokument,
} from '@dv/shared/model/dokument';
import {
  DokumentTyp,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
  Gesuchstatus,
  TrancheSetting,
} from '@dv/shared/model/gesuch';
import {
  SharedModelGesuchFormStep,
  getFormStepByDocumentType,
} from '@dv/shared/model/gesuch-form';
import { PermissionMap } from '@dv/shared/model/permission-state';
import {
  DOKUMENT_TYP_TO_DOCUMENT_OPTIONS,
  SharedPatternDocumentUploadComponent,
  createGesuchDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { detailExpand } from '@dv/shared/ui/animations';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { RemoteData, isPending } from '@dv/shared/util/remote-data';

import { DokumentStatusActionsComponent } from '../dokument-status-actions/dokument-status-actions.component';

@Component({
  selector: 'dv-required-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiLoadingComponent,
    DokumentStatusActionsComponent,
  ],
  templateUrl: './required-dokumente.component.html',
  styleUrl: './required-dokumente.component.scss',
  animations: [detailExpand],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RequiredDokumenteComponent {
  private stepManager = inject(SharedUtilGesuchFormStepManagerService);
  dokumentStore = inject(DokumentsStore);

  dokumenteViewSig = input.required<{
    gesuchId: string | undefined;
    permissions: PermissionMap;
    trancheId: string | undefined;
    trancheSetting: TrancheSetting | undefined;
    isSachbearbeitungApp: boolean;
    allowTypes: string | undefined;
    stepsFlow: SharedModelGesuchFormStep[];
    dokuments: GesuchDokument[];
    kommentare: RemoteData<GesuchDokumentKommentar[]>;
    requiredDocumentTypes: DokumentTyp[];
    readonly: boolean;
    gesuchStatus?: Gesuchstatus;
  }>();
  getGesuchDokumentKommentare = output<SharedModelTableRequiredDokument>();
  dokumentAkzeptieren = output<SharedModelTableDokument>();
  dokumentAblehnen = output<SharedModelTableDokument>();

  detailColumns = ['kommentar'];
  displayedColumns = [
    'expander',
    'documentName',
    'formStep',
    'status',
    'actions',
  ];

  DokumentStatus = Dokumentstatus;

  expandedRowSig = signal<null | string>(null);

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
    } = this.dokumenteViewSig();

    if (!trancheId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableRequiredDokument>([]);
    }

    const uploadedDocuments: SharedModelTableRequiredDokument[] = dokuments.map(
      (gesuchDokument) => {
        const dokumentTyp = gesuchDokument.dokumentTyp;

        if (!dokumentTyp) {
          throw new Error('Document type is missing');
        }

        const dokumentOptions = createGesuchDokumentOptions({
          trancheId,
          permissions,
          allowTypes,
          dokumentTyp,
          gesuchDokument,
          initialDocuments: gesuchDokument.dokumente,
        });

        const formStep = getFormStepByDocumentType(dokumentTyp);

        return {
          dokumentTyp,
          gesuchDokument,
          kommentare: [],
          kommentarePending: false,
          formStep,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          dokumentOptions,
        };
      },
    );

    const missingDocuments: SharedModelTableRequiredDokument[] =
      requiredDocumentTypes.map((dokumentTyp) => {
        const formStep = getFormStepByDocumentType(dokumentTyp);

        const dokumentOptions = createGesuchDokumentOptions({
          trancheId,
          permissions,
          allowTypes,
          dokumentTyp,
          initialDocuments: [],
        });

        return {
          formStep,
          dokumentTyp,
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
            () => a.dokumentTyp.localeCompare(b.dokumentTyp),
          ),
        )
        .map((dokument) => ({
          ...dokument,
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
    effect(
      () => {
        const el = this.dokumentStore.expandedComponentList();

        if (el !== 'required') {
          this.expandedRowSig.set(null);
        }
      },
      { allowSignalWrites: true },
    );
  }

  trackByFn(_index: number, item: SharedModelTableRequiredDokument) {
    return item.dokumentTyp;
  }

  expandRow(dokument: SharedModelTableRequiredDokument) {
    const identifier = dokument.gesuchDokument?.id ?? dokument.dokumentTyp;

    if (this.expandedRowSig() === identifier) {
      this.expandedRowSig.set(null);
    } else {
      this.dokumentStore.setExpandedList('required');
      this.expandedRowSig.set(identifier);
      this.getGesuchDokumentKommentare.emit(dokument);
    }
  }
}
