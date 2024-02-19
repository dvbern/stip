package ch.dvbern.stip.api.common.exception;

/**
 * Wir definieren unserem eigenen Exception als es ist besser zu unterscheiden die Exception die sind aus Java geworfen
 * mit die die sind bei uns im Code geworfen.
 * Spaeter kann man dieser Klasse mit properties ergänzen zu verbessern den Benutzer Feedback
 */
public class AppErrorException extends RuntimeException {

    public AppErrorException(String message) {
        super(message);
    }
}
