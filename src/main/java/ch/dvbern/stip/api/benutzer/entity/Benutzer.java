package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Entity
@Audited
@Getter
@Setter
@EqualsAndHashCode
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
