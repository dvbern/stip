package ch.dvbern.stip.api.statistik.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableDto {

    @XmlElement(name = "head")
    private HeadDto head;

    @XmlElement(name = "pers")
    private List<PersDto> persons;
}
