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
public class HeadDto {
    @XmlElement(name = "version")
    private Integer version;

    @XmlElement(name = "canton")
    private Integer canton;

    @XmlElement(name = "dataDelivery")
    private String dataDelivery;

    @XmlElement(name = "deliveryDate")
    private String deliveryDate;
}
