import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { combineLatest, distinctUntilChanged, filter, map } from 'rxjs';

import {
  GesuchAppDataAccessAbschlussApiEvents,
  selectGesuchAppDataAccessAbschlussView,
} from '@dv/gesuch-app/data-access/abschluss';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { getLatestGesuchIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { isDefined } from '@dv/shared/util-fn/type-guards';

@Component({
  selector: 'dv-gesuch-app-feature-gesuch-form-abschluss',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    SharedUiLoadingComponent,
  ],
  templateUrl: './gesuch-app-feature-gesuch-form-abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureGesuchFormAbschlussComponent implements OnInit {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  destroyRef = inject(DestroyRef);

  viewSig = this.store.selectSignal(selectGesuchAppDataAccessAbschlussView);

  constructor() {
    // validate form only if no formErrors form validatePages are present
    combineLatest([
      getLatestGesuchIdFromGesuchOnUpdate$(this.viewSig).pipe(
        filter(isDefined),
      ),
      toObservable(this.viewSig).pipe(
        map((view) => view.canCheck),
        distinctUntilChanged(),
        filter((canCheck) => !!canCheck),
        takeUntilDestroyed(),
      ),
    ]).subscribe(([gesuchId]) => {
      this.store.dispatch(
        GesuchAppDataAccessAbschlussApiEvents.check({
          gesuchId,
        }),
      );
    });
  }

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormAbschluss.init());
  }

  abschliessen() {
    const { specificTrancheId, gesuch } = this.viewSig();
    if (!gesuch) {
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
          if (specificTrancheId) {
            this.store.dispatch(
              GesuchAppDataAccessAbschlussApiEvents.trancheAbschliessen({
                trancheId: specificTrancheId,
              }),
            );
          } else {
            this.store.dispatch(
              GesuchAppDataAccessAbschlussApiEvents.gesuchAbschliessen({
                gesuchId: gesuch.id,
              }),
            );
          }
        }
      });
  }
}
