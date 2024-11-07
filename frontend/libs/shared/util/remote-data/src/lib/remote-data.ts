import { Signal } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { Observable, catchError, of, pipe } from 'rxjs';

import { SharedModelError } from '@dv/shared/model/error';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

type Initial = {
  type: 'initial';
  data: undefined;
  error: undefined;
};

/**
 * Represents the initial state of a remote data object.
 * No data has been loaded yet.
 */
export const initial = (): Initial => ({
  type: 'initial',
  data: undefined,
  error: undefined,
});

/**
 * Checks if the given remote data object is in the initial state.
 *
 * @example
 * store.values().map(filter(isInitial))
 */
export const isInitial = (
  response: RemoteData<unknown> | CachedRemoteData<unknown>,
): response is Initial => {
  return response.type === 'initial';
};

type Failure = {
  type: 'failure';
  data: undefined;
  error: unknown;
};

type CachedFailure<T = unknown> = {
  type: 'failure';
  data: T | undefined;
  error: unknown;
};

/**
 * Represents the failure state of a remote data object.
 *
 * @example
 * service.postData$().pipe(catchError((error) => of(failure(error))))
 */
export const failure = (error: unknown): Failure => ({
  type: 'failure',
  data: undefined,
  error,
});

/**
 * Checks if the given remote data object is in the failure state.
 *
 * @example
 * store.values().map(filter(isFailure))
 */
export const isFailure = (
  response: RemoteData<unknown> | CachedRemoteData<unknown>,
): response is Failure => {
  return response.type === 'failure';
};

/**
 * Checks if the given remote data object is not in the pending state.
 *
 * @example
 * store.values().map(filter(isNotPending))
 */
export const isNotPending = <T>(
  response: RemoteData<T>,
): response is Exclude<RemoteData<T>, Pending> => {
  return response.type !== 'pending';
};

type Pending = {
  type: 'pending';
  data: undefined;
  error: undefined;
};

type CachedPending<T = unknown> = {
  type: 'pending';
  data: T | undefined;
  error: undefined;
};

/**
 * Represents the pending state of a remote data object.
 *
 * @example
 * loadAll$ = rxMethod(pipe(tap(() => patchState(store, { value: pending() })))
 */
export const pending = (): Pending => ({
  type: 'pending',
  data: undefined,
  error: undefined,
});

/**
 * Usable for cached remote data objects, the previous data is preserved in the `data` field.
 *
 * @example
 * loadValue$ = rxMethod(pipe(tap(() => patchState(store, (state) => ({ value: cachedPending(state.value) }))))
 */
export const cachedPending = <T>(
  previousRd: CachedRemoteData<T>,
): CachedRemoteData<T> => ({
  type: 'pending',
  data: previousRd.data,
  error: undefined,
});

/**
 * Checks if the given remote data object is in the pending state.
 *
 * @example
 * store.values().map(filter(isPending))
 */
export const isPending = (
  response: RemoteData<unknown> | CachedRemoteData<unknown>,
): response is Pending => {
  return response.type === 'pending';
};

/**
 * Checks if the given remote data object is in the pending state and without preserved cached `data`.
 * Only returns `true` if the `data` field is `undefined` and `remote data` is pending.
 *
 * This can for example be used to still being able to show old data but with an disabled save button.
 *
 * @example
 * pendingWithCacheSig = computed(() => isPendingWithoutCache(this.store.values()))
 */
export const isPendingWithoutCache = <T>(
  response: CachedRemoteData<T>,
): response is CachedPending<T> => {
  return response.data === undefined && isPending(response);
};

type Success<T = unknown> = {
  type: 'success';
  data: T;
  error?: never;
};

/**
 * Represents the success state of a remote data object.
 *
 * @example
 * service.getData$().pipe(map(success))
 */
export const success = <T>(data: T): Success<T> => ({
  type: 'success',
  data,
  error: undefined,
});

/**
 * Checks if the given remote data object is in the success state.
 *
 * @example
 * store.values().map(filter(isSuccess))
 */
export const isSuccess = <T>(
  response: RemoteData<T> | CachedRemoteData<T>,
): response is Success<T> => {
  return response.type === 'success';
};

/**
 * Create a Failure RemoteData object with cached data from a previous Success RemoteData object if possible.
 *
 * @example
 * this.someService
 *   .updateSomething$({
 *     id,
 *   })
 *   .pipe(
 *     handleApiResponse((something) => {
 *       patchState(this, (state) => ({
 *         something: cachedResult(state.something, something),
 *       }));
 *     })
 *   );
 */
