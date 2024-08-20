package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuszahlung {
    private @Valid Integer deliveryId;
    private @Valid @NotNull Integer vendorNo;
    private @Valid String positionsText;
    private @Valid String zahlungszweck;

    private @Valid @NotNull Auszahlung auszahlung;
    private @Valid @NotNull Integer sysId;

    private String docDate;
    private String pstingDate;
}
