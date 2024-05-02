import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  QueryList,
  ViewChild,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
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
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUtilPaginatorTranslation } from '@dv/shared/util/paginator-translation';

import { selectSachbearbeitungAppFeatureCockpitView } from './sachbearbeitung-app-feature-cockpit.selector';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
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
  items?: QueryList<SharedUiFocusableListItemDirective>;
  @ViewChild('gesuchePaginator', { static: true }) paginator!: MatPaginator;
  displayedColumns = [
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

  // FormControl is necessary instead of (change) event binding due to an potential issue
  // with the Angular Material SlideToggle, see https://github.com/angular/components/pull/28745
  showAllControl = new FormControl<boolean | undefined>(this.showAll());
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

    const showAllChanged = toSignal(this.showAllControl.valueChanges);
    effect(
      () => {
        const showAll = showAllChanged();
        this.router.navigate(['.'], {
          queryParams: showAll ? { ['show-all']: showAll } : undefined,
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.showAllControl.setValue(this.showAll());
    this.store.dispatch(
      SharedDataAccessGesuchEvents.loadAll({
        filter: { showAll: this.showAll() },
      }),
    );
  }
}
