package ch.dvbern.stip.api.dokument.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Table(
    name = "dokument",
    indexes = {
        @Index(name = "IX_dokument_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Dokument extends AbstractMandantEntity {
    @NotNull
    @ManyToMany(mappedBy = "dokumente")
    private List<GesuchDokument> gesuchDokumente = new ArrayList<>();

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "filepath", nullable = false)
    private String filepath;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "filesize", nullable = false)
    private String filesize;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "object_id", nullable = false)
    private String objectId;
}
