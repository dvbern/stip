package ch.dvbern.stip.api.scheduledtask.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "scheduledtask",
    indexes = {
        @Index(name = "IX_scheduledtask_timestamp", columnList = "timestamp"),
        @Index(name = "IX_scheduledtask_type", columnList = "type")
    }
)
@Audited
@Getter
@Setter
public class Scheduledtask extends AbstractEntity {
    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "json")
    private JsonNode payload;
}
