import type { Config } from 'tailwindcss';
import plugin from 'tailwindcss/plugin';

// Used for tailwindcss IntelliSense
const tw = (value: string) => value;

export default {
  content: ['./apps/**/*.{html,ts}', './libs/**/*.{html,ts}'],
  prefix: 'tw-',
  theme: {
    extend: {},
  },
  plugins: [
    require('@tailwindcss/container-queries'),
    plugin(function ({ addComponents }) {
      addComponents({
        '.form-grid': {
          [tw(
            '@apply tw-grid md:tw-grid-cols-2 xl:tw-max-w-3xl tw-pt-12 tw-gap-6',
          )]: {},
        },
      });
    }),
  ],
} satisfies Config;
