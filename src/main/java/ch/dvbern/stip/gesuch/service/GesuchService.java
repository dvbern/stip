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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.gesuch.service;

import ch.dvbern.stip.ausbildung.service.AusbildungService;
import ch.dvbern.stip.fall.model.Fall;
import ch.dvbern.stip.fall.service.FallService;
import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.dto.GesuchDTO;
import ch.dvbern.stip.gesuch.model.QGesuch;
import ch.dvbern.stip.gesuchsperiode.model.Gesuchsperiode;
import ch.dvbern.stip.gesuchsperiode.service.GesuchsperiodeService;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GesuchService {
    @Inject
    private EntityManager entityManager;

    @Inject
    private FallService fallService;

    @Inject
    private GesuchsperiodeService gesuchsperiodeService;

    @Inject
    private AusbildungService ausbildungService;

    public Optional<Gesuch> findGesuch(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        Gesuch g = entityManager.find(Gesuch.class, id);
        return Optional.ofNullable(g);
    }

    public Gesuch saveGesuch(GesuchDTO gesuchDTO) {
        Gesuch gesuch = gesuchDTO.getId() != null ? findGesuch(gesuchDTO.getId()).orElse(new Gesuch()) : new Gesuch();
        if (gesuch.getFall() == null) {
            Fall fall;
            if (gesuchDTO.getFall().getId() != null) {
                fall = fallService.findFall(gesuchDTO.getFall().getId()).orElseThrow(
                        () -> new RuntimeException("Fall existiert nicht")
                );
            } else {
                fall = fallService.saveFall(gesuchDTO.getFall());
            }
            gesuch.setFall(fall);
        }
        if (gesuch.getGesuchsperiode() == null) {
            Gesuchsperiode gesuchsperiode = gesuchsperiodeService.findGesuchsperiode(gesuchDTO.getGesuchsperiode().getId())
                    .orElseThrow(() -> new RuntimeException("Gesuchsperiode existiert nicht"));
            gesuch.setGesuchsperiode(gesuchsperiode);
        }

        gesuchDTO.apply(gesuch);
        // linked entities auf der Ausbildung
        handleAusbildungStammdaten(gesuch, gesuchDTO);

        return entityManager.merge(gesuch);
    }

    private void handleAusbildungStammdaten(Gesuch gesuch, GesuchDTO gesuchDTO) {
        if (gesuch.getAusbildungContainer() != null && gesuch.getAusbildungContainer().getAusbildungSB() != null) {
            if (gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungsgang() == null ||
            !gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungsgang().getId().equals(
                    gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungsgangId())) {
                gesuch.getAusbildungContainer().getAusbildungSB().setAusbildungsgang(
                        ausbildungService.findAusbildungsgangByID(
                                        gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungsgangId())
                                .orElseThrow(() -> new RuntimeException("Ausbildungsgang nicht gefunden")));
            }
            if (gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungstaette() == null ||
                    !gesuch.getAusbildungContainer().getAusbildungSB().getAusbildungstaette().getId().equals(
                            gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungstaetteId())) {
                gesuch.getAusbildungContainer().getAusbildungSB().setAusbildungstaette(
                        ausbildungService.findAusbildungstaetteByID(
                                        gesuchDTO.getAusbildungContainer().getAusbildungSB().getAusbildungstaetteId())
                                .orElseThrow(() -> new RuntimeException("Ausbildungstaette nicht gefunden")));
            }
        }
    }

    // it doesn't make any sense anymore too much object to map for such a query, this is good for small dtos...
    public Optional<GesuchDTO> findGesuchDTO(UUID id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QGesuch qgesuch = new QGesuch("gesuch");

        Gesuch gesuch = queryFactory.select(qgesuch
                ).from(qgesuch)
                .where(qgesuch.id.eq(id)).fetchOne();

        GesuchDTO gesuchDTO = gesuch != null ? GesuchDTO.from(gesuch) : null;

        return Optional.ofNullable(gesuchDTO);
    }

    public Optional<List<GesuchDTO>> findAll() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QGesuch qgesuch = new QGesuch("gesuch");

        var query = queryFactory.select(qgesuch
        ).from(qgesuch);
        List<Gesuch> gesuchList = query.fetch();
        List<GesuchDTO> gesuchDTOList = gesuchList.stream().map(gesuch -> GesuchDTO.from(gesuch)).toList();


        return Optional.ofNullable(gesuchDTOList);
    }
}
