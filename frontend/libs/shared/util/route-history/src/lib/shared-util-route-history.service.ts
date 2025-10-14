import { Injectable, effect, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { EventType, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SharedUtilRouteHistoryService {
  private history: NavigationEnd[] = [];
  private router = inject(Router);
  private historySig = toSignal(
    this.router.events.pipe(
      filter((event) => event.type === EventType.NavigationEnd),
    ),
  );

  constructor() {
    effect(() => {
      const newHistoryEntry = this.historySig();
      if (newHistoryEntry) {
        this.history.push(newHistoryEntry);
      }
    });
  }

  getHistory() {
    return this.history.map((entry) => ({ ...entry }));
  }

  /**
   * Navigate to the previous page in the history.
   *
   * But only if there is a previous page.
   * Otherwise it will navigate to the given `alternativeRoute`.
   *
   * This is treated as a new navigation itself, it is not a history pop.
   * The new navigation will be added to the history.
   *
   * _The angular Location service also provies a `back()` method,
   * but this could lead to a navigation back to another website
   * as it uses the browser history back functionality._
   */
  navigateToPreviousPage(alternativeRoute: string) {
    let route = alternativeRoute;
    if (this.history.length > 1) {
      route = this.history[this.history.length - 2].urlAfterRedirects;
    }

    return this.router.navigateByUrl(route);
  }
}
