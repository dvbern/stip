package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.DateRange;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_id"), nullable = false)
    private Gesuch gesuch;
}
