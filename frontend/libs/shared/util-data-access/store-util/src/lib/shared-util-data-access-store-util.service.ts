import { Injectable, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  Observable,
  exhaustMap,
  filter,
  from,
  lastValueFrom,
  map,
  take,
} from 'rxjs';

import {
  SharedDataAccessBenutzerApiEvents,
  selectCurrentBenutzerRd,
} from '@dv/shared/data-access/benutzer';
import {
  isFailure,
  isInitial,
  isNotPending,
} from '@dv/shared/util/remote-data';

@Injectable({
  providedIn: 'root',
})
export class StoreUtilService {
  private store = inject(Store);

  loadAndGetBenutzerData() {
    this.store.dispatch(
      SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer(),
    );
    return lastValueFrom(
      this.store.select(selectCurrentBenutzerRd).pipe(
        filter(isNotPending),
        take(1),
        map((benutzerRd) => {
          if (isInitial(benutzerRd)) {
            throw new Error('Benutzer initialization has not started yet');
          }
          if (isFailure(benutzerRd)) {
            throw benutzerRd.error;
          }
          return benutzerRd;
        }),
      ),
    );
  }

  waitForBenutzerData$<T>() {
    return (source: Observable<T>) =>
      source.pipe(
        exhaustMap((value) =>
          from(this.loadAndGetBenutzerData()).pipe(map(() => value)),
        ),
      );
  }
}
