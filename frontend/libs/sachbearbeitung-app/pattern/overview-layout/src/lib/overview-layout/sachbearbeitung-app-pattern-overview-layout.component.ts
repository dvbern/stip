import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

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
}
