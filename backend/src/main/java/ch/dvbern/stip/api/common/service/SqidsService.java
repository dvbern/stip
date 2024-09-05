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

    public String encodeLenghtFive(int numberToEncode) {
        var encoded =  sqidsLengthFive.encode(List.of((long) numberToEncode));
        if (encoded.length() > 5) {
            // TODO: Handle cases where the encoded string length exceeds 5 characters
        }
        return encoded;
    }

    public String encodeLenghtSix(int numberToEncode) {
        var encoded = sqidsLengthSix.encode(List.of((long) numberToEncode));
        if (encoded.length() > 6) {

            // TODO: Handle cases where the encoded string length exceeds 6 characters
        }
        return encoded;
    }
}
