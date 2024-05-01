package ch.dvbern.stip.api.einnahmen_kosten.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(indexes = @Index(name = "IX_einnahme_kosten_mandant", columnList = "mandant"))
@Getter
@Setter
public class EinnahmenKosten extends AbstractMandantEntity {
    @NotNull
    @Column(nullable = false)
    private Integer nettoerwerbseinkommen;
    @NotNull
    @Column(nullable = false)
    private Integer fahrkosten;
    @Nullable
    @Column(nullable = true)
    private Integer wohnkosten;
    @Nullable
    @Column(nullable = true)
    private Boolean wgWohnend;
    @NotNull
    @Column(nullable = false)
    private Boolean verdienstRealisiert;
    @Column
    private Integer alimente;
    @Column
    private Integer zulagen;
    @Column
    private Integer renten;
    @Column
    private Integer eoLeistungen;
    @Column
    private Integer ergaenzungsleistungen;
    @Column
    private Integer beitraege;
    @Column
    private Integer ausbildungskostenSekundarstufeZwei;
    @Column
    private Integer ausbildungskostenTertiaerstufe;
    @Column
    private Boolean willDarlehen;
    @Column
    private Integer auswaertigeMittagessenProWoche;
    @Column
    private Integer betreuungskostenKinder;
}
