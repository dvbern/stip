import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
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
    SharedUiBadgeComponent,
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
  store = inject(BenutzerverwaltungStore);

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
}
