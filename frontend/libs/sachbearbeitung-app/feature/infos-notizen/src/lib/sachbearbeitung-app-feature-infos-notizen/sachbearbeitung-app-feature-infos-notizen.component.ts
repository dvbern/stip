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
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { SharedEventGesuchFormProtokoll } from '@dv/shared/event/gesuch-form-protokoll';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [CommonModule, TranslateModule, MatTable],
  templateUrl: './sachbearbeitung-app-feature-infos-notizen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenComponent {
  displayedColumns = ['datum', 'typ', 'notiz'];
  notizStore = inject(NotizStore);
  store = inject(Store);

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
        this.store.dispatch(SharedEventGesuchFormProtokoll.init());
        this.notizStore.loadNotizen$({
          gesuchId,
        });
      },
      { allowSignalWrites: true },
    );
  }

  sortSig = viewChild(MatSort);
}
