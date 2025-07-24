import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe } from '@ngx-translate/core';

import { BeschwerdeStore } from '@dv/sachbearbeitung-app/data-access/beschwerde';
import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppDialogBeschwerdeEntryComponent } from '@dv/sachbearbeitung-app/dialog/beschwerde-entry';
import { BeschwerdeVerlaufEntry } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTooltipModule,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    SharedUiDownloadButtonDirective,
  ],
  templateUrl: './verlauf.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BeschwerdeStore, paginatorTranslationProvider()],
})
export class VerlaufComponent {
  private beschwerdeStore = inject(BeschwerdeStore);
  private matDialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  gesuchStore = inject(GesuchStore);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });
  displayColumns = [
    'timestampErstellt',
    'userErstellt',
    'title',
    'kommentar',
    'successfull',
    'document',
    'actions',
  ];
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  beschwerdeVerlaufSig = computed(() => {
    const beschwerdeVelauf = this.beschwerdeStore.beschwerdeVelaufSig();
    const paginator = this.paginatorSig();
    const sort = this.sortSig();
    const dataSource = new MatTableDataSource(beschwerdeVelauf);

    if (paginator) {
      dataSource.paginator = paginator;
    }

    if (sort) {
      dataSource.sort = sort;
    }

    return dataSource;
  });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      if (!gesuchId) {
        return;
      }

      this.beschwerdeStore.loadBeschwerden$({ gesuchId });
    });
  }

  showDetails(beschwerde: BeschwerdeVerlaufEntry) {
    SachbearbeitungAppDialogBeschwerdeEntryComponent.open(
      this.matDialog,
      beschwerde,
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
