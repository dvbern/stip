package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@MappedSuperclass
@Audited
@Getter
@Setter
public abstract class AbstractFamilieEntity extends AbstractEntity {
	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String nachname;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String vorname;

	@NotNull
	@Column(nullable = false)
	private LocalDate geburtsdatum;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Wohnsitz wohnsitz;

	@Column(nullable = true)
	private BigDecimal wohnsitzAnteilMutter;

	@Column(nullable = true)
	private BigDecimal wohnsitzAnteilVater;

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
		AbstractFamilieEntity that = (AbstractFamilieEntity) o;
		return Objects.equals(getNachname(), that.getNachname())
				&& Objects.equals(getVorname(), that.getVorname())
				&& Objects.equals(getGeburtsdatum(), that.getGeburtsdatum())
				&& getWohnsitz() == that.getWohnsitz()
				&& Objects.equals(getWohnsitzAnteilMutter(), that.getWohnsitzAnteilMutter())
				&& Objects.equals(getWohnsitzAnteilVater(), that.getWohnsitzAnteilVater());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				super.hashCode(),
				getNachname(),
				getVorname(),
				getGeburtsdatum(),
				getWohnsitz(),
				getWohnsitzAnteilMutter(),
				getWohnsitzAnteilVater());
	}
}
