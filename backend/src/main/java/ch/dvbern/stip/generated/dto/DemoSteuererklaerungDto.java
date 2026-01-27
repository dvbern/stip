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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoSteuererklaerungDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp type;
  private @Valid Boolean steuererklaerungInBern;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer renten;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer andereEinnahmen;

  /**
   **/
  public DemoSteuererklaerungDto type(ch.dvbern.stip.api.eltern.type.ElternTyp type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public ch.dvbern.stip.api.eltern.type.ElternTyp getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(ch.dvbern.stip.api.eltern.type.ElternTyp type) {
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


}

