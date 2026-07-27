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



@JsonTypeName("GesuchsjahrUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchsjahrUpdateDto  implements Serializable {
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid Integer technischesJahr;

  protected GesuchsjahrUpdateDto(GesuchsjahrUpdateDtoBuilder<?, ?> b) {
    this.bezeichnungDe = b.bezeichnungDe;
    this.bezeichnungFr = b.bezeichnungFr;
    this.technischesJahr = b.technischesJahr;
  }

  public GesuchsjahrUpdateDto() {
  }

  /**
   **/
  public GesuchsjahrUpdateDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public GesuchsjahrUpdateDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public GesuchsjahrUpdateDto technischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
    return this;
  }

  
  @JsonProperty("technischesJahr")
  public Integer getTechnischesJahr() {
    return technischesJahr;
  }

  @JsonProperty("technischesJahr")
  public void setTechnischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsjahrUpdateDto gesuchsjahrUpdate = (GesuchsjahrUpdateDto) o;
    return Objects.equals(this.bezeichnungDe, gesuchsjahrUpdate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsjahrUpdate.bezeichnungFr) &&
        Objects.equals(this.technischesJahr, gesuchsjahrUpdate.technischesJahr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, technischesJahr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsjahrUpdateDto {\n");
    
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    technischesJahr: ").append(toIndentedString(technischesJahr)).append("\n");
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


  public static GesuchsjahrUpdateDtoBuilder<?, ?> builder() {
    return new GesuchsjahrUpdateDtoBuilderImpl();
  }

  private static final class GesuchsjahrUpdateDtoBuilderImpl extends GesuchsjahrUpdateDtoBuilder<GesuchsjahrUpdateDto, GesuchsjahrUpdateDtoBuilderImpl> {

    @Override
    protected GesuchsjahrUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchsjahrUpdateDto build() {
      return new GesuchsjahrUpdateDto(this);
    }
  }

  public static abstract class GesuchsjahrUpdateDtoBuilder<C extends GesuchsjahrUpdateDto, B extends GesuchsjahrUpdateDtoBuilder<C, B>>  {
    private String bezeichnungDe;
    private String bezeichnungFr;
    private Integer technischesJahr;
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
    public B technischesJahr(Integer technischesJahr) {
      this.technischesJahr = technischesJahr;
      return self();
    }
  }
}

