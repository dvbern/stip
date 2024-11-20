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
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { SharedEventGesuchFormProtokoll } from '@dv/shared/event/gesuch-form-protokoll';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-gesuch-form-statusprotokoll',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    FormsModule,
    ReactiveFormsModule,
    TypeSafeMatCellDefDirective,
    MatSortModule,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-gesuch-form-statusprotokoll.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchFormStatusprotokollComponent
  implements OnInit
{
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

  ngOnInit() {
    const gesuchId = this.gesuchIdSig();
    this.store.dispatch(SharedEventGesuchFormProtokoll.init());
    this.statusprotokollStore.loadCachedStatusprotokoll$({
      gesuchId,
    });
  }

  sortSig = viewChild(MatSort);
}
