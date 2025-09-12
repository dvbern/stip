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

const ALL_TABS = [
  'ausbildungsgang',
  'ausbildungsstaette',
  'abschluss',
] as const;
@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-ausbildungsstaette',
  imports: [RouterOutlet, RouterLink, MatTabsModule, TranslocoPipe],
  templateUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent {
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
