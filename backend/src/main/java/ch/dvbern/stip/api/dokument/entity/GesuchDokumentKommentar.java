package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
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
        @Index(name = "IX_gesuch_dokument_kommentar_gesuch_id", columnList = "gesuch_id"),
        @Index(name = "IX_gesuch_dokument_kommentar_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class GesuchDokumentKommentar extends AbstractMandantEntity {
    @NotNull
    @Column(name = "dokument_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private DokumentTyp dokumentTyp;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_kommentar_gesuch_id"))
    private Gesuch gesuch;

    @NotNull
    @Column(name = "dokumentstatus")
    @Enumerated(EnumType.STRING)
    private Dokumentstatus dokumentstatus;

    @NotNull
    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(name = "kommentar")
    private String kommentar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "benutzer_id",
        foreignKey = @ForeignKey(name = "FK_gesuchdokumentkommentar_benutzer_id"),
        nullable = false
    )
    private Benutzer benutzer;
}
