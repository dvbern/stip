package ch.dvbern.stip.api.notiz.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

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

    @Column(name = "betreff")
    @Size(max = DB_DEFAULT_MAX_LENGTH)

    private String betreff;

    @Column(name = "text")
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    private String text;
}