export const cachedResult = <T>(
  previousRd: CachedRemoteData<T>,
  currentRd: CachedRemoteData<T>,
): CachedRemoteData<T> =>
  currentRd.type === 'failure' && previousRd.type === 'success'
    ? {
        type: 'failure',
        data: previousRd.data,
        error: currentRd.error,
      }
    : currentRd;

/**
 * Represents a remote data object that can be in one of the following states:
 * - initial
 * - failure
 * - pending
 * - success
 *
 * @example
 * const remoteData: RemoteData<number> = success(42);
 * // has { type: 'success', data: 42, error: undefined }
 * const pendingData: RemoteData<number> = pending();
 * // has { type: 'pending', data: undefined, error: undefined }
 */
export type RemoteData<T> = Initial | Failure | Pending | Success<T>;

/**
 * Represents a cached remote data object that can be in one of the following states:
 * - initial
 * - failure
 * - pending (cached/preserved)
 * - success
 *
 * @example
 * const remoteData: CachedRemoteData<number> = success(42);
 * // has { type: 'success', data: 42, error: undefined }
 * const cachedRemoteData: CachedRemoteData<number> = cachedPending(remoteData);
 * // has { type: 'pending', data: 42, error: undefined }
 */
export type CachedRemoteData<T> =
  | Initial
  | CachedFailure<T>
  | CachedPending<T>
  | Success<T>;

/**
 * A helper function to signalize that the extracted data is potentially cached.
 *
 * It also helps to avoid the incorrect selection of data from a deep signal,
 * which could lead to unwanted change detections.
 *
 * @example
 * // Transforms code like
 * viewSig = computed(() => store.values.data()?.map(...)
 * // to
 * viewSig = computed(() => fromCachedDataSig(store.values)?.map(...)
 */
export function fromCachedDataSig<T>(cachedRd: {
  data: Signal<T>;
}): T | undefined {
  return cachedRd.data();
}

/**
 * Transforms a cached remote data object to a shared model error object if it is in the failure state.
 */
export function transformErrorSig(
  cachedRd: CachedRemoteData<unknown>,
): SharedModelError | undefined {
  if (isFailure(cachedRd) && cachedRd.error) {
    return sharedUtilFnErrorTransformer(cachedRd.error);
  }
  return undefined;
}

/**
 * Handles the response of an API call and calls the given handler function.
 *
 * It is possible to pass additional functions that are called specifically on success or failure.
 *
 * @example
 * service.getData$().pipe(handleApiResponse((response) => {
 *   // response is either a success or a failure object of the type given by service.getData$()
 *   patchState(store, { value: response });
 * });
 *
 * @example
 * // Redirect from a URL .../create to .../:id on success
 * ...service.getData$().pipe(handleApiResponse((response) => {
 *   patchState(store, { value: response });
 * }, {
 *   onSuccess: (data) => {
 *     this.router.navigate(['..', data.id], {
 *       relativeTo: this.route,
 *     });
 *   },
 * });
 */
export function handleApiResponse<T>(
  handler: (value: Success<T> | Failure) => void,
  config?: {
    onSuccess?: (value: T) => void;
    onFailure?: (error: unknown) => void;
  },
) {
  return pipe(
    tapResponse<T>({
      next: (data) => {
        handler(success(data));
        config?.onSuccess?.(data);
      },
      error: (error) => {
        handler(failure(error));
        config?.onFailure?.(error);
      },
    }),
    catchRemoteDataError((error) => {
      handler(failure(error));
      config?.onFailure?.(error);
    }),
  );
}

/**
 * Catches an error in a remote data object and calls the given handler function.
 *
 * {@link handleApiResponse} already uses this function, so it is not necessary to use it in combination with `handleApiResponse`.
 *
 * This should always be used when a service call is made, otherwise the action will not be recognized again.
 * Use it at the end of a pipe chain inside of the `switchMap` which calls the service.
 *
 * @example
 * service.ignorableResult$().pipe(catchRemoteDataError((error) => {
 *  globalNotificationStore.createErrorNotification({ message: 'An error occurred' });
 * }));
 */
export function catchRemoteDataError<T>(
  handler?: (error: RemoteData<T>) => void,
) {
  return pipe(
    catchError<T, Observable<undefined>>((error: unknown) => {
      handler?.(failure(error));
      return of(undefined);
    }),
  );
}
