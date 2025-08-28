import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  QueryList,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslocoPipe } from '@jsverse/transloco';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizTyp,
} from '@dv/shared/model/gesuch';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

import { SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent } from '../sachbearbeitung-app-feature-infos-notizen-detail-dialog/sachbearbeitung-app-feature-infos-notizen-detail-dialog.component';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  imports: [
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    MatPaginatorModule,
    MatMenuModule,
    MatTooltipModule,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiTruncateTooltipDirective,
    SharedUiFocusableListDirective,
    SharedUiFocusableListItemDirective,
    SharedUiHasRolesDirective,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './sachbearbeitung-app-feature-infos-notizen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenComponent {
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = ['notizTyp', 'datum', 'user', 'betreff', 'actions'];
  notizStore = inject(NotizStore);
  permissionStore = inject(PermissionStore);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);

  notizSig = computed(() => {
    const notiz = this.notizStore.notizenListViewSig();
    const datasource = new MatTableDataSource(notiz);
    datasource.sort = this.sortSig() ?? null;
    datasource.paginator = this.paginatorSig() ?? null;
    return datasource;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      this.notizStore.loadNotizen$({
        gesuchId,
      });
    });
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

  openNotiz(notiz: GesuchNotiz) {
    SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent.open(
      this.dialog,
      {
        notizTyp: notiz.notizTyp,
        notiz,
        readonly: true,
      },
    );
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
