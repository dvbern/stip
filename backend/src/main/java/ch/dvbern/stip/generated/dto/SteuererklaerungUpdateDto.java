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



@JsonTypeName("SteuererklaerungUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SteuererklaerungUpdateDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
  private @Valid Boolean steuererklaerungInBern;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer renten;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer andereEinnahmen;

  protected SteuererklaerungUpdateDto(SteuererklaerungUpdateDtoBuilder<?, ?> b) {
    this.steuerdatenTyp = b.steuerdatenTyp;
    this.steuererklaerungInBern = b.steuererklaerungInBern;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.renten = b.renten;
    this.einnahmenBGSA = b.einnahmenBGSA;
    this.andereEinnahmen = b.andereEinnahmen;
  }

  public SteuererklaerungUpdateDto() {
  }

  /**
   **/
  public SteuererklaerungUpdateDto steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
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
  public SteuererklaerungUpdateDto steuererklaerungInBern(Boolean steuererklaerungInBern) {
    this.steuererklaerungInBern = steuererklaerungInBern;
    return this;
  }

  
  @JsonProperty("steuererklaerungInBern")
  @NotNull
  public Boolean getSteuererklaerungInBern() {
    return steuererklaerungInBern;
  }

  @JsonProperty("steuererklaerungInBern")
  public void setSteuererklaerungInBern(Boolean steuererklaerungInBern) {
    this.steuererklaerungInBern = steuererklaerungInBern;
  }

  /**
   **/
  public SteuererklaerungUpdateDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public SteuererklaerungUpdateDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public SteuererklaerungUpdateDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public SteuererklaerungUpdateDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public SteuererklaerungUpdateDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteuererklaerungUpdateDto steuererklaerungUpdate = (SteuererklaerungUpdateDto) o;
    return Objects.equals(this.steuerdatenTyp, steuererklaerungUpdate.steuerdatenTyp) &&
        Objects.equals(this.steuererklaerungInBern, steuererklaerungUpdate.steuererklaerungInBern) &&
        Objects.equals(this.ergaenzungsleistungen, steuererklaerungUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.unterhaltsbeitraege, steuererklaerungUpdate.unterhaltsbeitraege) &&
        Objects.equals(this.renten, steuererklaerungUpdate.renten) &&
        Objects.equals(this.einnahmenBGSA, steuererklaerungUpdate.einnahmenBGSA) &&
        Objects.equals(this.andereEinnahmen, steuererklaerungUpdate.andereEinnahmen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdatenTyp, steuererklaerungInBern, ergaenzungsleistungen, unterhaltsbeitraege, renten, einnahmenBGSA, andereEinnahmen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuererklaerungUpdateDto {\n");
    
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    steuererklaerungInBern: ").append(toIndentedString(steuererklaerungInBern)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
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


  public static SteuererklaerungUpdateDtoBuilder<?, ?> builder() {
    return new SteuererklaerungUpdateDtoBuilderImpl();
  }

  private static final class SteuererklaerungUpdateDtoBuilderImpl extends SteuererklaerungUpdateDtoBuilder<SteuererklaerungUpdateDto, SteuererklaerungUpdateDtoBuilderImpl> {

    @Override
    protected SteuererklaerungUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SteuererklaerungUpdateDto build() {
      return new SteuererklaerungUpdateDto(this);
    }
  }

  public static abstract class SteuererklaerungUpdateDtoBuilder<C extends SteuererklaerungUpdateDto, B extends SteuererklaerungUpdateDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
    private Boolean steuererklaerungInBern;
    private Integer ergaenzungsleistungen;
    private Integer unterhaltsbeitraege;
    private Integer renten;
    private Integer einnahmenBGSA;
    private Integer andereEinnahmen;
    protected abstract B self();

    public abstract C build();

    public B steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
      this.steuerdatenTyp = steuerdatenTyp;
      return self();
    }
    public B steuererklaerungInBern(Boolean steuererklaerungInBern) {
      this.steuererklaerungInBern = steuererklaerungInBern;
      return self();
    }
    public B ergaenzungsleistungen(Integer ergaenzungsleistungen) {
      this.ergaenzungsleistungen = ergaenzungsleistungen;
      return self();
    }
    public B unterhaltsbeitraege(Integer unterhaltsbeitraege) {
      this.unterhaltsbeitraege = unterhaltsbeitraege;
      return self();
    }
    public B renten(Integer renten) {
      this.renten = renten;
      return self();
    }
    public B einnahmenBGSA(Integer einnahmenBGSA) {
      this.einnahmenBGSA = einnahmenBGSA;
      return self();
    }
    public B andereEinnahmen(Integer andereEinnahmen) {
      this.andereEinnahmen = andereEinnahmen;
      return self();
    }
  }
}

