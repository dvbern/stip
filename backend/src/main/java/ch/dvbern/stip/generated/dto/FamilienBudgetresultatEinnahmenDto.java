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



@JsonTypeName("FamilienBudgetresultatEinnahmen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FamilienBudgetresultatEinnahmenDto  implements Serializable {
  private Integer total;
  private Integer totalEinkuenfte;
  private Integer einnahmenBGSA;
  private Integer ergaenzungsleistungen;
  private Integer andereEinnahmen;
  private Integer eigenmietwert;
  private Integer unterhaltsbeitraege;
  private Integer sauele3;
  private Integer sauele2;
  private Integer renten;
  private Integer einkommensfreibetrag;
  private Integer zwischentotal;
  private Integer anrechenbaresVermoegen;
  private Integer steuerbaresVermoegen;

  protected FamilienBudgetresultatEinnahmenDto(FamilienBudgetresultatEinnahmenDtoBuilder<?, ?> b) {
    this.total = b.total;
    this.totalEinkuenfte = b.totalEinkuenfte;
    this.einnahmenBGSA = b.einnahmenBGSA;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.andereEinnahmen = b.andereEinnahmen;
    this.eigenmietwert = b.eigenmietwert;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.sauele3 = b.sauele3;
    this.sauele2 = b.sauele2;
    this.renten = b.renten;
    this.einkommensfreibetrag = b.einkommensfreibetrag;
    this.zwischentotal = b.zwischentotal;
    this.anrechenbaresVermoegen = b.anrechenbaresVermoegen;
    this.steuerbaresVermoegen = b.steuerbaresVermoegen;
  }

  public FamilienBudgetresultatEinnahmenDto() {
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty(required = true, value = "total")
  @NotNull public Integer getTotal() {
    return total;
  }

  @JsonProperty(required = true, value = "total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto totalEinkuenfte(Integer totalEinkuenfte) {
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
  public FamilienBudgetresultatEinnahmenDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty(required = true, value = "einnahmenBGSA")
  @NotNull public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty(required = true, value = "einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty(required = true, value = "ergaenzungsleistungen")
  @NotNull public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty(required = true, value = "ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty(required = true, value = "andereEinnahmen")
  @NotNull public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty(required = true, value = "andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto eigenmietwert(Integer eigenmietwert) {
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
  public FamilienBudgetresultatEinnahmenDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty(required = true, value = "unterhaltsbeitraege")
  @NotNull public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty(required = true, value = "unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto sauele3(Integer sauele3) {
    this.sauele3 = sauele3;
    return this;
  }

  
  @JsonProperty(required = true, value = "sauele3")
  @NotNull public Integer getSauele3() {
    return sauele3;
  }

  @JsonProperty(required = true, value = "sauele3")
  public void setSauele3(Integer sauele3) {
    this.sauele3 = sauele3;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto sauele2(Integer sauele2) {
    this.sauele2 = sauele2;
    return this;
  }

  
  @JsonProperty(required = true, value = "sauele2")
  @NotNull public Integer getSauele2() {
    return sauele2;
  }

  @JsonProperty(required = true, value = "sauele2")
  public void setSauele2(Integer sauele2) {
    this.sauele2 = sauele2;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty(required = true, value = "renten")
  @NotNull public Integer getRenten() {
    return renten;
  }

  @JsonProperty(required = true, value = "renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto einkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty(required = true, value = "einkommensfreibetrag")
  @NotNull public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty(required = true, value = "einkommensfreibetrag")
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto zwischentotal(Integer zwischentotal) {
    this.zwischentotal = zwischentotal;
    return this;
  }

  
  @JsonProperty(required = true, value = "zwischentotal")
  @NotNull public Integer getZwischentotal() {
    return zwischentotal;
  }

  @JsonProperty(required = true, value = "zwischentotal")
  public void setZwischentotal(Integer zwischentotal) {
    this.zwischentotal = zwischentotal;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "anrechenbaresVermoegen")
  @NotNull public Integer getAnrechenbaresVermoegen() {
    return anrechenbaresVermoegen;
  }

  @JsonProperty(required = true, value = "anrechenbaresVermoegen")
  public void setAnrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto steuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "steuerbaresVermoegen")
  @NotNull public Integer getSteuerbaresVermoegen() {
    return steuerbaresVermoegen;
  }

  @JsonProperty(required = true, value = "steuerbaresVermoegen")
  public void setSteuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FamilienBudgetresultatEinnahmenDto familienBudgetresultatEinnahmen = (FamilienBudgetresultatEinnahmenDto) o;
    return Objects.equals(this.total, familienBudgetresultatEinnahmen.total) &&
        Objects.equals(this.totalEinkuenfte, familienBudgetresultatEinnahmen.totalEinkuenfte) &&
        Objects.equals(this.einnahmenBGSA, familienBudgetresultatEinnahmen.einnahmenBGSA) &&
        Objects.equals(this.ergaenzungsleistungen, familienBudgetresultatEinnahmen.ergaenzungsleistungen) &&
        Objects.equals(this.andereEinnahmen, familienBudgetresultatEinnahmen.andereEinnahmen) &&
        Objects.equals(this.eigenmietwert, familienBudgetresultatEinnahmen.eigenmietwert) &&
        Objects.equals(this.unterhaltsbeitraege, familienBudgetresultatEinnahmen.unterhaltsbeitraege) &&
        Objects.equals(this.sauele3, familienBudgetresultatEinnahmen.sauele3) &&
        Objects.equals(this.sauele2, familienBudgetresultatEinnahmen.sauele2) &&
        Objects.equals(this.renten, familienBudgetresultatEinnahmen.renten) &&
        Objects.equals(this.einkommensfreibetrag, familienBudgetresultatEinnahmen.einkommensfreibetrag) &&
        Objects.equals(this.zwischentotal, familienBudgetresultatEinnahmen.zwischentotal) &&
        Objects.equals(this.anrechenbaresVermoegen, familienBudgetresultatEinnahmen.anrechenbaresVermoegen) &&
        Objects.equals(this.steuerbaresVermoegen, familienBudgetresultatEinnahmen.steuerbaresVermoegen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, totalEinkuenfte, einnahmenBGSA, ergaenzungsleistungen, andereEinnahmen, eigenmietwert, unterhaltsbeitraege, sauele3, sauele2, renten, einkommensfreibetrag, zwischentotal, anrechenbaresVermoegen, steuerbaresVermoegen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatEinnahmenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    sauele3: ").append(toIndentedString(sauele3)).append("\n");
    sb.append("    sauele2: ").append(toIndentedString(sauele2)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    einkommensfreibetrag: ").append(toIndentedString(einkommensfreibetrag)).append("\n");
    sb.append("    zwischentotal: ").append(toIndentedString(zwischentotal)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
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


  public static FamilienBudgetresultatEinnahmenDtoBuilder<?, ?> builder() {
    return new FamilienBudgetresultatEinnahmenDtoBuilderImpl();
  }

  private static final class FamilienBudgetresultatEinnahmenDtoBuilderImpl extends FamilienBudgetresultatEinnahmenDtoBuilder<FamilienBudgetresultatEinnahmenDto, FamilienBudgetresultatEinnahmenDtoBuilderImpl> {

    @Override
    protected FamilienBudgetresultatEinnahmenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FamilienBudgetresultatEinnahmenDto build() {
      return new FamilienBudgetresultatEinnahmenDto(this);
    }
  }

  public static abstract class FamilienBudgetresultatEinnahmenDtoBuilder<C extends FamilienBudgetresultatEinnahmenDto, B extends FamilienBudgetresultatEinnahmenDtoBuilder<C, B>>  {
    private Integer total;
    private Integer totalEinkuenfte;
    private Integer einnahmenBGSA;
    private Integer ergaenzungsleistungen;
    private Integer andereEinnahmen;
    private Integer eigenmietwert;
    private Integer unterhaltsbeitraege;
    private Integer sauele3;
    private Integer sauele2;
    private Integer renten;
    private Integer einkommensfreibetrag;
    private Integer zwischentotal;
    private Integer anrechenbaresVermoegen;
    private Integer steuerbaresVermoegen;
    protected abstract B self();

    public abstract C build();

    public B total(Integer total) {
      this.total = total;
      return self();
    }
    public B totalEinkuenfte(Integer totalEinkuenfte) {
      this.totalEinkuenfte = totalEinkuenfte;
      return self();
    }
    public B einnahmenBGSA(Integer einnahmenBGSA) {
      this.einnahmenBGSA = einnahmenBGSA;
      return self();
    }
    public B ergaenzungsleistungen(Integer ergaenzungsleistungen) {
      this.ergaenzungsleistungen = ergaenzungsleistungen;
      return self();
    }
    public B andereEinnahmen(Integer andereEinnahmen) {
      this.andereEinnahmen = andereEinnahmen;
      return self();
    }
    public B eigenmietwert(Integer eigenmietwert) {
      this.eigenmietwert = eigenmietwert;
      return self();
    }
    public B unterhaltsbeitraege(Integer unterhaltsbeitraege) {
      this.unterhaltsbeitraege = unterhaltsbeitraege;
      return self();
    }
    public B sauele3(Integer sauele3) {
      this.sauele3 = sauele3;
      return self();
    }
    public B sauele2(Integer sauele2) {
      this.sauele2 = sauele2;
      return self();
    }
    public B renten(Integer renten) {
      this.renten = renten;
      return self();
    }
    public B einkommensfreibetrag(Integer einkommensfreibetrag) {
      this.einkommensfreibetrag = einkommensfreibetrag;
      return self();
    }
    public B zwischentotal(Integer zwischentotal) {
      this.zwischentotal = zwischentotal;
      return self();
    }
    public B anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
      this.anrechenbaresVermoegen = anrechenbaresVermoegen;
      return self();
    }
    public B steuerbaresVermoegen(Integer steuerbaresVermoegen) {
      this.steuerbaresVermoegen = steuerbaresVermoegen;
      return self();
    }
  }
}
