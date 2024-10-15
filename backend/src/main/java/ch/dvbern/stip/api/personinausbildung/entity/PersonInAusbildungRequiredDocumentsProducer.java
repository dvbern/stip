package ch.dvbern.stip.api.personinausbildung.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
@RequiredArgsConstructor
public class PersonInAusbildungRequiredDocumentsProducer implements RequiredDocumentProducer {
    private final PlzService plzService;

    private final Map<Niederlassungsstatus, DokumentTyp> niederlassungsstatusMap = Map.of(
        Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
        Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
        Niederlassungsstatus.FLUECHTLING, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE
    );

    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var pia = formular.getPersonInAusbildung();
        if (pia == null) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<DokumentTyp>();
        final var niederlassungsstatus = pia.getNiederlassungsstatus();
        if (niederlassungsstatus != null && niederlassungsstatusMap.containsKey(niederlassungsstatus)) {
            requiredDocs.add(niederlassungsstatusMap.get(niederlassungsstatus));
        }

        if (pia.isVormundschaft()) {
            requiredDocs.add(DokumentTyp.PERSON_KESB_ERNENNUNG);
        }

        if (pia.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
            requiredDocs.add(DokumentTyp.PERSON_MIETVERTRAG);
        }

        if (pia.isSozialhilfebeitraege()) {
            requiredDocs.add(DokumentTyp.PERSON_SOZIALHILFEBUDGET);
        }

        final var zivilstand = pia.getZivilstand();
        if (zivilstand == Zivilstand.GESCHIEDEN_GERICHTLICH ||
            zivilstand == Zivilstand.AUFGELOESTE_PARTNERSCHAFT) {
            requiredDocs.add(DokumentTyp.PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG);
        }

        if (plzService.isInBern(pia.getAdresse()) && parentsLiveAbroad(formular)) {
            requiredDocs.add(DokumentTyp.PERSON_AUSWEIS);
        }

        return ImmutablePair.of("personInAusbildung", requiredDocs);
    }

    // Returns whether both parents live abroad or not
    private boolean parentsLiveAbroad(final GesuchFormular formular) {
        if (formular.getElterns().isEmpty()) {
            return false;
        }

        return formular.getElterns().stream().allMatch(x -> {
            final var adresse = x.getAdresse();
            if (adresse == null) {
                return false;
            }

            return adresse.getLand() != Land.CH;
        });
    }
}
