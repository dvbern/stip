package ch.dvbern.stip.api.common.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import org.sqids.Sqids;

@ApplicationScoped
public class SqidsService {
    private final Sqids sqidsLengthFive;
    private final Sqids sqidsLengthSix;

    public SqidsService() {
        this.sqidsLengthFive = Sqids.builder().alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").minLength(5).build();
        this.sqidsLengthSix = Sqids.builder().alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").minLength(6).build();
    }

    public String encodeLenghtFive(int numberToEncode) {
        return sqidsLengthFive.encode(List.of((long) numberToEncode));
    }

    public String encodeLenghtSix(int numberToEncode) {
        return sqidsLengthSix.encode(List.of((long) numberToEncode));
    }
}
