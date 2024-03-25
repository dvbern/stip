import {
  HttpContext,
  HttpContextToken,
  HttpHandlerFn,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { EMPTY, catchError, of, throwError } from 'rxjs';

import { SharedDataAccessGlobalNotificationEvents } from '@dv/shared/data-access/global-notification';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

const IGNORE_ERRORS = new HttpContextToken<boolean>(() => false);
const NO_GLOBAL_ERRORS = new HttpContextToken<boolean>(() => false);

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
 * Set this context to ignore global errors in the request error interceptor
 */
export const noGlobalErrorsIf = (
  ignore: boolean,
  context: HttpContext = new HttpContext(),
) => {
  return context.set(NO_GLOBAL_ERRORS, ignore);
};

export interface DvGlobalHttpErrorInterceptorFnOptions {
  /*
   * Choose the type of global error handling you want for the application.
   * - globalOnly: the global interceptor catches all http errors and dispatches them to the global notification API. Local catchErrors will never be reached.
   * - globalAndLocal: the global interceptor catches http errors and dispatches them to the notification API first. After this, the local catchErrors will be executed.
   * - without: only local catchErrors are used. Choose this options if at some point you do not want to dispatch an error to the global notification API.
   */
  type: 'without' | 'globalOnly' | 'globalAndLocal';
}

export function withDvGlobalHttpErrorInterceptorFn({
  type,
}: DvGlobalHttpErrorInterceptorFnOptions) {
  if (type === 'without') {
    return [];
  } else {
    // explicit function name is displayed in stack traces, arrow functions are anonymous
    // eslint-disable-next-line no-inner-declarations
    function HttpErrorInterceptor(
      req: HttpRequest<unknown>,
      next: HttpHandlerFn,
    ) {
      const store = inject(Store);
      return next(req).pipe(
        catchError((error) => {
          // ignore errors if the HTTP context is set to ignore errors
          if (req.context.get(IGNORE_ERRORS)) {
            return of(new HttpResponse({}));
          }

          const storableError = JSON.parse(JSON.stringify(error));
          if (!req.context.get(NO_GLOBAL_ERRORS)) {
            store.dispatch(
              SharedDataAccessGlobalNotificationEvents.httpRequestFailed({
                errors: [sharedUtilFnErrorTransformer(storableError)],
              }),
            );
          }

          if (type === 'globalOnly') {
            return EMPTY; // global errors only. Effects will never fail, no local catchErrors are reached
          } else {
            // TODO fix this: throwError stops stuff and the local error handling is not reached
            return throwError(storableError); // global errors plus local catchErrors in Effects.
          }
        }),
      );
    }

    return [HttpErrorInterceptor];
  }
}
