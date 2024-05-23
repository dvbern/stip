import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { <%= classify(name) %>Service } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type <%= classify(name) %>State = {
  cached<%= classify(name) %>: CachedRemoteData<unknown>;
  <%= name %>: RemoteData<unknown>;
};

const initialState: <%= classify(name) %>State = {
  cached<%= classify(name) %>: initial(),
  <%= name %>: initial(),
};

@Injectable()
export class <%= classify(name) %>Store extends signalStore(
  withState(initialState),
  withDevtools('<%= classify(name) %>Store'),
) {
  private <%= name %>Service = inject(<%= classify(name) %>Service);

  cached<%= classify(name) %>ListViewSig = computed(() => {
    return fromCachedDataSig(this.cached<%= classify(name) %>);
  });

  <%= name %>ViewSig = computed(() => {
    return this.<%= name %>.data();
  });

  loadCached<%= classify(name) %>$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cached<%= classify(name) %>: cachedPending(
            state.cached<%= classify(name) %>,
          ),
        }));
      }),
      switchMap(() =>
        this.<%= name %>Service.get<%= classify(name) %>$().pipe(
          handleApiResponse((cached<%= classify(name) %>) =>
            patchState(this, { cached<%= classify(name) %> }),
          ),
        ),
      ),
    ),
  );

  load<%= classify(name) %>$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          <%= name %>: pending(),
        }));
      }),
      switchMap(() =>
        this.<%= name %>Service.get<%= classify(name) %>$().pipe(
          handleApiResponse((<%= name %>) => patchState(this, { <%= name %> })),
        ),
      ),
    ),
  );

  save<%= classify(name) %>$ = rxMethod<{
    <%= name %>Id: string;
    values: unknown;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cached<%= classify(name) %>: cachedPending(
            state.cached<%= classify(name) %>,
          ),
        }));
      }),
      switchMap(({ <%= name %>Id, values }) =>
        this.<%= name %>Service.update<%= classify(name) %>$({
          <%= name %>Id,
          payload: values,
        }).pipe(
          handleApiResponse(
            (<%= name %>) => {
              patchState(this, { <%= name %> });
            },
            {
              onSuccess: (<%= name %>) => {
                // Do something after save, like showing a notification
              },
            },
          ),
        ),
      ),
    ),
  );
}
