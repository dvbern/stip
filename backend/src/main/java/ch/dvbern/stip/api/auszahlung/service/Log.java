package ch.dvbern.stip.api.auszahlung.service;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Getter
@Setter
public class Log{
    private String DATETIME;
    private String MESSAGE;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(DATETIME);
        builder.append("/n");
        builder.append(MESSAGE);
        builder.append("/n");
        return builder.toString();    }
}
