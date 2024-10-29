package ch.dvbern.stip.api.benutzer.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Getter
@Setter
public class SozialdienstAdmin extends Benutzer{
    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "eMail", nullable = true)
    private String eMail;
}
