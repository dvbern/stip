package ch.dvbern.stip.api.statistik.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalPersonIdDto {
    @XmlElement(name = "personIdCategory", namespace = "http://www.ech.ch/xmlns/eCH-0044/1")
    private String personIdCategory;

    @XmlElement(name = "personId", namespace = "http://www.ech.ch/xmlns/eCH-0044/1")
    private String personId;
}
