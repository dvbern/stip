import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DarlehenBuchhaltungOverview,
  DarlehenService,
  DarlehenServiceCreateDarlehenBuchhaltungSaldokorrekturRequestParams,
  DarlehenServiceGetDarlehenBuchhaltungEntrysRequestParams,
  DatenschutzbriefOverview,
  DatenschutzbriefService,
  DatenschutzbriefTyp,
  Dokument,
  Verfuegung,
  VerfuegungService,
} from '@dv/shared/model/gesuch';
import { Extends, isDefined } from '@dv/shared/model/type-util';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type DatenschutzbriefView = Omit<
  DatenschutzbriefOverview,
  'dokument' | 'massendruckJobId'
> &
  (
    | {
        typ: Extends<DatenschutzbriefTyp, 'MANUELL'>;
        dokument: Dokument;
      }
    | {
        typ: Extends<DatenschutzbriefTyp, 'MASSENDRUCK'>;
        massendruckJobId: string;
      }
  );

type InfosAdminState = {
  verfuegungen: CachedRemoteData<Verfuegung[]>;
  darlehenBuchhaltung: CachedRemoteData<DarlehenBuchhaltungOverview>;
  datenschutzbriefeDokumente: CachedRemoteData<DatenschutzbriefOverview[]>;
};

const initialState: InfosAdminState = {
  verfuegungen: initial(),
  darlehenBuchhaltung: initial(),
  datenschutzbriefeDokumente: initial(),
};

@Injectable()
export class InfosGesuchsdokumenteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private verfuegungService = inject(VerfuegungService);
  private darlehenService = inject(DarlehenService);
  private datenschutzbriefService = inject(DatenschutzbriefService);

  verfuegungenViewSig = computed(() => {
    return {
      verfuegungen: fromCachedDataSig(this.verfuegungen),
      loading: isPending(this.verfuegungen()),
    };
  });

  darlehenBuchhaltungViewSig = computed(() => {
    return {
      darlehenBuchhaltung: fromCachedDataSig(this.darlehenBuchhaltung),
      loading: isPending(this.darlehenBuchhaltung()),
    };
  });

  datenschutzbriefeDokumenteViewSig = computed(() => {
    return {
      datenschutzbriefe: fromCachedDataSig(this.datenschutzbriefeDokumente)
        ?.map(toDatenschutzView)
        .filter(isDefined),
      loading: isPending(this.datenschutzbriefeDokumente()),
    };
  });

  loadVerfuegungDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          verfuegungen: cachedPending(state.verfuegungen),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.verfuegungService
          .getVerfuegungen$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((verfuegungen) =>
              patchState(this, { verfuegungen }),
            ),
          ),
      ),
    ),
  );

  loadDarlehenBuchhaltungEntrys$ =
    rxMethod<DarlehenServiceGetDarlehenBuchhaltungEntrysRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            darlehenBuchhaltung: cachedPending(state.darlehenBuchhaltung),
          }));
        }),
        switchMap((req) =>
          this.darlehenService.getDarlehenBuchhaltungEntrys$(req).pipe(
            handleApiResponse((darlehenBuchhaltung) => {
              patchState(this, { darlehenBuchhaltung });
            }),
          ),
        ),
      ),
    );

  createDarlehenBuchhaltungSaldokorrektur$ =
    rxMethod<DarlehenServiceCreateDarlehenBuchhaltungSaldokorrekturRequestParams>(
      pipe(
        switchMap((req) =>
          this.darlehenService
            .createDarlehenBuchhaltungSaldokorrektur$(req)
            .pipe(
              handleApiResponse(() => {
                this.loadDarlehenBuchhaltungEntrys$({ gesuchId: req.gesuchId });
              }),
            ),
        ),
      ),
    );

  loadDatenschutzbriefeDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          datenschutzbriefeDokumente: cachedPending(
            state.datenschutzbriefeDokumente,
          ),
        }));
      }),
      switchMap((req) =>
        this.datenschutzbriefService.getAllDatenschutzbriefs$(req).pipe(
          handleApiResponse((datenschutzbriefeDokumente) => {
            patchState(this, { datenschutzbriefeDokumente });
          }),
        ),
      ),
    ),
  );
}

const toDatenschutzView = (
  datenschutzbrief: DatenschutzbriefOverview,
): DatenschutzbriefView | null => {
  if (datenschutzbrief.massendruckJobId) {
    return {
      ...datenschutzbrief,
      typ: 'MASSENDRUCK',
      massendruckJobId: datenschutzbrief.massendruckJobId,
    };
  }
  if (datenschutzbrief.dokument) {
    return {
      ...datenschutzbrief,
      typ: 'MANUELL',
      dokument: datenschutzbrief.dokument,
    };
  }
  return null;
};
