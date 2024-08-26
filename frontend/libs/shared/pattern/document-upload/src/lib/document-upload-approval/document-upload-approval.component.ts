import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnDestroy,
  OnInit,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { UploadView } from '@dv/shared/model/dokument';
import { GesuchDokument } from '@dv/shared/model/gesuch';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiPrefixAppTypePipe } from '@dv/shared/ui/prefix-app-type';
import {
  RejectDokument,
  SharedUiRejectDokumentComponent,
} from '@dv/shared/ui/reject-dokument';

@Component({
  selector: 'dv-shared-pattern-document-upload-approval',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiIconBadgeComponent,
    SharedUiPrefixAppTypePipe,
    TranslateModule,
  ],
  templateUrl: './document-upload-approval.component.html',
  styleUrl: './document-upload-approval.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentUploadApprovalComponent implements OnInit, OnDestroy {
  uploadViewSig = input.required<UploadView>();
  dokumentsStore = inject(DokumentsStore);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  public ngOnInit(): void {
    const { trancheId, type } = this.uploadViewSig();
    this.dokumentsStore.getGesuchDokument$({
      trancheId,
      dokumentTyp: type,
    });
  }

  public ngOnDestroy(): void {
    this.dokumentsStore.resetGesuchDokumentStateToInitial({});
  }

  dokumentAkzeptieren() {
    const dokumentId = this.dokumentsStore.dokumentViewSig()?.id;
    if (!dokumentId) return;

    this.dokumentsStore.gesuchDokumentAkzeptieren$({
      gesuchDokumentId: dokumentId,
      afterSuccess: () => {
        this.reloadDokumente();
      },
    });
  }

  dokumentAblehnen() {
    const dialogRef = this.dialog.open<
      SharedUiRejectDokumentComponent,
      GesuchDokument,
      RejectDokument
    >(SharedUiRejectDokumentComponent, {
      data: this.dokumentsStore.dokumentViewSig(),
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentsStore.gesuchDokumentAblehnen$({
            gesuchTrancheId: this.uploadViewSig().trancheId,
            dokumentTyp: this.uploadViewSig().type,
            gesuchDokumentId: result.id,
            kommentar: result.kommentar,
            afterSuccess: () => {
              this.reloadDokumente();
            },
          });
        }
      });
  }

  private reloadDokumente() {
    if (this.uploadViewSig().initialDocuments) {
      this.dokumentsStore.getDokumenteAndRequired$(
        this.uploadViewSig().trancheId,
      );
    }
    this.dokumentsStore.getGesuchDokument$({
      trancheId: this.uploadViewSig().trancheId,
      dokumentTyp: this.uploadViewSig().type,
    });
  }
}
