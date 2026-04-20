package ch.dvbern.stip.api.statistik.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PersDto {
    @XmlElement(name = "personIdentificationRoot")
    private PersonIdentificationRootDto personIdentificationRoot;

    @XmlElement(name = "nationality")
    private Integer nationality;

    @XmlElement(name = "residencePermitCategory")
    private String residencePermitCategory;

    @XmlElement(name = "place")
    private Integer place;

    @XmlElement(name = "com")
    private String com;

    @XmlElement(name = "form")
    private List<FormDto> forms;
}
