package ch.dvbern.stip.api.common.service.seeding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.bildungskategorie.repo.BildungskategorieRepository;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Seeds the known testcases
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TestcaseSeeding extends Seeder {
    private final ConfigService configService;
    private final ObjectMapper objectMapper;
    private final FallRepository fallRepository;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final BenutzerRepository benutzerRepository;
    private final BildungskategorieRepository bildungskategorieRepository;
    private final GesuchRepository gesuchRepository;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    protected void doSeed() {
        // Find the JSONs to import
        final var testcasesJson = getJsons();
        final var gesuchperiodeToAttach =
            gesuchsperiodeRepository.findAll(Sort.by("gesuchsperiodeStart").descending()).firstResult();
        final var possibleAusbildungsgaenge = ausbildungsgangRepository.findAll().stream().toList();

        final int year = LocalDate.now().getYear();
        int index = 0;
        for (final var testcaseJson : testcasesJson) {
            final var testcase = testcaseJson.getLeft();
            final var json = testcaseJson.getRight();
            GesuchDto dto;
            try {
                // Deserialize to GesuchUpdateDto
                dto = objectMapper.readValue(json, GesuchDto.class);
            } catch (JsonProcessingException e) {
                LOG.error(String.format("Failed to deserialize testcase %s", testcase), e);
                continue;
            }

            // Find and map to already seeded values
            final var ausbildung = dto.getGesuchTrancheToWorkWith().getGesuchFormular().getAusbildung();
            ausbildung.getAusbildungsgang()
                .setId(getOrCreateAusbildungsgaenge(possibleAusbildungsgaenge, ausbildung.getAusbildungsgang()));

            // Map to entity and correct the mapping
            final var tranche = gesuchTrancheMapper.toEntity(dto.getGesuchTrancheToWorkWith());
            tranche.setTyp(GesuchTrancheTyp.TRANCHE);
            tranche.setStatus(GesuchTrancheStatus.AKZEPTIERT);
            tranche.setGueltigkeit(new DateRange(
                gesuchperiodeToAttach.getGesuchsperiodeStart(),
                gesuchperiodeToAttach.getGesuchsperiodeStopp()
            ));

            correctAuszahlungAdresse(tranche.getGesuchFormular());

            // Clear IDs
            clearIds(tranche);

            // Create Gesuchsteller, Fall and Gesuch
            final var fall = createFall(String.format("BE.F.T%04d", index), createGesuchsteller(testcase));
            final var gesuch = createGesuch(
                gesuchperiodeToAttach,
                String.format("BE.%s.G.T%04d", year, index),
                tranche
            );

            gesuch.setFall(fall);
            fall.setGesuch(Set.of(gesuch));
            tranche.setGesuch(gesuch);

            // Persist to database
            fallRepository.persist(fall);
            gesuchRepository.persist(gesuch);

            index++;
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedTestcasesOnProfile();
    }

    UUID getOrCreateAusbildungsgaenge(
        final List<Ausbildungsgang> possibleAusbildungsgaenge,
        final AusbildungsgangDto dto
    ) {
        return possibleAusbildungsgaenge.stream()
            .filter(possibleAusbildungsgang ->
                possibleAusbildungsgang.getBildungskategorie().getBfs() == dto.getBildungskategorie().getBfs() &&
                    possibleAusbildungsgang.getBezeichnungDe().equals(dto.getBezeichnungDe()))
            .findFirst()
            .orElseGet(() -> {
                final var bildungskategorieToCreate = new Bildungskategorie()
                    .setBfs(dto.getBildungskategorie().getBfs())
                    .setBezeichnungFr(dto.getBildungskategorie().getBezeichnungFr())
                    .setBezeichnungDe(dto.getBildungskategorie().getBezeichnungDe());

                bildungskategorieRepository.persist(bildungskategorieToCreate);

                final var toCreate = new Ausbildungsgang()
                    .setAusbildungsstaette(((Ausbildungsstaette) new Ausbildungsstaette()
                        .setNameDe(dto.getBildungskategorie().getBezeichnungDe())
                        .setNameFr(dto.getBildungskategorie().getBezeichnungFr())
                        .setId(dto.getAusbildungsstaetteId()))
                    )
                    .setBezeichnungFr(dto.getBezeichnungFr())
                    .setBezeichnungDe(dto.getBezeichnungDe())
                    .setBildungskategorie(bildungskategorieToCreate);

                ausbildungsgangRepository.persist(toCreate);
                return toCreate;
            })
            .getId();
    }

    List<Pair<String, String>> getJsons() {
        final var jsonStrings = new ArrayList<Pair<String, String>>();

        final var classLoader = getClass().getClassLoader();
        for (final var toLoad : configService.getTestcasesToSeed()) {
            try (final var is = classLoader.getResourceAsStream(String.format("/seeding/gesuch/%s.json", toLoad))) {
                if (is == null) {
                    LOG.warn("Tried to load testcase {}.json but cannot find it", toLoad);
                    continue;
                }

                final var reader = new BufferedReader(new InputStreamReader(is));
                final var read = reader.lines().collect(Collectors.joining());
                jsonStrings.add(ImmutablePair.of(toLoad, read));
            } catch (IOException e) {
                LOG.error(String.format("Failed to load testcase %s.json", toLoad), e);
            }
        }

        return jsonStrings;
    }

    void correctAuszahlungAdresse(final GesuchFormular formular) {
        final Function<ElternTyp, Eltern> getElternteilByTyp = elternTyp -> {
            if (formular.getElterns() == null) {
                return null;
            }

            return formular.getElterns()
                .stream()
                .filter(elternteil -> elternteil.getElternTyp() == elternTyp)
                .findFirst()
                .orElse(null);
        };

        final var piaAdresse = formular.getPersonInAusbildung().getAdresse();
        final var mutterAdresse = getElternteilByTyp.apply(ElternTyp.MUTTER);
        final var vaterAdresse = getElternteilByTyp.apply(ElternTyp.VATER);

        final var auszahlungAdresse = switch (formular.getAuszahlung().getKontoinhaber()) {
            case GESUCHSTELLER -> piaAdresse;
            case MUTTER -> mutterAdresse.getAdresse();
            case VATER -> vaterAdresse.getAdresse();
            default -> formular.getAuszahlung().getAdresse();
        };

        formular.getAuszahlung().setAdresse(auszahlungAdresse);
    }

    void clearIds(final GesuchTranche tranche) {
        tranche.setId(null);
        final var formular = tranche.getGesuchFormular();
        formular.setId(null);
        final Consumer<AbstractEntity> nullId = entity -> {
            if (entity == null) {
                return;
            }

            entity.setId(null);
        };

        nullId.accept(formular.getPersonInAusbildung());
        formular.getPersonInAusbildung().getAdresse().setId(null);
        nullId.accept(formular.getAusbildung());
        nullId.accept(formular.getFamiliensituation());
        nullId.accept(formular.getPartner());

        if (formular.getPartner() != null) {
            formular.getPartner().getAdresse().setId(null);
        }

        nullId.accept(formular.getAuszahlung());
        formular.getAuszahlung().getAdresse().setId(null);
        nullId.accept(formular.getEinnahmenKosten().setId(null));

        if (formular.getLebenslaufItems() != null) {
            formular.getLebenslaufItems().forEach(lebenslaufItem -> lebenslaufItem.setId(null));
        }

        if (formular.getGeschwisters() != null) {
            formular.getGeschwisters().forEach(geschwister -> geschwister.setId(null));
        }

        if (formular.getElterns() != null) {
            formular.getElterns().forEach(elternteil -> {
                elternteil.setId(null);
                elternteil.getAdresse().setId(null);
            });
        }

        if (formular.getKinds() != null) {
            formular.getKinds().forEach(kind -> kind.setId(null));
        }

        if (formular.getSteuerdaten() != null) {
            formular.getSteuerdaten().forEach(steuerdaten -> steuerdaten.setId(null));
        }
    }

    Benutzer createGesuchsteller(final String testcase) {
        final var benutzer = new Benutzer()
            .setVorname(testcase)
            .setNachname(testcase)
            .setBenutzerStatus(BenutzerStatus.AKTIV)
            .setBenutzereinstellungen(new Benutzereinstellungen());

        benutzerRepository.persist(benutzer);
        return benutzer;
    }

    Fall createFall(final String fallNummer, final Benutzer gesuchsteller) {
        return new Fall()
            .setFallNummer(fallNummer)
            .setGesuchsteller(gesuchsteller);
    }

    Gesuch createGesuch(final Gesuchsperiode gesuchsperiode, final String gesuchNummer, final GesuchTranche tranche) {
        return new Gesuch()
            .setGesuchsperiode(gesuchsperiode)
            .setGesuchStatus(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .setGesuchNummer(gesuchNummer)
            .setGesuchTranchen(List.of(tranche));
    }
}