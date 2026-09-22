package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungsgangDataDto;
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



@JsonTypeName("AusbildungsstaetteSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungsstaetteSlimDto  implements Serializable {
  private UUID id;
  private String nameDe;
  private String nameFr;
  private @Valid List<@Valid AusbildungsgangDataDto> ausbildungsgaenge = new ArrayList<>();
  private Boolean aktiv;

  protected AusbildungsstaetteSlimDto(AusbildungsstaetteSlimDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.nameDe = b.nameDe;
    this.nameFr = b.nameFr;
    this.ausbildungsgaenge = b.ausbildungsgaenge;
    this.aktiv = b.aktiv;
  }

  public AusbildungsstaetteSlimDto() {
  }

  /**
   **/
  public AusbildungsstaetteSlimDto id(UUID id) {
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
  public AusbildungsstaetteSlimDto nameDe(String nameDe) {
    this.nameDe = nameDe;
    return this;
  }

  
  @JsonProperty(required = true, value = "nameDe")
  @NotNull public String getNameDe() {
    return nameDe;
  }

  @JsonProperty(required = true, value = "nameDe")
  public void setNameDe(String nameDe) {
    this.nameDe = nameDe;
  }

  /**
   **/
  public AusbildungsstaetteSlimDto nameFr(String nameFr) {
    this.nameFr = nameFr;
    return this;
  }

  
  @JsonProperty(required = true, value = "nameFr")
  @NotNull public String getNameFr() {
    return nameFr;
  }

  @JsonProperty(required = true, value = "nameFr")
  public void setNameFr(String nameFr) {
    this.nameFr = nameFr;
  }

  /**
   **/
  public AusbildungsstaetteSlimDto ausbildungsgaenge(List<@Valid AusbildungsgangDataDto> ausbildungsgaenge) {
    this.ausbildungsgaenge = ausbildungsgaenge;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungsgaenge")
  @NotNull @Valid public List<@Valid AusbildungsgangDataDto> getAusbildungsgaenge() {
    return ausbildungsgaenge;
  }

  @JsonProperty(required = true, value = "ausbildungsgaenge")
  public void setAusbildungsgaenge(List<@Valid AusbildungsgangDataDto> ausbildungsgaenge) {
    this.ausbildungsgaenge = ausbildungsgaenge;
  }

  public AusbildungsstaetteSlimDto addAusbildungsgaengeItem(AusbildungsgangDataDto ausbildungsgaengeItem) {
    if (this.ausbildungsgaenge == null) {
      this.ausbildungsgaenge = new ArrayList<>();
    }

    this.ausbildungsgaenge.add(ausbildungsgaengeItem);
    return this;
  }

  public AusbildungsstaetteSlimDto removeAusbildungsgaengeItem(AusbildungsgangDataDto ausbildungsgaengeItem) {
    if (ausbildungsgaengeItem != null && this.ausbildungsgaenge != null) {
      this.ausbildungsgaenge.remove(ausbildungsgaengeItem);
    }

    return this;
  }
  /**
   **/
  public AusbildungsstaetteSlimDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty(required = true, value = "aktiv")
  @NotNull public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty(required = true, value = "aktiv")
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
    AusbildungsstaetteSlimDto ausbildungsstaetteSlim = (AusbildungsstaetteSlimDto) o;
    return Objects.equals(this.id, ausbildungsstaetteSlim.id) &&
        Objects.equals(this.nameDe, ausbildungsstaetteSlim.nameDe) &&
        Objects.equals(this.nameFr, ausbildungsstaetteSlim.nameFr) &&
        Objects.equals(this.ausbildungsgaenge, ausbildungsstaetteSlim.ausbildungsgaenge) &&
        Objects.equals(this.aktiv, ausbildungsstaetteSlim.aktiv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nameDe, nameFr, ausbildungsgaenge, aktiv);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsstaetteSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nameDe: ").append(toIndentedString(nameDe)).append("\n");
    sb.append("    nameFr: ").append(toIndentedString(nameFr)).append("\n");
    sb.append("    ausbildungsgaenge: ").append(toIndentedString(ausbildungsgaenge)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
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


  public static AusbildungsstaetteSlimDtoBuilder<?, ?> builder() {
    return new AusbildungsstaetteSlimDtoBuilderImpl();
  }

  private static final class AusbildungsstaetteSlimDtoBuilderImpl extends AusbildungsstaetteSlimDtoBuilder<AusbildungsstaetteSlimDto, AusbildungsstaetteSlimDtoBuilderImpl> {

    @Override
    protected AusbildungsstaetteSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungsstaetteSlimDto build() {
      return new AusbildungsstaetteSlimDto(this);
    }
  }

  public static abstract class AusbildungsstaetteSlimDtoBuilder<C extends AusbildungsstaetteSlimDto, B extends AusbildungsstaetteSlimDtoBuilder<C, B>>  {
    private UUID id;
    private String nameDe;
    private String nameFr;
    private List<AusbildungsgangDataDto> ausbildungsgaenge = new ArrayList<>();
    private Boolean aktiv;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B nameDe(String nameDe) {
      this.nameDe = nameDe;
      return self();
    }
    public B nameFr(String nameFr) {
      this.nameFr = nameFr;
      return self();
    }
    public B ausbildungsgaenge(List<AusbildungsgangDataDto> ausbildungsgaenge) {
      this.ausbildungsgaenge = ausbildungsgaenge;
      return self();
    }
    public B aktiv(Boolean aktiv) {
      this.aktiv = aktiv;
      return self();
    }
  }
}
