export function isDefined<T>(
  value: T | null | undefined,
): value is NonNullable<T> {
  return value !== undefined && value !== null;
}

export function type<T>(value: T): T {
  return value;
}

/**
 * @deprecated use {@link isDefined} instead
 */
export function sharedUtilFnTypeGuardsIsDefined<T>(
  value: T | null | undefined,
): value is NonNullable<T> {
  return isDefined(value);
}

export function assertUnreachable(unreachable: never): never {
  throw new Error('Not all cases were handled: ' + unreachable);
}
