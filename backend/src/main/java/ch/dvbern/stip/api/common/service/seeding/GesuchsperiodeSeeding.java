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
import java.time.Year;
import java.util.ArrayList;
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
import org.apache.commons.lang3.tuple.ImmutablePair;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class GesuchsperiodeSeeding extends Seeder {
    private static final LocalDate HALF_OF_YEAR = LocalDate.of(Year.now().getValue(), 7, 1);
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchsjahrRepository gesuchsjahrRepository;
    private final ConfigService configService;

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    protected void seed() {
        int currentYear = LocalDate.now().getYear();
        var isCurrentDayPastHalfOfYear = LocalDate.now().isAfter(HALF_OF_YEAR);

        var autumnOfPastYearStatus = GueltigkeitStatus.PUBLIZIERT;

        if (isCurrentDayPastHalfOfYear) {
            autumnOfPastYearStatus = GueltigkeitStatus.ARCHIVIERT;
        }

        LOG.info("Seeding Gesuchsperiode and Jahr");
        final var yearsToSeed = List.of(
            ImmutablePair.of(
                currentYear - 1,
                List.of(
                    ImmutablePair.of(Season.SPRING, GueltigkeitStatus.ARCHIVIERT),
                    ImmutablePair.of(Season.FALL, autumnOfPastYearStatus)
                )
            ),
            ImmutablePair.of(
                currentYear,
                List.of(
                    ImmutablePair.of(Season.SPRING, GueltigkeitStatus.PUBLIZIERT),
                    ImmutablePair.of(Season.FALL, GueltigkeitStatus.PUBLIZIERT)
                )
            )
        );

        for (final var toSeed : yearsToSeed) {
            final var yearToSeed = toSeed.getLeft();
            var gesuchsjahr = gesuchsjahrRepository.find("technischesJahr", yearToSeed).firstResult();
            if (gesuchsjahr != null) {
                continue;
            }

            final var newPerioden = new ArrayList<Gesuchsperiode>();
            gesuchsjahr = getJahrForSeeding(yearToSeed);
            for (final var periodeToSeed : toSeed.getRight()) {
                newPerioden.add(switch (periodeToSeed.getLeft()) {
                    case SPRING -> getPeriodeForSeeding(
                        "Frühling",
                        "Printemps",
                        gesuchsjahr,
                        periodeToSeed.getRight(),
                        LocalDate.of(yearToSeed, 1, 1),
                        LocalDate.of(yearToSeed, 12, 31),
                        LocalDate.of(yearToSeed, 1, 15),
                        LocalDate.of(yearToSeed, 6, 30),
                        LocalDate.of(yearToSeed, 9, 30),
                        LocalDate.of(yearToSeed, 12, 31)
                    );
                    case FALL -> getPeriodeForSeeding(
                        "Herbst",
                        "Automne",
                        gesuchsjahr,
                        periodeToSeed.getRight(),
                        LocalDate.of(yearToSeed, 7, 1),
                        LocalDate.of(yearToSeed + 1, 6, 30),
                        LocalDate.of(yearToSeed, 7, 15),
                        LocalDate.of(yearToSeed, 12, 31),
                        LocalDate.of(yearToSeed + 1, 3, 31),
                        LocalDate.of(yearToSeed, 12, 31)
                    );
                });
            }

            gesuchsjahrRepository.persistAndFlush(gesuchsjahr);
            gesuchsperiodeRepository.persist(newPerioden);
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    Gesuchsjahr getJahrForSeeding(final int technischesJahr) {
        // Technically this limits us to the 2nd millennium, but I hope this won't be used in the year 3000+
        String yearSuffix = String.valueOf(technischesJahr - 2000);
        return new Gesuchsjahr()
            .setBezeichnungDe("Gesuchsjahr " + yearSuffix)
            .setBezeichnungFr("Année de la demande " + yearSuffix)
            .setTechnischesJahr(technischesJahr)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
    }

    Gesuchsperiode getPeriodeForSeeding(
        final String prefixDe,
        final String prefixFr,
        final Gesuchsjahr jahr,
        final GueltigkeitStatus gueltigkeitStatus,
        final LocalDate from,
        final LocalDate to,
        final LocalDate aufschaltterminStart,
        final LocalDate einreichefristNormal,
        final LocalDate einreichefristReduziert,
        final LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung
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
            .setEinreichefristNormal(einreichefristNormal)
            .setEinreichefristReduziert(einreichefristReduziert)
            .setAusbKostenSekII(2000)
            .setAusbKostenTertiaer(3000)
            .setFreibetragVermoegen(30000)
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
            .setKinder0017(1400)
            .setJugendlicheErwachsene1824(4600)
            .setErwachsene2599(5400)
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
            .setGueltigkeitStatus(gueltigkeitStatus)
            .setPreisProMahlzeit(10)
            .setMaxSaeule3a(getMaxSaeule3a(jahr.getTechnischesJahr()))
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38)
            .setVermoegensanteilInProzent(15)
            .setLimiteAlterAntragsstellerHalbierungElternbeitrag(25)
            .setReduzierungDesGrundbedarfs(2838)
            .setZweiterAuszahlungsterminMonat(6)
            .setZweiterAuszahlungsterminTag(1)
            .setFristNachreichenDokumente(30)
            .setStichtagVolljaehrigkeitMedizinischeGrundversorgung(stichtagVolljaehrigkeitMedizinischeGrundversorgung);
    }

    private int getMaxSaeule3a(int year) {
        if (year <= 2025) {
            return 7056;
        }
        return 7258;
    }

    private enum Season {
        SPRING,
        FALL
    }
}
