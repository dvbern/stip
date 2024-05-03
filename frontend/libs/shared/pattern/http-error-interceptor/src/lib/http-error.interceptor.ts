import { HttpHandlerFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { EMPTY, catchError, of, throwError } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  HANDLE_NOT_FOUND,
  IGNORE_ERRORS,
  IGNORE_NOT_FOUND_ERRORS,
  NO_GLOBAL_ERRORS,
} from '@dv/shared/util/http';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

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
      const store = inject(GlobalNotificationStore);
      return next(req).pipe(
        catchError((error) => {
          // ignore errors if the HTTP context is set to ignore errors
          if (req.context.get(IGNORE_ERRORS)) {
            return of(new HttpResponse({}));
          }

          const storableError = JSON.parse(JSON.stringify(error));
          const errorToDispatch = sharedUtilFnErrorTransformer(storableError);
          if (
            req.context.get(IGNORE_NOT_FOUND_ERRORS) &&
            errorToDispatch.status === 404
          ) {
            return of(new HttpResponse({}));
          }

          let hasBeenHandled = false;
          if (errorToDispatch.type === 'unknownHttpError') {
            // Check for 403 FORBIDDEN
            if (errorToDispatch.status === 403) {
              store.handleForbiddenError(errorToDispatch);
              hasBeenHandled = true;
            }
            // Check for 404 NOT FOUND if a handler is provided
            const notFoundHandler = req.context.get(HANDLE_NOT_FOUND);
            if (notFoundHandler && errorToDispatch.status === 404) {
              notFoundHandler(errorToDispatch);
              hasBeenHandled = true;
            }
          }

          if (!hasBeenHandled && !req.context.get(NO_GLOBAL_ERRORS)) {
            // Show a generic error notification
            store.handleHttpRequestFailed([errorToDispatch]);
          }

          if (type === 'globalOnly') {
            return EMPTY; // global errors only. Effects will never fail, no local catchErrors are reached
          } else {
            // TODO fix this: throwError stops stuff and the local error handling is not reached
            return throwError(() => storableError); // global errors plus local catchErrors in Effects.
          }
        }),
      );
    }

    return [HttpErrorInterceptor];
  }
}
