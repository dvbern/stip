import { isDefined } from '@dv/shared/model/type-util';

export const restrictNumberParam =
  (restriction: { max: number; min: number }) =>
  (value: number | undefined) => {
    if (!isDefined(value)) {
      return undefined;
    }
    if (+value > restriction.max) {
      return restriction.max;
    }
    return +value < restriction.min ? restriction.min : +value;
  };
