package ch.dvbern.stip.api.benutzer.entity;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Entity
@Table(
    name = "benutzer",
    indexes = {
        @Index(name = "IX_benutzer_keycloak_id", columnList = "keycloak_id", unique = true),
        @Index(name = "IX_benuter_mandant", columnList = "mandant"),
        @Index(name = "IX_benutzer_benutzereinstellungen_id", columnList = "benutzereinstellungen_id"),
    }
)
@Audited
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Benutzer extends AbstractMandantEntity {
    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "nachname", nullable = false)
    private String nachname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @AhvConstraint(optional = true)
    @Column(name = "sozialversicherungsnummer")
    @Nullable
    private String sozialversicherungsnummer;

    @NotNull
    @Column(name = "benutzer_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BenutzerStatus benutzerStatus;

    @ManyToMany
    @JoinTable(
        name = "benutzer_rollen",
        joinColumns = @JoinColumn(name = "benutzer_id"),
        foreignKey = @ForeignKey(name = "FK_benutzer_rollen"),
        inverseJoinColumns = @JoinColumn(name = "rolle_id"),
        inverseForeignKey = @ForeignKey(name = "FK_rolle_rollen")
    )
    private Set<Rolle> rollen = new HashSet<>();

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "benutzereinstellungen_id",
        foreignKey = @ForeignKey(name = "FK_benutzer_benutzereinstellungen_id"),
        nullable = false)
    private @Valid Benutzereinstellungen benutzereinstellungen;

    public String getFullName() {
        return getVorname() + " " + getNachname();
    }

    public boolean hasOneOfRoles(final Set<String> roleIds) {
        return getRollen().stream().anyMatch(rolle -> roleIds.contains(rolle.getKeycloakIdentifier()));
    }
    public boolean hasRole(final String roleId) {
        return getRollen().stream().anyMatch(rolle -> rolle.getKeycloakIdentifier().equals(roleId));
    }
}
