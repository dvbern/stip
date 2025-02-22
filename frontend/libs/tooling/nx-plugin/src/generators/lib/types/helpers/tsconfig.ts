import path from 'path';

import { Tree, updateJson } from '@nx/devkit';

import { NormalizedSchema } from '../../generator.interface';

export function updateTsConfig(tree: Tree, options: NormalizedSchema): void {
  updateJson(tree, path.join(options.projectRoot, 'tsconfig.json'), (json) => {
    json.compilerOptions = {
      ...json.compilerOptions,
      ...{
        forceConsistentCasingInFileNames: true,
        strict: true,
        noImplicitOverride: true,
        noImplicitReturns: true,
        noFallthroughCasesInSwitch: true,
      },
    };
    json.angularCompilerOptions = {
      ...json.angularCompilerOptions,
      ...{
        enableI18nLegacyMessageIdFormat: false,
        strictInjectionParameters: true,
        strictInputAccessModifiers: true,
        strictTemplates: true,
      },
    };

    return json;
  });
}
