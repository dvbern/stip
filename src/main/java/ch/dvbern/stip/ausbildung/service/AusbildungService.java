package ch.dvbern.stip.ausbildung.service;

import ch.dvbern.stip.ausbildung.dto.AusbildungsgangDTO;
import ch.dvbern.stip.ausbildung.dto.AusbildungstaetteDTO;
import ch.dvbern.stip.ausbildung.model.Ausbildungsgang;
import ch.dvbern.stip.ausbildung.model.Ausbildungstaette;
import ch.dvbern.stip.ausbildung.model.QAusbildungsgang;
import ch.dvbern.stip.ausbildung.model.QAusbildungstaette;
import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import ch.dvbern.stip.gesuchsperiode.model.QGesuchsperiode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

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
}
