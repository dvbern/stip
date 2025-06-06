import * as fs from 'fs';
import path from 'path';

import { applicationGenerator } from '@nx/angular/generators';
import {
  Tree,
  formatFiles,
  generateFiles,
  getWorkspaceLayout,
  names,
  offsetFromRoot,
  updateJson,
} from '@nx/devkit';
import {
  camelize,
  capitalize,
  classify,
  dasherize,
} from '@nx/devkit/src/utils/string-utils';

import { AppGeneratorSchema } from './schema';

export default async function (tree: Tree, options: AppGeneratorSchema) {
  const projectName = `${dasherize(options.name)}-app`;
  const projectRoot = `${getWorkspaceLayout(tree).appsDir}/${projectName}`;
  await applicationGenerator(tree, {
    name: projectName,
    style: 'scss',
    standalone: true,
    directory: projectRoot,
    routing: true,
    tags: `type:app,scope:${projectName}`,
  });

  addFiles(tree, options, { projectRoot });

  const pathToApp = path.join(projectRoot, 'src', 'app');
  removeNxWelcomeComponent(tree, pathToApp);
  removeAppComponentTests(tree, pathToApp);
  updateProjectJson(tree, projectRoot);
  addScope(tree, projectName);

  await formatFiles(tree);

  return async () => {
    console['log'](
      `\nℹ️ Scope for "${projectName}" added to "eslint.config.js" and "libs/tooling/nx-plugin/src/generators/lib/schema.json"\n\n`,
    );
    console['log'](`Project: --project ${projectName}\n`);
    console['log'](`Can be used to run additional commands like`);
    console['log'](`eg "nx g remove --project ${projectName}"\n`);
  };
}

function addFiles(
  tree: Tree,
  options: AppGeneratorSchema,
  { projectRoot }: { projectRoot: string },
) {
  const templateOptions = {
    ...options,
    ...names(options.name),
    offsetFromRoot: offsetFromRoot(projectRoot),
    classify,
    dasherize,
    capitalize,
    camelize,
  };
  const tplPath = path.join(__dirname, 'files');
  if (!fs.existsSync(tplPath)) {
    console.warn(`Could not find files`);
    return;
  }

  generateFiles(tree, tplPath, path.join(projectRoot, 'src'), templateOptions);
}

function updateProjectJson(tree: Tree, projectRoot: string) {
  updateJson(tree, `${projectRoot}/project.json`, (json) => {
    json.targets.build.options.assets = [
      {
        glob: '**/*',
        input: 'libs/shared/assets/i18n/src',
        output: 'assets/i18n',
      },
      {
        glob: '**/*',
        input: 'libs/shared/assets/images/src',
        output: 'assets/images',
      },
      ...json.targets.build.options.assets,
    ];
    json.targets.build.options.stylePreprocessorOptions = {
      includePaths: [
        'libs/shared/styles/theme/src',
        'libs/shared/styles/components/src',
      ],
    };
    json.implicitDependencies = [
      'libs/shared/assets/i18n',
      'libs/shared/assets/images',
      'libs/shared/styles/theme',
      'libs/shared/styles/components',
    ];
    return json;
  });
}

function removeAppComponentTests(tree: Tree, pathToApp: string) {
  tree.delete(path.join(pathToApp, 'app.component.spec.ts'));
}

function removeNxWelcomeComponent(tree: Tree, pathToApp: string) {
  tree.delete(path.join(pathToApp, 'nx-welcome.component.ts'));
  const appComponentContent = tree
    ?.read(path.join(pathToApp, 'app.component.ts'))
    ?.toString();
  if (appComponentContent) {
    tree.write(
      path.join(pathToApp, 'app.component.ts'),
      appComponentContent
        .replace('NxWelcomeComponent,', '')
        .replace(
          "import { NxWelcomeComponent } from './nx-welcome.component';",
          '',
        ),
    );
  }
  const appComponentSpecContent = tree
    ?.read(path.join(pathToApp, 'app.component.spec.ts'))
    ?.toString();
  if (appComponentSpecContent) {
    tree.write(
      path.join(pathToApp, 'app.component.spec.ts'),
      appComponentSpecContent
        .replace('NxWelcomeComponent,', '')
        .replace(
          "import { NxWelcomeComponent } from './nx-welcome.component';",
          '',
        ),
    );
  }
}

function addScope(tree: Tree, projectName: string) {
  updateJson(
    tree,
    'libs/tooling/nx-plugin/src/generators/lib/schema.json',
    (json) => {
      json.properties.scope['x-prompt'].items.push({
        value: projectName,
        label: `${projectName} - used only by ${projectName}`,
      });
      return json;
    },
  );
}
