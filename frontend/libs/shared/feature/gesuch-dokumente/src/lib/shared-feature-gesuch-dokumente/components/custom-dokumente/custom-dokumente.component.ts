import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslatePipe } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
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
    gesuchId: string | undefined;
    allowTypes: string | undefined;
    unterschriftenblaetter: UnterschriftenblattDokument[];
    singleUpload?: boolean;
    permissions: PermissionMap;
    readonly: boolean;
    requiredDocumentTypes: UnterschriftenblattDokumentTyp[];
  }>();
  public dokumentsStore = inject(DokumentsStore);

  displayedColumns = ['spacer', 'documentName', 'actions'];

  dokumenteDataSourceSig = computed(() => {
    const {
      gesuchId,
      unterschriftenblaetter,
      allowTypes,
      permissions,
      readonly,
      singleUpload,
      requiredDocumentTypes,
    } = this.dokumenteViewSig();

    if (!gesuchId || !allowTypes) {
      return new MatTableDataSource<SharedModelTableAdditionalDokument>([]);
    }

    const uploadedDokuments = unterschriftenblaetter.map(
      (gesuchDokument) =>
        ({
          dokumentTyp: gesuchDokument.dokumentTyp,
          gesuchDokument,
          dokumentOptions: createAdditionalDokumentOptions({
            gesuchId,
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

  deleteCustomDokumentTyp() {
    // this.dokumentsStore.deleteCustomDokumentTyp(dokumentTyp);
  }

  trackByFn(_index: number, item: SharedModelTableAdditionalDokument) {
    return item.dokumentTyp;
  }
}
