import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import {
  Gesuch,
  GesuchFormular,
  GesuchTranche,
  SharedModelGesuch,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import {
  ELTERN,
  ELTERN_STEUERERKLAERUNG_FAMILIE,
} from '@dv/shared/model/gesuch-form';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { DeepPartial } from '@dv/shared/pattern/jest-test-setup';
import { initial, success } from '@dv/shared/util/remote-data';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';
import {
  State,
  reducer,
  sharedDataAccessGesuchsFeature,
} from './shared-data-access-gesuch.feature';
import {
  isGesuchFormularProp,
  prepareTranchenChanges,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from './shared-data-access-gesuch.selectors';

describe('selectSharedDataAccessGesuchsView', () => {
  it('selects view', () => {
    const state: State = {
      gesuch: null,
      gesuchs: [],
      gesuchFormular: null,
      isEditingAenderung: null,
      trancheTyp: null,
      gsDashboard: [],
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
      {
        tranchenChanges: null,
      },
      state.lastUpdate,
      state.loading,
      { gesuch: state.gesuch, gesuchFormular: state.gesuchFormular },
      state.isEditingAenderung,
      state.trancheTyp,
      {
        rolesMap: {},
        currentBenutzerRd: initial(),
        lastFetchTs: null,
      },
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
      typ: 'TRANCHE',
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
      typ: 'TRANCHE',
    });
    const firstState = reducer(undefined, firstAction);
    const secondAction = SharedEventGesuchFormPerson.init();
    const secondState = reducer(firstState, secondAction);
    const result =
      sharedDataAccessGesuchsFeature.selectCache.projector(secondState);
    expect(result.gesuchFormular).toEqual(
      firstUpdate.gesuchTrancheToWorkWith.gesuchFormular,
    );
  });

  it.each([
    [{}, 'personInAusbildung', false],
    [{}, 'partner', false],
    [{ kinds: [] }, 'kinds', true],
    [{ kinds: [] }, 'invalid-property', false],
  ] as [GesuchFormular, string, boolean][])(
    'should check if a given property is a formular prop: %s[%s] -> %s',
    (gesuchFormular, prop, expected) => {
      expect(isGesuchFormularProp(Object.keys(gesuchFormular))(prop)).toEqual(
        expected,
      );
    },
  );

  it('should append steuerdatenTab Familie to steps after Eltern', () => {
    const state: State = {
      gesuch: null,
      gesuchs: [],
      gesuchFormular: null,
      isEditingAenderung: null,
      trancheTyp: null,
      gsDashboard: [],
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
    const steuererklaerungTabIndex = result.steps.findIndex(
      (step) => step.route === ELTERN_STEUERERKLAERUNG_FAMILIE.route,
    );
    expect(elternIndex + 1).toEqual(steuererklaerungTabIndex);
  });
});

describe('selectSharedDataAccessGesuchStepsView - calculate differences', () => {
  it('should identify the changed form steps', () => {
    const originalAndChangedGS = [
      // Original
      {
        gesuchFormular: {
          elterns: [
            { id: '1', elternTyp: 'VATER', nachname: 'Muster' },
            { id: '2', elternTyp: 'MUTTER', nachname: 'Muster' },
          ],
          kinds: [{ id: '1', nachname: 'Muster' }],
        },
      },
      // Changes GS
      {
        gesuchFormular: {
          elterns: [
            { id: '1', elternTyp: 'VATER', nachname: 'Alvarez' },
            { id: '2', elternTyp: 'MUTTER', nachname: 'Sanchez' },
          ],
          kinds: [],
        },
      },
    ] satisfies DeepPartial<GesuchTranche>[] as GesuchTranche[];

    // changes SB
    const currentTrancheSB = {
      // SB
      gesuchFormular: {
        elterns: [
          { id: '1', elternTyp: 'VATER', nachname: 'Alvarez' },
          { id: '2', elternTyp: 'MUTTER', nachname: 'SanchezGS' },
        ],
        kinds: [],
      },
    } as unknown as GesuchTranche;

    const changes = prepareTranchenChanges({
      gesuchTrancheToWorkWith: currentTrancheSB,
      changes: originalAndChangedGS,
    } as SharedModelGesuch);

    expect(changes).toEqual(
      expect.objectContaining({
        sb: {
          affectedSteps: expect.arrayContaining(['elterns']),
          tranche: expect.any(Object),
        },
        gs: {
          affectedSteps: expect.arrayContaining(['elterns', 'kinds']),
          tranche: expect.any(Object),
        },
      }),
    );
  });

  it('should identify the changed form steps for gs und sb', () => {
    const originalAndChangedGS = [
      // Original
      {
        gesuchFormular: {
          personInAusbildung: { vorname: 'Max', nachname: 'Muster' },
        },
      },
      // Changes GS
      {
        gesuchFormular: {
          personInAusbildung: { vorname: 'Max', nachname: 'AlvarezGS' },
        },
      },
    ] satisfies DeepPartial<GesuchTranche>[] as GesuchTranche[];

    // changes SB
    const currentTrancheSB = {
      // SB
      gesuchFormular: {
        personInAusbildung: { vorname: 'Max', nachname: 'AlvarezSB' },
        einnahmenKosten: { vermoegen: 1000 },
      },
    } as unknown as GesuchTranche;

    const changes = prepareTranchenChanges({
      gesuchTrancheToWorkWith: currentTrancheSB,
      changes: originalAndChangedGS,
    } as SharedModelGesuch);

    expect(changes).toEqual(
      expect.objectContaining({
        sb: {
          affectedSteps: expect.arrayContaining(['personInAusbildung']),
          tranche: expect.any(Object),
        },
        gs: {
          affectedSteps: expect.arrayContaining([
            'personInAusbildung',
            'einnahmenKosten',
          ]),
          tranche: expect.any(Object),
        },
      }),
    );
  });
});
