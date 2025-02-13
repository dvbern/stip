import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  output,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

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
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { RemoteData } from '@dv/shared/util/remote-data';

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
    SharedUiRdIsPendingPipe,
    DokumentStatusActionsComponent,
  ],
  templateUrl: './required-dokumente.component.html',
  styleUrl: './required-dokumente.component.scss',
  animations: [detailExpand],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RequiredDokumenteComponent {
  private stepManager = inject(SharedUtilGesuchFormStepManagerService);
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
  expandedRowId: string | null = null;

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      permissions,
      trancheId,
      trancheSetting,
      allowTypes,
      stepsFlow,
      dokuments,
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
    return item.dokumentTyp;
  }

  expandRow(dokument: SharedModelTableRequiredDokument) {
    if (this.expandedRowId === dokument.dokumentTyp) {
      this.expandedRowId = null;
    } else {
      this.expandedRowId = dokument.dokumentTyp;
      this.getGesuchDokumentKommentare.emit(dokument);
    }
  }
}
