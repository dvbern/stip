package ch.dvbern.stip.api.einnahmen_kosten.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Audited
@Entity
@Table(indexes = {
		@Index(name = "IX_einnahme_kosten_mandant", columnList = "mandant")
})
@Getter
@Setter
public class EinnahmenKosten extends AbstractMandantEntity {
	@NotNull
	@Column(nullable = false)
	private BigDecimal nettoerwerbseinkommen;
	@NotNull
	@Column(nullable = false)
	private BigDecimal fahrkosten;
	@Nullable
	@Column(nullable = true)
	private BigDecimal wohnkosten;
	@Nullable
	@Column(nullable = true)
	private BigDecimal personenImHaushalt;
	@NotNull
	@Column(nullable = false)
	private Boolean verdienstRealisiert;
	@Column
	private BigDecimal alimente;
	@Column
	private BigDecimal zulagen;
	@Column
	private BigDecimal renten;
	@Column
	private BigDecimal eoLeistungen;
	@Column
	private BigDecimal ergaenzungsleistungen;
	@Column
	private BigDecimal beitraege;
	@Column
	private BigDecimal ausbildungskostenSekundarstufeZwei;
	@Column
	private BigDecimal ausbildungskostenTertiaerstufe;
	@Column
	private Boolean willDarlehen;
	@Column
	private Integer auswaertigeMittagessenProWoche;

}
