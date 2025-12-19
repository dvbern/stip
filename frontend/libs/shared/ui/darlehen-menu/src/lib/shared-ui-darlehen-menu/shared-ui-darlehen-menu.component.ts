import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  output,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelGsDashboardView } from '@dv/shared/model/ausbildung';
import { Darlehen, DarlehenStatus } from '@dv/shared/model/gesuch';

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
   * Set undefined in SB app, so no create button is shown there
   */
  fallIdSig = input.required<string | undefined>();
  /**
   * Set in SB app, so link is correct!
   */
  gesuchIdSig = input.required<string | undefined>();
  darlehenListSig = input.required<Darlehen[] | undefined>();
  dashboardViewSig = input<SharedModelGsDashboardView | undefined>();
  createDarlehen = output<{ fallId: string }>();
  router = inject(Router);
  route = inject(ActivatedRoute);

  darlehenRouteIdSig = computed(() => {
    return this.route.snapshot.paramMap.get('darlehenId');
  });

  darlehenCompletedStates: DarlehenCompleteStates[] = [
    'open',
    'rejected',
    'accepted',
  ];

  darlehenListByStatusSig = computed(() => {
    const darlehenList = this.darlehenListSig() ?? [];

    // reduce with swithch case to group by status
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

    const hasNoDarlehen = !darlehenList.some((darlehen) => {
      return (
        darlehen.status === 'IN_BEARBEITUNG_GS' ||
        darlehen.status === 'EINGEGEBEN' ||
        darlehen.status === 'IN_FREIGABE'
      );
    });

    return hasActiveAusbildungWithGesuchNotInBearbeitung && hasNoDarlehen;
  });
}
