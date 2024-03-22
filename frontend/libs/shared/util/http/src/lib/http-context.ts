import { HttpContext, HttpContextToken } from '@angular/common/http';

export const IGNORE_ERRORS = new HttpContextToken<boolean>(() => false);
export const IGNORE_NOT_FOUND_ERRORS = new HttpContextToken<boolean>(
  () => false,
);

/**
 * Set this context to ignore errors in the request error interceptor
 */
export const shouldIgnoreErrorsIf = (
  ignore: boolean,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(IGNORE_ERRORS, ignore);
};

/**
 * Set this context to ignore 404 errors in the request error interceptor
 */
export const shouldIgnoreNotFoundErrorsIf = (
  ignore: boolean,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(IGNORE_NOT_FOUND_ERRORS, ignore);
};
