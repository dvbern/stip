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
import { TranslocoPipe } from '@jsverse/transloco';
import { debounceTime, map } from 'rxjs';

import {
  SozialdienstBenutzerViewEntry,
  SozialdienstStore,
} from '@dv/shared/data-access/sozialdienst';
import { SozialdienstBenutzer } from '@dv/shared/model/gesuch';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
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

@Component({
  imports: [
    RouterLink,
    TranslocoPipe,
    ReactiveFormsModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiMaxLengthDirective,
    SharedUiClearButtonComponent,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './sozialdienst-mitarbeiter-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [paginatorTranslationProvider()],
})
export class SozialdienstMitarbeiterOverviewComponent {
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  private formBuilder = inject(NonNullableFormBuilder);
  store = inject(SozialdienstStore);
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);

  filterForm = this.formBuilder.group({
    name: [<string | null>null],
    email: [<string | null>null],
  });
  private filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(
      debounceTime(INPUT_DELAY),
      map(() => this.filterForm.getRawValue()),
    ),
  );
  benutzerListDataSourceSig = computed(() => {
    const benutzers = this.store.sozialdienstBenutzersView();
    const datasource = new MatTableDataSource(benutzers.data);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();

    datasource.filterPredicate = (
      data: SozialdienstBenutzerViewEntry,
      filter: string,
    ) => {
      const { name, email } = JSON.parse(filter);
      if (name && !data.name.toLowerCase().includes(name.toLowerCase())) {
        return false;
      }
      if (email && !data.email.toLowerCase().includes(email.toLowerCase())) {
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
  displayedColumns = ['name', 'email', 'actions'];
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  constructor() {
    this.store.loadSozialdienstBenutzerList$();
    effect(() => {
      const filterValues = this.filterFormChangedSig();
      this.benutzerListDataSourceSig().filter = JSON.stringify(filterValues);
    });
  }

  deleteBenutzer(benutzer: SozialdienstBenutzer) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'sozialdienst-app.admin.sozialdienstBenutzer.confirmDelete.title',
      message: 'sozialdienst-app.admin.sozialdienstBenutzer.confirmDelete.text',
      translationObject: benutzer,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteSozialdienstBenutzer$({
            sozialdienstBenutzerId: benutzer.id,
          });
        }
      });
  }
}
