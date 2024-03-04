package ch.dvbern.stip.api.benutzer.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BenutzerTyp {
    GESUCHSTELLER("Gesuchsteller"),
    SACHBEARBEITER("Sachbearbeiter"),
    ADMIN("Admin");

    private final String roleName;
}
