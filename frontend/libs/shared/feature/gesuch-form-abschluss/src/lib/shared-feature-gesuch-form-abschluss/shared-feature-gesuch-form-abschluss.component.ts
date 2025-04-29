import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';

@Component({
  selector: 'dv-shared-feature-gesuch-form-abschluss',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    SharedUiInfoContainerComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './shared-feature-gesuch-form-abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormAbschlussComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  destroyRef = inject(DestroyRef);

  einreichenStore = inject(EinreichenStore);
  dokumentsStore = inject(DokumentsStore);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);

  canGSSendMissingDocumentsSig = computed(() => {
    return !!this.dokumentsStore.dokumenteCanFlagsSig()
      .gsCanDokumenteUebermitteln;
  });

  dokumenteRouteSig = computed(() => {
    const { gesuchId, trancheSetting } = this.gesuchViewSig();
    if (!gesuchId || !trancheSetting) {
      return null;
    }
    return ['/gesuch', 'dokumente', gesuchId, ...trancheSetting.routesSuffix];
  });

  constructor() {
    getLatestTrancheIdFromGesuchOnUpdate$(this.gesuchViewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.dokumentsStore.getDokumenteAndRequired$({ gesuchTrancheId });
        this.einreichenStore.validateEinreichen$({
          gesuchTrancheId,
        });
      });
  }

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAbschluss.init());
  }

  abschliessen() {
    const { isEditingAenderung, gesuch, trancheId } = this.gesuchViewSig();
    if (!gesuch || !trancheId) {
      return;
    }
    const dialogRef = SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.abschluss.dialog.title',
      message: 'shared.form.abschluss.dialog.text',
      confirmText: 'shared.form.abschluss.abschliessen',
      cancelText: 'shared.cancel',
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          if (isEditingAenderung) {
            this.einreichenStore.aenderungEinreichen$({
              trancheId,
            });
          } else {
            this.einreichenStore.gesuchEinreichen$({
              gesuchTrancheId: trancheId,
            });
          }
        }
      });
  }

  fehlendeDokumenteEinreichen() {
    const { trancheId, trancheSetting } = this.gesuchViewSig();

    if (trancheId && trancheSetting) {
      this.dokumentsStore.fehlendeDokumenteEinreichen$({
        trancheId,
        tranchenTyp: trancheSetting.type,
        onSuccess: () => {
          // Reload gesuch because the status has changed
          this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
        },
      });
    }
  }
}
