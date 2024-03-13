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
  effect,
  inject,
  input,
} from '@angular/core';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Router, RouterModule } from '@angular/router';
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
    MatSlideToggleModule,
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
  private router = inject(Router);
  showAll = input<true | undefined>(undefined, { alias: 'show-all' });

  @ViewChildren(SharedUiFocusableListItemDirective)
  public items?: QueryList<SharedUiFocusableListItemDirective>;
  @ViewChild('gesuchePaginator', { static: true }) paginator!: MatPaginator;
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

  constructor() {
    let isFirstChange = true;
    effect(
      () => {
        const showAll = this.showAll();
        if (isFirstChange) {
          isFirstChange = false;
          return;
        }
        this.store.dispatch(
          SharedDataAccessGesuchEvents.loadAllDebounced({
            filter: { showAll: showAll },
          }),
        );
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.store.dispatch(
      SharedDataAccessGesuchEvents.loadAll({
        filter: { showAll: this.showAll() },
      }),
    );
  }

  showAllChanged(showAll: boolean) {
    this.router.navigate(['.'], {
      queryParams: showAll ? { ['show-all']: showAll } : undefined,
      replaceUrl: true,
    });
  }
}
