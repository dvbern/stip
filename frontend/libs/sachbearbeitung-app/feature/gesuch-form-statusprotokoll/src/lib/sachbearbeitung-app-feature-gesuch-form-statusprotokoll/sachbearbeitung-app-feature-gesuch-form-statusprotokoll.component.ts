import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  ViewChild,
  computed,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { fromBackendLocalDate } from '@dv/shared/util/validator-date';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-gesuch-form-statusprotokoll',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
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

  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });

  statusprotokollSig = computed(() => {
    const protokoll =
      this.statusprotokollStore.cachedStatusprotokollListViewSig();
    const datasource = new MatTableDataSource(
      protokoll?.map((p) => {
        return {
          datum: p.timestamp,
          status: p.status,
          user: 'tbd',
          kommentar: 'tbd',
        };
      }) ?? [],
    );
    datasource.sort = this.sortSig() ?? null;
    return datasource;
  });

  ngOnInit() {
    const gesuchId = this.gesuchIdSig();
    this.statusprotokollStore.loadCachedStatusprotokoll$({
      gesuchId,
    });
  }

  @ViewChild(MatSort) sort2: MatSort | undefined;
  sortSig = viewChild(MatSort);
}
