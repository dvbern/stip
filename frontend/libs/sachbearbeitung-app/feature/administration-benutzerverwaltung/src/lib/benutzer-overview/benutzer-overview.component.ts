import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-administration-benutzer-overview',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    RouterLink,
    TypeSafeMatCellDefDirective,
    SharedUiBadgeComponent,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './benutzer-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BenutzerOverviewComponent {
  store = inject(BenutzerverwaltungStore);

  displayedColumns = ['name', 'email', 'roles'];

  sortSig = viewChild(MatSort);
  benutzerListDataSourceSig = computed(() => {
    const benutzers = this.store.benutzers();
    const datasource = new MatTableDataSource(benutzers.data);
    const sort = this.sortSig();

    if (sort) {
      datasource.sort = sort;
    }
    return datasource;
  });

  constructor() {
    this.store.loadAllSbAppBenutzers$();
  }
}
