import { Injectable, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, exhaustMap, filter, map, take } from 'rxjs';

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

  waitForBenutzerData$<T>() {
    this.store.dispatch(
      SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer(),
    );
    return (source: Observable<T>) =>
      source.pipe(
        exhaustMap((value) =>
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
              return value;
            }),
          ),
        ),
      );
  }
}
