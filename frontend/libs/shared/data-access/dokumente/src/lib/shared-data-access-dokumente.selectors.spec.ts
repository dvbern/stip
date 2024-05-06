import { State } from './shared-data-access-dokumente.feature';
import { selectSharedDataAccessDokumentesView } from './shared-data-access-dokumente.selectors';

describe('selectSharedDataAccessDokumentesView', () => {
  it('selects view', () => {
    const state: State = {
      dokumentes: [],
      requiredDocumentTypes: [],
      loading: false,
      error: undefined,
    };

    const result = selectSharedDataAccessDokumentesView.projector(state);
    expect(result).toEqual(state);
  });
});
