package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoDarlehenGruendeDto;
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
  private @Valid Integer betragGewuenscht;
  private @Valid Integer schulden;
  private @Valid Integer anzahlBetreibungen;
  private @Valid DemoDarlehenGruendeDto gruende;

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
  public DemoDarlehenDto betragGewuenscht(Integer betragGewuenscht) {
    this.betragGewuenscht = betragGewuenscht;
    return this;
  }

  
  @JsonProperty("betragGewuenscht")
  @NotNull
  public Integer getBetragGewuenscht() {
    return betragGewuenscht;
  }

  @JsonProperty("betragGewuenscht")
  public void setBetragGewuenscht(Integer betragGewuenscht) {
    this.betragGewuenscht = betragGewuenscht;
  }

  /**
   **/
  public DemoDarlehenDto schulden(Integer schulden) {
    this.schulden = schulden;
    return this;
  }

  
  @JsonProperty("schulden")
  @NotNull
  public Integer getSchulden() {
    return schulden;
  }

  @JsonProperty("schulden")
  public void setSchulden(Integer schulden) {
    this.schulden = schulden;
  }

  /**
   **/
  public DemoDarlehenDto anzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
    return this;
  }

  
  @JsonProperty("anzahlBetreibungen")
  @NotNull
  public Integer getAnzahlBetreibungen() {
    return anzahlBetreibungen;
  }

  @JsonProperty("anzahlBetreibungen")
  public void setAnzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
  }

  /**
   **/
  public DemoDarlehenDto gruende(DemoDarlehenGruendeDto gruende) {
    this.gruende = gruende;
    return this;
  }

  
  @JsonProperty("gruende")
  @NotNull
  public DemoDarlehenGruendeDto getGruende() {
    return gruende;
  }

  @JsonProperty("gruende")
  public void setGruende(DemoDarlehenGruendeDto gruende) {
    this.gruende = gruende;
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
        Objects.equals(this.betragGewuenscht, demoDarlehen.betragGewuenscht) &&
        Objects.equals(this.schulden, demoDarlehen.schulden) &&
        Objects.equals(this.anzahlBetreibungen, demoDarlehen.anzahlBetreibungen) &&
        Objects.equals(this.gruende, demoDarlehen.gruende);
  }

  @Override
  public int hashCode() {
    return Objects.hash(willDarlehen, betragGewuenscht, schulden, anzahlBetreibungen, gruende);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDarlehenDto {\n");
    
    sb.append("    willDarlehen: ").append(toIndentedString(willDarlehen)).append("\n");
    sb.append("    betragGewuenscht: ").append(toIndentedString(betragGewuenscht)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    gruende: ").append(toIndentedString(gruende)).append("\n");
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

