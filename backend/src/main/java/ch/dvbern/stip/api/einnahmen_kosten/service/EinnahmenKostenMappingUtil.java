package ch.dvbern.stip.api.einnahmen_kosten.service;

import java.util.Objects;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchFormularCalculationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenMappingUtil {
    public Integer calculateVermoegen(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return null;
        }
        Integer vermoegen = gesuchFormular.getEinnahmenKosten().getVermoegen();
        if (GesuchFormularCalculationUtil.wasGSOlderThan18(gesuchFormular)) {
            return Objects.requireNonNullElse(vermoegen, 0);
        }
        return null;
    }

    public Integer calculateSteuern(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return null;
        }
        int totalEinkommen = 0;
        if (gesuchFormular.getEinnahmenKosten() != null && gesuchFormular.getEinnahmenKosten().getNettoerwerbseinkommen() != null) {
            totalEinkommen += gesuchFormular.getEinnahmenKosten().getNettoerwerbseinkommen();
        }
        if (gesuchFormular.getPartner() != null && gesuchFormular.getPartner().getJahreseinkommen() != null) {
            totalEinkommen += gesuchFormular.getPartner().getJahreseinkommen();
        }
        if (totalEinkommen >= 20000) {
            return (int)(totalEinkommen * 0.1);
        }
        return 0;
    }
}
