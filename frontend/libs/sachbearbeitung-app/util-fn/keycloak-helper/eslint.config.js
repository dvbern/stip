const baseConfig = require('../../../../eslint.config.js');

module.exports = [
  ...baseConfig,
  {
    rules: {
      '@nx/enforce-module-boundaries': [
        'error',
        {
          allow: ['@angular/common/http'],
        },
      ],
    },
  },
];
