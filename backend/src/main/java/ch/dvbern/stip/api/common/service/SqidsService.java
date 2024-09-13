package ch.dvbern.stip.api.common.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import org.sqids.Sqids;

@ApplicationScoped
public class SqidsService {
    private static final String UPPERCASE_ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final Sqids sqidsLengthFive;
    private final Sqids sqidsLengthSix;

    public SqidsService() {
        this.sqidsLengthFive = Sqids.builder().alphabet(UPPERCASE_ALPHANUMERIC).minLength(5).build();
        this.sqidsLengthSix = Sqids.builder().alphabet(UPPERCASE_ALPHANUMERIC).minLength(6).build();
    }

    public String encodeLengthFive(int numberToEncode) {
        if (numberToEncode > 99_999) {
            throw new IllegalArgumentException("numberToEncode is too large");
        }

        var encoded = sqidsLengthFive.encode(List.of((long) numberToEncode));
        if (encoded.length() > 5) {
            throwEncodedTooLong(numberToEncode, encoded);
        }
        return encoded;
    }

    public String encodeLengthSix(int numberToEncode) {
        if (numberToEncode > 999_999) {
            throw new IllegalArgumentException("numberToEncode is too large");
        }

        var encoded = sqidsLengthSix.encode(List.of((long) numberToEncode));
        if (encoded.length() > 6) {
            throwEncodedTooLong(numberToEncode, encoded);
        }
        return encoded;
    }

    void throwEncodedTooLong(final int numberToEncode, final String encoded) {
        throw new IllegalStateException(String.format(
            "This should be unreachable, trying to encode %s resulted in %s",
            numberToEncode,
            encoded
        ));
    }
}
