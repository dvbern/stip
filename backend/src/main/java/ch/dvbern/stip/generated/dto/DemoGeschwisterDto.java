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



@JsonTypeName("DemoGeschwister")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoGeschwisterDto  implements Serializable {
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei;
  private @Valid ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private @Valid Integer wohnsitzAnteilVater;
  private @Valid Integer wohnsitzAnteilMutter;

  /**
   **/
  public DemoGeschwisterDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  @NotNull
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public DemoGeschwisterDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public DemoGeschwisterDto geburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DemoGeschwisterDto wohnsitzBei(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei) {
    this.wohnsitzBei = wohnsitzBei;
    return this;
  }

  
  @JsonProperty("wohnsitzBei")
  @NotNull
  public ch.dvbern.stip.api.common.type.Wohnsitz getWohnsitzBei() {
    return wohnsitzBei;
  }

  @JsonProperty("wohnsitzBei")
  public void setWohnsitzBei(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei) {
    this.wohnsitzBei = wohnsitzBei;
  }

  /**
   **/
  public DemoGeschwisterDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
    this.ausbildungssituation = ausbildungssituation;
    return this;
  }

  
  @JsonProperty("ausbildungssituation")
  @NotNull
  public ch.dvbern.stip.api.common.type.Ausbildungssituation getAusbildungssituation() {
    return ausbildungssituation;
  }

  @JsonProperty("ausbildungssituation")
  public void setAusbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
    this.ausbildungssituation = ausbildungssituation;
  }

  /**
   **/
  public DemoGeschwisterDto wohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilVater")
  public Integer getWohnsitzAnteilVater() {
    return wohnsitzAnteilVater;
  }

  @JsonProperty("wohnsitzAnteilVater")
  public void setWohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
  }

  /**
   **/
  public DemoGeschwisterDto wohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilMutter")
  public Integer getWohnsitzAnteilMutter() {
    return wohnsitzAnteilMutter;
  }

  @JsonProperty("wohnsitzAnteilMutter")
  public void setWohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoGeschwisterDto demoGeschwister = (DemoGeschwisterDto) o;
    return Objects.equals(this.nachname, demoGeschwister.nachname) &&
        Objects.equals(this.vorname, demoGeschwister.vorname) &&
        Objects.equals(this.geburtsdatum, demoGeschwister.geburtsdatum) &&
        Objects.equals(this.wohnsitzBei, demoGeschwister.wohnsitzBei) &&
        Objects.equals(this.ausbildungssituation, demoGeschwister.ausbildungssituation) &&
        Objects.equals(this.wohnsitzAnteilVater, demoGeschwister.wohnsitzAnteilVater) &&
        Objects.equals(this.wohnsitzAnteilMutter, demoGeschwister.wohnsitzAnteilMutter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nachname, vorname, geburtsdatum, wohnsitzBei, ausbildungssituation, wohnsitzAnteilVater, wohnsitzAnteilMutter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoGeschwisterDto {\n");
    
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    wohnsitzBei: ").append(toIndentedString(wohnsitzBei)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    wohnsitzAnteilVater: ").append(toIndentedString(wohnsitzAnteilVater)).append("\n");
    sb.append("    wohnsitzAnteilMutter: ").append(toIndentedString(wohnsitzAnteilMutter)).append("\n");
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

