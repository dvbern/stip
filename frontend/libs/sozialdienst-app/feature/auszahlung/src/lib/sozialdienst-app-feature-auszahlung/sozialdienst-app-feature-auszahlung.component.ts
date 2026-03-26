import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';

@Component({
  selector: 'dv-sozialdienst-app-feature-auszahlung',
  imports: [SharedUiAuszahlungComponent, TranslocoDirective],
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
