import { isDefined } from './shared-model-type-util-guard';

export type Modify<T, R> = Omit<T, keyof R> & R;
export type ModifyList<T, R> =
  T extends Array<infer U> ? Array<Modify<U, R>> : never;
export type Extends<T, U extends T> = T extends U ? T : never;

/**
 * Asserts that a value was exhausted
 *
 * @example
 * // All switch cases are handled
 * switch (value) { // value: 'a' | 'b'
 *   case 'a':
 *     return 1;
 *   case 'b':
 *     return 2;
 *   default:
 *     return assertUnreachable(value);
 * }
 */
export function assertUnreachable(unreachable: never): never {
  throw new Error('Not all cases were handled: ' + unreachable);
}

/**
 * A type guard that checks if all properties of a type are defined
 */
export const ifPropsAreDefined = <T extends Record<string, unknown>>(
  value: Partial<T>,
): value is { [K in keyof T]: Exclude<T[K], null | undefined> } => {
  return Object.values(value).every(isDefined);
};

/**
 * A generic function comparing two objects by their id property
 */
export const compareById = <T extends { id: string | number }>(a: T, b?: T) =>
  a.id === b?.id;

/**
 * A type that appends 'From' and 'To' to a given string
 * @example
 * type MyType = AppendStartEnd<'date'>; // 'dateFrom' | 'dateTo'
 * type MyType2 = AppendStartEnd<'date' | 'time'>; // 'dateFrom' | 'dateTo' | 'timeFrom' | 'timeTo'
 */
export type AppendFromTo<T extends string> = `${T}From` | `${T}To`;
