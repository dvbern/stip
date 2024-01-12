import { selectSharedDataAccessAusbildungsstaettesView } from './shared-data-access-ausbildungsstaette.selectors';

describe('selectSharedDataAccessAusbildungsstaettesView', () => {
  it('selects view', () => {
    const state = {
      ausbildungsstaettes: [],
      ausbildungsstaetteByLand: {
        AUSLAND: [],
        CH: [],
      },
      loading: false,
      error: undefined,
    };
    const result =
      selectSharedDataAccessAusbildungsstaettesView.projector(state);
    expect(result).toEqual(state);
  });
});
