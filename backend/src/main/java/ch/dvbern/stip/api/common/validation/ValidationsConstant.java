/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.validation;

import java.util.regex.Pattern;

public final class ValidationsConstant {
    public static final String EMAIL_VALIDATION_PATTERN =
        "((?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
        "*|\\\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e"
        + "-\\x7f])*\\\")@"
        +
        "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:"
        + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
        +
        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
        + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01"
        +
        "-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\]))";
    private static final int BUCHSTABEN_RANGE_VALIDATION_MAX_LENGTH = 3;
    public static final String BUCHSTABEN_RANGE_VALIDATION_STRING_PATTERN =
        "([A-Z]{1,%1$s}-[A-Z]{1,%1$s}|[A-Z]{1,%1$s})(,([A-Z]{1,%1$s}-[A-Z]{1,%1$s}|[A-Z]{1,%1$s}))*".formatted(
            BUCHSTABEN_RANGE_VALIDATION_MAX_LENGTH
        );
    public static final Pattern BUCHSTABEN_RANGE_VALIDATION_PATTERN =
        Pattern.compile(BUCHSTABEN_RANGE_VALIDATION_STRING_PATTERN);
    public static final String VALIDATION_NACHNAME_NOTBLANK_MESSAGE =
        "{jakarta.validation.constraints.nachname.NotBlank.message}";
    public static final String VALIDATION_VORNAME_NOTBLANK_MESSAGE =
        "{jakarta.validation.constraints.vorname.NotBlank.message}";
    public static final String VALIDATION_IZW_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.izw.required.message}";
    public static final String VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.izw.required.null.message}";
    public static final String VALIDATION_HEIMATORT_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.heimatort.required.message}";
    public static final String VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.heimatort.required.null.message}";
    public static final String VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.niederlassungsstatus.required.message}";
    public static final String VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.niederlassungsstatus.required.null.message}";
    public static final String VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.wohnsitzanteil.required.message}";
    public static final String VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.wohnsitzanteil.required.null.message}";
    public static final String VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.alternativeAusbildung.required.message}";
    public static final String VALIDATION_PARTNER_KOSTEN_FIELDS_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.partner.kostenfields.required.message}";
    public static final String VALIDATION_PARTNER_KOSTEN_FIELDS_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.partner.kostenfields.required.null.message}";
    public static final String VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.alternativeAusbildung.required.null.message}";
    public static final String VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.ausbildung.required.message}";
    public static final String VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.ausbildung.required.null.message}";
    public static final String VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.werZahltAlimente.required.message}";
    public static final String VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.werZahltAlimente.required.null.message}";
    public static final String VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.obhut.required.message}";
    public static final String VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.obhut.required.null.message}";
    public static final String VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE =
        "{jakarta.validation.constraints.wohnsitzanteil.berechnung.message}";
    public static final String VALIDATION_OBHUT_GEMEINSAM_BERECHNUNG_MESSAGE =
        "{jakarta.validation.constraints.obhut.berechnung.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.art.required.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_BERUFSBEZEICHNUNG_NOTNULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.ausbildung.berufsbezeichnung.notnull.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.ausbildung.berufsbezeichnung.null.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_NOTNULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.ausbildung.fachrichtung.notnull.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_NULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.ausbildung.fachrichtung.null.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NOTNULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.ausbildung.titelDesAbschlusses.notnull.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.ausbildung.titelDesAbschlusses.null.message}";
    public static final String VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE =
        "{jakarta.validation.constraints.lebenslaufitem.art.required.null.message}";
    public static final String VALIDATION_AHV_MESSAGE = "{jakarta.validation.constraints.ahv.message}";
    public static final String VALIDATION_AHV_IF_SWISS_MESSAGE = "{jakarta.validation.contraints.ahvIfSwiss.message}";
    public static final String VALIDATION_EMAIL_MESSAGE = "{jakarta.validation.constraints.email.message}";
    public static final String VALIDATION_BUCHSTABEN_RANGE_MESSAGE =
        "{jakarta.validation.constraints.buchstaben.range.message}";
    public static final String VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.familiensituation.eltern.entity.message}";
    public static final String VALIDATION_FAMILIENSITUATION_STEUERDATEN_ENTITY_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.familiensituation.steuerdaten.entity.message}";
    public static final String VALIDATION_FAMILIENSITUATION_WOHNSITUATION_MESSAGE =
        "{jakarta.validation.constraints.familiensituation.wohnsituation.message}";
    public static final String VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE =
        "{jakarta.validation.constraints.lebenslauf.luckenlos.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_ALIMENTE_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.alimente.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_RENTEN_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.renten.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_WOHNKOSTEN_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.wohnkosten.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_VERMOEGEN_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.vermoegen.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.zulagen.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_DARLEHEN_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.darlehen.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_VERMOEGEN_INVALID_VALUE_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.vermoegen.invalid.value.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE2_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.ausbildungskosten.stufe2.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.ausbildungskosten.stufe3.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_STEUERJAHR_INVALID_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.steuerjahr.invalid.message}";
    public static final String VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE =
        "{jakarta.validation.constraints.steuerdaten.steuerjahr.invalid.message}";
    public static final String VALIDATION_LEBENSLAUF_AUSBILDUNG_UEBERSCHNEIDEN_MESSAGE =
        "{jakarta.validation.constraints.lebenslauf.ausbildung.ueberschneiden.message}";
    public static final String VALIDATION_PERSONS_AHV_NUMBER_UNIQUENESS_MESSAGE =
        "{jakarta.validation.constraints.sv.uniqueness.message}";
    public static final String VALIDATION_PARTNER_NULL_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.partner.null.required.message}";
    public static final String VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.partner.not.null.required.message}";
    public static final String VALIDATION_ALIMENTE_REQUIRED_WHEN_ALIMENTEREGELUNG =
        "{jakarta.validation.constraints.alimente.alimenteregelung.required.message}";
    public static final String VALIDATION_ALIMENTE_NULL_WHEN_NO_ALIMENTEREGELUNG =
        "{jakarta.validation.constraints.alimente.no.alimenteregelung.null.message}";
    public static final String VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE =
        "dvbern.stip.validation.gesuch.einreichen.svnummer.unique.message";
    public static final String VALIDATION_IBAN_MESSAGE = "{jakarta.validation.constraints.iban.message}";
    public static final String VALIDATION_EINREISEDATUM_FIELD_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.personInAusbildung.einreisedatum.required}";
    public static final String VALIDATION_NO_OVERLAP_INAUSBILDUNGEN =
        "{jakarta.validation.constraints.ausbildungen.noOverlap}";
    public static final String VALIDATION_AUSBILDUNG_ENDDATE_AFTER_STARTDATE =
        "{jakarta.validation.constraints.ausbildung.endDate.after.message}";
    public static final String VALIDATION_VERMOEGEN_VORJAHR_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.person.vermoegenVorjahr.required.message}";
    public static final String VALIDATION_EINNAHMEN_KOSTEN_BETREUUNGSKOSTEN_KINDER_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.einnahmen.kosten.betreuungskostenKinder.required.message}";
    public static final String VALIDATION_DOCUMENTS_REQUIRED_MESSAGE =
        "{jakarta.validation.constraints.gesuchFormular.documents.required.message}";
    public static final String VALIDATION_AUSBILDUNGSORT_IF_SWISS_MESSAGE =
        "{jakarta.validation.constraints.ausbildung.ausbildungsort.if.swiss.message}";
    public static final String VALIDATION_DOCUMENTS_INVALID_STATUS =
        "{jakarta.validation.constraints.gesuchFormular.documents.invalidStatus.message}";
    public static final String VALIDATION_TRANCHEN_INVALID_STATUS =
        "{jakarta.validation.constraints.gesuchTranche.invalidStatus.message}";
    public static final String VALIDATION_STEUERDATEN_FIELDS_REQUIRED_IF_TYPE =
        "{jakarta.validation.constraints.steuerdaten.fieldRequiredIfType.message}";
    public static final String VALIDATION_DOCUMENTS_STATUS_ACCEPTED_MESSAGE =
        "{jakarta.validation.constraints.gesuchFormular.documents.mustBe.accepted.message}";
    public static final String VALIDATION_GESUCH_NO_VALID_GESUCHSPERIODE =
        "jakarta.validation.constraints.gesuch.create.gesuchsperiode.notfound.message";
    public static final String VALIDATION_AUSBILDUNG_ONLY_ONE_GESUCH_PER_YEAR =
        "{jakarta.validation.constraints.ausbildung.gesuch.onlyone.message}";
    public static final String VALIDATION_ADRESSE_LAND_NOT_STATELESS =
        "{jakarta.validation.constraints.adresse.land.no.stateless}";

    private ValidationsConstant() {
        throw new IllegalStateException("Validations Constant class");
    }
}
