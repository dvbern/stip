package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Value
@Jacksonized
public class StammdatenV1 {
    int maxSaeule3a;
    int einkommensfreibetrag;
    int freibetragErwerbseinkommen;
    int freibetragVermoegen;
    int vermoegensanteilInProzent;
    int anzahlWochenLehre;
    int anzahlWochenSchule;
    int preisProMahlzeit;
    int stipLimiteMinimalstipendium;
    int limiteAlterAntragsstellerHalbierungElternbeitrag;

    public static StammdatenV1 fromGesuchsperiode(final Gesuchsperiode gesuchsperiode) {
        return new StammdatenV1Builder()
            .maxSaeule3a(gesuchsperiode.getMaxSaeule3a())
            .einkommensfreibetrag(gesuchsperiode.getEinkommensfreibetrag())
            .freibetragErwerbseinkommen(gesuchsperiode.getFreibetragErwerbseinkommen())
            .freibetragVermoegen(gesuchsperiode.getFreibetragVermoegen())
            .vermoegensanteilInProzent(gesuchsperiode.getVermoegensanteilInProzent())
            .anzahlWochenLehre(gesuchsperiode.getAnzahlWochenLehre())
            .anzahlWochenSchule(gesuchsperiode.getAnzahlWochenSchule())
            .preisProMahlzeit(gesuchsperiode.getPreisProMahlzeit())
            .stipLimiteMinimalstipendium(gesuchsperiode.getStipLimiteMinimalstipendium())
            .limiteAlterAntragsstellerHalbierungElternbeitrag(25)
            .build();
    }
}
