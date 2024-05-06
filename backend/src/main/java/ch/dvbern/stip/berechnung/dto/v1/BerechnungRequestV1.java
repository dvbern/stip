package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class BerechnungRequestV1 implements BerechnungRequest {
    @JsonProperty("Stammdaten_V1")
    StammdatenV1 stammdaten;

    public static BerechnungRequestV1 createRequest(final Gesuch gesuch) {
        return new BerechnungRequestV1(
            StammdatenV1.fromGesuchsperiode(gesuch.getGesuchsperiode())
        );
    }
}
