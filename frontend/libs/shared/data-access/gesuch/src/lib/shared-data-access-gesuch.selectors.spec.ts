import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import { Gesuch, SharedModelGesuchFormular } from '@dv/shared/model/gesuch';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';
import { State, reducer } from './shared-data-access-gesuch.feature';
import {
  isGesuchFormularProp,
  selectSharedDataAccessGesuchValidationView,
  selectSharedDataAccessGesuchsView,
} from './shared-data-access-gesuch.selectors';

describe('selectSharedDataAccessGesuchsView', () => {
  it('selects view', () => {
    const state: State = {
      gesuch: null,
      gesuchs: [],
      validations: { errors: [], hasDocuments: null },
      gesuchFormular: null,
      cache: {
        gesuchId: null,
        gesuchFormular: null,
      },
      lastUpdate: null,
      loading: false,
      error: undefined,
    };

    const result = selectSharedDataAccessGesuchsView.projector(
      {
        deploymentConfig: undefined,
        compileTimeConfig: undefined,
        loading: false,
        error: undefined,
        isGesuchApp: true,
        isSachbearbeitungApp: false,
      },
      state.lastUpdate,
      state.loading,
      state.gesuch,
      state.gesuchFormular,
    );
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
    const result =
      selectSharedDataAccessGesuchValidationView.projector(secondState);
    expect(result.cachedGesuchFormular).toEqual(
      firstUpdate.gesuchTrancheToWorkWith.gesuchFormular,
    );
  });

  it.each([
    [{}, 'personInAusbildung', false],
    [{}, 'partner', false],
    [{ kinds: [] }, 'kinds', true],
    [{ kinds: [] }, 'invalid-property', false],
  ] as [SharedModelGesuchFormular, string, boolean][])(
    'should check if a given property is a formular prop: %s[%s] -> %s',
    (gesuchFormular, prop, expected) => {
      expect(isGesuchFormularProp(Object.keys(gesuchFormular))(prop)).toEqual(
        expected,
      );
    },
  );

  it('should select correct invalidFormularProps', () => {
    const state: State = {
      gesuch: null,
      gesuchs: [],
      validations: {
        errors: [
          { message: '', messageTemplate: '', propertyPath: 'partner' },
          { message: '', messageTemplate: '', propertyPath: 'kinds' },
          { message: '', messageTemplate: '', propertyPath: 'invalid' },
        ],
        hasDocuments: null,
      },
      gesuchFormular: {
        personInAusbildung: {} as any,
        partner: {} as any,
        kinds: [],
      },
      cache: {
        gesuchId: null,
        gesuchFormular: null,
      },
      lastUpdate: null,
      loading: false,
      error: undefined,
    };
    const result = selectSharedDataAccessGesuchValidationView.projector(state);
    expect(result.invalidFormularProps.validations).toEqual({
      errors: ['partner', 'kinds'],
      validationWarnings: undefined,
      hasDocuments: null,
    });
  });
});
