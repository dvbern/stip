package ch.dvbern.stip.api.ausbildung.entity;

import ch.dvbern.stip.api.ausbildung.type.Ausbildungsland;
import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.Objects;

@Audited
@Entity
@Getter
@Setter
public class Ausbildungsstaette extends AbstractEntity {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ausbildungsstaette")
	private List<Ausbildungsgang> ausbildungsgaenge;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Ausbildungsland ausbildungsland;

	@NotNull
	@Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String name;

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
		Ausbildungsstaette that = (Ausbildungsstaette) o;
		return Objects.equals(getAusbildungsgaenge(), that.getAusbildungsgaenge())
				&& getAusbildungsland() == that.getAusbildungsland()
				&& Objects.equals(getName(), that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getAusbildungsgaenge(), getAusbildungsland(), getName());
	}
}
