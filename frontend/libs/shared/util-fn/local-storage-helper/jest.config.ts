import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

export default {
  displayName: 'shared-util-fn-local-storage-helper',
  preset: '../../../../jest.preset.js',
  transform: {
    '^.+\\.[tj]s$': swcAngularJestTransformer(),
  },
  moduleFileExtensions: ['ts', 'js', 'html'],
  coverageDirectory:
    '../../../../coverage/libs/shared/util-fn/local-storage-helper',
};
