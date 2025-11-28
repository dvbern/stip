const { globalIgnores } = require('eslint/config');
const { FlatCompat } = require('@eslint/eslintrc');
const js = require('@eslint/js');
const nxEslintPlugin = require('@nx/eslint-plugin');
const ngrxEslintPlugin = require('@ngrx/eslint-plugin');

const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
});

module.exports = [
  ...nxEslintPlugin.configs['flat/angular'],
  ...nxEslintPlugin.configs['flat/angular-template'],
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
    plugins: {
      '@nx': nxEslintPlugin,
      '@ngrx': ngrxEslintPlugin,
    },
  },
  {
    settings: {
      'import/resolver': { typescript: { project: './tsconfig.base.json' } },
    },
  },
  {
    files: ['**/*.ts', '**/*.tsx', '**/*.js', '**/*.jsx'],
    rules: {
      '@nx/enforce-module-boundaries': [
        'error',
        {
          enforceBuildableLibDependency: false,
          banTransitiveDependencies: true,
          allow: ['@jscutlery/**', 'reflect-metadata'],
          depConstraints: [
            {
              sourceTag: 'scope:sachbearbeitung-app',
              onlyDependOnLibsWithTags: [
                'scope:shared',
                'scope:sachbearbeitung-app',
              ],
            },
            {
              sourceTag: 'scope:gesuch-app',
              onlyDependOnLibsWithTags: ['scope:shared', 'scope:gesuch-app'],
            },
            {
              sourceTag: 'scope:sozialdienst-app',
              onlyDependOnLibsWithTags: [
                'scope:shared',
                'scope:sozialdienst-app',
              ],
            },
            {
              sourceTag: 'scope:demo-data-app',
              onlyDependOnLibsWithTags: ['scope:shared', 'scope:demo-data-app'],
            },
            {
              sourceTag: 'scope:shared',
              onlyDependOnLibsWithTags: ['scope:shared'],
            },
            {
              sourceTag: 'scope:tooling',
              onlyDependOnLibsWithTags: ['scope:tooling'],
            },
            {
              sourceTag: 'type:app',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:feature',
                'type:data-access',
                'type:pattern',
                'type:model',
                'type:global',
              ],
            },
            {
              sourceTag: 'type:feature',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:feature',
                'type:pattern',
                'type:data-access',
                'type:event',
                'type:dialog',
                'type:ui',
                'type:util',
                'type:util-data-access',
                'type:util-fn',
                'type:model',
                'type:global',
              ],
            },
            {
              sourceTag: 'type:pattern',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:dialog',
                'type:pattern',
                'type:data-access',
                'type:event',
                'type:ui',
                'type:util',
                'type:util-fn',
                'type:model',
                'type:global',
              ],
            },
            {
              sourceTag: 'type:data-access',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:data-access',
                'type:event',
                'type:util',
                'type:util-data-access',
                'type:util-fn',
                'type:model',
                'type:global',
              ],
            },
            {
              sourceTag: 'type:event',
              onlyDependOnLibsWithTags: ['type:model'],
            },
            {
              sourceTag: 'type:ui',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:ui',
                'type:util',
                'type:util-data-access',
                'type:util-fn',
                'type:model',
                'type:global',
              ],
              bannedExternalImports: ['@ngrx/*'],
            },
            {
              sourceTag: 'type:dialog',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:feature',
                'type:data-access',
                'type:ui',
                'type:util',
                'type:util-data-access',
                'type:util-fn',
                'type:model',
              ],
            },
            {
              sourceTag: 'type:util',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:util',
                'type:util-fn',
                'type:model',
                'type:global',
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
            {
              sourceTag: 'type:util-data-access',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:data-access',
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
                '@ngrx/store-devtools',
              ],
            },
            {
              sourceTag: 'type:util-fn',
              onlyDependOnLibsWithTags: [
                'type:assets',
                'type:util-fn',
                'type:model',
              ],
              bannedExternalImports: ['@angular/*', '@ngrx/*'],
            },
            {
              sourceTag: 'type:model',
              onlyDependOnLibsWithTags: ['type:assets', 'type:model'],
              bannedExternalImports: ['@ngrx/*'],
            },
            {
              sourceTag: 'type:e2e',
              onlyDependOnLibsWithTags: [
                'type:util',
                'type:util-fn',
                'type:model',
              ],
            },
            {
              sourceTag: 'type:styles',
              onlyDependOnLibsWithTags: ['type:styles'],
            },
          ],
        },
      ],
    },
  },
  ...compat
    .config({
      extends: [
        'plugin:@nx/typescript',
        'plugin:import/recommended',
        'plugin:import/typescript',
        'plugin:prettier/recommended',
      ],
    })
    .map((config) => ({
      ...config,
      files: ['**/*.ts', '**/*.tsx', '**/*.cts', '**/*.mts'],
      rules: {
        ...config.rules,
        'sort-imports': [
          'error',
          {
            ignoreCase: false,
            ignoreDeclarationSort: true,
            ignoreMemberSort: false,
            memberSyntaxSortOrder: ['none', 'all', 'multiple', 'single'],
            allowSeparatedGroups: true,
          },
        ],
        'import/no-unresolved': 'error',
        'import/order': [
          'error',
          {
            groups: [
              'builtin',
              'external',
              'internal',
              ['parent', 'sibling'],
              'index',
            ],
            'newlines-between': 'always',
            alphabetize: {
              order: 'asc',
              caseInsensitive: true,
            },
          },
        ],
        '@typescript-eslint/no-unused-vars': 'error',
        '@typescript-eslint/no-empty-object-type': 'error',
        'prettier/prettier': 'warn',
      },
    })),
  ...compat.config({ extends: ['plugin:@nx/javascript'] }).map((config) => ({
    ...config,
    files: ['**/*.js', '**/*.jsx', '**/*.cjs', '**/*.mjs'],
    rules: {
      ...config.rules,
    },
  })),
  ...compat.config({ env: { jest: true } }).map((config) => ({
    ...config,
    files: ['**/*.spec.ts', '**/*.spec.tsx', '**/*.spec.js', '**/*.spec.jsx'],
    rules: {
      ...config.rules,
    },
  })),
  {
    files: ['**/*.json'],
    // Override or add rules here
    rules: {},
    languageOptions: { parser: require('jsonc-eslint-parser') },
  },
  {
    files: ['**/*.spec.ts'],
    rules: { '@typescript-eslint/no-explicit-any': 'off' },
  },
  {
    name: 'angular-eslint/template-accessibility',
    rules: {
      '@angular-eslint/template/label-has-associated-control': ['off'],
    },
    files: ['**/*.html'],
  },
  globalIgnores([
    'libs/shared/model/gesuch/src/lib/openapi',
    'libs/tooling/',
    '.*',
    '*.*',
    'Caddyfile',
    'Dockerfile',
    'coverage',
    'deploy',
    'dist',
    'docs',
    'extensions',
    'node_modules',
    'scripts',
    'tmp',
  ]),
];
