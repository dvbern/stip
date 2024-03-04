import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';

import { AdminAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/ausbildungsstaette';
import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-ausbildungsstaette',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiLoadingComponent,
    TranslateModule,
    MatTableModule,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.scss',
  animations: [
    trigger('detailExpand', [
      state('collapsed,void', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)'),
      ),
    ]),
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent {
  store = inject(AdminAusbildungsstaetteStore);

  displayedColumns: string[] = [
    'nameDe',
    'nameFr',
    'ort',
    'ausbildungsgaengeCount',
    'actions',
  ];

  expandedRow: AusbildungsstaetteTableData | null = null;

  displayedChildColumns: string[] = [
    'bezeichnungDe',
    'bezeichnungFr',
    'ausbildungsrichtung',
    'ausbildungsort',
    'actions',
  ];
}
