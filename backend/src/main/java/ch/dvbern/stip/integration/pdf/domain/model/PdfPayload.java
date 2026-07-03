package ch.dvbern.stip.integration.pdf.domain.model;

import java.io.Serializable;

import ch.dvbern.stip.api.config.type.TenantAdapterConfig;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    public String toJson(final ObjectMapper objectMapper, final TenantAdapterConfig.PdfAdapter tenantAdapterConfig) throws JsonProcessingException {
        final ObjectNode node = objectMapper.createObjectNode();
        node.put("lang", lang.getLocale().toLanguageTag());
        node.put("template", tenantAdapterConfig.templatePath().get(template));
        node.set("data", objectMapper.valueToTree(data));
        return objectMapper.writeValueAsString(node);
    }
}
