package ch.dvbern.stip.api.geschwister.entity;

import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Getter
@Setter
public class Geschwister extends AbstractFamilieEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Geschwister that = (Geschwister) o;
        return getAusbildungssituation() == that.getAusbildungssituation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAusbildungssituation());
    }
}
