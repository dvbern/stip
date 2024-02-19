package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.entity.DateRange;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(indexes = {
    @Index(name = "IX_gesuch_tranche_gesuch_id", columnList = "gesuch_id"),
    @Index(name = "IX_gesuch_tranche_gesuch_formular_id", columnList = "gesuch_formular_id")
})
@Getter
@Setter
public class GesuchTranche extends AbstractEntity {

    @NotNull
    @Embedded
    private @Valid DateRange gueltigkeit = new DateRange();

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_formular_id"), nullable = true)
    private @Valid GesuchFormular gesuchFormular;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_id"), nullable = false)
    private Gesuch gesuch;
}
