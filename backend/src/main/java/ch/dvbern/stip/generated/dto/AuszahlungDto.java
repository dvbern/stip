package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Auszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AuszahlungDto  implements Serializable {
  private @Valid String vorname;
  private @Valid AdresseDto adresse;
  private @Valid String iban;
  private @Valid String nachname;
  private @Valid ch.dvbern.stip.api.auszahlung.type.Kontoinhaber kontoinhaber;
  private @Valid Integer businessPartnerId;

  /**
   **/
  public AuszahlungDto vorname(String vorname) {
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
  public AuszahlungDto adresse(AdresseDto adresse) {
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
  public AuszahlungDto iban(String iban) {
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
  public AuszahlungDto nachname(String nachname) {
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
  public AuszahlungDto kontoinhaber(ch.dvbern.stip.api.auszahlung.type.Kontoinhaber kontoinhaber) {
    this.kontoinhaber = kontoinhaber;
    return this;
  }

  
  @JsonProperty("kontoinhaber")
  @NotNull
  public ch.dvbern.stip.api.auszahlung.type.Kontoinhaber getKontoinhaber() {
    return kontoinhaber;
  }

  @JsonProperty("kontoinhaber")
  public void setKontoinhaber(ch.dvbern.stip.api.auszahlung.type.Kontoinhaber kontoinhaber) {
    this.kontoinhaber = kontoinhaber;
  }

  /**
   **/
  public AuszahlungDto businessPartnerId(Integer businessPartnerId) {
    this.businessPartnerId = businessPartnerId;
    return this;
  }

  
  @JsonProperty("businessPartnerId")
  public Integer getBusinessPartnerId() {
    return businessPartnerId;
  }

  @JsonProperty("businessPartnerId")
  public void setBusinessPartnerId(Integer businessPartnerId) {
    this.businessPartnerId = businessPartnerId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuszahlungDto auszahlung = (AuszahlungDto) o;
    return Objects.equals(this.vorname, auszahlung.vorname) &&
        Objects.equals(this.adresse, auszahlung.adresse) &&
        Objects.equals(this.iban, auszahlung.iban) &&
        Objects.equals(this.nachname, auszahlung.nachname) &&
        Objects.equals(this.kontoinhaber, auszahlung.kontoinhaber) &&
        Objects.equals(this.businessPartnerId, auszahlung.businessPartnerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, adresse, iban, nachname, kontoinhaber, businessPartnerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuszahlungDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    kontoinhaber: ").append(toIndentedString(kontoinhaber)).append("\n");
    sb.append("    businessPartnerId: ").append(toIndentedString(businessPartnerId)).append("\n");
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

