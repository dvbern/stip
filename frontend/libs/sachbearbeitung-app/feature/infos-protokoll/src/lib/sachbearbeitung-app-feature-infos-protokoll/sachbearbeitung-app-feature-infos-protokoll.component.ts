import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
    selector: 'dv-sachbearbeitung-app-feature-infos-protokoll',
    imports: [
        CommonModule,
        TranslatePipe,
        MatTableModule,
        MatSortModule,
        MatPaginatorModule,
        TypeSafeMatCellDefDirective,
        ReactiveFormsModule,
        SharedUiTruncateTooltipDirective,
    ],
    providers: [paginatorTranslationProvider()],
    templateUrl: './sachbearbeitung-app-feature-infos-protokoll.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SachbearbeitungAppFeatureInfosProtokollComponent {
  displayedColumns = ['datum', 'status', 'user', 'kommentar'];
  statusprotokollStore = inject(StatusprotokollStore);
  store = inject(Store);

  gesuchIdSig = input.required<string>({ alias: 'id' });

  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  statusprotokollSig = computed(() => {
    const protokoll =
      this.statusprotokollStore.cachedStatusprotokollListViewSig();
    const datasource = new MatTableDataSource(protokoll);
    datasource.sort = this.sortSig() ?? null;
    datasource.paginator = this.paginatorSig() ?? null;
    return datasource;
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();
        this.statusprotokollStore.loadCachedStatusprotokoll$({
          gesuchId,
        });
      },
      { allowSignalWrites: true },
    );
  }
}
