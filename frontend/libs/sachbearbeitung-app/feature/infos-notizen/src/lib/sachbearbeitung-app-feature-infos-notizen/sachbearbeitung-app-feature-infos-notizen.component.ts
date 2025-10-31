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
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizTyp,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';
import { assertUnreachable } from '@dv/shared/model/type-util';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
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
  private gesuchInfoStore = inject(GesuchInfoStore);
  private config = inject(SharedModelCompileTimeConfig);

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = ['notizTyp', 'datum', 'user', 'betreff', 'actions'];
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  notizStore = inject(NotizStore);
  permissionStore = inject(PermissionStore);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);

  canCreateJurNotizSig = computed(() => {
    const gesuchStatus = this.gesuchInfoStore.gesuchInfo().data?.gesuchStatus;
    const rolesMap = this.permissionStore.rolesMapSig();
    if (!gesuchStatus) {
      return false;
    }
    const permissions = getGesuchPermissions(
      { gesuchStatus },
      this.config.appType,
      rolesMap,
    );

    return permissions.permissions.canCreateJurNotiz;
  });

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
            onSuccess: () => {
              if (gesuchNotizCreate.notizTyp === 'JURISTISCHE_NOTIZ') {
                this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
              }
            },
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

        switch (notiz.notizTyp) {
          case 'PENDENZ_NOTIZ':
          case 'GESUCH_NOTIZ':
            this.notizStore.editNotiz$({
              gesuchId: this.gesuchIdSig(),
              gesuchNotizUpdate: {
                id: notiz.id,
                betreff: result.betreff,
                text: result.text,
                pendenzAbgeschlossen: result.pendenzAbgeschlossen,
              },
            });
            return;
          case 'JURISTISCHE_NOTIZ':
            if (result.antwort) {
              this.notizStore.answerJuristischeAbklaerungNotiz$({
                gesuchId: this.gesuchIdSig(),
                notizId: notiz.id,
                juristischeAbklaerungNotizAntwort: {
                  antwort: result.antwort,
                },
              });
            }
            return;
          default:
            assertUnreachable(notiz.notizTyp);
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
