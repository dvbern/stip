package ch.dvbern.stip.api.auszahlung.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAuszahlungImportStatusRequest {
    private @Valid @NotNull Integer deliveryId;
    private @Valid @NotNull Integer sysId;
}
