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

    private static String encodeWithMax(
        final int numberToEncode,
        final Sqids sqids,
        final int maxInt,
        final int sqidsLength
    ) {
        if (numberToEncode > maxInt) {
            throw new IllegalArgumentException("numberToEncode is too large");
        }

        var encoded = sqids.encode(List.of((long) numberToEncode));
        if (encoded.length() > sqidsLength) {
            throwEncodedTooLong(numberToEncode, encoded, sqidsLength);
        }
        return encoded;
    }

    private static void throwEncodedTooLong(final int numberToEncode, final String encoded, final int sqidsLength) {
        throw new IllegalStateException(String.format(
            "This should be unreachable, trying to encode %s resulted in %s, SqidsLength: %s",
            numberToEncode,
            encoded,
            sqidsLength
        ));
    }

    public String encodeLengthFive(int numberToEncode) {
        return encodeWithMax(numberToEncode, sqidsLengthFive, 99_999, 5);
    }

    public String encodeLengthSix(int numberToEncode) {
        return encodeWithMax(numberToEncode, sqidsLengthSix, 999_999, 6);
    }
}
