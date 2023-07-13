package ch.dvbern.stip.api.auszahlung.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
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
public class Auszahlung extends AbstractEntity {

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Kontoinhaber kontoinhaber;
	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String vorname;

	@NotNull
	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_auszahlung_adresse_id"), nullable = false)
	private Adresse adresse;
	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String iban;
	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String nachname;

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
		Auszahlung that = (Auszahlung) o;
		return getKontoinhaber() == that.getKontoinhaber()
				&& Objects.equals(getVorname(), that.getVorname())
				&& Objects.equals(getAdresse(), that.getAdresse())
				&& Objects.equals(getIban(), that.getIban())
				&& Objects.equals(getNachname(), that.getNachname());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getKontoinhaber(), getVorname(), getAdresse(), getIban(), getNachname());
	}
}
