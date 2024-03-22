type Initial = {
  type: 'initial';
  data?: never;
  error?: never;
};

export const initial = () => ({
  type: 'initial' as const,
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
  error: Error;
};

export const failure = (error: Error) => ({
  type: 'failure' as const,
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

export const pending = () => ({
  type: 'pending' as const,
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

export const success = <T>(data: T) => ({
  type: 'success' as const,
  data,
  error: undefined,
});

export const isSuccess = <T>(
  response: RemoteData<T>,
): response is Success<T> => {
  return response.type === 'success';
};

export type RemoteData<T> = Initial | Failure | Pending | Success<T>;
