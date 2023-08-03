package ch.dvbern.stip.api.common.entity;

import java.io.Serial;
import java.util.UUID;

public class ReferenceEntity extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = -5378715716527748758L;

    public ReferenceEntity(UUID id) {
        this.setId(id);
    }
}
