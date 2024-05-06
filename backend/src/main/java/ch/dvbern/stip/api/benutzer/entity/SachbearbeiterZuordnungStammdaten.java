package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "sachbearbeiter_zuordnung_stammdaten",
    indexes = {
        @Index(name = "IX_sachbearbeiter_zuordnung_stammdaten_mandant", columnList = "mandant"),
        @Index(name = "IX_sachbearbeiter_zuordnung_stammdaten_benutzer_id", columnList = "benutzer_id")
    }
)
@Audited
@Getter
@Setter
public class SachbearbeiterZuordnungStammdaten extends AbstractMandantEntity {
    @Nullable
    @Column(name = "buchstaben_de")
    @BuchstabenRangeConstraint
    private String buchstabenDe;

    @Nullable
    @Column(name = "buchstaben_fr")
    @BuchstabenRangeConstraint
    private String buchstabenFr;

    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(name = "benutzer_id", foreignKey = @ForeignKey(name = "FK_sachbearbeiter_zuordnung_stammdaten_benutzer_id"), nullable = false)
    private Benutzer benutzer;
}
