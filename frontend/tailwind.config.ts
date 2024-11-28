import { resolve } from 'node:path';

import type { Config } from 'tailwindcss';

import cssplugin from './cssplugin';

export default {
  content: ['./apps/**/*.{html,ts}', './libs/**/*.{html,ts}'],
  prefix: 'tw-',
  theme: {
    extend: {
      colors: {
        'dv-warning': 'var(--dv-yellow-bg-subtle)',
      },
    },
  },
  plugins: [
    require('@tailwindcss/container-queries'),
    cssplugin(
      resolve(__dirname, './libs/shared/styles/theme/src/tailwind.css'),
    ),
  ],
} satisfies Config;
