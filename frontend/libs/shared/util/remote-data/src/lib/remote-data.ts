import { tapResponse } from '@ngrx/operators';

type Initial = {
  type: 'initial';
  data?: never;
  error?: never;
};

export const initial = (): Initial => ({
  type: 'initial',
  data: undefined,
  error: undefined,
});

export const isInitial = (
  response: RemoteData<unknown>,
): response is Initial => {
  return response.type === 'initial';
};

type Failure = {
  type: 'failure';
  data?: never;
  error: unknown;
};

export const failure = (error: unknown): Failure => ({
  type: 'failure',
  data: undefined,
  error,
});

export const isFailure = (
  response: RemoteData<unknown>,
): response is Failure => {
  return response.type === 'failure';
};

type Pending = {
  type: 'pending';
  data?: never;
  error?: never;
};

export const pending = (): Pending => ({
  type: 'pending',
  data: undefined,
  error: undefined,
});

export const isPending = (
  response: RemoteData<unknown>,
): response is Pending => {
  return response.type === 'pending';
};

type Success<T = unknown> = {
  type: 'success';
  data: T;
  error?: never;
};

export const success = <T>(data: T): Success<T> => ({
  type: 'success',
  data,
  error: undefined,
});

export const isSuccess = <T>(
  response: RemoteData<T>,
): response is Success<T> => {
  return response.type === 'success';
};

export type RemoteData<T> = Initial | Failure | Pending | Success<T>;

export function handleApiResponse<T>(handler: (value: RemoteData<T>) => void) {
  return tapResponse<T>({
    next: (data) => {
      handler(success(data));
    },
    error: (error) => {
      handler(failure(error));
    },
  });
}
