import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { isDefined } from '@dv/shared/util-fn/type-guards';

@Component({
  selector: 'dv-gesuch-app-feature-gesuch-form-abschluss',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    SharedUiInfoContainerComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl: './gesuch-app-feature-gesuch-form-abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureGesuchFormAbschlussComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  destroyRef = inject(DestroyRef);

  einreichenStore = inject(EinreichenStore);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);

  constructor() {
    getLatestTrancheIdFromGesuchOnUpdate$(this.gesuchViewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateEinreichen$({
          gesuchTrancheId,
        });
      });
  }

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAbschluss.init());
  }

  abschliessen() {
    const { isEditingTranche, gesuch, trancheId } = this.gesuchViewSig();
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
          if (isEditingTranche) {
            this.einreichenStore.trancheEinreichen$({
              trancheId,
            });
          } else {
            this.einreichenStore.gesuchEinreichen$({
              gesuchId: gesuch.id,
            });
          }
        }
      });
  }
}
