import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
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
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { BeschwerdeStore } from '@dv/sachbearbeitung-app/data-access/beschwerde';
import { SachbearbeitungAppDialogBeschwaerdeEntscheidComponent } from '@dv/sachbearbeitung-app/dialog/beschwaerde-entscheid';
import { SachbearbeitungAppDialogBeschwerdeEntryComponent } from '@dv/sachbearbeitung-app/dialog/beschwerde-entry';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { BeschwerdeVerlaufEntry, Gesuchstatus } from '@dv/shared/model/gesuch';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  imports: [
    TranslocoPipe,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTooltipModule,
  ],
  templateUrl: './verwaltung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BeschwerdeStore, paginatorTranslationProvider()],
})
export class VerwaltungComponent {
  private store = inject(Store);
  private beschwerdeStore = inject(BeschwerdeStore);
  private matDialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  canCreateEntscheidSig = computed(() => {
    const gesuchStatus = this.gesuchInfoStore.gesuchInfo.data()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    return [
      Gesuchstatus.KEIN_STIPENDIENANSPRUCH,
      Gesuchstatus.STIPENDIENANSPRUCH,
    ].includes(gesuchStatus);
  });
  gesuchInfoStore = inject(GesuchInfoStore);
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

  entscheidHochladen() {
    const gesuchId = this.gesuchInfoStore.gesuchInfo().data?.id;

    if (!gesuchId) {
      return;
    }
    SachbearbeitungAppDialogBeschwaerdeEntscheidComponent.open(this.matDialog, {
      allowedTypes:
        this.deploymentConfigSig().deploymentConfig?.allowedMimeTypes,
    })
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
              this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
              this.beschwerdeStore.loadBeschwerden$({ gesuchId });
            },
          });
        }
      });
  }

  setBeschwerdeTo(beschwerdeHaengig: boolean) {
    const gesuchId = this.gesuchInfoStore.gesuchInfo().data?.id;

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
              this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
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
