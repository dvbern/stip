import { AppConfig } from '@dv/shared/model/config';

import { selectSharedDataAccessConfigsView } from './shared-data-access-config.selectors';

describe('selectSharedDataAccessConfigsView', () => {
  it('selects view', () => {
    const state = {
      deploymentConfig: undefined,
      compileTimeConfig: undefined,
      appConfig: {
        type: 'gesuch-app',
        view: 'gesuchsteller',
        keyPrefix: 'gesuch-app',
      } satisfies AppConfig,
      loading: false,
      error: undefined,
    };
    const result = selectSharedDataAccessConfigsView.projector(state);
    expect(result).toEqual(state);
  });
});
