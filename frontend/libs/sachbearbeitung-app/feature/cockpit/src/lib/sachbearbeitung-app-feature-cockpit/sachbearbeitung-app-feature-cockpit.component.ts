import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  QueryList,
  Signal,
  ViewChild,
  ViewChildren,
  computed,
  inject,
} from '@angular/core';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { countByStatus } from '@dv/sachbearbeitung-app/util-fn/gesuch-helper';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUtilPaginatorTranslation } from '@dv/shared/util/paginator-translation';

import { selectSachbearbeitungAppFeatureCockpitView } from './sachbearbeitung-app-feature-cockpit.selector';

type GesuchGroup = {
  status: Gesuchstatus;
  iconName: string;
  count: number;
};

@Component({
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    RouterModule,
    A11yModule,
    SharedUiIconChipComponent,
    MatPaginatorModule,
    SachbearbeitungAppPatternOverviewLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-cockpit.component.html',
  styleUrls: ['./sachbearbeitung-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    { provide: MatPaginatorIntl, useClass: SharedUtilPaginatorTranslation },
  ],
})
export class SachbearbeitungAppFeatureCockpitComponent implements OnInit {
  private store = inject(Store);

  @ViewChildren(SharedUiFocusableListItemDirective)
  public items?: QueryList<SharedUiFocusableListItemDirective>;
  public displayedColumns = [
    'fall',
    'sv-nummer',
    'nachname',
    'vorname',
    'geburtsdatum',
    'ort',
    'status',
    'bearbeiter',
    'letzteAktivitaet',
  ];

  dataSoruce = new MatTableDataSource<SharedModelGesuch>([]);

  @ViewChild('gesuchePaginator', { static: true }) paginator!: MatPaginator;

  cockpitViewSig = this.store.selectSignal(
    selectSachbearbeitungAppFeatureCockpitView,
  );

  gesucheDataSourceSig = computed(() => {
    const gesuche = this.cockpitViewSig().gesuche;

    const dataSource = new MatTableDataSource<SharedModelGesuch>(gesuche);
    dataSource.paginator = this.paginator;
    return dataSource;
  });

  public groupsSig: Signal<GesuchGroup[]> = computed(() => {
    return countByStatus(this.cockpitViewSig().gesuche);
  });

  ngOnInit() {
    this.store.dispatch(SharedDataAccessGesuchEvents.loadAll());
  }

  trackByIndex(index: number) {
    return index;
  }
}
