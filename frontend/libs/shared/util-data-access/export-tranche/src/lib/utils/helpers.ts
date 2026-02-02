import { TranslocoService } from '@jsverse/transloco';
import { format } from 'date-fns';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { lowercased, prepareLanguage } from '@dv/shared/model/type-util';

import { ExportLand } from '../types';

export const _t = <T extends object>(
  t: TranslocoService,
  key: SharedTranslationKey,
  params?: T,
) => {
  return t.translate<string>(key, params);
};

export const getFullname = (
  person?: { vorname: string; nachname: string },
  limit?: number,
) => {
  if (!person) {
    return '';
  }
  const { vorname, nachname } = person;
  if (!limit || vorname.length + nachname.length <= limit) {
    return `${vorname} ${nachname}`;
  }
  return `${limitText(vorname, limit - 3)} ${nachname[0]}.`;
};

export const getBoolean = (
  t: TranslocoService,
  boolean: boolean | undefined,
) => {
  if (boolean === undefined) {
    return '';
  }
  return _t(t, boolean ? 'shared.ui.yes' : 'shared.ui.no');
};

export const getLandName = (
  t: TranslocoService,
  laender: ExportLand[],
  landId: string,
) => {
  const land = laender.find((l) => l.id === landId);
  if (!land) {
    return '';
  }
  return land[`${lowercased(prepareLanguage(t.getActiveLang()))}Kurzform`];
};

export const formatDate = (date: string | Date | undefined): string => {
  if (!date) {
    return '';
  }
  return format(date, 'dd.MM.yyyy');
};

export const limitText = (text: string, limit: number): string => {
  if (!text || text.length <= limit) {
    return text;
  }
  return `${text.slice(0, limit - 3)}...`;
};
