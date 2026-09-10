import { join } from 'node:path';

import { nxE2EPreset } from '@nx/playwright/preset';
import { defineConfig, devices } from '@playwright/test';
import * as dotenv from 'dotenv';

import {
  CustomTestOptions,
  baseConfig,
  getE2eUrls,
} from '@dv/shared/util-fn/e2e-util';

dotenv.config({ path: join(__dirname, '../../.env') });

const urls = getE2eUrls();

export default defineConfig<CustomTestOptions>({
  ...nxE2EPreset(__filename, { testDir: './src' }),
  ...baseConfig,
  workers: 1,
  use: {
    ...baseConfig.use,
    baseURL: urls.sb,
    testUser: 'SACHBEARBEITER',
  },
  projects: [
    {
      name: 'setup',
      testMatch: /.*\.setup\.ts/,
      use: {
        ...devices['Desktop Chrome'],
      },
    },
    {
      name: 'sachbearbeitung-app-e2e',
      testDir: `src/tests`,
      use: {
        ...devices['Desktop Chrome'],
      },
      dependencies: ['setup'],
    },
  ],
});
