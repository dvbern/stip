package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
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
import java.util.UUID;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@LebenslaufItemArtRequiredFieldsConstraint
@Entity
@Table(indexes = {
		@Index(name = "IX_lebenslauf_item_mandant", columnList = "mandant")
})
@Getter
@Setter
public class LebenslaufItem extends AbstractMandantEntity {

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Bildungsart bildungsart;


	@Column(nullable = true)
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

	@Column(nullable = true)
	private UUID copyOfId;
}
