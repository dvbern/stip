/// <reference types='vitest' />
import angular from '@analogjs/vite-plugin-angular';
import { nxViteTsPaths } from '@nx/vite/plugins/nx-tsconfig-paths.plugin';
import { defineConfig } from 'vitest/config';

export default defineConfig(() => ({
  root: __dirname,
  plugins: [angular(), nxViteTsPaths()],
  test: {
    globals: true,
    isolate: false,
    environment: 'jsdom',
    include: ['{apps,libs}/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
    exclude: ['libs/tooling/**/*', '**/*-e2e/**/*'],
    setupFiles: ['test-setup.ts'],
  },
}));
