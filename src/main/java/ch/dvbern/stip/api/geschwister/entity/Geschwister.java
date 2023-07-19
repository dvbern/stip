package ch.dvbern.stip.api.geschwister.entity;

import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Geschwister extends AbstractFamilieEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Column(nullable = true)
    private UUID copyOfId;
}
