import {
  ChangeDetectionStrategy,
  Component,
  Input,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-overview-layout',
  imports: [
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiHasRolesDirective,
    TranslocoPipe,
  ],
  templateUrl: './sachbearbeitung-app-pattern-overview-layout.component.html',
  styleUrls: ['./sachbearbeitung-app-pattern-overview-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternOverviewLayoutComponent {
  @Input() closeMenu: { value: boolean } | null = null;

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);

  constructor() {
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      pageSize: 10,
    });
  }
}
