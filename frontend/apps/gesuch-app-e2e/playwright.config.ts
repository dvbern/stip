import { nxE2EPreset } from '@nx/playwright/preset';
import { Project, defineConfig, devices } from '@playwright/test';
import * as dotenv from 'dotenv';

import {
  AuthenticatedTest,
  GS_STORAGE_STATE,
  baseConfig,
} from '@dv/shared/util-fn/e2e-util';

dotenv.config({ path: '../../.env' });

const baseURL = process.env['E2E_BASEURL_GS'];

// GET CWD
console.log(process.cwd());

const createTestConfig = (
  dir: string,
  name: string,
  baseStorageState: string,
  feature: AuthenticatedTest,
): [ReturnType<typeof defineConfig<AuthenticatedTest>>, Project] => {
  return [
    {
      name,
      testMatch: /.*\.setup\.ts/,
      use: feature,
    },
    {
      name: `${name}-chromium`,
      testDir: `src/tests/${dir}`,
      use: {
        ...devices['Desktop Chrome'],
        storageState: feature.storageState,
      },
      dependencies: [name],
    },
  ];
};

export default defineConfig<AuthenticatedTest>({
  ...nxE2EPreset(__filename, { testDir: './src' }),
  ...baseConfig,
  use: {
    ...baseConfig.use,
    baseURL,
  },
  projects: [
    // Setup project for authentication.
    ...createTestConfig('gesuch', 'simple-full-gesuch', GS_STORAGE_STATE, {
      authentication: 'GESUCHSTELLER_1',
      storageState: `gesuch_${GS_STORAGE_STATE}`,
    }),
    ...createTestConfig('upload', 'dokument-upload', GS_STORAGE_STATE, {
      authentication: 'GESUCHSTELLER_2',
      storageState: `upload_${GS_STORAGE_STATE}`,
    }),
  ],
});
