package ch.dvbern.stip.api.geschwister.entity;

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
    name = "geschwister",
    indexes = @Index(name = "IX_geschwister_mandant", columnList = "mandant")
)
@Getter
@Setter
public class Geschwister extends AbstractFamilieEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ausbildungssituation", nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;
}
