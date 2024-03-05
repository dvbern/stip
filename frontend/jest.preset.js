const nxPreset = require('@nx/jest/preset').default;

module.exports = {
  ...nxPreset,
  coverageReporters: ['lcov'],
  testTimeout: process.env['CI'] ? 20000 : 10000,
};
