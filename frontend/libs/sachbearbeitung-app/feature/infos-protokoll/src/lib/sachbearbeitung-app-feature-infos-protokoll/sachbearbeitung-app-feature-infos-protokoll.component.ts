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
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { SharedEventGesuchFormProtokoll } from '@dv/shared/event/gesuch-form-protokoll';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-protokoll',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    TypeSafeMatCellDefDirective,
    ReactiveFormsModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-protokoll.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosProtokollComponent {
  displayedColumns = ['datum', 'status', 'user', 'kommentar'];
  statusprotokollStore = inject(StatusprotokollStore);
  store = inject(Store);

  gesuchIdSig = input.required<string>({ alias: 'id' });

  statusprotokollSig = computed(() => {
    const protokoll =
      this.statusprotokollStore.cachedStatusprotokollListViewSig();
    const datasource = new MatTableDataSource(protokoll);
    datasource.sort = this.sortSig() ?? null;
    return datasource;
  });
  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();
        this.store.dispatch(SharedEventGesuchFormProtokoll.init());
        this.statusprotokollStore.loadCachedStatusprotokoll$({
          gesuchId,
        });
      },
      { allowSignalWrites: true },
    );
  }

  sortSig = viewChild(MatSort);
}
