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

package ch.dvbern.stip.api.einnahmen_kosten.service;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EinnahmenKostenMappingUtilTest {

    private EinnahmenKosten einnahmenKosten;

    @BeforeEach
    void setUp() {
        einnahmenKosten = new EinnahmenKosten();
    }

    @Test
    void isNotQuellenbesteuertIfCH() {
        final var personInAusbildung = new PersonInAusbildung()
            .setNationalitaet(new Land().setLaendercodeBfs(WellKnownLand.CHE.getLaendercodeBfs()));
        assertFalse(EinnahmenKostenMappingUtil.isQuellenBesteuert(personInAusbildung));
    }

    @Test
    void isNotQuellenbesteuertIfAusweisC() {
        final var personInAusbildung = new PersonInAusbildung()
            .setNationalitaet(new Land().setLaendercodeBfs(WellKnownLand.IRN.getLaendercodeBfs()))
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);
        assertFalse(EinnahmenKostenMappingUtil.isQuellenBesteuert(personInAusbildung));
    }

    @Test
    void isQuellenbesteuertIfNotAusweisC() {
        final var personInAusbildung = new PersonInAusbildung()
            .setNationalitaet(new Land().setLaendercodeBfs(WellKnownLand.IRN.getLaendercodeBfs()))
            .setNiederlassungsstatus(Niederlassungsstatus.ASYLSUCHEND_N);
        assertTrue(EinnahmenKostenMappingUtil.isQuellenBesteuert(personInAusbildung));
    }

    @CsvSource(
        {
            // isQuellenbesteuert, nettoerwerbseinkommen, expected, text
            "true,                               100_000,        0, has 0 steuern if quellenbesteuert",
            "false,                              100_000,   10_000, has 10'000 steuern if einkommen is 100'000",
            "false,                               10_000,        0, has 0 steuern if einkommen is 10'000",
        }
    )
    @ParameterizedTest(name = "{3}")
    void checkIf(
        final boolean isQuellenbesteuert,
        final Integer nettoerwerbseinkommen,
        final int expected,
        final String assertText
    ) {
        final var steuern = EinnahmenKostenMappingUtil
            .calculateSteuern(einnahmenKosten.setNettoerwerbseinkommen(nettoerwerbseinkommen), isQuellenbesteuert);
        assertThat(assertText, steuern, is(expected));
    }
}
