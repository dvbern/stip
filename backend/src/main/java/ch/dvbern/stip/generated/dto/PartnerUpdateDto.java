package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("PartnerUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PartnerUpdateDto  implements Serializable {
  private @Valid AdresseDto adresse;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid String sozialversicherungsnummer;
  private @Valid String nachname;
  private @Valid Boolean ausbildungMitEinkommenOderErwerbstaetig;
  private @Valid Integer jahreseinkommen;
  private @Valid Integer fahrkosten;
  private @Valid Integer verpflegungskosten;

  /**
   **/
  public PartnerUpdateDto adresse(AdresseDto adresse) {
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
  public PartnerUpdateDto vorname(String vorname) {
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
  public PartnerUpdateDto geburtsdatum(LocalDate geburtsdatum) {
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
  public PartnerUpdateDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public PartnerUpdateDto nachname(String nachname) {
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
  public PartnerUpdateDto ausbildungMitEinkommenOderErwerbstaetig(Boolean ausbildungMitEinkommenOderErwerbstaetig) {
    this.ausbildungMitEinkommenOderErwerbstaetig = ausbildungMitEinkommenOderErwerbstaetig;
    return this;
  }


  @JsonProperty("ausbildungMitEinkommenOderErwerbstaetig")
  public Boolean getAusbildungMitEinkommenOderErwerbstaetig() {
    return ausbildungMitEinkommenOderErwerbstaetig;
  }

  @JsonProperty("ausbildungMitEinkommenOderErwerbstaetig")
  public void setAusbildungMitEinkommenOderErwerbstaetig(Boolean ausbildungMitEinkommenOderErwerbstaetig) {
    this.ausbildungMitEinkommenOderErwerbstaetig = ausbildungMitEinkommenOderErwerbstaetig;
  }

  /**
   * Required falls ausbildungMitEinkommenOderErwerbstaetig true ist
   **/
  public PartnerUpdateDto jahreseinkommen(Integer jahreseinkommen) {
    this.jahreseinkommen = jahreseinkommen;
    return this;
  }


  @JsonProperty("jahreseinkommen")
  public Integer getJahreseinkommen() {
    return jahreseinkommen;
  }

  @JsonProperty("jahreseinkommen")
  public void setJahreseinkommen(Integer jahreseinkommen) {
    this.jahreseinkommen = jahreseinkommen;
  }

  /**
   * Required falls ausbildungMitEinkommenOderErwerbstaetig true ist
   **/
  public PartnerUpdateDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }


  @JsonProperty("fahrkosten")
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   * Required falls ausbildungMitEinkommenOderErwerbstaetig true ist
   **/
  public PartnerUpdateDto verpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
    return this;
  }


  @JsonProperty("verpflegungskosten")
  public Integer getVerpflegungskosten() {
    return verpflegungskosten;
  }

  @JsonProperty("verpflegungskosten")
  public void setVerpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartnerUpdateDto partnerUpdate = (PartnerUpdateDto) o;
    return Objects.equals(this.adresse, partnerUpdate.adresse) &&
        Objects.equals(this.vorname, partnerUpdate.vorname) &&
        Objects.equals(this.geburtsdatum, partnerUpdate.geburtsdatum) &&
        Objects.equals(this.sozialversicherungsnummer, partnerUpdate.sozialversicherungsnummer) &&
        Objects.equals(this.nachname, partnerUpdate.nachname) &&
        Objects.equals(this.ausbildungMitEinkommenOderErwerbstaetig, partnerUpdate.ausbildungMitEinkommenOderErwerbstaetig) &&
        Objects.equals(this.jahreseinkommen, partnerUpdate.jahreseinkommen) &&
        Objects.equals(this.fahrkosten, partnerUpdate.fahrkosten) &&
        Objects.equals(this.verpflegungskosten, partnerUpdate.verpflegungskosten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, vorname, geburtsdatum, sozialversicherungsnummer, nachname, ausbildungMitEinkommenOderErwerbstaetig, jahreseinkommen, fahrkosten, verpflegungskosten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartnerUpdateDto {\n");

    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    ausbildungMitEinkommenOderErwerbstaetig: ").append(toIndentedString(ausbildungMitEinkommenOderErwerbstaetig)).append("\n");
    sb.append("    jahreseinkommen: ").append(toIndentedString(jahreseinkommen)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
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

