import { Signal, computed } from '@angular/core';

import { DokumentOptions } from '@dv/shared/model/dokument';
import {
  CustomDokumentTyp,
  Dokument,
  DokumentTyp,
  GesuchDokument,
  UnterschriftenblattDokument,
  UnterschriftenblattDokumentTyp,
} from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';

export const DOKUMENT_TYP_TO_DOCUMENT_OPTIONS: {
  readonly [K in DokumentTyp]: DokumentOptions['titleKey'];
} & Partial<
  Record<`${DokumentTyp}_DESCRIPTION`, DokumentOptions['descriptionKey']>
> = {
  AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE:
    'shared.form.ausbildung.file.AUSBILDUNGSSTAETTE',
  PERSON_NIEDERLASSUNGSSTATUS_SAISONARBEITEND_A:
    'shared.form.person.file.SAISONARBEITERBEWILLIGUNG_A',
  PERSON_NIEDERLASSUNGSSTATUS_B:
    'shared.form.person.file.AUFENTHALTSBEWILLIGUNG_B',
  PERSON_NIEDERLASSUNGSSTATUS_C:
    'shared.form.person.file.NIEDERLASSUNGSBEWILLIGUNG_C',
  PERSON_NIEDERLASSUNGSSTATUS_PARTNER_ERWERBSTAETIG_UND_KIND_CI:
    'shared.form.person.file.EHEPARTNERBEWILLIGUNG_CI',
  PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS:
    'shared.form.person.file.VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS',
  PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS_DESCRIPTION:
    'shared.form.person.file.VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS_DESCRIPTION',
  PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT:
    'shared.form.person.file.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT',
  PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT_DESCRIPTION:
    'shared.form.person.file.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT_DESCRIPTION',
  PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON:
    'shared.form.person.file.VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON',
  PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON_DESCRIPTION:
    'shared.form.person.file.VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON_DESCRIPTION',
  PERSON_NIEDERLASSUNGSSTATUS_GRENZGAENGIG_G:
    'shared.form.person.file.GRENZGAENGERBEWILLIGUNG_G',
  PERSON_NIEDERLASSUNGSSTATUS_KURZAUFENTHALT_L:
    'shared.form.person.file.KURZAUFENTHALTERBEWILLIGUNG_L',
  PERSON_NIEDERLASSUNGSSTATUS_ASYLSUCHEND_N:
    'shared.form.person.file.ASYLSUCHENDEBEWILLIGUNG_N',
  PERSON_NIEDERLASSUNGSSTATUS_SCHUTZBEDUERFTIG_S:
    'shared.form.person.file.SCHUTZBEDUERFTIGEBEWILLIGUNG_S',
  PERSON_NIEDERLASSUNGSSTATUS_MELDEPFLICHTIG:
    'shared.form.person.file.MELDEPFLICHTIGEBEWILLIGUNG_10',
  PERSON_NIEDERLASSUNGSSTATUS_DIPLOMATISCHE_FUNKTION:
    'shared.form.person.file.DIPLOMATENBEWILLIGUNG_11',
  PERSON_NIEDERLASSUNGSSTATUS_INTERNATIONALE_FUNKTION:
    'shared.form.person.file.INTERNATIONALEFUNKTIONAERINBEWILLIGUNG_12',
  PERSON_NIEDERLASSUNGSSTATUS_NICHT_ZUGETEILT:
    'shared.form.person.file.NICHTZUGEWIESEN_13',
  PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN:
    'shared.form.person.file.BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN',
  PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN_DESCRIPTION:
    'shared.form.person.file.BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN_DESCRIPTION',
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
  FAMILIENSITUATION_TRENNUNGSKONVENTION_DESCRIPTION:
    'shared.form.familiensituation.file.TRENNUNGSKONVENTION_DESCRIPTION',
  ELTERN_ERGAENZUNGSLEISTUNGEN_VATER:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_VATER',
  ELTERN_ERGAENZUNGSLEISTUNGEN_VATER_DESCRIPTION:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_VATER_DESCRIPTION',
  ELTERN_SOZIALHILFEBUDGET_VATER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_VATER',
  ELTERN_SOZIALHILFEBUDGET_VATER_DESCRIPTION:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_VATER_DESCRIPTION',
  ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_MUTTER',
  ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER_DESCRIPTION:
    'shared.form.eltern.file.ERGAENZUNGSLEISTUNGEN_MUTTER_DESCRIPTION',
  ELTERN_SOZIALHILFEBUDGET_MUTTER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_MUTTER',
  ELTERN_SOZIALHILFEBUDGET_MUTTER_DESCRIPTION:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_MUTTER_DESCRIPTION',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER_DESCRIPTION:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_DESCRIPTION',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER_DESCRIPTION:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_DESCRIPTION',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG',
  ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE_DESCRIPTION:
    'shared.form.eltern.file.MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_DESCRIPTION',
  GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE:
    'shared.form.geschwister.file.AUSBILDUNGSSTAETTE',
  GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE_DESCRIPTION:
    'shared.form.geschwister.file.AUSBILDUNGSSTAETTE_DESCRIPTION',
  KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION:
    'shared.form.kinder.file.UNTERHALTSVERTRAG_TRENNUNGSKONVENTION',
  KINDER_ALIMENTENVERORDUNG: 'shared.form.kinder.file.ALIMENTENVERORDNUNG',
  PARTNER_AUSBILDUNG_LOHNABRECHNUNG:
    'shared.form.partner.file.AUSBILDUNG_LOHNABRECHNUNG',
  PARTNER_AUSBILDUNG_LOHNABRECHNUNG_DESCRIPTION:
    'shared.form.partner.file.AUSBILDUNG_LOHNABRECHNUNG_DESCRIPTION',
  PARTNER_BELEG_OV_ABONNEMENT: 'shared.form.partner.file.OV_ABONNEMENT',
  PARTNER_BELEG_OV_ABONNEMENT_DESCRIPTION:
    'shared.form.partner.file.OV_ABONNEMENT_DESCRIPTION',
  EK_BELEG_ALIMENTE: 'shared.form.einnahmenkosten.file.ALIMENTE',
  EK_BELEG_ALIMENTE_DESCRIPTION:
    'shared.form.einnahmenkosten.file.ALIMENTE_DESCRIPTION',
  EK_BELEG_KINDERZULAGEN: 'shared.form.einnahmenkosten.file.KINDERZULAGEN',
  EK_VERFUEGUNG_GEMEINDE_INSTITUTION:
    'shared.form.einnahmenkosten.file.GEMEINDE_INSTITUTION',
  EK_VERFUEGUNG_GEMEINDE_INSTITUTION_DESCRIPTION:
    'shared.form.einnahmenkosten.file.GEMEINDE_INSTITUTION_DESCRIPTION',
  EK_BELEG_BEZAHLTE_RENTEN: 'shared.form.einnahmenkosten.file.BEZAHLTE_RENTEN',
  EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN:
    'shared.form.einnahmenkosten.file.ERGAENZUNGSLEISTUNGEN',
  EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO:
    'shared.form.einnahmenkosten.file.ERGAENZUNGSLEISTUNGEN_EO',
  EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO_DESCRIPTION:
    'shared.form.einnahmenkosten.file.ERGAENZUNGSLEISTUNGEN_EO_DESCRIPTION',
  EK_BELEG_OV_ABONNEMENT: 'shared.form.einnahmenkosten.file.OV_ABONNEMENT',
  EK_BELEG_OV_ABONNEMENT_DESCRIPTION:
    'shared.form.einnahmenkosten.file.OV_ABONNEMENT_DESCRIPTION',
  EK_MIETVERTRAG: 'shared.form.einnahmenkosten.file.MIETVERTRAG',
  EK_BELEG_BETREUUNGSKOSTEN_KINDER:
    'shared.form.einnahmenkosten.file.BETREUUNGSKOSTEN_KINDER',
  EK_BELEG_BETREUUNGSKOSTEN_KINDER_DESCRIPTION:
    'shared.form.einnahmenkosten.file.BETREUUNGSKOSTEN_KINDER_DESCRIPTION',
  EK_LOHNABRECHNUNG: 'shared.form.einnahmenkosten.file.LOHNABRECHNUNG',
  EK_VERDIENST: 'shared.form.einnahmenkosten.file.VERDIENST',
  EK_VERDIENST_DESCRIPTION:
    'shared.form.einnahmenkosten.file.VERDIENST_DESCRIPTION',
  EK_VERMOEGEN: 'shared.form.einnahmenkosten.file.VERMOEGEN',
  EK_VERMOEGEN_DESCRIPTION:
    'shared.form.einnahmenkosten.file.VERMOEGEN_DESCRIPTION',
  DARLEHEN_BETREIBUNGSREGISTERAUSZUG:
    'shared.form.darlehen.file.BETREIBUNGSREGISTERAUSZUG',
  DARLEHEN_AUFSTELLUNG_KOSTEN_ELTERN:
    'shared.form.darlehen.file.AUFSTELLUNG_KOSTEN_ELTERN',
  DARLEHEN_AUFSTELLUNG_KOSTEN_ELTERN_DESCRIPTION:
    'shared.form.darlehen.file.AUFSTELLUNG_KOSTEN_ELTERN_DESCRIPTION',
  DARLEHEN_KOPIE_SCHULGELDRECHNUNG:
    'shared.form.darlehen.file.SCHULGELDRECHNUNG',
  DARLEHEN_KOPIE_SCHULGELDRECHNUNG_DESCRIPTION:
    'shared.form.darlehen.file.SCHULGELDRECHNUNG_DESCRIPTION',
  DARLEHEN_BELEGE_ANSCHAFFUNGEN:
    'shared.form.darlehen.file.BELEGE_ANSCHAFFUNGEN',
  DARLEHEN_BELEGE_ANSCHAFFUNGEN_DESCRIPTION:
    'shared.form.darlehen.file.BELEGE_ANSCHAFFUNGEN_DESCRIPTION',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE_DESCRIPTION',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER:
    'shared.form.eltern-steuererklaerung.file.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER_DESCRIPTION',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER:
    'shared.form.eltern-steuererklaerung.file.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER_DESCRIPTION',
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
    permissions: PermissionMap;
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
   * @param options - some additional options for the upload.
   *                  If initialDocuments are provided, the the upload component will not try to fetch the documents on initialization,
   *                  but display the provided documents instead. Primarily used for Dokument Table view.
   */
  return (
    lazyDokumentTyp: (view: T) => DokumentTyp | null | undefined,
    options?: { initialDocuments?: Dokument[] },
  ) => {
    return computed<DokumentOptions | null>(() => {
      const permissions = view().permissions;
      const trancheId = view().trancheId;
      const allowTypes = view().allowTypes;
      const dokumentTyp = lazyDokumentTyp(view);
      return dokumentTyp && trancheId && allowTypes
        ? {
            permissions,
            allowTypes,
            titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
            descriptionKey:
              DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[`${dokumentTyp}_DESCRIPTION`],
            dokument: {
              trancheId,
              dokumentTyp,
              art: 'GESUCH_DOKUMENT',
            },
            initialDokumente: options?.initialDocuments,
          }
        : null;
    });
  };
}

