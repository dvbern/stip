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
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { SozialdienstBenutzer } from '@dv/shared/model/gesuch';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';

@Component({
  standalone: true,
  imports: [
    RouterLink,
    TranslatePipe,
    MatTooltipModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './sozialdienst-mitarbeiter-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstMitarbeiterOverviewComponent {
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  store = inject(SozialdienstStore);
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  benutzerListDataSourceSig = computed(() => {
    const benutzers = this.store.sozialdienstBenutzersView();
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
  displayedColumns = ['name', 'email', 'actions'];

  constructor() {
    this.store.loadSozialdienstBenutzer$();
  }

  deleteBenutzer(benutzer: SozialdienstBenutzer) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.sozialdienstBenutzer.confirmDelete.benutzer.title',
      message:
        'sachbearbeitung-app.admin.sozialdienstBenutzer.confirmDelete.benutzer.text',
      translationObject: benutzer,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteSozialdienstBenutzer$({
            benutzerId: benutzer.id,
          });
        }
      });
  }
}
