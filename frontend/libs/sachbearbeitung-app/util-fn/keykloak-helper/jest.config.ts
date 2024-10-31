import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

export default {
  displayName: 'sachbearbeitung-app-util-fn-keykloak-helper',
  preset: '../../../../jest.preset.js',
  transform: {
    '^.+\\.[tj]s$': swcAngularJestTransformer(),
  },
  transformIgnorePatterns: ['node_modules/(?!.*\\.mjs$)'],
  moduleFileExtensions: ['ts', 'js', 'html'],
  coverageDirectory:
    '../../../../coverage/libs/sachbearbeitung-app/util-fn/keykloak-helper',
};
