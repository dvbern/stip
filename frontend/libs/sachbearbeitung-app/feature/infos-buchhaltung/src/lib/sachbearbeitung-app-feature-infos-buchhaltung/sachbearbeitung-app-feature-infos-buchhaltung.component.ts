import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import {
  BuchhaltungEntryView,
  BuchhaltungStore,
} from '@dv/sachbearbeitung-app/data-access/buchhaltung';
import { SachbearbeitungAppDialogBuchhaltungInfoComponent } from '@dv/sachbearbeitung-app/dialog/buchhaltung-info';
import { SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent } from '@dv/sachbearbeitung-app/dialog/create-buchhaltungs-korrektur';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { BuchhaltungEntry } from '@dv/shared/model/gesuch';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-buchhaltung',
  imports: [
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    MatTooltipModule,
    MatPaginatorModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiFormatChfPipe,
    SharedUiHasRolesDirective,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiTruncateTooltipDirective,
    SharedUiDownloadButtonDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-buchhaltung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BuchhaltungStore],
})
export class SachbearbeitungAppFeatureInfosBuchhaltungComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private gesuchIdSig = this.store.selectSignal(selectRouteId);

  buchhaltungStore = inject(BuchhaltungStore);
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  displayedColumns = [
    'datum',
    'stipendienbetrag',
    'auszahlung',
    'rueckforderung',
    'saldoAenderung',
    'saldo',
    'comment',
    'sapStatus',
    'info',
  ];
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  buchhaltungDataSourceSig = computed(() => {
    const buchhaltungEntries =
      this.buchhaltungStore.buchhaltungEntriesViewSig().buchhaltungEntrys;

    const dataSource = new MatTableDataSource(buchhaltungEntries);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();

    if (sort) {
      dataSource.sort = sort;
    }
    if (paginator) {
      dataSource.paginator = paginator;
    }

    return dataSource;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      if (!gesuchId) {
        return;
      }

      this.buchhaltungStore.loadBuchhaltung$({ gesuchId });
    });
  }

  isStartOfNewGesuch(_: number, buchhaltungEntry: BuchhaltungEntryView) {
    return buchhaltungEntry.type === 'gesuchStart';
  }

  retryAuszahlung() {
    const gesuchId = this.gesuchIdSig();

    if (!gesuchId) {
      return;
    }

    this.buchhaltungStore.retryFailedAuszahlung$({ gesuchId });
  }

  createBuchhaltungsKorrektur() {
    const gesuchId = this.gesuchIdSig();

    if (!gesuchId) {
      return;
    }

    SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent.open(
      this.dialog,
    )
      .afterClosed()
      .subscribe((korrektur) => {
        if (!korrektur) {
          return;
        }
        this.buchhaltungStore.createBuchhaltungsKorrektur$({
          buchhaltungSaldokorrektur: korrektur,
          gesuchId,
        });
      });
  }

  showDetails(entry: BuchhaltungEntry) {
    SachbearbeitungAppDialogBuchhaltungInfoComponent.open(this.dialog, entry);
  }
}
