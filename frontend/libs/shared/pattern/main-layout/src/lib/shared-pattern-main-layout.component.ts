import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  input,
  viewChild,
} from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';

import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';

@Component({
  selector: 'dv-shared-pattern-main-layout',
  imports: [
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
  ],
  templateUrl: './shared-pattern-main-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternMainLayoutComponent {
  private sidenavSig = viewChild.required(MatSidenav);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });
  }
}
