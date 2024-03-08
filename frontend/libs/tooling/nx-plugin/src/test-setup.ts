import 'jest-preset-angular/setup-jest';
import { jest } from '@jest/globals';

const original: any = jest.requireActual('@nx/devkit');

jest.mock('prettier', () => ({
  format: jest.fn(),
}));

jest.mock('@nx/devkit', () => ({
  ...original,
  // Ignore prettier in jest tests due to the ESM issues
  formatFiles: async () => {},
  ensurePackage: ensurePackageMock,
}));

// ensurePackage also needs to be mocked to avoid ESM issues with prettier
function ensurePackageMock(pkg: string, version: string) {
  if (pkg === 'prettier') {
    return;
  }
  return original.ensurePackage(pkg, version);
}
