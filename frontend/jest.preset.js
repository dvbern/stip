const nxPreset = require('@nx/jest/preset').default;

process.env.TZ = 'UTC';

module.exports = {
  ...nxPreset,
  collectCoverage: true,
  coverageReporters: ['lcov'],
  testTimeout: process.env['CI'] ? 20000 : 10000,
};
