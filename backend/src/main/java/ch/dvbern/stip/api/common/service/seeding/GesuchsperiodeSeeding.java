package ch.dvbern.stip.api.common.service.seeding;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class
GesuchsperiodeSeeding extends Seeder{
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchsjahrRepository gesuchsjahrRepository;
    private final ConfigService configService;

    @Override
    @Startup
    public void startup() {
        seed();
    }

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
                LocalDate.of(currentYear, 8, 1),
                LocalDate.of(currentYear + 1, 7, 31)
            ),
            getPeriodeForSeeding(
                "Frühling",
                "Printemps",
                newJahr,
                LocalDate.of(currentYear, 2, 1),
                LocalDate.of(currentYear + 1, 1, 31)
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
        String yearSuffix = yearAsString.substring(yearAsString.length() -2 , yearAsString.length());
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
        final LocalDate to
    ) {
        String jahrAsString = String.valueOf(jahr.getTechnischesJahr());

        return new Gesuchsperiode()
            .setBezeichnungDe(prefixDe + ' ' + jahrAsString)
            .setBezeichnungFr(prefixFr + ' ' + jahrAsString)
            .setFiskaljahr(jahrAsString)
            .setGesuchsjahr(jahr)
            .setGesuchsperiodeStart(from)
            .setGesuchsperiodeStopp(to)
            .setAufschaltterminStart(from)
            .setAufschaltterminStopp(to)
            .setEinreichefristNormal(to.minusMonths(7))
            .setEinreichefristReduziert(to.minusMonths(4))
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
