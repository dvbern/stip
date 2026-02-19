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

package ch.dvbern.stip.api.ausbildung.entity;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AusbildungUnterbruchAntragConstraintValidatorTest {

    @Test
    void testRequiredFieldsInBearbeitungGs() {
        final var validator = new AusbildungUnterbruchAntragRequiredFieldsConstraintValidator();
        final var antrag = new AusbildungUnterbruchAntrag();

        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setAusbildung(new Ausbildung());
        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setGesuch(new Gesuch());
        assertThat(validator.isValid(antrag, null), is(true));
    }

    @Test
    void testRequiredFieldsEingegeben() {
        final var validator = new AusbildungUnterbruchAntragRequiredFieldsConstraintValidator();
        final var antrag = new AusbildungUnterbruchAntrag();
        antrag.setStatus(AusbildungUnterbruchAntragStatus.EINGEGEBEN);

        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setAusbildung(new Ausbildung());
        antrag.setGesuch(new Gesuch());
        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setGueltigkeit(new DateRange());
        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setKommentarGS("asd");
        assertThat(validator.isValid(antrag, null), is(true));
    }

    @Test
    void testRequiredFieldsAkzeptiert() {
        final var validator = new AusbildungUnterbruchAntragRequiredFieldsConstraintValidator();
        final var antrag = new AusbildungUnterbruchAntrag();
        antrag.setStatus(AusbildungUnterbruchAntragStatus.AKZEPTIERT);

        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setAusbildung(new Ausbildung());
        antrag.setGesuch(new Gesuch());
        antrag.setGueltigkeit(new DateRange());
        antrag.setKommentarGS("asd");
        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setKommentarSB("asd");
        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setMonateOhneAnspruch(0);
        assertThat(validator.isValid(antrag, null), is(true));
    }

    @Test
    void testRequiredFieldsAbgelehnt() {
        final var validator = new AusbildungUnterbruchAntragRequiredFieldsConstraintValidator();
        final var antrag = new AusbildungUnterbruchAntrag();
        antrag.setStatus(AusbildungUnterbruchAntragStatus.ABGELEHNT);

        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setAusbildung(new Ausbildung());
        antrag.setGesuch(new Gesuch());
        antrag.setGueltigkeit(new DateRange());
        antrag.setKommentarGS("asd");
        assertThat(validator.isValid(antrag, null), is(false));
        antrag.setKommentarSB("asd");
        assertThat(validator.isValid(antrag, null), is(true));
    }

    @Test
    void testGueltigkeit() {
        final var validator = new AusbildungUnterbruchAntragGueltigkeitConstraintValidator();
        final var antrag = new AusbildungUnterbruchAntrag();
        final var gesuch = new Gesuch();
        final var tranche = new GesuchTranche();
        tranche.setTyp(GesuchTrancheTyp.TRANCHE);
        final var now = LocalDate.now();
        final var trancheGueltigkeit = new DateRange();
        trancheGueltigkeit.setGueltigAb(now.minusYears(1));
        trancheGueltigkeit.setGueltigBis(now);
        tranche.setGueltigkeit(trancheGueltigkeit);
        gesuch.setGesuchTranchen(List.of(tranche));

        assertThat(validator.isValid(antrag, null), is(true));

        antrag.setGesuch(gesuch);
        assertThat(validator.isValid(antrag, null), is(true));

        final var antragGueltigkeit = new DateRange();
        antrag.setGueltigkeit(antragGueltigkeit);
        antragGueltigkeit.setGueltigAb(trancheGueltigkeit.getGueltigAb());
        antragGueltigkeit.setGueltigBis(trancheGueltigkeit.getGueltigBis());
        assertThat(validator.isValid(antrag, null), is(true));
        antragGueltigkeit.setGueltigAb(trancheGueltigkeit.getGueltigAb().minusMonths(1));
        antragGueltigkeit.setGueltigBis(trancheGueltigkeit.getGueltigBis());
        assertThat(validator.isValid(antrag, null), is(false));
        antragGueltigkeit.setGueltigAb(trancheGueltigkeit.getGueltigAb());
        antragGueltigkeit.setGueltigBis(trancheGueltigkeit.getGueltigBis().plusDays(1));
        assertThat(validator.isValid(antrag, null), is(false));
        antragGueltigkeit.setGueltigAb(trancheGueltigkeit.getGueltigAb().plusMonths(1));
        antragGueltigkeit.setGueltigBis(trancheGueltigkeit.getGueltigBis().minusMonths(1));
        assertThat(validator.isValid(antrag, null), is(true));

    }

}
