import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
  output,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedModelTableCustomDokument,
  SharedModelTableDokument,
} from '@dv/shared/model/dokument';
import {
  CustomDokumentTyp,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
} from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
import {
  SharedPatternDocumentUploadComponent,
  createCustomDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { detailExpand } from '@dv/shared/ui/animations';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { RemoteData } from '@dv/shared/util/remote-data';

import { DokumentStatusActionsComponent } from '../dokument-status-actions/dokument-status-actions.component';

@Component({
  selector: 'dv-custom-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    MatTooltipModule,
    DokumentStatusActionsComponent,
    SharedUiIfSachbearbeiterDirective,
  ],
  templateUrl: './custom-dokumente.component.html',
  styleUrl: './custom-dokumente.component.scss',
  animations: [detailExpand],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomDokumenteComponent {
  dokumenteViewSig = input.required<{
    trancheId: string | undefined;
    gesuchId: string | undefined;
    allowTypes: string | undefined;
    dokuments: GesuchDokument[];
    permissions: PermissionMap;
    isSachbearbeitungApp: boolean;
    requiredDocumentTypes: CustomDokumentTyp[];
    kommentare: RemoteData<GesuchDokumentKommentar[]>;
    readonly: boolean;
  }>();
  canCreateCustomDokumentTypSig = input.required<boolean>();

  getGesuchDokumentKommentare = output<SharedModelTableCustomDokument>();
  deleteCustomDokumentTyp = output<SharedModelTableCustomDokument>();
  dokumentAkzeptieren = output<SharedModelTableDokument>();
  dokumentAblehnen = output<SharedModelTableDokument>();
  createCustomDokumentTyp = output();

  detailColumns = ['kommentar'];
  displayedColumns = [
    'expander',
    'documentName',
    'description',
    'status',
    'actions',
  ];

  DokumentStatus = Dokumentstatus;
  expandedRowId: string | null = null;

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      trancheId,
      allowTypes,
      permissions,
      dokuments,
      requiredDocumentTypes,
    } = this.dokumenteViewSig();

    if (!gesuchId || !allowTypes || !trancheId) {
      return new MatTableDataSource<SharedModelTableCustomDokument>([]);
    }

    const uploadedDokuments = dokuments.map((gesuchDokument) => {
      if (!gesuchDokument.customDokumentTyp) {
        throw new Error('Custom Dokument Typ missing');
      }

      return {
        dokumentTyp: gesuchDokument.customDokumentTyp,
        gesuchDokument,
        dokumentOptions: createCustomDokumentOptions({
          gesuchId,
          trancheId,
          dokumentTyp: gesuchDokument.customDokumentTyp,
          allowTypes,
          gesuchDokument,
          permissions,
          initialDocuments: gesuchDokument.dokumente,
        }),
      } satisfies SharedModelTableCustomDokument;
    });
    const list = [
      ...uploadedDokuments,
      ...requiredDocumentTypes.map((dokumentTyp) => ({
        dokumentTyp: dokumentTyp,
        dokumentOptions: createCustomDokumentOptions({
          gesuchId,
          trancheId,
          allowTypes,
          dokumentTyp,
          permissions,
        }),
      })),
    ];
    return new MatTableDataSource<SharedModelTableCustomDokument>(list);
  });

  expandRow(dokument: SharedModelTableCustomDokument) {
    if (this.expandedRowId === dokument.dokumentTyp.id) {
      this.expandedRowId = null;
    } else {
      this.expandedRowId = dokument.dokumentTyp.id;
      this.getGesuchDokumentKommentare.emit(dokument);
    }
  }

  trackByFn(_index: number, item: SharedModelTableCustomDokument) {
    return item.dokumentTyp?.id;
  }
}
