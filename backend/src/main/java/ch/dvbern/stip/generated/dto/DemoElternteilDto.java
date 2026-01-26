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



@JsonTypeName("DemoElternteil")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoElternteilDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp type;
  private @Valid String sozialversicherungsnummer;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String strasse;
  private @Valid String hausnummer;
  private @Valid String plz;
  private @Valid String ort;
  private @Valid String land;
  private @Valid Boolean identischerZivilrechtlicherWohnsitz;
  private @Valid LocalDate geburtsdatum;
  private @Valid String telefonnummer;
  private @Valid Boolean sozialhilfebeitraege;
  private @Valid Boolean ausweisbFluechtling;
  private @Valid String coAdresse;
  private @Valid String identischerZivilrechtlicherWohnsitzPLZ;
  private @Valid String identischerZivilrechtlicherWohnsitzOrt;
  private @Valid Integer wohnkosten;

  /**
   **/
  public DemoElternteilDto type(ch.dvbern.stip.api.eltern.type.ElternTyp type) {
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
  public DemoElternteilDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }

  
  @JsonProperty("sozialversicherungsnummer")
  @NotNull
  public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty("sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   **/
  public DemoElternteilDto nachname(String nachname) {
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
  public DemoElternteilDto vorname(String vorname) {
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
  public DemoElternteilDto strasse(String strasse) {
    this.strasse = strasse;
    return this;
  }

  
  @JsonProperty("strasse")
  @NotNull
  public String getStrasse() {
    return strasse;
  }

  @JsonProperty("strasse")
  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  /**
   **/
  public DemoElternteilDto hausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
    return this;
  }

  
  @JsonProperty("hausnummer")
  @NotNull
  public String getHausnummer() {
    return hausnummer;
  }

  @JsonProperty("hausnummer")
  public void setHausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
  }

  /**
   **/
  public DemoElternteilDto plz(String plz) {
    this.plz = plz;
    return this;
  }

  
  @JsonProperty("plz")
  @NotNull
  public String getPlz() {
    return plz;
  }

  @JsonProperty("plz")
  public void setPlz(String plz) {
    this.plz = plz;
  }

  /**
   **/
  public DemoElternteilDto ort(String ort) {
    this.ort = ort;
    return this;
  }

  
  @JsonProperty("ort")
  @NotNull
  public String getOrt() {
    return ort;
  }

  @JsonProperty("ort")
  public void setOrt(String ort) {
    this.ort = ort;
  }

  /**
   **/
  public DemoElternteilDto land(String land) {
    this.land = land;
    return this;
  }

  
  @JsonProperty("land")
  @NotNull
  public String getLand() {
    return land;
  }

  @JsonProperty("land")
  public void setLand(String land) {
    this.land = land;
  }

  /**
   **/
  public DemoElternteilDto identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
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
  public DemoElternteilDto geburtsdatum(LocalDate geburtsdatum) {
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
  public DemoElternteilDto telefonnummer(String telefonnummer) {
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
  public DemoElternteilDto sozialhilfebeitraege(Boolean sozialhilfebeitraege) {
    this.sozialhilfebeitraege = sozialhilfebeitraege;
    return this;
  }

  
  @JsonProperty("sozialhilfebeitraege")
  @NotNull
  public Boolean getSozialhilfebeitraege() {
    return sozialhilfebeitraege;
  }

  @JsonProperty("sozialhilfebeitraege")
  public void setSozialhilfebeitraege(Boolean sozialhilfebeitraege) {
    this.sozialhilfebeitraege = sozialhilfebeitraege;
  }

  /**
   **/
  public DemoElternteilDto ausweisbFluechtling(Boolean ausweisbFluechtling) {
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
  public DemoElternteilDto coAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
    return this;
  }

  
  @JsonProperty("coAdresse")
  public String getCoAdresse() {
    return coAdresse;
  }

  @JsonProperty("coAdresse")
  public void setCoAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
  }

  /**
   **/
  public DemoElternteilDto identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
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
  public DemoElternteilDto identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
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
   **/
  public DemoElternteilDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoElternteilDto demoElternteil = (DemoElternteilDto) o;
    return Objects.equals(this.type, demoElternteil.type) &&
        Objects.equals(this.sozialversicherungsnummer, demoElternteil.sozialversicherungsnummer) &&
        Objects.equals(this.nachname, demoElternteil.nachname) &&
        Objects.equals(this.vorname, demoElternteil.vorname) &&
        Objects.equals(this.strasse, demoElternteil.strasse) &&
        Objects.equals(this.hausnummer, demoElternteil.hausnummer) &&
        Objects.equals(this.plz, demoElternteil.plz) &&
        Objects.equals(this.ort, demoElternteil.ort) &&
        Objects.equals(this.land, demoElternteil.land) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitz, demoElternteil.identischerZivilrechtlicherWohnsitz) &&
        Objects.equals(this.geburtsdatum, demoElternteil.geburtsdatum) &&
        Objects.equals(this.telefonnummer, demoElternteil.telefonnummer) &&
        Objects.equals(this.sozialhilfebeitraege, demoElternteil.sozialhilfebeitraege) &&
        Objects.equals(this.ausweisbFluechtling, demoElternteil.ausweisbFluechtling) &&
        Objects.equals(this.coAdresse, demoElternteil.coAdresse) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzPLZ, demoElternteil.identischerZivilrechtlicherWohnsitzPLZ) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzOrt, demoElternteil.identischerZivilrechtlicherWohnsitzOrt) &&
        Objects.equals(this.wohnkosten, demoElternteil.wohnkosten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, sozialversicherungsnummer, nachname, vorname, strasse, hausnummer, plz, ort, land, identischerZivilrechtlicherWohnsitz, geburtsdatum, telefonnummer, sozialhilfebeitraege, ausweisbFluechtling, coAdresse, identischerZivilrechtlicherWohnsitzPLZ, identischerZivilrechtlicherWohnsitzOrt, wohnkosten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoElternteilDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitz: ").append(toIndentedString(identischerZivilrechtlicherWohnsitz)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    sozialhilfebeitraege: ").append(toIndentedString(sozialhilfebeitraege)).append("\n");
    sb.append("    ausweisbFluechtling: ").append(toIndentedString(ausweisbFluechtling)).append("\n");
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzPLZ: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzPLZ)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzOrt: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzOrt)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
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

