import path from 'path';

import { Tree, updateJson, writeJson } from '@nx/devkit';

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

export function updateSpecTsConfig(
  tree: Tree,
  options: NormalizedSchema,
): void {
  updateJson(tree, path.join(options.projectRoot, 'tsconfig.json'), (json) => {
    json.references = [
      ...json.references,
      {
        path: './tsconfig.spec.json',
      },
    ];
    return json;
  });
  const relativePath = options.projectRoot
    .split('/')
    .map(() => '..')
    .join('/');
  writeJson(tree, path.join(options.projectRoot, 'tsconfig.spec.json'), {
    extends: `${relativePath}/tsconfig.spec.json`,
    include: ['src/**/*.test.ts', 'src/**/*.spec.ts'],
  });
}
