import {
  ChangeDetectionStrategy,
  Component,
  DOCUMENT,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatTabsModule } from '@angular/material/tabs';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { map, startWith } from 'rxjs';

import { urlAfterNavigationEnd } from '@dv/shared/model/router';

const ALL_TABS = ['fall-dokumente', 'darlehen-dokumente'] as const;

@Component({
  selector: 'dv-shared-feature-fall-dokumente-layout',
  imports: [RouterOutlet, RouterLink, MatTabsModule, TranslocoPipe],
  templateUrl: './shared-feature-fall-dokumente-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureFallDokumenteLayoutComponent {
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
