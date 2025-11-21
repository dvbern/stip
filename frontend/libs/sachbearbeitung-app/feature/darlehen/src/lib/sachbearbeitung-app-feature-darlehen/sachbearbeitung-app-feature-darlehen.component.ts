import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiDarlehenComponent } from '@dv/shared/ui/darlehen';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-darlehen',
  imports: [
    CommonModule,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
    SharedUiDarlehenComponent,
    TranslocoPipe,
  ],
  templateUrl: './sachbearbeitung-app-feature-darlehen.component.html',
  styleUrl: './sachbearbeitung-app-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureDarlehenComponent {
  private store = inject(Store);
  gesuchIdSig = this.store.selectSignal(selectRouteId);
}
