import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizTyp,
} from '@dv/shared/model/gesuch';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

import { SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent } from '../sachbearbeitung-app-feature-infos-notizen-detail-dialog/sachbearbeitung-app-feature-infos-notizen-detail-dialog.component';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    MatTooltipModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingWithoutCachePipe,
    MatMenuModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-notizen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  displayedColumns = [
    'notizTyp',
    'datum',
    'user',
    'betreff',
    'notiz',
    'actions',
  ];
  notizStore = inject(NotizStore);
  permissionStore = inject(PermissionStore);

  gesuchIdSig = input.required<string>({ alias: 'id' });
  sortSig = viewChild(MatSort);

  notizSig = computed(() => {
    const notiz = this.notizStore.notizenListViewSig();
    const datasource = new MatTableDataSource(notiz);
    datasource.sort = this.sortSig() ?? null;
    return datasource;
  });

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        this.notizStore.loadNotizen$({
          gesuchId,
        });
      },
      { allowSignalWrites: true },
    );
  }

  createNotiz(notizTyp: GesuchNotizTyp) {
    SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent.open(
      this.dialog,
      {
        notizTyp,
      },
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        const gesuchId = this.gesuchIdSig();

        if (result) {
          const gesuchNotizCreate: GesuchNotizCreate = {
            betreff: result.betreff,
            text: result.text,
            notizTyp,
            gesuchId,
          };

          this.notizStore.createNotiz$({
            gesuchNotizCreate,
          });
        }
      });
  }

  editNotiz(notiz: GesuchNotiz) {
    SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent.open(
      this.dialog,
      {
        notizTyp: notiz.notizTyp,
        notiz,
      },
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (!result) return;

        if (notiz.notizTyp === 'GESUCH_NOTIZ') {
          this.notizStore.editNotiz$({
            gesuchId: this.gesuchIdSig(),
            gesuchNotizUpdate: {
              id: notiz.id,
              betreff: result.betreff,
              text: result.text,
            },
          });
          return;
        }

        if (notiz.notizTyp === 'JURISTISCHE_NOTIZ' && result.antwort) {
          this.notizStore.answerJuristischeAbklaerungNotiz$({
            gesuchId: this.gesuchIdSig(),
            notizId: notiz.id,
            juristischeAbklaerungNotizAntwort: {
              antwort: result.antwort,
            },
          });
        }
      });
  }

  deleteNotiz(notizId: string) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'sachbearbeitung-app.infos.notiz.delete.title',
      message: 'sachbearbeitung-app.infos.notiz.delete.text',
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        const gesuchId = this.gesuchIdSig();
        if (confirmed) {
          this.notizStore.deleteNotiz$({ gesuchId, notizId });
        }
      });
  }
}