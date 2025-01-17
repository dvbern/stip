import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedModelTableAdditionalDokument } from '@dv/shared/model/dokument';
import {
  UnterschriftenblattDokument,
  UnterschriftenblattDokumentTyp,
} from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
import {
  SharedPatternDocumentUploadComponent,
  createAdditionalDokumentOptions,
} from '@dv/shared/pattern/document-upload';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'dv-additional-dokumente',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl: './additional-dokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdditionalDokumenteComponent {
  dokumenteViewSig = input.required<{
    gesuchId: string | undefined;
    trancheId: string | undefined;
    allowTypes: string | undefined;
    unterschriftenblaetter: UnterschriftenblattDokument[];
    singleUpload?: boolean;
    permissions: PermissionMap;
    readonly: boolean;
    requiredDocumentTypes: UnterschriftenblattDokumentTyp[];
  }>();

  displayedColumns = ['spacer', 'documentName', 'actions'];

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      trancheId,
      unterschriftenblaetter,
      allowTypes,
      permissions,
      readonly,
      singleUpload,
      requiredDocumentTypes,
    } = this.dokumenteViewSig();

    if (!gesuchId || !trancheId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableAdditionalDokument>([]);
    }

    const uploadedDokuments = unterschriftenblaetter.map(
      (gesuchDokument) =>
        ({
          dokumentTyp: gesuchDokument.dokumentTyp,
          gesuchDokument,
          dokumentOptions: createAdditionalDokumentOptions({
            gesuchId,
            trancheId,
            dokumentTyp: gesuchDokument.dokumentTyp,
            allowTypes,
            gesuchDokument,
            permissions,
            readonly,
            initialDocuments: gesuchDokument?.dokumente,
            singleUpload,
          }),
        }) satisfies SharedModelTableAdditionalDokument,
    );
    const list = [
      ...uploadedDokuments,
      ...requiredDocumentTypes.map((dokumentTyp) => ({
        dokumentTyp: dokumentTyp,
        dokumentOptions: createAdditionalDokumentOptions({
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
    return new MatTableDataSource<SharedModelTableAdditionalDokument>(list);
  });

  trackByFn(_index: number, item: SharedModelTableAdditionalDokument) {
    return item.dokumentTyp;
  }
}
