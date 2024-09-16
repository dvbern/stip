package ch.dvbern.stip.api.common.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import org.sqids.Sqids;

@ApplicationScoped
public class SqidsService {
    private final Sqids sqidsLengthFive;
    private final Sqids sqidsLengthSix;
    private final String UPPERCASE_ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public SqidsService() {
        this.sqidsLengthFive = Sqids.builder().alphabet(UPPERCASE_ALPHANUMERIC).minLength(5).build();
        this.sqidsLengthSix = Sqids.builder().alphabet(UPPERCASE_ALPHANUMERIC).minLength(6).build();
    }

    public String encodeWithMaxLength(int numberToEncode, int maximumLength) {
        var encoded =  sqidsLengthFive.encode(List.of((long) numberToEncode));
        if (encoded.length() >= maximumLength) {
            throw new IllegalArgumentException("Sqids length exceeds maximum length");
        }
        return encoded;
    }

    public String encodeLenghtFive(int numberToEncode) {
        return encodeWithMaxLength(numberToEncode, 5);
    }

    public String encodeLenghtSix(int numberToEncode) {
        return encodeWithMaxLength(numberToEncode, 6);
    }
}
