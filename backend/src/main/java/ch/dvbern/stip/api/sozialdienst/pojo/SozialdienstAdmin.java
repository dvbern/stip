package ch.dvbern.stip.api.sozialdienst.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SozialdienstAdmin {
    private String keykloakId;
    private String vorname;
    private String nachname;
    private String email;
}
