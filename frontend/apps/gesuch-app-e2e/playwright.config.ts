import { join } from 'node:path';

import { nxE2EPreset } from '@nx/playwright/preset';
import { defineConfig, devices } from '@playwright/test';
import * as dotenv from 'dotenv';

import { baseConfig, getE2eUrls } from '@dv/shared/util-fn/e2e-util';

dotenv.config({ path: join(__dirname, '../../.env') });

const urls = getE2eUrls();

export default defineConfig({
  ...nxE2EPreset(__filename, { testDir: './src' }),
  ...baseConfig,
  use: {
    ...baseConfig.use,
    baseURL: urls.gs,
  },
  workers: 3,
  projects: [
    {
      name: 'gesuch-app-e2e',
      testDir: `src/tests`,
      use: {
        ...devices['Desktop Chrome'],
      },
    },
  ],
});
