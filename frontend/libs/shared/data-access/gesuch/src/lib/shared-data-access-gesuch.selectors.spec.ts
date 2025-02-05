import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import {
  Gesuch,
  GesuchTranche,
  SharedModelGesuch,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularPropsSteuerdatenSteps,
  Steuerdaten,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import { ELTERN, ELTERN_STEUER_FAMILIE } from '@dv/shared/model/gesuch-form';
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
  ] as [SharedModelGesuchFormular, string, boolean][])(
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
    const steuerdatenTabIndex = result.steps.findIndex(
      (step) => step.route === ELTERN_STEUER_FAMILIE.route,
    );
    expect(elternIndex + 1).toEqual(steuerdatenTabIndex);
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

  it.each([
    [
      'Simple Family Change',
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 2000 }],
      ['steuerdaten'],
    ],
    [
      'Simple Mother Change',
      [{ id: '1', steuerdatenTyp: 'MUTTER', eigenmietwert: 500 }],
      [{ id: '1', steuerdatenTyp: 'MUTTER', eigenmietwert: 1000 }],
      ['steuerdatenMutter'],
    ],
    [
      'Simple Father Change',
      [{ id: '1', steuerdatenTyp: 'VATER', eigenmietwert: 300 }],
      [{ id: '1', steuerdatenTyp: 'VATER', eigenmietwert: 600 }],
      ['steuerdatenVater'],
    ],
    [
      'From Family to Mother and Father',
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      [
        { id: '1', steuerdatenTyp: 'MUTTER', eigenmietwert: 500 },
        { id: '2', steuerdatenTyp: 'VATER', eigenmietwert: 300 },
      ],
      ['steuerdatenVater', 'steuerdatenMutter'],
    ],
    [
      'From Family to Mother',
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      [{ id: '1', steuerdatenTyp: 'MUTTER', eigenmietwert: 500 }],
      ['steuerdatenMutter'],
    ],
    [
      'From Family to Father',
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      [{ id: '1', steuerdatenTyp: 'VATER', eigenmietwert: 300 }],
      ['steuerdatenVater'],
    ],
    [
      'From Mother and Father to Family',
      [
        { id: '1', steuerdatenTyp: 'MUTTER', eigenmietwert: 500 },
        { id: '2', steuerdatenTyp: 'VATER', eigenmietwert: 300 },
      ],
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      ['steuerdaten'],
    ],
    [
      'From Mother to Family',
      [{ id: '1', steuerdatenTyp: 'MUTTER', eigenmietwert: 500 }],
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      ['steuerdaten'],
    ],
    [
      'From Father to Family',
      [{ id: '1', steuerdatenTyp: 'VATER', eigenmietwert: 300 }],
      [{ id: '1', steuerdatenTyp: 'FAMILIE', eigenmietwert: 1000 }],
      ['steuerdaten'],
    ],
  ] satisfies [
    string,
    DeepPartial<Steuerdaten[]>,
    DeepPartial<Steuerdaten[]>,
    SharedModelGesuchFormularPropsSteuerdatenSteps[],
  ][])(
    'should identify correctly the steuerdaten changes: %s',
    (_, steuerdatenA, steuerdatenB, affectedSteps) => {
      const [original, changed] = [
        {
          gesuchFormular: {
            steuerdaten: steuerdatenA,
          },
        },
        {
          gesuchFormular: {
            steuerdaten: steuerdatenB,
          },
        },
      ] satisfies DeepPartial<GesuchTranche>[] as GesuchTranche[];

      const changes = prepareTranchenChanges({
        gesuchTrancheToWorkWith: original,
        changes: [changed],
      } satisfies DeepPartial<SharedModelGesuch> as SharedModelGesuch);

      // imporove test data to include sb changes
      expect(changes).toEqual(
        expect.objectContaining({
          sb: undefined,
          gs: {
            affectedSteps: expect.arrayContaining(affectedSteps),
            tranche: expect.any(Object),
          },
        }),
      );
    },
  );
});
