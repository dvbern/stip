package ch.dvbern.stip.api.eltern.entity;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Audited
@Entity
@IdentischerZivilrechtlicherWohnsitzRequiredConstraint
@Table(indexes = {
    @Index(name = "IX_eltern_adresse_id", columnList = "adresse_id"),
    @Index(name = "IX_eltern_mandant", columnList = "mandant")
})
@Getter
@Setter
@AhvIfSwissConstraint
public class Eltern extends AbstractPerson {

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_eltern_adresse_id"), nullable = false)
    private Adresse adresse;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ElternTyp elternTyp;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String telefonnummer;

    @NotNull
    @Column(nullable = false)
    private Integer wohnkosten;

    @NotNull
    @Column(nullable = false)
    private Boolean sozialhilfebeitraegeAusbezahlt;

    @NotNull
    @Column(nullable = false)
    private Boolean ausweisbFluechtling;

    @NotNull
    @Column(nullable = false)
    private Boolean ergaenzungsleistungAusbezahlt;

    @NotNull
    @Column(nullable = false)
    private boolean identischerZivilrechtlicherWohnsitz = true;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String identischerZivilrechtlicherWohnsitzOrt;

    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(nullable = true)
    private String identischerZivilrechtlicherWohnsitzPLZ;

    @Column(nullable = true)
    private UUID copyOfId;
}
