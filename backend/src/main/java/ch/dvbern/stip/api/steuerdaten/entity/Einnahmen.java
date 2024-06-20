package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "einnahmen")
@Getter
@Setter
public class Einnahmen extends AbstractEntity {
    private Integer totalEinkuenfte;
    private Integer eigenmietwert;
    private Boolean isArbeitsverhaeltnisSelbstaendig;
    private Integer saeule3a;
    private Integer saeule2;
    private Integer kinderalimente;
    private Integer ergaenzungsleistungen;
    private Integer vermoegen;
}
