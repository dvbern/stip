package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonTypeName("Stammdaten_V1")
public class StammdatenV1 {
    int maxSaeule3a;
    int einkommensfreibetrag;
    int anzahlWochenLehre;
    int anzahlWochenSchule;
    int preisProMahlzeit;

    public static StammdatenV1 fromGesuchsperiode(final Gesuchsperiode gesuchsperiode) {
        // TODO KSTIP-542: Remove hardcoded values and actually map them from somewhere
        return new StammdatenV1Builder()
            .maxSaeule3a(gesuchsperiode.getMaxSaeule3a())
            .einkommensfreibetrag(gesuchsperiode.getEinkommensfreibetrag())
            .anzahlWochenLehre(gesuchsperiode.getAnzahlWochenLehre())
            .anzahlWochenSchule(gesuchsperiode.getAnzahlWochenSchule())
            .preisProMahlzeit(gesuchsperiode.getPreisProMahlzeit())
            .build();
    }
}
