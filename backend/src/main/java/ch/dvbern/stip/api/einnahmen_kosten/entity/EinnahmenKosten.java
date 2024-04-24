package ch.dvbern.stip.api.einnahmen_kosten.entity;

import java.math.BigDecimal;

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
@Table(
    name = "einnahmen_kosten",
    indexes = @Index(name = "IX_einnahme_kosten_mandant", columnList = "mandant")
)
@Getter
@Setter
public class EinnahmenKosten extends AbstractMandantEntity {
    @NotNull
    @Column(name = "nettoerwerbseinkommen", nullable = false)
    private BigDecimal nettoerwerbseinkommen;

    @NotNull
    @Column(name = "fahrkosten", nullable = false)
    private BigDecimal fahrkosten;

    @Nullable
    @Column(name = "wohnkosten")
    private BigDecimal wohnkosten;

    @Nullable
    @Column(name = "wg_wohnend")
    private Boolean wgWohnend;

    @NotNull
    @Column(name = "verdienst_realisiert", nullable = false)
    private Boolean verdienstRealisiert;

    @Nullable
    @Column(name = "alimente")
    private BigDecimal alimente;

    @Nullable
    @Column(name = "zulagen")
    private BigDecimal zulagen;

    @Nullable
    @Column(name = "renten")
    private BigDecimal renten;

    @Nullable
    @Column(name = "eo_leistungen")
    private BigDecimal eoLeistungen;

    @Nullable
    @Column(name = "ergaenzungsleistungen")
    private BigDecimal ergaenzungsleistungen;

    @Nullable
    @Column(name = "beitraege")
    private BigDecimal beitraege;

    @Nullable
    @Column(name = "ausbildungskosten_sekundarstufe_zwei")
    private BigDecimal ausbildungskostenSekundarstufeZwei;

    @Nullable
    @Column(name = "ausbildungskosten_tertiaerstufe")
    private BigDecimal ausbildungskostenTertiaerstufe;

    @Nullable
    @Column(name = "will_darlehen")
    private Boolean willDarlehen;

    @Nullable
    @Column(name = "auswaertige_mittagessen_pro_woche")
    private Integer auswaertigeMittagessenProWoche;

    @Nullable
    @Column(name = "betreuungskosten_kinder")
    private BigDecimal betreuungskostenKinder;
}
