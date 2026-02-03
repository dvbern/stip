import path from 'path';

import { libraryGenerator } from '@nx/angular/generators';
import { Tree } from '@nx/devkit';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
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
  updateTsConfig(tree, options);
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
