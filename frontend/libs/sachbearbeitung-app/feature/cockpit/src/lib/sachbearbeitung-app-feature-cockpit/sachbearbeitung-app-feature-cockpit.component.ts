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
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchFilter, SharedModelGesuch } from '@dv/shared/model/gesuch';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { SharedUtilPaginatorTranslation } from '@dv/shared/util/paginator-translation';

import { selectSachbearbeitungAppFeatureCockpitView } from './sachbearbeitung-app-feature-cockpit.selector';

const DEFAULT_FILTER: GesuchFilter = 'ALLE_BEARBEITBAR_MEINE';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  standalone: true,
  imports: [
    A11yModule,
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    MatRadioModule,
    ReactiveFormsModule,
    RouterModule,
    MatPaginatorModule,
    SharedUiIconChipComponent,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiVersionTextComponent,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
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
  private formBuilder = inject(NonNullableFormBuilder);
  showSig = input<GesuchFilter | undefined>(undefined, {
    alias: 'show',
  });

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  @ViewChild('gesuchePaginator', { static: true }) paginator!: MatPaginator;
  displayedColumns = [
    'fall',
    'svNummer',
    'nachname',
    'vorname',
    'geburtsdatum',
    'ort',
    'status',
    'bearbeiter',
    'letzteAktivitaet',
  ];

  quickFilterForm = this.formBuilder.group({
    query: [<GesuchFilter | undefined>undefined],
  });
  dataSoruce = new MatTableDataSource<SharedModelGesuch>([]);

  cockpitViewSig = this.store.selectSignal(
    selectSachbearbeitungAppFeatureCockpitView,
  );
  quickFilters: { typ: GesuchFilter; icon: string }[] = [
    {
      typ: 'ALLE_BEARBEITBAR_MEINE',
      icon: 'person',
    },
    {
      typ: 'ALLE_BEARBEITBAR',
      icon: 'people',
    },
    {
      typ: 'ALLE',
      icon: 'all_inclusive',
    },
  ];
  showViewSig = computed<GesuchFilter>(() => {
    const show = this.showSig();
    return show ?? DEFAULT_FILTER;
  });

  gesucheDataSourceSig = computed(() => {
    const sort = this.sortSig();
    const gesuche = this.cockpitViewSig().gesuche.map((gesuch) => ({
      id: gesuch.id,
      fall: gesuch.fall.fallNummer,
      svNummer:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.sozialversicherungsnummer,
      nachname:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.nachname,
      vorname:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.vorname,
      geburtsdatum:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.geburtsdatum,
      ort: gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
        ?.adresse.ort,
      status: gesuch.gesuchStatus,
      bearbeiter: gesuch.bearbeiter,
      letzteAktivitaet: gesuch.aenderungsdatum,
    }));

    const dataSource = new MatTableDataSource(gesuche);

    dataSource.paginator = this.paginator;
    if (sort) {
      dataSource.sort = sort;
    }

    return dataSource;
  });
  sortSig = viewChild(MatSort);

  constructor() {
    let isFirstChange = true;
    effect(
      () => {
        const query = this.showViewSig();
        if (isFirstChange) {
          isFirstChange = false;
          return;
        }
        this.store.dispatch(
          SharedDataAccessGesuchEvents.loadAllDebounced({
            query,
          }),
        );
      },
      { allowSignalWrites: true },
    );

    const quickFilterChanged = toSignal(
      this.quickFilterForm.controls.query.valueChanges,
    );
    effect(
      () => {
        const query = quickFilterChanged();
        if (!query) {
          return;
        }
        this.router.navigate(['.'], {
          queryParams: {
            show: query === DEFAULT_FILTER ? undefined : query,
          },
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    const query = this.showViewSig();
    this.quickFilterForm.reset({ query });
    this.store.dispatch(
      SharedDataAccessGesuchEvents.loadAll({
        query,
      }),
    );
  }
}
