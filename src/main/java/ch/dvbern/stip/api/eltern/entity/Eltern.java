package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Eltern extends AbstractEntity {

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String nachname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String vorname;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Anrede geschlecht;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String telefonnummer;

    @NotNull
    @Column(nullable = false)
    private LocalDate geburtsdatum;

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
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_eltern_container_gesuch_id"))
    private GesuchFormular gesuchFormular;
}
