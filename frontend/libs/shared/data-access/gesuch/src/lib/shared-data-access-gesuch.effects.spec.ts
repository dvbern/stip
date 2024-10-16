import { TestScheduler } from 'rxjs/testing';

import { GesuchService } from '@dv/shared/model/gesuch';

import { loadOwnGesuchs } from './shared-data-access-gesuch.effects';
import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';

describe('sharedDataAccessGesuch Effects', () => {
  const storeUtilMock: any = {
    waitForBenutzerData$: jest.fn(() => (s: unknown) => s),
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

  //   it('loads gesuch initially and then debounced', () => {
  //     scheduler.run(({ expectObservable, hot, cold }) => {
  //       const gesuch1 = { id: '1' } as SharedModelGesuch;
  //       const gesuch2 = { id: '2' } as SharedModelGesuch;

  //       const gesuchServiceMock = mockGesuchService({
  //         getGesucheSb$: (
  //           ...args: Parameters<GesuchService['getGesucheSb$']>
  //         ) => {
  //           if (args[0].getGesucheSBQueryType === 'ALLE_BEARBEITBAR_MEINE') {
  //             return cold('a', { a: [gesuch1] });
  //           }
  //           return cold('a', { a: [gesuch1, gesuch2] });
  //         },
  //       });

  //       const debounced = `${LOAD_ALL_DEBOUNCE_TIME}ms`;

  //       /**
  //        * (A) Initial load                                          -> should fire
  //        * (B) Multiple debounced loads with same filter             -> should fire (only once)
  //        * (B) Another load with same filter but after debounce time -> should not fire (as it hasn't changed)
  //        * (C) Debounced load with different filter                  -> should fire
  //        */
  //       const actionsMock$ = hot(`abb ${debounced} --bc`, {
  //         a: SharedDataAccessGesuchEvents.loadAll({
  //           query: 'ALLE_BEARBEITBAR_MEINE',
  //         }),
  //         b: SharedDataAccessGesuchEvents.loadAllDebounced({
  //           query: 'ALLE',
  //         }),
  //         c: SharedDataAccessGesuchEvents.loadAllDebounced({
  //           query: 'ALLE_BEARBEITBAR_MEINE',
  //         }),
  //       });

  //       const effectStream$ = loadAllGesuchs(
  //         actionsMock$,
  //         gesuchServiceMock,
  //         storeUtilMock,
  //       );

  //       /**
  //        * (A) Initial load
  //        * (B) Only one debounced load with more gesuchs
  //        * (A) Debounced load with less gesuchs again
  //        */
  //       expectObservable(effectStream$).toBe(
  //         `a- ${debounced} b ${debounced} ---a`,
  //         {
  //           a: SharedDataAccessGesuchEvents.gesuchsLoadedSuccess({
  //             gesuchs: [gesuch1],
  //           }),
  //           b: SharedDataAccessGesuchEvents.gesuchsLoadedSuccess({
  //             gesuchs: [gesuch1, gesuch2],
  //           }),
  //         },
  //       );
  //     });
  //   });
});

const mockGesuchService = (
  mock: Partial<Record<keyof GesuchService, any>>,
): GesuchService => {
  return mock as GesuchService;
};
