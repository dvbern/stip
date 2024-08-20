package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeAuszahlungKreditor {
    private @Valid @NotNull Integer businessPartnerId;
    private @Valid @NotNull Integer deliveryId;
    private @Valid @NotNull Integer extId;
    private @Valid @NotNull Auszahlung auszahlung;
    private @Valid @NotNull Integer sysId;
}
