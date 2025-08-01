import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Observable, map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  Abschluss,
  AbschlussSlim,
  Ausbildungsgang,
  Ausbildungsstaette,
  AusbildungsstaetteService,
  AusbildungsstaetteServiceCreateAbschlussBrueckenangebotRequestParams,
  AusbildungsstaetteServiceCreateAusbildungsgangRequestParams,
  AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams,
  AusbildungsstaetteServiceGetAllAbschlussForUebersichtRequestParams,
  AusbildungsstaetteServiceGetAllAusbildungsgangForUebersichtRequestParams,
  AusbildungsstaetteServiceGetAllAusbildungsstaetteForUebersichtRequestParams,
  PaginatedAbschluss,
  PaginatedAusbildungsgang,
  PaginatedAusbildungsstaette,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type EntityTypes = 'ausbildungsgang' | 'abschluss' | 'ausbildungsstaette';

type AusbildungsstaetteState = {
  lastCreate: RemoteData<unknown>;
  availableAbschluesse: CachedRemoteData<AbschlussSlim[]>;
  ausbildungsgaenge: CachedRemoteData<PaginatedAusbildungsgang>;
  ausbildungsstaetten: CachedRemoteData<PaginatedAusbildungsstaette>;
  abschluesse: CachedRemoteData<PaginatedAbschluss>;
};

const initialState: AusbildungsstaetteState = {
  lastCreate: initial(),
  availableAbschluesse: initial(),
  ausbildungsgaenge: initial(),
  ausbildungsstaetten: initial(),
  abschluesse: initial(),
};

