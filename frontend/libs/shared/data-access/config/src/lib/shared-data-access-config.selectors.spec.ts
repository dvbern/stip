import { selectSharedDataAccessConfigsView } from './shared-data-access-config.selectors';

describe('selectSharedDataAccessConfigsView', () => {
  it('selects view', () => {
    const state = {
      deploymentConfig: undefined,
      compileTimeConfig: undefined,
      isGesuchApp: false,
      isSachbearbeitungApp: false,
      loading: false,
      error: undefined,
    };
    const result = selectSharedDataAccessConfigsView.projector(state);
    expect(result).toEqual(state);
  });
});
