import path from 'path';
import { Tree, updateJson } from '@nx/devkit';
import { libraryGenerator } from '@nx/angular/generators';

import { NormalizedSchema, LibTypeGenerator } from '../generator.interface';
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

  updateJson(
    tree,
    path.join(options.projectRoot, options.nameDasherized, '.eslintrc.json'),
    (json) => {
      json.overrides = [
        {
          files: ['*.ts'],
          rules: {},
        },
      ];
      return json;
    },
  );
  tree.delete(
    path.join(
      options.projectRoot,
      options.nameDasherized,
      'src',
      'lib',
      options.projectName + '.component.ts',
    ),
  );
  tree.delete(
    path.join(options.projectRoot, options.nameDasherized, 'README.md'),
  );
}
