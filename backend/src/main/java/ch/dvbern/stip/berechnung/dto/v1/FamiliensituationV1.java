package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
public class FamiliensituationV1 {
    Boolean elternVerheiratetZusammen;
    Boolean gerichtlicheAlimentenregelung;
    String werZahltAlimente;
    Boolean elternteilUnbekanntVerstorben;
    Boolean vaterUnbekanntVerstorben;
    Boolean mutterUnbekanntVerstorben;
    Boolean vaterWiederverheiratet;
    Boolean mutterWiederverheiratet;

    public static FamiliensituationV1 fromFamiliensituation(final Familiensituation familiensituation) {
        return new FamiliensituationV1Builder()
            .elternVerheiratetZusammen(familiensituation.getElternVerheiratetZusammen())
            .gerichtlicheAlimentenregelung(familiensituation.getGerichtlicheAlimentenregelung())
            .werZahltAlimente(familiensituation.getWerZahltAlimente() != null ? familiensituation.getWerZahltAlimente().toString() : null)
            .elternteilUnbekanntVerstorben(familiensituation.getElternteilUnbekanntVerstorben())
            .vaterUnbekanntVerstorben(familiensituation.getVaterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH)
            .mutterUnbekanntVerstorben(familiensituation.getMutterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH)
            .vaterWiederverheiratet(familiensituation.getVaterWiederverheiratet())
            .mutterWiederverheiratet(familiensituation.getMutterWiederverheiratet())
            .build();
    }
}
