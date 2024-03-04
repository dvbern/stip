import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-overview-layout',
  standalone: true,
  imports: [
    RouterLink,
    SharedPatternAppHeaderComponent,
    SharedUiSearchComponent,
    TranslateModule,
  ],
  templateUrl: './sachbearbeitung-app-pattern-overview-layout.component.html',
  styleUrls: ['./sachbearbeitung-app-pattern-overview-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternOverviewLayoutComponent {
  @Input() closeMenu: { value?: unknown } | null = null;
}
