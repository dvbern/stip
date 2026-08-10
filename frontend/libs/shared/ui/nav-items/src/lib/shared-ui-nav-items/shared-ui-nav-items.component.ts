import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
} from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { NavItem } from '@dv/shared/util/navigation';

@Component({
  selector: 'dv-shared-ui-nav-items',
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatMenuModule,
    MatButtonModule,
    TranslocoDirective,
    MatBadgeModule,
  ],
  templateUrl: './shared-ui-nav-items.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNavItemsComponent {
  @HostBinding('class') klass = 'tw:flex tw:grow tw:items-center tw:gap-4';

  navItemsSig = input.required<NavItem[]>();
}
