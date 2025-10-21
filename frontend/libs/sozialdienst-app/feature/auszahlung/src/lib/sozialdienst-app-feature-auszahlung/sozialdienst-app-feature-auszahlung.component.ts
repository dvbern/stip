import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';

@Component({
  selector: 'dv-sozialdienst-app-feature-auszahlung',
  imports: [
    RouterLink,
    TranslocoPipe,
    SharedPatternMainLayoutComponent,
    SharedUiAuszahlungComponent,
    SharedUiHasRolesDirective,
  ],
  templateUrl: './sozialdienst-app-feature-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureAuszahlungComponent extends SharedFeatureAuszahlungComponent {
  extendedAuszahlungViewSig = computed(() => {
    const baseView = this.auszahlungViewSig();

    return {
      ...baseView,
      origin,
      readonly: true,
    };
  });
}
