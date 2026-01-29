import { TranslocoService } from '@jsverse/transloco';
import type {
  Content,
  ContentBase,
  ContentTable,
  DynamicContent,
  TableCell,
} from 'pdfmake/interfaces';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { GesuchTranche, SharedModelGesuch } from '@dv/shared/model/gesuch';
import { isDefined, type } from '@dv/shared/model/type-util';
import { toFormatedNumber } from '@dv/shared/util/maskito-util';

import {
  AvailableFonts,
  MARGINS_FOOTER,
  MARGINS_HEADER,
  SEPARATOR_HEIGHT,
  Styles,
  TABLE_BG_COLORS,
} from '../types';
import { _t, formatDate } from '../utils/helpers';

export const getFonts = (origin: string) =>
  ({
    OpenSans: {
      normal: `${origin}/assets/fonts/OpenSans-Regular.ttf`,
      bold: `${origin}/assets/fonts/OpenSans-Bold.ttf`,
    },
  }) as const satisfies Record<
    AvailableFonts,
    { normal: string; bold: string }
  >;

export const getTable = (body: TableCell[][]): ContentTable => {
  return {
    layout: {
      fillColor: (i) =>
        i % 2 === 0 ? TABLE_BG_COLORS.shaded : TABLE_BG_COLORS.default,
    },
    table: {
      dontBreakRows: true,
      widths: ['*', '*'],
      body,
    },
  };
};

export const getPageHeader = (
  t: TranslocoService,
  gesuch: SharedModelGesuch,
  tranche: GesuchTranche,
): Content => {
  return {
    margin: MARGINS_HEADER,
    text: _t(t, 'shared.export.tranche.header', {
      fallnummer: gesuch.fallNummer,
      gesuchsnummer: gesuch.gesuchNummer,
      gueltigAb: formatDate(tranche.gueltigAb),
      gueltigBis: formatDate(tranche.gueltigBis),
    }),
    style: type<Styles>('header'),
  };
};
export const getPageFooter =
  (): DynamicContent => (currentPage: number, pageCount: number) => ({
    text: `${currentPage} / ${pageCount}`,
    alignment: 'right',
    margin: MARGINS_FOOTER,
  });

export const getValueList = <Key extends string>(
  t: TranslocoService,
  valueList: (readonly [Key, string | number | undefined] | null)[],
  translation: (key: Key) => SharedTranslationKey,
) =>
  valueList.filter(isDefined).map(
    ([key, value]) =>
      [
        {
          text: _t(t, translation(key)),
          bold: true,
        },
        {
          text:
            typeof value === 'number' ? toFormatedNumber(value) : (value ?? ''),
        },
      ] satisfies Content,
  );

export const getSeparator = (): Content => ({
  text: '',
  marginTop: SEPARATOR_HEIGHT,
});

export const getTitle = (
  t: TranslocoService,
  titleKey: SharedTranslationKey,
  options?: ContentBase,
): Content => {
  return {
    text: _t(t, titleKey),
    style: type<Styles>('title'),
    ...options,
  };
};

export const getSection = (
  t: TranslocoService,
  titleKey: SharedTranslationKey,
  options?: ContentBase,
): Content => {
  return {
    text: _t(t, titleKey),
    style: type<Styles>('section'),
    ...options,
  };
};
