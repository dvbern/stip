import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { merge, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  Gesuchsjahr,
  GesuchsjahrCreate,
  GesuchsjahrService,
  Gesuchsperiode,
  GesuchsperiodeCreate,
  GesuchsperiodeService,
  GesuchsperiodeWithDaten,
  GueltigkeitStatus,
  NullableGesuchsperiodeWithDaten,
  StatusColor,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { formatBackendLocalDate } from '@dv/shared/util/validator-date';

type GesuchsperiodeState = {
  gesuchsjahre: CachedRemoteData<Gesuchsjahr[]>;
  gesuchsperioden: CachedRemoteData<Gesuchsperiode[]>;
  latestGesuchsperiode: CachedRemoteData<NullableGesuchsperiodeWithDaten>;
  currentGesuchsperiode: CachedRemoteData<GesuchsperiodeWithDaten>;
  currentGesuchsjahr: CachedRemoteData<Gesuchsjahr>;
};

const initialState: GesuchsperiodeState = {
  gesuchsjahre: initial(),
  gesuchsperioden: initial(),
  latestGesuchsperiode: initial(),
  currentGesuchsjahr: initial(),
  currentGesuchsperiode: initial(),
};

@Injectable()
export class GesuchsperiodeStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('GesuchsperiodeStore'),
) {
  private gesuchsperiodeService = inject(GesuchsperiodeService);
  private gesuchsjahrService = inject(GesuchsjahrService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  gesuchsperiodenListViewSig = computed(() => {
    return fromCachedDataSig(this.gesuchsperioden)?.map((g) =>
      prepareView({
        ...g,
        gesuchsperiode:
          formatBackendLocalDate(g.gesuchsperiodeStart, 'de') +
          ' - ' +
          formatBackendLocalDate(g.gesuchsperiodeStopp, 'de'),
      }),
    );
  });

  gesuchsjahreListViewSig = computed(() => {
    return fromCachedDataSig(this.gesuchsjahre)?.map((g) =>
      prepareView({
        ...g,
        ausbildungsjahr: `${g.technischesJahr}/${(g.technischesJahr + 1)
          .toString()
          .slice(-2)}`,
      }),
    );
  });

  currentGesuchsperiodeViewSig = computed(() => {
    return prepareNullableView(this.currentGesuchsperiode.data());
  });

  currentGesuchsjahrViewSig = computed(() => {
    return prepareNullableView(this.currentGesuchsjahr.data());
  });

  latestGesuchsperiodeViewSig = computed(() => {
    return prepareNullableView(this.latestGesuchsperiode.data()?.value);
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
        this.gesuchsperiodeService
          .getGesuchsperiode$({
            gesuchsperiodeId: id,
          })
          .pipe(
            handleApiResponse((currentGesuchsperiode) =>
              patchState(this, { currentGesuchsperiode }),
            ),
          ),
      ),
    ),
  );

  loadLatestGesuchsperiode$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          latestGesuchsperiode: cachedPending(state.latestGesuchsperiode),
        }));
      }),
      switchMap(() =>
        this.gesuchsperiodeService
          .getLatest$()
          .pipe(
            handleApiResponse((latestGesuchsperiode) =>
              patchState(this, { latestGesuchsperiode }),
            ),
          ),
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
        this.gesuchsjahrService.getGesuchsjahr$({ gesuchsjahrId }).pipe(
          handleApiResponse((currentGesuchsJahr) => {
            patchState(this, {
              currentGesuchsjahr: currentGesuchsJahr,
            });
          }),
        ),
      ),
    ),
  );

  loadAllGesuchsjahre$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchsjahre: cachedPending(state.gesuchsjahre),
        }));
      }),
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

const statusColorMap: Record<GueltigkeitStatus, StatusColor> = {
  ENTWURF: 'caution',
  ARCHIVIERT: 'primary',
  PUBLIZIERT: 'success',
} as const;

const prepareView = <T extends { gueltigkeitStatus: GueltigkeitStatus }>(
  value: T,
) => {
  const editable = isEditable(value);
  return {
    ...value,
    statusColor: statusColorMap[value.gueltigkeitStatus],
    isEditable: editable,
    isReadonly: !editable,
  };
};

const prepareNullableView = <
  T extends { gueltigkeitStatus: GueltigkeitStatus },
>(
  value: T | undefined | null,
) => (value ? prepareView(value) : undefined);
