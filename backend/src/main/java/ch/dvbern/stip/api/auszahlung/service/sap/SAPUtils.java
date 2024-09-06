package ch.dvbern.stip.api.auszahlung.service.sap;

import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class SAPUtils {
    public BigDecimal generateDeliveryId(){
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextLong(19));
    }

    public String generateExtId(){
        return UUID.randomUUID().toString();
    }

}
