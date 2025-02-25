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

import { SharedModelTableGesuchDokument } from '@dv/shared/model/dokument';
import {
  DokumentTyp,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
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
import { detailExpand } from '@dv/shared/ui/animations';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiPrefixAppTypePipe } from '@dv/shared/ui/prefix-app-type';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { RemoteData } from '@dv/shared/util/remote-data';

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
    SharedUiIconBadgeComponent,
    SharedUiRdIsPendingPipe,
    SharedUiPrefixAppTypePipe,
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
    stepsFlow: GesuchFormStep[];
    dokuments: GesuchDokument[];
    kommentare: RemoteData<GesuchDokumentKommentar[]>;
    requiredDocumentTypes: DokumentTyp[];
    readonly: boolean;
  }>();
  getGesuchDokumentKommentare = output<SharedModelTableGesuchDokument>();
  fehlendeDokumenteUebermitteln = output<SharedModelTableGesuchDokument>();
  dokumentAkzeptieren = output<SharedModelTableGesuchDokument>();
  dokumentAblehnen = output<SharedModelTableGesuchDokument>();

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
      readonly,
    } = this.dokumenteViewSig();

    if (!trancheId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableGesuchDokument>([]);
    }

    const uploadedDocuments: SharedModelTableGesuchDokument[] = dokuments.map(
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
          readonly,
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

    const missingDocuments: SharedModelTableGesuchDokument[] =
      requiredDocumentTypes.map((dokumentTyp) => {
        const formStep = getFormStepByDocumentType(dokumentTyp);

        const dokumentOptions = createGesuchDokumentOptions({
          trancheId,
          permissions,
          allowTypes,
          dokumentTyp,
          initialDocuments: [],
          readonly,
        });

        return {
          formStep,
          dokumentTyp,
          titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
          dokumentOptions,
        };
      });

    return new MatTableDataSource<SharedModelTableGesuchDokument>(
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

  trackByFn(_index: number, item: SharedModelTableGesuchDokument) {
    return item.dokumentTyp;
  }

  expandRow(dokument: SharedModelTableGesuchDokument) {
    if (this.expandedRowId === dokument.dokumentTyp) {
      this.expandedRowId = null;
    } else {
      this.expandedRowId = dokument.dokumentTyp;
      this.getGesuchDokumentKommentare.emit(dokument);
    }
  }
}
