import { defineConfig } from '@playwright/test';

export const BEARER_COOKIE = 'access_cookie';
export const REFRESH_COOKIE = 'refresh_cookie';

export const GS_STORAGE_STATE = 'gs-storage-state.json';
export const SB_STORAGE_STATE = 'sb-storage-state.json';

export const baseConfig = defineConfig({
  use: {
    trace: 'on-first-retry',
    ignoreHTTPSErrors: true,
  },
});
