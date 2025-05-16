import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { debounceTime, map } from 'rxjs';

import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { Sozialdienst } from '@dv/shared/model/gesuch';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

const INPUT_DELAY = 600;

@Component({
    imports: [
        CommonModule,
        TranslatePipe,
        ReactiveFormsModule,
        MatTableModule,
        MatSortModule,
        MatPaginatorModule,
        MatTooltipModule,
        RouterLink,
        TypeSafeMatCellDefDirective,
        SharedUiRdIsPendingPipe,
        SharedUiRdIsPendingWithoutCachePipe,
        SharedUiLoadingComponent,
        SharedUiClearButtonComponent,
        SharedUiMaxLengthDirective,
        SharedUiTruncateTooltipDirective,
        MatFormFieldModule,
        MatInputModule,
    ],
    providers: [paginatorTranslationProvider()],
    templateUrl: './sozialdienst-overview.component.html',
    styleUrl: './sozialdienst-overview.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SozialdienstOverviewComponent {
  private dialog = inject(MatDialog);
  private formBuilder = inject(NonNullableFormBuilder);
  store = inject(SozialdienstStore);
  destroyRef = inject(DestroyRef);

  displayedColumns = ['name', 'ort', 'actions'];

  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);

  filterForm = this.formBuilder.group({
    name: [<string | null>null],
    ort: [<string | null>null],
  });
  private filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(
      debounceTime(INPUT_DELAY),
      map(() => this.filterForm.getRawValue()),
    ),
  );

  sozialDiensteListDataSourceSig = computed(() => {
    const dienste = this.store.sozialdienste().data?.map((sozialdienst) => ({
      ...sozialdienst,
      ort: sozialdienst.adresse.ort,
    }));
    const datasource = new MatTableDataSource(dienste);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();

    datasource.filterPredicate = (data, filter) => {
      const { name, ort } = JSON.parse(filter);
      if (name && !data.name.toLowerCase().includes(name.toLowerCase())) {
        return false;
      }
      if (ort && !data.ort.toLowerCase().includes(ort.toLowerCase())) {
        return false;
      }
      return true;
    };

    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }

    return datasource;
  });

  constructor() {
    this.store.loadAllSozialdienste$();
    effect(
      () => {
        const filterValues = this.filterFormChangedSig();
        this.sozialDiensteListDataSourceSig().filter =
          JSON.stringify(filterValues);
      },
      { allowSignalWrites: true },
    );
  }

  deleteSozialdienst(sozialdienst: Sozialdienst) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.sozialdienst.confirmDelete.sozialdienst.title',
      message:
        'sachbearbeitung-app.admin.sozialdienst.confirmDelete.sozialdienst.text',
      translationObject: sozialdienst,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteSozialdienst$(sozialdienst);
        }
      });
  }
}
