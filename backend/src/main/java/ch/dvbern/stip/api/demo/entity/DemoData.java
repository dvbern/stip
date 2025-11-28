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

package ch.dvbern.stip.api.demo.entity;

import java.io.IOException;
import java.io.StringWriter;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.generated.dto.DemoDataDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import software.amazon.awssdk.annotations.NotNull;

@Audited
@Entity
@Table(
    name = "darlehen",
    indexes = {
        @Index(name = "IX_darlehen_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class DemoData extends AbstractMandantEntity {
    @NotNull
    @Column(name = "test_fall", nullable = false)
    private String testFall;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Nullable
    @Column(name = "erfasser", nullable = true)
    private String erfasser;

    @NotNull
    @Column(name = "gesuchsjahr", nullable = false)
    private String gesuchsjahr;

    @NotNull
    @Column(name = "gesuchseingang", nullable = false)
    private String gesuchseingang;

    @NotNull
    @Column(name = "jsonData", nullable = false)
    private String jsonData;

    @Transient
    @Getter
    @Setter(AccessLevel.NONE)
    private DemoDataDto demoDataDto = new DemoDataDto();

    public void persistDemoData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, demoDataDto);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        jsonData = writer.toString();
    }

    public DemoDataDto parseDemoDataDto() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(jsonData, DemoDataDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error parsing stored demoData list", e);
        }
    }
}
