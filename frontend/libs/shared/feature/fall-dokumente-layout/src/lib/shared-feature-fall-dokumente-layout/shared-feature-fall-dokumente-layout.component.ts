import {
  ChangeDetectionStrategy,
  Component,
  DOCUMENT,
  HostBinding,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatTabsModule } from '@angular/material/tabs';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { map, startWith } from 'rxjs';

import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

const ALL_TABS = ['stipendien', 'darlehen'] as const;

@Component({
  selector: 'dv-shared-feature-fall-dokumente-layout',
  imports: [
    RouterOutlet,
    RouterLink,
    MatTabsModule,
    SharedUiIconChipComponent,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-feature-fall-dokumente-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureFallDokumenteLayoutComponent {
  @HostBinding('class') class = 'tw:dv-pass-height tw:p-6';

  private router = inject(Router);
  private wndw = inject(DOCUMENT, { optional: true })?.defaultView;

  activeTabSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.wndw?.location.pathname),
      startWith(this.wndw?.location.pathname),
    ),
  );

  tabsSig = computed(() => {
    const path = this.activeTabSig();
    return ALL_TABS.map((tab) => ({
      active: !!path?.endsWith(tab),
      name: tab,
    }));
  });
}
