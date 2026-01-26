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
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

export interface GesuchTableColumns {
  timestamp: string;
  user: string;
  kommentar: string;
}

@Component({
  imports: [
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './ausbildung-abschliessen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AusbildungAbschliessenComponent {
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);

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

  gesuchAbschliessenSig = computed(() => {
    return new MatTableDataSource<GesuchTableColumns>(
      this.gesuchTablelColumnsDummyData,
    );
  });

  gesuchAbschliessen() {
    SharedUiKommentarDialogComponent.open(this.dialog, {
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
