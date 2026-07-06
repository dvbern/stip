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

package ch.dvbern.stip.integration.pdf.domain.model;

import java.io.Serializable;

import ch.dvbern.stip.api.config.type.TenantAdapterConfig;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.annotations.NotNull;

@Data
@Builder
public class PdfPayload<T> implements Serializable {

    @NotNull
    @JsonIgnore
    private Sprache lang;

    @NotNull
    @JsonIgnore
    private PdfTemplateType template;

    @Valid
    @NotNull
    @JsonIgnore
    private T data;

    public String toJson(final ObjectMapper objectMapper, final TenantAdapterConfig.PdfAdapter tenantAdapterConfig)
    throws JsonProcessingException {
        final ObjectNode node = objectMapper.createObjectNode();
        node.put("lang", lang.getLocale().toLanguageTag());
        node.put("template", tenantAdapterConfig.templatePath().get(template));
        node.set("payload", objectMapper.valueToTree(data));
        return objectMapper.writeValueAsString(node);
    }
}
