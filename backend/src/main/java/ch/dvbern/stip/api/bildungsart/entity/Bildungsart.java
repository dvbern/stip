package ch.dvbern.stip.api.bildungsart.entity;

import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Getter
@Setter
@Table
@RequiredArgsConstructor
public class Bildungsart extends AbstractEntity {
	@Column(name = "beschreibung", nullable = false)
	private String beschreibung;

	@Enumerated(EnumType.STRING)
	@Column(name = "bildungsstufe", nullable = false)
	private Bildungsstufe bildungsstufe;

	@Column(name = "bfs", nullable = false)
	private int bfs;

}
