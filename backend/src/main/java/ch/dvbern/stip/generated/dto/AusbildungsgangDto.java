package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
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



@JsonTypeName("Ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungsgangDto  implements Serializable {
  private @Valid UUID id;
  private @Valid AbschlussDto abschluss;
  private @Valid AusbildungsstaetteDto ausbildungsstaette;
  private @Valid Boolean aktiv;

  protected AusbildungsgangDto(AusbildungsgangDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.abschluss = b.abschluss;
    this.ausbildungsstaette = b.ausbildungsstaette;
    this.aktiv = b.aktiv;
  }

  public AusbildungsgangDto() {
  }

  /**
   **/
  public AusbildungsgangDto id(UUID id) {
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
  public AusbildungsgangDto abschluss(AbschlussDto abschluss) {
    this.abschluss = abschluss;
    return this;
  }

  
  @JsonProperty("abschluss")
  @NotNull
  public AbschlussDto getAbschluss() {
    return abschluss;
  }

  @JsonProperty("abschluss")
  public void setAbschluss(AbschlussDto abschluss) {
    this.abschluss = abschluss;
  }

  /**
   **/
  public AusbildungsgangDto ausbildungsstaette(AusbildungsstaetteDto ausbildungsstaette) {
    this.ausbildungsstaette = ausbildungsstaette;
    return this;
  }

  
  @JsonProperty("ausbildungsstaette")
  @NotNull
  public AusbildungsstaetteDto getAusbildungsstaette() {
    return ausbildungsstaette;
  }

  @JsonProperty("ausbildungsstaette")
  public void setAusbildungsstaette(AusbildungsstaetteDto ausbildungsstaette) {
    this.ausbildungsstaette = ausbildungsstaette;
  }

  /**
   **/
  public AusbildungsgangDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty("aktiv")
  @NotNull
  public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty("aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsgangDto ausbildungsgang = (AusbildungsgangDto) o;
    return Objects.equals(this.id, ausbildungsgang.id) &&
        Objects.equals(this.abschluss, ausbildungsgang.abschluss) &&
        Objects.equals(this.ausbildungsstaette, ausbildungsgang.ausbildungsstaette) &&
        Objects.equals(this.aktiv, ausbildungsgang.aktiv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, abschluss, ausbildungsstaette, aktiv);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    abschluss: ").append(toIndentedString(abschluss)).append("\n");
    sb.append("    ausbildungsstaette: ").append(toIndentedString(ausbildungsstaette)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
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


  public static AusbildungsgangDtoBuilder<?, ?> builder() {
    return new AusbildungsgangDtoBuilderImpl();
  }

  private static final class AusbildungsgangDtoBuilderImpl extends AusbildungsgangDtoBuilder<AusbildungsgangDto, AusbildungsgangDtoBuilderImpl> {

    @Override
    protected AusbildungsgangDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungsgangDto build() {
      return new AusbildungsgangDto(this);
    }
  }

  public static abstract class AusbildungsgangDtoBuilder<C extends AusbildungsgangDto, B extends AusbildungsgangDtoBuilder<C, B>>  {
    private UUID id;
    private AbschlussDto abschluss;
    private AusbildungsstaetteDto ausbildungsstaette;
    private Boolean aktiv;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B abschluss(AbschlussDto abschluss) {
      this.abschluss = abschluss;
      return self();
    }
    public B ausbildungsstaette(AusbildungsstaetteDto ausbildungsstaette) {
      this.ausbildungsstaette = ausbildungsstaette;
      return self();
    }
    public B aktiv(Boolean aktiv) {
      this.aktiv = aktiv;
      return self();
    }
  }
}

