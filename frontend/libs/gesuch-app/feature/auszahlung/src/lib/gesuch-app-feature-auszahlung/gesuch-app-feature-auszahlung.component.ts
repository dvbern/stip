import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';

import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';

@Component({
  selector: 'dv-gesuch-app-feature-auszahlung',
  imports: [
    RouterLink,
    TranslatePipe,
    TranslateDirective,
    SharedPatternMainLayoutComponent,
    SharedUiAuszahlungComponent,
  ],
  templateUrl: './gesuch-app-feature-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureAuszahlungComponent extends SharedFeatureAuszahlungComponent {}
