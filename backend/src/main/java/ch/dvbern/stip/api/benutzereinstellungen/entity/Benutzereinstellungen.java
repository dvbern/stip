package ch.dvbern.stip.api.benutzereinstellungen.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "benutzereinstellungen",
    indexes = @Index(name = "IX_benutzereinstellungen_mandant", columnList = "mandant")
)
@Audited
@Getter
@Setter
public class Benutzereinstellungen extends AbstractMandantEntity {
    @NotNull
    @Column(name = "digitale_kommunikation", nullable = false)
    private boolean digitaleKommunikation = true;
}
