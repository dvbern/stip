package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "ausgaben")
@Getter
@Setter
public class Ausgaben extends AbstractEntity {
    @NotNull
    @Column(name = "steuern_staat", nullable = false)
    @Min(value = 0)
    private Integer steuernStaat;
    @NotNull
    @Column(name = "steuern_bund", nullable = false)
    @Min(value = 0)
    private Integer steuernBund;
    @NotNull
    @Column(name = "fahrkosten", nullable = false)
    @Min(value = 0)
    private Integer fahrkosten;
    @Nullable
    @Column(name = "fahrkosten_partner", nullable = true)
    @Min(value = 0)
    private Integer fahrkostenPartner;
    @NotNull
    @Column(name = "verpflegung", nullable = false)
    @Min(value = 0)
    private Integer verpflegung;
    @Nullable
    @Column(name = "verpflegung_partner", nullable = true)
    @Min(value = 0)
    private Integer verpflegungPartner;
    @NotNull
    @Column(name = "steuerjahr", nullable = false)
    @Min(value = 0)
    private Integer steuerjahr;
    @NotNull
    @Column(name = "veranlagungscode", nullable = false)
    @Min(value = 0)
    private Integer veranlagungscode;
}
