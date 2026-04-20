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
public class FormationDto {
    @XmlElement(name = "formLevel")
    private Integer formLevel;

    @XmlElement(name = "matuProf")
    private Integer matuProf;

    @XmlElement(name = "diploma")
    private Integer diploma;

    @XmlElement(name = "task")
    private Integer task;

    @XmlElement(name = "firstForm")
    private Integer firstForm;
}
