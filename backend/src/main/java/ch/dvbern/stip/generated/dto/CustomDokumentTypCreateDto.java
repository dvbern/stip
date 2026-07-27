package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("CustomDokumentTypCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class CustomDokumentTypCreateDto  implements Serializable {
  private @Valid String type;
  private @Valid String description;
  private @Valid UUID trancheId;

  protected CustomDokumentTypCreateDto(CustomDokumentTypCreateDtoBuilder<?, ?> b) {
    this.type = b.type;
    this.description = b.description;
    this.trancheId = b.trancheId;
  }

  public CustomDokumentTypCreateDto() {
  }

  /**
   **/
  public CustomDokumentTypCreateDto type(String type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public CustomDokumentTypCreateDto description(String description) {
    this.description = description;
    return this;
  }

  
  @JsonProperty("description")
  @NotNull
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public CustomDokumentTypCreateDto trancheId(UUID trancheId) {
    this.trancheId = trancheId;
    return this;
  }

  
  @JsonProperty("trancheId")
  @NotNull
  public UUID getTrancheId() {
    return trancheId;
  }

  @JsonProperty("trancheId")
  public void setTrancheId(UUID trancheId) {
    this.trancheId = trancheId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomDokumentTypCreateDto customDokumentTypCreate = (CustomDokumentTypCreateDto) o;
    return Objects.equals(this.type, customDokumentTypCreate.type) &&
        Objects.equals(this.description, customDokumentTypCreate.description) &&
        Objects.equals(this.trancheId, customDokumentTypCreate.trancheId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, trancheId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomDokumentTypCreateDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    trancheId: ").append(toIndentedString(trancheId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static CustomDokumentTypCreateDtoBuilder<?, ?> builder() {
    return new CustomDokumentTypCreateDtoBuilderImpl();
  }

  private static final class CustomDokumentTypCreateDtoBuilderImpl extends CustomDokumentTypCreateDtoBuilder<CustomDokumentTypCreateDto, CustomDokumentTypCreateDtoBuilderImpl> {

    @Override
    protected CustomDokumentTypCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public CustomDokumentTypCreateDto build() {
      return new CustomDokumentTypCreateDto(this);
    }
  }

  public static abstract class CustomDokumentTypCreateDtoBuilder<C extends CustomDokumentTypCreateDto, B extends CustomDokumentTypCreateDtoBuilder<C, B>>  {
    private String type;
    private String description;
    private UUID trancheId;
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
    public B trancheId(UUID trancheId) {
      this.trancheId = trancheId;
      return self();
    }
  }
}

