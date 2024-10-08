package ch.dvbern.stip.api.dokument.util;

import java.util.List;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchDokumentCopyUtil {
    /**
     * Copies the given {@link GesuchDokument}e and sets the {@link GesuchDokument#dokumente} to the same reference
     * as the dokumente in the respective entry in the source
     */
    public List<GesuchDokument> copyGesuchDokumenteWithDokumentReferences(
        final GesuchTranche targetTranche,
        final List<GesuchDokument> toCopy
    ) {
        return toCopy.stream().map(original -> {
            final var copy = new GesuchDokument();

            copy.setGesuchTranche(targetTranche);
            copy.setDokumentTyp(original.getDokumentTyp());
            copy.setStatus(original.getStatus());
            original.getDokumente().forEach(copy::addDokument);

            return copy;
        }).toList();
    }
}
