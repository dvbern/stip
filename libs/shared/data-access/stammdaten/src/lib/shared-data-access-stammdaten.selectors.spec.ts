import { selectSharedDataAccessStammdatensView } from './shared-data-access-stammdaten.selectors';

describe('selectSharedDataAccessStammdatensView', () => {
  it('selects view', () => {
    const state = {
      laender: [],
      loading: false,
      error: null,
    };
    const result = selectSharedDataAccessStammdatensView.projector(state);
    expect(result).toEqual(state);
  });
});
