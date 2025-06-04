import { MaskitoOptions } from '@maskito/core';

export const MASK_SOZIALVERSICHERUNGSNUMMER: MaskitoOptions = {
  mask: [
    /\d/,
    /\d/,
    /\d/,
    '.',
    /\d/,
    /\d/,
    /\d/,
    /\d/,
    '.',
    /\d/,
    /\d/,
    /\d/,
    /\d/,
    '.',
    /\d/,
    /\d/,
  ],
};

export const MASK_IBAN: MaskitoOptions = {
  mask: [
    /\d/,
    /\d/,
    ' ',
    /\d/,
    /\d/,
    /\d/,
    /\d/,
    ' ',
    /\d/,
    /\d/,
    /\d/,
    /\d/,
    ' ',
    /\d/,
    /\d/,
    /\d/,
    /\d/,
    ' ',
    /\d/,
    /\d/,
    /\d/,
    /\d/,
    ' ',
    /(\d|[A-z])/,
  ],
};

export const MASK_MM_YYYY: MaskitoOptions = {
  mask: [/\d/, /\d/, '.', /\d/, /\d/, /\d/, /\d/],
};

/**
 * The pattern is very long and has ", ', `, \ and other special characters, so it is base64 encoded to avoid escaping issues
 * and window.atob is used instead of atob to avoid using the node.js types
 *
 * @see https://stackoverflow.com/a/201378
 */
export const PATTERN_EMAIL = new RegExp(
  window.atob(
    'Xig/OlthLXpBLVowLTkhIyQlJicqKy89P15fYHt8fX4tXSsoPzpcLlthLXpBLVowLTkhIyQlJicqKy89P15fYHt8fX4tXSspKnwiKD86W1x4MDEtXHgwOFx4MGJceDBjXHgwZS1ceDFmXHgyMVx4MjMtXHg1Ylx4NWQtXHg3Zl18XFxbXHgwMS1ceDA5XHgwYlx4MGNceDBlLVx4N2ZdKSoiKUAoPzooPzpbYS16QS1aMC05XSg/OlthLXpBLVowLTktXSpbYS16QS1aMC05XSk/XC4pK1thLXpBLVowLTldKD86W2EtekEtWjAtOS1dKlthLXpBLVowLTldKT98XFsoPzooPzooMig1WzAtNV18WzAtNF1bMC05XSl8MVswLTldWzAtOV18WzEtOV0/WzAtOV0pKVwuKXszfSg/OigyKDVbMC01XXxbMC00XVswLTldKXwxWzAtOV1bMC05XXxbMS05XT9bMC05XSl8W2EtekEtWjAtOS1dKlthLXpBLVowLTldOig/OltceDAxLVx4MDhceDBiXHgwY1x4MGUtXHgxZlx4MjEtXHg1YVx4NTMtXHg3Zl18XFxbXHgwMS1ceDA5XHgwYlx4MGNceDBlLVx4N2ZdKSspXF0pJA==',
  ),
  'i',
);
