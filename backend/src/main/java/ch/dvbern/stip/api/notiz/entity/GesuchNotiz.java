package ch.dvbern.stip.api.notiz.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


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
    @ManyToOne
    @JoinColumn(name = "gesuch_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_notiz_gesuch_id"))
    private Gesuch gesuch;

    @Column(name = "betreff")
    @Size(max = DB_DEFAULT_MAX_LENGTH)

    private String betreff;

    @Column(name = "text")
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    private String text;
}
