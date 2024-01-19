import { Gesuch } from '@dv/shared/model/gesuch';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';

import { State, reducer } from './shared-data-access-gesuch.feature';
import { selectSharedDataAccessGesuchsView } from './shared-data-access-gesuch.selectors';
import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';

describe('selectSharedDataAccessGesuchsView', () => {
  it('selects view', () => {
    const state: State = {
      gesuch: null,
      gesuchs: [],
      gesuchFormular: null,
      cache: {
        gesuchFormular: null,
      },
      loading: false,
      error: undefined,
    };
    const result = selectSharedDataAccessGesuchsView.projector(state);
    expect(result.loading).toBeFalsy();
  });

  it('should correctly add gesuchFormular if successfull', () => {
    const expected: Gesuch = {
      gesuchTrancheToWorkWith: {
        gesuchFormular: { personInAusbildung: { vorname: 'Max' } },
      },
    } as Partial<Gesuch> as any;
    const action = SharedDataAccessGesuchEvents.gesuchLoadedSuccess({
      gesuch: expected,
    });
    expect(reducer(undefined, action).gesuchFormular).toEqual(
      expected.gesuchTrancheToWorkWith.gesuchFormular,
    );
  });

  it('should still have the cache if gesuchFormular is undefined', () => {
    const firstUpdate: Gesuch = {
      gesuchTrancheToWorkWith: {
        gesuchFormular: { personInAusbildung: { vorname: 'Max' } },
      },
    } as Partial<Gesuch> as any;
    const firstAction = SharedDataAccessGesuchEvents.gesuchLoadedSuccess({
      gesuch: firstUpdate,
    });
    const firstState = reducer(undefined, firstAction);
    const secondAction = SharedEventGesuchFormPerson.init();
    const secondState = reducer(firstState, secondAction);
    const result = selectSharedDataAccessGesuchsView.projector(secondState);
    expect(result.cachedGesuchFormular).toEqual(
      firstUpdate.gesuchTrancheToWorkWith.gesuchFormular,
    );
  });
});
