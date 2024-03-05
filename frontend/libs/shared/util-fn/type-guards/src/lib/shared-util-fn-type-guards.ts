export function isDefined<T>(
  value: T | null | undefined,
): value is NonNullable<T> {
  return value !== undefined && value !== null;
}

/**
 * @deprecated use {@link isDefined} instead
 */
export function sharedUtilFnTypeGuardsIsDefined<T>(
  value: T | null | undefined,
): value is NonNullable<T> {
  return isDefined(value);
}
