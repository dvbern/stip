package ch.dvbern.stip.api.common.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NACHNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VORNAME_NOTBLANK_MESSAGE;

@MappedSuperclass
@Audited
@Getter
@Setter
public abstract class AbstractPerson extends AbstractMandantEntity {
    @NotBlank(message = VALIDATION_NACHNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "nachname", nullable = false)
    private String nachname;

    @NotBlank(message = VALIDATION_VORNAME_NOTBLANK_MESSAGE)
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @NotNull
    @Column(name = "geburtsdatum", nullable = false)
    private LocalDate geburtsdatum;
}
