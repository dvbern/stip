import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Gesuch,
  GesuchService,
  Gesuchsperiode,
  GesuchsperiodeService,
} from '@dv/shared/model/gesuch';
import {
  RemoteData,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type ChangeGesuchsperiodeState = {
  assignableGesuchsperioden: RemoteData<Gesuchsperiode[]>;
  changeGesuchsperiode: RemoteData<Gesuch>;
};

const initialState: ChangeGesuchsperiodeState = {
  assignableGesuchsperioden: initial(),
  changeGesuchsperiode: initial(),
};

@Injectable()
export class ChangeGesuchsperiodeStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchsperiodeService = inject(GesuchsperiodeService);
  private gesuchService = inject(GesuchService);

  getAllAssignableGesuchsperiode$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, {
          assignableGesuchsperioden: pending(),
        });
      }),
      switchMap(() =>
        this.gesuchsperiodeService
          .getAllAssignableGesuchsperiode$()
          .pipe(
            handleApiResponse((assignableGesuchsperioden) =>
              patchState(this, { assignableGesuchsperioden }),
            ),
          ),
      ),
    ),
  );

  setGesuchsperiodeForGesuch$ = rxMethod<{
    gesuchTrancheId: string;
    gesuchsperiodeId: string;
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          changeGesuchsperiode: pending(),
        }));
      }),
      switchMap(({ gesuchTrancheId, gesuchsperiodeId, onSuccess }) =>
        this.gesuchService
          .setGesuchsperiodeForGesuch$({
            gesuchTrancheId,
            gesuchsperiodeId,
          })
          .pipe(
            handleApiResponse(
              (changeGesuchsperiode) =>
                patchState(this, { changeGesuchsperiode }),
              { onSuccess },
            ),
          ),
      ),
    ),
  );
}
