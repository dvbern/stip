/**
 * Find the index of the first element in one of the lists that satisfies the predicate.
 * Returns -1 if no list contains an element that satisfies the predicate.
 *
 * @example
 * const lists = [[{ id: 1 }, { id: 2 }], [{ id: 3 }, { id: 4 }]];
 * const index = findIndexInOneOf((item) => item.id === 2, ...lists);
 * console.log(index); // 1
 */
export function findIndexInOneOf<T>(
  predicate: (item: T) => boolean,
  ...lists: T[][]
): number {
  if (lists.length === 0) {
    return -1;
  }
  return Math.max(...lists.map((list) => list.findIndex(predicate)));
}
