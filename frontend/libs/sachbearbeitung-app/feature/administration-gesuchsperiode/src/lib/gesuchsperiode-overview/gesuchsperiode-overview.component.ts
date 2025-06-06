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
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import { Gesuchsjahr } from '@dv/shared/model/gesuch';
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
    TranslatePipe,
    TranslatedPropertyPipe,
    TypeSafeMatCellDefDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './gesuchsperiode-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeOverviewComponent implements OnInit {
  private dialog = inject(MatDialog);
  store = inject(GesuchsperiodeStore);
  translate = inject(TranslateService);

  displayedColumnsGesuchsperiode: string[] = [
    'bezeichnung',
    'gesuchsperiode',
    'gesuchsjahr',
    'gueltigkeitStatus',
    'actions',
  ];

  displayedColumnsGesuchsjahr: string[] = [
    'bezeichnung',
    'ausbildungsjahr',
    'technischesJahr',
    'gueltigkeitStatus',
    'actions',
  ];

  gesuchsperiodenSortSig = viewChild('gesuchsperiodenSort', { read: MatSort });
  gesuchsperiodenPaginatorSig = viewChild('gesuchsperiodenPaginator', {
    read: MatPaginator,
  });
  gesuchsperiodenDatasourceSig = computed(() => {
    const gesuchsperioden = this.store.gesuchsperiodenListViewSig();
    const datasource = new MatTableDataSource(gesuchsperioden);
    const sort = this.gesuchsperiodenSortSig();
    const paginator = this.gesuchsperiodenPaginatorSig();
    datasource.sortingDataAccessor = (item, property) => {
      if (property === 'gesuchsperiode') {
        return item['gesuchsperiodeStart'];
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

  gesuchsjahrSortSig = viewChild('gesuchsjahrSort', { read: MatSort });
  gesuchsjahrPaginatorSig = viewChild('gesuchsjahrPaginator', {
    read: MatPaginator,
  });
  gesuchsJahrDatasourceSig = computed(() => {
    const gesuchsjahre = this.store.gesuchsjahreListViewSig();
    const datasource = new MatTableDataSource(gesuchsjahre);
    const sort = this.gesuchsjahrSortSig();
    const paginator = this.gesuchsjahrPaginatorSig();
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

  private deleteEntry(
    entry: { bezeichnungDe: string; bezeichnungFr: string },
    deleteHandler: () => void,
  ) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsperiode.title',
      message:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsperiode.text',
      translationObject: entry,
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          deleteHandler();
        }
      });
  }

  deleteGesuchsperiode<
    T extends { id: string; bezeichnungDe: string; bezeichnungFr: string },
  >(gesuchsperiode: T) {
    this.deleteEntry(gesuchsperiode, () =>
      this.store.deleteGesuchsperiode$(gesuchsperiode.id),
    );
  }

  deleteGesuchsjahr(gesuchsjahr: Gesuchsjahr) {
    this.deleteEntry(gesuchsjahr, () =>
      this.store.deleteGesuchsjahr$(gesuchsjahr.id),
    );
  }
}
