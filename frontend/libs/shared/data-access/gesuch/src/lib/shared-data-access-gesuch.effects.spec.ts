import { TestScheduler } from 'rxjs/testing';

import { GesuchService } from '@dv/shared/model/gesuch';

import { loadOwnGesuchs } from './shared-data-access-gesuch.effects';
import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';

describe('sharedDataAccessGesuch Effects', () => {
  const storeUtilMock: any = {
    waitForBenutzerData$: vitest.fn(() => (s: unknown) => s),
  };
  let scheduler: TestScheduler;

  beforeEach(() => {
    scheduler = new TestScheduler((actual, expected) =>
      expect(actual).toEqual(expected),
    );
  });

  it('loads Gesuch effect - success', () => {
    scheduler.run(({ expectObservable, hot, cold }) => {
      const gesuchServiceMock = mockGesuchService({
        getGesucheGs$: () => cold('150ms a', { a: [] }),
      });

      const actionsMock$ = hot('10ms a', {
        a: SharedDataAccessGesuchEvents.init(),
      });

      const effectStream$ = loadOwnGesuchs(
        actionsMock$,
        gesuchServiceMock,
        storeUtilMock,
      );

      expectObservable(effectStream$).toBe('160ms a', {
        a: SharedDataAccessGesuchEvents.gesuchsLoadedSuccess({
          gesuchs: [],
        }),
      });
    });
  });
});

const mockGesuchService = (
  mock: Partial<Record<keyof GesuchService, any>>,
): GesuchService => {
  return mock as GesuchService;
};
