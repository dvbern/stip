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

import { SharedModelGsDashboardView } from '@dv/shared/model/ausbildung';
import { DarlehenStatus, FreiwilligDarlehen } from '@dv/shared/model/gesuch';

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
   * If undefined, no create button is shown.
   */
  fallIdSig = input<string | undefined>();
  /**
   * Set in SB app, so link is correct!
   */
  gesuchIdSig = input<string | undefined>();
  darlehenListSig = input.required<FreiwilligDarlehen[] | undefined>();
  dashboardViewSig = input<SharedModelGsDashboardView | undefined>();
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
      {} as Record<DarlehenCompleteStates, FreiwilligDarlehen[]>,
    );
  });

  canCreateDarlehenSig = computed((): boolean => {
    const dashboardView = this.dashboardViewSig();
    const darlehenList = this.darlehenListSig() ?? [];

    if (!dashboardView) {
      return false;
    }

    const hasActiveAusbildungWithGesuchNotInBearbeitung =
      dashboardView.activeAusbildungen.some((ausbildung) =>
        ausbildung.gesuchs.some(
          (gesuch) => gesuch.gesuchStatus !== 'IN_BEARBEITUNG_GS',
        ),
      );

    const hasNoOpenDarlehen = darlehenList.every((darlehen) => {
      return (
        darlehen.status === 'ABGELEHNT' || darlehen.status === 'AKZEPTIERT'
      );
    });

    return hasActiveAusbildungWithGesuchNotInBearbeitung && hasNoOpenDarlehen;
  });
}
