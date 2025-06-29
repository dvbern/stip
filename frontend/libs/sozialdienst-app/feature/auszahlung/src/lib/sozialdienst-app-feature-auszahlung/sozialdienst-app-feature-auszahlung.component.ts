import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';

import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';

@Component({
  selector: 'dv-sozialdienst-app-feature-auszahlung',
  imports: [
    RouterLink,
    TranslatePipe,
    TranslateDirective,
    SharedPatternMainLayoutComponent,
    SharedUiAuszahlungComponent,
    SharedUiHasRolesDirective,
  ],
  templateUrl: './sozialdienst-app-feature-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureAuszahlungComponent extends SharedFeatureAuszahlungComponent {}
