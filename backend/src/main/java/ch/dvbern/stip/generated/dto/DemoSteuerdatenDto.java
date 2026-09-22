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



@JsonTypeName("DemoSteuerdaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoSteuerdatenDto  implements Serializable {
  private ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type;
  private Integer totalEinkuenfte;
  private Integer eigenmietwert;
  private Boolean isArbeitsverhaeltnisSelbstaendig;
  private Integer saeule3a;
  private Integer saeule2;
  private Integer vermoegen;
  private Integer steuernKantonGemeinde;
  private Integer steuernBund;
  private Integer fahrkosten;
  private Integer fahrkostenPartner;
  private Integer verpflegung;
  private Integer verpflegungPartner;
  private Integer steuerjahr;
  private String veranlagungsStatus;

  protected DemoSteuerdatenDto(DemoSteuerdatenDtoBuilder<?, ?> b) {
    this.type = b.type;
    this.totalEinkuenfte = b.totalEinkuenfte;
    this.eigenmietwert = b.eigenmietwert;
    this.isArbeitsverhaeltnisSelbstaendig = b.isArbeitsverhaeltnisSelbstaendig;
    this.saeule3a = b.saeule3a;
    this.saeule2 = b.saeule2;
    this.vermoegen = b.vermoegen;
    this.steuernKantonGemeinde = b.steuernKantonGemeinde;
    this.steuernBund = b.steuernBund;
    this.fahrkosten = b.fahrkosten;
    this.fahrkostenPartner = b.fahrkostenPartner;
    this.verpflegung = b.verpflegung;
    this.verpflegungPartner = b.verpflegungPartner;
    this.steuerjahr = b.steuerjahr;
    this.veranlagungsStatus = b.veranlagungsStatus;
  }

  public DemoSteuerdatenDto() {
  }

  /**
   **/
  public DemoSteuerdatenDto type(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty(required = true, value = "type")
  @NotNull public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getType() {
    return type;
  }

  @JsonProperty(required = true, value = "type")
  public void setType(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type) {
    this.type = type;
  }

  /**
   **/
  public DemoSteuerdatenDto totalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
    return this;
  }

  
  @JsonProperty(required = true, value = "totalEinkuenfte")
  @NotNull public Integer getTotalEinkuenfte() {
    return totalEinkuenfte;
  }

  @JsonProperty(required = true, value = "totalEinkuenfte")
  public void setTotalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
  }

  /**
   **/
  public DemoSteuerdatenDto eigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
    return this;
  }

  
  @JsonProperty(required = true, value = "eigenmietwert")
  @NotNull public Integer getEigenmietwert() {
    return eigenmietwert;
  }

  @JsonProperty(required = true, value = "eigenmietwert")
  public void setEigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
  }

  /**
   **/
  public DemoSteuerdatenDto isArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
    this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
    return this;
  }

  
  @JsonProperty(required = true, value = "isArbeitsverhaeltnisSelbstaendig")
  @NotNull public Boolean getIsArbeitsverhaeltnisSelbstaendig() {
    return isArbeitsverhaeltnisSelbstaendig;
  }

  @JsonProperty(required = true, value = "isArbeitsverhaeltnisSelbstaendig")
  public void setIsArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
    this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
  }

  /**
   **/
  public DemoSteuerdatenDto saeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
    return this;
  }

  
  @JsonProperty(required = true, value = "saeule3a")
  @NotNull public Integer getSaeule3a() {
    return saeule3a;
  }

  @JsonProperty(required = true, value = "saeule3a")
  public void setSaeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
  }

  /**
   **/
  public DemoSteuerdatenDto saeule2(Integer saeule2) {
    this.saeule2 = saeule2;
    return this;
  }

  
  @JsonProperty(required = true, value = "saeule2")
  @NotNull public Integer getSaeule2() {
    return saeule2;
  }

  @JsonProperty(required = true, value = "saeule2")
  public void setSaeule2(Integer saeule2) {
    this.saeule2 = saeule2;
  }

  /**
   **/
  public DemoSteuerdatenDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "vermoegen")
  @NotNull public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty(required = true, value = "vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   **/
  public DemoSteuerdatenDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty(required = true, value = "steuernKantonGemeinde")
  @NotNull public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty(required = true, value = "steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }

  /**
   **/
  public DemoSteuerdatenDto steuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
    return this;
  }

  
  @JsonProperty(required = true, value = "steuernBund")
  @NotNull public Integer getSteuernBund() {
    return steuernBund;
  }

  @JsonProperty(required = true, value = "steuernBund")
  public void setSteuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
  }

  /**
   **/
  public DemoSteuerdatenDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "fahrkosten")
  @NotNull public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty(required = true, value = "fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public DemoSteuerdatenDto fahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

  
  @JsonProperty(required = true, value = "fahrkostenPartner")
  @NotNull public Integer getFahrkostenPartner() {
    return fahrkostenPartner;
  }

  @JsonProperty(required = true, value = "fahrkostenPartner")
  public void setFahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
  }

  /**
   **/
  public DemoSteuerdatenDto verpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
    return this;
  }

  
  @JsonProperty(required = true, value = "verpflegung")
  @NotNull public Integer getVerpflegung() {
    return verpflegung;
  }

  @JsonProperty(required = true, value = "verpflegung")
  public void setVerpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
  }

  /**
   **/
  public DemoSteuerdatenDto verpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
    return this;
  }

  
  @JsonProperty(required = true, value = "verpflegungPartner")
  @NotNull public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }

  @JsonProperty(required = true, value = "verpflegungPartner")
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
  }

  /**
   **/
  public DemoSteuerdatenDto steuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
    return this;
  }

  
  @JsonProperty(required = true, value = "steuerjahr")
  @NotNull public Integer getSteuerjahr() {
    return steuerjahr;
  }

  @JsonProperty(required = true, value = "steuerjahr")
  public void setSteuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
  }

  /**
   **/
  public DemoSteuerdatenDto veranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
    return this;
  }

  
  @JsonProperty(required = true, value = "veranlagungsStatus")
  @NotNull public String getVeranlagungsStatus() {
    return veranlagungsStatus;
  }

  @JsonProperty(required = true, value = "veranlagungsStatus")
  public void setVeranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoSteuerdatenDto demoSteuerdaten = (DemoSteuerdatenDto) o;
    return Objects.equals(this.type, demoSteuerdaten.type) &&
        Objects.equals(this.totalEinkuenfte, demoSteuerdaten.totalEinkuenfte) &&
        Objects.equals(this.eigenmietwert, demoSteuerdaten.eigenmietwert) &&
        Objects.equals(this.isArbeitsverhaeltnisSelbstaendig, demoSteuerdaten.isArbeitsverhaeltnisSelbstaendig) &&
        Objects.equals(this.saeule3a, demoSteuerdaten.saeule3a) &&
        Objects.equals(this.saeule2, demoSteuerdaten.saeule2) &&
        Objects.equals(this.vermoegen, demoSteuerdaten.vermoegen) &&
        Objects.equals(this.steuernKantonGemeinde, demoSteuerdaten.steuernKantonGemeinde) &&
        Objects.equals(this.steuernBund, demoSteuerdaten.steuernBund) &&
        Objects.equals(this.fahrkosten, demoSteuerdaten.fahrkosten) &&
        Objects.equals(this.fahrkostenPartner, demoSteuerdaten.fahrkostenPartner) &&
        Objects.equals(this.verpflegung, demoSteuerdaten.verpflegung) &&
        Objects.equals(this.verpflegungPartner, demoSteuerdaten.verpflegungPartner) &&
        Objects.equals(this.steuerjahr, demoSteuerdaten.steuerjahr) &&
        Objects.equals(this.veranlagungsStatus, demoSteuerdaten.veranlagungsStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, totalEinkuenfte, eigenmietwert, isArbeitsverhaeltnisSelbstaendig, saeule3a, saeule2, vermoegen, steuernKantonGemeinde, steuernBund, fahrkosten, fahrkostenPartner, verpflegung, verpflegungPartner, steuerjahr, veranlagungsStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoSteuerdatenDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    isArbeitsverhaeltnisSelbstaendig: ").append(toIndentedString(isArbeitsverhaeltnisSelbstaendig)).append("\n");
    sb.append("    saeule3a: ").append(toIndentedString(saeule3a)).append("\n");
    sb.append("    saeule2: ").append(toIndentedString(saeule2)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    steuernBund: ").append(toIndentedString(steuernBund)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    veranlagungsStatus: ").append(toIndentedString(veranlagungsStatus)).append("\n");
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


  public static DemoSteuerdatenDtoBuilder<?, ?> builder() {
    return new DemoSteuerdatenDtoBuilderImpl();
  }

  private static final class DemoSteuerdatenDtoBuilderImpl extends DemoSteuerdatenDtoBuilder<DemoSteuerdatenDto, DemoSteuerdatenDtoBuilderImpl> {

    @Override
    protected DemoSteuerdatenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoSteuerdatenDto build() {
      return new DemoSteuerdatenDto(this);
    }
  }

  public static abstract class DemoSteuerdatenDtoBuilder<C extends DemoSteuerdatenDto, B extends DemoSteuerdatenDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type;
    private Integer totalEinkuenfte;
    private Integer eigenmietwert;
    private Boolean isArbeitsverhaeltnisSelbstaendig;
    private Integer saeule3a;
    private Integer saeule2;
    private Integer vermoegen;
    private Integer steuernKantonGemeinde;
    private Integer steuernBund;
    private Integer fahrkosten;
    private Integer fahrkostenPartner;
    private Integer verpflegung;
    private Integer verpflegungPartner;
    private Integer steuerjahr;
    private String veranlagungsStatus;
    protected abstract B self();

    public abstract C build();

    public B type(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type) {
      this.type = type;
      return self();
    }
    public B totalEinkuenfte(Integer totalEinkuenfte) {
      this.totalEinkuenfte = totalEinkuenfte;
      return self();
    }
    public B eigenmietwert(Integer eigenmietwert) {
      this.eigenmietwert = eigenmietwert;
      return self();
    }
    public B isArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
      this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
      return self();
    }
    public B saeule3a(Integer saeule3a) {
      this.saeule3a = saeule3a;
      return self();
    }
    public B saeule2(Integer saeule2) {
      this.saeule2 = saeule2;
      return self();
    }
    public B vermoegen(Integer vermoegen) {
      this.vermoegen = vermoegen;
      return self();
    }
    public B steuernKantonGemeinde(Integer steuernKantonGemeinde) {
      this.steuernKantonGemeinde = steuernKantonGemeinde;
      return self();
    }
    public B steuernBund(Integer steuernBund) {
      this.steuernBund = steuernBund;
      return self();
    }
    public B fahrkosten(Integer fahrkosten) {
      this.fahrkosten = fahrkosten;
      return self();
    }
    public B fahrkostenPartner(Integer fahrkostenPartner) {
      this.fahrkostenPartner = fahrkostenPartner;
      return self();
    }
    public B verpflegung(Integer verpflegung) {
      this.verpflegung = verpflegung;
      return self();
    }
    public B verpflegungPartner(Integer verpflegungPartner) {
      this.verpflegungPartner = verpflegungPartner;
      return self();
    }
    public B steuerjahr(Integer steuerjahr) {
      this.steuerjahr = steuerjahr;
      return self();
    }
    public B veranlagungsStatus(String veranlagungsStatus) {
      this.veranlagungsStatus = veranlagungsStatus;
      return self();
    }
  }
}
