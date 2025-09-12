import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslocoPipe, TranslocoService } from '@jsverse/transloco';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  imports: [
    MatTableModule,
    MatSortModule,
    MatChipsModule,
    MatTooltipModule,
    MatPaginatorModule,
    RouterLink,
    TranslocoPipe,
    TranslatedPropertyPipe,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingWithoutCachePipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './gesuchsperiode.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeComponent implements OnInit {
  private dialog = inject(MatDialog);
  store = inject(GesuchsperiodeStore);
  translate = inject(TranslocoService);

  displayedColumns: string[] = [
    'bezeichnung',
    'gesuchsperiode',
    'gesuchsjahr',
    'gueltigkeitStatus',
    'actions',
  ];
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  sortSig = viewChild('sort', { read: MatSort });
  paginatorSig = viewChild('paginator', {
    read: MatPaginator,
  });
  gesuchsperiodenDatasourceSig = computed(() => {
    const gesuchsperioden = this.store.gesuchsperiodenListViewSig();
    const datasource = new MatTableDataSource(gesuchsperioden);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();
    datasource.sortingDataAccessor = (item, property) => {
      if (property === 'gesuchsperiode') {
        return item.gesuchsperiodeStart;
      }
      return property;
    };
    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }
    return datasource;
  });

  ngOnInit(): void {
    this.store.loadOverview$();
  }

  deleteGesuchsperiode(gesuchsperiode: {
    id: string;
    bezeichnungDe: string;
    bezeichnungFr: string;
  }) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsperiode.title',
      message:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsperiode.text',
      translationObject: gesuchsperiode,
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.store.deleteGesuchsperiode$(gesuchsperiode.id);
        }
      });
  }
}
