package ch.dvbern.stip.ausbildung.entity;

import ch.dvbern.stip.persistence.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Getter
@Setter
public class AusbildungContainer extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildung_container_ausbildung_gs_id"), nullable = true)
    private Ausbildung ausbildungGS;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildung_container_ausbildung_sb_id"), nullable = true)
    private Ausbildung ausbildungSB;
}
