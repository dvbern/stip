import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { GesuchTranche } from '@dv/shared/model/gesuch';

import { getTable, getTitle, getValueList } from './generic';
import { getBoolean } from '../utils/helpers';

export const getSteuererklaerungs = (
  t: TranslocoService,
  tranche: GesuchTranche,
): Content => {
  const steuererklaerungs = tranche.gesuchFormular?.steuererklaerung;
  if (!steuererklaerungs?.length) {
    return [];
  }

  return [
    steuererklaerungs.map((steuererklaerung, i) => [
      getTitle(
        t,
        `shared.eltern-steuererklaerung.title.${steuererklaerung.steuerdatenTyp}`,
        i == 0 ? { pageBreak: 'before' } : { marginTop: 30 },
      ),
      getTable([
        ...getValueList(
          t,
          [
            [
              'steuererklaerungInBern',
              getBoolean(t, steuererklaerung.steuererklaerungInBern),
            ],
            ['unterhaltsbeitraege', steuererklaerung.unterhaltsbeitraege],
            ['renten', steuererklaerung.renten],
            ['ergaenzungsleistungen', steuererklaerung.ergaenzungsleistungen],
            ['einnahmenBGSA', steuererklaerung.einnahmenBGSA],
            ['andereEinnahmen', steuererklaerung.andereEinnahmen],
          ],
          (key) => `shared.form.eltern-steuererklaerung.${key}.label`,
        ),
      ]),
    ]),
  ];
};
