const nxPreset = require('@nx/jest/preset').default;

process.env.TZ = 'UTC';

module.exports = {
  ...nxPreset,
  coverageReporters: ['lcov'],
  testTimeout: process.env['CI'] ? 20000 : 10000,
};
