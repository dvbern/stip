import { CommonModule, DOCUMENT } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
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

import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

const ALL_TABS = ['verlauf', 'verwaltung'] as const;
@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-beschwerde',
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet,
    MatTabsModule,
    TranslocoPipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-beschwerde.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [paginatorTranslationProvider()],
})
export class SachbearbeitungAppFeatureInfosBeschwerdeComponent {
  private router = inject(Router);
  private wndw = inject(DOCUMENT, { optional: true })?.defaultView;
  gesuchInfoStore = inject(GesuchInfoStore);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });

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

      if (!gesuchId) {
        return;
      }

      this.gesuchInfoStore.loadGesuchInfo$({ gesuchId });
    });
  }
}
