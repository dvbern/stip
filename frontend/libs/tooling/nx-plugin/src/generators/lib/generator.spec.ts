import { Tree, readProjectConfiguration } from '@nrwl/devkit';
import { createTreeWithEmptyWorkspace } from '@nrwl/devkit/testing';

import generator from './generator';
import { LibType } from './generator.interface';
import { LibGeneratorSchema } from './schema';

// sanity tests, for generators it's better to keep it open and flexible to allow easy extension
// and adjustment of the logic in the future

describe('lib generator', () => {
  let tree: Tree;
  const createOptions = (name: string, type: LibType): LibGeneratorSchema => ({
    name,
    type,
    scope: 'shared',
  });

  beforeEach(() => {
    tree = createTreeWithEmptyWorkspace({ layout: 'apps-libs' });
    tree.write('.gitignore', '');
  });

  describe('model', () => {
    it('should generate model library', async () => {
      await generator(tree, createOptions('example', 'model'));
      const config = readProjectConfiguration(tree, 'shared-model-example');
      expect(config).toBeDefined();
      expect(tree.exists('libs/shared/model/example/package.json')).toBeFalsy();
      expect(
        tree.exists(
          'libs/shared/model/example/src/lib/shared-model-example.ts',
        ),
      ).toBeTruthy();
    });

    it('should generate model library (complex name - class)', async () => {
      await generator(tree, createOptions('exampleComplexName', 'model'));
      const config = readProjectConfiguration(
        tree,
        'shared-model-example-complex-name',
      );
      expect(config).toBeDefined();
      expect(
        tree.exists('libs/shared/model/example-complex-name/package.json'),
      ).toBeFalsy();
      expect(
        tree.exists(
          'libs/shared/model/example-complex-name/src/lib/shared-model-example-complex-name.ts',
        ),
      ).toBeTruthy();
      expect(
        tree.exists('libs/shared/model/example-complex-name/package.json'),
      ).toBeFalsy();
    });

    it('should generate model library (complex name - dasherized)', async () => {
      await generator(tree, createOptions('example-complex-name', 'model'));
      const config = readProjectConfiguration(
        tree,
        'shared-model-example-complex-name',
      );
      expect(config).toBeDefined();
      expect(
        tree.exists('libs/shared/model/example-complex-name/package.json'),
      ).toBeFalsy();
      expect(
        tree.exists(
          'libs/shared/model/example-complex-name/src/lib/shared-model-example-complex-name.ts',
        ),
      ).toBeTruthy();
    });
  });

  describe('util-fn', () => {
    it('should generate util-fn library', async () => {
      await generator(tree, createOptions('example', 'util-fn'));
      const config = readProjectConfiguration(tree, 'shared-util-fn-example');
      expect(config).toBeDefined();
      expect(
        tree.exists('libs/shared/util-fn/example/package.json'),
      ).toBeFalsy();
      expect(
        tree.exists(
          'libs/shared/util-fn/example/src/lib/shared-util-fn-example.ts',
        ),
      ).toBeTruthy();
      expect(
        tree.exists(
          'libs/shared/util-fn/example/src/lib/shared-util-fn-example.spec.ts',
        ),
      ).toBeTruthy();
    });
  });

  describe('util', () => {
    it('should generate util library', async () => {
      await generator(tree, createOptions('example', 'util'));
      const config = readProjectConfiguration(tree, 'shared-util-example');
      expect(config).toBeDefined();
      expect(tree.exists('libs/shared/util/example/package.json')).toBeFalsy();
      expect(
        tree.exists('libs/shared/util/src/lib/example.service.ts'),
      ).toBeFalsy();
      expect(
        tree.exists(
          'libs/shared/util/example/src/lib/shared-util-example.service.ts',
        ),
      ).toBeTruthy();
      expect(
        tree.exists(
          'libs/shared/util/example/src/lib/shared-util-example.service.spec.ts',
        ),
      ).toBeTruthy();
    });
  });

  describe('ui', () => {
    it('should generate ui library', async () => {
      await generator(tree, createOptions('example', 'ui'));
      const config = readProjectConfiguration(tree, 'shared-ui-example');
      expect(config).toBeDefined();
      expect(tree.exists('libs/shared/ui/example/package.json')).toBeFalsy();
      expect(tree.exists('libs/shared/ui/example/README.md')).toBeFalsy();
    });
  });

  describe('dialog', () => {
    it('should generate dialog library', async () => {
      await generator(tree, createOptions('example', 'dialog'));
      const config = readProjectConfiguration(tree, 'shared-dialog-example');
      expect(config).toBeDefined();
      expect(
        tree.exists('libs/shared/dialog/example/package.json'),
      ).toBeFalsy();
      expect(tree.exists('libs/shared/dialog/example/README.md')).toBeFalsy();
    });
  });

  describe('data-access', () => {
    it('should generate data-access library', async () => {
      await generator(tree, createOptions('example', 'data-access'));
      const config = readProjectConfiguration(
        tree,
        'shared-data-access-example',
      );
      expect(config).toBeDefined();
      expect(
        tree.exists('libs/shared/data-access/example/package.json'),
      ).toBeFalsy();
      expect(
        tree.exists('libs/shared/data-access/example/README.md'),
      ).toBeFalsy();
      expect(
        tree.exists(
          'libs/shared/data-access/example/src/lib/shared-data-access-example.actions.ts',
        ),
      ).toBeFalsy();
    });
  });

  describe('pattern', () => {
    it('should generate pattern library', async () => {
      await generator(tree, createOptions('example', 'pattern'));
      const config = readProjectConfiguration(tree, 'shared-pattern-example');
      expect(config).toBeDefined();
      expect(tree.exists('libs/shared/pattern/example/lib/')).toBeFalsy();
      expect(
        tree.exists('libs/shared/pattern/example/package.json'),
      ).toBeFalsy();
      expect(tree.exists('libs/shared/pattern/example/README.md')).toBeFalsy();
    });
  });

  describe('feature', () => {
    it('should generate feature library', async () => {
      await generator(tree, createOptions('example', 'feature'));
      const config = readProjectConfiguration(tree, 'shared-feature-example');
      expect(config).toBeDefined();
      expect(
        tree.exists(
          'libs/shared/feature/example/src/lib/shared-feature-example.routes.ts',
        ),
      ).toBeTruthy();
      expect(
        tree.exists('libs/shared/feature/example/src/lib/lib.routes.ts'),
      ).toBeFalsy();
      expect(
        tree.exists('libs/shared/feature/example/package.json'),
      ).toBeFalsy();
      expect(tree.exists('libs/shared/feature/example/README.md')).toBeFalsy();
    });
  });
});
