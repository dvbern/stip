package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Eltern extends AbstractEntity {

	@NotNull
	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_eltern_adresse_id"), nullable = false)
	private Adresse adresse;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String sozialversicherungsnummer;

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
	@Enumerated(EnumType.STRING)
	private ElternTyp elternTyp;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String telefonnummer;

	@NotNull
	@Column(nullable = false)
	private LocalDate geburtsdatum;

	@NotNull
	@Column(nullable = false)
	private Boolean sozialhilfebeitraegeAusbezahlt;

	@NotNull
	@Column(nullable = false)
	private Boolean ausweisbFluechtling;

	@NotNull
	@Column(nullable = false)
	private Boolean ergaenzungsleistungAusbezahlt;

	@NotNull
	@Column(nullable = false)
	private boolean identischerZivilrechtlicherWohnsitz = true;

	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = true)
	private String identischerZivilrechtlicherWohnsitzOrt;

	@Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
	@Column(nullable = true)
	private String identischerZivilrechtlicherWohnsitzPLZ;

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
		Eltern eltern = (Eltern) o;
		return isIdentischerZivilrechtlicherWohnsitz() == eltern.isIdentischerZivilrechtlicherWohnsitz()
				&& Objects.equals(getAdresse(), eltern.getAdresse())
				&& Objects.equals(getSozialversicherungsnummer(), eltern.getSozialversicherungsnummer())
				&& Objects.equals(getNachname(), eltern.getNachname())
				&& Objects.equals(getVorname(), eltern.getVorname())
				&& getElternTyp() == eltern.getElternTyp()
				&& Objects.equals(getTelefonnummer(), eltern.getTelefonnummer())
				&& Objects.equals(getGeburtsdatum(), eltern.getGeburtsdatum())
				&& Objects.equals(getSozialhilfebeitraegeAusbezahlt(), eltern.getSozialhilfebeitraegeAusbezahlt())
				&& Objects.equals(getAusweisbFluechtling(), eltern.getAusweisbFluechtling())
				&& Objects.equals(getErgaenzungsleistungAusbezahlt(), eltern.getErgaenzungsleistungAusbezahlt())
				&& Objects.equals(getIdentischerZivilrechtlicherWohnsitzOrt(), eltern.getIdentischerZivilrechtlicherWohnsitzOrt())
				&& Objects.equals(getIdentischerZivilrechtlicherWohnsitzPLZ(), eltern.getIdentischerZivilrechtlicherWohnsitzPLZ());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				super.hashCode(),
				getAdresse(),
				getSozialversicherungsnummer(),
				getNachname(),
				getVorname(),
				getElternTyp(),
				getTelefonnummer(),
				getGeburtsdatum(),
				getSozialhilfebeitraegeAusbezahlt(),
				getAusweisbFluechtling(),
				getErgaenzungsleistungAusbezahlt(),
				isIdentischerZivilrechtlicherWohnsitz(),
				getIdentischerZivilrechtlicherWohnsitzOrt(),
				getIdentischerZivilrechtlicherWohnsitzPLZ());
	}
}
