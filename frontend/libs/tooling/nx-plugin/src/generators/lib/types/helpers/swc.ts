import { Tree } from '@nx/devkit';
import { tsquery } from '@phenomnomnominal/tsquery';

import { NormalizedSchema } from '../../generator.interface';

export function extendTestSetupSwc(tree: Tree, options: NormalizedSchema) {
  const testSetupPath = `${options.projectRoot}/src/test-setup.ts`;

  const testSetupContent = tree.read(testSetupPath, 'utf-8');

  if (!testSetupContent) {
    console.warn(`WARNING: Could not read test setup file at ${testSetupPath}`);
    return;
  }
  if (testSetupContent.includes('reflect-metadata')) {
    return;
  }

  const newTestSetupContent = testSetupContent.replace(
    "import 'jest-preset-angular/setup-jest';",
    "import 'jest-preset-angular/setup-jest';\nimport 'reflect-metadata';",
  );

  tree.write(testSetupPath, newTestSetupContent);
}

export function extendJestConfigSwc(tree: Tree, options: NormalizedSchema) {
  const jestConfigPath = `${options.projectRoot}/jest.config.ts`;

  const jestConfigContent = tree.read(jestConfigPath, 'utf-8');

  if (!jestConfigContent) {
    console.warn(
      `WARNING: Could not read jest config file at ${jestConfigPath}`,
    );
    return;
  }

  if (jestConfigContent.includes('swcAngularJestTransformer()')) {
    return;
  }

  const updatedJestConfig = tsquery.replace(
    jestConfigContent,
    'PropertyAssignment[name.text="transform"]',
    (node) => {
      const addForHtml =
        tsquery.query(
          node,
          'ObjectLiteralExpression > PropertyAssignment[name.text="^.+\\\\.(ts|mjs|js|html)$"]',
        ).length > 0;

      if (addForHtml) {
        return `transform: {
          '^.+\\\\.(ts|mjs|js)$': swcAngularJestTransformer(),
          '^.+\\\\.(html)$': [
            'jest-preset-angular',
            {
              tsconfig: '<rootDir>/tsconfig.spec.json',
              stringifyContentPathRegex: '\\\\.(html|svg)$',
            },
          ],
        }`;
      }
      return `transform: {
        '^.+\\.[tj]s$': swcAngularJestTransformer(),
      }`;
    },
  );

  const addImport = `import { swcAngularJestTransformer } from '@jscutlery/swc-angular';

  ${updatedJestConfig}`;

  tree.write(jestConfigPath, addImport);
}
