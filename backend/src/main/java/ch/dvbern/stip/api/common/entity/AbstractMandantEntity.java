package ch.dvbern.stip.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TenantId;
import org.hibernate.envers.Audited;

@MappedSuperclass
@Audited
@Getter
@Setter
public abstract class AbstractMandantEntity extends AbstractEntity {
    @TenantId
    @Column(nullable = false)
    private String mandant;
}
