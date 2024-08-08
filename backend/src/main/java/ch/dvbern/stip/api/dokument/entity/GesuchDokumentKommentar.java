package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

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

    @NotNull
    @Column(name = "benutzer")
    private String benutzer;

    @NotNull
    @Column(name = "datum")
    private LocalDate datum;
}
