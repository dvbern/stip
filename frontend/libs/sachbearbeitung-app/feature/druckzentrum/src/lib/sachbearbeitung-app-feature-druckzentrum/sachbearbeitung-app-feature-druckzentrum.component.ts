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
import { differenceInCalendarMonths } from 'date-fns/differenceInCalendarMonths';
import { differenceInCalendarYears } from 'date-fns/differenceInCalendarYears';
import { differenceInDays } from 'date-fns/differenceInDays';
import { format } from 'date-fns/format';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { DruckauftragStore } from '@dv/sachbearbeitung-app/data-access/druckauftrag';
import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import {
  DruckauftraegeColumn,
  Druckauftrag,
  DruckauftragStatus,
  DruckauftragTyp,
  GetDruckauftraegeQueryType,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
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
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import {
  paginateList,
  partiallyDebounceFormValueChangesSig,
  restrictNumberParam,
  sortList,
} from '@dv/shared/util/table';
import { getDiffFormat } from '@dv/shared/util/validator-date';

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
    SharedUiLoadingComponent,
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

  druckauftragStore = inject(DruckauftragStore);

  show = input<GetDruckauftraegeQueryType | undefined>(undefined);
  batchName = input<string | undefined>(undefined);
  bearbeiter = input<string | undefined>(undefined);
  timestampErstellt = input<string | undefined>(undefined);
  druckauftragStatus = input<DruckauftragStatus | undefined>(undefined);
  druckauftragTyp = input<DruckauftragTyp | undefined>(undefined);
  sortColumn = input<DruckauftraegeColumn | undefined>(undefined);
  sortOrder = input<SortOrder | undefined>(undefined);
  timestampErstelltVon = input<string | undefined>(undefined);
  timestampErstelltBis = input<string | undefined>(undefined);
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
  displayedColumns = Object.keys(DruckauftraegeColumn);

  filterForm = this.formBuilder.group({
    batchName: [''],
    bearbeiter: [''],
    druckauftragTyp: [undefined as DruckauftragTyp | undefined],
    druckauftragStatus: [undefined as string | undefined],
  });

  filterVonBisForm = this.formBuilder.group({
    timestampErstelltVon: [<Date | undefined>undefined],
    timestampErstelltBis: [<Date | undefined>undefined],
  });

  private timestampVonChangedSig = toSignal(
    this.filterVonBisForm.controls.timestampErstelltVon.valueChanges,
  );
  private timestampBisChangedSig = toSignal(
    this.filterVonBisForm.controls.timestampErstelltBis.valueChanges,
  );
  timestampErstelltRangeSig = computed(() => {
    const start = this.timestampVonChangedSig();
    const end = this.timestampBisChangedSig();

    if (!start || !end) {
      return '';
    }
    const difference = {
      days: differenceInDays(end, start),
      months: differenceInCalendarMonths(end, start),
      years: differenceInCalendarYears(end, start),
    };
    return difference.days
      ? [
          `${getDiffFormat(start, difference)}`,
          `${format(end, 'dd.MM.yy')}`,
        ].join(' - ')
      : format(start, 'dd.MM.yyyy');
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  showViewSig = computed<GetDruckauftraegeQueryType>(() => {
    const show = this.show();
    return show ?? GetDruckauftraegeQueryType.ALLE;
  });
  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);

  quickFilterForm = this.formBuilder.group({
    query: [GetDruckauftraegeQueryType.ALLE],
  });

  quickFilters: {
    typ: GetDruckauftraegeQueryType;
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
      typ: 'ALLE_ARCHIVIERT',
      icon: 'archive',
    },
  ];

  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'druckauftragStatus',
    'druckauftragTyp',
  ]);
  filterVonBisFormChangedSig = toSignal(
    this.filterVonBisForm.valueChanges.pipe(
      distinctUntilChanged(
        (a, b) =>
          // Only emit if both fields are defined or both are undefined
          // otherwise the list will update on first range picker interaction
          isDefined(b.timestampErstelltVon) ===
            isDefined(b.timestampErstelltBis) && a === b,
      ),
      debounceTime(INPUT_DELAY),
    ),
  );

  dataSource = new MatTableDataSource<Druckauftrag>([]);

  druckzentrumDataSourceSig = computed(() => {
    const druckauftraege =
      this.druckauftragStore.cachedDruckauftragListViewSig().druckauftraege
        ?.entries ?? [];
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

  loading = computed(() => false);
  totalEntriesSig = computed(() => {
    return (
      this.druckauftragStore.cachedDruckauftragListViewSig().druckauftraege
        ?.totalEntries ?? 0
    );
  });

  constructor() {
    // Handle the case where the page is higher than the total number of pages
    effect(() => {
      const { page, pageSize } = this.getInputs();
      const totalEntries = this.totalEntriesSig();

      if (page && pageSize && totalEntries && page * pageSize > totalEntries) {
        this.router.navigate(['.'], {
          queryParams: {
            page: Math.ceil(totalEntries / pageSize) - 1,
          },
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      }
    });

    // When the route param inputs change, load the druckzentrum data
    effect(() => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { page, pageSize } = this.getInputs();

      // TODO: Load druckzentrum data
      // this.druckzentrumStore.getDruckzentrumItems$({
      //   page: page ?? 0,
      //   pageSize: pageSize ?? DEFAULT_PAGE_SIZE,
      // });
    });
  }

  /**
   * Bundle all route param inputs into an object
   */
  private getInputs() {
    const page = this.page();
    const pageSize = this.pageSize();

    return {
      page,
      pageSize,
    };
  }
}
