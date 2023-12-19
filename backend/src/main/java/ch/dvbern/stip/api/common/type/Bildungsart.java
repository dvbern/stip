package ch.dvbern.stip.api.common.type;

import lombok.Getter;

import static ch.dvbern.stip.api.common.type.Ausbildungsstufe.SEKUNDAR_2;
import static ch.dvbern.stip.api.common.type.Ausbildungsstufe.TERTIAER;

@Getter
public enum Bildungsart {
    GYMNASIALE_MATURITAETSSCHULEN(2, SEKUNDAR_2),
    SCHULEN_FUER_ALLGEMEINBILDUNG(3, SEKUNDAR_2),
    VOLLZEITBERUFSSCHULEN(4, SEKUNDAR_2),
    LEHREN_ANLEHREN(5, SEKUNDAR_2),
    BERUFSMATURITAET_NACH_LEHRE(6, SEKUNDAR_2),
    HOEHERE_BERUFSBILDUNG(7, SEKUNDAR_2),
    FACHHOCHSCHULEN(8, TERTIAER),
    UNIVERSITAETEN_ETH(9, TERTIAER);

    private final int bfs;
    private final Ausbildungsstufe ausbildungsstufe;

    Bildungsart(int bfs, Ausbildungsstufe ausbildungsstufe) {
        this.bfs = bfs;
        this.ausbildungsstufe = ausbildungsstufe;
    }
}
