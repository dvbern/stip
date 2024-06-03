package ch.dvbern.stip.api.scheduledtask.entity;

import java.time.LocalDateTime;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    name = "scheduledtask",
    indexes = {
        @Index(name = "IX_scheduledtask_last_execution", columnList = "last_execution"),
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
    @Column(name = "last_execution", nullable = false)
    private LocalDateTime lastExecution;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "json")
    private JsonNode payload;
}
