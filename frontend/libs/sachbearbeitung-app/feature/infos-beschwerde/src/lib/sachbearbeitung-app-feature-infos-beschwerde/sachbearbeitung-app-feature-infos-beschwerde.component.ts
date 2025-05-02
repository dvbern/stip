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
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { BeschwerdeStore } from '@dv/sachbearbeitung-app/data-access/beschwerde';
import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppDialogBeschwaerdeEntscheidComponent } from '@dv/sachbearbeitung-app/dialog/beschwaerde-entscheid';
import { SachbearbeitungAppDialogBeschwerdeEntryComponent } from '@dv/sachbearbeitung-app/dialog/beschwerde-entry';
import { BeschwerdeVerlaufEntry, Gesuchstatus } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-beschwerde',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTooltipModule,
    SharedUiKommentarDialogComponent,
    SharedUiTruncateTooltipDirective,
    SharedUiLoadingComponent,
    TypeSafeMatCellDefDirective,
    SharedUiDownloadButtonDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-beschwerde.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BeschwerdeStore, paginatorTranslationProvider()],
})
export class SachbearbeitungAppFeatureInfosBeschwerdeComponent {
  private beschwerdeStore = inject(BeschwerdeStore);
  private matDialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  canCreateEntscheidSig = computed(() => {
    const gesuchStatus = this.gesuchStore.gesuchInfo.data()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    return [
      Gesuchstatus.KEIN_STIPENDIENANSPRUCH,
      Gesuchstatus.STIPENDIENANSPRUCH,
    ].includes(gesuchStatus);
  });
  gesuchStore = inject(GesuchStore);
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
    const { data } = this.beschwerdeStore.beschwerden();
    const paginator = this.paginatorSig();
    const sort = this.sortSig();
    const dataSource = new MatTableDataSource(data);

    if (paginator) {
      dataSource.paginator = paginator;
    }

    if (sort) {
      dataSource.sort = sort;
    }

    return dataSource;
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        if (!gesuchId) {
          return;
        }

        this.gesuchStore.loadGesuchInfo$({ gesuchId });
        this.beschwerdeStore.loadBeschwerden$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
  }

  entscheidHochladen() {
    const gesuchId = this.gesuchStore.gesuchInfo().data?.id;

    if (!gesuchId) {
      return;
    }
    SachbearbeitungAppDialogBeschwaerdeEntscheidComponent.open(this.matDialog)
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.beschwerdeStore.createBeschwerdeEntscheid$({
            gesuchId,
            fileUpload: result.fileUpload,
            beschwerdeErfolgreich: result.beschwerdeErfolgreich,
            kommentar: result.kommentar,
            onSucces: () => {
              this.gesuchStore.loadGesuchInfo$({ gesuchId });
              this.beschwerdeStore.loadBeschwerden$({ gesuchId });
            },
          });
        }
      });
  }

  setBeschwerdeTo(beschwerdeHaengig: boolean) {
    const gesuchId = this.gesuchStore.gesuchInfo().data?.id;

    if (!gesuchId) {
      return;
    }

    SharedUiKommentarDialogComponent.open(this.matDialog, {
      titleKey:
        'sachbearbeitung-app.infos.beschwerde.create.title.' +
        beschwerdeHaengig,
      confirmKey: 'sachbearbeitung-app.infos.beschwerde.create.confirm',
      placeholderKey:
        'sachbearbeitung-app.infos.beschwerde.create.placeholder.' +
        beschwerdeHaengig,
      entityId: gesuchId,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.beschwerdeStore.createBeschwerdeEntry$({
            gesuchId,
            values: {
              beschwerdeSetTo: !beschwerdeHaengig,
              kommentar: result?.kommentar,
            },
            onSucces: () => {
              this.gesuchStore.loadGesuchInfo$({ gesuchId });
              this.beschwerdeStore.loadBeschwerden$({ gesuchId });
            },
          });
        }
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
