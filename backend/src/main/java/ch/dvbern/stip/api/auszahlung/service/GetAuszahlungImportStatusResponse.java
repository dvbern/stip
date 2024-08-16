package ch.dvbern.stip.api.auszahlung.service;


import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@XmlRootElement(name = "POSITION")
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAuszahlungImportStatusResponse {
    private String STATUS;
    @XmlElement(name="LOGS")
    private List<Log> LOGS;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Status: ").append(STATUS);
        builder.append("/n");
        LOGS.forEach(log -> builder.append(log.toString()));
        return builder.toString();
    }
}

