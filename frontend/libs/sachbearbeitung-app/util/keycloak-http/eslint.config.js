const nx = require('@nx/eslint-plugin');
const baseConfig = require('../../../../eslint.config.js');
const { de } = require('date-fns/locale');

module.exports = [
  ...baseConfig,
  ...nx.configs['flat/angular'],
  ...nx.configs['flat/angular-template'],
  {
    files: ['**/*.ts'],
    rules: {
      '@angular-eslint/directive-selector': [
        'error',
        {
          type: 'attribute',
          prefix: 'dv',
          style: 'camelCase',
        },
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'dv',
          style: 'kebab-case',
        },
      ],
    },
  },
  {
    files: ['**/*.html'],
    // Override or add rules here
    rules: {
      '@nx/enforce-module-boundaries': [
        'error',
        {
          depConstraints: [
            {
              sourceTag: 'type:util',
              onlyDependOnLibsWithTags: [
                'type:util',
                'type:util-fn',
                'type:model',
              ],
              bannedExternalImports: [
                '@ngrx/component-store',
                '@ngrx/effects',
                '@ngrx/entity',
                '@ngrx/eslint-plugin',
                '@ngrx/router-store',
                '@ngrx/signals',
                '@ngrx/store',
                '@ngrx/store-devtools',
              ],
            },
          ],
        },
      ],
    },
  },
];
