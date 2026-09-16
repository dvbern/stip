import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
  signal,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';

import {
  GesuchTrancheSlim,
  InitialGesuchs,
  VerfuegtGesuch,
} from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';

interface VersionenLink {
  routerLink: (string | undefined)[];
  queryParams: Record<string, unknown>;
  labelKey: 'aktuell' | 'versionen' | 'initial' | 'eingereicht' | 'verfuegt';
  labelTimestamp?: string;
}

@Component({
  selector: 'dv-shared-ui-versionen-menu',
  imports: [
    CommonModule,
    RouterModule,
    MatMenuModule,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-ui-versionen-menu.component.html',
  styleUrl: './shared-ui-versionen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiVersionenMenuComponent {
  versionenSig = input.required<VerfuegtGesuch[]>();
  initialSig = input.required<InitialGesuchs | undefined>();
  isInitialRouteSig = input.required<boolean | undefined>();
  isEingereichtRouteSig = input.required<boolean | undefined>();
  gesuchIdSig = input.required<string | undefined>();
  isGesuchRouteSig = input.required<boolean | undefined>();
  trancheIdSig = input.required<string | undefined>();
  berechnungIdSig = input.required<string | undefined>();
  originStepSig = input.required<string>();
  currentTranchenSig = input.required<GesuchTrancheSlim[] | undefined>();

  firstCurrentTrancheIdSig = computed(() => {
    const currentTranchen = this.currentTranchenSig();
    return currentTranchen && currentTranchen.length > 0
      ? currentTranchen[0].id
      : undefined;
  });

  tabRouteSegmentsSig = computed(() => {
    const originStep = this.originStepSig();

    return originStep.split('/').filter(Boolean);
  });

  currentVersionSig = computed(() => {
    const versionen = this.versionenSig();
    const berechnungId = this.berechnungIdSig();

    return versionen.find((version) => version.berechnungId === berechnungId);
  });

  private selectedMenuLinkSig = signal<VersionenLink | null>(null);

  effectiveLinkSig = computed(
    () => this.selectedMenuLinkSig() ?? this.defaultLinkSig(),
  );

  linksSig = computed(() => {
    const versionen = this.versionenSig();
    const initial = this.initialSig();
    const firstTrancheId = this.firstCurrentTrancheIdSig();
    const vg = initial?.verfuegtGesuch;
    const eg = initial?.eingereichtGesuch;

    return {
      aktuell: this.buildLink('tranche', firstTrancheId, 'aktuell'),
      versionen: versionen.map((version) => ({
        version,
        link: this.buildLink('tranche', version.tranchen[0].id, 'versionen', {
          berechnungId: version.berechnungId,
          revision: version.tranchen[0].revision,
          labelTimestamp: version.timestamp,
        }),
      })),
      verfuegt: vg
        ? this.buildLink('initial', vg.tranchen[0].id, 'verfuegt', {
            berechnungId: vg.berechnungId,
            revision: vg.tranchen[0].revision,
          })
        : undefined,
      eingereicht: eg
        ? this.buildLink('eingereicht', eg.id, 'eingereicht', {
            revision: eg.revision,
          })
        : undefined,
    };
  });

  private defaultLinkSig = computed((): VersionenLink => {
    const links = this.linksSig();
    const currentVersion = this.currentVersionSig();

    if (this.isInitialRouteSig() && links.verfuegt) {
      return links.verfuegt;
    }
    if (this.isEingereichtRouteSig() && links.eingereicht) {
      return links.eingereicht;
    }
    if (currentVersion) {
      const match = links.versionen.find(
        (item) => item.version.berechnungId === currentVersion.berechnungId,
      );
      if (match) {
        return match.link;
      }
    }
    return links.aktuell;
  });

  select(link: VersionenLink): void {
    this.selectedMenuLinkSig.set(link);
  }

  private buildLink(
    type: 'tranche' | 'initial' | 'eingereicht',
    trancheId: string | undefined,
    labelKey: VersionenLink['labelKey'],
    context?: {
      berechnungId?: string;
      revision?: number;
      labelTimestamp?: string;
    },
  ): VersionenLink {
    const gesuchId = this.gesuchIdSig();
    const tabSegments = this.tabRouteSegmentsSig();
    const originStep = this.originStepSig();

    return {
      routerLink: ['/', 'gesuch', ...tabSegments, gesuchId, type, trancheId],
      queryParams: {
        ...(context?.berechnungId
          ? { berechnungId: context.berechnungId }
          : {}),
        ...(context?.revision !== undefined
          ? { revision: context.revision }
          : {}),
        originStep,
      },
      labelKey,
      labelTimestamp: context?.labelTimestamp,
    };
  }
}
