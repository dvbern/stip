import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  inject,
  signal,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
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
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
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
    SharedUiClearButtonComponent,
    MatFormFieldModule,
    MatInputModule,
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
  filterSig = signal<string | null>(null);

  sozialDiensteListDataSourceSig = computed(() => {
    const benutzers = this.store.sozialdienste();
    const datasource = new MatTableDataSource(benutzers.data);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();
    const filter = this.filterSig();

    datasource.filterPredicate = (data, filter) => {
      return data.name.toLocaleLowerCase().includes(filter);
    };

    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }
    if (filter) {
      datasource.filter = filter.trim().toLocaleLowerCase();
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
          this.store.deleteSozialdienst$(sozialdienst);
        }
      });
  }
}
