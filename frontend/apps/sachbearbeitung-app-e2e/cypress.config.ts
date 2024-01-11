import { nxE2EPreset } from '@nx/cypress/plugins/cypress-preset';
import { defineConfig } from 'cypress';
import * as dotenv from 'dotenv';

import { dvCypressConfigs } from '@dv/shared/util-fn/cypress-config';

dotenv.config({ path: '../../.env' });

export default defineConfig({
  e2e: {
    ...nxE2EPreset(__dirname),
    baseUrl: process.env['E2E_BASEURL_SB'],
    ...dvCypressConfigs.e2e,
  },
  env: {
    E2E_USERNAME: process.env['E2E_USERNAME'],
    E2E_PASSWORD: process.env['E2E_PASSWORD'],
  },
  scrollBehavior: 'nearest',
});
