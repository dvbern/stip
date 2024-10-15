import path from 'path';

import { Tree } from '@nx/devkit';
import { libraryGenerator } from '@nx/js';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
import { extendJestConfigSwc, extendTestSetupSwc } from './helpers/swc';

export function utilFnTypeFactory(options: NormalizedSchema): LibTypeGenerator {
  return {
    libGenerator: libraryGenerator,
    libDefaultOptions: {
      bundler: 'none',
    },
    generators: [],
    postprocess,
  };
}

function postprocess(tree: Tree, options: NormalizedSchema) {
  extendTestSetupSwc(tree, options);
  extendJestConfigSwc(tree, options);

  tree.delete(path.join(options.projectRoot, 'package.json'));
  tree.delete(path.join(options.projectRoot, 'README.md'));
}
