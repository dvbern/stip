import {
  ChangeDetectionStrategy,
  Component,
  effect,
  input,
  signal,
  viewChild,
} from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';

import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiCommingSoonComponent } from '@dv/shared/ui/comming-soon';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sozialdienst-app-feature-cockpit',
  standalone: true,
  imports: [
    MatSidenavModule,
    SharedUiCommingSoonComponent,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiRouterOutletWrapperComponent,
  ],
  templateUrl: './sozialdienst-app-feature-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureCockpitComponent {
  stepSig = signal<GesuchFormStep | undefined>(undefined);
  private sidenavSig = viewChild.required(MatSidenav);
  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });
  }
}
