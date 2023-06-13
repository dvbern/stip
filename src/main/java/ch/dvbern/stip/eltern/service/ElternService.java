package ch.dvbern.stip.eltern.service;

import ch.dvbern.stip.eltern.dto.ElternContainerDTO;
import ch.dvbern.stip.eltern.model.Eltern;
import ch.dvbern.stip.eltern.model.ElternContainer;
import ch.dvbern.stip.eltern.model.QElternContainer;
import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import ch.dvbern.stip.gesuchsperiode.model.QGesuchsperiode;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ElternService {

    @Inject
    private EntityManager entityManager;

    public Optional<Eltern> findEltern(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        Eltern e = entityManager.find(Eltern.class, id);
        return Optional.ofNullable(e);
    }

    public Optional<ElternContainer> findElternContainer(UUID id) {
        Objects.requireNonNull(id, "id muss gesetzt sein");
        ElternContainer ec = entityManager.find(ElternContainer.class, id);
        return Optional.ofNullable(ec);
    }

    public Optional<List<ElternContainerDTO>> findElternContainerDTOForGesuch(UUID gesuchId) {
        Objects.requireNonNull(gesuchId, "id muss gesetzt sein");
            JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
            QElternContainer elternContainer = new QElternContainer("eltern_container");

            var query = queryFactory.select(elternContainer
            ).from(elternContainer).where(elternContainer.gesuch.id.eq(gesuchId));
            List<ElternContainer> elternContainerList = query.fetch();
            List<ElternContainerDTO> elternContainerDTOS = elternContainerList.stream().map(ec -> ElternContainerDTO.from(ec)).toList();
            return Optional.ofNullable(elternContainerDTOS);
    }
}
