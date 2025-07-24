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
import { TranslatePipe } from '@ngx-translate/core';

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
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './ausbildung-unterbrechen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AusbildungUnterbrechenComponent {
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

  gesuchUnterbrechenSig = computed(() => {
    return new MatTableDataSource<GesuchTableColumns>(
      this.gesuchTablelColumnsDummyData,
    );
  });

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
}
