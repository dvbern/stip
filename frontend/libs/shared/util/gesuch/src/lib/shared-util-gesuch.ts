import { Signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { Route } from '@angular/router';
import {
  combineLatest,
  distinctUntilChanged,
  filter,
  map,
  startWith,
} from 'rxjs';

import { Gesuchstatus } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';

/**
 * Returns the latest gesuch id from the view
 */
export function getLatestGesuchIdFromGesuch$(
  viewSig: Signal<{ gesuch?: { id: string } | null; gesuchId?: string }>,
) {
  return toObservable(viewSig).pipe(
    map((view) => view.gesuch?.id ?? view.gesuchId),
    startWith(viewSig().gesuch?.id),
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

export type StatusUebergang =
  | 'BEARBEITUNG_ABSCHLIESSEN'
  | 'ZURUECKWEISEN'
  | 'VERFUEGT'
  | 'BEREIT_FUER_BEARBEITUNG'
  | 'VERSENDET';

export const StatusUebergaengeMap: Partial<
  Record<Gesuchstatus, StatusUebergang[]>
> = {
  IN_BEARBEITUNG_SB: ['BEARBEITUNG_ABSCHLIESSEN', 'ZURUECKWEISEN'],
  IN_FREIGABE: ['VERFUEGT', 'BEREIT_FUER_BEARBEITUNG'],
  VERSANDBEREIT: ['VERSENDET'],
};

export const StatusUebergaengeOptions = {
  BEARBEITUNG_ABSCHLIESSEN: (context?: { hasAcceptedAllDokuments: boolean }) =>
    ({
      icon: 'check',
      titleKey: 'BEARBEITUNG_ABSCHLIESSEN',
      typ: 'BEARBEITUNG_ABSCHLIESSEN',
      disabledReason: context?.hasAcceptedAllDokuments
        ? undefined
        : 'DOKUMENTE_OFFEN',
    }) as const,
  ZURUECKWEISEN: () =>
    ({
      icon: 'undo',
      titleKey: 'ZURUECKWEISEN',
      typ: 'ZURUECKWEISEN',
      disabledReason: undefined,
    }) as const,
  VERFUEGT: () =>
    ({
      icon: 'done',
      titleKey: 'VERFUEGT',
      typ: 'VERFUEGT',
      disabledReason: undefined,
    }) as const,
  BEREIT_FUER_BEARBEITUNG: () =>
    ({
      icon: 'play_arrow',
      titleKey: 'BEREIT_FUER_BEARBEITUNG',
      typ: 'BEREIT_FUER_BEARBEITUNG',
      disabledReason: undefined,
    }) as const,
  VERSENDET: () =>
    ({
      icon: 'mark_email_read',
      titleKey: 'VERSENDET',
      typ: 'VERSENDET',
      disabledReason: undefined,
    }) as const,
} satisfies Record<StatusUebergang, unknown>;
