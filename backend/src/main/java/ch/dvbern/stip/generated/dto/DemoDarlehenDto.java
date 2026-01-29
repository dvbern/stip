package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoDarlehenGruendeDarlehenDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoDarlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDarlehenDto  implements Serializable {
  private @Valid Boolean willDarlehen;
  private @Valid String betragDarlehen;
  private @Valid String schulden;
  private @Valid String anzahlBetreibungen;
  private @Valid DemoDarlehenGruendeDarlehenDto gruendeDarlehen;

  /**
   **/
  public DemoDarlehenDto willDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
    return this;
  }

  
  @JsonProperty("willDarlehen")
  @NotNull
  public Boolean getWillDarlehen() {
    return willDarlehen;
  }

  @JsonProperty("willDarlehen")
  public void setWillDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
  }

  /**
   **/
  public DemoDarlehenDto betragDarlehen(String betragDarlehen) {
    this.betragDarlehen = betragDarlehen;
    return this;
  }

  
  @JsonProperty("betragDarlehen")
  @NotNull
  public String getBetragDarlehen() {
    return betragDarlehen;
  }

  @JsonProperty("betragDarlehen")
  public void setBetragDarlehen(String betragDarlehen) {
    this.betragDarlehen = betragDarlehen;
  }

  /**
   **/
  public DemoDarlehenDto schulden(String schulden) {
    this.schulden = schulden;
    return this;
  }

  
  @JsonProperty("schulden")
  @NotNull
  public String getSchulden() {
    return schulden;
  }

  @JsonProperty("schulden")
  public void setSchulden(String schulden) {
    this.schulden = schulden;
  }

  /**
   **/
  public DemoDarlehenDto anzahlBetreibungen(String anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
    return this;
  }

  
  @JsonProperty("anzahlBetreibungen")
  @NotNull
  public String getAnzahlBetreibungen() {
    return anzahlBetreibungen;
  }

  @JsonProperty("anzahlBetreibungen")
  public void setAnzahlBetreibungen(String anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
  }

  /**
   **/
  public DemoDarlehenDto gruendeDarlehen(DemoDarlehenGruendeDarlehenDto gruendeDarlehen) {
    this.gruendeDarlehen = gruendeDarlehen;
    return this;
  }

  
  @JsonProperty("gruendeDarlehen")
  @NotNull
  public DemoDarlehenGruendeDarlehenDto getGruendeDarlehen() {
    return gruendeDarlehen;
  }

  @JsonProperty("gruendeDarlehen")
  public void setGruendeDarlehen(DemoDarlehenGruendeDarlehenDto gruendeDarlehen) {
    this.gruendeDarlehen = gruendeDarlehen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDarlehenDto demoDarlehen = (DemoDarlehenDto) o;
    return Objects.equals(this.willDarlehen, demoDarlehen.willDarlehen) &&
        Objects.equals(this.betragDarlehen, demoDarlehen.betragDarlehen) &&
        Objects.equals(this.schulden, demoDarlehen.schulden) &&
        Objects.equals(this.anzahlBetreibungen, demoDarlehen.anzahlBetreibungen) &&
        Objects.equals(this.gruendeDarlehen, demoDarlehen.gruendeDarlehen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(willDarlehen, betragDarlehen, schulden, anzahlBetreibungen, gruendeDarlehen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDarlehenDto {\n");
    
    sb.append("    willDarlehen: ").append(toIndentedString(willDarlehen)).append("\n");
    sb.append("    betragDarlehen: ").append(toIndentedString(betragDarlehen)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    gruendeDarlehen: ").append(toIndentedString(gruendeDarlehen)).append("\n");
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

