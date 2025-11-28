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



@JsonTypeName("DemoDarlehen_gruendeDarlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDarlehenGruendeDarlehenDto  implements Serializable {
  private @Valid Boolean grundNichtBerechtigt;
  private @Valid Boolean grundAusbildungZwoelfJahre;
  private @Valid Boolean grundHoheGebuehren;
  private @Valid Boolean grundAnschaffungenFuerAusbildung;
  private @Valid Boolean grundZweitausbildung;

  /**
   **/
  public DemoDarlehenGruendeDarlehenDto grundNichtBerechtigt(Boolean grundNichtBerechtigt) {
    this.grundNichtBerechtigt = grundNichtBerechtigt;
    return this;
  }

  
  @JsonProperty("grundNichtBerechtigt")
  public Boolean getGrundNichtBerechtigt() {
    return grundNichtBerechtigt;
  }

  @JsonProperty("grundNichtBerechtigt")
  public void setGrundNichtBerechtigt(Boolean grundNichtBerechtigt) {
    this.grundNichtBerechtigt = grundNichtBerechtigt;
  }

  /**
   **/
  public DemoDarlehenGruendeDarlehenDto grundAusbildungZwoelfJahre(Boolean grundAusbildungZwoelfJahre) {
    this.grundAusbildungZwoelfJahre = grundAusbildungZwoelfJahre;
    return this;
  }

  
  @JsonProperty("grundAusbildungZwoelfJahre")
  public Boolean getGrundAusbildungZwoelfJahre() {
    return grundAusbildungZwoelfJahre;
  }

  @JsonProperty("grundAusbildungZwoelfJahre")
  public void setGrundAusbildungZwoelfJahre(Boolean grundAusbildungZwoelfJahre) {
    this.grundAusbildungZwoelfJahre = grundAusbildungZwoelfJahre;
  }

  /**
   **/
  public DemoDarlehenGruendeDarlehenDto grundHoheGebuehren(Boolean grundHoheGebuehren) {
    this.grundHoheGebuehren = grundHoheGebuehren;
    return this;
  }

  
  @JsonProperty("grundHoheGebuehren")
  public Boolean getGrundHoheGebuehren() {
    return grundHoheGebuehren;
  }

  @JsonProperty("grundHoheGebuehren")
  public void setGrundHoheGebuehren(Boolean grundHoheGebuehren) {
    this.grundHoheGebuehren = grundHoheGebuehren;
  }

  /**
   **/
  public DemoDarlehenGruendeDarlehenDto grundAnschaffungenFuerAusbildung(Boolean grundAnschaffungenFuerAusbildung) {
    this.grundAnschaffungenFuerAusbildung = grundAnschaffungenFuerAusbildung;
    return this;
  }

  
  @JsonProperty("grundAnschaffungenFuerAusbildung")
  public Boolean getGrundAnschaffungenFuerAusbildung() {
    return grundAnschaffungenFuerAusbildung;
  }

  @JsonProperty("grundAnschaffungenFuerAusbildung")
  public void setGrundAnschaffungenFuerAusbildung(Boolean grundAnschaffungenFuerAusbildung) {
    this.grundAnschaffungenFuerAusbildung = grundAnschaffungenFuerAusbildung;
  }

  /**
   **/
  public DemoDarlehenGruendeDarlehenDto grundZweitausbildung(Boolean grundZweitausbildung) {
    this.grundZweitausbildung = grundZweitausbildung;
    return this;
  }

  
  @JsonProperty("grundZweitausbildung")
  public Boolean getGrundZweitausbildung() {
    return grundZweitausbildung;
  }

  @JsonProperty("grundZweitausbildung")
  public void setGrundZweitausbildung(Boolean grundZweitausbildung) {
    this.grundZweitausbildung = grundZweitausbildung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDarlehenGruendeDarlehenDto demoDarlehenGruendeDarlehen = (DemoDarlehenGruendeDarlehenDto) o;
    return Objects.equals(this.grundNichtBerechtigt, demoDarlehenGruendeDarlehen.grundNichtBerechtigt) &&
        Objects.equals(this.grundAusbildungZwoelfJahre, demoDarlehenGruendeDarlehen.grundAusbildungZwoelfJahre) &&
        Objects.equals(this.grundHoheGebuehren, demoDarlehenGruendeDarlehen.grundHoheGebuehren) &&
        Objects.equals(this.grundAnschaffungenFuerAusbildung, demoDarlehenGruendeDarlehen.grundAnschaffungenFuerAusbildung) &&
        Objects.equals(this.grundZweitausbildung, demoDarlehenGruendeDarlehen.grundZweitausbildung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(grundNichtBerechtigt, grundAusbildungZwoelfJahre, grundHoheGebuehren, grundAnschaffungenFuerAusbildung, grundZweitausbildung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDarlehenGruendeDarlehenDto {\n");
    
    sb.append("    grundNichtBerechtigt: ").append(toIndentedString(grundNichtBerechtigt)).append("\n");
    sb.append("    grundAusbildungZwoelfJahre: ").append(toIndentedString(grundAusbildungZwoelfJahre)).append("\n");
    sb.append("    grundHoheGebuehren: ").append(toIndentedString(grundHoheGebuehren)).append("\n");
    sb.append("    grundAnschaffungenFuerAusbildung: ").append(toIndentedString(grundAnschaffungenFuerAusbildung)).append("\n");
    sb.append("    grundZweitausbildung: ").append(toIndentedString(grundZweitausbildung)).append("\n");
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

