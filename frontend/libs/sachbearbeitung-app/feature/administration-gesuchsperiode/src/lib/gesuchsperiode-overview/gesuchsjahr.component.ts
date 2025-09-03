import { CommonModule } from '@angular/common';
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
import { Gesuchsjahr } from '@dv/shared/model/gesuch';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  imports: [
    CommonModule,
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
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './gesuchsjahr.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsjahrComponent implements OnInit {
  private dialog = inject(MatDialog);
  store = inject(GesuchsperiodeStore);
  translate = inject(TranslocoService);

  displayedColumns: string[] = [
    'bezeichnung',
    'ausbildungsjahr',
    'technischesJahr',
    'gueltigkeitStatus',
    'actions',
  ];
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  sortSig = viewChild('sort', { read: MatSort });
  paginatorSig = viewChild('paginator', {
    read: MatPaginator,
  });
  gesuchsJahrDatasourceSig = computed(() => {
    const gesuchsjahre = this.store.gesuchsjahreListViewSig();
    const datasource = new MatTableDataSource(gesuchsjahre);
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

  ngOnInit(): void {
    this.store.loadOverview$();
  }

  deleteGesuchsjahr(gesuchsjahr: Gesuchsjahr) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsjahr.title',
      message:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsjahr.text',
      translationObject: gesuchsjahr,
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.store.deleteGesuchsjahr$(gesuchsjahr.id);
        }
      });
  }
}
