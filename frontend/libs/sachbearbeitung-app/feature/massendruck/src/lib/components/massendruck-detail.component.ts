import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  Component,
  computed,
  effect,
  inject,
  input,
  viewChildren,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { translatableSb } from '@dv/sachbearbeitung-app/assets/i18n';
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
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
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
import { partiallyDebounceFormValueChangesSig } from '@dv/shared/util/table';

type TableItem = { adressat: ElternTyp | 'PIA' } & (
  | MassendruckDatenschutzbrief
  | MassendruckVerfuegung
);

@Component({
  selector: 'dv-sachbearbeitung-app-feature-massendruck-detail',
  templateUrl: './massendruck-detail.component.html',
  styleUrls: ['./massendruck-detail.component.scss'],
  imports: [
    A11yModule,
    CommonModule,
    TranslocoPipe,
    RouterLink,
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
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    SachbearbeitungAppPatternOverviewLayoutComponent,
    SharedUiDownloadButtonDirective,
  ],
})
export class MassendruckDetailComponent {
  private dialog = inject(MatDialog);
  private router = inject(Router);
  displayedColumns = ['Versendet', 'Gesuch', 'Nachname', 'Vorname', 'Adressat'];

  massendruckStore = inject(MassendruckStore);
  formBuilder = inject(NonNullableFormBuilder);

  id = input<string | undefined>(undefined);

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  itemsSig = viewChildren(SharedUiFocusableListItemDirective);

  filterForm = this.formBuilder.group({
    versendet: [<boolean | null>null],
    gesuch: [''],
  });

  private filterFormChangedSig = partiallyDebounceFormValueChangesSig(
    this.filterForm,
    ['versendet'],
  );

  massendruckDetailDataSourceSig = computed(() => {
    const { massendruckJob } = this.massendruckStore.massendruckViewSig();

    const dataSource = this.genereateFilteredDataSource(massendruckJob);
    dataSource.filterPredicate = (data) => {
      const { versendet, gesuch } = this.filterForm.getRawValue();
      if (versendet !== null && data.isVersendet !== versendet) {
        return false;
      }
      if (
        gesuch &&
        !data.gesuchNummer
          .toLocaleLowerCase()
          .includes(gesuch.toLocaleLowerCase())
      ) {
        return false;
      }
      return true;
    };
    return dataSource;
  });

  reloadMassendruckJobSig = computed(
    () => {
      const { areAllSent, massendruckJob } =
        this.massendruckStore.massendruckViewSig();

      return { areAllSent, id: massendruckJob?.id };
    },
    {
      equal: (a, b) => a.areAllSent === b.areAllSent || b.areAllSent === null,
    },
  );

  constructor() {
    effect(() => {
      const id = this.id();
      if (id) {
        this.massendruckStore.loadMassendruckJob$({ massendruckJobId: id });
      }
    });

    let previousAreAllSent: boolean | null = null;
    effect(() => {
      const { areAllSent, id } = this.reloadMassendruckJobSig();

      if (areAllSent && id && previousAreAllSent !== null) {
        this.massendruckStore.loadMassendruckJob$({
          massendruckJobId: id,
        });
      }
      previousAreAllSent = areAllSent;
    });

    effect(() => {
      this.filterFormChangedSig();
      this.massendruckDetailDataSourceSig().filter = JSON.stringify(
        this.filterForm.getRawValue(),
      );
    });
  }

  setMasendruckJobVersendet(item: TableItem) {
    if (item.isVersendet) {
      return;
    }

    if ('elternTyp' in item) {
      // Datenschutzbrief
      this.massendruckStore.massendruckDatenschutzbriefVersenden$({
        massendruckDatenschutzbriefId: item.id,
      });
    } else {
      // Verfügung
      this.massendruckStore.massendruckVerfuegungVersenden$({
        massendruckVerfuegungId: item.id,
      });
    }
  }

  cancelMassendruckJob(massendruckJobDetail: MassendruckJobDetail) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: translatableSb(
        'sachbearbeitung-app.massendruck-detail.cancel.dialog.title',
      ),
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.massendruckStore.cancelMassendruckJob$({
            massendruckJobId: massendruckJobDetail.id,
            onSuccess: () => this.router.navigate(['/massendruck']),
          });
        }
      });
  }

  retryMassendruckJob(massendruckJobDetail: MassendruckJobDetail) {
    this.massendruckStore.retryMassendruckJob$({
      massendruckJobId: massendruckJobDetail.id,
    });
  }

  private genereateFilteredDataSource(
    massendruckJobDetail: MassendruckJobDetail | undefined,
  ) {
    if (!massendruckJobDetail) {
      return new MatTableDataSource<TableItem>([]);
    }

    let data: TableItem[] = [];

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

    return new MatTableDataSource<TableItem>(data);
  }
}
