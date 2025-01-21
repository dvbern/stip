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
import { TranslatePipe } from '@ngx-translate/core';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedModelGesuchDokument,
  UploadView,
} from '@dv/shared/model/dokument';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiPrefixAppTypePipe } from '@dv/shared/ui/prefix-app-type';
import {
  RejectDokument,
  SharedUiRejectDokumentComponent,
} from '@dv/shared/ui/reject-dokument';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
import { isInitial } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-shared-pattern-document-upload-approval',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiIconBadgeComponent,
    SharedUiPrefixAppTypePipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    TranslatePipe,
  ],
  templateUrl: './document-upload-approval.component.html',
  styleUrl: './document-upload-approval.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentUploadApprovalComponent implements OnInit, OnDestroy {
  uploadViewSig = input.required<UploadView>();
  public dokumentsStore = inject(DokumentsStore);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  isInitial = isInitial;

  public ngOnInit(): void {
    const { dokumentModel, hasEntries } = this.uploadViewSig();
    if (!hasEntries || dokumentModel.art !== 'GESUCH_DOKUMENT') return;
    // why is this needed?
    this.dokumentsStore.getGesuchDokument$({
      trancheId: dokumentModel.trancheId,
      dokumentTyp: dokumentModel.dokumentTyp,
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
    const { dokumentModel, hasEntries } = this.uploadViewSig();
    const gesuchDokumentId = this.dokumentsStore.dokumentViewSig()?.id;
    if (
      !gesuchDokumentId ||
      !hasEntries ||
      dokumentModel.art !== 'GESUCH_DOKUMENT'
    )
      return;

    const dialogRef = this.dialog.open<
      SharedUiRejectDokumentComponent,
      SharedModelGesuchDokument,
      RejectDokument
    >(SharedUiRejectDokumentComponent);

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.dokumentsStore.gesuchDokumentAblehnen$({
            gesuchTrancheId: dokumentModel.trancheId,
            dokumentTyp: dokumentModel.dokumentTyp,
            gesuchDokumentId,
            kommentar: result.kommentar,
            afterSuccess: () => {
              this.reloadDokumente();
            },
          });
        }
      });
  }

  private reloadDokumente() {
    const { dokumentModel, hasEntries, initialDokuments } =
      this.uploadViewSig();
    if (!hasEntries || dokumentModel.art !== 'GESUCH_DOKUMENT') return;

    if (initialDokuments) {
      this.dokumentsStore.getDokumenteAndRequired$({
        gesuchTrancheId: dokumentModel.trancheId,
      });
    }
    this.dokumentsStore.getGesuchDokument$({
      trancheId: dokumentModel.trancheId,
      dokumentTyp: dokumentModel.dokumentTyp,
    });
  }
}
