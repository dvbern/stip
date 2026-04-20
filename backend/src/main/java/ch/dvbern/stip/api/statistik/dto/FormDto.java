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
public class FormDto {
    @XmlElement(name = "formId")
    private Integer formId;

    @XmlElement(name = "formation")
    private FormationDto formation;

    @XmlElement(name = "instIdentificationRoot")
    private InstIdentificationRootDto instIdentificationRoot;

    @XmlElement(name = "formPlace")
    private Integer formPlace;

    @XmlElement(name = "com")
    private String com;

    @XmlElement(name = "sum")
    private List<SumDto> sums;
}
