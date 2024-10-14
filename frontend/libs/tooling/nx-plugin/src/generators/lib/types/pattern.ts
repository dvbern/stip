import path from 'path';

import { libraryGenerator } from '@nx/angular/generators';
import { Tree } from '@nx/devkit';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
import { extendEslintJson } from './helpers/eslint';
import { extendJestConfigSwc, extendTestSetupSwc } from './helpers/swc';
import { updateTsConfig } from './helpers/tsconfig';

export function patternTypeFactory(
  options: NormalizedSchema,
): LibTypeGenerator {
  return {
    libGenerator: libraryGenerator,
    libDefaultOptions: {
      skipModule: true,
      flat: true,
      style: 'none',
      skipSelector: true,
      skipTests: true,
      inlineStyle: true,
      inlineTemplate: true,
    },
    generators: [],
    postprocess,
  };
}

function postprocess(tree: Tree, options: NormalizedSchema) {
  extendEslintJson(tree, 'angular', options);
  updateTsConfig(tree, options);
  extendTestSetupSwc(tree, options);
  extendJestConfigSwc(tree, options);

  tree.delete(
    path.join(
      options.projectRoot,
      options.nameDasherized,
      'src',
      'lib',
      options.projectName + '.component.ts',
    ),
  );
  tree.delete(path.join(options.projectRoot, 'README.md'));
}
