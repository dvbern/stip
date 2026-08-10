import {
  ChangeDetectionStrategy,
  Component,
  DOCUMENT,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatTabsModule } from '@angular/material/tabs';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { map, startWith } from 'rxjs';

import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
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
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private router = inject(Router);
  private wndw = inject(DOCUMENT, { optional: true })?.defaultView;
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });
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

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      this.gesuchHeaderStore.loadHeader$({
        gesuchId,
      });
    });
  }
}
