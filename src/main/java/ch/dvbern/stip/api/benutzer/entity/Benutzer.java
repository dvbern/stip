package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Entity
@Table
@Audited
@Getter
@Setter
public class Benutzer extends AbstractEntity {

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
	private String sozialversicherungsnummer;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BenutzerStatus benutzerStatus;

	@NotNull
	@Column(nullable = false)
	private String mandant = MandantIdentifier.BERN.name();
}