@Injectable()
export class AdministrationAusbildungsstaetteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private globalNotificationStore = inject(GlobalNotificationStore);
  private ausbildungsstaetteService = inject(AusbildungsstaetteService);

  ausbildungsgaengeViewSig = computed(() => {
    return this.ausbildungsgaenge.data();
  });
  abschluesseViewSig = computed(() => {
    return preparePaginatedData(this.abschluesse.data(), (e) => ({
      ...e,
      canArchive: e.aktiv && e.ausbildungskategorie === 'BRUECKENANGEBOT',
    }));
  });
  ausbildungsstaettenViewSig = computed(() => {
    return preparePaginatedData(this.ausbildungsstaetten.data(), (e) => ({
      ...e,
      canArchive: e.aktiv && !e.chShis,
    }));
  });

  loadAvailableAbschluesse$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          availableAbschluesse: cachedPending(state.availableAbschluesse),
        }));
      }),
      switchMap(() =>
        this.ausbildungsstaetteService
          .getAllAbschluessForAuswahl$()
          .pipe(
            handleApiResponse((availableAbschluesse) =>
              patchState(this, { availableAbschluesse }),
            ),
          ),
      ),
    ),
  );

  loadAusbildungsgaenge$ = rxMethod<{
    filter: AusbildungsstaetteServiceGetAllAusbildungsgangForUebersichtRequestParams;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsgaenge: cachedPending(state.ausbildungsgaenge),
        }));
      }),
      switchMap(({ filter }) =>
        this.ausbildungsstaetteService
          .getAllAusbildungsgangForUebersicht$(filter)
          .pipe(
            handleApiResponse((ausbildungsgaenge) =>
              patchState(this, { ausbildungsgaenge }),
            ),
          ),
      ),
    ),
  );

  loadAusbildungsstaetten$ = rxMethod<{
    filter: AusbildungsstaetteServiceGetAllAusbildungsstaetteForUebersichtRequestParams;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsstaetten: cachedPending(state.ausbildungsstaetten),
        }));
      }),
      switchMap(({ filter }) =>
        this.ausbildungsstaetteService
          .getAllAusbildungsstaetteForUebersicht$(filter)
          .pipe(
            handleApiResponse((ausbildungsstaetten) =>
              patchState(this, { ausbildungsstaetten }),
            ),
          ),
      ),
    ),
  );

  loadAbschluesse$ = rxMethod<{
    filter: AusbildungsstaetteServiceGetAllAbschlussForUebersichtRequestParams;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          abschluesse: cachedPending(state.abschluesse),
        }));
      }),
      switchMap(({ filter }) =>
        this.ausbildungsstaetteService
          .getAllAbschlussForUebersicht$(filter)
          .pipe(
            handleApiResponse((abschluesse) =>
              patchState(this, { abschluesse }),
            ),
          ),
      ),
    ),
  );

  createAusbildungsgang$ = rxMethod<{
    values: AusbildungsstaetteServiceCreateAusbildungsgangRequestParams;
    onSuccess?: (ausbildungsgang: Ausbildungsgang) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsgaenge: cachedPending(state.ausbildungsgaenge),
        }));
      }),
      switchMap(({ values, onSuccess }) =>
        this.ausbildungsstaetteService.createAusbildungsgang$(values).pipe(
          handleApiResponse(
            (ausbildungsgang) => {
              patchState(this, { lastCreate: ausbildungsgang });
            },
            {
              onSuccess: (value) => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsgang.createDialog.success',
                });
                onSuccess?.(value);
              },
            },
          ),
        ),
      ),
    ),
  );

  createAusbildungsstaette$ = rxMethod<{
    values: AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams;
    onSuccess?: (ausbildungsstaette: Ausbildungsstaette) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsstaetten: cachedPending(state.ausbildungsstaetten),
        }));
      }),
      switchMap(({ values, onSuccess }) =>
        this.ausbildungsstaetteService.createAusbildungsstaette$(values).pipe(
          handleApiResponse(
            (ausbildungsstaette) => {
              patchState(this, { lastCreate: ausbildungsstaette });
            },
            {
              onSuccess: (value) => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsstaette.createDialog.success',
                });
                onSuccess?.(value);
              },
            },
          ),
        ),
      ),
    ),
  );

  createAbschluss$ = rxMethod<{
    values: AusbildungsstaetteServiceCreateAbschlussBrueckenangebotRequestParams;
    onSuccess?: (abschluss: Abschluss) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          abschluesse: cachedPending(state.abschluesse),
        }));
      }),
      switchMap(({ values, onSuccess }) =>
        this.ausbildungsstaetteService
          .createAbschlussBrueckenangebot$(values)
          .pipe(
            handleApiResponse(
              (abschluss) => {
                patchState(this, { lastCreate: abschluss });
              },
              {
                onSuccess: (value) => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschluss.createDialog.success',
                  });
                  onSuccess?.(value);
                },
              },
            ),
          ),
      ),
    ),
  );

  archiveEntity$ = rxMethod<{
    type: EntityTypes;
    id: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          abschluesse: cachedPending(state.abschluesse),
        }));
      }),
      switchMap(({ type, id, onSuccess }) => {
        const ignoreResult = (observable: Observable<unknown>) =>
          observable.pipe(map(() => undefined));

        const servicesCalls$ = {
          ausbildungsgang: () =>
            ignoreResult(
              this.ausbildungsstaetteService.setAusbildungsgangInaktiv$({
                ausbildungsgangId: id,
              }),
            ),
          abschluss: () =>
            ignoreResult(
              this.ausbildungsstaetteService.setAbschlussInaktiv$({
                abschlussId: id,
              }),
            ),
          ausbildungsstaette: () =>
            ignoreResult(
              this.ausbildungsstaetteService.setAusbildungsstaetteInaktiv$({
                ausbildungsstaetteId: id,
              }),
            ),
        };

        return servicesCalls$[type]().pipe(
          handleApiResponse(() => undefined, {
            onSuccess: () => {
              this.globalNotificationStore.createSuccessNotification({
                messageKey: `sachbearbeitung-app.feature.administration.ausbildungsstaette.${type}.archiveDialog.success`,
              });
              onSuccess();
            },
          }),
        );
      }),
    ),
  );
}

const preparePaginatedData = <List extends { entries?: unknown[] }, R>(
  data: List | undefined,
  mapper: (entry: Exclude<List['entries'], undefined>[number]) => R,
) => {
  return data
    ? {
        ...data,
        entries: data.entries?.map(mapper),
      }
    : data;
};
