package ch.dvbern.stip.lebenslauf.model;


import ch.dvbern.stip.gesuch.model.Gesuch;
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
public class LebenslaufItemContainer extends AbstractEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_lebenslauf_item_container_gesuch_id"))
    private Gesuch gesuch;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_lebenslauf_item_container_lebenslauf_item_gs_id"), nullable = true)
    private LebenslaufItem lebenslaufItemGS;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_lebenslauf_item_container_lebenslauf_item_sb_id"), nullable = true)
    private LebenslaufItem lebenslaufItemSB;

}
