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

import { GesuchAenderungs } from '@dv/shared/model/gesuch';

@Component({
  selector: 'dv-shared-ui-aenderungen-menu',
  imports: [CommonModule, MatMenuModule, TranslocoDirective, RouterModule],
  templateUrl: './shared-ui-aenderungen-menu.component.html',
  styleUrl: './shared-ui-aenderungen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiAenderungenMenuComponent {
  gesuchIdSig = input.required<string | undefined>();
  trancheIdSig = input.required<string | undefined>();
  revisionSig = input.required<number | undefined>();
  aenderungenSig = input.required<GesuchAenderungs | undefined>();
  isAenderungRouteSig = input.required<boolean | undefined>();

  currentAenderungSig = computed(() => {
    const aenderungen = this.aenderungenSig();
    const trancheId = this.trancheIdSig();
    const offeneAenderung = aenderungen?.offen;
    const akzeptierteAenderungen = aenderungen?.akzeptiert;
    const abgelehnteAenderungen = aenderungen?.abgelehnt;

    const allAenderungen = [
      ...(offeneAenderung
        ? [offeneAenderung].map((a, index) => ({ ...a, index }))
        : []),
      ...(akzeptierteAenderungen?.map((a, index) => ({ ...a, index })) ?? []),
      ...(abgelehnteAenderungen?.map((a, index) => ({ ...a, index })) ?? []),
    ];

    return allAenderungen.find((aenderung) => aenderung.id === trancheId);
  });
}
