package ch.dvbern.stip.api.benutzereinstellungen.entity;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = "benutzereinstellungen")
@Table(indexes = {
    @Index(name = "IX_benutzereinstellungen_benutzer_id", columnList = "benutzer_id"),
    @Index(name = "IX_benutzereinstellungen_mandant", columnList = "mandant")
})
@Audited
@Getter
@Setter
public class Benutzereinstellungen extends AbstractMandantEntity {

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_benutzereinstellungen_benutzer_id"), nullable = false)
    private @Valid Benutzer benutzer;

    @NotNull
    @Column(nullable = false)
    private boolean digitaleKommunikation = true;
}
