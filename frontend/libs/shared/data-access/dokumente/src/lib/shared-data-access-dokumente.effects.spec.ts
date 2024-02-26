import { TestScheduler } from 'rxjs/testing';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';

import {
  GesuchServiceMock,
  loadDokumentes,
} from './shared-data-access-dokumente.effects';
import { SharedDataAccessDokumenteApiEvents } from './shared-data-access-dokumente.events';

describe('SharedDataAccessDokumente Effects', () => {
  let scheduler: TestScheduler;

  beforeEach(() => {
    scheduler = new TestScheduler((actual, expected) =>
      expect(actual).toEqual(expected),
    );
  });

  it('loads Dokumente effect - success', () => {
    scheduler.run(({ expectObservable, hot, cold }) => {
      const gesuchServiceMock = {
        getDokumente$: () => cold('150ms a', { a: [] }),
      } as unknown as GesuchServiceMock;

      const eventsMock$ = hot('10ms a', {
        a: SharedEventGesuchDokumente.init(),
      });

      const effectStream$ = loadDokumentes(eventsMock$, gesuchServiceMock);

      expectObservable(effectStream$).toBe('160ms a', {
        a: SharedDataAccessDokumenteApiEvents.dokumentesLoadedSuccess({
          dokumentes: [],
        }),
      });
    });
  });
});
