package ch.dvbern.stip.eltern.model;

import ch.dvbern.stip.gesuch.entity.Gesuch;
import ch.dvbern.stip.persistence.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Getter
@Setter
public class ElternContainer extends AbstractEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_eltern_container_gesuch_id"))
    private Gesuch gesuch;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_eltern_container_eltern_gs_id"), nullable = true)
    private Eltern elternGS;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_eltern_container_eltern_sb_id"), nullable = true)
    private Eltern elternSB;
}
