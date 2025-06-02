import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  QueryList,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { debounceTime } from 'rxjs';

import { selectVersion } from '@dv/shared/data-access/config';
import { PermissionStore } from '@dv/shared/global/permission';
import { SozialdienstBenutzerRole } from '@dv/shared/model/benutzer';
import {
  DelegierenServiceGetDelegierungsOfSozialdienstRequestParams,
  FallWithDelegierung,
  GetDelegierungSozQueryType,
  SortOrder,
  SozDashboardColumn,
} from '@dv/shared/model/gesuch';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
import { parseDate, toBackendLocalDate } from '@dv/shared/util/validator-date';
import {
  inverseSortMap,
  makeEmptyStringPropertiesNull,
  restrictNumberParam,
  sortMap,
} from '@dv/shared/util-fn/filter-util';
import { DelegationStore } from '@dv/sozialdienst-app/data-access/delegation';
import { DelegierungDialogComponent } from '@dv/sozialdienst-app/feature/delegierung-dialog';
import {
  SozCockitComponentInputs,
  SozCockpitFilterFormKeys,
} from '@dv/sozialdienst-app/model/delegation';

const DEFAULT_FILTER: GetDelegierungSozQueryType = 'ALLE_BEARBEITBAR_MEINE';

@Component({
  selector: 'dv-sozialdienst-app-feature-cockpit',
  standalone: true,
  imports: [
    A11yModule,
    CommonModule,
    TranslatePipe,
    MatSidenavModule,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatTooltipModule,
    ReactiveFormsModule,
    RouterModule,
    MatPaginatorModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiHasRolesDirective,
    SharedUiIconChipComponent,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiVersionTextComponent,
    SharedUiMaxLengthDirective,
    SharedUiClearButtonComponent,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
  ],
  templateUrl: './sozialdienst-app-feature-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [provideDvDateAdapter(), paginatorTranslationProvider()],
})
export class SozialdienstAppFeatureCockpitComponent
  implements SozCockitComponentInputs, OnInit
{
  private sidenavSig = viewChild.required(MatSidenav);
  private formBuilder = inject(NonNullableFormBuilder);
  private permissionStore = inject(PermissionStore);
  private router = inject(Router);
  private store = inject(Store);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  delegationStore = inject(DelegationStore);
  versionSig = this.store.selectSignal(selectVersion);

  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });
  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  show = input<GetDelegierungSozQueryType | undefined>(undefined);
  sortColumn = input<SozDashboardColumn | undefined>(undefined);
  sortOrder = input<SortOrder | undefined>(undefined);
  fallNummer = input<string | undefined>(undefined);
  nachname = input<string | undefined>(undefined);
  vorname = input<string | undefined>(undefined);
  geburtsdatum = input<string | undefined>(undefined);
  wohnort = input<string | undefined>(undefined);
  page = input<number | undefined, string | undefined>(undefined, {
    transform: restrictNumberParam({ min: 0, max: 999 }),
  });
  pageSize = input<number | undefined, string | undefined>(undefined, {
    transform: restrictNumberParam({
      min: PAGE_SIZES[0],
      max: PAGE_SIZES[PAGE_SIZES.length - 1],
    }),
  });
  delegierungAngenommen = input<boolean | undefined, string | undefined>(
    undefined,
    {
      transform: booleanOrUndefined,
    },
  );

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = [...Object.keys(SozDashboardColumn), 'AKTIONEN'];

  filterForm = this.formBuilder.group({
    fallNummer: [<string | undefined>undefined],
    nachname: [<string | undefined>undefined],
    vorname: [<string | undefined>undefined],
    geburtsdatum: [<Date | undefined>undefined],
    wohnort: [<string | undefined>undefined],
    delegierungAngenommen: [<boolean | undefined>undefined],
  } satisfies Record<SozCockpitFilterFormKeys, unknown>);

  quickFilterForm = this.formBuilder.group({
    query: [<GetDelegierungSozQueryType | undefined>undefined],
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  showViewSig = computed<GetDelegierungSozQueryType>(() => {
    const roles = this.permissionStore.rolesMapSig();

    const show = this.show();
    return show ?? (roles['V0_Sozialdienst-Admin'] ? 'ALLE' : DEFAULT_FILTER);
  });

  dataSoruce = new MatTableDataSource<FallWithDelegierung>([]);

  quickFilters: {
    typ: GetDelegierungSozQueryType;
    icon: string;
    roles: SozialdienstBenutzerRole[];
  }[] = [
    {
      typ: 'ALLE_BEARBEITBAR_MEINE',
      icon: 'person',
      roles: ['V0_Sozialdienst-Mitarbeiter'],
    },
    {
      typ: 'ALLE',
      icon: 'all_inclusive',
      roles: ['V0_Sozialdienst-Mitarbeiter'],
    },
  ];

  availableQuickFiltersSig = computed(() => {
    const roles = this.permissionStore.rolesMapSig();

    return this.quickFilters.filter((filter) =>
      filter.roles.some((role) => roles?.[role]),
    );
  });

  // for delegierungAngenommen  "sachbearbeitung-app.admin.euEftaLaender.iso3code.error.notUniqueIso3": "ISO3 Code existiert bereits",
  statusValues = [
    {
      key: 'DELEGIERUNG',
      value: true,
    },
    {
      key: 'DELEGIERUNG_ANFRAGE',
      value: false,
    },
  ];

  filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(debounceTime(INPUT_DELAY)),
  );

  faelleDataSourceSig = computed(() => {
    const faelle =
      this.delegationStore.cockpitViewSig().paginatedSozDashboard?.entries;

    const dataSource = new MatTableDataSource<FallWithDelegierung>(faelle);
    return dataSource;
  });

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });

    // Handle the case where the page is higher than the total number of pages
    effect(
      () => {
        const { page, pageSize } = this.getInputs();
        const totalEntries =
          this.delegationStore.cockpitViewSig().paginatedSozDashboard
            ?.totalEntries;

        if (
          page &&
          pageSize &&
          totalEntries &&
          page * pageSize > totalEntries
        ) {
          this.router.navigate(['.'], {
            queryParams: {
              page: Math.ceil(totalEntries / pageSize) - 1,
            },
            queryParamsHandling: 'merge',
            replaceUrl: true,
          });
        }
      },
      { allowSignalWrites: true },
    );

    // Handle string filter form control changes
    effect(
      () => {
        this.filterFormChangedSig();
        const formValue = this.filterForm.getRawValue();
        const query = createQuery({
          ...formValue,
          geburtsdatum: formValue.geburtsdatum
            ? toBackendLocalDate(formValue.geburtsdatum)
            : undefined,
        });

        this.router.navigate(['.'], {
          queryParams: makeEmptyStringPropertiesNull(query),
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );

    // Handle the quick filter form control changes (show / getGesucheSBQueryType)
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
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );

    // when the route param inputs change, load the data
    effect(
      () => {
        const { query, filter, sortColumn, sortOrder, page, pageSize } =
          this.getInputs();

        this.delegationStore.loadPaginatedSozDashboard$({
          getDelegierungSozQueryType: query,
          ...filter,
          sortColumn,
          sortOrder,
          page: page ?? 0,
          pageSize: pageSize ?? DEFAULT_PAGE_SIZE,
        });
      },
      { allowSignalWrites: true },
    );
  }

  openDialog(fall: FallWithDelegierung) {
    DelegierungDialogComponent.open(this.dialog, { fall })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          const { query, filter, sortColumn, sortOrder, page, pageSize } =
            this.getInputs();

          this.delegationStore.loadPaginatedSozDashboard$({
            getDelegierungSozQueryType: query,
            ...filter,
            sortColumn,
            sortOrder,
            page: page ?? 0,
            pageSize: pageSize ?? DEFAULT_PAGE_SIZE,
          });
        }
      });
  }

  private getInputs() {
    const query = this.showViewSig();
    const filter = {
      fallNummer: this.fallNummer(),
      nachname: this.nachname(),
      vorname: this.vorname(),
      geburtsdatum: this.geburtsdatum(),
      wohnort: this.wohnort(),
      delegierungAngenommen: this.delegierungAngenommen(),
    };

    const page = this.page();
    const pageSize = this.pageSize();
    const sortColumn = this.sortColumn();
    const sortOrder = this.sortOrder();

    return {
      query,
      filter,
      page,
      pageSize,
      sortColumn,
      sortOrder,
    };
  }

  sortData(event: Sort) {
    this.router.navigate(['.'], {
      queryParams: createQuery({
        sortColumn: event.active as SozDashboardColumn,
        sortOrder: sortMap[event.direction],
      }),
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  }

  paginate(event: PageEvent) {
    this.router.navigate(['.'], {
      queryParams: createQuery({
        page: event.pageIndex,
        pageSize: event.pageSize,
      }),
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  }

  ngOnInit() {
    const { query, filter, sortColumn, sortOrder } = this.getInputs();

    this.quickFilterForm.reset({ query });
    this.filterForm.reset({
      ...filter,
      delegierungAngenommen: filter.delegierungAngenommen,
      geburtsdatum: parseDate(filter.geburtsdatum ?? ''),
    });

    if (sortColumn && sortOrder) {
      this.sortSig().sort({
        id: sortColumn,
        start: inverseSortMap[sortOrder],
        disableClear: false,
      });
    }

    this.filterForm.markAllAsTouched();
  }
}

const booleanOrUndefined = (value: string | undefined): boolean | undefined => {
  if (value === undefined) {
    return undefined;
  }
  return value === 'true';
};

const createQuery = <
  T extends
    Partial<DelegierenServiceGetDelegierungsOfSozialdienstRequestParams>,
>(
  value: T,
) => {
  return value;
};
