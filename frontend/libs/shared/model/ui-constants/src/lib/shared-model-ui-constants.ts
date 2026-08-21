export const DVBreakpoints = {
  SM: 640,
  MD: 768,
  LG: 1024,
  XL: 1280,
  '2XL': 1536,
} as const;

export const PAGE_SIZES = [10, 20, 50];
export const DEFAULT_PAGE_SIZE = 20;
export const INPUT_DELAY = 600;
export const TOOLTIP_DELAY = 200;

export const THREE_LINE_CHARS_COUNT = 150;

export const BFSCODE_SCHWEIZ = '8100';

export const MAX_EINKOMMEN = 9_999_999;

// Is negative so not every form step has to be included!
export const hideAktionenRoutes = ['infos', 'darlehen'];
// Is negative so not every form step has to be included!
export const notGesuchRoute = ['aenderung', 'infos', 'darlehen'];
