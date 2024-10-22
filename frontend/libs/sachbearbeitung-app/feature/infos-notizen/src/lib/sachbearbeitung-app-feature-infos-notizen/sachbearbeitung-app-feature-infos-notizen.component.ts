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
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { SharedUiFocusableListDirective } from '@dv/shared/ui/focusable-list';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedUiFocusableListDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-notizen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenComponent {
  store = inject(Store);
  displayedColumns = ['datum', 'typ', 'text', 'user', 'actions'];
  notizStore = inject(NotizStore);

  gesuchIdSig = input.required<string>({ alias: 'id' });

  notizSig = computed(() => {
    const notiz = this.notizStore.notizenListViewSig();
    const datasource = new MatTableDataSource(notiz);
    datasource.sort = this.sortSig() ?? null;
    return datasource;
  });
  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();
        this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
        this.notizStore.loadNotizen$({
          gesuchId,
        });
      },
      { allowSignalWrites: true },
    );
  }

  sortSig = viewChild(MatSort);
}
