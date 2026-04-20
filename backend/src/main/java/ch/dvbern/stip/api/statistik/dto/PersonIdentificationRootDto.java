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
public class PersonIdentificationRootDto {
    @XmlElement(name = "localPersonId")
    private LocalPersonIdDto localPersonId;

    @XmlElement(name = "sex")
    private Integer sex;

    @XmlElement(name = "dateOfBirth")
    private String dateOfBirth;
}
