import { nxE2EPreset } from '@nx/playwright/preset';
import { defineConfig } from '@playwright/test';
import * as dotenv from 'dotenv';

import {
  AuthenticatedTest,
  GS_STORAGE_STATE,
  baseConfig,
  createTestConfigWithSetup,
} from '@dv/shared/util-fn/e2e-util';

dotenv.config({ path: '../../.env' });

const baseURL = process.env['E2E_BASEURL_GS'];

export default defineConfig<AuthenticatedTest>({
  ...nxE2EPreset(__filename, { testDir: './src' }),
  ...baseConfig,
  use: {
    ...baseConfig.use,
    baseURL,
  },
  projects: [
    // Setup project for authentication.
    ...createTestConfigWithSetup({
      dir: 'gesuch',
      name: 'simple-full-gesuch',
      storageStatePath: `gesuch_${GS_STORAGE_STATE}`,
      fixtures: {
        authentication: 'GESUCHSTELLER_1',
      },
    }),
    ...createTestConfigWithSetup({
      dir: 'upload',
      name: 'dokument-upload',
      storageStatePath: `upload_${GS_STORAGE_STATE}`,
      fixtures: {
        authentication: 'GESUCHSTELLER_2',
      },
    }),
  ],
});
