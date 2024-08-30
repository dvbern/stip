import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import {
  Gesuch,
  SharedModelGesuchFormular,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import { ELTERN, ELTERN_STEUER_FAMILIE } from '@dv/shared/model/gesuch-form';
import { initial, success } from '@dv/shared/util/remote-data';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';
import { State, reducer } from './shared-data-access-gesuch.feature';
import {
  isGesuchFormularProp,
  selectSharedDataAccessGesuchStepsView,
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
      specificTrancheId: null,
      cache: {
        gesuch: null,
        gesuchId: null,
        gesuchFormular: null,
      },
      steuerdatenTabs: initial(),
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
      state.specificTrancheId,
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
      specificTrancheId: null,
      steuerdatenTabs: initial(),
      cache: {
        gesuch: null,
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
      warnings: undefined,
      hasDocuments: null,
    });
  });

  it('should append steuerdatenTab Familie to steps after Eltern', () => {
    const state: State = {
      gesuch: null,
      gesuchs: [],
      validations: {
        errors: [],
        hasDocuments: null,
      },
      gesuchFormular: null,
      specificTrancheId: null,
      steuerdatenTabs: success([SteuerdatenTyp.FAMILIE]),
      cache: {
        gesuch: null,
        gesuchId: null,
        gesuchFormular: null,
      },
      lastUpdate: null,
      loading: false,
      error: undefined,
    };
    const result = selectSharedDataAccessGesuchStepsView.projector(state, {
      deploymentConfig: undefined,
      compileTimeConfig: {
        appType: 'gesuch-app',
        authClientId: 'stip-gesuch-app',
      },
      loading: false,
      error: undefined,
      isGesuchApp: true,
      isSachbearbeitungApp: false,
    });
    const elternIndex = result.steps.findIndex(
      (step) => step.route === ELTERN.route,
    );
    const steuerdatenTabIndex = result.steps.findIndex(
      (step) => step.route === ELTERN_STEUER_FAMILIE.route,
    );
    expect(elternIndex + 1).toEqual(steuerdatenTabIndex);
  });
});
