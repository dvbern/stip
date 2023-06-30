package ch.dvbern.stip.dokument.repo;

import ch.dvbern.stip.common.repostitory.BaseRepository;
import ch.dvbern.stip.dokument.entity.Dokument;
import ch.dvbern.stip.dokument.entity.DokumentTyp;
import ch.dvbern.stip.dokument.entity.GesuchDokument;
import ch.dvbern.stip.dokument.entity.QGesuchDokument;
import ch.dvbern.stip.dokument.service.DokumentMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentRepository implements BaseRepository<Dokument> {

    public GesuchDokument findGesuchDokument(DokumentTyp dokumentTyp, UUID gesuchId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(getEntityManager());
        QGesuchDokument qGesuchDokument = new QGesuchDokument("gesuchDokument");
        var query = queryFactory.select(qGesuchDokument).from(qGesuchDokument).where(
                qGesuchDokument.dokumentTyp.eq(DokumentTyp.valueOf(dokumentTyp.name())).and(qGesuchDokument.gesuch.id.eq(gesuchId))
        );
        return query.fetchOne();
    }
}
