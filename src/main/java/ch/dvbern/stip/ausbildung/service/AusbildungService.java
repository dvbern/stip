package ch.dvbern.stip.ausbildung.service;

import ch.dvbern.stip.ausbildung.dto.AusbildungstaetteDTO;
import ch.dvbern.stip.ausbildung.model.Ausbildungsgang;
import ch.dvbern.stip.ausbildung.model.Ausbildungstaette;
import ch.dvbern.stip.ausbildung.model.QAusbildungstaette;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AusbildungService {

    @Inject
    private EntityManager entityManager;

    public List<AusbildungstaetteDTO> findAlleAusbildungstaetteDTO() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAusbildungstaette ausbildungstaette = new QAusbildungstaette("ausbildungstaette");

        List<Ausbildungstaette> ausbildungstaetteList = queryFactory.select(ausbildungstaette
        ).from(ausbildungstaette).fetch();

        return ausbildungstaetteList.stream().map(ausbildungstaette1 -> AusbildungstaetteDTO.from(ausbildungstaette1)).toList();
    }

    public Optional<Ausbildungstaette> findAusbildungstaetteByID(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        Ausbildungstaette ausbildungstaette = entityManager.find(Ausbildungstaette.class, id);
        return Optional.ofNullable(ausbildungstaette);
    }

    public Optional<Ausbildungsgang> findAusbildungsgangByID(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        Ausbildungsgang ausbildungsgang = entityManager.find(Ausbildungsgang.class, id);
        return Optional.ofNullable(ausbildungsgang);
    }
}
