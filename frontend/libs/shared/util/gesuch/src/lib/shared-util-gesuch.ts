import { Signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { Route } from '@angular/router';
import { combineLatest, distinctUntilChanged, filter, map } from 'rxjs';

import { Gesuchstatus } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/util-fn/type-guards';

/**
 * Returns the latest gesuch id from the view
 */
export function getLatestGesuchIdFromGesuch$(
  viewSig: Signal<{ gesuch?: { id: string } | null }>,
) {
  return toObservable(viewSig).pipe(
    map((view) => view.gesuch?.id),
    filter(isDefined),
    distinctUntilChanged(),
  );
}

/**
 * Returns the latest tranche id from the view
 */
export function getLatestTrancheIdFromGesuch$(
  viewSig: Signal<{
    trancheId?: string | null;
  }>,
) {
  return toObservable(viewSig).pipe(
    map((view) => view.trancheId),
    filter(isDefined),
    distinctUntilChanged(),
  );
}

export function getLatestTrancheIdFromGesuchOnUpdate$(
  viewSig: Signal<{
    trancheId?: string | null;
    lastUpdate: string | null;
  }>,
) {
  return combineLatest([
    // Get the last update time distinctly
    toObservable(viewSig).pipe(map(({ lastUpdate }) => lastUpdate)),
    // Get the latest gesuch id
    getLatestTrancheIdFromGesuch$(viewSig).pipe(),
  ]).pipe(
    distinctUntilChanged(
      ([lastUpdate1, trancheId1], [lastUpdate2, trancheId2]) =>
        lastUpdate1 === lastUpdate2 && trancheId1 === trancheId2,
    ),
    map(([, trancheId]) => trancheId),
  );
}

/**
 * Create 2 route configs, one with the id and one with the id and tranche id
 */
export function idAndTrancheIdRoutes<T extends Route>(route: T) {
  return [
    {
      ...route,
      path: ':id',
    },
    {
      ...route,
      path: ':id/:trancheTyp/:trancheId',
    },
  ];
}

export type StatusUebergang = 'BEARBEITUNG_ABSCHLIESSEN' | 'ZURUECKWEISEN';

export const StatusUebergaengeMap: Partial<
  Record<Gesuchstatus, StatusUebergang[]>
> = {
  IN_BEARBEITUNG_SB: ['BEARBEITUNG_ABSCHLIESSEN', 'ZURUECKWEISEN'],
};

export type StatusUebergangOption = {
  icon: string;
  titleKey: StatusUebergang;
  typ: StatusUebergang;
};

export const StatusUebergaengeOptions: Record<
  StatusUebergang,
  StatusUebergangOption
> = {
  BEARBEITUNG_ABSCHLIESSEN: {
    icon: 'check',
    titleKey: 'BEARBEITUNG_ABSCHLIESSEN',
    typ: 'BEARBEITUNG_ABSCHLIESSEN',
  },
  ZURUECKWEISEN: {
    icon: 'undo',
    titleKey: 'ZURUECKWEISEN',
    typ: 'ZURUECKWEISEN',
  },
};
