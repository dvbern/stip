package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import jakarta.annotation.Nullable;
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

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Audited
@Entity
@Table(
    name = "gesuch_dokument_kommentar",
    indexes = {
        @Index(name = "IX_gesuch_dokument_kommentar_gesuch_id", columnList = "gesuch_tranche_id"),
        @Index(name = "IX_gesuch_dokument_kommentar_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class GesuchDokumentKommentar extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "gesuch_tranche_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_kommentar_gesuch_tranche_id"))
    private GesuchTranche gesuchTranche;

    @NotNull
    @Column(name = "dokument_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private DokumentTyp dokumentTyp;

    @NotNull
    @Column(name = "dokumentstatus")
    @Enumerated(EnumType.STRING)
    private Dokumentstatus dokumentstatus;

    @Nullable
    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(name = "kommentar", nullable = true)
    private String kommentar;
}
