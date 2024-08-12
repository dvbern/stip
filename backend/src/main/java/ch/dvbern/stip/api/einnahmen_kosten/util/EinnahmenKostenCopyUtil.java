package ch.dvbern.stip.api.einnahmen_kosten.util;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenCopyUtil {
    public EinnahmenKosten createCopy(final EinnahmenKosten other) {
        final var copy = new EinnahmenKosten();

        copy.setNettoerwerbseinkommen(other.getNettoerwerbseinkommen());
        copy.setFahrkosten(other.getFahrkosten());
        copy.setWohnkosten(other.getWohnkosten());
        copy.setWgWohnend(other.getWgWohnend());
        copy.setVerdienstRealisiert(other.getVerdienstRealisiert());
        copy.setAlimente(other.getAlimente());
        copy.setZulagen(other.getZulagen());
        copy.setRenten(other.getRenten());
        copy.setEoLeistungen(other.getEoLeistungen());
        copy.setErgaenzungsleistungen(other.getErgaenzungsleistungen());
        copy.setBeitraege(other.getBeitraege());
        copy.setAusbildungskostenSekundarstufeZwei(other.getAusbildungskostenSekundarstufeZwei());
        copy.setAusbildungskostenTertiaerstufe(other.getAusbildungskostenTertiaerstufe());
        copy.setWillDarlehen(other.getWillDarlehen());
        copy.setAuswaertigeMittagessenProWoche(other.getAuswaertigeMittagessenProWoche());
        copy.setBetreuungskostenKinder(other.getBetreuungskostenKinder());
        copy.setVeranlagungsCode(other.getVeranlagungsCode());
        copy.setSteuerjahr(other.getSteuerjahr());
        copy.setVermoegen(other.getVermoegen());

        return copy;
    }
}
