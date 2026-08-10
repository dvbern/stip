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



@JsonTypeName("Steuerdaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SteuerdatenDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
  private @Valid Integer steuernKantonGemeinde;
  private @Valid Integer steuernBund;
  private @Valid Integer fahrkosten;
  private @Valid Integer verpflegung;
  private @Valid Integer totalEinkuenfte;
  private @Valid Integer eigenmietwert;
  private @Valid Boolean isArbeitsverhaeltnisSelbstaendig;
  private @Valid Integer vermoegen;
  private @Valid UUID id;
  private @Valid Integer fahrkostenPartner;
  private @Valid Integer verpflegungPartner;
  private @Valid Integer steuerjahr;
  private @Valid String veranlagungsStatus;
  private @Valid Integer saeule3a;
  private @Valid Integer saeule2;

  protected SteuerdatenDto(SteuerdatenDtoBuilder<?, ?> b) {
    this.steuerdatenTyp = b.steuerdatenTyp;
    this.steuernKantonGemeinde = b.steuernKantonGemeinde;
    this.steuernBund = b.steuernBund;
    this.fahrkosten = b.fahrkosten;
    this.verpflegung = b.verpflegung;
    this.totalEinkuenfte = b.totalEinkuenfte;
    this.eigenmietwert = b.eigenmietwert;
    this.isArbeitsverhaeltnisSelbstaendig = b.isArbeitsverhaeltnisSelbstaendig;
    this.vermoegen = b.vermoegen;
    this.id = b.id;
    this.fahrkostenPartner = b.fahrkostenPartner;
    this.verpflegungPartner = b.verpflegungPartner;
    this.steuerjahr = b.steuerjahr;
    this.veranlagungsStatus = b.veranlagungsStatus;
    this.saeule3a = b.saeule3a;
    this.saeule2 = b.saeule2;
  }

  public SteuerdatenDto() {
  }

  /**
   **/
  public SteuerdatenDto steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
    return this;
  }

  
  @JsonProperty("steuerdatenTyp")
  @NotNull
  public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getSteuerdatenTyp() {
    return steuerdatenTyp;
  }

  @JsonProperty("steuerdatenTyp")
  public void setSteuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
  }

  /**
   **/
  public SteuerdatenDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeinde")
  @NotNull
  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty("steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }

  /**
   **/
  public SteuerdatenDto steuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
    return this;
  }

  
  @JsonProperty("steuernBund")
  @NotNull
  public Integer getSteuernBund() {
    return steuernBund;
  }

  @JsonProperty("steuernBund")
  public void setSteuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
  }

  /**
   **/
  public SteuerdatenDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public SteuerdatenDto verpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
    return this;
  }

  
  @JsonProperty("verpflegung")
  @NotNull
  public Integer getVerpflegung() {
    return verpflegung;
  }

  @JsonProperty("verpflegung")
  public void setVerpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
  }

  /**
   **/
  public SteuerdatenDto totalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
    return this;
  }

  
  @JsonProperty("totalEinkuenfte")
  @NotNull
  public Integer getTotalEinkuenfte() {
    return totalEinkuenfte;
  }

  @JsonProperty("totalEinkuenfte")
  public void setTotalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
  }

  /**
   **/
  public SteuerdatenDto eigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
    return this;
  }

  
  @JsonProperty("eigenmietwert")
  @NotNull
  public Integer getEigenmietwert() {
    return eigenmietwert;
  }

  @JsonProperty("eigenmietwert")
  public void setEigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
  }

  /**
   **/
  public SteuerdatenDto isArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
    this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
    return this;
  }

  
  @JsonProperty("isArbeitsverhaeltnisSelbstaendig")
  @NotNull
  public Boolean getIsArbeitsverhaeltnisSelbstaendig() {
    return isArbeitsverhaeltnisSelbstaendig;
  }

  @JsonProperty("isArbeitsverhaeltnisSelbstaendig")
  public void setIsArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
    this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
  }

  /**
   **/
  public SteuerdatenDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty("vermoegen")
  @NotNull
  public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty("vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   **/
  public SteuerdatenDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public SteuerdatenDto fahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

  
  @JsonProperty("fahrkostenPartner")
  public Integer getFahrkostenPartner() {
    return fahrkostenPartner;
  }

  @JsonProperty("fahrkostenPartner")
  public void setFahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
  }

  /**
   **/
  public SteuerdatenDto verpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
    return this;
  }

  
  @JsonProperty("verpflegungPartner")
  public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }

  @JsonProperty("verpflegungPartner")
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
  }

  /**
   **/
  public SteuerdatenDto steuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
    return this;
  }

  
  @JsonProperty("steuerjahr")
  public Integer getSteuerjahr() {
    return steuerjahr;
  }

  @JsonProperty("steuerjahr")
  public void setSteuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
  }

  /**
   **/
  public SteuerdatenDto veranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
    return this;
  }

  
  @JsonProperty("veranlagungsStatus")
  public String getVeranlagungsStatus() {
    return veranlagungsStatus;
  }

  @JsonProperty("veranlagungsStatus")
  public void setVeranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
  }

  /**
   **/
  public SteuerdatenDto saeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
    return this;
  }

  
  @JsonProperty("saeule3a")
  public Integer getSaeule3a() {
    return saeule3a;
  }

  @JsonProperty("saeule3a")
  public void setSaeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
  }

  /**
   **/
  public SteuerdatenDto saeule2(Integer saeule2) {
    this.saeule2 = saeule2;
    return this;
  }

  
  @JsonProperty("saeule2")
  public Integer getSaeule2() {
    return saeule2;
  }

  @JsonProperty("saeule2")
  public void setSaeule2(Integer saeule2) {
    this.saeule2 = saeule2;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteuerdatenDto steuerdaten = (SteuerdatenDto) o;
    return Objects.equals(this.steuerdatenTyp, steuerdaten.steuerdatenTyp) &&
        Objects.equals(this.steuernKantonGemeinde, steuerdaten.steuernKantonGemeinde) &&
        Objects.equals(this.steuernBund, steuerdaten.steuernBund) &&
        Objects.equals(this.fahrkosten, steuerdaten.fahrkosten) &&
        Objects.equals(this.verpflegung, steuerdaten.verpflegung) &&
        Objects.equals(this.totalEinkuenfte, steuerdaten.totalEinkuenfte) &&
        Objects.equals(this.eigenmietwert, steuerdaten.eigenmietwert) &&
        Objects.equals(this.isArbeitsverhaeltnisSelbstaendig, steuerdaten.isArbeitsverhaeltnisSelbstaendig) &&
        Objects.equals(this.vermoegen, steuerdaten.vermoegen) &&
        Objects.equals(this.id, steuerdaten.id) &&
        Objects.equals(this.fahrkostenPartner, steuerdaten.fahrkostenPartner) &&
        Objects.equals(this.verpflegungPartner, steuerdaten.verpflegungPartner) &&
        Objects.equals(this.steuerjahr, steuerdaten.steuerjahr) &&
        Objects.equals(this.veranlagungsStatus, steuerdaten.veranlagungsStatus) &&
        Objects.equals(this.saeule3a, steuerdaten.saeule3a) &&
        Objects.equals(this.saeule2, steuerdaten.saeule2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdatenTyp, steuernKantonGemeinde, steuernBund, fahrkosten, verpflegung, totalEinkuenfte, eigenmietwert, isArbeitsverhaeltnisSelbstaendig, vermoegen, id, fahrkostenPartner, verpflegungPartner, steuerjahr, veranlagungsStatus, saeule3a, saeule2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuerdatenDto {\n");
    
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    steuernBund: ").append(toIndentedString(steuernBund)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    isArbeitsverhaeltnisSelbstaendig: ").append(toIndentedString(isArbeitsverhaeltnisSelbstaendig)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    veranlagungsStatus: ").append(toIndentedString(veranlagungsStatus)).append("\n");
    sb.append("    saeule3a: ").append(toIndentedString(saeule3a)).append("\n");
    sb.append("    saeule2: ").append(toIndentedString(saeule2)).append("\n");
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


  public static SteuerdatenDtoBuilder<?, ?> builder() {
    return new SteuerdatenDtoBuilderImpl();
  }

  private static final class SteuerdatenDtoBuilderImpl extends SteuerdatenDtoBuilder<SteuerdatenDto, SteuerdatenDtoBuilderImpl> {

    @Override
    protected SteuerdatenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SteuerdatenDto build() {
      return new SteuerdatenDto(this);
    }
  }

  public static abstract class SteuerdatenDtoBuilder<C extends SteuerdatenDto, B extends SteuerdatenDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
    private Integer steuernKantonGemeinde;
    private Integer steuernBund;
    private Integer fahrkosten;
    private Integer verpflegung;
    private Integer totalEinkuenfte;
    private Integer eigenmietwert;
    private Boolean isArbeitsverhaeltnisSelbstaendig;
    private Integer vermoegen;
    private UUID id;
    private Integer fahrkostenPartner;
    private Integer verpflegungPartner;
    private Integer steuerjahr;
    private String veranlagungsStatus;
    private Integer saeule3a;
    private Integer saeule2;
    protected abstract B self();

    public abstract C build();

    public B steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
      this.steuerdatenTyp = steuerdatenTyp;
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
    public B verpflegung(Integer verpflegung) {
      this.verpflegung = verpflegung;
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
    public B vermoegen(Integer vermoegen) {
      this.vermoegen = vermoegen;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B fahrkostenPartner(Integer fahrkostenPartner) {
      this.fahrkostenPartner = fahrkostenPartner;
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
    public B saeule3a(Integer saeule3a) {
      this.saeule3a = saeule3a;
      return self();
    }
    public B saeule2(Integer saeule2) {
      this.saeule2 = saeule2;
      return self();
    }
  }
}

