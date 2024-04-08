package ch.dvbern.stip.api.gesuchsjahr.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(indexes = @Index(name = "IX_gesuchsjahr_mandant", columnList = "mandant"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gesuchsjahr extends AbstractMandantEntity {

    @Column(name = "bezeichnung_de")
    private String bezeichnungDe;

    @Column(name = "bezeichnung_fr")
    private String bezeichnungFr;

    @Column(name = "technisches_jahr")
    private Integer technischesJahr;

    @Enumerated(EnumType.STRING)
    @Column(name = "gueltigkeit_status")
    private GueltigkeitStatus gueltigkeitStatus;
}
