import * as path from 'path';

import { nxComponentTestingPreset } from '@nx/angular/plugins/component-testing';
import { defineConfig } from 'cypress';

import { dvCypressConfigs } from '@dv/shared/util-fn/cypress-config';

import { setupCoverageWebpack } from './coverage.webpack';

const nxPreset = nxComponentTestingPreset(__filename);

export default defineConfig({
  component: {
    ...nxPreset,
    devServer: {
      ...nxPreset.devServer,
      webpackConfig: setupCoverageWebpack([path.join(__dirname, 'src')]),
    },
    ...dvCypressConfigs.component,
  },
  scrollBehavior: 'nearest',
});
