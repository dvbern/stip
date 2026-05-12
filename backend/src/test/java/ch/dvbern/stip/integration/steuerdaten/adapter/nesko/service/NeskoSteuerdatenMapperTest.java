package ch.dvbern.stip.integration.steuerdaten.adapter.nesko.service;

import java.math.BigDecimal;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.AufwaendeSelbstErwerbType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.MannFrauEffSatzType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.SteuerdatenType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.VeranlagungsStatusType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
class NeskoSteuerdatenMapperTest {

    private static final int STEUERJAHR = 2023;

    private static final double TOTAL_EINKUENFTE_EFFEKTIV = 50_000;
    private static final double TOTAL_EINKUENFTE_SATZBESTIMMEND = 48_000;

    private static final double VERMOEGEN_EFFEKTIV = 10_000;
    private static final double VERMOEGEN_SATZBESTIMMEND = 9_000;

    private static final double STEUERBETRAG_KANTON = 1_200;
    private static final double STEUERBETRAG_BUND = 300;
    private static final double MIETWERT_KANTON = 500;

    private static final double FAHRKOSTEN_MANN_EFFEKTIV = 1_000;
    private static final double FAHRKOSTEN_MANN_SATZBESTIMMEND = 900;
    private static final double FAHRKOSTEN_FRAU_EFFEKTIV = 600;
    private static final double FAHRKOSTEN_FRAU_SATZBESTIMMEND = 500;

    private static final double VERPFLEGUNG_MANN_EFFEKTIV = 200;
    private static final double VERPFLEGUNG_MANN_SATZBESTIMMEND = 150;
    private static final double VERPFLEGUNG_FRAU_EFFEKTIV = 100;
    private static final double VERPFLEGUNG_FRAU_SATZBESTIMMEND = 80;

    private static final double SAEULE3A_MANN_EFFEKTIV = 1_000;
    private static final double SAEULE3A_MANN_SATZBESTIMMEND = 900;
    private static final double SAEULE3A_FRAU_EFFEKTIV = 800;
    private static final double SAEULE3A_FRAU_SATZBESTIMMEND = 700;
    private static final int SAEULE3A_TOTAL = 1_800;

    private static final double SAEULE2_PERSOENLICH_ANGEFRAGTE_EFFEKTIV = 500;
    private static final double SAEULE2_PERSOENLICH_ANGEFRAGTE_SATZBESTIMMEND = 400;
    private static final double SAEULE2_ER_ANGEFRAGTE_EFFEKTIV = 200;
    private static final double SAEULE2_ER_ANGEFRAGTE_SATZBESTIMMEND = 150;
    private static final double SAEULE2_PERSOENLICH_EHEPARTNER_EFFEKTIV = 300;
    private static final double SAEULE2_PERSOENLICH_EHEPARTNER_SATZBESTIMMEND = 250;
    private static final double SAEULE2_ER_EHEPARTNER_EFFEKTIV = 100;
    private static final double SAEULE2_ER_EHEPARTNER_SATZBESTIMMEND = 80;
    private static final int SAEULE2_TOTAL = 500;

    private static final double VERMOEGEN_NEGATIV_EFFEKTIV = -5_000;
    private static final double VERMOEGEN_NEGATIV_SATZBESTIMMEND = -3_000;

    private static EffSatzType effSatz(double effektiv, double satzbestimmend) {
        final var t = new EffSatzType();
        t.setEffektiv(BigDecimal.valueOf(effektiv));
        t.setSatzbestimmend(BigDecimal.valueOf(satzbestimmend));
        return t;
    }

    private static MannFrauEffSatzType mannFrau(EffSatzType mann, EffSatzType frau) {
        final var t = new MannFrauEffSatzType();
        t.setMann(mann);
        t.setFrau(frau);
        return t;
    }

    private static GetSteuerdatenResponse minimalResponse(SteuerdatenType steuerdaten) {
        final var response = new GetSteuerdatenResponse();
        response.setSteuerjahr(STEUERJAHR);
        response.setSteuerdaten(steuerdaten);
        return response;
    }

