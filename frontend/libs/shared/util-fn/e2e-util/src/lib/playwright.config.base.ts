import { defineConfig } from '@playwright/test';

export const BEARER_COOKIE = 'access_cookie';
export const REFRESH_COOKIE = 'refresh_cookie';

export const GS_STORAGE_STATE = 'gs-storage-state.json';
export const SB_STORAGE_STATE = 'sb-storage-state.json';

export interface KeycloakResponse {
  access_token: string;
  expires_in: number;
  refresh_expires_in: number;
  refresh_token: string;
  token_type: string;
  id_token: string;
  'not-before-policy': number;
  session_state: string;
  scope: string;
}

export const baseConfig = defineConfig({
  use: {
    trace: 'on-first-retry',
    ignoreHTTPSErrors: true,
    screenshot: 'on',
    video: {
      mode: 'on-first-retry',
      // size: { width: 1920, height: 1080 },
      // smaller video size to reduce file size
      size: { width: 1280, height: 720 },
    },
  },
  forbidOnly: !!process.env.CI,
  retries: 1,
  // workers: 2,
});
