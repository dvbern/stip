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



@JsonTypeName("DemoAuszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoAuszahlungDto  implements Serializable {
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String strasse;
  private @Valid String hausnummer;
  private @Valid String plz;
  private @Valid String ort;
  private @Valid String land;
  private @Valid String iban;
  private @Valid String coAdresse;

  /**
   **/
  public DemoAuszahlungDto nachname(String nachname) {
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
  public DemoAuszahlungDto vorname(String vorname) {
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
  public DemoAuszahlungDto strasse(String strasse) {
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
  public DemoAuszahlungDto hausnummer(String hausnummer) {
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
  public DemoAuszahlungDto plz(String plz) {
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
  public DemoAuszahlungDto ort(String ort) {
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
  public DemoAuszahlungDto land(String land) {
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
  public DemoAuszahlungDto iban(String iban) {
    this.iban = iban;
    return this;
  }

  
  @JsonProperty("iban")
  @NotNull
  public String getIban() {
    return iban;
  }

  @JsonProperty("iban")
  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   **/
  public DemoAuszahlungDto coAdresse(String coAdresse) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoAuszahlungDto demoAuszahlung = (DemoAuszahlungDto) o;
    return Objects.equals(this.nachname, demoAuszahlung.nachname) &&
        Objects.equals(this.vorname, demoAuszahlung.vorname) &&
        Objects.equals(this.strasse, demoAuszahlung.strasse) &&
        Objects.equals(this.hausnummer, demoAuszahlung.hausnummer) &&
        Objects.equals(this.plz, demoAuszahlung.plz) &&
        Objects.equals(this.ort, demoAuszahlung.ort) &&
        Objects.equals(this.land, demoAuszahlung.land) &&
        Objects.equals(this.iban, demoAuszahlung.iban) &&
        Objects.equals(this.coAdresse, demoAuszahlung.coAdresse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nachname, vorname, strasse, hausnummer, plz, ort, land, iban, coAdresse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoAuszahlungDto {\n");
    
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
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

