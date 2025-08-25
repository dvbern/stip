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
import { ReactiveFormsModule } from '@angular/forms';
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
import { TranslatePipe } from '@ngx-translate/core';

import { BuchhaltungStore } from '@dv/sachbearbeitung-app/data-access/buchhaltung';
import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { FailedAuszahlungBuchhaltung } from '@dv/shared/model/gesuch';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { restrictNumberParam } from '@dv/shared/util/table';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-fehlgeschlagene-zahlungen',
  imports: [
    A11yModule,
    CommonModule,
    TranslatePipe,
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
  ],
  templateUrl:
    './sachbearbeitung-app-feature-fehlgeschlagene-zahlungen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureFehlgeschlageneZahlungenComponent {
  buchhaltungsStore = inject(BuchhaltungStore);

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

  displayedColumns = [
    'fallNummer',
    'gesuchNummer',
    'nachname',
    'vorname',
    'lastTryDate',
  ];

  items?: QueryList<SharedUiFocusableListItemDirective>;

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  dataSource = new MatTableDataSource<FailedAuszahlungBuchhaltung>([]);

  fehlgeschlageneZahlungenDataSourceSig = computed(() => {
    const fehlgeschlageneZahlungen =
      this.buchhaltungsStore.fehlgeschlageneZahlungenView().data?.entries ?? [];
    const dataSource = new MatTableDataSource(fehlgeschlageneZahlungen);
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
      const totalEntries =
        this.buchhaltungsStore.fehlgeschlageneZahlungenView().data
          ?.totalEntries ?? 0;

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

    // When the route param inputs change, load the gesuche
    effect(() => {
      const { page, pageSize } = this.getInputs();

      this.buchhaltungsStore.getFehlgeschlageneZahlungen$({
        page: page ?? 0,
        pageSize: pageSize ?? DEFAULT_PAGE_SIZE,
      });
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
