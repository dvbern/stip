package ch.dvbern.stip.api.kind.entity;

import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
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
@Entity
@Table(
    name = "kind",
    indexes = @Index(name = "IX_kind_mandant", columnList = "mandant")
)
@Getter
@Setter
public class Kind extends AbstractFamilieEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ausbildungssituation", nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Nullable
    @Column(name = "erhaltene_alimentebeitraege")
    private Integer erhalteneAlimentebeitraege;

    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;
}
