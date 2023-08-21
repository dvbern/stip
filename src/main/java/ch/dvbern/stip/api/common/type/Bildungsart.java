package ch.dvbern.stip.api.common.type;

public enum Bildungsart {
    BERUFSVORBEREITENDES_SCHULJAHR_VORLEHRE(2),
    GYMNASIALE_MATURITAETSSCHULEN(2),
    SCHULEN_FUER_ALLGEMEINBILDUNG(2),
    VOLLZEITBERUFSSCHULEN(2),
    LEHREN_ANLEHREN(2),
    BERUFSMATURITAET_NACH_LEHRE(2),
    HOEHERE_BERUFSBILDUNG(2),
    FACHHOCHSCHULEN(3),
    UNIVERSITAETEN_ETH(3);

    private final int level;

    Bildungsart(int level) {
        this.level = level;
    }
}
