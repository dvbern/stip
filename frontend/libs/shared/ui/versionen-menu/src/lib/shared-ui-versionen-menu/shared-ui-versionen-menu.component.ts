import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { GesuchTrancheSlim, VerfuegtGesuch } from '@dv/shared/model/gesuch';
import { TRANCHE } from '@dv/shared/model/gesuch-form';

@Component({
  selector: 'dv-shared-ui-versionen-menu',
  imports: [CommonModule, RouterModule, TranslocoDirective, MatMenuModule],
  templateUrl: './shared-ui-versionen-menu.component.html',
  styleUrl: './shared-ui-versionen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiVersionenMenuComponent {
  versionenSig = input.required<VerfuegtGesuch[]>();
  initialSig = input.required<VerfuegtGesuch | undefined>();
  isInitialRouteSig = input<boolean | undefined>();
  gesuchIdSig = input.required<string | undefined>();
  isGesuchRouteSig = input<boolean | undefined>();
  trancheIdSig = input.required<string | undefined>();
  berechnungIdSig = input.required<string | undefined>();
  formularTabSig = input<string | undefined>();
  currentTranchenSig = input.required<GesuchTrancheSlim[] | undefined>();

  firstCurrentTrancheIdSig = computed(() => {
    const currentTranchen = this.currentTranchenSig();
    return currentTranchen && currentTranchen.length > 0
      ? currentTranchen[0].id
      : undefined;
  });

  tabRouteSegmentsSig = computed(() => {
    const formularTab = this.formularTabSig();

    const tab = decodeURI(formularTab ?? '') || TRANCHE.route;

    return tab.split('/').filter(Boolean);
  });

  currentVersionSig = computed(() => {
    const versionen = this.versionenSig();
    const berechnungId = this.berechnungIdSig();

    if (!versionen || versionen.length === 0) {
      return undefined;
    }

    const version = versionen.find(
      (version) => version.berechnungId === berechnungId,
    );

    return version;
  });
}
