import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

/* eslint-disable */
export default {
  displayName: 'gesuch-app-feature-cockpit',
  preset: '../../../../jest.preset.js',
  setupFilesAfterEnv: ['<rootDir>/src/test-setup.ts'],
  coverageDirectory: '../../../../coverage/libs/gesuch-app/feature/cockpit',
  transform: {
    '^.+\\.(ts|mjs|js)$': swcAngularJestTransformer(),
    '^.+\\.(html)$': [
      'jest-preset-angular',
      {
        tsconfig: '<rootDir>/tsconfig.spec.json',
        stringifyContentPathRegex: '\\.(html|svg)$',
      },
    ],
  },
  transformIgnorePatterns: ['node_modules/(?!.*\\.mjs$)'],
  snapshotSerializers: [
    'jest-preset-angular/build/serializers/no-ng-attributes',
    'jest-preset-angular/build/serializers/ng-snapshot',
    'jest-preset-angular/build/serializers/html-comment',
  ],
};
