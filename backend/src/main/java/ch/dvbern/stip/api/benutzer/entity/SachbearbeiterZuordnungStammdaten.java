package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    indexes = {@Index(name = "IX_sachbearbeiter_zuordnung_stammdaten_mandant", columnList = "mandant"),
        @Index(name = "IX_sachbearbeiter_zuordnung_stammdaten_benutzer_id", columnList = "benutzer_id")}
)
@Audited
@Getter
@Setter
public class SachbearbeiterZuordnungStammdaten extends AbstractMandantEntity {

    @Column
    @BuchstabenRangeConstraint
    private String buchstabenDe;

    @Column
    @BuchstabenRangeConstraint
    private String buchstabenFr;

    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_sachbearbeiter_zuordnung_stammdaten_benutzer_id"), nullable = false)
    private Benutzer benutzer;
}
