// eslint-disable-next-line @nx/enforce-module-boundaries
import { SortDirection } from '@angular/material/sort';

import { SortOrder } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';

export const restrictNumberParam =
  (restriction: { max: number; min: number }) =>
  (value: string | undefined) => {
    if (!isDefined(value)) {
      return undefined;
    }
    if (+value > restriction.max) {
      return restriction.max;
    }
    return +value < restriction.min ? restriction.min : +value;
  };

export const makeEmptyStringPropertiesNull = <T extends object>(obj: T): T => {
  return Object.entries(obj).reduce(
    (acc, [key, value]) => ({
      ...acc,
      [key]: value === '' ? null : value,
    }),
    {} as T,
  );
};

export const sortMap = {
  ['']: undefined,
  asc: 'ASCENDING',
  desc: 'DESCENDING',
} satisfies Record<SortDirection, SortOrder | undefined>;
export const inverseSortMap = {
  ASCENDING: 'asc',
  DESCENDING: 'desc',
} satisfies Record<SortOrder, SortDirection>;
