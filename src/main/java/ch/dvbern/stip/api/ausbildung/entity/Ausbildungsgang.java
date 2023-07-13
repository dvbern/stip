package ch.dvbern.stip.api.ausbildung.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Ausbildungsgang extends AbstractEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildungsgang_ausbildungsstaette_id"), nullable = true)
	private Ausbildungsstaette ausbildungsstaette;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String bezeichnungDe;

	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column
	private String bezeichnungFr;

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
		Ausbildungsgang that = (Ausbildungsgang) o;
		return Objects.equals(getAusbildungsstaette(), that.getAusbildungsstaette())
				&& Objects.equals(getBezeichnungDe(), that.getBezeichnungDe())
				&& Objects.equals(getBezeichnungFr(), that.getBezeichnungFr());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getAusbildungsstaette(), getBezeichnungDe(), getBezeichnungFr());
	}
}
