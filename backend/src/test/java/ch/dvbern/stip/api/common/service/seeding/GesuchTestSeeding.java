package ch.dvbern.stip.api.common.service.seeding;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.util.TestConstants;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class GesuchTestSeeding extends Seeder {
    private final GesuchsjahrRepository gesuchsjahrRepository;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;

    @Override
    @Startup
    public void startup() {
        seed();
    }

    @Override
    protected void doSeed() {
        if (gesuchsperiodeRepository.count() == 0) {
            LOG.info("Seeding Gesuchsjahr and Gesuchsperiode");

            final var gesuchsjahr = getGesuchsjahr();
            gesuchsjahrRepository.persistAndFlush(gesuchsjahr);
            TestConstants.TEST_GESUCHSJAHR_ID = gesuchsjahr.getId();

            final var gesuchsperiode = getGesuchsperiode(gesuchsjahr);
            gesuchsperiodeRepository.persistAndFlush(gesuchsperiode);
            TestConstants.TEST_GESUCHSPERIODE_ID = gesuchsperiode.getId();
        }
    }

    @Override
    protected List<String> getProfiles() {
        return List.of("test");
    }

    private static Gesuchsjahr getGesuchsjahr() {
        final var jahr = LocalDate.now().getYear();
        return new Gesuchsjahr()
            .setBezeichnungDe("Gesuchsjahr " + (jahr - 2000))
            .setBezeichnungFr("Année de la demande " + (jahr - 2000))
            .setTechnischesJahr(jahr)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
    }

    private static Gesuchsperiode getGesuchsperiode(final Gesuchsjahr gesuchsjahr) {
        final var jahr = LocalDate.now().getYear();
        final var start = getGesuchsperiodeStart();
        final var stopp = getGesuchsperiodeStopp();
        TestConstants.GUELTIGKEIT_PERIODE_23_24 = new DateRange(start, stopp);

        return new Gesuchsperiode()
            .setGesuchsjahr(gesuchsjahr)
            .setBezeichnungDe("Test DE")
            .setBezeichnungFr("Test FR")
            .setFiskaljahr(String.valueOf(jahr))
            .setGesuchsperiodeStart(start)
            .setGesuchsperiodeStopp(stopp)
            .setAufschaltterminStart(LocalDate.now().with(firstDayOfYear()))
            .setAufschaltterminStopp(LocalDate.now().with(lastDayOfYear()))
            .setEinreichefristNormal(LocalDate.now())
            .setEinreichefristReduziert(LocalDate.now().plusMonths(3))
            .setAusbKostenSekII(1)
            .setAusbKostenTertiaer(1)
            .setFreibetragVermoegen(1)
            .setFreibetragErwerbseinkommen(1)
            .setEinkommensfreibetrag(1)
            .setElternbeteiligungssatz(1)
            .setVermogenSatzAngerechnet(1)
            .setIntegrationszulage(1)
            .setLimiteEkFreibetragIntegrationszulage(1)
            .setStipLimiteMinimalstipendium(1)
            .setPerson1(1)
            .setPersonen2(1)
            .setPersonen3(1)
            .setPersonen4(1)
            .setPersonen5(1)
            .setPersonen6(1)
            .setPersonen7(1)
            .setProWeiterePerson(1)
            .setKinder0018(1)
            .setJugendlicheErwachsene1925(1)
            .setErwachsene2699(1)
            .setWohnkostenFam1pers(1)
            .setWohnkostenFam2pers(1)
            .setWohnkostenFam3pers(1)
            .setWohnkostenFam4pers(1)
            .setWohnkostenFam5pluspers(1)
            .setWohnkostenPersoenlich1pers(1)
            .setWohnkostenPersoenlich2pers(1)
            .setWohnkostenPersoenlich3pers(1)
            .setWohnkostenPersoenlich4pers(1)
            .setWohnkostenPersoenlich5pluspers(1)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .setPreisProMahlzeit(1)
            .setMaxSaeule3a(1)
            .setAnzahlWochenLehre(1)
            .setAnzahlWochenSchule(1)
            .setVermoegensanteilInProzent(15)
            .setReduzierungDesGrundbedarfs(2754)
            .setZweiterAuszahlungsterminMonat(6)
            .setZweiterAuszahlungsterminTag(1);
    }

    private static LocalDate getGesuchsperiodeStart() {
        var jahr = LocalDate.now().getYear();
        if (LocalDate.now().isBefore(LocalDate.of(jahr, 7, 1))) {
            jahr -= 1;
        }

        return LocalDate.of(jahr, 7, 1);
    }

    private static LocalDate getGesuchsperiodeStopp() {
        var jahr = LocalDate.now().getYear();
        if (LocalDate.now().isAfter(LocalDate.of(jahr, 6, 30))) {
            jahr += 1;
        }

        return LocalDate.of(jahr, 6, 30);
    }
}
