package ch.dvbern.stip.api.notiz.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;


@Entity
@Table(name="notizen")
public class Notiz extends AbstractEntity {
    @Max(100)
    @Column(name = "betreff")
    private String betreff;
    @Column(name = "text")
    private String text;
}
