import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import { Gesuchsjahr, Gesuchsperiode } from '@dv/shared/model/gesuch';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiFocusableListDirective } from '@dv/shared/ui/focusable-list';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    MatChipsModule,
    RouterLink,
    TranslateModule,
    TranslatedPropertyPipe,
    TypeSafeMatCellDefDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
  ],
  templateUrl: './gesuchsperiode-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeOverviewComponent implements OnInit {
  private dialog = inject(MatDialog);
  store = inject(GesuchsperiodeStore);
  translate = inject(TranslateService);

  displayedColumnsGesuchsperiode: string[] = [
    'bezeichnung',
    'gesuchsperiode',
    'gesuchsjahr',
    'gueltigkeitStatus',
    'actions',
  ];

  displayedColumnsGesuchsjahr: string[] = [
    'bezeichnung',
    'ausbildungsjahr',
    'technischesJahr',
    'gueltigkeitStatus',
    'actions',
  ];

  gesuchsperiodenSortSig = viewChild('gesuchsperiodenSort', { read: MatSort });
  gesuchsperiodenDatasourceSig = computed(() => {
    const gesuchsperioden = this.store.gesuchsperiodenListViewSig();
    const datasource = new MatTableDataSource(gesuchsperioden);
    const sort = this.gesuchsperiodenSortSig();
    if (sort) {
      datasource.sort = sort;
    }
    return datasource;
  });

  gesuchsjahrSortSig = viewChild('gesuchsjahrSort', { read: MatSort });
  gesuchsJahrDatasourceSig = computed(() => {
    const gesuchsjahre = this.store.gesuchsjahreListViewSig();
    const datasource = new MatTableDataSource(gesuchsjahre);
    const sort = this.gesuchsjahrSortSig();
    if (sort) {
      datasource.sort = sort;
    }
    return datasource;
  });

  ngOnInit(): void {
    this.store.loadOverview$();
  }

  private deleteEntry(
    entry: { bezeichnungDe: string; bezeichnungFr: string },
    deleteHandler: () => void,
  ) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsperiode.title',
      message:
        'sachbearbeitung-app.admin.gesuchsperiode.confirmDelete.gesuchsperiode.text',
      translationObject: entry,
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          deleteHandler();
        }
      });
  }

  deleteGesuchsperiode(gesuchsperiode: Gesuchsperiode) {
    this.deleteEntry(gesuchsperiode, () =>
      this.store.deleteGesuchsperiode$(gesuchsperiode.id),
    );
  }

  deleteGesuchsjahr(gesuchsjahr: Gesuchsjahr) {
    this.deleteEntry(gesuchsjahr, () =>
      this.store.deleteGesuchsjahr$(gesuchsjahr.id),
    );
  }
}
