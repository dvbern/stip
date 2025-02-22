import * as fs from 'fs';
import * as path from 'path';

import {
  Tree,
  formatFiles,
  generateFiles,
  getWorkspaceLayout,
  names,
  offsetFromRoot,
  readProjectConfiguration,
  updateProjectConfiguration,
} from '@nx/devkit';
import {
  camelize,
  capitalize,
  classify,
  dasherize,
} from '@nx/devkit/src/utils/string-utils';

import { LibTypeGeneratorMap, NormalizedSchema } from './generator.interface';
import { LibGeneratorSchema } from './schema';
import { dataAccessTypeFactory } from './types/data-access';
import { dialogTypeFactory } from './types/dialog';
import { eventTypeFactory } from './types/event';
import { featureTypeFactory } from './types/feature';
import { globalTypeFactory } from './types/global';
import { modelTypeFactory } from './types/model';
import { patternTypeFactory } from './types/pattern';
import { uiTypeFactory } from './types/ui';
import { utilTypeFactory } from './types/util';
import { utilDataAccessTypeFactory } from './types/util-data-access';
import { utilFnTypeFactory } from './types/util-fn';

const LIB_TYPE_GENERATOR_MAP: LibTypeGeneratorMap = {
  feature: featureTypeFactory,
  pattern: patternTypeFactory,
  'data-access': dataAccessTypeFactory,
  event: eventTypeFactory,
  global: globalTypeFactory,
  ui: uiTypeFactory,
  util: utilTypeFactory,
  'util-data-access': utilDataAccessTypeFactory,
  'util-fn': utilFnTypeFactory,
  dialog: dialogTypeFactory,
  model: modelTypeFactory,
};

function normalizeOptions(
  tree: Tree,
  options: LibGeneratorSchema,
): NormalizedSchema {
  const nameDasherized = dasherize(options.name);
  const projectDirectory = `${getWorkspaceLayout(tree).libsDir}/${options.scope}/${options.type}/${nameDasherized}`;
  const projectName = `${options.scope}-${options.type}-${nameDasherized}`;
  const prefix = 'dv';
  const projectImportPath = `@${prefix}/${options.scope}/${options.type}/${nameDasherized}`;
  const projectRoot = projectDirectory;
  const parsedTags = [`type:${options.type}`, `scope:${options.scope}`];

  return {
    ...options,
    nameDasherized,
    prefix,
    projectName,
    projectRoot,
    projectDirectory,
    projectImportPath,
    parsedTags,
  };
}

export default async function (tree: Tree, options: LibGeneratorSchema) {
  const normalizedOptions = normalizeOptions(tree, options);
  const { type } = normalizedOptions;
  const libTypeFactory = LIB_TYPE_GENERATOR_MAP[type];

  const { libGenerator, libDefaultOptions, generators, postprocess } =
    libTypeFactory(normalizedOptions);

  await libGenerator(tree, {
    ...libDefaultOptions,
    prefix: normalizedOptions.prefix,
    name: normalizedOptions.projectName,
    directory: normalizedOptions.projectDirectory,
    importPath: normalizedOptions.projectImportPath,
    tags: normalizedOptions.parsedTags.join(','),
  });
  const projectConfig = readProjectConfiguration(
    tree,
    normalizedOptions.projectName,
  );
  // Add a dummy build target if it no target exists which configures    node_modules/@nx/devkit/src/generators/project-name-and-root-utils.js:115:19
  // the location of the tsconfig file.
  // @see {@link file://./../../../../../../eslint.config.js} -> enforceBuildableLibDependency
  if (
    ['test', 'build'].some(
      (target) => !projectConfig.targets?.[target]?.options?.tsConfig,
    )
  ) {
    updateProjectConfiguration(tree, normalizedOptions.projectName, {
      ...projectConfig,
      targets: {
        build: {
          executor: 'nx:noop',
          options: {
            tsConfig: `${normalizedOptions.projectRoot}/tsconfig.lib.json`,
          },
        },
        ...projectConfig.targets,
      },
    });
  }

  for (const { generator, defaultOptions } of generators) {
    await generator(tree, {
      ...defaultOptions,
      name: normalizedOptions.name,
      parent: normalizedOptions.projectName,
      directory: normalizedOptions.projectDirectory,
    });
  }

  addFiles(tree, normalizedOptions);
  postprocess(tree, normalizedOptions);
  await formatFiles(tree);

  return async () => {
    console['log'](`\nProject: --project ${normalizedOptions.projectName}\n`);
    console['log'](
      `Can be used to generate additional components, service or perform other commands like`,
    );
    console['log'](
      `eg "nx g remove --project ${normalizedOptions.projectName}"\n`,
    );
  };
}

function addFiles(tree: Tree, options: NormalizedSchema) {
  const templateOptions = {
    ...options,
    ...names(options.name),
    offsetFromRoot: offsetFromRoot(options.projectRoot),
    classify,
    dasherize,
    capitalize,
    camelize,
  };
  const tplPath = path.join(__dirname, 'files', options.type);
  if (!fs.existsSync(tplPath)) {
    return;
  }

  generateFiles(
    tree,
    tplPath,
    path.join(options.projectRoot, 'src'),
    templateOptions,
  );
}
