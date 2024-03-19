import { Signal, computed } from '@angular/core';

import { DokumentTyp } from '@dv/shared/model/gesuch';

import { DocumentOptions } from '../upload.model';

export function createUploadOptionsFactory(
  view: Signal<{
    gesuchId: string | undefined;
    allowTypes: string | undefined;
  }>,
) {
  return (dokumentTyp: DokumentTyp, options?: { singleUpload?: boolean }) => {
    const DOKUMENT_TYP_TO_DOCUMENT_OPTIONS: {
      [K in DokumentTyp]: DocumentOptions['titleKey'];
    } = {
      PERSON_NIEDERLASSUNGSSTATUS_B:
        'shared.form.person.file.AUFENTHALTSBEWILLIGUNG_B',
      PERSON_NIEDERLASSUNGSSTATUS_C:
        'shared.form.person.file.NIEDERLASSUNGSBEWILLIGUNG_C',
      PERSON_NIEDERLASSUNGSSTATUS_COMPLETE:
        'shared.form.person.file.FLUECHTLING',
      PERSON_VERMOEGENSNACHWEIS_VORJAHR:
        'shared.form.person.file.VERMOEGENSNACHWEIS_VORJAHR',
      PERSON_VERMOEGENSNACHWEIS: 'shared.form.person.file.VERMOEGENSNACHWEIS',
      PERSON_KESB_ERNENNUNG: 'shared.form.person.file.VORMUNDSCHAFT',
      PERSON_MIETVERTRAG: 'shared.form.person.file.EIGENER_HAUSHALT',
      PERSON_SOZIALHILFEBUDGET: 'shared.form.person.file.SOZIALHILFE',
      PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG:
        'shared.form.person.file.ZIVILSTAND',
      PERSON_AUSWEIS: 'shared.form.person.file.HEIMATORT',
      ELTERN_DOK: 'shared.form.person.file.ELTERN_DOK',
      AUSBILDUNG_DOK: 'shared.form.person.file.AUSBILDUNG_DOK',
      PARTNER_DOK: 'shared.form.person.file.PARTNER_DOK',
    };
    return computed<DocumentOptions | null>(() => {
      const { gesuchId, allowTypes } = view();
      return gesuchId && allowTypes
        ? {
            allowTypes,
            titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
            dokumentTyp,
            singleUpload: options?.singleUpload ?? false,
            gesuchId,
          }
        : null;
    });
  };
}
