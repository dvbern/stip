package ch.dvbern.stip.api.common.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@MappedSuperclass
@Audited
@Getter
@Setter
@EqualsAndHashCode
public class AbstractPerson extends AbstractEntity {

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
}
