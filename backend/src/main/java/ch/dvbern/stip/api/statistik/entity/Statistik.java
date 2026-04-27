package ch.dvbern.stip.api.statistik.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Entity
@Audited
@Getter
@Setter
@Table(
    name = "statistik",
    indexes = {
        @Index(name = "IX_statistik_mandant", columnList = "mandant"),
        @Index(name = "IX_statistik_year", columnList = "year")
    }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistik extends AbstractMandantEntity {
    @NotNull
    @Column(name = "year", nullable = false)
    private int year;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filepath", nullable = false)
    private String filepath;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filesize", nullable = false)
    private int filesize;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "object_id", nullable = false)
    private String objectId;
}
