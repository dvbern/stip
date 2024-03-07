import { HttpContext, HttpContextToken } from '@angular/common/http';

export const IGNORE_ERRORS = new HttpContextToken<boolean>(() => false);

/**
 * Set this context to ignore errors in the request error interceptor
 */
export const shouldIgnoreErrorsIf = (
  ignore: boolean,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(IGNORE_ERRORS, ignore);
};
