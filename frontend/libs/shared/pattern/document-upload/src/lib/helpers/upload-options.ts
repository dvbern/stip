import { Signal, computed } from '@angular/core';

import { DokumentTyp } from '@dv/shared/model/gesuch';

import { DocumentOptions } from '../upload.model';

export const DOKUMENT_TYP_TO_DOCUMENT_OPTIONS: {
  readonly [K in DokumentTyp]: DocumentOptions['titleKey'];
} = {
  PERSON_NIEDERLASSUNGSSTATUS_B:
    'shared.form.person.file.AUFENTHALTSBEWILLIGUNG_B',
  PERSON_NIEDERLASSUNGSSTATUS_C:
    'shared.form.person.file.NIEDERLASSUNGSBEWILLIGUNG_C',
  PERSON_NIEDERLASSUNGSSTATUS_COMPLETE: 'shared.form.person.file.FLUECHTLING',
  PERSON_VERMOEGENSNACHWEIS_VORJAHR:
    'shared.form.person.file.VERMOEGENSNACHWEIS_VORJAHR',
  PERSON_VERMOEGENSNACHWEIS: 'shared.form.person.file.VERMOEGENSNACHWEIS',
  PERSON_KESB_ERNENNUNG: 'shared.form.person.file.VORMUNDSCHAFT',
  PERSON_MIETVERTRAG: 'shared.form.person.file.EIGENER_HAUSHALT',
  PERSON_SOZIALHILFEBUDGET: 'shared.form.person.file.SOZIALHILFE',
  PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG: 'shared.form.person.file.ZIVILSTAND',
  PERSON_AUSWEIS: 'shared.form.person.file.HEIMATORT',
  ELTERN_DOK: 'shared.form.person.file.ELTERN_DOK',
  AUSBILDUNG_DOK: 'shared.form.person.file.AUSBILDUNG_DOK',
  PARTNER_DOK: 'shared.form.person.file.PARTNER_DOK',
};

/**
 * Factory function to create a computed signal that returns an upload options object for a given view
 *
 * The view should have the following properties:
 * - **gesuchId**:   string | undefined
 * - **allowTypes**: string | undefined
 *
 * @example
 * ```typescript
 * import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
 *
 * const viewSelector = createSelector(
     selectSharedDataAccessConfigsView, // to obtain the allowTypes
     ...
 * );
 * ...
 * viewSig = this.store.selectSignal(viewSelector);
 *
 * createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);
 * ...
 * wohnsitzBeiDocumentOptionsSig = this.createUploadOptionsSig(() => {
 *   const wohnsitzBei = this.wohnsitzBeiChangedSig();
 *   return wohnsitzBei === Wohnsitz.EIGENER_HAUSHALT
 *     ? DokumentTyp.PERSON_MIETVERTRAG
 *     : null;
 * });
 * ```
 */
export function createUploadOptionsFactory<
  T extends Signal<{
    gesuchId: string | undefined;
    allowTypes: string | undefined;
  }>,
>(view: T) {
  /**
   * Used to obtain a computed signal which returns an upload options object or `null` if the upload is not required
   *
   * @see {@link createUploadOptionsFactory}
   * @example
   * ```typescript
   * createUploadOptionsSig(() => {
   *   const wohnsitzBei = this.wohnsitzBeiChangedSig();
   *   return wohnsitzBei === Wohnsitz.EIGENER_HAUSHALT
   *     ? DokumentTyp.PERSON_MIETVERTRAG
   *     : null;
   * });
   * ```
   *
   * @param lazyDokumentTyp - a function that should return the {@link DokumentTyp} or `null | undefined` if the upload is not required
   * @param options - additional options for the upload
   */
  return (
    lazyDokumentTyp: (view: T) => DokumentTyp | null | undefined,
    options?: { singleUpload?: boolean },
  ) => {
    return computed<DocumentOptions | null>(() => {
      const gesuchId = view().gesuchId;
      const allowTypes = view().allowTypes;
      const dokumentTyp = lazyDokumentTyp(view);
      return dokumentTyp && gesuchId && allowTypes
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
