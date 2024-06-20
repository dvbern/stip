package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
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
@Table(name = "einnahmen")
@Getter
@Setter
public class Einnahmen extends AbstractEntity {
    @NotNull
    @Column(name = "total_einkuenfte", nullable = false)
    @Min(value = 0)
    private Integer totalEinkuenfte;
    @NotNull
    @Column(name = "eigenmietwert", nullable = false)
    @Min(value = 0)
    private Integer eigenmietwert;
    @Column(name = "isArbeistverhaeltnisSelbstaendig", nullable = false)
    private Boolean isArbeitsverhaeltnisSelbstaendig = false;
    @NotNull
    @Column(name = "saeule3a", nullable = false)
    @Min(value = 0)
    private Integer saeule3a;
    @NotNull
    @Column(name = "saeule2", nullable = false)
    @Min(value = 0)
    private Integer saeule2;
    @NotNull
    @Column(name = "kinderalimente", nullable = false)
    @Min(value = 0)
    private Integer kinderalimente;
    @NotNull
    @Column(name = "ergaenzungsleistungen", nullable = false)
    @Min(value = 0)
    private Integer ergaenzungsleistungen;
    @NotNull
    @Column(name = "vermoegen", nullable = false)
    @Min(value = 0)
    private Integer vermoegen;
}
