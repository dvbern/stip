package ch.dvbern.stip.api.notiz.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Getter
@Setter
@Entity
@Audited
@Table(
    name = "gesuch_notizen",
    indexes = {
        @Index(name = "IX_gesuch_notiz_mandant", columnList = "mandant")
    }
)
public class GesuchNotiz extends AbstractMandantEntity {
    @NotNull
    @Column(name = "gesuch_id")
    private UUID gesuchId;

    @Column(name = "betreff", length = 100)
    private String betreff;

    @Column(name = "text")
    private String text;
}