export function createGesuchDokumentOptions(options: {
  trancheId: string;
  allowTypes: string;
  dokumentTyp: DokumentTyp;
  gesuchDokument?: GesuchDokument;
  initialDocuments?: Dokument[];
  permissions: PermissionMap;
}): DokumentOptions {
  const {
    trancheId,
    allowTypes,
    dokumentTyp,
    gesuchDokument,
    initialDocuments,
    permissions,
  } = options;
  return {
    allowTypes,
    permissions,
    titleKey: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
    descriptionKey:
      DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[`${dokumentTyp}_DESCRIPTION`],
    dokument: {
      dokumentTyp,
      trancheId,
      gesuchDokument,
      art: 'GESUCH_DOKUMENT',
    },
    initialDokumente: initialDocuments,
  };
}

export function createAdditionalDokumentOptions(options: {
  gesuchId: string;
  trancheId: string;
  allowTypes: string;
  dokumentTyp: UnterschriftenblattDokumentTyp;
  gesuchDokument?: UnterschriftenblattDokument;
  initialDocuments?: Dokument[];
  permissions: PermissionMap;
}): DokumentOptions {
  const {
    gesuchId,
    trancheId,
    allowTypes,
    dokumentTyp,
    gesuchDokument,
    initialDocuments,
    permissions,
  } = options;
  return {
    allowTypes,
    permissions,
    titleKey: `shared.dokumente.file.unterschriftenblatt.${dokumentTyp}`,
    dokument: {
      dokumentTyp,
      gesuchId,
      trancheId,
      art: 'UNTERSCHRIFTENBLATT',
      gesuchDokument,
    },
    initialDokumente: initialDocuments,
  };
}

export function createCustomDokumentOptions(options: {
  gesuchId: string;
  trancheId: string;
  allowTypes: string;
  dokumentTyp: CustomDokumentTyp;
  gesuchDokument?: GesuchDokument;
  initialDocuments?: Dokument[];
  permissions: PermissionMap;
}): DokumentOptions {
  const {
    gesuchId,
    trancheId,
    allowTypes,
    dokumentTyp,
    gesuchDokument,
    initialDocuments,
    permissions,
  } = options;
  return {
    allowTypes,
    permissions,
    titleKey: dokumentTyp.type,
    descriptionKey: dokumentTyp.description,
    dokument: {
      dokumentTyp,
      gesuchId,
      trancheId,
      gesuchDokument,
      art: 'CUSTOM_DOKUMENT',
    },
    initialDokumente: initialDocuments,
  };
}
