package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "steuerdaten")
public class Steuerdaten extends AbstractEntity {
    @NotNull
    @JoinColumn(name="einnahmen_id", foreignKey = @ForeignKey(name = "FK_steuerdaten_einnahmen_id"))
    @OneToOne
    private Einnahmen einnahmen;

    @NotNull
    @JoinColumn(name="ausgaben_id", foreignKey = @ForeignKey(name = "FK_steuerdaten_ausgaben_id"))
    @OneToOne
    private Ausgaben ausgaben;
}
