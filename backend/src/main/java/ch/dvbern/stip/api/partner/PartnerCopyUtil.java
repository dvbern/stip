package ch.dvbern.stip.api.partner;

import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.partner.entity.Partner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PartnerCopyUtil {
    public Partner createCopyIgnoreReferences(final Partner other) {
        if (other == null) {
            return null;
        }

        final var copy = new Partner();

        AbstractPersonCopyUtil.copy(other, copy);
        copy.setSozialversicherungsnummer(other.getSozialversicherungsnummer());
        copy.setAusbildungMitEinkommenOderErwerbstaetig(other.isAusbildungMitEinkommenOderErwerbstaetig());
        copy.setJahreseinkommen(other.getJahreseinkommen());
        copy.setVerpflegungskosten(other.getVerpflegungskosten());
        copy.setFahrkosten(other.getFahrkosten());

        return copy;
    }
}
