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



@JsonTypeName("KindUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class KindUpdateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private @Valid Integer wohnsitzAnteilPia;
  private @Valid String nachname;
  private @Valid UUID id;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer kinderUndAusbildungszulagen;
  private @Valid Integer renten;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer andereEinnahmen;

  /**
   **/
  public KindUpdateDto vorname(String vorname) {
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
  public KindUpdateDto geburtsdatum(LocalDate geburtsdatum) {
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
  public KindUpdateDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
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
  public KindUpdateDto wohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
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
  public KindUpdateDto nachname(String nachname) {
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
  public KindUpdateDto id(UUID id) {
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
  public KindUpdateDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
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
  public KindUpdateDto kinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
    this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
    return this;
  }

  
  @JsonProperty("kinderUndAusbildungszulagen")
  public Integer getKinderUndAusbildungszulagen() {
    return kinderUndAusbildungszulagen;
  }

  @JsonProperty("kinderUndAusbildungszulagen")
  public void setKinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
    this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
  }

  /**
   **/
  public KindUpdateDto renten(Integer renten) {
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
  public KindUpdateDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
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
  public KindUpdateDto andereEinnahmen(Integer andereEinnahmen) {
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
    KindUpdateDto kindUpdate = (KindUpdateDto) o;
    return Objects.equals(this.vorname, kindUpdate.vorname) &&
        Objects.equals(this.geburtsdatum, kindUpdate.geburtsdatum) &&
        Objects.equals(this.ausbildungssituation, kindUpdate.ausbildungssituation) &&
        Objects.equals(this.wohnsitzAnteilPia, kindUpdate.wohnsitzAnteilPia) &&
        Objects.equals(this.nachname, kindUpdate.nachname) &&
        Objects.equals(this.id, kindUpdate.id) &&
        Objects.equals(this.unterhaltsbeitraege, kindUpdate.unterhaltsbeitraege) &&
        Objects.equals(this.kinderUndAusbildungszulagen, kindUpdate.kinderUndAusbildungszulagen) &&
        Objects.equals(this.renten, kindUpdate.renten) &&
        Objects.equals(this.ergaenzungsleistungen, kindUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.andereEinnahmen, kindUpdate.andereEinnahmen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, geburtsdatum, ausbildungssituation, wohnsitzAnteilPia, nachname, id, unterhaltsbeitraege, kinderUndAusbildungszulagen, renten, ergaenzungsleistungen, andereEinnahmen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KindUpdateDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    wohnsitzAnteilPia: ").append(toIndentedString(wohnsitzAnteilPia)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    kinderUndAusbildungszulagen: ").append(toIndentedString(kinderUndAusbildungszulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
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

