package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Index(name = "IX_dokument_gesuch_dokument_id", columnList = "gesuch_dokument_id"),
    @Index(name = "IX_dokument_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Dokument extends AbstractMandantEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_dokument_gesuch_dokument_id"), nullable = false)
    private GesuchDokument gesuchDokument;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String filename;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String filepfad;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String filesize;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String objectId;
}
