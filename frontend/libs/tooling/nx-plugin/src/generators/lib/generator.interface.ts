import { Schema } from '@nx/angular/src/generators/library/schema';
import { Tree } from '@nx/devkit';
import { Generator, GeneratorCallback } from 'nx/src/config/misc-interfaces';

import { LibGeneratorSchema } from './schema';
import { LibraryGeneratorSchema } from '@nx/js/src/generators/library/schema';

export type LibType =
  | 'feature'
  | 'pattern'
  | 'data-access'
  | 'event'
  | 'global'
  | 'ui'
  | 'util'
  | 'util-data-access'
  | 'util-fn'
  | 'dialog'
  | 'model';

export type LibScope = 'shared' | 'customer-app';

export interface NormalizedSchema extends LibGeneratorSchema {
  nameDasherized: string;
  prefix: string;
  projectName: string;
  projectRoot: string;
  projectDirectory: string;
  projectImportPath: string;
  parsedTags: string[];
}

export interface DefaultOptions {
  [key: string]: string | number | boolean;
}

export interface LibTypeGenerator {
  libGenerator:
    | {
        type: 'angular';
        generator: (tree: Tree, schema: Schema) => Promise<GeneratorCallback>;
      }
    | {
        type: 'library';
        generator: (
          tree: Tree,
          schema: LibraryGeneratorSchema,
        ) => Promise<GeneratorCallback>;
      };
  libDefaultOptions: DefaultOptions;
  generators: {
    generator: Generator<Schema>;
    defaultOptions: DefaultOptions;
  }[];
  postprocess(tree: Tree, options: NormalizedSchema): void;
}

export type LibTypeGeneratorMap = {
  [key in LibType]: (options: NormalizedSchema) => LibTypeGenerator;
};
