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
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { SozialdienstStore } from '@dv/sachbearbeitung-app/data-access/sozialdienst';
import { Sozialdienst } from '@dv/shared/model/gesuch';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

type Filter = {
  column: string;
  value: string | null;
};

@Component({
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatTooltipModule,
    RouterLink,
    TypeSafeMatCellDefDirective,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    SharedUiClearButtonComponent,
    SharedUiMaxLengthDirective,
    SharedUiTruncateTooltipDirective,
    MatFormFieldModule,
    MatInputModule,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './sozialdienst-overview.component.html',
  styleUrl: './sozialdienst-overview.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstOverviewComponent {
  private dialog = inject(MatDialog);
  store = inject(SozialdienstStore);
  destroyRef = inject(DestroyRef);

  displayedColumns = ['name', 'ort', 'actions'];

  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);

  nameFilterSig = signal<string | null>(null);
  ortFilterSig = signal<string | null>(null);
  filtersSig = computed<string>(() => {
    const filters: Filter[] = [
      { column: 'name', value: this.nameFilterSig() },
      { column: 'ort', value: this.ortFilterSig() },
    ];
    return JSON.stringify(filters);
  });

  sozialDiensteListDataSourceSig = computed(() => {
    const dienste = this.store.sozialdienste().data?.map((sozialdienst) => ({
      ...sozialdienst,
      ort: sozialdienst.adresse.ort,
    }));
    const datasource = new MatTableDataSource(dienste);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();
    const filters = this.filtersSig();

    datasource.filterPredicate = (data, filters) => {
      const filterList: Filter[] = JSON.parse(filters);

      return filterList.every((filter) => {
        if (!filter.value) {
          return true;
        }

        if (filter.column === 'name') {
          return data.name.toLowerCase().includes(filter.value.toLowerCase());
        }

        if (filter.column === 'ort') {
          return data.adresse.ort
            .toLowerCase()
            .includes(filter.value.toLowerCase());
        }

        return true;
      });
    };

    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }
    if (filters) {
      datasource.filter = filters;
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
