import { Signal, computed } from '@angular/core';

import { SharedModelGsDashboardView } from '@dv/shared/model/ausbildung';
import { Darlehen } from '@dv/shared/model/gesuch';

export const canCreateDarlehenFn = (
  dashBoardSig: Signal<SharedModelGsDashboardView | undefined>,
  darlehenListSig: Signal<Darlehen[] | undefined>,
): Signal<boolean> => {
  return computed(() => {
    const dashboardView = dashBoardSig();
    const darlehenList = darlehenListSig();
    if (!dashboardView || !darlehenList) {
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
};
