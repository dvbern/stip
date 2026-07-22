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



@JsonTypeName("AusbildungsgangCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungsgangCreateDto  implements Serializable {
  private @Valid UUID abschlussId;
  private @Valid UUID ausbildungsstaetteId;

  protected AusbildungsgangCreateDto(AusbildungsgangCreateDtoBuilder<?, ?> b) {
    this.abschlussId = b.abschlussId;
    this.ausbildungsstaetteId = b.ausbildungsstaetteId;
  }

  public AusbildungsgangCreateDto() {
  }

  /**
   **/
  public AusbildungsgangCreateDto abschlussId(UUID abschlussId) {
    this.abschlussId = abschlussId;
    return this;
  }

  
  @JsonProperty("abschlussId")
  @NotNull
  public UUID getAbschlussId() {
    return abschlussId;
  }

  @JsonProperty("abschlussId")
  public void setAbschlussId(UUID abschlussId) {
    this.abschlussId = abschlussId;
  }

  /**
   **/
  public AusbildungsgangCreateDto ausbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
    return this;
  }

  
  @JsonProperty("ausbildungsstaetteId")
  @NotNull
  public UUID getAusbildungsstaetteId() {
    return ausbildungsstaetteId;
  }

  @JsonProperty("ausbildungsstaetteId")
  public void setAusbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsgangCreateDto ausbildungsgangCreate = (AusbildungsgangCreateDto) o;
    return Objects.equals(this.abschlussId, ausbildungsgangCreate.abschlussId) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgangCreate.ausbildungsstaetteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abschlussId, ausbildungsstaetteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangCreateDto {\n");
    
    sb.append("    abschlussId: ").append(toIndentedString(abschlussId)).append("\n");
    sb.append("    ausbildungsstaetteId: ").append(toIndentedString(ausbildungsstaetteId)).append("\n");
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


  public static AusbildungsgangCreateDtoBuilder<?, ?> builder() {
    return new AusbildungsgangCreateDtoBuilderImpl();
  }

  private static final class AusbildungsgangCreateDtoBuilderImpl extends AusbildungsgangCreateDtoBuilder<AusbildungsgangCreateDto, AusbildungsgangCreateDtoBuilderImpl> {

    @Override
    protected AusbildungsgangCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungsgangCreateDto build() {
      return new AusbildungsgangCreateDto(this);
    }
  }

  public static abstract class AusbildungsgangCreateDtoBuilder<C extends AusbildungsgangCreateDto, B extends AusbildungsgangCreateDtoBuilder<C, B>>  {
    private UUID abschlussId;
    private UUID ausbildungsstaetteId;
    protected abstract B self();

    public abstract C build();

    public B abschlussId(UUID abschlussId) {
      this.abschlussId = abschlussId;
      return self();
    }
    public B ausbildungsstaetteId(UUID ausbildungsstaetteId) {
      this.ausbildungsstaetteId = ausbildungsstaetteId;
      return self();
    }
  }
}

