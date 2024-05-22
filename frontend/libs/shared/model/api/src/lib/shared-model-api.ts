const SHARED_MODEL_API_PREFIX = 'api';
const SHARED_MODEL_API_VERSION = 'v1';
export const SHARED_MODEL_API_URL =
  `/${SHARED_MODEL_API_PREFIX}/${SHARED_MODEL_API_VERSION}` as const;

export type UpdateOrCreate<TCreate, TUpdate extends { id: string }> =
  | TUpdate
  | (TCreate & { id: 'new' });

export const isNewEntry = <
  TCreate extends { id: 'new' },
  TStandard extends { id: string },
>(
  entry: TCreate | TStandard,
): entry is TCreate => entry.id === 'new';
