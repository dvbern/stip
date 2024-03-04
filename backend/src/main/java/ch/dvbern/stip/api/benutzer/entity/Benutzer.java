package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzer.type.BenutzerTyp;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Entity
@Table(
    indexes = {
        @Index(name = "IX_benutzer_keycloak_id", columnList = "keycloak_id", unique = true),
        @Index(name = "IX_benuter_mandant", columnList = "mandant"),
        @Index(name = "IX_benutzer_benutzereinstellungen_id", columnList = "benutzereinstellungen_id"),
    }
)
@Audited
@Getter
@Setter
public class Benutzer extends AbstractMandantEntity {

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "keycloak_id", nullable = true, unique = true)
    private String keycloakId;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String nachname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String vorname;

    @AhvConstraint(optional = true)
    @Column(nullable = true)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BenutzerStatus benutzerStatus;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BenutzerTyp benutzerTyp = BenutzerTyp.GESUCHSTELLER;

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_benutzer_benutzereinstellungen_id"), nullable = false)
    private @Valid Benutzereinstellungen benutzereinstellungen;
}
