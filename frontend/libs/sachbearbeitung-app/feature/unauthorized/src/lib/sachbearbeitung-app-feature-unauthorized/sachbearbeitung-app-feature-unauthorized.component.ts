import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import { SachbearbeitungAppPatternBasicLayoutComponent } from '@dv/sachbearbeitung-app/pattern/basic-layout';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-unauthorized',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    SachbearbeitungAppPatternBasicLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-unauthorized.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureUnauthorizedComponent {}
