package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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



@JsonTypeName("PersonInAusbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PersonInAusbildungDto  implements Serializable {
  private @Valid AdresseDto adresse;
  private @Valid String sozialversicherungsnummer;
  private @Valid String vorname;
  private @Valid ch.dvbern.stip.api.common.type.Anrede anrede;
  private @Valid Boolean identischerZivilrechtlicherWohnsitz;
  private @Valid String email;
  private @Valid String telefonnummer;
  private @Valid LocalDate geburtsdatum;
  private @Valid UUID nationalitaetId;
  private @Valid ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz;
  private @Valid Boolean sozialhilfebeitraege;
  private @Valid String nachname;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Sprache korrespondenzSprache;
  private @Valid String heimatort;
  private @Valid String heimatPLZ;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus;
  private @Valid LocalDate einreisedatum;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand;
  private @Valid BigDecimal wohnsitzAnteilMutter;
  private @Valid BigDecimal wohnsitzAnteilVater;
  private @Valid Boolean vormundschaft;
  private @Valid String identischerZivilrechtlicherWohnsitzOrt;
  private @Valid String identischerZivilrechtlicherWohnsitzPLZ;
  private @Valid ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB;

  /**
   **/
  public PersonInAusbildungDto adresse(AdresseDto adresse) {
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
  public PersonInAusbildungDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public PersonInAusbildungDto vorname(String vorname) {
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
  public PersonInAusbildungDto anrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
    this.anrede = anrede;
    return this;
  }

  
  @JsonProperty("anrede")
  @NotNull
  public ch.dvbern.stip.api.common.type.Anrede getAnrede() {
    return anrede;
  }

  @JsonProperty("anrede")
  public void setAnrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
    this.anrede = anrede;
  }

  /**
   **/
  public PersonInAusbildungDto identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
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
  public PersonInAusbildungDto email(String email) {
    this.email = email;
    return this;
  }

  
  @JsonProperty("email")
  @NotNull
  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   **/
  public PersonInAusbildungDto telefonnummer(String telefonnummer) {
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
  public PersonInAusbildungDto geburtsdatum(LocalDate geburtsdatum) {
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
  public PersonInAusbildungDto nationalitaetId(UUID nationalitaetId) {
    this.nationalitaetId = nationalitaetId;
    return this;
  }

  
  @JsonProperty("nationalitaetId")
  @NotNull
  public UUID getNationalitaetId() {
    return nationalitaetId;
  }

  @JsonProperty("nationalitaetId")
  public void setNationalitaetId(UUID nationalitaetId) {
    this.nationalitaetId = nationalitaetId;
  }

  /**
   **/
  public PersonInAusbildungDto wohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
    this.wohnsitz = wohnsitz;
    return this;
  }

  
  @JsonProperty("wohnsitz")
  @NotNull
  public ch.dvbern.stip.api.common.type.Wohnsitz getWohnsitz() {
    return wohnsitz;
  }

  @JsonProperty("wohnsitz")
  public void setWohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
    this.wohnsitz = wohnsitz;
  }

  /**
   **/
  public PersonInAusbildungDto sozialhilfebeitraege(Boolean sozialhilfebeitraege) {
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
  public PersonInAusbildungDto nachname(String nachname) {
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
  public PersonInAusbildungDto korrespondenzSprache(ch.dvbern.stip.api.personinausbildung.type.Sprache korrespondenzSprache) {
    this.korrespondenzSprache = korrespondenzSprache;
    return this;
  }

  
  @JsonProperty("korrespondenzSprache")
  @NotNull
  public ch.dvbern.stip.api.personinausbildung.type.Sprache getKorrespondenzSprache() {
    return korrespondenzSprache;
  }

  @JsonProperty("korrespondenzSprache")
  public void setKorrespondenzSprache(ch.dvbern.stip.api.personinausbildung.type.Sprache korrespondenzSprache) {
    this.korrespondenzSprache = korrespondenzSprache;
  }

  /**
   * Required nur wenn Land &#x3D; CH
   **/
  public PersonInAusbildungDto heimatort(String heimatort) {
    this.heimatort = heimatort;
    return this;
  }

  
  @JsonProperty("heimatort")
  public String getHeimatort() {
    return heimatort;
  }

  @JsonProperty("heimatort")
  public void setHeimatort(String heimatort) {
    this.heimatort = heimatort;
  }

  /**
   * Required nur wenn Land &#x3D; CH
   **/
  public PersonInAusbildungDto heimatPLZ(String heimatPLZ) {
    this.heimatPLZ = heimatPLZ;
    return this;
  }

  
  @JsonProperty("heimatPLZ")
  public String getHeimatPLZ() {
    return heimatPLZ;
  }

  @JsonProperty("heimatPLZ")
  public void setHeimatPLZ(String heimatPLZ) {
    this.heimatPLZ = heimatPLZ;
  }

  /**
   **/
  public PersonInAusbildungDto niederlassungsstatus(ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus) {
    this.niederlassungsstatus = niederlassungsstatus;
    return this;
  }

  
  @JsonProperty("niederlassungsstatus")
  public ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus getNiederlassungsstatus() {
    return niederlassungsstatus;
  }

  @JsonProperty("niederlassungsstatus")
  public void setNiederlassungsstatus(ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus) {
    this.niederlassungsstatus = niederlassungsstatus;
  }

  /**
   **/
  public PersonInAusbildungDto einreisedatum(LocalDate einreisedatum) {
    this.einreisedatum = einreisedatum;
    return this;
  }

  
  @JsonProperty("einreisedatum")
  public LocalDate getEinreisedatum() {
    return einreisedatum;
  }

  @JsonProperty("einreisedatum")
  public void setEinreisedatum(LocalDate einreisedatum) {
    this.einreisedatum = einreisedatum;
  }

  /**
   **/
  public PersonInAusbildungDto zivilstand(ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand) {
    this.zivilstand = zivilstand;
    return this;
  }

  
  @JsonProperty("zivilstand")
  public ch.dvbern.stip.api.personinausbildung.type.Zivilstand getZivilstand() {
    return zivilstand;
  }

  @JsonProperty("zivilstand")
  public void setZivilstand(ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand) {
    this.zivilstand = zivilstand;
  }

  /**
   * Required wenn Wohnsitz.MUTTER_VATER.
   **/
  public PersonInAusbildungDto wohnsitzAnteilMutter(BigDecimal wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilMutter")
  public BigDecimal getWohnsitzAnteilMutter() {
    return wohnsitzAnteilMutter;
  }

  @JsonProperty("wohnsitzAnteilMutter")
  public void setWohnsitzAnteilMutter(BigDecimal wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
  }

  /**
   * Required wenn Wohnsitz.MUTTER_VATER.
   **/
  public PersonInAusbildungDto wohnsitzAnteilVater(BigDecimal wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilVater")
  public BigDecimal getWohnsitzAnteilVater() {
    return wohnsitzAnteilVater;
  }

  @JsonProperty("wohnsitzAnteilVater")
  public void setWohnsitzAnteilVater(BigDecimal wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
  }

  /**
   * Required nur wenn Land &#x3D; CH
   **/
  public PersonInAusbildungDto vormundschaft(Boolean vormundschaft) {
    this.vormundschaft = vormundschaft;
    return this;
  }

  
  @JsonProperty("vormundschaft")
  public Boolean getVormundschaft() {
    return vormundschaft;
  }

  @JsonProperty("vormundschaft")
  public void setVormundschaft(Boolean vormundschaft) {
    this.vormundschaft = vormundschaft;
  }

  /**
   * Required wenn identischerZivilrechtlicherWohnsitz &#x3D; false
   **/
  public PersonInAusbildungDto identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
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
  public PersonInAusbildungDto identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
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
  public PersonInAusbildungDto zustaendigeKESB(ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB) {
    this.zustaendigeKESB = zustaendigeKESB;
    return this;
  }

  
  @JsonProperty("zustaendigeKESB")
  public ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB getZustaendigeKESB() {
    return zustaendigeKESB;
  }

  @JsonProperty("zustaendigeKESB")
  public void setZustaendigeKESB(ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB) {
    this.zustaendigeKESB = zustaendigeKESB;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonInAusbildungDto personInAusbildung = (PersonInAusbildungDto) o;
    return Objects.equals(this.adresse, personInAusbildung.adresse) &&
        Objects.equals(this.sozialversicherungsnummer, personInAusbildung.sozialversicherungsnummer) &&
        Objects.equals(this.vorname, personInAusbildung.vorname) &&
        Objects.equals(this.anrede, personInAusbildung.anrede) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitz, personInAusbildung.identischerZivilrechtlicherWohnsitz) &&
        Objects.equals(this.email, personInAusbildung.email) &&
        Objects.equals(this.telefonnummer, personInAusbildung.telefonnummer) &&
        Objects.equals(this.geburtsdatum, personInAusbildung.geburtsdatum) &&
        Objects.equals(this.nationalitaetId, personInAusbildung.nationalitaetId) &&
        Objects.equals(this.wohnsitz, personInAusbildung.wohnsitz) &&
        Objects.equals(this.sozialhilfebeitraege, personInAusbildung.sozialhilfebeitraege) &&
        Objects.equals(this.nachname, personInAusbildung.nachname) &&
        Objects.equals(this.korrespondenzSprache, personInAusbildung.korrespondenzSprache) &&
        Objects.equals(this.heimatort, personInAusbildung.heimatort) &&
        Objects.equals(this.heimatPLZ, personInAusbildung.heimatPLZ) &&
        Objects.equals(this.niederlassungsstatus, personInAusbildung.niederlassungsstatus) &&
        Objects.equals(this.einreisedatum, personInAusbildung.einreisedatum) &&
        Objects.equals(this.zivilstand, personInAusbildung.zivilstand) &&
        Objects.equals(this.wohnsitzAnteilMutter, personInAusbildung.wohnsitzAnteilMutter) &&
        Objects.equals(this.wohnsitzAnteilVater, personInAusbildung.wohnsitzAnteilVater) &&
        Objects.equals(this.vormundschaft, personInAusbildung.vormundschaft) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzOrt, personInAusbildung.identischerZivilrechtlicherWohnsitzOrt) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzPLZ, personInAusbildung.identischerZivilrechtlicherWohnsitzPLZ) &&
        Objects.equals(this.zustaendigeKESB, personInAusbildung.zustaendigeKESB);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, sozialversicherungsnummer, vorname, anrede, identischerZivilrechtlicherWohnsitz, email, telefonnummer, geburtsdatum, nationalitaetId, wohnsitz, sozialhilfebeitraege, nachname, korrespondenzSprache, heimatort, heimatPLZ, niederlassungsstatus, einreisedatum, zivilstand, wohnsitzAnteilMutter, wohnsitzAnteilVater, vormundschaft, identischerZivilrechtlicherWohnsitzOrt, identischerZivilrechtlicherWohnsitzPLZ, zustaendigeKESB);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonInAusbildungDto {\n");
    
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    anrede: ").append(toIndentedString(anrede)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitz: ").append(toIndentedString(identischerZivilrechtlicherWohnsitz)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    nationalitaetId: ").append(toIndentedString(nationalitaetId)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
    sb.append("    sozialhilfebeitraege: ").append(toIndentedString(sozialhilfebeitraege)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    korrespondenzSprache: ").append(toIndentedString(korrespondenzSprache)).append("\n");
    sb.append("    heimatort: ").append(toIndentedString(heimatort)).append("\n");
    sb.append("    heimatPLZ: ").append(toIndentedString(heimatPLZ)).append("\n");
    sb.append("    niederlassungsstatus: ").append(toIndentedString(niederlassungsstatus)).append("\n");
    sb.append("    einreisedatum: ").append(toIndentedString(einreisedatum)).append("\n");
    sb.append("    zivilstand: ").append(toIndentedString(zivilstand)).append("\n");
    sb.append("    wohnsitzAnteilMutter: ").append(toIndentedString(wohnsitzAnteilMutter)).append("\n");
    sb.append("    wohnsitzAnteilVater: ").append(toIndentedString(wohnsitzAnteilVater)).append("\n");
    sb.append("    vormundschaft: ").append(toIndentedString(vormundschaft)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzOrt: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzOrt)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzPLZ: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzPLZ)).append("\n");
    sb.append("    zustaendigeKESB: ").append(toIndentedString(zustaendigeKESB)).append("\n");
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

