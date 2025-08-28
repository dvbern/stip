import { initial } from '@dv/shared/util/remote-data';

import { selectSharedDataAccessBenutzersView } from './shared-data-access-benutzer.selectors';

describe('selectSharedDataAccessBenutzersView', () => {
  it('selects view', () => {
    const state = {
      currentBenutzerRd: initial(),
      lastFetchTs: null,
      rolesMap: {},
    };
    const result = selectSharedDataAccessBenutzersView.projector(state);
    expect(result).toEqual(state);
  });
});
