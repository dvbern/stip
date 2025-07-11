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

package ch.dvbern.stip.api.zuordnung.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.util.TestRollen;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
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
import static org.hamcrest.Matchers.not;

class ZuordnungServiceTest {
    private final UUID sachbearbeiterZuordnungStammdatenId = UUID.randomUUID();
    private final UUID sachbearbeiterId = UUID.randomUUID();
    private final UUID fallId = UUID.randomUUID();
    private Fall fall;
    private List<Zuordnung> zuordnungen = new ArrayList<>();

    private ZuordnungService zuordnungService;

    @BeforeEach
    void setup() {
        final var query = Mockito.mock(PanacheQuery.class);
        Mockito.when(query.page(Mockito.any())).thenReturn(query);
        final var sb = (Sachbearbeiter) new Sachbearbeiter()
            .setTelefonnummer("")
            .setFunktionFr("")
            .setFunktionDe("")
            .setEmail("")
            .setVorname("John")
            .setNachname("Doe")
            .setRollen(TestRollen.getComposite(TestRollen.SACHBEARBEITER))
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
        Mockito.when(zuordnungRepo.findAllWithType(ZuordnungType.AUTOMATIC))
            .thenReturn(
                zuordnungen.stream().filter(x -> x.getZuordnungType() == ZuordnungType.AUTOMATIC)
            );
        Mockito.doAnswer((Answer<Void>) invocation -> {
            final var toDrop = (Set<UUID>) invocation.getArgument(0);
            zuordnungen = zuordnungen.stream().filter(x -> toDrop.contains(x.getFall().getId())).toList();
            return null;
        }).when(zuordnungRepo).deleteByFallIds(Mockito.any());
        Mockito.doAnswer((Answer<Void>) invocation -> {
            zuordnungen.addAll(invocation.getArgument(0));
            return null;
        }
        ).when(zuordnungRepo).persist(Mockito.anyIterable());

        final var sachbearbeiterRepository = Mockito.mock(SachbearbeiterRepository.class);
        Mockito.when(sachbearbeiterRepository.findByRolle(Mockito.any()))
            .thenReturn(
                Stream.of((Sachbearbeiter) new Sachbearbeiter().setRollen(TestRollen.getComposite(TestRollen.ADMIN)))
            );

        final var pia = (PersonInAusbildung) new PersonInAusbildung()
            .setKorrespondenzSprache(Sprache.DEUTSCH)
            .setNachname("Alfred");

        fall = (Fall) new Fall().setId(fallId);

        final var ausbildung = new Ausbildung().setFall(fall);

        final var gesuch = new Gesuch().setGesuchTranchen(
            List.of(
                new GesuchTranche().setGesuchFormular(
                    new GesuchFormular().setPersonInAusbildung(pia)
                )
            )
        );

        gesuch.setAusbildung(ausbildung);

        fall.setAusbildungs(Set.of(ausbildung));
        ausbildung.setGesuchs(List.of(gesuch));

        final var gesuchsRepo = Mockito.mock(GesuchRepository.class);
        Mockito.when(gesuchsRepo.findAllNewestWithPia()).thenReturn(Stream.of(gesuch));

        zuordnungService = new ZuordnungService(szsRepo, zuordnungRepo, gesuchsRepo, sachbearbeiterRepository);
    }

    @Test
    void testCreateZuordnungOnFall() {
        zuordnungService.updateZuordnungOnAllFaelle();
        assertThat(zuordnungen.size(), is(1));
        assertThat(zuordnungen.get(0).getFall().getId(), is(fallId));
    }

    @Test
    void testUpdateZuordnungOnFall() {
        final var oldSbId = UUID.randomUUID();

        zuordnungen.add(
            (Zuordnung) new Zuordnung()
                .setZuordnungType(ZuordnungType.AUTOMATIC)
                .setFall(fall)
                .setSachbearbeiter(
                    (Sachbearbeiter) new Sachbearbeiter()
                        .setRollen(TestRollen.getComposite(TestRollen.SACHBEARBEITER))
                        .setId(oldSbId)
                )
                .setId(UUID.randomUUID())
        );

        zuordnungService.updateZuordnungOnAllFaelle();
        assertThat(oldSbId, is(not(sachbearbeiterId)));
        assertThat(zuordnungen.size(), is(1));
        assertThat(zuordnungen.get(0).getSachbearbeiter().getId(), is(sachbearbeiterId));

    }
}
