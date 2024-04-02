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
  FAMILIENSITUATION_GEBURTSSCHEIN:
    'shared.form.familiensituation.file.GEBURTSSCHEIN',
  FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER:
    'shared.form.familiensituation.file.AUFENTHALT_UNBEKANNT_VATER',
  FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER:
    'shared.form.familiensituation.file.AUFENTHALT_UNBEKANNT_MUTTER',
  FAMILIENSITUATION_TRENNUNGSKONVENTION:
    'shared.form.familiensituation.file.TRENNUNGSKONVENTION',
  ELTERN_LOHNABRECHNUNG_VERMOEGEN_VATER:
    'shared.form.eltern.file.LOHNABRECHNUNG_VERMOEGEN_VATER',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER',
  ELTERN_ERGAENZUNGSLEISTUNGEN_VATER:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_VATER',
  ELTERN_SOZIALHILFEBUDGET_VATER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_VATER',
  ELTERN_STEUERUNTERLAGEN_VATER:
    'shared.form.eltern.file.STEUERUNTERLAGEN_VATER',
  ELTERN_LOHNABRECHNUNG_VERMOEGEN_MUTTER:
    'shared.form.eltern.file.LOHNABRECHNUNG_VERMOEGEN_MUTTER',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER',
  ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_MUTTER',
  ELTERN_SOZIALHILFEBUDGET_MUTTER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_MUTTER',
  ELTERN_STEUERUNTERLAGEN_MUTTER:
    'shared.form.eltern.file.STEUERUNTERLAGEN_MUTTER',
  GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE:
    'shared.form.geschwister.file.AUSBILDUNGSSTAETTE',
  KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION:
    'shared.form.kinder.file.UNTERHALTSVERTRAG_TRENNUNGSKONVENTION',
  PARNTER_AUSBILUNG_LOHNABRECHNUNG:
    'shared.form.partner.file.AUSBILDUNG_LOHNABRECHNUNG',
  PARTNER_BELEG_OV_ABONNEMENT: 'shared.form.partner.file.OV_ABONNEMENT',
  AUSZAHLUNG_ABTRETUNGSERKLAERUNG:
    'shared.form.auszahlung.file.ABTRETUNGSERKLAERUNG',
  EK_BELEG_ALIMENTE: 'shared.form.einnahmenkosten.file.ALIMENTE',
  EK_BELEG_KINDERZULAGEN: 'shared.form.einnahmenkosten.file.KINDERZULAGEN',
  EK_VERFUEGUNG_GEMEINDE_INSTITUTION:
    'shared.form.einnahmenkosten.file.GEMEINDE_INSTITUTION',
  EK_BELEG_BEZAHLTE_RENTEN: 'shared.form.einnahmenkosten.file.BEZAHLTE_RENTEN',
  EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN:
    'shared.form.einnahmenkosten.file.ERGAENZUNGSLEISTUNGEN',
  EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO:
    'shared.form.einnahmenkosten.file.ERGAENZUNGSLEISTUNGEN_EO',
  EK_BELEG_OV_ABONNEMENT: 'shared.form.einnahmenkosten.file.OV_ABONNEMENT',
  EK_MIETVERTRAG: 'shared.form.einnahmenkosten.file.MIETVERTRAG',
  EK_BELEG_BETREUUNGSKOSTEN_KINDER:
    'shared.form.einnahmenkosten.file.BETREUUNGSKOSTEN_KINDER',
  EK_LOHNABRECHNUNG: 'shared.form.einnahmenkosten.file.LOHNABRECHNUNG',
  EK_VERDIENST: 'shared.form.einnahmenkosten.file.VERDIENST',
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
