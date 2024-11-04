import { HttpHandlerFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { EMPTY, Observable, catchError, of, throwError } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelError } from '@dv/shared/model/error';
import {
  HANDLE_NOT_FOUND,
  HANDLE_UNAUTHORIZED,
  IGNORE_ERRORS,
  IGNORE_NOT_FOUND_ERRORS,
  NO_GLOBAL_ERRORS,
} from '@dv/shared/util/http';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

export interface DvGlobalHttpErrorInterceptorFnOptions {
  /**
   * Choose the type of global error handling you want for the application.
   * - globalOnly: the global interceptor catches all http errors and dispatches them to the global notification API. Local catchErrors will never be reached.
   * - globalAndLocal: the global interceptor catches http errors and dispatches them to the notification API first. After this, the local catchErrors will be executed.
   * - without: only local catchErrors are used. Choose this options if at some point you do not want to dispatch an error to the global notification API.
   */
  type: 'without' | 'globalOnly' | 'globalAndLocal';
}

const handledError = <T>(value?: Observable<HttpResponse<T>>) => ({
  handled: true,
  result: value,
});
const unhandledError = { handled: false, result: undefined };

export function withDvGlobalHttpErrorInterceptorFn({
  type,
}: DvGlobalHttpErrorInterceptorFnOptions) {
  if (type === 'without') {
    return [];
  } else {
    // explicit function name is displayed in stack traces, arrow functions are anonymous

    function HttpErrorInterceptor(
      req: HttpRequest<unknown>,
      next: HttpHandlerFn,
    ) {
      const store = inject(GlobalNotificationStore);
      const oauth = inject(OAuthService);
      const router = inject(Router);

      const knownHandlers = [
        // This list is ordered by priority, the first handler that returns a result will prevent the others from being executed
        handleUnknownHttpError(req, oauth, router, store),
        handle404(req),
      ];

      return next(req).pipe(
        catchError((error) => {
          // ignore errors if the HTTP context is set to ignore errors
          if (req.context.get(IGNORE_ERRORS)) {
            return of(new HttpResponse({}));
          }

          const storableError = JSON.parse(JSON.stringify(error));
          const errorToDispatch = sharedUtilFnErrorTransformer(storableError);
          let hasBeenHandled = false;

          // Run all known handlers and stop if one returns a result
          for (const handler of knownHandlers) {
            const { handled, result } = handler(errorToDispatch);
            if (result) {
              return result;
            }
            hasBeenHandled = hasBeenHandled || handled;
          }

          // Only dispatch the error if it has not been handled yet and the global errors are not disabled
          if (!hasBeenHandled && !req.context.get(NO_GLOBAL_ERRORS)) {
            // Show a generic error notification
            store.handleHttpRequestFailed([errorToDispatch]);
          }

          if (type === 'globalOnly') {
            return EMPTY; // global errors only. Effects will never fail, no local catchErrors are reached
          } else {
            return throwError(() => storableError); // global errors plus local catchErrors in Effects.
          }
        }),
      );
    }

    return [HttpErrorInterceptor];
  }
}

const handleUnknownHttpError = (
  req: HttpRequest<unknown>,
  oauth: OAuthService,
  router: Router,
  notificationStore: GlobalNotificationStore,
) => {
  return (error: SharedModelError) => {
    if (error.type === 'unknownHttpError') {
      if (error.status === 401) {
        const unauthorizedHandler = req.context.get(HANDLE_UNAUTHORIZED);
        if (unauthorizedHandler) {
          unauthorizedHandler(error);
        } else {
          oauth.logOut(false, router.url);
        }
        return handledError(EMPTY);
      }
      // Check for 403 FORBIDDEN
      if (error.status === 403) {
        notificationStore.handleForbiddenError(error);
        return handledError();
      }
      // Check for 404 NOT FOUND if a handler is provided
      const notFoundHandler = req.context.get(HANDLE_NOT_FOUND);
      if (notFoundHandler && error.status === 404) {
        notFoundHandler(error);
        return handledError();
      }
    }
    return unhandledError;
  };
};

const handle404 = (req: HttpRequest<unknown>) => (error: SharedModelError) => {
  if (req.context.get(IGNORE_NOT_FOUND_ERRORS) && error.status === 404) {
    return handledError(of(new HttpResponse({})));
  }
  return unhandledError;
};
