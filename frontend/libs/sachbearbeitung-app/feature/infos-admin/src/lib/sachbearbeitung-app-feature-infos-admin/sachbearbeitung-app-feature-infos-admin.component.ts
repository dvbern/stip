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
import { TranslatePipe } from '@ngx-translate/core';

import { InfosAdminStore } from '@dv/sachbearbeitung-app/data-access/infos-admin';
import { Verfuegung } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

export interface GesuchTableColumns {
  timestamp: string;
  user: string;
  kommentar: string;
}

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-admin',
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedUiDownloadButtonDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-admin.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosAdminComponent {
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private infosAdminStore = inject(InfosAdminStore);
  verfuegungenTableColumns = ['timestamp', 'verfuegung'];
  verfuegungenSig = computed(() => {
    const verfuegungen = this.infosAdminStore.verfuegungenViewSig();
    return new MatTableDataSource<Verfuegung>(verfuegungen ?? []);
  });

  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });
  gesuchTableColumns = ['timestamp', 'user', 'kommentar'];

  gesuchTablelColumnsDummyData = [
    {
      timestamp: '01.01.2021',
      user: 'Max Mustermann',
      kommentar: 'Kommentar 1',
    },
    {
      timestamp: '02.01.2021',
      user: 'Max Mustermann',
      kommentar: 'Kommentar 2',
    },
  ];

  gesuchAbbrechenSig = computed(() => {
    return new MatTableDataSource<GesuchTableColumns>(
      this.gesuchTablelColumnsDummyData,
    );
  });

  gesuchUnterbrechenSig = computed(() => {
    return new MatTableDataSource<GesuchTableColumns>(
      this.gesuchTablelColumnsDummyData,
    );
  });

  gesuchAbschliessenSig = computed(() => {
    return new MatTableDataSource<GesuchTableColumns>(
      this.gesuchTablelColumnsDummyData,
    );
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        if (!gesuchId) {
          return;
        }

        this.infosAdminStore.loadVerfuegungen$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
  }

  gesuchAbbrechen() {
    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: 'gesuchId',
      titleKey: 'sachbearbeitung-app.infos.admin.ausbildung-abbrechen',
      messageKey:
        'sachbearbeitung-app.infos.admin.ausbildung-abbrechen.message',
      placeholderKey: 'sachbearbeitung-app.infos.admin.kommentar.placeholder',
      confirmKey: 'sachbearbeitung-app.infos.admin.ausbildung-abbrechen',
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          // do something
        }
      });
  }

  gesuchUnterbrechen() {
    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: 'gesuchId',
      titleKey: 'sachbearbeitung-app.infos.admin.ausbildung-unterbrechen',
      messageKey:
        'sachbearbeitung-app.infos.admin.ausbildung-unterbrechen.message',
      placeholderKey: 'sachbearbeitung-app.infos.admin.kommentar.placeholder',
      confirmKey: 'sachbearbeitung-app.infos.admin.ausbildung-unterbrechen',
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          // do something
        }
      });
  }

  gesuchAbschliessen() {
    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: 'gesuchId',
      titleKey: 'sachbearbeitung-app.infos.admin.ausbildung-abschliessen',
      messageKey:
        'sachbearbeitung-app.infos.admin.ausbildung-abschliessen.message',
      placeholderKey: 'sachbearbeitung-app.infos.admin.kommentar.placeholder',
      confirmKey: 'sachbearbeitung-app.infos.admin.ausbildung-abschliessen',
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          // do something
        }
      });
  }
}
