import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';

import { GesuchAenderungs } from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';

@Component({
  selector: 'dv-shared-ui-aenderungen-menu',
  imports: [
    CommonModule,
    MatMenuModule,
    SharedUiAdvTranslocoDirective,
    RouterModule,
  ],
  templateUrl: './shared-ui-aenderungen-menu.component.html',
  styleUrl: './shared-ui-aenderungen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiAenderungenMenuComponent {
  gesuchIdSig = input.required<string | undefined>();
  trancheIdSig = input.required<string | undefined>();
  stepRouteSegmentsSig = input.required<string[]>();
  revisionSig = input.required<number | undefined>();
  aenderungenSig = input.required<GesuchAenderungs | undefined>();
  isAenderungRouteSig = input.required<boolean | undefined>();

  aenderungenCountSig = computed(() => {
    const aenderungen = this.aenderungenSig();
    if (!aenderungen) {
      return 0;
    }
    const offeneAenderungCount = aenderungen.offen ? 1 : 0;
    const akzeptierteAenderungenCount = aenderungen.akzeptiert?.length ?? 0;
    const abgelehnteAenderungenCount = aenderungen.abgelehnt?.length ?? 0;

    return (
      offeneAenderungCount +
      akzeptierteAenderungenCount +
      abgelehnteAenderungenCount
    );
  });

  currentAenderungSig = computed(() => {
    const aenderungen = this.aenderungenSig();
    const trancheId = this.trancheIdSig();
    const revision = this.revisionSig() ?? null;
    const offeneAenderung = aenderungen?.offen;
    const eingereichteAenderung = aenderungen?.eingereicht;
    const akzeptierteAenderungen = aenderungen?.akzeptiert;
    const manuelleAenderungen = aenderungen?.manuell;
    const abgelehnteAenderungen = aenderungen?.abgelehnt;
    const fehlendeDokumente = aenderungen?.fehlendeDokumente;

    const allAenderungen = [
      ...(offeneAenderung
        ? [offeneAenderung].map((a) => ({
            ...a,
            completeState: 'open' as const,
          }))
        : []),
      ...(eingereichteAenderung
        ? [eingereichteAenderung].map((a) => ({
            ...a,
            completeState: 'eingereicht' as const,
          }))
        : []),
      ...(manuelleAenderungen?.map((a) => ({
        ...a,
        completeState: 'manual' as const,
      })) ?? []),
      ...(akzeptierteAenderungen?.map((a) => ({
        ...a,
        completeState: 'completed' as const,
      })) ?? []),
      ...(abgelehnteAenderungen?.map((a) => ({
        ...a,
        completeState: 'rejected' as const,
      })) ?? []),
      ...(fehlendeDokumente?.map((a) => ({
        ...a,
        completeState: 'fehlendeDokumente' as const,
      })) ?? []),
    ];

    return allAenderungen.find(
      (aenderung) =>
        aenderung.id === trancheId && aenderung.revision === revision,
    );
  });
}
