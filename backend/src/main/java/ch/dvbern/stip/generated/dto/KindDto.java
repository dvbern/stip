package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Kind")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class KindDto  implements Serializable {
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private @Valid Integer wohnsitzAnteilPia;
  private @Valid String nachname;
  private @Valid UUID id;
  private @Valid Integer erhalteneAlimentebeitraege;

  /**
   **/
  public KindDto vorname(String vorname) {
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
   * dd.MM.yyyy
   **/
  public KindDto geburtsdatum(LocalDate geburtsdatum) {
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
  public KindDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
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
  public KindDto wohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
    this.wohnsitzAnteilPia = wohnsitzAnteilPia;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilPia")
  @NotNull
  public Integer getWohnsitzAnteilPia() {
    return wohnsitzAnteilPia;
  }

  @JsonProperty("wohnsitzAnteilPia")
  public void setWohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
    this.wohnsitzAnteilPia = wohnsitzAnteilPia;
  }

  /**
   **/
  public KindDto nachname(String nachname) {
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
  public KindDto id(UUID id) {
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
  public KindDto erhalteneAlimentebeitraege(Integer erhalteneAlimentebeitraege) {
    this.erhalteneAlimentebeitraege = erhalteneAlimentebeitraege;
    return this;
  }

  
  @JsonProperty("erhalteneAlimentebeitraege")
  public Integer getErhalteneAlimentebeitraege() {
    return erhalteneAlimentebeitraege;
  }

  @JsonProperty("erhalteneAlimentebeitraege")
  public void setErhalteneAlimentebeitraege(Integer erhalteneAlimentebeitraege) {
    this.erhalteneAlimentebeitraege = erhalteneAlimentebeitraege;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KindDto kind = (KindDto) o;
    return Objects.equals(this.vorname, kind.vorname) &&
        Objects.equals(this.geburtsdatum, kind.geburtsdatum) &&
        Objects.equals(this.ausbildungssituation, kind.ausbildungssituation) &&
        Objects.equals(this.wohnsitzAnteilPia, kind.wohnsitzAnteilPia) &&
        Objects.equals(this.nachname, kind.nachname) &&
        Objects.equals(this.id, kind.id) &&
        Objects.equals(this.erhalteneAlimentebeitraege, kind.erhalteneAlimentebeitraege);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, geburtsdatum, ausbildungssituation, wohnsitzAnteilPia, nachname, id, erhalteneAlimentebeitraege);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KindDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    wohnsitzAnteilPia: ").append(toIndentedString(wohnsitzAnteilPia)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    erhalteneAlimentebeitraege: ").append(toIndentedString(erhalteneAlimentebeitraege)).append("\n");
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

