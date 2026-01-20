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
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import software.amazon.awssdk.annotations.NotNull;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_TESTCASE_JSON_DATA_LENGTH;

@Audited
@Entity
@Table(
    name = "demo_data",
    indexes = {
        @Index(name = "IX_demo_data_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class DemoData extends AbstractMandantEntity {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "test_fall", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String testFall;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "name", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String name;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "description", nullable = false, length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String description;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "erfasser", nullable = true, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String erfasser;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "gesuchsjahr", nullable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String gesuchsjahr;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "gesuchseingang", nullable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String gesuchseingang;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_TESTCASE_JSON_DATA_LENGTH)
    @Column(name = "json_data", nullable = false, length = DB_DEFAULT_STRING_TESTCASE_JSON_DATA_LENGTH)
    private String jsonData;

    @Transient
    @Getter
    @Setter(AccessLevel.NONE)
    private DemoDataDto demoDataDto = new DemoDataDto();

    public void serializeDemoData() {
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
