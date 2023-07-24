package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@MappedSuperclass
@Audited
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractFamilieEntity extends AbstractPerson {

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Wohnsitz wohnsitz;

	@Column(nullable = true)
	private BigDecimal wohnsitzAnteilMutter;

	@Column(nullable = true)
	private BigDecimal wohnsitzAnteilVater;
}
