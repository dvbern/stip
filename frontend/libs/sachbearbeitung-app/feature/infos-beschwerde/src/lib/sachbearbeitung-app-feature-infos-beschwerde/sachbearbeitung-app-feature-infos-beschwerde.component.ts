import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { BeschwerdeStore } from '@dv/sachbearbeitung-app/data-access/beschwerde';
import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppDialogBeschwerdeEntryComponent } from '@dv/sachbearbeitung-app/dialog/beschwerde-entry';
import { BeschwerdeVerlaufEntry } from '@dv/shared/model/gesuch';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-beschwerde',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    MatTableModule,
    MatTooltipModule,
    SharedUiKommentarDialogComponent,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-beschwerde.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [GesuchStore, BeschwerdeStore],
})
export class SachbearbeitungAppFeatureInfosBeschwerdeComponent {
  private beschwerdeStore = inject(BeschwerdeStore);
  private gesuchStore = inject(GesuchStore);
  private matDialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);

  gesuchIdSig = input.required<string>({ alias: 'id' });
  displayColumns = ['userErstellt', 'timestampErstellt', 'title', 'kommentar'];
  beschwerdeVerlaufSig = computed(() => {
    const { data } = this.beschwerdeStore.beschwerden();
    const dataSource = new MatTableDataSource(data);

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

  setBeschwerdeTo(beschwerdeHaengig: boolean) {
    const gesuchId = this.gesuchStore.gesuchInfo().data?.id;

    if (!gesuchId) {
      return;
    }

    SharedUiKommentarDialogComponent.open(this.matDialog, {
      titleKey: 'sachbearbeitung.beschwerde.create.title',
      messageKey: 'sachbearbeitung.beschwerde.create.message',
      confirmKey: 'sachbearbeitung.beschwerde.create.confirm',
      placeholderKey: 'sachbearbeitung.beschwerde.create.placeholder',
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
