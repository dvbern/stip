package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
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



@JsonTypeName("ElternUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ElternUpdateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid AdresseDto adresse;
  private @Valid Boolean identischerZivilrechtlicherWohnsitz;
  private @Valid String telefonnummer;
  private @Valid Integer wohnkosten;
  private @Valid LocalDate geburtsdatum;
  private @Valid Boolean sozialhilfebeitraegeAusbezahlt;
  private @Valid Boolean ausweisbFluechtling;
  private @Valid Boolean ergaenzungsleistungAusbezahlt;
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp;
  private @Valid String nachname;
  private @Valid String identischerZivilrechtlicherWohnsitzOrt;
  private @Valid String identischerZivilrechtlicherWohnsitzPLZ;
  private @Valid String sozialversicherungsnummer;
  private @Valid UUID id;

  /**
   **/
  public ElternUpdateDto vorname(String vorname) {
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
  public ElternUpdateDto adresse(AdresseDto adresse) {
    this.adresse = adresse;
    return this;
  }

  
  @JsonProperty("adresse")
  @NotNull
  public AdresseDto getAdresse() {
    return adresse;
  }

  @JsonProperty("adresse")
  public void setAdresse(AdresseDto adresse) {
    this.adresse = adresse;
  }

  /**
   **/
  public ElternUpdateDto identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
    this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
    return this;
  }

  
  @JsonProperty("identischerZivilrechtlicherWohnsitz")
  @NotNull
  public Boolean getIdentischerZivilrechtlicherWohnsitz() {
    return identischerZivilrechtlicherWohnsitz;
  }

  @JsonProperty("identischerZivilrechtlicherWohnsitz")
  public void setIdentischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
    this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
  }

  /**
   **/
  public ElternUpdateDto telefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
    return this;
  }

  
  @JsonProperty("telefonnummer")
  @NotNull
  public String getTelefonnummer() {
    return telefonnummer;
  }

  @JsonProperty("telefonnummer")
  public void setTelefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
  }

  /**
   **/
  public ElternUpdateDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  @NotNull
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   * dd.MM.YYYY
   **/
  public ElternUpdateDto geburtsdatum(LocalDate geburtsdatum) {
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
  public ElternUpdateDto sozialhilfebeitraegeAusbezahlt(Boolean sozialhilfebeitraegeAusbezahlt) {
    this.sozialhilfebeitraegeAusbezahlt = sozialhilfebeitraegeAusbezahlt;
    return this;
  }

  
  @JsonProperty("sozialhilfebeitraegeAusbezahlt")
  @NotNull
  public Boolean getSozialhilfebeitraegeAusbezahlt() {
    return sozialhilfebeitraegeAusbezahlt;
  }

  @JsonProperty("sozialhilfebeitraegeAusbezahlt")
  public void setSozialhilfebeitraegeAusbezahlt(Boolean sozialhilfebeitraegeAusbezahlt) {
    this.sozialhilfebeitraegeAusbezahlt = sozialhilfebeitraegeAusbezahlt;
  }

  /**
   **/
  public ElternUpdateDto ausweisbFluechtling(Boolean ausweisbFluechtling) {
    this.ausweisbFluechtling = ausweisbFluechtling;
    return this;
  }

  
  @JsonProperty("ausweisbFluechtling")
  @NotNull
  public Boolean getAusweisbFluechtling() {
    return ausweisbFluechtling;
  }

  @JsonProperty("ausweisbFluechtling")
  public void setAusweisbFluechtling(Boolean ausweisbFluechtling) {
    this.ausweisbFluechtling = ausweisbFluechtling;
  }

  /**
   **/
  public ElternUpdateDto ergaenzungsleistungAusbezahlt(Boolean ergaenzungsleistungAusbezahlt) {
    this.ergaenzungsleistungAusbezahlt = ergaenzungsleistungAusbezahlt;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungAusbezahlt")
  @NotNull
  public Boolean getErgaenzungsleistungAusbezahlt() {
    return ergaenzungsleistungAusbezahlt;
  }

  @JsonProperty("ergaenzungsleistungAusbezahlt")
  public void setErgaenzungsleistungAusbezahlt(Boolean ergaenzungsleistungAusbezahlt) {
    this.ergaenzungsleistungAusbezahlt = ergaenzungsleistungAusbezahlt;
  }

  /**
   **/
  public ElternUpdateDto elternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
    this.elternTyp = elternTyp;
    return this;
  }

  
  @JsonProperty("elternTyp")
  @NotNull
  public ch.dvbern.stip.api.eltern.type.ElternTyp getElternTyp() {
    return elternTyp;
  }

  @JsonProperty("elternTyp")
  public void setElternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
    this.elternTyp = elternTyp;
  }

  /**
   **/
  public ElternUpdateDto nachname(String nachname) {
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
   * Required wenn identischerZivilrechtlicherWohnsitz &#x3D; false
   **/
  public ElternUpdateDto identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
    this.identischerZivilrechtlicherWohnsitzOrt = identischerZivilrechtlicherWohnsitzOrt;
    return this;
  }

  
  @JsonProperty("identischerZivilrechtlicherWohnsitzOrt")
  public String getIdentischerZivilrechtlicherWohnsitzOrt() {
    return identischerZivilrechtlicherWohnsitzOrt;
  }

  @JsonProperty("identischerZivilrechtlicherWohnsitzOrt")
  public void setIdentischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
    this.identischerZivilrechtlicherWohnsitzOrt = identischerZivilrechtlicherWohnsitzOrt;
  }

  /**
   * Required wenn identischerZivilrechtlicherWohnsitz &#x3D; false
   **/
  public ElternUpdateDto identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
    this.identischerZivilrechtlicherWohnsitzPLZ = identischerZivilrechtlicherWohnsitzPLZ;
    return this;
  }

  
  @JsonProperty("identischerZivilrechtlicherWohnsitzPLZ")
  public String getIdentischerZivilrechtlicherWohnsitzPLZ() {
    return identischerZivilrechtlicherWohnsitzPLZ;
  }

  @JsonProperty("identischerZivilrechtlicherWohnsitzPLZ")
  public void setIdentischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
    this.identischerZivilrechtlicherWohnsitzPLZ = identischerZivilrechtlicherWohnsitzPLZ;
  }

  /**
   **/
  public ElternUpdateDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }

  
  @JsonProperty("sozialversicherungsnummer")
  public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty("sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   **/
  public ElternUpdateDto id(UUID id) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElternUpdateDto elternUpdate = (ElternUpdateDto) o;
    return Objects.equals(this.vorname, elternUpdate.vorname) &&
        Objects.equals(this.adresse, elternUpdate.adresse) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitz, elternUpdate.identischerZivilrechtlicherWohnsitz) &&
        Objects.equals(this.telefonnummer, elternUpdate.telefonnummer) &&
        Objects.equals(this.wohnkosten, elternUpdate.wohnkosten) &&
        Objects.equals(this.geburtsdatum, elternUpdate.geburtsdatum) &&
        Objects.equals(this.sozialhilfebeitraegeAusbezahlt, elternUpdate.sozialhilfebeitraegeAusbezahlt) &&
        Objects.equals(this.ausweisbFluechtling, elternUpdate.ausweisbFluechtling) &&
        Objects.equals(this.ergaenzungsleistungAusbezahlt, elternUpdate.ergaenzungsleistungAusbezahlt) &&
        Objects.equals(this.elternTyp, elternUpdate.elternTyp) &&
        Objects.equals(this.nachname, elternUpdate.nachname) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzOrt, elternUpdate.identischerZivilrechtlicherWohnsitzOrt) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzPLZ, elternUpdate.identischerZivilrechtlicherWohnsitzPLZ) &&
        Objects.equals(this.sozialversicherungsnummer, elternUpdate.sozialversicherungsnummer) &&
        Objects.equals(this.id, elternUpdate.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, adresse, identischerZivilrechtlicherWohnsitz, telefonnummer, wohnkosten, geburtsdatum, sozialhilfebeitraegeAusbezahlt, ausweisbFluechtling, ergaenzungsleistungAusbezahlt, elternTyp, nachname, identischerZivilrechtlicherWohnsitzOrt, identischerZivilrechtlicherWohnsitzPLZ, sozialversicherungsnummer, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElternUpdateDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitz: ").append(toIndentedString(identischerZivilrechtlicherWohnsitz)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    sozialhilfebeitraegeAusbezahlt: ").append(toIndentedString(sozialhilfebeitraegeAusbezahlt)).append("\n");
    sb.append("    ausweisbFluechtling: ").append(toIndentedString(ausweisbFluechtling)).append("\n");
    sb.append("    ergaenzungsleistungAusbezahlt: ").append(toIndentedString(ergaenzungsleistungAusbezahlt)).append("\n");
    sb.append("    elternTyp: ").append(toIndentedString(elternTyp)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzOrt: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzOrt)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzPLZ: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzPLZ)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

