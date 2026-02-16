import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  input,
  viewChild,
} from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';

import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';

@Component({
  selector: 'dv-shared-pattern-gesuchsteller-layout',
  imports: [
    MatSidenavModule,
    RouterOutlet,
    SharedPatternMobileSidenavComponent,
    SharedPatternGlobalHeaderComponent,
  ],
  template: `<mat-sidenav-container>
    <mat-sidenav #sidenav mode="over" position="end">
      <dv-shared-pattern-mobile-sidenav (closeSidenav)="sidenav.close()">
        <ng-content
          select="[dvMobileNavContent]"
          ngProjectAs="[dvMobileNavContent]"
        ></ng-content>
      </dv-shared-pattern-mobile-sidenav>
    </mat-sidenav>
    <mat-sidenav-content class="d-flex flex-column">
      <dv-shared-pattern-global-header
        (closeSidenav)="sidenav.close()"
        (openSidenav)="sidenav.open()"
      ></dv-shared-pattern-global-header>

      <main class="page-body">
        <router-outlet></router-outlet>
      </main>
    </mat-sidenav-content>
  </mat-sidenav-container>`,
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchstellerLayoutComponent {
  private sidenavSig = viewChild.required(MatSidenav);
  // todo: refactor
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
