import { TestScheduler } from 'rxjs/testing';

import { loadDokumentes } from './shared-data-access-dokumente.effects';

import { SharedDataAccessDokumenteService } from './shared-data-access-dokumente.service';
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
      const sharedDataAccessDokumenteServiceMock = {
        getAll: () => cold('150ms a', { a: [] }),
      } as unknown as SharedDataAccessDokumenteService;

      const eventsMock$ = hot('10ms a', {
        // TODO replace with a trigger event (eg some page init)
        a: SharedDataAccessDokumenteApiEvents.dummy(),
      });

      const effectStream$ = loadDokumentes(
        eventsMock$,
        sharedDataAccessDokumenteServiceMock,
      );

      expectObservable(effectStream$).toBe('160ms a', {
        a: SharedDataAccessDokumenteApiEvents.dokumentesLoadedSuccess({
          dokumentes: [],
        }),
      });
    });
  });
});
