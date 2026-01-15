import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  output,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { map } from 'rxjs';

import { Darlehen, DarlehenStatus } from '@dv/shared/model/gesuch';

export type IdType = 'gesuch' | 'fall';

type DarlehenCompleteStates = 'open' | 'rejected' | 'accepted';
const darlehenStatusMapping: Record<DarlehenStatus, DarlehenCompleteStates> = {
  IN_BEARBEITUNG_GS: 'open',
  EINGEGEBEN: 'open',
  IN_FREIGABE: 'open',
  ABGELEHNT: 'rejected',
  AKZEPTIERT: 'accepted',
};

@Component({
  selector: 'dv-shared-ui-darlehen-menu',
  imports: [CommonModule, TranslocoPipe, RouterModule, MatMenuModule],
  templateUrl: './shared-ui-darlehen-menu.component.html',
  styleUrl: './shared-ui-darlehen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDarlehenMenuComponent {
  /**
   * A tuple containing
   * - Type of ID to be used in the routes (either 'gesuch' or 'fall').
   * - The actual ID value.
   */
  idTypeSig = input.required<[IdType, string | undefined]>();
  darlehenListSig = input.required<Darlehen[] | undefined>();
  canCreateDarlehenSig = input<boolean | undefined>();
  createDarlehen = output<{ fallId: string }>();
  router = inject(Router);
  route = inject(ActivatedRoute);

  darlehenRouteIdSig = toSignal(
    this.route.paramMap.pipe(
      map((params) => params.get('darlehenId') || undefined),
    ),
  );

  darlehenCompletedStates: DarlehenCompleteStates[] = [
    'open',
    'rejected',
    'accepted',
  ];

  idTypeValueSig = computed(() => {
    const [type, id] = this.idTypeSig();
    return { type, id };
  });

  darlehenListByStatusSig = computed(() => {
    const darlehenList = this.darlehenListSig() ?? [];

    return darlehenList.reduce(
      (acc, darlehen) => {
        const statusKey = darlehenStatusMapping[darlehen.status!];

        if (!acc[statusKey]) {
          acc[statusKey] = [];
        }

        acc[statusKey].push(darlehen);
        return acc;
      },
      {} as Record<DarlehenCompleteStates, Darlehen[]>,
    );
  });
}
