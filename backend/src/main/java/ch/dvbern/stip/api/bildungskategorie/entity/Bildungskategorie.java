package ch.dvbern.stip.api.bildungskategorie.entity;

import ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Bildungskategorie extends AbstractEntity {
	@Column(name = "bezeichnung_de", nullable = false)
	private String bezeichnungDe;

    @Column(name = "bezeichnung_fr", nullable = false)
    private String bezeichnungFr;

	@Column(name = "bfs", nullable = false)
	private int bfs;

    public Bildungsstufe getBildungsstufe() {
        return bfs >= 7 ? Bildungsstufe.TERTIAER : Bildungsstufe.SEKUNDAR_2;
    }
}
