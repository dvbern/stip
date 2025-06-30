import { toSignal } from '@angular/core/rxjs-interop';
import {
  ActivationEnd,
  EventType,
  NavigationEnd,
  Router,
} from '@angular/router';
import { GesuchUrlType } from '@dv/shared/model/gesuch';
import { lowercased } from '@dv/shared/model/type-util';
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
export const getRelativeTrancheRoute = (
  router: Router,
  trancheTyp: GesuchUrlType,
) =>
  toSignal(
    router.events.pipe(
      filter((event) => event.type === EventType.ActivationEnd),
      filter(isTrancheRoute),
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

        return [
          ...paths
            // Remove the TrancheId and TrancheTyp part from the path
            .slice(0, -2),
          // Add the target TrancheTyp to the path
          lowercased(trancheTyp),
        ];
      }),
    ),
    {
      initialValue: null,
    },
  );

export const getCurrentUrl = (document: Document) => {
  return document.location.origin;
};

const isTrancheRoute = (
  routeEvent: ActivationEnd,
): routeEvent is ActivationEnd & {
  snapshot: ActivationEnd['snapshot'] & {
    params: { trancheId: string; trancheTyp: string };
  };
} =>
  routeEvent.type === EventType.ActivationEnd &&
  'trancheId' in routeEvent.snapshot.params &&
  'trancheTyp' in routeEvent.snapshot.params;
