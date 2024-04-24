import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { merge, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  Gesuchsjahr,
  GesuchsjahrCreate,
  GesuchsjahrService,
  Gesuchsperiode,
  GesuchsperiodeCreate,
  GesuchsperiodeService,
  GesuchsperiodeWithDaten,
  GueltigkeitStatus,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import {
  formatBackendLocalDate,
  fromBackendLocalDate,
} from '@dv/shared/util/validator-date';

type GesuchsperiodeState = {
  gesuchsjahre: CachedRemoteData<Gesuchsjahr[]>;
  gesuchsperioden: CachedRemoteData<Gesuchsperiode[]>;
  currentGesuchsperiode: CachedRemoteData<GesuchsperiodeWithDaten>;
  currentGesuchsjahr: CachedRemoteData<Gesuchsjahr>;
};

const initialState: GesuchsperiodeState = {
  gesuchsjahre: initial(),
  gesuchsperioden: initial(),
  currentGesuchsjahr: initial(),
  currentGesuchsperiode: initial(),
};

@Injectable()
export class GesuchsperiodeStore extends signalStore(
  withState(initialState),
  withDevtools('GesuchsperiodeStore'),
) {
  private gesuchsperiodeService = inject(GesuchsperiodeService);
  private gesuchsjahrService = inject(GesuchsjahrService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  gesuchsperiodenListViewSig = computed(() => {
    return fromCachedDataSig(this.gesuchsperioden)?.map((g) => ({
      ...g,
      gesuchsperiode:
        formatBackendLocalDate(g.gesuchsperiodeStart, 'de') +
        ' - ' +
        formatBackendLocalDate(g.gesuchsperiodeStopp, 'de'),
      gesuchsjahr: fromBackendLocalDate(g.einreichefristNormal)?.getFullYear(),
      isEditable: isEditable(g),
    }));
  });

  gesuchsjahreListViewSig = computed(() => {
    return fromCachedDataSig(this.gesuchsjahre)?.map((g) => ({
      ...g,
      ausbildungsjahr: `${g.technischesJahr}/${(g.technischesJahr + 1)
        .toString()
        .slice(-2)}`,
      isEditable: isEditable(g),
    }));
  });

  currentGesuchsperiodeViewSig = computed(() => {
    return prepareView(this.currentGesuchsperiode.data());
  });

  currentGesuchsjahrViewSig = computed(() => {
    return prepareView(this.currentGesuchsjahr.data());
  });

  resetCurrentData() {
    patchState(this, {
      currentGesuchsjahr: initial(),
      currentGesuchsperiode: initial(),
    });
  }

  loadOverview$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchsperioden: cachedPending(state.gesuchsperioden),
          gesuchsjahre: cachedPending(state.gesuchsjahre),
        }));
      }),
      switchMap(() =>
        merge(
          this.gesuchsperiodeService
            .getGesuchsperioden$()
            .pipe(
              handleApiResponse((gesuchsperioden) =>
                patchState(this, { gesuchsperioden }),
              ),
            ),
          this.gesuchsjahrService
            .getGesuchsjahre$()
            .pipe(
              handleApiResponse((gesuchsjahre) =>
                patchState(this, { gesuchsjahre }),
              ),
            ),
        ),
      ),
    ),
  );

  loadGesuchsperiode$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          currentGesuchsperiode: cachedPending(state.currentGesuchsperiode),
        }));
      }),
      switchMap((id) =>
        this.gesuchsperiodeService.getGesuchsperiode$({
          gesuchsperiodeId: id,
        }),
      ),
      handleApiResponse((currentGesuchsperiode) =>
        patchState(this, { currentGesuchsperiode }),
      ),
    ),
  );

  saveGesuchsperiode$ = rxMethod<{
    gesuchsperiodeId?: string;
    gesuchsperiodenDaten: GesuchsperiodeCreate;
    onAfterSave?: (gesuchsperiode: GesuchsperiodeWithDaten) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          currentGesuchsperiode: cachedPending(state.currentGesuchsperiode),
        }));
      }),
      switchMap(({ gesuchsperiodeId, gesuchsperiodenDaten, onAfterSave }) =>
        (gesuchsperiodeId
          ? this.gesuchsperiodeService.updateGesuchsperiode$({
              gesuchsperiodeId,
              gesuchsperiodeUpdate: gesuchsperiodenDaten,
            })
          : this.gesuchsperiodeService.createGesuchsperiode$({
              gesuchsperiodeCreate: gesuchsperiodenDaten,
            })
        ).pipe(
          handleApiResponse(
            (currentGesuchsperiode) => {
              patchState(this, {
                currentGesuchsperiode,
              });
            },
            {
              onSuccess: (gesuchsperiode) => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.admin.gesuchsperiode.saveSuccess',
                });
                onAfterSave?.(gesuchsperiode);
              },
            },
          ),
        ),
      ),
    ),
  );

  deleteGesuchsperiode$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchsperioden: cachedPending(state.gesuchsperioden),
        }));
      }),
      switchMap((gesuchsperiodeId) =>
        this.gesuchsperiodeService.deleteGesuchsperiode$({ gesuchsperiodeId }),
      ),
      switchMap(() =>
        this.gesuchsperiodeService
          .getGesuchsperioden$()
          .pipe(
            handleApiResponse((gesuchsperioden) =>
              patchState(this, { gesuchsperioden }),
            ),
          ),
      ),
    ),
  );

  publishGesuchsperiode$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          currentGesuchsperiode: cachedPending(state.currentGesuchsperiode),
        }));
      }),
      switchMap((gesuchsperiodeId) =>
        this.gesuchsperiodeService
          .publishGesuchsperiode$({ gesuchsperiodeId })
          .pipe(
            handleApiResponse((currentGesuchsperiode) => {
              patchState(this, {
                currentGesuchsperiode,
              });
            }),
          ),
      ),
    ),
  );

  loadGesuchsjahr$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          currentGesuchsjahr: cachedPending(state.currentGesuchsjahr),
        }));
      }),
      switchMap((gesuchsjahrId) =>
        this.gesuchsjahrService.getGesuchsjahr$({ gesuchsjahrId }),
      ),
      handleApiResponse((currentGesuchsJahr) => {
        patchState(this, {
          currentGesuchsjahr: currentGesuchsJahr,
        });
      }),
    ),
  );

  saveGesuchsjahr$ = rxMethod<{
    gesuchsjahrId?: string;
    gesuchsjahrDaten: GesuchsjahrCreate;
    onAfterSave?: (gesuchsjahr: Gesuchsjahr) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          currentGesuchsjahr: cachedPending(state.currentGesuchsjahr),
        }));
      }),
      switchMap(({ gesuchsjahrId, gesuchsjahrDaten, onAfterSave }) =>
        (gesuchsjahrId
          ? this.gesuchsjahrService.updateGesuchsjahr$({
              gesuchsjahrId,
              gesuchsjahrUpdate: gesuchsjahrDaten,
            })
          : this.gesuchsjahrService.createGesuchsjahr$({
              gesuchsjahrCreate: gesuchsjahrDaten,
            })
        ).pipe(
          handleApiResponse(
            (currentGesuchsJahr) => {
              patchState(this, {
                currentGesuchsjahr: currentGesuchsJahr,
              });
            },
            {
              onSuccess: (gesuchsjahr) => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.admin.gesuchsjahr.saveSuccess',
                });
                onAfterSave?.(gesuchsjahr);
              },
            },
          ),
        ),
      ),
    ),
  );

  deleteGesuchsjahr$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchsjahre: cachedPending(state.gesuchsjahre),
        }));
      }),
      switchMap((gesuchsjahrId) =>
        this.gesuchsjahrService.deleteGesuchsjahr$({ gesuchsjahrId }),
      ),
      switchMap(() =>
        this.gesuchsjahrService
          .getGesuchsjahre$()
          .pipe(
            handleApiResponse((gesuchsjahre) =>
              patchState(this, { gesuchsjahre }),
            ),
          ),
      ),
    ),
  );

  publishGesuchsjahr$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          currentGesuchsjahr: cachedPending(state.currentGesuchsjahr),
        }));
      }),
      switchMap((gesuchsjahrId) =>
        this.gesuchsjahrService.publishGesuchsjahr$({ gesuchsjahrId }).pipe(
          handleApiResponse((currentGesuchsjahr) => {
            patchState(this, {
              currentGesuchsjahr,
            });
          }),
        ),
      ),
    ),
  );
}

const isEditable = <T extends { gueltigkeitStatus: GueltigkeitStatus }>(
  value: T,
) => value.gueltigkeitStatus === 'ENTWURF';

const prepareView = <T extends { gueltigkeitStatus: GueltigkeitStatus }>(
  value: T | undefined,
) => {
  if (!value) return undefined;
  const editable = isEditable(value);
  return {
    ...value,
    isEditable: editable,
    isReadonly: !editable,
  };
};
