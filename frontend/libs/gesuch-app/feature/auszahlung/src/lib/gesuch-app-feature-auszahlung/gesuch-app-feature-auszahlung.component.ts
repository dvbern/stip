import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { SharedFeatureAuszahlungComponent } from '@dv/shared/feature/auszahlung';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';

@Component({
  selector: 'dv-gesuch-app-feature-auszahlung',
  imports: [TranslocoDirective, SharedUiAuszahlungComponent],
  templateUrl: './gesuch-app-feature-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureAuszahlungComponent extends SharedFeatureAuszahlungComponent {}
