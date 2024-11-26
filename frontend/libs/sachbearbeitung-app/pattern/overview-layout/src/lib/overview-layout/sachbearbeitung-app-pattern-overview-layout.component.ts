import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-overview-layout',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiSearchComponent,
    SharedUiHasRolesDirective,
    TranslatePipe,
  ],
  templateUrl: './sachbearbeitung-app-pattern-overview-layout.component.html',
  styleUrls: ['./sachbearbeitung-app-pattern-overview-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternOverviewLayoutComponent {
  @Input() closeMenu: { value: boolean } | null = null;
}
