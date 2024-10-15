// Ignore file boundary for this case as it is a test for functions that will be
// used with @ngrx/signals

// eslint-disable-next-line @nx/enforce-module-boundaries
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { map, of } from 'rxjs';

import {
  cachedPending,
  catchRemoteDataError,
  failure,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isFailure,
  isInitial,
  isPending,
  isSuccess,
  pending,
  success,
} from './remote-data';

describe('RemoteData', () => {
  it('should create initial state', () => {
    expect(initial()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
  it('should check if response is initial', () => {
    expect(isInitial(initial())).toBe(true);
  });

  it('should create pending state', () => {
    expect(pending()).toEqual({
      type: 'pending',
      data: undefined,
      error: undefined,
    });
  });
  it('should check if response is pending', () => {
    expect(isPending(pending())).toBe(true);
  });

  it('should create success state', () => {
    const data = 'data';
    expect(success(data)).toEqual({
      type: 'success',
      data,
      error: undefined,
    });
  });
  it('should check if response is success', () => {
    expect(isSuccess({ type: 'success', data: 'data', error: undefined })).toBe(
      true,
    );
  });

  it('should create failure state', () => {
    const error = new Error();
    expect(failure(error)).toEqual({ type: 'failure', data: undefined, error });
  });
  it('should check if response is failure', () => {
    expect(isFailure(failure(new Error()))).toBe(true);
  });

  it('should cache value in pending state', () => {
    const previousRd = success(42);
    expect(cachedPending(previousRd)).toEqual({
      type: 'pending',
      data: 42,
      error: undefined,
    });
  });

  it('should get cached value from store', () => {
    const Store = signalStore(
      withState({ value: success(42) }),
      withMethods((state) => ({
        setValue: (value: number) =>
          patchState(state, { value: success(value) }),
      })),
    );
    const store = new Store();
    store.setValue(42);
    expect(fromCachedDataSig(store.value)).toEqual(42);
  });
});

describe('handleApiResponse', () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });
  afterEach(() => {
    jest.useRealTimers();
  });
  it('should handle success', () => {
    const handler = jest.fn();
    const data = 'data';
    of(data).pipe(handleApiResponse(handler)).subscribe();

    jest.runAllTimers();
    expect(handler).toHaveBeenCalledWith({
      type: 'success',
      data,
      error: undefined,
    });
  });
  it('should handle error', () => {
    const handler = jest.fn();
    const error = new Error();
    of(null)
      .pipe(
        map(() => {
          throw error;
        }),
        handleApiResponse(handler),
      )
      .subscribe();

    jest.runAllTimers();
    expect(handler).toHaveBeenCalledWith({
      type: 'failure',
      data: undefined,
      error,
    });
  });
  it('runs a custom success handler', () => {
    const handler = jest.fn();
    const success = jest.fn();
    const data = 'data';
    of(data)
      .pipe(handleApiResponse(handler, { onSuccess: success }))
      .subscribe();

    jest.runAllTimers();
    expect(success).toHaveBeenCalledWith(data);
  });
  it('runs a custom error handler', () => {
    const handler = jest.fn();
    const error = new Error();
    const customError = jest.fn();
    of(null)
      .pipe(
        map(() => {
          throw error;
        }),
        handleApiResponse(handler, { onFailure: customError }),
      )
      .subscribe();

    jest.runAllTimers();
    expect(customError).toHaveBeenCalledWith(error);
  });

  it('should break the pipe if an error occurs', () => {
    expect(() => {
      of(null)
        .pipe(
          map(() => {
            throw new Error();
          }),
        )
        .subscribe();

      jest.runAllTimers();
    }).toThrow();
  });

  it('catchRemoteDataError should prevent the pipe from breaking', () => {
    expect(() => {
      of(null)
        .pipe(
          map(() => {
            throw new Error();
          }),
          catchRemoteDataError(),
        )
        .subscribe();

      jest.runAllTimers();
    }).not.toThrow();
  });
});
