import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedModelAuszahlung } from '@dv/shared/model/auszahlung';
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
    const gesuchView = this.gesuchsViewSig();
    let origin: SharedModelAuszahlung['origin'] = undefined;

    if (gesuchView.gesuch?.id && gesuchView.trancheId) {
      origin = {
        gesuchId: gesuchView.gesuch.id,
        gesuchTrancheId: gesuchView.trancheId,
        backlink: this.router.url,
      };
    }
    return {
      ...baseView,
      origin,
      readonly: true,
    };
  });
}
