import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  Component,
  QueryList,
  ViewChildren,
  computed,
  inject,
  input,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslocoPipe } from '@jsverse/transloco';

import { MassendruckStore } from '@dv/sachbearbeitung-app/data-access/massendruck';
import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import {
  ElternTyp,
  MassendruckDatenschutzbrief,
  MassendruckJobDetail,
  MassendruckVerfuegung,
} from '@dv/shared/model/gesuch';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
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

type TableItems = { adressat: ElternTyp | 'PIA' } & (
  | MassendruckDatenschutzbrief
  | MassendruckVerfuegung
);

// const isDatenschutzbrief = (
//   item: TableItems,
// ): item is MassendruckDatenschutzbrief => {
//   return (item as MassendruckDatenschutzbrief).elternTyp !== undefined;
// };

@Component({
  selector: 'dv-sachbearbeitung-app-feature-massendruck-detail',
  templateUrl: './massendruck-detail.component.html',
  styleUrls: ['./massendruck-detail.component.scss'],
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
    MatCheckbox,
    MatRadioModule,
    ReactiveFormsModule,
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
    SharedUiDownloadButtonDirective,
  ],
})
export class MassendruckDetailComponent {
  displayedColumns = ['Versendet', 'Gesuch', 'Nachname', 'Vorname', 'Adressat'];

  massendruckStore = inject(MassendruckStore);
  formBuilder = inject(FormBuilder);

  id = input<string | undefined>(undefined);

  constructor() {
    console.log('MassendruckDetailComponent constructor', this.id());
  }

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;

  filterForm = this.formBuilder.group({
    versendet: [null],
    gesuch: [''],
  });

  massendruckDetailDataSourceSig = computed(() => {
    const { massendruckJob } = this.massendruckStore.massendruckViewSig();

    return this.genereateFilteredDataSource(massendruckJob);
  });

  private genereateFilteredDataSource(
    massendruckJobDetail: MassendruckJobDetail | undefined,
  ) {
    if (!massendruckJobDetail) {
      return new MatTableDataSource<TableItems>([]);
    }

    let data: TableItems[] = [];

    if (massendruckJobDetail.massendruckJobTyp === 'DATENSCHUTZBRIEF') {
      data =
        massendruckJobDetail.datenschutzbriefMassendrucks?.map((i) => {
          return {
            ...i,
            adressat: i.elternTyp,
          };
        }) ?? [];
    } else {
      data =
        massendruckJobDetail.verfuegungMassendrucks?.map((i) => {
          return { ...i, adressat: 'PIA' };
        }) ?? [];
    }

    return new MatTableDataSource<TableItems>(data);
  }
}
