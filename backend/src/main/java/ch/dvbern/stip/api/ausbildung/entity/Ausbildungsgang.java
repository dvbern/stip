package ch.dvbern.stip.api.ausbildung.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.Bildungsart;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Table(indexes = {
    @Index(name = "IX_ausbildungsgang_mandant", columnList = "mandant"),
    @Index(name = "IX_ausbildungsgang_ausbildungsstaette_id", columnList = "ausbildungsstaette_id")
})
@Getter
@Setter
public class Ausbildungsgang extends AbstractMandantEntity {

    @NotNull
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildungsgang_ausbildungsstaette_id"), nullable = true)
    private Ausbildungsstaette ausbildungsstaette;

    @NotNull
    @Column(nullable = false)
    private String ausbildungsort;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String bezeichnungDe;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String bezeichnungFr;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bildungsart ausbildungsrichtung;
}
