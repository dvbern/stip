package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.lebenslauf.type.Bildungsart;
import ch.dvbern.stip.api.lebenslauf.type.Taetigskeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class LebenslaufItem extends AbstractEntity {

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Bildungsart bildungsart;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Taetigskeitsart taetigskeitsart;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String beschreibung;

	@NotNull
	@Column(nullable = false)
	private LocalDate von;

	@NotNull
	@Column(nullable = false)
	private LocalDate bis;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WohnsitzKanton wohnsitz;

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
		LebenslaufItem that = (LebenslaufItem) o;
		return getBildungsart() == that.getBildungsart()
				&& getTaetigskeitsart() == that.getTaetigskeitsart()
				&& Objects.equals(getBeschreibung(), that.getBeschreibung())
				&& Objects.equals(getVon(), that.getVon())
				&& Objects.equals(getBis(), that.getBis())
				&& getWohnsitz() == that.getWohnsitz();
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				super.hashCode(),
				getBildungsart(),
				getTaetigskeitsart(),
				getBeschreibung(),
				getVon(),
				getBis(),
				getWohnsitz());
	}
}
