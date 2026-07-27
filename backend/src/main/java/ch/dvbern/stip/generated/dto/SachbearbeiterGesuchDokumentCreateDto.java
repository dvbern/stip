package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SachbearbeiterGesuchDokumentCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SachbearbeiterGesuchDokumentCreateDto  implements Serializable {
  private @Valid String type;
  private @Valid String description;

  protected SachbearbeiterGesuchDokumentCreateDto(SachbearbeiterGesuchDokumentCreateDtoBuilder<?, ?> b) {
    this.type = b.type;
    this.description = b.description;
  }

  public SachbearbeiterGesuchDokumentCreateDto() {
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentCreateDto type(String type) {
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
  public SachbearbeiterGesuchDokumentCreateDto description(String description) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterGesuchDokumentCreateDto sachbearbeiterGesuchDokumentCreate = (SachbearbeiterGesuchDokumentCreateDto) o;
    return Objects.equals(this.type, sachbearbeiterGesuchDokumentCreate.type) &&
        Objects.equals(this.description, sachbearbeiterGesuchDokumentCreate.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterGesuchDokumentCreateDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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


  public static SachbearbeiterGesuchDokumentCreateDtoBuilder<?, ?> builder() {
    return new SachbearbeiterGesuchDokumentCreateDtoBuilderImpl();
  }

  private static final class SachbearbeiterGesuchDokumentCreateDtoBuilderImpl extends SachbearbeiterGesuchDokumentCreateDtoBuilder<SachbearbeiterGesuchDokumentCreateDto, SachbearbeiterGesuchDokumentCreateDtoBuilderImpl> {

    @Override
    protected SachbearbeiterGesuchDokumentCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SachbearbeiterGesuchDokumentCreateDto build() {
      return new SachbearbeiterGesuchDokumentCreateDto(this);
    }
  }

  public static abstract class SachbearbeiterGesuchDokumentCreateDtoBuilder<C extends SachbearbeiterGesuchDokumentCreateDto, B extends SachbearbeiterGesuchDokumentCreateDtoBuilder<C, B>>  {
    private String type;
    private String description;
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
  }
}

