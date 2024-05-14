import { selectGesuchAppDataAccessAbschlussView } from './gesuch-app-data-access-abschluss.selectors';

describe('selectGesuchAppDataAccessAbschlussView', () => {
  it('selects view', () => {
    const state = {
      abschlussPhase: 'NOT_READY',
      canCheck: true,
      checkResult: undefined,
      error: undefined,
      gesuch: null,
      lastUpdate: null,
      loading: false,
      validations: [],
    };
    const result = selectGesuchAppDataAccessAbschlussView.projector(
      state,
      null,
      null,
      { errors: [] },
    );
    expect(result).toEqual(state);
  });
});
