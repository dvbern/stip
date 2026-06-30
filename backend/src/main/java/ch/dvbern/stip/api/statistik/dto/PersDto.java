/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.statistik.dto;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@lombok.Builder
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class PersDto {
    @XmlElement(name = "personIdentificationRoot")
    private PersonIdentificationRootDto personIdentificationRoot;

    @XmlElement(name = "nationality")
    private Integer nationality;

    @XmlElement(name = "residencePermitCategory")
    private String residencePermitCategory;

    @XmlElement(name = "place")
    private Integer place;

    @XmlElement(name = "placeHist")
    private Integer placeHist;

    @XmlElement(name = "country")
    private Integer country;

    @XmlElement(name = "com")
    private String com;

    @XmlElement(name = "form")
    private List<FormDto> forms;
}
