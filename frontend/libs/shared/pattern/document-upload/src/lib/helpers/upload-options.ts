import { Signal, computed } from '@angular/core';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import {
  CustomDokumentOptions,
  DokumentOptions,
  StandardDokumentOptions,
} from '@dv/shared/model/dokument';
import {
  CustomDokumentTyp,
  DarlehenDokumentType,
  Dokument,
  DokumentTyp,
  FreiwilligDarlehen,
  GesuchDokument,
  UnterschriftenblattDokument,
  UnterschriftenblattDokumentTyp,
} from '@dv/shared/model/gesuch';
import {
  DarlehenPermissionMap,
  PermissionMap,
} from '@dv/shared/model/permission-state';

export const DARLEHEN_DOKUMENT_TYP_TO_DOCUMENT_OPTIONS: {
  readonly [K in DarlehenDokumentType]: SharedTranslationKey;
} & Partial<
  Record<`${DarlehenDokumentType}_DESCRIPTION`, SharedTranslationKey>
> = {
  BETREIBUNGS_AUSZUG: 'shared.form.darlehen.file.BETREIBUNGSREGISTERAUSZUG',
  AUFSTELLUNG_KOSTEN_ELTERN:
    'shared.form.darlehen.file.AUFSTELLUNG_KOSTEN_ELTERN',
  AUFSTELLUNG_KOSTEN_ELTERN_DESCRIPTION:
    'shared.form.darlehen.file.AUFSTELLUNG_KOSTEN_ELTERN_DESCRIPTION',
  KOPIE_SCHULGELDRECHNUNG: 'shared.form.darlehen.file.SCHULGELDRECHNUNG',
  KOPIE_SCHULGELDRECHNUNG_DESCRIPTION:
    'shared.form.darlehen.file.SCHULGELDRECHNUNG_DESCRIPTION',
  BELEGE_ANSCHAFFUNGEN: 'shared.form.darlehen.file.BELEGE_ANSCHAFFUNGEN',
  BELEGE_ANSCHAFFUNGEN_DESCRIPTION:
    'shared.form.darlehen.file.BELEGE_ANSCHAFFUNGEN_DESCRIPTION',
};

