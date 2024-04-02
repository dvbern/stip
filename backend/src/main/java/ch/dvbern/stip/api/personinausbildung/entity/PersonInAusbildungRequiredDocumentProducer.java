package ch.dvbern.stip.api.personinausbildung.entity;

import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class PersonInAusbildungRequiredDocumentProducer implements RequiredDocumentProducer {
    private static final Map<Niederlassungsstatus, DokumentTyp> MAP = Map.of(
        Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
        Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
        Niederlassungsstatus.FLUECHTLING, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE
    );

    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        if (formular.getPersonInAusbildung() == null) {
            return ImmutablePair.of("", List.of());
        }

        final var niederlassungsstatus = formular.getPersonInAusbildung().getNiederlassungsstatus();
        if (MAP.containsKey(niederlassungsstatus)) {
            return ImmutablePair.of(
                "personInAusbildung.niederlassungsstatus",
                List.of(MAP.get(niederlassungsstatus))
            );
        }

        return ImmutablePair.of("", List.of());
    }
}
