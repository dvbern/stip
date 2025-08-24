/**
 * Utility function to check if a value is defined (not null or undefined).
 *
 * @example
 * isDefined(5); // true
 * isDefined(null); // false
 * isDefined(undefined); // false
 */
export function isDefined<T>(
  value: T | null | undefined,
): value is NonNullable<T> {
  return value !== undefined && value !== null;
}

/**
 * Ensure that the passed value is of type T.
 *
 * @example
 * type<GesuchStatus>('IN_BEARBEITUNG_GS'); // typesafe 'IN_BEARBEITUNG_GS'
 */
export function type<T>(value: T): T {
  return value;
}
