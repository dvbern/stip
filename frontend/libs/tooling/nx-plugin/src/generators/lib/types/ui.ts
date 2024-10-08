import path from 'path';
import { Tree } from '@nx/devkit';
import { libraryGenerator } from '@nx/angular/generators';

import { NormalizedSchema, LibTypeGenerator } from '../generator.interface';
import { extendEslintJson } from './helpers/eslint';
import { updateTsConfig } from './helpers/tsconfig';
import { extendTestSetupSwc, extendJestConfigSwc } from './helpers/swc';

export function uiTypeFactory(options: NormalizedSchema): LibTypeGenerator {
  return {
    libGenerator: libraryGenerator,
    libDefaultOptions: {
      standalone: true,
      skipModule: true,
      displayBlock: true,
      style: 'scss',
      changeDetection: 'OnPush',
    },
    generators: [],
    postprocess,
  };
}

function postprocess(tree: Tree, options: NormalizedSchema) {
  extendEslintJson(tree, 'angular', options);
  extendTestSetupSwc(tree, options);
  extendJestConfigSwc(tree, options);

  updateTsConfig(tree, options);
  tree.delete(
    path.join(options.projectRoot, options.nameDasherized, 'README.md'),
  );
}
