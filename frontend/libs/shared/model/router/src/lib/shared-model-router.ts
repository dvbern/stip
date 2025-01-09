import { NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs/operators';

export const urlAfterNavigationEnd = (router: Router) =>
  router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd),
    map((event) => event.url),
  );
