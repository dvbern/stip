import { State } from './shared-data-access-dokumente.feature';
import { selectSharedDataAccessDokumentesView } from './shared-data-access-dokumente.selectors';

describe('selectSharedDataAccessDokumentesView', () => {
  it('selects view', () => {
    const state: State = {
      dokumentes: [],
      loading: false,
      error: undefined,
    };
    const gesuch = {
      gesuch: null,
      gesuchs: [],
      gesuchFormular: null,
      loading: false,
      error: undefined,
      readonly: false,
      trancheId: '',
    };
    const result = selectSharedDataAccessDokumentesView.projector(
      state,
      gesuch,
    );
    expect(result).toEqual(state);
  });
});