    private static SteuerdatenType minimalSteuerdaten() {
        final var sd = new SteuerdatenType();
        sd.setStatusVeranlagung(VeranlagungsStatusType.AUTOMATISCH_DEFINITIV_VERANLAGT);
        sd.setTotalEinkuenfte(effSatz(TOTAL_EINKUENFTE_EFFEKTIV, TOTAL_EINKUENFTE_SATZBESTIMMEND));
        sd.setSteuerbaresVermoegenKanton(effSatz(VERMOEGEN_EFFEKTIV, VERMOEGEN_SATZBESTIMMEND));
        sd.setSteuerbetragKanton(BigDecimal.valueOf(STEUERBETRAG_KANTON));
        sd.setSteuerbetragBund(BigDecimal.valueOf(STEUERBETRAG_BUND));
        sd.setMietwertKanton(BigDecimal.valueOf(MIETWERT_KANTON));
        return sd;
    }


    @Test
    void toSteuerdatenPortData_basicFields_areMappedCorrectly() {
        final var sd = minimalSteuerdaten();
        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getSteuerJahr(), is(STEUERJAHR));
        assertThat(result.getVeranlagungsStatus(), is(VeranlagungsStatusType.AUTOMATISCH_DEFINITIV_VERANLAGT.value()));
        assertThat(result.getSteuernKantonGemeinde(), is((int) STEUERBETRAG_KANTON));
        assertThat(result.getSteuernBund(), is((int) STEUERBETRAG_BUND));
        assertThat(result.getEigenmietwert(), is((int) MIETWERT_KANTON));
    }

    @Test
    void toSteuerdatenPortData_totalEinkuenfte_takesMaxOfEffektivAndSatzbestimmend() {
        final var sd = minimalSteuerdaten();
        // satzbestimmend (48_000) < effektiv (50_000) → max is 50_000
        sd.setTotalEinkuenfte(effSatz(TOTAL_EINKUENFTE_EFFEKTIV, TOTAL_EINKUENFTE_SATZBESTIMMEND));

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getTotalEinkuenfte(), is((int) TOTAL_EINKUENFTE_EFFEKTIV));
    }

    @Test
    void toSteuerdatenPortData_vermoegen_isZeroWhenNegative() {
        final var sd = minimalSteuerdaten();
        sd.setSteuerbaresVermoegenKanton(effSatz(VERMOEGEN_NEGATIV_EFFEKTIV, VERMOEGEN_NEGATIV_SATZBESTIMMEND));

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getVermoegen(), is(0));
    }

    @Test
    void toSteuerdatenPortData_vermoegen_isPositiveValue() {
        final var sd = minimalSteuerdaten();
        sd.setSteuerbaresVermoegenKanton(effSatz(VERMOEGEN_EFFEKTIV, VERMOEGEN_SATZBESTIMMEND));

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getVermoegen(), is((int) VERMOEGEN_EFFEKTIV));
    }

    @Test
    void toSteuerdatenPortData_nullMietwertKanton_defaultsToZero() {
        final var sd = minimalSteuerdaten();
        sd.setMietwertKanton(null);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getEigenmietwert(), is(0));
    }

    @Test
    void toSteuerdatenPortData_nullSteuerbetrage_defaultToZero() {
        final var sd = minimalSteuerdaten();
        sd.setSteuerbetragKanton(null);
        sd.setSteuerbetragBund(null);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getSteuernKantonGemeinde(), is(0));
        assertThat(result.getSteuernBund(), is(0));
    }

    @Test
    void toSteuerdatenPortData_familie_unselbstaendigWhenBothSUS_true() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(true);
        sd.setFrauErwerbstaetigkeitSUS(true);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getIsArbeitsverhaeltnisSelbstaendig(), is(false));
    }

    @Test
    void toSteuerdatenPortData_familie_selbstaendigWhenMannSUS_false() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(false);
        sd.setFrauErwerbstaetigkeitSUS(true);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getIsArbeitsverhaeltnisSelbstaendig(), is(true));
    }

    @Test
    void toSteuerdatenPortData_mutter_selbstaendigWhenFrauSUS_false() {
        final var sd = minimalSteuerdaten();
        sd.setFrauErwerbstaetigkeitSUS(false);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.MUTTER);

        assertThat(result.getIsArbeitsverhaeltnisSelbstaendig(), is(true));
    }

    @Test
    void toSteuerdatenPortData_vater_selbstaendigWhenMannSUS_false() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(false);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.VATER);

        assertThat(result.getIsArbeitsverhaeltnisSelbstaendig(), is(true));
    }

    @Test
    void toSteuerdatenPortData_defaultSUS_null_treatedAsUnselbstaendig() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(null);
        sd.setFrauErwerbstaetigkeitSUS(null);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getIsArbeitsverhaeltnisSelbstaendig(), is(false));
    }

    @Test
    void toSteuerdatenPortData_saeule3aAndSaeule2_areZeroWhenUnselbstaendig() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(true);
        sd.setFrauErwerbstaetigkeitSUS(true);

        final var beitraege = mannFrau(
            effSatz(SAEULE3A_MANN_EFFEKTIV, SAEULE3A_MANN_SATZBESTIMMEND),
            effSatz(SAEULE3A_FRAU_EFFEKTIV, SAEULE3A_FRAU_SATZBESTIMMEND)
        );
        sd.setBeitraegeSaeule3A(beitraege);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getSaeule3a(), is(0));
        assertThat(result.getSaeule2(), is(0));
    }

    @Test
    void toSteuerdatenPortData_saeule3a_sumsMannAndFrauWhenSelbstaendig() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(false);
        sd.setFrauErwerbstaetigkeitSUS(true);

        // mann max(1000,900)=1000, frau max(800,700)=800 → total 1800
        sd.setBeitraegeSaeule3A(mannFrau(
            effSatz(SAEULE3A_MANN_EFFEKTIV, SAEULE3A_MANN_SATZBESTIMMEND),
            effSatz(SAEULE3A_FRAU_EFFEKTIV, SAEULE3A_FRAU_SATZBESTIMMEND)
        ));

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getSaeule3a(), is(SAEULE3A_TOTAL));
    }

    @Test
    void toSteuerdatenPortData_saeule2_calculatedFromAufwaendeWhenSelbstaendig() {
        final var sd = minimalSteuerdaten();
        sd.setMannErwerbstaetigkeitSUS(false);

        // angefragte person: persoenlich=500, er=200 → net 300
        final var aufwaendeAngefragtePerson = new AufwaendeSelbstErwerbType();
        aufwaendeAngefragtePerson.setPersoenlicheBeitraegeSaeule2(
            effSatz(SAEULE2_PERSOENLICH_ANGEFRAGTE_EFFEKTIV, SAEULE2_PERSOENLICH_ANGEFRAGTE_SATZBESTIMMEND)
        );
        aufwaendeAngefragtePerson.setERBelasteteAnteileSaeule2(
            effSatz(SAEULE2_ER_ANGEFRAGTE_EFFEKTIV, SAEULE2_ER_ANGEFRAGTE_SATZBESTIMMEND)
        );
        sd.setAufwaendeSelbstErwerbAngefragtePerson(aufwaendeAngefragtePerson);

        // ehepartner: persoenlich=300, er=100 → net 200
        final var aufwaendeEhepartner = new AufwaendeSelbstErwerbType();
        aufwaendeEhepartner.setPersoenlicheBeitraegeSaeule2(
            effSatz(SAEULE2_PERSOENLICH_EHEPARTNER_EFFEKTIV, SAEULE2_PERSOENLICH_EHEPARTNER_SATZBESTIMMEND)
        );
        aufwaendeEhepartner.setERBelasteteAnteileSaeule2(
            effSatz(SAEULE2_ER_EHEPARTNER_EFFEKTIV, SAEULE2_ER_EHEPARTNER_SATZBESTIMMEND)
        );
        sd.setAufwaendeSelbstErwerbEhepartnerIn(aufwaendeEhepartner);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        // saeule2 = (500-200) + (300-100) = 300 + 200 = 500
        assertThat(result.getSaeule2(), is(SAEULE2_TOTAL));
    }

    @Test
    void toSteuerdatenPortData_familie_fahrkostenTakesMannForHauptAndFrauForPartner() {
        final var sd = minimalSteuerdaten();
        // Mann effektiv=1000, Frau effektiv=600
        sd.setFahrkosten(mannFrau(
            effSatz(FAHRKOSTEN_MANN_EFFEKTIV, FAHRKOSTEN_MANN_SATZBESTIMMEND),
            effSatz(FAHRKOSTEN_FRAU_EFFEKTIV, FAHRKOSTEN_FRAU_SATZBESTIMMEND)
        ));
        sd.setKostenAuswaertigeVerpflegung(mannFrau(
            effSatz(VERPFLEGUNG_MANN_EFFEKTIV, VERPFLEGUNG_MANN_SATZBESTIMMEND),
            effSatz(VERPFLEGUNG_FRAU_EFFEKTIV, VERPFLEGUNG_FRAU_SATZBESTIMMEND)
        ));

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getFahrkosten(), is((int) FAHRKOSTEN_MANN_EFFEKTIV));
        assertThat(result.getFahrkostenPartner(), is((int) FAHRKOSTEN_FRAU_EFFEKTIV));
        assertThat(result.getVerpflegung(), is((int) VERPFLEGUNG_MANN_EFFEKTIV));
        assertThat(result.getVerpflegungPartner(), is((int) VERPFLEGUNG_FRAU_EFFEKTIV));
    }

    @Test
    void toSteuerdatenPortData_mutter_fahrkostenTakesFrauForHauptAndMannForPartner() {
        final var sd = minimalSteuerdaten();
        sd.setFahrkosten(mannFrau(
            effSatz(FAHRKOSTEN_MANN_EFFEKTIV, FAHRKOSTEN_MANN_SATZBESTIMMEND),
            effSatz(FAHRKOSTEN_FRAU_EFFEKTIV, FAHRKOSTEN_FRAU_SATZBESTIMMEND)
        ));
        sd.setKostenAuswaertigeVerpflegung(mannFrau(
            effSatz(VERPFLEGUNG_MANN_EFFEKTIV, VERPFLEGUNG_MANN_SATZBESTIMMEND),
            effSatz(VERPFLEGUNG_FRAU_EFFEKTIV, VERPFLEGUNG_FRAU_SATZBESTIMMEND)
        ));

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.MUTTER);

        assertThat(result.getFahrkosten(), is((int) FAHRKOSTEN_FRAU_EFFEKTIV));        // Frau
        assertThat(result.getFahrkostenPartner(), is((int) FAHRKOSTEN_MANN_EFFEKTIV)); // Mann
        assertThat(result.getVerpflegung(), is((int) VERPFLEGUNG_FRAU_EFFEKTIV));      // Frau
        assertThat(result.getVerpflegungPartner(), is((int) VERPFLEGUNG_MANN_EFFEKTIV)); // Mann
    }

    @Test
    void toSteuerdatenPortData_nullFahrkosten_defaultsToZero() {
        final var sd = minimalSteuerdaten();
        sd.setFahrkosten(null);
        sd.setKostenAuswaertigeVerpflegung(null);

        final var result = NeskoSteuerdatenMapper.toSteuerdatenPortData(minimalResponse(sd), SteuerdatenTyp.FAMILIE);

        assertThat(result.getFahrkosten(), is(0));
        assertThat(result.getFahrkostenPartner(), is(0));
        assertThat(result.getVerpflegung(), is(0));
        assertThat(result.getVerpflegungPartner(), is(0));
    }
}
