import { findIndexInOneOf } from './shared-util-fn-array-helper';

describe('findIndexInOneOf', () => {
  const lists = [
    [{ id: 1 }, { id: 2 }, { id: 3 }],
    [{ id: 4 }, { id: 5 }, { id: 6 }],
  ];

  it('should return -1 when no lists contain an element that satisfies the predicate', () => {
    const result = findIndexInOneOf((item) => item.id === 33, ...lists);
    expect(result).toBe(-1);
  });

  it('should return -1 when no the list of lists is empty', () => {
    const result = findIndexInOneOf(
      (item) => item.id === 1,
      ...([] as { id: number }[][]),
    );
    expect(result).toBe(-1);
  });

  it('should return the index of the first element that satisfies the predicate', () => {
    const result = findIndexInOneOf((item) => item.id === 2, ...lists);
    expect(result).toBe(1);
  });
});
