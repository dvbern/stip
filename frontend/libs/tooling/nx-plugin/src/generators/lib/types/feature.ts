import path from 'path';

import { libraryGenerator } from '@nx/angular/generators';
import { Tree } from '@nx/devkit';

import { LibTypeGenerator, NormalizedSchema } from '../generator.interface';
import { updateSpecTsConfig } from './helpers/tsconfig';

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
      spec: false,
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
  tree.delete(path.join(options.projectRoot, 'README.md'));
  tree.delete(path.join(options.projectRoot, 'src', 'lib', 'lib.routes.ts'));

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
