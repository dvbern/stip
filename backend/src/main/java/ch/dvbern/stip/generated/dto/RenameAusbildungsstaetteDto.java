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



@JsonTypeName("RenameAusbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class RenameAusbildungsstaetteDto  implements Serializable {
  private @Valid String nameDe;
  private @Valid String nameFr;

  protected RenameAusbildungsstaetteDto(RenameAusbildungsstaetteDtoBuilder<?, ?> b) {
    this.nameDe = b.nameDe;
    this.nameFr = b.nameFr;
  }

  public RenameAusbildungsstaetteDto() {
  }

  /**
   **/
  public RenameAusbildungsstaetteDto nameDe(String nameDe) {
    this.nameDe = nameDe;
    return this;
  }

  
  @JsonProperty("nameDe")
  @NotNull
  public String getNameDe() {
    return nameDe;
  }

  @JsonProperty("nameDe")
  public void setNameDe(String nameDe) {
    this.nameDe = nameDe;
  }

  /**
   **/
  public RenameAusbildungsstaetteDto nameFr(String nameFr) {
    this.nameFr = nameFr;
    return this;
  }

  
  @JsonProperty("nameFr")
  @NotNull
  public String getNameFr() {
    return nameFr;
  }

  @JsonProperty("nameFr")
  public void setNameFr(String nameFr) {
    this.nameFr = nameFr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RenameAusbildungsstaetteDto renameAusbildungsstaette = (RenameAusbildungsstaetteDto) o;
    return Objects.equals(this.nameDe, renameAusbildungsstaette.nameDe) &&
        Objects.equals(this.nameFr, renameAusbildungsstaette.nameFr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameDe, nameFr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RenameAusbildungsstaetteDto {\n");
    
    sb.append("    nameDe: ").append(toIndentedString(nameDe)).append("\n");
    sb.append("    nameFr: ").append(toIndentedString(nameFr)).append("\n");
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


  public static RenameAusbildungsstaetteDtoBuilder<?, ?> builder() {
    return new RenameAusbildungsstaetteDtoBuilderImpl();
  }

  private static final class RenameAusbildungsstaetteDtoBuilderImpl extends RenameAusbildungsstaetteDtoBuilder<RenameAusbildungsstaetteDto, RenameAusbildungsstaetteDtoBuilderImpl> {

    @Override
    protected RenameAusbildungsstaetteDtoBuilderImpl self() {
      return this;
    }

    @Override
    public RenameAusbildungsstaetteDto build() {
      return new RenameAusbildungsstaetteDto(this);
    }
  }

  public static abstract class RenameAusbildungsstaetteDtoBuilder<C extends RenameAusbildungsstaetteDto, B extends RenameAusbildungsstaetteDtoBuilder<C, B>>  {
    private String nameDe;
    private String nameFr;
    protected abstract B self();

    public abstract C build();

    public B nameDe(String nameDe) {
      this.nameDe = nameDe;
      return self();
    }
    public B nameFr(String nameFr) {
      this.nameFr = nameFr;
      return self();
    }
  }
}

