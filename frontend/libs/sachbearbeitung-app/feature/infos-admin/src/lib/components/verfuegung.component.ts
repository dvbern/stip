import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslatePipe } from '@ngx-translate/core';

import { InfosAdminStore } from '@dv/sachbearbeitung-app/data-access/infos-admin';
import { Verfuegung } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
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
    SharedUiDownloadButtonDirective,
  ],
  templateUrl: './verfuegung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class VerfuegungComponent {
  private infosAdminStore = inject(InfosAdminStore);
  displayColumns = ['timestamp', 'verfuegung'];
  verfuegungenSig = computed(() => {
    const verfuegungen = this.infosAdminStore.verfuegungenViewSig();
    return new MatTableDataSource<Verfuegung>(verfuegungen ?? []);
  });
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      if (!gesuchId) {
        return;
      }

      this.infosAdminStore.loadVerfuegungen$({ gesuchId });
    });
  }
}
