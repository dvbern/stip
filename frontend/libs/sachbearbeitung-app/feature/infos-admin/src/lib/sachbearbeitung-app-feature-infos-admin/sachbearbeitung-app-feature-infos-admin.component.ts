import { CommonModule, DOCUMENT } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatTabsModule } from '@angular/material/tabs';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { map, startWith } from 'rxjs';

import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

const ALL_TABS = [
  'verfuegung',
  'ausbildung-abbrechen',
  'ausbildung-unterbrechen',
  'ausbildung-abschliessen',
] as const;
@Component({
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet,
    TranslocoPipe,
    MatTabsModule,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './sachbearbeitung-app-feature-infos-admin.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosAdminComponent {
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
