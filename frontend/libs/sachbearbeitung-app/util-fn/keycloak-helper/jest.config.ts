import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

// export default {
//   displayName: 'sachbearbeitung-app-util-fn-keycloak-helper',
//   preset: '../../../../jest.preset.js',
//   transform: {
//     '^.+\\.[tj]s$': swcAngularJestTransformer(),
//   },
//   transformIgnorePatterns: ['node_modules/(?!.*\\.mjs$)'],
//   // moduleFileExtensions: ['ts', 'js', 'html'],
//   coverageDirectory:
//     '../../../../coverage/libs/sachbearbeitung-app/util-fn/keycloak-helper',
// };

export default {
  displayName: 'sachbearbeitung-app-util-fn-keycloak-helper',
  preset: '../../../../jest.preset.js',
  setupFilesAfterEnv: ['<rootDir>/src/test-setup.ts'],
  coverageDirectory:
    '../../../../coverage/libs/sachbearbeitung-app/util-fn/keycloak-helper',
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
