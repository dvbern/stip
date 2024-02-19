package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
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

import java.math.BigDecimal;

@MappedSuperclass
@Audited
@WohnsitzAnteilRequiredConstraint
@WohnsitzAnteilBerechnungConstraint
@Getter
@Setter
public abstract class AbstractFamilieEntity extends AbstractPerson {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wohnsitz wohnsitz;

    @Column(nullable = true)
    @DecimalMax("100.00")
    @DecimalMin("0.00")
    private BigDecimal wohnsitzAnteilMutter;

    @Column(nullable = true)
    @DecimalMax("100.00")
    @DecimalMin("0.00")
    private BigDecimal wohnsitzAnteilVater;
}
