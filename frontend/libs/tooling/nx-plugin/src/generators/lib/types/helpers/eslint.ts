import path from 'path';
import { Tree, updateJson } from '@nx/devkit';

export function extendEslintJson(
  tree: Tree,
  type: 'angular' | 'ngrx',
  options: { projectRoot: string; nameDasherized: string },
) {
  updateJson(
    tree,
    path.join(options.projectRoot, options.nameDasherized, 'eslint.config.js'),
    (json) => {
      json.extends = [
        ...json.extends,
        json.extends[0].replace('.eslintrc', `.eslintrc-${type}`),
      ];
      return json;
    },
  );
}
