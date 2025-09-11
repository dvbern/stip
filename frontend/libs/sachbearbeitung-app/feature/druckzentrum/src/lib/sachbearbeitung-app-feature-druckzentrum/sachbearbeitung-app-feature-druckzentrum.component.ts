import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  QueryList,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
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
import { restrictNumberParam } from '@dv/shared/util/table';

// TODO: Define proper interface for druckzentrum data
interface DruckzentrumItem {
  id: string;
  batchname: string;
  auftraggeber: string;
  auftragsdatum: string;
  status: string;
  typ: string;
}

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
  // TODO: Inject proper store for druckauftrag data
  // druckauftragStore = inject(DruckauftragStore);

  page = input(<number | undefined>undefined, {
    transform: restrictNumberParam({ min: 0, max: 999 }),
  });
  pageSize = input(<number | undefined>undefined, {
    transform: restrictNumberParam({
      min: PAGE_SIZES[0],
      max: PAGE_SIZES[PAGE_SIZES.length - 1],
    }),
  });

  private router = inject(Router);
  private formBuilder = inject(NonNullableFormBuilder);

  quickFilterForm = this.formBuilder.group({
    query: [<string | undefined>undefined],
  });

  quickFilters: {
    typ: string;
    icon: string;
  }[] = [
    {
      typ: 'ALLE',
      icon: 'print',
    },
    {
      typ: 'AKTIV',
      icon: 'print_connect',
    },
    {
      typ: 'ARCHIVIERT',
      icon: 'archive',
    },
  ];

  displayedColumns = [
    'batchname',
    'auftraggeber',
    'auftragsdatum',
    'status',
    'typ',
  ];

  items?: QueryList<SharedUiFocusableListItemDirective>;

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  dataSource = new MatTableDataSource<DruckzentrumItem>([]);

  // TODO: Replace with actual data from store
  loading = computed(() => false);
  totalEntries = computed(() => 0);

  druckzentrumDataSourceSig = computed(() => {
    // TODO: Replace with actual data from store
    const druckzentrumItems: DruckzentrumItem[] = [];
    const dataSource = new MatTableDataSource(druckzentrumItems);
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

  constructor() {
    // Handle the case where the page is higher than the total number of pages
    effect(() => {
      const { page, pageSize } = this.getInputs();
      const totalEntries = this.totalEntries();

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
