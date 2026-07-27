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



@JsonTypeName("DemoSteuererklaerung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoSteuererklaerungDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type;
  private @Valid Boolean steuererklaerungInBern;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer renten;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer andereEinnahmen;

  protected DemoSteuererklaerungDto(DemoSteuererklaerungDtoBuilder<?, ?> b) {
    this.type = b.type;
    this.steuererklaerungInBern = b.steuererklaerungInBern;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.renten = b.renten;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.einnahmenBGSA = b.einnahmenBGSA;
    this.andereEinnahmen = b.andereEinnahmen;
  }

  public DemoSteuererklaerungDto() {
  }

  /**
   **/
  public DemoSteuererklaerungDto type(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type) {
    this.type = type;
  }

  /**
   **/
  public DemoSteuererklaerungDto steuererklaerungInBern(Boolean steuererklaerungInBern) {
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
  public DemoSteuererklaerungDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  @NotNull
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public DemoSteuererklaerungDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  @NotNull
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public DemoSteuererklaerungDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public DemoSteuererklaerungDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  @NotNull
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public DemoSteuererklaerungDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  @NotNull
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
    DemoSteuererklaerungDto demoSteuererklaerung = (DemoSteuererklaerungDto) o;
    return Objects.equals(this.type, demoSteuererklaerung.type) &&
        Objects.equals(this.steuererklaerungInBern, demoSteuererklaerung.steuererklaerungInBern) &&
        Objects.equals(this.unterhaltsbeitraege, demoSteuererklaerung.unterhaltsbeitraege) &&
        Objects.equals(this.renten, demoSteuererklaerung.renten) &&
        Objects.equals(this.ergaenzungsleistungen, demoSteuererklaerung.ergaenzungsleistungen) &&
        Objects.equals(this.einnahmenBGSA, demoSteuererklaerung.einnahmenBGSA) &&
        Objects.equals(this.andereEinnahmen, demoSteuererklaerung.andereEinnahmen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, steuererklaerungInBern, unterhaltsbeitraege, renten, ergaenzungsleistungen, einnahmenBGSA, andereEinnahmen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoSteuererklaerungDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    steuererklaerungInBern: ").append(toIndentedString(steuererklaerungInBern)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
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


  public static DemoSteuererklaerungDtoBuilder<?, ?> builder() {
    return new DemoSteuererklaerungDtoBuilderImpl();
  }

  private static final class DemoSteuererklaerungDtoBuilderImpl extends DemoSteuererklaerungDtoBuilder<DemoSteuererklaerungDto, DemoSteuererklaerungDtoBuilderImpl> {

    @Override
    protected DemoSteuererklaerungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoSteuererklaerungDto build() {
      return new DemoSteuererklaerungDto(this);
    }
  }

  public static abstract class DemoSteuererklaerungDtoBuilder<C extends DemoSteuererklaerungDto, B extends DemoSteuererklaerungDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type;
    private Boolean steuererklaerungInBern;
    private Integer unterhaltsbeitraege;
    private Integer renten;
    private Integer ergaenzungsleistungen;
    private Integer einnahmenBGSA;
    private Integer andereEinnahmen;
    protected abstract B self();

    public abstract C build();

    public B type(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp type) {
      this.type = type;
      return self();
    }
    public B steuererklaerungInBern(Boolean steuererklaerungInBern) {
      this.steuererklaerungInBern = steuererklaerungInBern;
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
    public B ergaenzungsleistungen(Integer ergaenzungsleistungen) {
      this.ergaenzungsleistungen = ergaenzungsleistungen;
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