export const DOKUMENT_TYP_TO_DOCUMENT_OPTIONS: {
  readonly [K in DokumentTyp]: SharedTranslationKey;
} & Partial<Record<`${DokumentTyp}_DESCRIPTION`, SharedTranslationKey>> = {
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
  ELTERN_SOZIALHILFEBUDGET_VATER:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_VATER',
  ELTERN_SOZIALHILFEBUDGET_VATER_DESCRIPTION:
    'shared.form.eltern.file.SOZIALHILFEBUDGET_VATER_DESCRIPTION',
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
  KINDER_ERGAENZUNGSLEISTUNGEN: 'shared.form.kinder.file.ERGAENZUNGSLEISTUNGEN',
  KINDER_UND_AUSBILDUNGSZULAGEN:
    'shared.form.kinder.file.UND_AUSBILDUNGSZULAGEN',
  KINDER_RENTEN: 'shared.form.kinder.file.RENTEN',
  KINDER_ANDERE_EINNAHMEN: 'shared.form.kinder.file.ANDERE_EINNAHMEN',
  KINDER_ANDERE_EINNAHMEN_DESCRIPTION:
    'shared.form.kinder.file.ANDERE_EINNAHMEN_DESCRIPTION',
  EK_BELEG_UNTERHALTSBEITRAEGE:
    'shared.form.einnahmenkosten.file.UNTERHALTSBEITRAEGE',
  EK_BELEG_UNTERHALTSBEITRAEGE_DESCRIPTION:
    'shared.form.einnahmenkosten.file.UNTERHALTSBEITRAEGE_DESCRIPTION',
  EK_BELEG_EINNAHMEN_BGSA: 'shared.form.einnahmenkosten.file.EINNAHMEN_BGSA',
  EK_BELEG_TAGGELDER_AHV_IV:
    'shared.form.einnahmenkosten.file.TAGGELDER_AHV_IV',
  EK_BELEG_ANDERE_EINNAHMEN:
    'shared.form.einnahmenkosten.file.ANDERE_EINNAHMEN',
  EK_BELEG_ANDERE_EINNAHMEN_DESCRIPTION:
    'shared.form.einnahmenkosten.file.ANDERE_EINNAHMEN_DESCRIPTION',
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
  EK_VERMOEGEN: 'shared.form.einnahmenkosten.file.VERMOEGEN',
  EK_VERMOEGEN_DESCRIPTION:
    'shared.form.einnahmenkosten.file.VERMOEGEN_DESCRIPTION',
  EK_PARTNER_BELEG_UNTERHALTSBEITRAEGE:
    'shared.form.einnahmenkosten.file.PARTNER_UNTERHALTSBEITRAEGE',
  EK_PARTNER_BELEG_UNTERHALTSBEITRAEGE_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_UNTERHALTSBEITRAEGE_DESCRIPTION',
  EK_PARTNER_BELEG_EINNAHMEN_BGSA:
    'shared.form.einnahmenkosten.file.PARTNER_EINNAHMEN_BGSA',
  EK_PARTNER_BELEG_TAGGELDER_AHV_IV:
    'shared.form.einnahmenkosten.file.PARTNER_TAGGELDER_AHV_IV',
  EK_PARTNER_BELEG_ANDERE_EINNAHMEN:
    'shared.form.einnahmenkosten.file.PARTNER_ANDERE_EINNAHMEN',
  EK_PARTNER_BELEG_ANDERE_EINNAHMEN_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_ANDERE_EINNAHMEN_DESCRIPTION',
  EK_PARTNER_BELEG_KINDERZULAGEN:
    'shared.form.einnahmenkosten.file.PARTNER_KINDERZULAGEN',
  EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION:
    'shared.form.einnahmenkosten.file.PARTNER_GEMEINDE_INSTITUTION',
  EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_GEMEINDE_INSTITUTION_DESCRIPTION',
  EK_PARTNER_BELEG_BEZAHLTE_RENTEN:
    'shared.form.einnahmenkosten.file.PARTNER_BEZAHLTE_RENTEN',
  EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN:
    'shared.form.einnahmenkosten.file.PARTNER_ERGAENZUNGSLEISTUNGEN',
  EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO:
    'shared.form.einnahmenkosten.file.PARTNER_ERGAENZUNGSLEISTUNGEN_EO',
  EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_ERGAENZUNGSLEISTUNGEN_EO_DESCRIPTION',
  EK_PARTNER_BELEG_OV_ABONNEMENT:
    'shared.form.einnahmenkosten.file.PARTNER_OV_ABONNEMENT',
  EK_PARTNER_BELEG_OV_ABONNEMENT_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_OV_ABONNEMENT_DESCRIPTION',
  EK_PARTNER_BELEG_BETREUUNGSKOSTEN_KINDER:
    'shared.form.einnahmenkosten.file.PARTNER_BETREUUNGSKOSTEN_KINDER',
  EK_PARTNER_BELEG_BETREUUNGSKOSTEN_KINDER_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_BETREUUNGSKOSTEN_KINDER_DESCRIPTION',
  EK_PARTNER_LOHNABRECHNUNG:
    'shared.form.einnahmenkosten.file.PARTNER_LOHNABRECHNUNG',
  EK_PARTNER_VERMOEGEN: 'shared.form.einnahmenkosten.file.PARTNER_VERMOEGEN',
  EK_PARTNER_VERMOEGEN_DESCRIPTION:
    'shared.form.einnahmenkosten.file.PARTNER_VERMOEGEN_DESCRIPTION',
  ELTERN_LOHNABRECHNUNG_VERMOEGEN_VATER:
    'shared.form.eltern.file.LOHNABRECHNUNG_VERMOEGEN_VATER',
  ELTERN_LOHNABRECHNUNG_VERMOEGEN_MUTTER:
    'shared.form.eltern.file.LOHNABRECHNUNG_VERMOEGEN_MUTTER',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.AUSBILDUNGSBEITRAEGE_FAMILIE',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_FAMILIE_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.AUSBILDUNGSBEITRAEGE_FAMILIE_DESCRIPTION',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER:
    'shared.form.eltern-steuererklaerung.file.AUSBILDUNGSBEITRAEGE_MUTTER',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_MUTTER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.AUSBILDUNGSBEITRAEGE_MUTTER_DESCRIPTION',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER:
    'shared.form.eltern-steuererklaerung.file.AUSBILDUNGSBEITRAEGE_VATER',
  STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_VATER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.AUSBILDUNGSBEITRAEGE_VATER_DESCRIPTION',
  STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_VATER:
    'shared.form.eltern-steuererklaerung.file.UNTERHALTSBEITRAEGE_VATER',
  STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_VATER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.UNTERHALTSBEITRAEGE_VATER_DESCRIPTION',
  STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_MUTTER:
    'shared.form.eltern-steuererklaerung.file.UNTERHALTSBEITRAEGE_MUTTER',
  STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_MUTTER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.UNTERHALTSBEITRAEGE_MUTTER_DESCRIPTION',
  STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.UNTERHALTSBEITRAEGE_FAMILIE',
  STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_FAMILIE_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.UNTERHALTSBEITRAEGE_FAMILIE_DESCRIPTION',
  STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_VATER:
    'shared.form.eltern-steuererklaerung.file.ERGAENZUNGSLEISTUNGEN_VATER',
  STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_MUTTER:
    'shared.form.eltern-steuererklaerung.file.ERGAENZUNGSLEISTUNGEN_MUTTER',
  STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.ERGAENZUNGSLEISTUNGEN_FAMILIE',
  STEUERERKLAERUNG_RENTEN_VATER:
    'shared.form.eltern-steuererklaerung.file.RENTEN_VATER',
  STEUERERKLAERUNG_RENTEN_VATER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.RENTEN_VATER_DESCRIPTION',
  STEUERERKLAERUNG_RENTEN_MUTTER:
    'shared.form.eltern-steuererklaerung.file.RENTEN_MUTTER',
  STEUERERKLAERUNG_RENTEN_MUTTER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.RENTEN_MUTTER_DESCRIPTION',
  STEUERERKLAERUNG_RENTEN_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.RENTEN_FAMILIE',
  STEUERERKLAERUNG_RENTEN_FAMILIE_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.RENTEN_FAMILIE_DESCRIPTION',
  STEUERERKLAERUNG_EINNAHMEN_BGSA_VATER:
    'shared.form.eltern-steuererklaerung.file.EINNAHMEN_BGSA_VATER',
  STEUERERKLAERUNG_EINNAHMEN_BGSA_MUTTER:
    'shared.form.eltern-steuererklaerung.file.EINNAHMEN_BGSA_MUTTER',
  STEUERERKLAERUNG_EINNAHMEN_BGSA_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.EINNAHMEN_BGSA_FAMILIE',
  STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER:
    'shared.form.eltern-steuererklaerung.file.ANDERE_EINNAHMEN_VATER',
  STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.ANDERE_EINNAHMEN_VATER_DESCRIPTION',
  STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER:
    'shared.form.eltern-steuererklaerung.file.ANDERE_EINNAHMEN_MUTTER',
  STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.ANDERE_EINNAHMEN_MUTTER_DESCRIPTION',
  STEUERERKLAERUNG_ANDERE_EINNAHMEN_FAMILIE:
    'shared.form.eltern-steuererklaerung.file.ANDERE_EINNAHMEN_FAMILIE',
  STEUERERKLAERUNG_ANDERE_EINNAHMEN_FAMILIE_DESCRIPTION:
    'shared.form.eltern-steuererklaerung.file.ANDERE_EINNAHMEN_FAMILIE_DESCRIPTION',
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
            allowTypes,
            info: {
              type: 'TRANSLATABLE',
              title: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
              description:
                DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[`${dokumentTyp}_DESCRIPTION`],
            },
            dokument: {
              permissions,
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

export function createDarlehenUploadOptionsFactory<
  T extends {
    darlehen: Signal<FreiwilligDarlehen | null | undefined>;
    allowTypes: string | undefined;
    permissions: Signal<DarlehenPermissionMap | undefined>;
  },
>(view: T) {
  return (
    lazyDokumentTyp: (view: T) => DarlehenDokumentType | null | undefined,
  ) => {
    return computed<DokumentOptions | null>(() => {
      const dokumentTyp = lazyDokumentTyp(view);
      const darlehenId = view.darlehen()?.id;
      const permissions = view.permissions();
      const allowTypes = view.allowTypes;
      return dokumentTyp && darlehenId && allowTypes && permissions
        ? {
            allowTypes,
            info: {
              type: 'TRANSLATABLE',
              title: DARLEHEN_DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
              description:
                DARLEHEN_DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[
                  `${dokumentTyp}_DESCRIPTION`
                ],
            },
            dokument: {
              permissions,
              darlehenId,
              dokumentTyp,
              art: 'DARLEHEN_DOKUMENT',
            },
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
}): StandardDokumentOptions {
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
    info: {
      type: 'TRANSLATABLE',
      title: DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[dokumentTyp],
      description:
        DOKUMENT_TYP_TO_DOCUMENT_OPTIONS[`${dokumentTyp}_DESCRIPTION`],
    },
    dokument: {
      permissions,
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
}): StandardDokumentOptions {
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
    info: {
      type: 'TRANSLATABLE',
      title: `shared.dokumente.file.unterschriftenblatt.${dokumentTyp}`,
    },
    dokument: {
      permissions,
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
}): CustomDokumentOptions {
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
    info: {
      type: 'TEXT',
      title: dokumentTyp.type,
      description: dokumentTyp.description,
    },
    dokument: {
      permissions,
      dokumentTyp,
      gesuchId,
      trancheId,
      gesuchDokument,
      art: 'CUSTOM_DOKUMENT',
    },
    initialDokumente: initialDocuments,
  };
}
