import { HttpContext, HttpContextToken } from '@angular/common/http';

import { SharedModelError } from '@dv/shared/model/error';

export const UNAUTHORIZED = new HttpContextToken<boolean>(() => false);
export const IGNORE_ERRORS = new HttpContextToken<boolean>(() => false);
export const IGNORE_NOT_FOUND_ERRORS = new HttpContextToken<boolean>(
  () => false,
);
export const NO_GLOBAL_ERRORS = new HttpContextToken<boolean>(() => false);
export const HANDLE_NOT_FOUND = new HttpContextToken<
  ((error: SharedModelError) => void) | undefined
>(() => undefined);
export const HANDLE_UNAUTHORIZED = new HttpContextToken<
  ((error: SharedModelError) => void) | undefined
>(() => undefined);

export const shouldNotAuthorizeRequestIf = (
  ignore: boolean,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(UNAUTHORIZED, ignore);
};

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

/**
 * Set this context to ignore global errors in the request error interceptor
 */
export const noGlobalErrorsIf = (
  ignore: boolean,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(NO_GLOBAL_ERRORS, ignore);
};

export const handleNotFound = (
  handler: (error: SharedModelError) => void,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(HANDLE_NOT_FOUND, handler);
};

export const handleNotFoundAndUnauthorized = (
  notFoundHandler: (error: SharedModelError) => void,
  unauthorizedHandler: (error: SharedModelError) => void,
  context: HttpContext = new HttpContext(),
) => {
  context.set(HANDLE_NOT_FOUND, notFoundHandler);
  context.set(HANDLE_UNAUTHORIZED, unauthorizedHandler);
  return context;
};
