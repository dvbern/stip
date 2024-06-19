package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class StammdatenV1 {
    int maxSaeule3a;
    int einkommensfreibetrag;
    int anzahlWochenLehre;
    int anzahlWochenSchule;
    int preisProMahlzeit;

    public static StammdatenV1 fromGesuchsperiode(final Gesuchsperiode gesuchsperiode) {
        return new StammdatenV1Builder()
            .maxSaeule3a(gesuchsperiode.getMaxSaeule3a())
            .einkommensfreibetrag(gesuchsperiode.getEinkommensfreibetrag())
            .anzahlWochenLehre(gesuchsperiode.getAnzahlWochenLehre())
            .anzahlWochenSchule(gesuchsperiode.getAnzahlWochenSchule())
            .preisProMahlzeit(gesuchsperiode.getPreisProMahlzeit())
            .build();
    }
}
