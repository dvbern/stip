package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
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



@JsonTypeName("Partner")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PartnerDto  implements Serializable {
  private @Valid AdresseDto adresse;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid String sozialversicherungsnummer;
  private @Valid String nachname;
  private @Valid Boolean inAusbildung;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum ausbildungspensum;

  /**
   **/
  public PartnerDto adresse(AdresseDto adresse) {
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
  public PartnerDto vorname(String vorname) {
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
  public PartnerDto geburtsdatum(LocalDate geburtsdatum) {
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
  public PartnerDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public PartnerDto nachname(String nachname) {
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
  public PartnerDto inAusbildung(Boolean inAusbildung) {
    this.inAusbildung = inAusbildung;
    return this;
  }

  
  @JsonProperty("inAusbildung")
  public Boolean getInAusbildung() {
    return inAusbildung;
  }

  @JsonProperty("inAusbildung")
  public void setInAusbildung(Boolean inAusbildung) {
    this.inAusbildung = inAusbildung;
  }

  /**
   **/
  public PartnerDto ausbildungspensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum ausbildungspensum) {
    this.ausbildungspensum = ausbildungspensum;
    return this;
  }

  
  @JsonProperty("ausbildungspensum")
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum getAusbildungspensum() {
    return ausbildungspensum;
  }

  @JsonProperty("ausbildungspensum")
  public void setAusbildungspensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum ausbildungspensum) {
    this.ausbildungspensum = ausbildungspensum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartnerDto partner = (PartnerDto) o;
    return Objects.equals(this.adresse, partner.adresse) &&
        Objects.equals(this.vorname, partner.vorname) &&
        Objects.equals(this.geburtsdatum, partner.geburtsdatum) &&
        Objects.equals(this.sozialversicherungsnummer, partner.sozialversicherungsnummer) &&
        Objects.equals(this.nachname, partner.nachname) &&
        Objects.equals(this.inAusbildung, partner.inAusbildung) &&
        Objects.equals(this.ausbildungspensum, partner.ausbildungspensum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, vorname, geburtsdatum, sozialversicherungsnummer, nachname, inAusbildung, ausbildungspensum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartnerDto {\n");
    
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    inAusbildung: ").append(toIndentedString(inAusbildung)).append("\n");
    sb.append("    ausbildungspensum: ").append(toIndentedString(ausbildungspensum)).append("\n");
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

