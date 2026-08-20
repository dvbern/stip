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

    if (!versionen || versionen.length === 0) {
      return undefined;
    }

    const version = versionen.find(
      (version) => version.berechnungId === berechnungId,
    );

    return version;
  });

  private defaultLinkSig = computed((): VersionenLink => {
    const gesuchId = this.gesuchIdSig();
    const tabSegments = this.tabRouteSegmentsSig();
    const originStep = this.originStepSig();
    const currentVersion = this.currentVersionSig();
    const initial = this.initialSig();
    const firstTrancheId = this.firstCurrentTrancheIdSig();

    if (this.isInitialRouteSig() && initial?.verfuegtGesuch) {
      const vg = initial.verfuegtGesuch;
      return {
        routerLink: [
          '/',
          'gesuch',
          ...tabSegments,
          gesuchId,
          'initial',
          vg.tranchen[0].id,
        ],
        queryParams: {
          berechnungId: vg.berechnungId,
          revision: vg.tranchen[0].revision,
          originStep,
        },
        labelKey: 'verfuegt',
      };
    }
    if (this.isEingereichtRouteSig() && initial?.eingereichtGesuch) {
      const eg = initial.eingereichtGesuch;
      return {
        routerLink: [
          '/',
          'gesuch',
          ...tabSegments,
          gesuchId,
          'eingereicht',
          eg.id,
        ],
        queryParams: { revision: eg.revision, originStep },
        labelKey: 'eingereicht',
      };
    }
    if (currentVersion) {
      return {
        routerLink: [
          '/',
          'gesuch',
          ...tabSegments,
          gesuchId,
          'tranche',
          currentVersion.tranchen[0].id,
        ],
        queryParams: {
          berechnungId: currentVersion.berechnungId,
          revision: currentVersion.tranchen[0].revision,
          originStep,
        },
        labelKey: 'versionen',
        labelTimestamp: currentVersion.timestamp,
      };
    }
    return {
      routerLink: [
        '/',
        'gesuch',
        ...tabSegments,
        gesuchId,
        'tranche',
        firstTrancheId,
      ],
      queryParams: { originStep },
      labelKey: 'aktuell',
    };
  });

  selectedMenuLinkSig = signal<VersionenLink | null>(null);

  effectiveLinkSig = computed(() => {
    return this.selectedMenuLinkSig() ?? this.defaultLinkSig();
  });

  setAktuellLink(): void {
    const gesuchId = this.gesuchIdSig();
    const tabSegments = this.tabRouteSegmentsSig();
    const originStep = this.originStepSig();
    const firstTrancheId = this.firstCurrentTrancheIdSig();
    this.selectedMenuLinkSig.set({
      routerLink: [
        '/',
        'gesuch',
        ...tabSegments,
        gesuchId,
        'tranche',
        firstTrancheId,
      ],
      queryParams: { originStep },
      labelKey: 'aktuell',
    });
  }

  setVersionLink(version: VerfuegtGesuch): void {
    const gesuchId = this.gesuchIdSig();
    const tabSegments = this.tabRouteSegmentsSig();
    const originStep = this.originStepSig();
    this.selectedMenuLinkSig.set({
      routerLink: [
        '/',
        'gesuch',
        ...tabSegments,
        gesuchId,
        'tranche',
        version.tranchen[0].id,
      ],
      queryParams: {
        berechnungId: version.berechnungId,
        revision: version.tranchen[0].revision,
        originStep,
      },
      labelKey: 'versionen',
      labelTimestamp: version.timestamp,
    });
  }

  setVerfuegtLink(): void {
    const vg = this.initialSig()?.verfuegtGesuch;
    if (!vg) return;
    const gesuchId = this.gesuchIdSig();
    const tabSegments = this.tabRouteSegmentsSig();
    const originStep = this.originStepSig();
    this.selectedMenuLinkSig.set({
      routerLink: [
        '/',
        'gesuch',
        ...tabSegments,
        gesuchId,
        'initial',
        vg.tranchen[0].id,
      ],
      queryParams: {
        berechnungId: vg.berechnungId,
        revision: vg.tranchen[0].revision,
        originStep,
      },
      labelKey: 'verfuegt',
    });
  }

  setEingereichtLink(): void {
    const eg = this.initialSig()?.eingereichtGesuch;
    if (!eg) return;
    const gesuchId = this.gesuchIdSig();
    const tabSegments = this.tabRouteSegmentsSig();
    const originStep = this.originStepSig();
    this.selectedMenuLinkSig.set({
      routerLink: [
        '/',
        'gesuch',
        ...tabSegments,
        gesuchId,
        'eingereicht',
        eg.id,
      ],
      queryParams: { revision: eg.revision, originStep },
      labelKey: 'eingereicht',
    });
  }
}
