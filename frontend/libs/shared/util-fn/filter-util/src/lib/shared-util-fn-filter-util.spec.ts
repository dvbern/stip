import {
  makeEmptyStringPropertiesNull,
  restrictNumberParam,
} from './shared-util-fn-filter-util';

describe('restrictNumberParam', () => {
  const restriction = { min: 0, max: 100 };
  const restrict = restrictNumberParam(restriction);

  it('should return undefined for undefined input', () => {
    expect(restrict(undefined)).toBeUndefined();
  });

  it('should return the same number for a valid input', () => {
    expect(restrict('50')).toBe(50);
  });

  it('should return the max value for an input greater than max', () => {
    expect(restrict('150')).toBe(100);
  });

  it('should return the min value for an input less than min', () => {
    expect(restrict('-10')).toBe(0);
  });
});

describe('makeEmptyStringPropertiesNull', () => {
  it('should convert empty string properties to null', () => {
    const input = {
      name: 'John',
      age: '',
      city: '',
      zip: true,
    };

    const expectedOutput = {
      name: 'John',
      age: null,
      city: null,
      zip: true,
    };

    expect(makeEmptyStringPropertiesNull(input)).toEqual(expectedOutput);
  });
});
