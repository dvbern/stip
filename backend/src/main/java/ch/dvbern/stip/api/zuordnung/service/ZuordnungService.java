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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.BuchstabenRangeUtil;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.repo.ZuordnungRepository;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class ZuordnungService {
    private final SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository;
    private final ZuordnungRepository zuordnungRepository;
    private final GesuchRepository gesuchRepository;
    private final SachbearbeiterRepository sachbearbeiterRepository;

    public void updateZuordnungOnAllFaelle() {
        final var newestWithPia = gesuchRepository.findAllNewestWithPia().toList();

        // Filter out duplicate Faelle
        // This is okay because we only care about the newest Tranche, and findAllNewestWithPia orders them accordingly
        final var seen = new HashSet<UUID>();
        final var fitered =
            newestWithPia.stream().filter(newest -> seen.add(newest.getAusbildung().getFall().getId())).toList();
        update(fitered);
    }

    public void updateZuordnungOnGesuch(final Gesuch gesuch) {
        update(List.of(gesuch));
    }

    private void update(final List<Gesuch> newestWithPia) {
        // Load all StammdatenSachbearbeiterZuordnungen from DB
        final var stammdaten = sachbearbeiterZuordnungStammdatenRepository.findAll().stream().toList();
        final var zuordnungen = zuordnungRepository
            .findAllWithType(ZuordnungType.AUTOMATIC)
            .collect(Collectors.toMap(zuordnung -> zuordnung.getFall().getId(), zuordnung -> zuordnung));

        final var newZuordnungen = new ArrayList<Zuordnung>();

        // Load the first admin to assign if no other SB is found
        final var admin = sachbearbeiterRepository
            .findByRolle(OidcConstants.ROLE_ADMIN)
            .findFirst()
            .orElseThrow(NotFoundException::new);

        // Stream through all Gesuche with a PiA attached
        newestWithPia.forEach(newest -> {
            // Find the SB to assign by PiA's last name and find potentially existing Zuordnung
            var sbToAssign = findSbToAssign(stammdaten, getPia(newest));
            if (sbToAssign.isEmpty()) {
                sbToAssign = Optional.of(admin);
            }

            var zuordnung = zuordnungen.remove(newest.getAusbildung().getFall().getId());
            if (zuordnung == null) {
                // If none exists, create a new one and add it to the list to persist
                zuordnung = new Zuordnung()
                    .setZuordnungType(ZuordnungType.AUTOMATIC)
                    .setFall(newest.getAusbildung().getFall())
                    .setSachbearbeiter(sbToAssign.get());

                newZuordnungen.add(zuordnung);
            } else {
                // If one exists, update it
                zuordnung
                    .setSachbearbeiter(sbToAssign.get())
                    .setZuordnungType(ZuordnungType.AUTOMATIC);
            }
        });

        // Persist the new Zuordnungen
        zuordnungRepository.persist(newZuordnungen);
    }

    private Optional<Sachbearbeiter> findSbToAssign(
        final List<SachbearbeiterZuordnungStammdaten> stammdaten,
        final PersonInAusbildung pia
    ) {
        return stammdaten.stream().filter(x -> {
            final var range =
                pia.getKorrespondenzSprache() == Sprache.DEUTSCH ? x.getBuchstabenDe() : x.getBuchstabenFr();

            return BuchstabenRangeUtil.contains(range, pia.getNachname());
        })
            .map(SachbearbeiterZuordnungStammdaten::getBenutzer)
            .findFirst();
    }

    private PersonInAusbildung getPia(final Gesuch gesuch) {
        final var newest = gesuch.getNewestGesuchTranche();
        // No need for extra null checks, as the query that gets these implicitly does that
        return newest.map(gesuchTranche -> gesuchTranche.getGesuchFormular().getPersonInAusbildung()).orElse(null);

    }
}
