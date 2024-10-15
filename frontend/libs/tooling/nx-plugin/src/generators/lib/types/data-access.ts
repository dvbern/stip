import path from 'path';

import { libraryGenerator } from '@nx/angular/generators';
import { Tree } from '@nx/devkit';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
import { extendJestConfigSwc, extendTestSetupSwc } from './helpers/swc';

export function dataAccessTypeFactory(
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
  extendTestSetupSwc(tree, options);
  extendJestConfigSwc(tree, options);

  tree.delete(
    path.join(
      options.projectRoot,
      'src',
      'lib',
      options.projectName + '.component.ts',
    ),
  );
  tree.delete(path.join(options.projectRoot, 'README.md'));
}
