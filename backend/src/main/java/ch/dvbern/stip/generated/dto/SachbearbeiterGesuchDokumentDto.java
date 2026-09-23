package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SachbearbeiterGesuchDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SachbearbeiterGesuchDokumentDto  implements Serializable {
  private String type;
  private String description;
  private UUID id;
  private UUID gesuchId;
  private @Valid List<@Valid DokumentDto> dokumente = new ArrayList<>();

  protected SachbearbeiterGesuchDokumentDto(SachbearbeiterGesuchDokumentDtoBuilder<?, ?> b) {
    this.type = b.type;
    this.description = b.description;
    this.id = b.id;
    this.gesuchId = b.gesuchId;
    this.dokumente = b.dokumente;
  }

  public SachbearbeiterGesuchDokumentDto() {
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto type(String type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty(required = true, value = "type")
  @NotNull public String getType() {
    return type;
  }

  @JsonProperty(required = true, value = "type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto description(String description) {
    this.description = description;
    return this;
  }

  
  @JsonProperty(required = true, value = "description")
  @NotNull public String getDescription() {
    return description;
  }

  @JsonProperty(required = true, value = "description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty(required = true, value = "id")
  @NotNull public UUID getId() {
    return id;
  }

  @JsonProperty(required = true, value = "id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchId")
  @NotNull public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty(required = true, value = "gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto dokumente(List<@Valid DokumentDto> dokumente) {
    this.dokumente = dokumente;
    return this;
  }

  
  @JsonProperty(required = true, value = "dokumente")
  @NotNull @Valid public List<@Valid DokumentDto> getDokumente() {
    return dokumente;
  }

  @JsonProperty(required = true, value = "dokumente")
  public void setDokumente(List<@Valid DokumentDto> dokumente) {
    this.dokumente = dokumente;
  }

  public SachbearbeiterGesuchDokumentDto addDokumenteItem(DokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public SachbearbeiterGesuchDokumentDto removeDokumenteItem(DokumentDto dokumenteItem) {
    if (dokumenteItem != null && this.dokumente != null) {
      this.dokumente.remove(dokumenteItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterGesuchDokumentDto sachbearbeiterGesuchDokument = (SachbearbeiterGesuchDokumentDto) o;
    return Objects.equals(this.type, sachbearbeiterGesuchDokument.type) &&
        Objects.equals(this.description, sachbearbeiterGesuchDokument.description) &&
        Objects.equals(this.id, sachbearbeiterGesuchDokument.id) &&
        Objects.equals(this.gesuchId, sachbearbeiterGesuchDokument.gesuchId) &&
        Objects.equals(this.dokumente, sachbearbeiterGesuchDokument.dokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, id, gesuchId, dokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterGesuchDokumentDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static SachbearbeiterGesuchDokumentDtoBuilder<?, ?> builder() {
    return new SachbearbeiterGesuchDokumentDtoBuilderImpl();
  }

  private static final class SachbearbeiterGesuchDokumentDtoBuilderImpl extends SachbearbeiterGesuchDokumentDtoBuilder<SachbearbeiterGesuchDokumentDto, SachbearbeiterGesuchDokumentDtoBuilderImpl> {

    @Override
    protected SachbearbeiterGesuchDokumentDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SachbearbeiterGesuchDokumentDto build() {
      return new SachbearbeiterGesuchDokumentDto(this);
    }
  }

  public static abstract class SachbearbeiterGesuchDokumentDtoBuilder<C extends SachbearbeiterGesuchDokumentDto, B extends SachbearbeiterGesuchDokumentDtoBuilder<C, B>>  {
    private String type;
    private String description;
    private UUID id;
    private UUID gesuchId;
    private List<DokumentDto> dokumente = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B type(String type) {
      this.type = type;
      return self();
    }
    public B description(String description) {
      this.description = description;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B gesuchId(UUID gesuchId) {
      this.gesuchId = gesuchId;
      return self();
    }
    public B dokumente(List<DokumentDto> dokumente) {
      this.dokumente = dokumente;
      return self();
    }
  }
}
