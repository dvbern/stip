package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "ausgaben")
@Getter
@Setter
public class Ausgaben extends AbstractEntity {
    private Integer steuernStaat;
    private Integer steuernBund;
    private Integer fahrkosten;
    private Integer fahrkostenPartner;
    private Integer verpflegung;
    private Integer verpflegungPartner;
    private Integer steuerjahr;
    private Integer veranlagungscode;
}
