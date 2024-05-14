import { TestScheduler } from 'rxjs/testing';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { GesuchService } from '@dv/shared/model/gesuch';

import { loadDokumentes } from './shared-data-access-dokumente.effects';
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
      const gesuchService = {
        getGesuchDokumente$: () => cold('150ms a', { a: [] }),
      } as unknown as GesuchService;

      const eventsMock$ = hot('10ms a', {
        a: SharedEventGesuchDokumente.loadDocuments({ gesuchId: '1' }),
      });

      const effectStream$ = loadDokumentes(eventsMock$, gesuchService);

      expectObservable(effectStream$).toBe('160ms a', {
        a: SharedDataAccessDokumenteApiEvents.dokumentesLoadedSuccess({
          dokumentes: [],
        }),
      });
    });
  });
});
