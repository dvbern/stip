import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedModelTableCustomDokument } from '@dv/shared/model/dokument';
import { CustomDokumentTyp, GesuchDokument } from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
import {
  SharedPatternDocumentUploadComponent,
  createCustomDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'dv-custom-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl: './custom-dokumente.component.html',
  styleUrl: './custom-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomDokumenteComponent {
  dokumenteViewSig = input.required<{
    trancheId: string | undefined;
    gesuchId: string | undefined;
    allowTypes: string | undefined;
    dokuments: GesuchDokument[];
    singleUpload?: boolean;
    permissions: PermissionMap;
    requiredDocumentTypes: CustomDokumentTyp[];
    readonly: boolean;
  }>();

  displayedColumns = ['spacer', 'documentName', 'actions'];

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      trancheId,
      allowTypes,
      permissions,
      dokuments,
      readonly,
      singleUpload,
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
          readonly,
          initialDocuments: gesuchDokument.dokumente,
          singleUpload,
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
          readonly,
          singleUpload,
        }),
      })),
    ];
    return new MatTableDataSource<SharedModelTableCustomDokument>(list);
  });

  deleteCustomDokumentTyp() {
    // this.dokumentsStore.deleteCustomDokumentTyp(dokumentTyp);
  }

  trackByFn(_index: number, item: SharedModelTableCustomDokument) {
    return item.dokumentTyp?.id;
  }
}
