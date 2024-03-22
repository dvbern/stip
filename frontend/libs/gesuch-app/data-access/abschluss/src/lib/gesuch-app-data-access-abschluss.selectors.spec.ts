import { selectGesuchAppDataAccessAbschlusssView } from './gesuch-app-data-access-abschluss.selectors';

describe('selectGesuchAppDataAccessAbschlusssView', () => {
  it('selects view', () => {
    const state = {
      abschlussPhase: 'NOT_READY',
      canAbschliessen: undefined,
      canCheck: true,
      checkResult: undefined,
      error: undefined,
      gesuch: null,
      lastUpdate: null,
      loading: false,
      validations: [],
    };
    const result = selectGesuchAppDataAccessAbschlusssView.projector(
      state,
      null,
      null,
      [],
    );
    expect(result).toEqual(state);
  });
});
