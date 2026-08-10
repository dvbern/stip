import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { NavMenuItem } from '@dv/shared/util/navigation';

@Component({
  host: {
    '[class]':
      'this.isMobile() ? "tw:flex tw:flex-col tw:ml-4" : "tw:flex tw:flex-col"',
  },
  selector: 'dv-shared-ui-nav-menu-items',
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatMenuModule,
    MatButtonModule,
    TranslocoDirective,
    MatBadgeModule,
  ],
  templateUrl: './shared-ui-nav-menu-items.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNavMenuItemsComponent {
  isMobile = input<boolean>(false);

  navMenuItemsSig = input.required<NavMenuItem[]>();
}
