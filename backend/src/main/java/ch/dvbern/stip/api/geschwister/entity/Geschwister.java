package ch.dvbern.stip.api.geschwister.entity;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Audited
@Entity
@Table(indexes = @Index(name = "IX_geschwister_mandant", columnList = "mandant"))
@Getter
@Setter
public class Geschwister extends AbstractFamilieEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Column(nullable = true)
    private UUID copyOfId;
}
