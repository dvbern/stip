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
public class SumDto {
    @XmlElement(name = "sumId")
    private Integer sumId;

    @XmlElement(name = "sumTotal")
    private Integer sumTotal;

    @XmlElement(name = "sumArt")
    private Integer sumArt;

    @XmlElement(name = "term")
    private Integer term;

    @XmlElement(name = "com")
    private String com;
}
