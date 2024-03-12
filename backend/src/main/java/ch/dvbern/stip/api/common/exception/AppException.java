package ch.dvbern.stip.api.common.exception;


import ch.dvbern.oss.commons.i18nl10n.I18nMessage;
import ch.dvbern.stip.api.common.util.DebugUtil;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serial;

@Getter
@SuppressWarnings("AbstractClassExtendsConcreteClass")
public abstract class AppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -30027164914702837L;

    private final transient I18nMessage i18nMessage;

    /**
     * An id that uniquely identifies this exception.
     * <br>
     * It is intended to find error messages in (e.g.) logs
     * and might be presented to the user (and thus: provided to telephone-support)
     */
    private final ExceptionId id;

    protected AppException(
        I18nMessage message,
        ExceptionId id,
        @Nullable Throwable cause
    ) {
        super(buildRawMessage(message, id), cause);
        this.i18nMessage = message;
        this.id = id;
    }

    private static String buildRawMessage(I18nMessage i18nMessage, ExceptionId id) {
        var result = i18nMessage.key().value();
        if (!i18nMessage.args().isEmpty()) {
            result += ", args: " + DebugUtil.prettyPrintMap(i18nMessage.args());
        }
        result += ", id: " + id;

        return result;
    }

}
