package ch.dvbern.stip.api.common.service.seeding;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class GesuchsperiodeSeeding extends Seeder{
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchsjahrRepository gesuchsjahrRepository;

    @Override
    @Startup
    void startup() {
        seed();
    }

    @Override
    void doSeed() {
        LOG.info("Seeding Gesuchsperiode");
        Gesuchsjahr newJahr = null;
        if (gesuchsperiodeRepository.count() == 0) {
            newJahr = getJahrForSeeding();
        }

        if (newJahr == null) {
            return;
        }

        final var newPerioden = List.of(
            getPeriodeForSeeding(
                newJahr,
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2023, 7, 31)
            ),
            getPeriodeForSeeding(
                newJahr,
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2024, 6, 30)
            )
        );

        gesuchsjahrRepository.persistAndFlush(newJahr);
        gesuchsperiodeRepository.persist(newPerioden);
    }

    Gesuchsjahr getJahrForSeeding() {
        return new Gesuchsjahr()
            .setBezeichnungDe("Gesuchsjahr 24")
            .setBezeichnungFr("Année de la demande 24")
            .setTechnischesJahr(2024)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
    }

    Gesuchsperiode getPeriodeForSeeding(
        final Gesuchsjahr jahr,
        final LocalDate from,
        final LocalDate to
    ) {
        return new Gesuchsperiode()
            .setBezeichnungDe("Frühling 2023")
            .setBezeichnungFr("Printemps 2023")
            .setFiskaljahr("2023")
            .setGesuchsjahr(jahr)
            .setGesuchsperiodeStart(from)
            .setGesuchsperiodeStopp(to)
            .setAufschaltterminStart(from)
            .setAufschaltterminStopp(to)
            .setEinreichefristNormal(from.minusMonths(1))
            .setEinreichefristReduziert(to.with(lastDayOfYear()))
            .setAusbKostenSekII(2000)
            .setAusbKostenTertiaer(3000)
            .setFreibetragVermoegen(0)
            .setFreibetragErwerbseinkommen(6000)
            .setEinkommensfreibetrag(6000)
            .setElternbeteiligungssatz(50)
            .setFreibetragVermoegen(30000)
            .setVermogenSatzAngerechnet(15)
            .setIntegrationszulage(2400)
            .setLimiteEkFreibetragIntegrationszulag(13200)
            .setStipLimiteMinimalstipendium(500)
            .setPerson1(11724)
            .setPersonen2(17940)
            .setPersonen3(21816)
            .setPersonen4(25080)
            .setPersonen5(28368)
            .setPersonen6(31656)
            .setPersonen7(34944)
            .setProWeiterePerson(3288)
            .setKinder0018(1400)
            .setJugendlicheErwachsene1925(4600)
            .setErwachsene2699(5400)
            .setWohnkostenFam1pers(10009)
            .setWohnkostenFam2pers(13536)
            .setWohnkostenFam3pers(16260)
            .setWohnkostenFam4pers(19932)
            .setWohnkostenFam5pluspers(25260)
            .setWohnkostenPersoenlich1pers(13536)
            .setWohnkostenPersoenlich2pers(16260)
            .setWohnkostenPersoenlich3pers(16260)
            .setWohnkostenPersoenlich4pers(19932)
            .setWohnkostenPersoenlich5pluspers(25260)
            .setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .setPreisProMahlzeit(7)
            .setMaxSaeule3a(7000)
            .setAnzahlWochenLehre(42)
            .setAnzahlWochenSchule(37);
    }
}
