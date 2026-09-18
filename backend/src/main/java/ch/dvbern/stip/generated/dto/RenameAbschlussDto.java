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



@JsonTypeName("RenameAbschluss")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class RenameAbschlussDto  implements Serializable {
  private String bezeichnungDe;
  private String bezeichnungFr;

  protected RenameAbschlussDto(RenameAbschlussDtoBuilder<?, ?> b) {
    this.bezeichnungDe = b.bezeichnungDe;
    this.bezeichnungFr = b.bezeichnungFr;
  }

  public RenameAbschlussDto() {
  }

  /**
   **/
  public RenameAbschlussDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty(required = true, value = "bezeichnungDe")
  @NotNull public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty(required = true, value = "bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public RenameAbschlussDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty(required = true, value = "bezeichnungFr")
  @NotNull public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty(required = true, value = "bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RenameAbschlussDto renameAbschluss = (RenameAbschlussDto) o;
    return Objects.equals(this.bezeichnungDe, renameAbschluss.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, renameAbschluss.bezeichnungFr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RenameAbschlussDto {\n");
    
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
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


  public static RenameAbschlussDtoBuilder<?, ?> builder() {
    return new RenameAbschlussDtoBuilderImpl();
  }

  private static final class RenameAbschlussDtoBuilderImpl extends RenameAbschlussDtoBuilder<RenameAbschlussDto, RenameAbschlussDtoBuilderImpl> {

    @Override
    protected RenameAbschlussDtoBuilderImpl self() {
      return this;
    }

    @Override
    public RenameAbschlussDto build() {
      return new RenameAbschlussDto(this);
    }
  }

  public static abstract class RenameAbschlussDtoBuilder<C extends RenameAbschlussDto, B extends RenameAbschlussDtoBuilder<C, B>>  {
    private String bezeichnungDe;
    private String bezeichnungFr;
    protected abstract B self();

    public abstract C build();

    public B bezeichnungDe(String bezeichnungDe) {
      this.bezeichnungDe = bezeichnungDe;
      return self();
    }
    public B bezeichnungFr(String bezeichnungFr) {
      this.bezeichnungFr = bezeichnungFr;
      return self();
    }
  }
}
