import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { SharedEventGesuchFormProtokoll } from '@dv/shared/event/gesuch-form-protokoll';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-gesuch-form-notizen',
  standalone: true,
  imports: [CommonModule, TranslateModule, MatTable],
  templateUrl:
    './sachbearbeitung-app-feature-gesuch-form-notizen.component.html',
  styleUrl: './sachbearbeitung-app-feature-gesuch-form-notizen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchFormNotizenComponent
  implements OnInit
{
  displayedColumns = ['datum', 'typ', 'notiz'];
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

  ngOnInit() {
    const gesuchId = this.gesuchIdSig();
    this.store.dispatch(SharedEventGesuchFormProtokoll.init());
    this.statusprotokollStore.loadCachedStatusprotokoll$({
      gesuchId,
    });
  }

  sortSig = viewChild(MatSort);
}
