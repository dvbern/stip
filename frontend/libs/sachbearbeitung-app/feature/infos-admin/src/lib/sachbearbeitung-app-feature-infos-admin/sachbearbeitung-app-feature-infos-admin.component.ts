import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslatePipe } from '@ngx-translate/core';

import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

export interface GesuchTableColumns {
  timestamp: string;
  user: string;
  kommentar: string;
}

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-admin',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-admin.component.html',
  styleUrl: './sachbearbeitung-app-feature-infos-admin.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosAdminComponent {
  verfuegungenTableColumns = ['timestamp', 'verfuegung'];
  verfuegungenSig = computed(() => {
    const verfuegungen = [
      {
        timestamp: '01.01.2021',
        pdf: 'PfG 1',
      },
      {
        timestamp: '02.01.2021',
        pdf: 'PfG 2',
      },
    ];

    return new MatTableDataSource<{ timestamp: string; pdf: string }>(
      verfuegungen,
    );
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
}
