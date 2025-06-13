import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

export default {
  displayName: 'sachbearbeitung-app-util-fn-sachbearbeiter-helper',
  preset: '../../../../jest.preset.js',
  transform: {
    '^.+\\.[tj]s$': swcAngularJestTransformer(),
  },
  moduleFileExtensions: ['ts', 'js', 'html'],
  coverageDirectory:
    '../../../../coverage/libs/sachbearbeitung-app/util-fn/sachbearbeiter-helper',
};
