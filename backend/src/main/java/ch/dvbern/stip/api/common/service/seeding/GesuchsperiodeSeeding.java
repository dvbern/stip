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

package ch.dvbern.stip.api.common.service.seeding;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class GesuchsperiodeSeeding extends Seeder {
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchsjahrRepository gesuchsjahrRepository;
    private final ConfigService configService;

    @Override
    protected void doSeed() {
        LOG.info("Seeding Gesuchsperiode");
        Gesuchsjahr newJahr = null;
        if (gesuchsperiodeRepository.count() == 0) {
            newJahr = getJahrForSeeding();
        }

        if (newJahr == null) {
            return;
        }

        int currentYear = newJahr.getTechnischesJahr();

        final var newPerioden = List.of(
            getPeriodeForSeeding(
                "Herbst",
                "Automne",
                newJahr,
                LocalDate.of(currentYear, 7, 1),
                LocalDate.of(currentYear + 1, 6, 30),
                LocalDate.of(currentYear, 7, 15),
                LocalDate.of(currentYear + 1, 3, 31),
                LocalDate.of(currentYear, 12, 31),
                LocalDate.of(currentYear + 1, 3, 31)
            ),
            getPeriodeForSeeding(
                "Frühling",
                "Printemps",
                newJahr,
                LocalDate.of(currentYear, 1, 1),
                LocalDate.of(currentYear, 12, 31),
                LocalDate.of(currentYear, 1, 15),
                LocalDate.of(currentYear, 9, 30),
                LocalDate.of(currentYear, 6, 30),
                LocalDate.of(currentYear, 9, 30)
            )
        );

        gesuchsjahrRepository.persistAndFlush(newJahr);
        gesuchsperiodeRepository.persist(newPerioden);
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    Gesuchsjahr getJahrForSeeding() {
        int currentYear = LocalDate.now().getYear();
        String yearAsString = String.valueOf(currentYear);
        String yearSuffix = yearAsString.substring(yearAsString.length() - 2, yearAsString.length());
        return new Gesuchsjahr()
            .setBezeichnungDe("Gesuchsjahr " + yearSuffix)
            .setBezeichnungFr("Année de la demande " + yearSuffix)
            .setTechnischesJahr(currentYear)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
    }

    Gesuchsperiode getPeriodeForSeeding(
        final String prefixDe,
        final String prefixFr,
        final Gesuchsjahr jahr,
        final LocalDate from,
        final LocalDate to,
        final LocalDate aufschaltterminStart,
        final LocalDate aufschaltterminStopp,
        final LocalDate einreichefristNormal,
        final LocalDate einreichefristReduziert
    ) {
        String jahrAsString = String.valueOf(jahr.getTechnischesJahr());

        return new Gesuchsperiode()
            .setBezeichnungDe(prefixDe + ' ' + jahrAsString)
            .setBezeichnungFr(prefixFr + ' ' + jahrAsString)
            .setFiskaljahr(jahrAsString)
            .setGesuchsjahr(jahr)
            .setGesuchsperiodeStart(from)
            .setGesuchsperiodeStopp(to)
            .setAufschaltterminStart(aufschaltterminStart)
            .setAufschaltterminStopp(aufschaltterminStopp)
            .setEinreichefristNormal(einreichefristNormal)
            .setEinreichefristReduziert(einreichefristReduziert)
            .setAusbKostenSekII(2000)
            .setAusbKostenTertiaer(3000)
            .setFreibetragVermoegen(0)
            .setFreibetragErwerbseinkommen(6000)
            .setEinkommensfreibetrag(6000)
            .setElternbeteiligungssatz(50)
            .setFreibetragVermoegen(30000)
            .setVermogenSatzAngerechnet(15)
            .setIntegrationszulage(2400)
            .setLimiteEkFreibetragIntegrationszulage(13200)
            .setStipLimiteMinimalstipendium(500)
            .setPerson1(12072)
            .setPersonen2(18468)
            .setPersonen3(22452)
            .setPersonen4(25836)
            .setPersonen5(29220)
            .setPersonen6(31668)
            .setPersonen7(34116)
            .setProWeiterePerson(2448)
            .setKinder0018(1400)
            .setJugendlicheErwachsene1925(4600)
            .setErwachsene2699(5400)
            .setWohnkostenFam1pers(13536)
            .setWohnkostenFam2pers(16260)
            .setWohnkostenFam3pers(16260)
            .setWohnkostenFam4pers(19932)
            .setWohnkostenFam5pluspers(25260)
            .setWohnkostenPersoenlich1pers(10009)
            .setWohnkostenPersoenlich2pers(13536)
            .setWohnkostenPersoenlich3pers(16260)
            .setWohnkostenPersoenlich4pers(19932)
            .setWohnkostenPersoenlich5pluspers(25260)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .setPreisProMahlzeit(10)
            .setMaxSaeule3a(7056)
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38)
            .setVermoegensanteilInProzent(15)
            .setLimiteAlterAntragsstellerHalbierungElternbeitrag(25)
            .setReduzierungDesGrundbedarfs(2838)
            .setZweiterAuszahlungsterminMonat(6)
            .setZweiterAuszahlungsterminTag(1);
    }
}
