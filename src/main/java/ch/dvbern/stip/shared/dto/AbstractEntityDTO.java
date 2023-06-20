package ch.dvbern.stip.shared.dto;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

//TODO abklaeren ob noetig
@Value
public class AbstractEntityDTO {
    private UUID id;

    private long version;

    private LocalDateTime timestampErstellt;

    private LocalDateTime timestampMutiert;

    private String userErstellt;

    private String userMutiert;
}
