import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

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
        RouterLink,
    ],
    templateUrl: './sachbearbeitung-app-feature-infos-admin.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SachbearbeitungAppFeatureInfosAdminComponent {
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  verfuegungenTableColumns = ['timestamp', 'verfuegung'];
  verfuegungenSig = computed(() => {
    const verfuegungen = [
      {
        timestamp: '01.01.2021',
        documentName: 'Pdf-bla-bla 1',
        link: 'https://www.google.com',
      },
      {
        timestamp: '02.01.2021',
        documentName: 'Pdf-da-da 2',
        link: 'https://www.bing.com',
      },
    ];

    return new MatTableDataSource<{
      timestamp: string;
      documentName: string;
      link: string;
    }>(verfuegungen);
  });

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
