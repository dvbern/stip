package ch.dvbern.stip.api.notiz.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="gesuch_notizen")
@Audited
public class GesuchNotiz extends AbstractMandantEntity {
    @NotNull
    @Column(name = "gesuchId")
    private UUID gesuchId;

    @Column(name = "betreff", length = 100)
    private String betreff;

    @Column(name = "text")
    private String text;
}
