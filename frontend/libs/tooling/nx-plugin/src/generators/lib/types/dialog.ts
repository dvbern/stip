import path from 'path';

import { libraryGenerator } from '@nx/angular/generators';
import { Tree } from '@nx/devkit';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
import { updateTsConfig } from './helpers/tsconfig';

export function dialogTypeFactory(): LibTypeGenerator {
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
  updateTsConfig(tree, options);
  tree.delete(path.join(options.projectRoot, 'README.md'));
}
