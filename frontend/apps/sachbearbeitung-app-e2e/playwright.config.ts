import { nxE2EPreset } from '@nx/playwright/preset';
import { defineConfig, devices } from '@playwright/test';
import * as dotenv from 'dotenv';

import { SB_STORAGE_STATE, baseConfig } from '@dv/shared/util-fn/e2e-util';

dotenv.config({ path: '../../.env' });

const baseURL = process.env['E2E_BASEURL_SB'];

export default defineConfig({
  ...nxE2EPreset(__filename, { testDir: './src' }),
  ...baseConfig,
  use: {
    ...baseConfig.use,
    baseURL,
  },
  projects: [
    // Setup project for authentication.
    { name: 'setup', testMatch: /.*\.setup\.ts/ },
    {
      name: 'chromium',
      use: {
        ...devices['Desktop Chrome'],
        storageState: SB_STORAGE_STATE,
      },
      dependencies: ['setup'],
    },
  ],
});
