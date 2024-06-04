package ch.dvbern.stip.api.plz.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "plz",
    uniqueConstraints = @UniqueConstraint(name = "UC_plz_ort_kantonskuerzel", columnNames={"plz", "ort", "kantonskuerzel"}),
    indexes = @Index(name = "IX_plz_kantonskuerzel", columnList = "kantonskuerzel")
)
@Audited
@Getter
@Setter
public class Plz extends AbstractEntity {
    @NotNull
    @Column(name = "plz", nullable = false)
    private String plz;

    @NotNull
    @Column(name = "ort", nullable = false)
    private String ort;

    @NotNull
    @Column(name = "kantonskuerzel", nullable = false)
    private String kantonskuerzel;
}
