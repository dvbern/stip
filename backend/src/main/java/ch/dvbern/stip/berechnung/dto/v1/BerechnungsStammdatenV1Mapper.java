package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.berechnung.dto.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import jakarta.inject.Singleton;

@Singleton
@DmnModelVersion(major = 1, minor = 0)
public class BerechnungsStammdatenV1Mapper implements BerechnungsStammdatenMapper {
    @Override
    public BerechnungsStammdatenDto mapFromRequest(DmnRequest request) {
        final BerechnungRequestV1 berechnungsRequest = (BerechnungRequestV1) request;

        return new BerechnungsStammdatenDto()
            .maxSaeule3a(berechnungsRequest.getStammdaten().getMaxSaeule3a())
            .einkommensfreibetrag(berechnungsRequest.getStammdaten().getEinkommensfreibetrag())
            .freibetragErwerbseinkommen(berechnungsRequest.getStammdaten().getFreibetragErwerbseinkommen())
            .freibetragVermoegen(berechnungsRequest.getStammdaten().getFreibetragVermoegen())
            .vermoegensanteilInProzent(berechnungsRequest.getStammdaten().getVermoegensanteilInProzent())
            .anzahlWochenLehre(berechnungsRequest.getStammdaten().getAnzahlWochenLehre())
            .anzahlWochenSchule(berechnungsRequest.getStammdaten().getAnzahlWochenSchule())
            .preisProMahlzeit(berechnungsRequest.getStammdaten().getPreisProMahlzeit())
            .stipLimiteMinimalstipendium(berechnungsRequest.getStammdaten().getStipLimiteMinimalstipendium())
            .limiteAlterAntragsstellerHalbierungElternbeitrag(
                berechnungsRequest.getStammdaten().getLimiteAlterAntragsstellerHalbierungElternbeitrag()
            );
    }
}
