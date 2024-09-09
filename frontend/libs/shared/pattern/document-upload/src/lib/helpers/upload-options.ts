import { Signal, computed } from '@angular/core';

import { DocumentOptions } from '@dv/shared/model/dokument';
import { Dokument, DokumentTyp } from '@dv/shared/model/gesuch';

export const DOKUMENT_TYP_TO_DOCUMENT_OPTIONS: {
  readonly [K in DokumentTyp]: DocumentOptions['titleKey'];
} = {
  PERSON_NIEDERLASSUNGSSTATUS_B:
    'shared.form.person.file.AUFENTHALTSBEWILLIGUNG_B',
  PERSON_NIEDERLASSUNGSSTATUS_C:
    'shared.form.person.file.NIEDERLASSUNGSBEWILLIGUNG_C',
  PERSON_NIEDERLASSUNGSSTATUS_COMPLETE: 'shared.form.person.file.FLUECHTLING',
  PERSON_KESB_ERNENNUNG: 'shared.form.person.file.VORMUNDSCHAFT',
  PERSON_MIETVERTRAG: 'shared.form.person.file.EIGENER_HAUSHALT',
  PERSON_SOZIALHILFEBUDGET: 'shared.form.person.file.SOZIALHILFE',
  PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG: 'shared.form.person.file.ZIVILSTAND',
  PERSON_AUSWEIS: 'shared.form.person.file.HEIMATORT',
  FAMILIENSITUATION_GEBURTSSCHEIN:
    'shared.form.familiensituation.file.GEBURTSSCHEIN',
  FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER:
    'shared.form.familiensituation.file.AUFENTHALT_UNBEKANNT_VATER',
  FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER:
    'shared.form.familiensituation.file.AUFENTHALT_UNBEKANNT_MUTTER',
  FAMILIENSITUATION_TRENNUNGSKONVENTION:
    'shared.form.familiensituation.file.TRENNUNGSKONVENTION',
  STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG',
  STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG',
  STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG',
  STEUERDATEN_ERGAENZUNGSLEISTUNGEN_VATER:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_VATER',
  STEUERDATEN_SOZIALHILFEBUDGET_VATER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_VATER',
  STEUERDATEN_ERGAENZUNGSLEISTUNGEN_MUTTER:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_MUTTER',
  STEUERDATEN_SOZIALHILFEBUDGET_MUTTER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_MUTTER',
  GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE:
    'shared.form.geschwister.file.AUSBILDUNGSSTAETTE',
  KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION:
    'shared.form.kinder.file.UNTERHALTSVERTRAG_TRENNUNGSKONVENTION',
  KINDER_ALIMENTENVERORDUNG: 'shared.form.kinder.file.ALIMENTENVERORDNUNG',
  PARTNER_AUSBILDUNG_LOHNABRECHNUNG:
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
  EK_VERMOEGEN: 'shared.form.einnahmenkosten.file.VERMOEGEN',
};

/**
 * Factory function to create a computed signal that returns an upload options object for a given view
 *
 * The view should have the following properties:
 * - **gesuchTrancheId**: string | undefined
 * - **allowTypes**:      string | undefined
 * - **readonly**:        boolean
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
    trancheId: string | undefined;
    allowTypes: string | undefined;
    readonly: boolean;
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
   * @param options - additional options for the upload. If initialDocuments are provided, the the upload component will not try to fetch the documents on initialization, but display the provided documents instead.
   */
  return (
    lazyDokumentTyp: (view: T) => DokumentTyp | null | undefined,
    options?: { singleUpload?: boolean; initialDocuments?: Dokument[] },
  ) => {
    return computed<DocumentOptions | null>(() => {
      const trancheId = view().trancheId;
      const allowTypes = view().allowTypes;
      const readonly = view().readonly;
      const dokumentTyp = lazyDokumentTyp(view);
      return dokumentTyp && trancheId && allowTypes
        ? {
            allowTypes,
            titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
            dokumentTyp,
            singleUpload: options?.singleUpload ?? false,
            trancheId,
            initialDocuments: options?.initialDocuments,
            readonly,
          }
        : null;
    });
  };
}

export function createDocumentOptions(options: {
  trancheId: string;
  allowTypes: string;
  dokumentTyp: DokumentTyp;
  initialDocuments?: Dokument[];
  singleUpload?: boolean;
  readonly: boolean;
}): DocumentOptions {
  const {
    trancheId,
    allowTypes,
    dokumentTyp,
    initialDocuments,
    singleUpload,
    readonly,
  } = options;
  return {
    allowTypes,
    titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
    dokumentTyp,
    singleUpload: singleUpload ?? false,
    trancheId,
    initialDocuments,
    readonly,
  };
}
