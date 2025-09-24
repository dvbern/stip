import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  QueryList,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { MassendruckStore } from '@dv/sachbearbeitung-app/data-access/massendruck';
import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import {
  GetMassendruckJobQueryType,
  MassendruckJobSortColumn,
  MassendruckJobStatus,
  MassendruckJobTyp,
  MassendruckServiceGetAllMassendruckJobsRequestParams,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import {
  getSortAndPageInputs,
  limitPageToNumberOfEntriesEffect,
  makeEmptyStringPropertiesNull,
  paginateList,
  partiallyDebounceFormValueChangesSig,
  restrictNumberParam,
  sortList,
} from '@dv/shared/util/table';
import { toBackendLocalDate } from '@dv/shared/util/validator-date';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-druckzentrum',
  imports: [
    A11yModule,
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    MatSortModule,
    MatSlideToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatTooltipModule,
    MatRadioModule,
    ReactiveFormsModule,
    RouterModule,
    MatPaginatorModule,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiClearButtonComponent,
    SharedUiLoadingComponent,
    SharedUiMaxLengthDirective,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    SachbearbeitungAppPatternOverviewLayoutComponent,
    SharedUiIconChipComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-druckzentrum.component.html',
  styleUrl: './sachbearbeitung-app-feature-druckzentrum.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureDruckzentrumComponent {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private formBuilder = inject(NonNullableFormBuilder);

  massendruckStore = inject(MassendruckStore);

  show = input<GetMassendruckJobQueryType | undefined>(undefined);
  massendruckJobNumber = input<string | undefined>(undefined);
  userErstellt = input<string | undefined>(undefined);
  timestampErstellt = input<string | undefined>(undefined);
  massendruckJobStatus = input<MassendruckJobStatus | undefined>(undefined);
  massendruckJobTyp = input<MassendruckJobTyp | undefined>(undefined);
  sortColumn = input<MassendruckJobSortColumn | undefined>(undefined);
  sortOrder = input<SortOrder | undefined>(undefined);

  page = input(<number | undefined>undefined, {
    transform: restrictNumberParam({ min: 0, max: 999 }),
  });
  pageSize = input(<number | undefined>undefined, {
    transform: restrictNumberParam({
      min: PAGE_SIZES[0],
      max: PAGE_SIZES[PAGE_SIZES.length - 1],
    }),
  });

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = Object.keys(MassendruckJobSortColumn);

  filterForm = this.formBuilder.group({
    massendruckJobNumber: [<string | undefined>undefined],
    userErstellt: [<string | undefined>undefined],
    timestampErstellt: [<Date | undefined>undefined],
    massendruckJobTyp: [<MassendruckJobTyp | undefined>undefined],
    massendruckJobStatus: [<MassendruckJobStatus | undefined>undefined],
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  availableTypes = Object.values(MassendruckJobTyp);
  availableStatus = Object.values(MassendruckJobStatus);
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  showViewSig = computed<GetMassendruckJobQueryType>(() => {
    const show = this.show();
    return show ?? GetMassendruckJobQueryType.ALLE;
  });
  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);

  defaultFilter = GetMassendruckJobQueryType.ALLE;

  quickFilterForm = this.formBuilder.group({
    query: [GetMassendruckJobQueryType.ALLE],
  });

  quickFilters: {
    typ: GetMassendruckJobQueryType;
    icon: string;
  }[] = [
    {
      typ: 'ALLE',
      icon: 'print',
    },
    {
      typ: 'ALLE_AKTIV',
      icon: 'print_connect',
    },
    {
      typ: 'ALLE_FEHLERHAFTE_GENERIERUNG',
      icon: 'error',
    },
    {
      typ: 'ALLE_ARCHIVIERT',
      icon: 'archive',
    },
  ];

  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'massendruckJobStatus',
    'massendruckJobTyp',
  ]);

  druckzentrumDataSourceSig = computed(() => {
    const druckauftraege =
      this.massendruckStore.paginatedMassendruckListViewSig()
        ?.paginatedMassendruckJobs?.entries ?? [];

    const dataSource = new MatTableDataSource(druckauftraege);
    return dataSource;
  });

  paginate(event: PageEvent) {
    this.router.navigate(['.'], {
      queryParams: {
        page: event.pageIndex,
        pageSize: event.pageSize,
      },
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  }

  // loading = computed(() => false);
  totalEntriesSig = computed(() => {
    return (
      this.massendruckStore.paginatedMassendruckListViewSig()
        ?.paginatedMassendruckJobs?.totalEntries ?? 0
    );
  });

  constructor() {
    limitPageToNumberOfEntriesEffect(
      this,
      this.totalEntriesSig,
      this.router,
      this.route,
    );

    // Handle normal filter form control changes
    effect(() => {
      this.filterFormChangedSig();
      const formValue = this.filterForm.getRawValue();
      const query = createQuery({
        ...formValue,
        timestampErstellt: formValue.timestampErstellt
          ? toBackendLocalDate(formValue.timestampErstellt)
          : undefined,
      });

      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: makeEmptyStringPropertiesNull(query),
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    });

    // Handle the quick filter form control changes (show / getGesucheSBQueryType)
    const quickFilterChanged = toSignal(
      this.quickFilterForm.controls.query.valueChanges,
    );
    effect(() => {
      const query = quickFilterChanged();
      const defaultFilter = this.defaultFilter;
      if (!query) {
        return;
      }
      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: {
          show: query === defaultFilter ? undefined : query,
        },
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    });

    // When the route param inputs change, load the gesuche
    effect(() => {
      const { query, filter } = this.getInputs();

      this.massendruckStore.loadPaginatedMassendruckJobs$({
        getMassendruckJobs: query,
        ...filter,
        ...getSortAndPageInputs(this),
      });
    });
  }

  /**
   * Bundle all route param inputs into an object
   */
  private getInputs() {
    const query = this.showViewSig();
    const filter = {
      massendruckJobNumber: this.massendruckJobNumber(),
      userErstellt: this.userErstellt(),
      timestampErstellt: this.timestampErstellt(),
      massendruckJobStatus: this.massendruckJobStatus(),
      massendruckJobTyp: this.massendruckJobTyp(),
    };

    return {
      query,
      filter,
    };
  }
}

const createQuery = <
  T extends Partial<MassendruckServiceGetAllMassendruckJobsRequestParams>,
>(
  query: T,
) => query;
