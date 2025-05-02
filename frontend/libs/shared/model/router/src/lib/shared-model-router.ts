import { toSignal } from '@angular/core/rxjs-interop';
import { EventType, NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs/operators';

/**
 * Returns the URL after a navigation end event.
 */
export const urlAfterNavigationEnd = (router: Router) =>
  router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd),
    map((event) => event.url),
  );

/**
 * Check if the current route contains a trancheId parameter and return the
 * route path without the trancheId part.
 */
export const getRelativeTrancheRoute = (router: Router) =>
  toSignal(
    router.events.pipe(
      filter((event) => event.type === EventType.ActivationEnd),
      filter((e) => 'trancheId' in e.snapshot.params),
      map((e) => {
        const paths: string[] = [];
        const stack = [e.snapshot.root];
        // Angular is giving the wrong active route here, so we need to
        // traverse the route tree to rebuild the path parts
        // https://github.com/angular/angular/issues/11023#issuecomment-752228784
        while (stack.length > 0) {
          const route = stack.pop();
          if (!route) {
            continue;
          }
          paths.push(...route.url.map((e) => e.path));
          stack.push(...route.children);
        }

        return (
          paths
            // Remove the TrancheId part from the path
            .slice(0, -1)
        );
      }),
    ),
    {
      initialValue: null,
    },
  );
