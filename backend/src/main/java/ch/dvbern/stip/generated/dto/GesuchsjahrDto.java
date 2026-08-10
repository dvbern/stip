package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GueltigkeitStatusDto;
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



@JsonTypeName("Gesuchsjahr")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchsjahrDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid Integer technischesJahr;
  private @Valid GueltigkeitStatusDto gueltigkeitStatus;

  protected GesuchsjahrDto(GesuchsjahrDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.bezeichnungDe = b.bezeichnungDe;
    this.bezeichnungFr = b.bezeichnungFr;
    this.technischesJahr = b.technischesJahr;
    this.gueltigkeitStatus = b.gueltigkeitStatus;
  }

  public GesuchsjahrDto() {
  }

  /**
   **/
  public GesuchsjahrDto id(UUID id) {
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
  public GesuchsjahrDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public GesuchsjahrDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public GesuchsjahrDto technischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
    return this;
  }

  
  @JsonProperty("technischesJahr")
  @NotNull
  public Integer getTechnischesJahr() {
    return technischesJahr;
  }

  @JsonProperty("technischesJahr")
  public void setTechnischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
  }

  /**
   **/
  public GesuchsjahrDto gueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
    return this;
  }

  
  @JsonProperty("gueltigkeitStatus")
  @NotNull
  public GueltigkeitStatusDto getGueltigkeitStatus() {
    return gueltigkeitStatus;
  }

  @JsonProperty("gueltigkeitStatus")
  public void setGueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsjahrDto gesuchsjahr = (GesuchsjahrDto) o;
    return Objects.equals(this.id, gesuchsjahr.id) &&
        Objects.equals(this.bezeichnungDe, gesuchsjahr.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsjahr.bezeichnungFr) &&
        Objects.equals(this.technischesJahr, gesuchsjahr.technischesJahr) &&
        Objects.equals(this.gueltigkeitStatus, gesuchsjahr.gueltigkeitStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, technischesJahr, gueltigkeitStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsjahrDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    technischesJahr: ").append(toIndentedString(technischesJahr)).append("\n");
    sb.append("    gueltigkeitStatus: ").append(toIndentedString(gueltigkeitStatus)).append("\n");
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


  public static GesuchsjahrDtoBuilder<?, ?> builder() {
    return new GesuchsjahrDtoBuilderImpl();
  }

  private static final class GesuchsjahrDtoBuilderImpl extends GesuchsjahrDtoBuilder<GesuchsjahrDto, GesuchsjahrDtoBuilderImpl> {

    @Override
    protected GesuchsjahrDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchsjahrDto build() {
      return new GesuchsjahrDto(this);
    }
  }

  public static abstract class GesuchsjahrDtoBuilder<C extends GesuchsjahrDto, B extends GesuchsjahrDtoBuilder<C, B>>  {
    private UUID id;
    private String bezeichnungDe;
    private String bezeichnungFr;
    private Integer technischesJahr;
    private GueltigkeitStatusDto gueltigkeitStatus;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
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
    public B gueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
      this.gueltigkeitStatus = gueltigkeitStatus;
      return self();
    }
  }
}

