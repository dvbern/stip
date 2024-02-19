package ch.dvbern.stip.api.familiensituation.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
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
@Table(indexes = @Index(name = "IX_familiensituation_mandant", columnList = "mandant"))
@Getter
@Setter
public class Familiensituation extends AbstractMandantEntity {

    @NotNull
    @Column(nullable = false)
    private Boolean elternVerheiratetZusammen;

    @Column(nullable = true)
    private Boolean elternteilUnbekanntVerstorben;

    @Column(nullable = true)
    private Boolean gerichtlicheAlimentenregelung;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ElternAbwesenheitsGrund mutterUnbekanntVerstorben;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ElternUnbekanntheitsGrund mutterUnbekanntGrund;

    @Column(nullable = true)
    private Boolean mutterWiederverheiratet;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ElternAbwesenheitsGrund vaterUnbekanntVerstorben;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ElternUnbekanntheitsGrund vaterUnbekanntGrund;

    @Column(nullable = true)
    private Boolean vaterWiederverheiratet;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung sorgerecht;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung obhut;

    @Column(nullable = true)
    private BigDecimal obhutMutter;

    @Column(nullable = true)
    private BigDecimal obhutVater;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Elternschaftsteilung werZahltAlimente;
}
