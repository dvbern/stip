import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { RouterLink } from '@angular/router';

import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-step-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    SharedPatternAppHeaderComponent,
    SharedUiSearchComponent,
    TranslateModule,
  ],
  templateUrl:
    './sachbearbeitung-app-pattern-gesuch-step-layout.component.html',
  styleUrls: [
    './sachbearbeitung-app-pattern-gesuch-step-layout.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchStepLayoutComponent {
  @Input() closeMenu: { value?: unknown } | null = null;
}
