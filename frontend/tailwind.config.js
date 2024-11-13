/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./apps/**/*.{html,ts}', './libs/**/*.{html,ts}'],
  prefix: 'tw-',
  theme: {
    extend: {},
  },
  plugins: [require('@tailwindcss/container-queries')],
};
