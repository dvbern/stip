package ch.dvbern.stip.api.steuerdaten.util;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuerdatenCopyUtil {
    public Steuerdaten createCopy(final Steuerdaten other) {
        final var copy = new Steuerdaten();

        copy.setSteuerdatenTyp(other.getSteuerdatenTyp());
        copy.setTotalEinkuenfte(other.getTotalEinkuenfte());
        copy.setEigenmietwert(other.getEigenmietwert());
        copy.setIsArbeitsverhaeltnisSelbstaendig(other.getIsArbeitsverhaeltnisSelbstaendig());
        copy.setSaeule3a(other.getSaeule3a());
        copy.setSaeule2(other.getSaeule2());
        copy.setKinderalimente(other.getKinderalimente());
        copy.setVermoegen(other.getVermoegen());
        copy.setSteuernKantonGemeinde(other.getSteuernKantonGemeinde());
        copy.setSteuernBund(other.getSteuernBund());
        copy.setFahrkosten(other.getFahrkosten());
        copy.setFahrkostenPartner(other.getFahrkostenPartner());
        copy.setVerpflegung(other.getVerpflegung());
        copy.setVerpflegungPartner(other.getVerpflegungPartner());
        copy.setSteuerjahr(other.getSteuerjahr());
        copy.setVeranlagungsCode(other.getVeranlagungsCode());

        return copy;
    }

    public Set<Steuerdaten> createCopySet(final Set<Steuerdaten> other) {
        final var copy = new LinkedHashSet<Steuerdaten>();
        for (final var entry : other) {
            copy.add(createCopy(entry));
        }

        return copy;
    }
}
