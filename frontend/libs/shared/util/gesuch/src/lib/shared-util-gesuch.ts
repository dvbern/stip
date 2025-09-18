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

import { AvailableBenutzerRole } from '@dv/shared/model/benutzer';
import { Gesuchstatus } from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
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
  const baseConfig = {
    ...route,
    data: {
      ...(route.data ?? {}),
      // reinitialize when navigated to the same route
      shouldReuseRoute: false,
    },
  };
  return [
    {
      ...baseConfig,
      path: ':id',
    },
    {
      ...baseConfig,
      path: ':id/:trancheTyp/:trancheId',
    },
  ];
}

/**
 * Available status transitions actions for the gesuch
 */
export type StatusUebergang =
  | 'SET_TO_BEARBEITUNG'
  | 'ANSPRUCH_PRUEFEN'
  | 'BEREIT_FUER_BEARBEITUNG'
  | 'ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG'
  | 'ZURUECKWEISEN'
  | 'BEARBEITUNG_ABSCHLIESSEN'
  | 'VERFUEGT'
  | 'VERSENDET'
  | 'NEGATIVE_VERFUEGUNG_ERSTELLEN'
  | 'STATUS_PRUEFUNG_AUSLOESEN';

/**
 * A map which contains the possible status transitions for specific gesuch statuses
 */
export const StatusUebergaengeMap: Partial<
  Record<Gesuchstatus, StatusUebergang[]>
> = {
  BEREIT_FUER_BEARBEITUNG: ['SET_TO_BEARBEITUNG'],
  ANSPRUCH_MANUELL_PRUEFEN: [
    'BEREIT_FUER_BEARBEITUNG',
    'NEGATIVE_VERFUEGUNG_ERSTELLEN',
  ],
  IN_BEARBEITUNG_SB: [
    'BEARBEITUNG_ABSCHLIESSEN',
    'ZURUECKWEISEN',
    'NEGATIVE_VERFUEGUNG_ERSTELLEN',
  ],
  NICHT_ANSPRUCHSBERECHTIGT: [
    'BEREIT_FUER_BEARBEITUNG',
    'NEGATIVE_VERFUEGUNG_ERSTELLEN',
  ],
  ABKLAERUNG_DURCH_RECHSTABTEILUNG: [
    'ANSPRUCH_PRUEFEN',
    'NEGATIVE_VERFUEGUNG_ERSTELLEN',
  ],
  IN_FREIGABE: ['VERFUEGT', 'ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG'],
  VERFUEGUNG_DRUCKBEREIT: ['VERSENDET'],
  DATENSCHUTZBRIEF_DRUCKBEREIT: ['BEREIT_FUER_BEARBEITUNG'],
};

type StatusUebergangOption = {
  icon: string;
  titleKey: string;
  typ: StatusUebergang;
  allowedFor: AvailableBenutzerRole[];
  disabledReason?: string;
};

/**
 * Options for the status transitions
 */
export const StatusUebergaengeOptions: Record<
  StatusUebergang,
  (context?: {
    hasAcceptedAllDokuments: boolean;
    isInvalid: boolean;
    permissions: PermissionMap;
  }) => StatusUebergangOption
> = {
  SET_TO_BEARBEITUNG: () =>
    ({
      icon: 'edit_note',
      titleKey: 'SET_TO_BEARBEITUNG',
      typ: 'SET_TO_BEARBEITUNG',
      allowedFor: ['V0_Sachbearbeiter'],
      disabledReason: undefined,
    }) as const,
  ANSPRUCH_PRUEFEN: (context) =>
    ({
      icon: 'check_circle_outline',
      titleKey: 'ANSPRUCH_PRUEFEN',
      typ: 'ANSPRUCH_PRUEFEN',
      allowedFor: ['V0_Jurist'],
      disabledReason: context?.isInvalid ? 'VALIDIERUNG_FEHLER' : undefined,
    }) as const,
  BEARBEITUNG_ABSCHLIESSEN: (context?: {
    hasAcceptedAllDokuments: boolean;
    isInvalid: boolean;
  }) => {
    let disabledReason = undefined;
    if (!context?.hasAcceptedAllDokuments) {
      disabledReason = 'DOKUMENTE_OFFEN';
    }
    if (context?.isInvalid) {
      disabledReason = 'VALIDIERUNG_FEHLER';
    }
    return {
      icon: 'check',
      titleKey: 'BEARBEITUNG_ABSCHLIESSEN',
      typ: 'BEARBEITUNG_ABSCHLIESSEN',
      allowedFor: ['V0_Sachbearbeiter'],
      disabledReason: disabledReason,
    } as const;
  },
  ZURUECKWEISEN: () =>
    ({
      icon: 'undo',
      titleKey: 'ZURUECKWEISEN',
      typ: 'ZURUECKWEISEN',
      allowedFor: ['V0_Sachbearbeiter'],
      disabledReason: undefined,
    }) as const,
  VERFUEGT: () =>
    ({
      icon: 'done',
      titleKey: 'VERFUEGT',
      typ: 'VERFUEGT',
      allowedFor: ['V0_Freigabestelle'],
      disabledReason: undefined,
    }) as const,
  BEREIT_FUER_BEARBEITUNG: () =>
    ({
      icon: 'play_arrow',
      titleKey: 'BEREIT_FUER_BEARBEITUNG',
      typ: 'BEREIT_FUER_BEARBEITUNG',
      allowedFor: ['V0_Sachbearbeiter'],
      disabledReason: undefined,
    }) as const,
  ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG: () =>
    ({
      icon: 'play_arrow',
      titleKey: 'BEREIT_FUER_BEARBEITUNG',
      typ: 'BEREIT_FUER_BEARBEITUNG',
      allowedFor: ['V0_Freigabestelle'],
      disabledReason: undefined,
    }) as const,
  VERSENDET: () =>
    ({
      icon: 'mark_email_read',
      titleKey: 'VERSENDET',
      typ: 'VERSENDET',
      allowedFor: ['V0_Sachbearbeiter'],
      disabledReason: undefined,
    }) as const,
  NEGATIVE_VERFUEGUNG_ERSTELLEN: (context) =>
    ({
      icon: 'block',
      titleKey: 'NEGATIVE_VERFUEGUNG_ERSTELLEN',
      typ: 'NEGATIVE_VERFUEGUNG_ERSTELLEN',
      allowedFor: ['V0_Sachbearbeiter', 'V0_Jurist'],
      disabledReason: context?.permissions.canNegativVerfuegen
        ? undefined
        : 'CANNOT_NEGATIV_VERFUEGEN',
    }) as const,
  STATUS_PRUEFUNG_AUSLOESEN: () =>
    ({
      icon: 'check_circle',
      titleKey: 'STATUS_PRUEFUNG_AUSLOESEN',
      typ: 'STATUS_PRUEFUNG_AUSLOESEN',
      allowedFor: ['V0_Sachbearbeiter'],
      disabledReason: undefined,
    }) as const,
};
