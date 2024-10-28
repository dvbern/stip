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
import { TranslateModule } from '@ngx-translate/core';

import { SozialdienstStore } from '@dv/sachbearbeitung-app/data-access/sozialdienst';
import { Sozialdienst } from '@dv/shared/model/gesuch';
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
    TranslateModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    RouterLink,
    TypeSafeMatCellDefDirective,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './sozialdienst-overview.component.html',
  styleUrl: './sozialdienst-overview.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    { provide: MatPaginatorIntl, useClass: SharedUtilPaginatorTranslation },
  ],
})
export class SozialdienstOverviewComponent {
  private dialog = inject(MatDialog);
  store = inject(SozialdienstStore);
  destroyRef = inject(DestroyRef);

  displayedColumns = ['name', 'ort', 'actions'];

  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  sozialDiensteListDataSourceSig = computed(() => {
    const benutzers = this.store.sozialdienste();
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
    this.store.loadAllSozialdienste$();
  }

  deleteSozialdienst(sozialdienst: Sozialdienst) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.sozialdienst.confirmDelete.sozialdienst.title',
      message:
        'sachbearbeitung-app.admin.sozialdienst.confirmDelete.sozialdienst.text',
      translationObject: sozialdienst,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteSozialdienst$({
            sozialdienstId: sozialdienst.id,
          });
        }
      });
  }
}
