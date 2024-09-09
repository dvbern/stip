import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

/* eslint-disable */
export default {
  displayName: 'shared-util-fn-string-helper',
  preset: '../../../../jest.preset.js',
  transform: {
    '^.+\\.[tj]s$': swcAngularJestTransformer(),
  },
  moduleFileExtensions: ['ts', 'js', 'html'],
  coverageDirectory: '../../../../coverage/libs/shared/util-fn/string-helper',
};
