import path from 'path';

import { libraryGenerator } from '@nx/angular/generators';
import { Tree } from '@nx/devkit';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
import { extendJestConfigSwc, extendTestSetupSwc } from './helpers/swc';

export function featureTypeFactory(
  options: NormalizedSchema,
): LibTypeGenerator {
  const { scope } = options;
  return {
    libGenerator: libraryGenerator,
    libDefaultOptions: {
      lazy: true,
      routing: true,
      standalone: true,
      style: 'scss',
      skipTests: true,
      changeDetection: 'OnPush',
      ...(scope !== 'shared'
        ? {
            parent: `apps/${scope}/src/app/app.routes.ts`,
          }
        : {}),
    },
    generators: [],
    postprocess,
  };
}

function postprocess(tree: Tree, options: NormalizedSchema) {
  extendTestSetupSwc(tree, options);
  extendJestConfigSwc(tree, options);

  tree.delete(path.join(options.projectRoot, 'README.md'));
  tree.delete(
    path.join(
      options.projectRoot,
      options.nameDasherized,
      'src',
      'lib',
      'lib.routes.ts',
    ),
  );

  const pathToIndex = path.join(
    options.projectRoot,
    options.nameDasherized,
    'src',
    'index.ts',
  );
  const indexTsContent = tree.read(pathToIndex)?.toString();
  if (indexTsContent) {
    tree.write(
      pathToIndex,
      indexTsContent.replace(
        'lib/lib.routes',
        `lib/${options.projectName}.routes`,
      ),
    );
  }
}
