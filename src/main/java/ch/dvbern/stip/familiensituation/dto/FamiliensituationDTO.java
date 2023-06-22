package ch.dvbern.stip.familiensituation.dto;

import ch.dvbern.stip.familiensituation.model.ElternAbwesenheitsGrund;
import ch.dvbern.stip.familiensituation.model.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.familiensituation.model.Elternschaftsteilung;
import ch.dvbern.stip.familiensituation.model.Familiensituation;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class FamiliensituationDTO {
    @NotNull
    private UUID id;

    @NotNull
    private Boolean elternVerheiratetZusammen;

    private Boolean elternteilVerstorben;

    private Boolean elternteilUnbekanntVerstorben;

    private Boolean gerichtlicheAlimentenregelung;

    private ElternAbwesenheitsGrund mutterUnbekanntVerstorben;

    private ElternUnbekanntheitsGrund mutterUnbekanntGrund;

    private Boolean mutterWiederverheiratet;

    private ElternAbwesenheitsGrund vaterUnbekanntVerstorben;

    private ElternUnbekanntheitsGrund vaterUnbekanntGrund;

    private Boolean vaterWiederverheiratet;

    private Elternschaftsteilung sorgerecht;

    private Elternschaftsteilung obhut;

    private BigDecimal obhutMutter;

    private BigDecimal obhutVater;

    private Elternschaftsteilung werZahltAlimente;

    public static FamiliensituationDTO from(Familiensituation familiensituation) {
        return familiensituation == null ? null : new FamiliensituationDTO(familiensituation.getId(),
                familiensituation.getElternVerheiratetZusammen(),
                familiensituation.getElternteilVerstorben(),
                familiensituation.getElternteilUnbekanntVerstorben(),
                familiensituation.getGerichtlicheAlimentenregelung(),
                familiensituation.getMutterUnbekanntVerstorben(),
                familiensituation.getMutterUnbekanntGrund(),
                familiensituation.getMutterWiederverheiratet(),
                familiensituation.getVaterUnbekanntVerstorben(),
                familiensituation.getVaterUnbekanntGrund(),
                familiensituation.getVaterWiederverheiratet(),
                familiensituation.getSorgerecht(),
                familiensituation.getObhut(),
                familiensituation.getObhutMutter(),
                familiensituation.getObhutVater(),
                familiensituation.getWerZahltAlimente());
    }

    public void apply(Familiensituation familiensituation) {
        familiensituation.setElternVerheiratetZusammen(elternVerheiratetZusammen);
        familiensituation.setElternteilVerstorben(elternteilVerstorben);
        familiensituation.setElternteilUnbekanntVerstorben(elternteilUnbekanntVerstorben);
        familiensituation.setGerichtlicheAlimentenregelung(gerichtlicheAlimentenregelung);
        familiensituation.setMutterUnbekanntVerstorben(mutterUnbekanntVerstorben);
        familiensituation.setMutterUnbekanntGrund(mutterUnbekanntGrund);
        familiensituation.setMutterWiederverheiratet(mutterWiederverheiratet);
        familiensituation.setVaterUnbekanntVerstorben(vaterUnbekanntVerstorben);
        familiensituation.setVaterUnbekanntGrund(vaterUnbekanntGrund);
        familiensituation.setVaterWiederverheiratet(vaterWiederverheiratet);
        familiensituation.setSorgerecht(sorgerecht);
        familiensituation.setObhut(obhut);
        familiensituation.setObhutMutter(obhutMutter);
        familiensituation.setObhutVater(obhutVater);
        familiensituation.setWerZahltAlimente(werZahltAlimente);
    }
}
