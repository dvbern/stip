import type { Margins, PageSize } from 'pdfmake/interfaces';

import { GesuchTranche, SharedModelGesuch } from '@dv/shared/model/gesuch';

export type ExportView = {
  isEditingAenderung: boolean;
  gesuch: SharedModelGesuch;
  tranche: GesuchTranche;
  periode: {
    bezeichnungDe: string;
    bezeichnungFr: string;
    year: string;
    einreichefrist: string;
  };
  sachbearbeiter?: string;
};
export type ExportLand = {
  id?: string;
  deKurzform: string;
  frKurzform: string;
};
export type Styles = 'title' | 'section';
export type AvailableFonts = 'OpenSans';

export const STYLES = {
  title: {
    bold: true,
    fontSize: 22,
    marginBottom: 12,
  },
  section: {
    bold: true,
    fontSize: 14,
    lineHeight: 1.2,
    marginTop: 10,
  },
} as const satisfies Record<Styles, unknown>;
export const CURRENT_FONT = 'OpenSans' satisfies AvailableFonts;
export const HEADER_NAME_LIMIT = 25;
export const UEBERSICHT_NAME_LIMIT = 35;
export const PAGE_SIZE = {
  width: 595.28,
  height: 841.89,
} as const satisfies PageSize; // A4 size in points https://github.com/bpampuch/pdfmake/blob/89c16c3acf83bb1cd84156c86a9407f1739b929b/src/standardPageSizes.js#L8C6-L8C22
export const MARGINS_PAGE = [40, 50] as const satisfies Margins;
export const MARGINS_HEADER = [40, 20, 40, 0] as const satisfies Margins;
export const MARGINS_FOOTER = [40, 15, 40, 0] as const satisfies Margins;
export const SEPARATOR_HEIGHT = 5;
export const TABLE_BG_COLORS = {
  default: '#ffffff',
  shaded: '#f8f8f8',
};
