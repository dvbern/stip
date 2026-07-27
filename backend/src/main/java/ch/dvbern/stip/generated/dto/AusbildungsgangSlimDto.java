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



@JsonTypeName("AusbildungsgangSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungsgangSlimDto  implements Serializable {
  private @Valid UUID id;
  private @Valid UUID abschlussId;
  private @Valid UUID ausbildungsstaetteId;

  protected AusbildungsgangSlimDto(AusbildungsgangSlimDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.abschlussId = b.abschlussId;
    this.ausbildungsstaetteId = b.ausbildungsstaetteId;
  }

  public AusbildungsgangSlimDto() {
  }

  /**
   **/
  public AusbildungsgangSlimDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public AusbildungsgangSlimDto abschlussId(UUID abschlussId) {
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
  public AusbildungsgangSlimDto ausbildungsstaetteId(UUID ausbildungsstaetteId) {
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
    AusbildungsgangSlimDto ausbildungsgangSlim = (AusbildungsgangSlimDto) o;
    return Objects.equals(this.id, ausbildungsgangSlim.id) &&
        Objects.equals(this.abschlussId, ausbildungsgangSlim.abschlussId) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgangSlim.ausbildungsstaetteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, abschlussId, ausbildungsstaetteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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


  public static AusbildungsgangSlimDtoBuilder<?, ?> builder() {
    return new AusbildungsgangSlimDtoBuilderImpl();
  }

  private static final class AusbildungsgangSlimDtoBuilderImpl extends AusbildungsgangSlimDtoBuilder<AusbildungsgangSlimDto, AusbildungsgangSlimDtoBuilderImpl> {

    @Override
    protected AusbildungsgangSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungsgangSlimDto build() {
      return new AusbildungsgangSlimDto(this);
    }
  }

  public static abstract class AusbildungsgangSlimDtoBuilder<C extends AusbildungsgangSlimDto, B extends AusbildungsgangSlimDtoBuilder<C, B>>  {
    private UUID id;
    private UUID abschlussId;
    private UUID ausbildungsstaetteId;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
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

