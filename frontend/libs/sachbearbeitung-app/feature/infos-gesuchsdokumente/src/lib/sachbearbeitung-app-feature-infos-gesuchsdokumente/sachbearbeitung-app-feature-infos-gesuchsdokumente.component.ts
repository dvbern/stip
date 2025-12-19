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

const ALL_TABS = ['stipendien', 'darlehen', 'datenschutzbriefe'] as const;

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-gesuchsdokumente',
  imports: [RouterOutlet, RouterLink, MatTabsModule, TranslocoPipe],
  templateUrl:
    './sachbearbeitung-app-feature-infos-gesuchsdokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosGesuchsDokumenteComponent {
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
