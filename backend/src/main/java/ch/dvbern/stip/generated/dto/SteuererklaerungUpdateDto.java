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



@JsonTypeName("SteuererklaerungUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SteuererklaerungUpdateDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
  private @Valid Boolean steuererklaerungInBern;
  private @Valid UUID id;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer renten;
  private @Valid Integer einnahmenBGSA;

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
  public SteuererklaerungUpdateDto id(UUID id) {
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
        Objects.equals(this.id, steuererklaerungUpdate.id) &&
        Objects.equals(this.ergaenzungsleistungen, steuererklaerungUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.unterhaltsbeitraege, steuererklaerungUpdate.unterhaltsbeitraege) &&
        Objects.equals(this.renten, steuererklaerungUpdate.renten) &&
        Objects.equals(this.einnahmenBGSA, steuererklaerungUpdate.einnahmenBGSA);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdatenTyp, steuererklaerungInBern, id, ergaenzungsleistungen, unterhaltsbeitraege, renten, einnahmenBGSA);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuererklaerungUpdateDto {\n");
    
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    steuererklaerungInBern: ").append(toIndentedString(steuererklaerungInBern)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
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

