import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

export default {
  displayName: 'shared-util-fn-array-helper',
  preset: '../../../../jest.preset.js',
  testEnvironment: 'node',
  transform: {
    '^.+\\.[tj]s$': swcAngularJestTransformer(),
  },
  moduleFileExtensions: ['ts', 'js', 'html'],
  coverageDirectory: '../../../../coverage/libs/shared/util-fn/array-helper',
};
