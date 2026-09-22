import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  output,
  signal,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedModelTableCustomDokument,
  SharedModelTableDokument,
} from '@dv/shared/model/dokument';
import {
  CustomDokumentTyp,
  Dokumentstatus,
  GesuchDokument,
} from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
import { THREE_LINE_CHARS_COUNT } from '@dv/shared/model/ui-constants';
import {
  SharedPatternDocumentUploadComponent,
  createCustomDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { detailExpand } from '@dv/shared/ui/animations';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiInfoDialogComponent } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  RowExpansionOverrides,
  TypeSafeMatCellDefDirective,
  isExpanded,
  toggleRowFn,
} from '@dv/shared/ui/table-helper';

import { DokumentStatusActionsComponent } from '../dokument-status-actions/dokument-status-actions.component';

@Component({
  selector: 'dv-custom-dokumente',
  imports: [
    CommonModule,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiLoadingComponent,
    MatTooltipModule,
    DokumentStatusActionsComponent,
    SharedUiIfSachbearbeiterDirective,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './custom-dokumente.component.html',
  styleUrl: './custom-dokumente.component.scss',
  animations: [detailExpand],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomDokumenteComponent {
  dokumentStore = inject(DokumentsStore);
  dialog = inject(MatDialog);

  dokumenteViewSig = input.required<{
    trancheId: string | undefined;
    gesuchId: string | undefined;
    allowTypes: string | undefined;
    dokuments: GesuchDokument[];
    permissions: PermissionMap;
    canApproveDecline: boolean;
    isSachbearbeitungApp: boolean;
    customDocumentTypes: CustomDokumentTyp[];
    loading: boolean;
    readonly: boolean;
  }>();
  canCreateCustomDokumentTypSig = input.required<boolean>();

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

  rowExpansionOverrides = signal<RowExpansionOverrides>({});

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      trancheId,
      allowTypes,
      permissions,
      dokuments,
      customDocumentTypes,
      isSachbearbeitungApp,
      readonly,
    } = this.dokumenteViewSig();

    if (!gesuchId || !allowTypes || !trancheId) {
      return new MatTableDataSource<SharedModelTableCustomDokument>([]);
    }

    const overrides = this.rowExpansionOverrides();

    const uploadedDokuments = dokuments.map((gesuchDokument) => {
      if (!gesuchDokument.customDokumentTyp) {
        throw new Error('Custom Dokument Typ missing');
      }

      const hasFiles = gesuchDokument.dokumente.length > 0;
      const canDelete = isSachbearbeitungApp && !hasFiles && !readonly;
      return {
        dokumentTyp: gesuchDokument.customDokumentTyp,
        gesuchDokument,
        canDelete,
        isExpanded: true,
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
      ...customDocumentTypes.map((dokumentTyp) => ({
        dokumentTyp: dokumentTyp,
        canDelete: false,
        gesuchDokument: undefined,
        isExpanded: true,
        dokumentOptions: createCustomDokumentOptions({
          gesuchId,
          trancheId,
          allowTypes,
          dokumentTyp,
          permissions,
        }),
      })),
    ]
      .map((dokument) => ({
        ...dokument,
        isExpanded: isExpanded(dokument.gesuchDokument, overrides),
        hasLongDescription:
          dokument.dokumentTyp.description.length > THREE_LINE_CHARS_COUNT,
      }))
      .sort((a, b) => {
        const typeA = a.gesuchDokument?.customDokumentTyp?.type ?? 'none';
        const typeB = b.gesuchDokument?.customDokumentTyp?.type ?? 'none';
        return typeA.localeCompare(typeB);
      });
    return new MatTableDataSource<SharedModelTableCustomDokument>(list);
  });

  showDescription(title: string, message: string) {
    SharedUiInfoDialogComponent.open(this.dialog, {
      data: {
        type: 'plain',
        title,
        message,
      },
    });
  }

  toggleRow(tableDok: SharedModelTableCustomDokument) {
    toggleRowFn(tableDok.gesuchDokument, this.rowExpansionOverrides);
  }

  trackByFn(_index: number, item: SharedModelTableCustomDokument) {
    return item.dokumentTyp?.id;
  }
}
