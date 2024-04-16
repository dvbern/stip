package ch.dvbern.stip.api.zuordnung.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.type.BenutzerTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.repo.ZuordnungRepository;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ZuordnungServiceTest {
    private final UUID sachbearbeiterZuordnungStammdatenId = UUID.randomUUID();
    private final UUID sachbearbeiterId = UUID.randomUUID();
    private final UUID fallId = UUID.randomUUID();
    private final List<Zuordnung> zuordnungen = new ArrayList<>();

    private ZuordnungService zuordnungService;

    @BeforeEach
    void setup() {
        final var query = Mockito.mock(PanacheQuery.class);
        Mockito.when(query.page(Mockito.any())).thenReturn(query);
        final var sb = (Benutzer) new Benutzer()
            .setBenutzerTyp(BenutzerTyp.SACHBEARBEITER)
            .setVorname("John")
            .setNachname("Doe")
            .setId(sachbearbeiterId);
        final var szs = (SachbearbeiterZuordnungStammdaten) new SachbearbeiterZuordnungStammdaten()
            .setBuchstabenDe("A-Z")
            .setBuchstabenFr("A-Z")
            .setBenutzer(sb)
            .setId(sachbearbeiterZuordnungStammdatenId);

        Mockito.when(query.stream()).thenReturn(Stream.of(szs));

        final var szsRepo = Mockito.mock(SachbearbeiterZuordnungStammdatenRepository.class);
        Mockito.when(szsRepo.findAll()).thenReturn(query);

        final var zuordnungRepo = Mockito.mock(ZuordnungRepository.class);
        Mockito.when(zuordnungRepo.findAllWithType(ZuordnungType.AUTOMATIC)).thenReturn(Stream.of());
        Mockito.doNothing().when(zuordnungRepo).deleteByFallIds(Mockito.any());
        Mockito.doAnswer((Answer<Void>) invocation -> {
                zuordnungen.addAll(invocation.getArgument(0));
                return null;
            }
        ).when(zuordnungRepo).persist(Mockito.anyIterable());

        final var pia = (PersonInAusbildung) new PersonInAusbildung()
            .setKorrespondenzSprache(Sprache.DEUTSCH)
            .setNachname("Alfred");

        final var fall = (Fall) new Fall().setId(fallId);
        final var gesuch = new Gesuch().setGesuchTranchen(
            List.of(
                new GesuchTranche().setGesuchFormular(
                    new GesuchFormular().setPersonInAusbildung(pia)
                )
            )
        );

        gesuch.setFall(fall);
        fall.setGesuch(Set.of(gesuch));

        final var gesuchsRepo = Mockito.mock(GesuchRepository.class);
        Mockito.when(gesuchsRepo.findAllNewestWithPia()).thenReturn(Stream.of(gesuch));

        zuordnungService = new ZuordnungService(szsRepo, zuordnungRepo, gesuchsRepo);
    }

    @Test
    void testUpdateZuordnungOnFall() {
        zuordnungService.updateZuordnungOnFall();
        assertThat(zuordnungen.size(), is(1));
        assertThat(zuordnungen.get(0).getFall().getId(), is(fallId));
    }
}
