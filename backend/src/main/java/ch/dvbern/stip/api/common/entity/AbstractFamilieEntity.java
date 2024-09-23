package ch.dvbern.stip.api.common.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@MappedSuperclass
@Audited
@WohnsitzAnteilRequiredConstraint
@WohnsitzAnteilBerechnungConstraint
@Getter
@Setter
public abstract class AbstractFamilieEntity extends AbstractPerson {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz", nullable = false)
    private Wohnsitz wohnsitz;

    @Nullable
    @Column(name = "wohnsitz_anteil_mutter")
    @DecimalMax("100.00")
    @DecimalMin("0.00")
    private BigDecimal wohnsitzAnteilMutter;

    @Nullable
    @Column(name = "wohnsitz_anteil_vater")
    @DecimalMax("100.00")
    @DecimalMin("0.00")
    private BigDecimal wohnsitzAnteilVater;

    public BigDecimal getWohnsitzAnteil(ElternTyp elternTyp) {
        return switch (elternTyp) {
            case VATER -> this.getWohnsitzAnteilVater();
            case MUTTER -> this.getWohnsitzAnteilMutter();
        };
    }
}
