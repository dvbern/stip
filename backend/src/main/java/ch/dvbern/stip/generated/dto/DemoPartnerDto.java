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



@JsonTypeName("DemoPartner")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoPartnerDto  implements Serializable {
  private @Valid String sozialversicherungsnummer;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String strasse;
  private @Valid String hausnummer;
  private @Valid String plz;
  private @Valid String ort;
  private @Valid String land;
  private @Valid LocalDate geburtsdatum;
  private @Valid Boolean inAusbildung;
  private @Valid String coAdresse;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum;

  /**
   **/
  public DemoPartnerDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public DemoPartnerDto nachname(String nachname) {
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
  public DemoPartnerDto vorname(String vorname) {
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
  public DemoPartnerDto strasse(String strasse) {
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
  public DemoPartnerDto hausnummer(String hausnummer) {
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
  public DemoPartnerDto plz(String plz) {
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
  public DemoPartnerDto ort(String ort) {
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
  public DemoPartnerDto land(String land) {
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
  public DemoPartnerDto geburtsdatum(LocalDate geburtsdatum) {
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
  public DemoPartnerDto inAusbildung(Boolean inAusbildung) {
    this.inAusbildung = inAusbildung;
    return this;
  }

  
  @JsonProperty("inAusbildung")
  @NotNull
  public Boolean getInAusbildung() {
    return inAusbildung;
  }

  @JsonProperty("inAusbildung")
  public void setInAusbildung(Boolean inAusbildung) {
    this.inAusbildung = inAusbildung;
  }

  /**
   **/
  public DemoPartnerDto coAdresse(String coAdresse) {
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
  public DemoPartnerDto pensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
    return this;
  }

  
  @JsonProperty("pensum")
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum getPensum() {
    return pensum;
  }

  @JsonProperty("pensum")
  public void setPensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoPartnerDto demoPartner = (DemoPartnerDto) o;
    return Objects.equals(this.sozialversicherungsnummer, demoPartner.sozialversicherungsnummer) &&
        Objects.equals(this.nachname, demoPartner.nachname) &&
        Objects.equals(this.vorname, demoPartner.vorname) &&
        Objects.equals(this.strasse, demoPartner.strasse) &&
        Objects.equals(this.hausnummer, demoPartner.hausnummer) &&
        Objects.equals(this.plz, demoPartner.plz) &&
        Objects.equals(this.ort, demoPartner.ort) &&
        Objects.equals(this.land, demoPartner.land) &&
        Objects.equals(this.geburtsdatum, demoPartner.geburtsdatum) &&
        Objects.equals(this.inAusbildung, demoPartner.inAusbildung) &&
        Objects.equals(this.coAdresse, demoPartner.coAdresse) &&
        Objects.equals(this.pensum, demoPartner.pensum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sozialversicherungsnummer, nachname, vorname, strasse, hausnummer, plz, ort, land, geburtsdatum, inAusbildung, coAdresse, pensum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoPartnerDto {\n");
    
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    inAusbildung: ").append(toIndentedString(inAusbildung)).append("\n");
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
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

