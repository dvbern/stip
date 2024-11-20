import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { SharedModelBenutzer } from '@dv/shared/model/benutzer';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUtilPaginatorTranslation } from '@dv/shared/util/paginator-translation';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    RouterLink,
    TypeSafeMatCellDefDirective,
    SharedUiBadgeComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './benutzer-overview.component.html',
  styleUrls: ['./benutzer-overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    { provide: MatPaginatorIntl, useClass: SharedUtilPaginatorTranslation },
  ],
})
export class BenutzerOverviewComponent {
  private dialog = inject(MatDialog);
  store = inject(BenutzerverwaltungStore);
  destroyRef = inject(DestroyRef);

  displayedColumns = ['name', 'email', 'roles', 'actions'];
  showFullListForBenutzer: Record<string, boolean> = {};

  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  benutzerListDataSourceSig = computed(() => {
    const benutzers = this.store.benutzers();
    const datasource = new MatTableDataSource(benutzers.data);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();

    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }
    return datasource;
  });

  constructor() {
    this.store.loadAllSbAppBenutzers$();
  }

  expandRolesForBenutzer(benutzerId: string) {
    this.showFullListForBenutzer[benutzerId] = true;
  }

  deleteBenutzer(benutzer: SharedModelBenutzer) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.benutzerverwaltung.confirmDelete.benutzer.title',
      message:
        'sachbearbeitung-app.admin.benutzerverwaltung.confirmDelete.benutzer.text',
      translationObject: benutzer,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteBenutzer$({
            benutzerId: benutzer.id,
          });
        }
      });
  }
}
