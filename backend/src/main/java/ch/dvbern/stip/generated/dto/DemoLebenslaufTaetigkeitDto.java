package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoLebenslaufTaetigkeit")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoLebenslaufTaetigkeitDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart;
  private @Valid String taetigkeitsBeschreibung;
  private @Valid LocalDate von;
  private @Valid LocalDate bis;
  private @Valid ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz;

  /**
   **/
  public DemoLebenslaufTaetigkeitDto taetigkeitsart(ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart) {
    this.taetigkeitsart = taetigkeitsart;
    return this;
  }

  
  @JsonProperty("taetigkeitsart")
  @NotNull
  public ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart getTaetigkeitsart() {
    return taetigkeitsart;
  }

  @JsonProperty("taetigkeitsart")
  public void setTaetigkeitsart(ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart) {
    this.taetigkeitsart = taetigkeitsart;
  }

  /**
   **/
  public DemoLebenslaufTaetigkeitDto taetigkeitsBeschreibung(String taetigkeitsBeschreibung) {
    this.taetigkeitsBeschreibung = taetigkeitsBeschreibung;
    return this;
  }

  
  @JsonProperty("taetigkeitsBeschreibung")
  @NotNull
  public String getTaetigkeitsBeschreibung() {
    return taetigkeitsBeschreibung;
  }

  @JsonProperty("taetigkeitsBeschreibung")
  public void setTaetigkeitsBeschreibung(String taetigkeitsBeschreibung) {
    this.taetigkeitsBeschreibung = taetigkeitsBeschreibung;
  }

  /**
   **/
  public DemoLebenslaufTaetigkeitDto von(LocalDate von) {
    this.von = von;
    return this;
  }

  
  @JsonProperty("von")
  @NotNull
  public LocalDate getVon() {
    return von;
  }

  @JsonProperty("von")
  public void setVon(LocalDate von) {
    this.von = von;
  }

  /**
   **/
  public DemoLebenslaufTaetigkeitDto bis(LocalDate bis) {
    this.bis = bis;
    return this;
  }

  
  @JsonProperty("bis")
  @NotNull
  public LocalDate getBis() {
    return bis;
  }

  @JsonProperty("bis")
  public void setBis(LocalDate bis) {
    this.bis = bis;
  }

  /**
   **/
  public DemoLebenslaufTaetigkeitDto wohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
    this.wohnsitz = wohnsitz;
    return this;
  }

  
  @JsonProperty("wohnsitz")
  @NotNull
  public ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton getWohnsitz() {
    return wohnsitz;
  }

  @JsonProperty("wohnsitz")
  public void setWohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
    this.wohnsitz = wohnsitz;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoLebenslaufTaetigkeitDto demoLebenslaufTaetigkeit = (DemoLebenslaufTaetigkeitDto) o;
    return Objects.equals(this.taetigkeitsart, demoLebenslaufTaetigkeit.taetigkeitsart) &&
        Objects.equals(this.taetigkeitsBeschreibung, demoLebenslaufTaetigkeit.taetigkeitsBeschreibung) &&
        Objects.equals(this.von, demoLebenslaufTaetigkeit.von) &&
        Objects.equals(this.bis, demoLebenslaufTaetigkeit.bis) &&
        Objects.equals(this.wohnsitz, demoLebenslaufTaetigkeit.wohnsitz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taetigkeitsart, taetigkeitsBeschreibung, von, bis, wohnsitz);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoLebenslaufTaetigkeitDto {\n");
    
    sb.append("    taetigkeitsart: ").append(toIndentedString(taetigkeitsart)).append("\n");
    sb.append("    taetigkeitsBeschreibung: ").append(toIndentedString(taetigkeitsBeschreibung)).append("\n");
    sb.append("    von: ").append(toIndentedString(von)).append("\n");
    sb.append("    bis: ").append(toIndentedString(bis)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
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

