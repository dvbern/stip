package ch.dvbern.stip.api.familiensituation.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@ObhutGemeinsamRequiredFieldsConstraint
@WerZahltAlimenteRequiredFieldConstraint
@ObhutGemeinsamBerechnungConstraint
@Entity
@Table(
    name = "familiensituation",
    indexes = @Index(name = "IX_familiensituation_mandant", columnList = "mandant")
)
@Getter
@Setter
public class Familiensituation extends AbstractMandantEntity {
    @NotNull
    @Column(name = "eltern_verheiratet_zusammen", nullable = false)
    private Boolean elternVerheiratetZusammen;

    @Nullable
    @Column(name = "elternteil_unbekannt_verstorben")
    private Boolean elternteilUnbekanntVerstorben;

    @Nullable
    @Column(name = "gerichtliche_alimentenregelung")
    private Boolean gerichtlicheAlimentenregelung;

    @Nullable
    @Column(name = "mutter_unbekannt_verstorben")
    @Enumerated(EnumType.STRING)
    private ElternAbwesenheitsGrund mutterUnbekanntVerstorben;

    @Nullable
    @Column(name = "mutter_unbekannt_grund")
    @Enumerated(EnumType.STRING)
    private ElternUnbekanntheitsGrund mutterUnbekanntGrund;

    @Nullable
    @Column(name = "mutter_wiederverheiratet")
    private Boolean mutterWiederverheiratet;

    @Nullable
    @Column(name = "vater_unbekannt_verstorben")
    @Enumerated(EnumType.STRING)
    private ElternAbwesenheitsGrund vaterUnbekanntVerstorben;

    @Nullable
    @Column(name = "vater_unbekannt_grund")
    @Enumerated(EnumType.STRING)
    private ElternUnbekanntheitsGrund vaterUnbekanntGrund;

    @Nullable
    @Column(name = "vater_wiederverheiratet")
    private Boolean vaterWiederverheiratet;

    @Nullable
    @Column(name = "sorgerecht")
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung sorgerecht;

    @Nullable
    @Column(name = "obhut")
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung obhut;

    @Nullable
    @Column(name = "obhut_mutter")
    private BigDecimal obhutMutter;

    @Nullable
    @Column(name = "obhut_vater")
    private BigDecimal obhutVater;

    @Nullable
    @Column(name = "wer_zahlt_alimente")
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung werZahltAlimente;
}
