import { Signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { combineLatest, distinctUntilChanged, filter, map } from 'rxjs';

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
 * Emits each time the gesuch has been updated
 */
export function getLatestGesuchIdFromGesuchOnUpdate$(
  viewSig: Signal<{
    gesuch?: { id: string } | null;
    lastUpdate: string | null;
  }>,
) {
  return combineLatest([
    // Get the last update time distinctly
    toObservable(viewSig).pipe(map(({ lastUpdate }) => lastUpdate)),
    // Get the latest gesuch id
    getLatestGesuchIdFromGesuch$(viewSig).pipe(),
  ]).pipe(
    distinctUntilChanged(
      ([lastUpdate1, gesuchId1], [lastUpdate2, gesuchId2]) =>
        lastUpdate1 === lastUpdate2 && gesuchId1 === gesuchId2,
    ),
    map(([, gesuchId]) => gesuchId),
  );
}
