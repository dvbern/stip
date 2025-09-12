import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslocoPipe } from '@jsverse/transloco';

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
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'dv-additional-dokumente',
  imports: [
    TranslocoPipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
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
    permissions: PermissionMap;
    readonly: boolean;
    loading: boolean;
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
            initialDocuments: gesuchDokument?.dokumente,
          }),
        }) satisfies SharedModelTableAdditionalDokument,
    );

    const list = [
      ...uploadedDokuments,
      ...requiredDocumentTypes
        .filter(
          (dokumentTyp) =>
            !uploadedDokuments.some((u) => u.dokumentTyp === dokumentTyp),
        )
        .map((dokumentTyp) => ({
          dokumentTyp: dokumentTyp,
          dokumentOptions: createAdditionalDokumentOptions({
            gesuchId,
            trancheId,
            allowTypes,
            dokumentTyp,
            permissions,
          }),
        })),
    ];
    return new MatTableDataSource<SharedModelTableAdditionalDokument>(list);
  });

  trackByFn(_index: number, item: SharedModelTableAdditionalDokument) {
    return item.dokumentTyp;
  }
}
