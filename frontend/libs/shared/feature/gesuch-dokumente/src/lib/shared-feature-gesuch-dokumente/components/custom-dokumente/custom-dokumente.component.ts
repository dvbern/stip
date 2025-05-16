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
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
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
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { RemoteData, isPending } from '@dv/shared/util/remote-data';

import { DokumentStatusActionsComponent } from '../dokument-status-actions/dokument-status-actions.component';

@Component({
    selector: 'dv-custom-dokumente',
    imports: [
        CommonModule,
        TranslatePipe,
        MatTableModule,
        TypeSafeMatCellDefDirective,
        SharedPatternDocumentUploadComponent,
        SharedUiLoadingComponent,
        MatTooltipModule,
        DokumentStatusActionsComponent,
        SharedUiIfSachbearbeiterDirective,
    ],
    templateUrl: './custom-dokumente.component.html',
    styleUrl: './custom-dokumente.component.scss',
    animations: [detailExpand],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomDokumenteComponent {
  dokumentStore = inject(DokumentsStore);
  expandedRowSig = signal<string | null>(null);

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

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      trancheId,
      allowTypes,
      permissions,
      dokuments,
      kommentare,
      requiredDocumentTypes,
      isSachbearbeitungApp,
      readonly,
    } = this.dokumenteViewSig();

    if (!gesuchId || !allowTypes || !trancheId) {
      return new MatTableDataSource<SharedModelTableCustomDokument>([]);
    }

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
        kommentare: [],
        kommentarePending: false,
        showUpload: hasFiles || !isSachbearbeitungApp,
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
        canDelete: false,
        showUpload: !isSachbearbeitungApp,
        gesuchDokument: undefined,
        kommentare: [],
        kommentarePending: false,
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
        kommentarePending: isPending(kommentare),
        kommentare:
          kommentare.data?.filter(
            (k) => k.gesuchDokumentId === dokument.gesuchDokument?.id,
          ) ?? [],
      }))
      .sort((a, b) => {
        const typeA = a.gesuchDokument?.customDokumentTyp?.type ?? 'none';
        const typeB = b.gesuchDokument?.customDokumentTyp?.type ?? 'none';
        return typeA.localeCompare(typeB);
      });
    return new MatTableDataSource<SharedModelTableCustomDokument>(list);
  });

  constructor() {
    effect(
      () => {
        const el = this.dokumentStore.expandedComponentList();

        if (el !== 'custom') {
          this.expandedRowSig.set(null);
        }
      },
      { allowSignalWrites: true },
    );
  }

  expandRow(dokument: SharedModelTableCustomDokument) {
    const gesuchDokumentId = dokument.gesuchDokument?.id;
    if (!gesuchDokumentId) return;

    if (this.expandedRowSig() === gesuchDokumentId) {
      this.expandedRowSig.set(null);
    } else {
      this.dokumentStore.setExpandedList('custom');
      this.expandedRowSig.set(gesuchDokumentId);
      this.getGesuchDokumentKommentare.emit(dokument);
    }
  }

  trackByFn(_index: number, item: SharedModelTableCustomDokument) {
    return item.dokumentTyp?.id;
  }
}
