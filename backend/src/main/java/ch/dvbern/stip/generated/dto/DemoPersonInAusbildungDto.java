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



@JsonTypeName("DemoPersonInAusbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoPersonInAusbildungDto  implements Serializable {
  private @Valid String sozialversicherungsnummer;
  private @Valid ch.dvbern.stip.api.common.type.Anrede anrede;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String strasse;
  private @Valid String hausnummer;
  private @Valid String plz;
  private @Valid String ort;
  private @Valid String land;
  private @Valid Boolean identischerZivilrechtlicherWohnsitz;
  private @Valid String email;
  private @Valid String telefonnummer;
  private @Valid String geburtsdatum;
  private @Valid Integer alter;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand;
  private @Valid String nationalitaet;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus;
  private @Valid LocalDate einreisedatum;
  private @Valid String heimatort;
  private @Valid String coAdresse;
  private @Valid String identischerZivilrechtlicherWohnsitzPLZ;
  private @Valid String identischerZivilrechtlicherWohnsitzOrt;
  private @Valid String heimatortPLZ;
  private @Valid ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz;
  private @Valid Integer wohnsitzAnteilVater;
  private @Valid Integer wohnsitzAnteilMutter;
  private @Valid Boolean vormundschaft;
  private @Valid ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB;
  private @Valid Boolean sozialhilfebeitraege;

  protected DemoPersonInAusbildungDto(DemoPersonInAusbildungDtoBuilder<?, ?> b) {
    this.sozialversicherungsnummer = b.sozialversicherungsnummer;
    this.anrede = b.anrede;
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.strasse = b.strasse;
    this.hausnummer = b.hausnummer;
    this.plz = b.plz;
    this.ort = b.ort;
    this.land = b.land;
    this.identischerZivilrechtlicherWohnsitz = b.identischerZivilrechtlicherWohnsitz;
    this.email = b.email;
    this.telefonnummer = b.telefonnummer;
    this.geburtsdatum = b.geburtsdatum;
    this.alter = b.alter;
    this.zivilstand = b.zivilstand;
    this.nationalitaet = b.nationalitaet;
    this.niederlassungsstatus = b.niederlassungsstatus;
    this.einreisedatum = b.einreisedatum;
    this.heimatort = b.heimatort;
    this.coAdresse = b.coAdresse;
    this.identischerZivilrechtlicherWohnsitzPLZ = b.identischerZivilrechtlicherWohnsitzPLZ;
    this.identischerZivilrechtlicherWohnsitzOrt = b.identischerZivilrechtlicherWohnsitzOrt;
    this.heimatortPLZ = b.heimatortPLZ;
    this.wohnsitz = b.wohnsitz;
    this.wohnsitzAnteilVater = b.wohnsitzAnteilVater;
    this.wohnsitzAnteilMutter = b.wohnsitzAnteilMutter;
    this.vormundschaft = b.vormundschaft;
    this.zustaendigeKESB = b.zustaendigeKESB;
    this.sozialhilfebeitraege = b.sozialhilfebeitraege;
  }

  public DemoPersonInAusbildungDto() {
  }

  /**
   **/
  public DemoPersonInAusbildungDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public DemoPersonInAusbildungDto anrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
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
  public DemoPersonInAusbildungDto nachname(String nachname) {
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
  public DemoPersonInAusbildungDto vorname(String vorname) {
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
  public DemoPersonInAusbildungDto strasse(String strasse) {
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
  public DemoPersonInAusbildungDto hausnummer(String hausnummer) {
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
  public DemoPersonInAusbildungDto plz(String plz) {
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
  public DemoPersonInAusbildungDto ort(String ort) {
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
  public DemoPersonInAusbildungDto land(String land) {
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
  public DemoPersonInAusbildungDto identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
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
  public DemoPersonInAusbildungDto email(String email) {
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
  public DemoPersonInAusbildungDto telefonnummer(String telefonnummer) {
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
  public DemoPersonInAusbildungDto geburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
 @Pattern(regexp="^\\d{2}.\\d{2}$")  public String getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DemoPersonInAusbildungDto alter(Integer alter) {
    this.alter = alter;
    return this;
  }

  
  @JsonProperty("alter")
  @NotNull
  public Integer getAlter() {
    return alter;
  }

  @JsonProperty("alter")
  public void setAlter(Integer alter) {
    this.alter = alter;
  }

  /**
   **/
  public DemoPersonInAusbildungDto zivilstand(ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand) {
    this.zivilstand = zivilstand;
    return this;
  }

  
  @JsonProperty("zivilstand")
  @NotNull
  public ch.dvbern.stip.api.personinausbildung.type.Zivilstand getZivilstand() {
    return zivilstand;
  }

  @JsonProperty("zivilstand")
  public void setZivilstand(ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand) {
    this.zivilstand = zivilstand;
  }

  /**
   **/
  public DemoPersonInAusbildungDto nationalitaet(String nationalitaet) {
    this.nationalitaet = nationalitaet;
    return this;
  }

  
  @JsonProperty("nationalitaet")
  @NotNull
  public String getNationalitaet() {
    return nationalitaet;
  }

  @JsonProperty("nationalitaet")
  public void setNationalitaet(String nationalitaet) {
    this.nationalitaet = nationalitaet;
  }

  /**
   **/
  public DemoPersonInAusbildungDto niederlassungsstatus(ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus) {
    this.niederlassungsstatus = niederlassungsstatus;
    return this;
  }

  
  @JsonProperty("niederlassungsstatus")
  @NotNull
  public ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus getNiederlassungsstatus() {
    return niederlassungsstatus;
  }

  @JsonProperty("niederlassungsstatus")
  public void setNiederlassungsstatus(ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus) {
    this.niederlassungsstatus = niederlassungsstatus;
  }

  /**
   **/
  public DemoPersonInAusbildungDto einreisedatum(LocalDate einreisedatum) {
    this.einreisedatum = einreisedatum;
    return this;
  }

  
  @JsonProperty("einreisedatum")
  @NotNull
  public LocalDate getEinreisedatum() {
    return einreisedatum;
  }

  @JsonProperty("einreisedatum")
  public void setEinreisedatum(LocalDate einreisedatum) {
    this.einreisedatum = einreisedatum;
  }

  /**
   **/
  public DemoPersonInAusbildungDto heimatort(String heimatort) {
    this.heimatort = heimatort;
    return this;
  }

  
  @JsonProperty("heimatort")
  @NotNull
  public String getHeimatort() {
    return heimatort;
  }

  @JsonProperty("heimatort")
  public void setHeimatort(String heimatort) {
    this.heimatort = heimatort;
  }

  /**
   **/
  public DemoPersonInAusbildungDto coAdresse(String coAdresse) {
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
  public DemoPersonInAusbildungDto identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
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
  public DemoPersonInAusbildungDto identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
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
  public DemoPersonInAusbildungDto heimatortPLZ(String heimatortPLZ) {
    this.heimatortPLZ = heimatortPLZ;
    return this;
  }

  
  @JsonProperty("heimatortPLZ")
  public String getHeimatortPLZ() {
    return heimatortPLZ;
  }

  @JsonProperty("heimatortPLZ")
  public void setHeimatortPLZ(String heimatortPLZ) {
    this.heimatortPLZ = heimatortPLZ;
  }

  /**
   **/
  public DemoPersonInAusbildungDto wohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
    this.wohnsitz = wohnsitz;
    return this;
  }

  
  @JsonProperty("wohnsitz")
  public ch.dvbern.stip.api.common.type.Wohnsitz getWohnsitz() {
    return wohnsitz;
  }

  @JsonProperty("wohnsitz")
  public void setWohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
    this.wohnsitz = wohnsitz;
  }

  /**
   **/
  public DemoPersonInAusbildungDto wohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilVater")
  public Integer getWohnsitzAnteilVater() {
    return wohnsitzAnteilVater;
  }

  @JsonProperty("wohnsitzAnteilVater")
  public void setWohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
  }

  /**
   **/
  public DemoPersonInAusbildungDto wohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilMutter")
  public Integer getWohnsitzAnteilMutter() {
    return wohnsitzAnteilMutter;
  }

  @JsonProperty("wohnsitzAnteilMutter")
  public void setWohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
  }

  /**
   **/
  public DemoPersonInAusbildungDto vormundschaft(Boolean vormundschaft) {
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
   **/
  public DemoPersonInAusbildungDto zustaendigeKESB(ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB) {
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

  /**
   **/
  public DemoPersonInAusbildungDto sozialhilfebeitraege(Boolean sozialhilfebeitraege) {
    this.sozialhilfebeitraege = sozialhilfebeitraege;
    return this;
  }

  
  @JsonProperty("sozialhilfebeitraege")
  public Boolean getSozialhilfebeitraege() {
    return sozialhilfebeitraege;
  }

  @JsonProperty("sozialhilfebeitraege")
  public void setSozialhilfebeitraege(Boolean sozialhilfebeitraege) {
    this.sozialhilfebeitraege = sozialhilfebeitraege;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoPersonInAusbildungDto demoPersonInAusbildung = (DemoPersonInAusbildungDto) o;
    return Objects.equals(this.sozialversicherungsnummer, demoPersonInAusbildung.sozialversicherungsnummer) &&
        Objects.equals(this.anrede, demoPersonInAusbildung.anrede) &&
        Objects.equals(this.nachname, demoPersonInAusbildung.nachname) &&
        Objects.equals(this.vorname, demoPersonInAusbildung.vorname) &&
        Objects.equals(this.strasse, demoPersonInAusbildung.strasse) &&
        Objects.equals(this.hausnummer, demoPersonInAusbildung.hausnummer) &&
        Objects.equals(this.plz, demoPersonInAusbildung.plz) &&
        Objects.equals(this.ort, demoPersonInAusbildung.ort) &&
        Objects.equals(this.land, demoPersonInAusbildung.land) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitz, demoPersonInAusbildung.identischerZivilrechtlicherWohnsitz) &&
        Objects.equals(this.email, demoPersonInAusbildung.email) &&
        Objects.equals(this.telefonnummer, demoPersonInAusbildung.telefonnummer) &&
        Objects.equals(this.geburtsdatum, demoPersonInAusbildung.geburtsdatum) &&
        Objects.equals(this.alter, demoPersonInAusbildung.alter) &&
        Objects.equals(this.zivilstand, demoPersonInAusbildung.zivilstand) &&
        Objects.equals(this.nationalitaet, demoPersonInAusbildung.nationalitaet) &&
        Objects.equals(this.niederlassungsstatus, demoPersonInAusbildung.niederlassungsstatus) &&
        Objects.equals(this.einreisedatum, demoPersonInAusbildung.einreisedatum) &&
        Objects.equals(this.heimatort, demoPersonInAusbildung.heimatort) &&
        Objects.equals(this.coAdresse, demoPersonInAusbildung.coAdresse) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzPLZ, demoPersonInAusbildung.identischerZivilrechtlicherWohnsitzPLZ) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzOrt, demoPersonInAusbildung.identischerZivilrechtlicherWohnsitzOrt) &&
        Objects.equals(this.heimatortPLZ, demoPersonInAusbildung.heimatortPLZ) &&
        Objects.equals(this.wohnsitz, demoPersonInAusbildung.wohnsitz) &&
        Objects.equals(this.wohnsitzAnteilVater, demoPersonInAusbildung.wohnsitzAnteilVater) &&
        Objects.equals(this.wohnsitzAnteilMutter, demoPersonInAusbildung.wohnsitzAnteilMutter) &&
        Objects.equals(this.vormundschaft, demoPersonInAusbildung.vormundschaft) &&
        Objects.equals(this.zustaendigeKESB, demoPersonInAusbildung.zustaendigeKESB) &&
        Objects.equals(this.sozialhilfebeitraege, demoPersonInAusbildung.sozialhilfebeitraege);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sozialversicherungsnummer, anrede, nachname, vorname, strasse, hausnummer, plz, ort, land, identischerZivilrechtlicherWohnsitz, email, telefonnummer, geburtsdatum, alter, zivilstand, nationalitaet, niederlassungsstatus, einreisedatum, heimatort, coAdresse, identischerZivilrechtlicherWohnsitzPLZ, identischerZivilrechtlicherWohnsitzOrt, heimatortPLZ, wohnsitz, wohnsitzAnteilVater, wohnsitzAnteilMutter, vormundschaft, zustaendigeKESB, sozialhilfebeitraege);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoPersonInAusbildungDto {\n");
    
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    anrede: ").append(toIndentedString(anrede)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitz: ").append(toIndentedString(identischerZivilrechtlicherWohnsitz)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    alter: ").append(toIndentedString(alter)).append("\n");
    sb.append("    zivilstand: ").append(toIndentedString(zivilstand)).append("\n");
    sb.append("    nationalitaet: ").append(toIndentedString(nationalitaet)).append("\n");
    sb.append("    niederlassungsstatus: ").append(toIndentedString(niederlassungsstatus)).append("\n");
    sb.append("    einreisedatum: ").append(toIndentedString(einreisedatum)).append("\n");
    sb.append("    heimatort: ").append(toIndentedString(heimatort)).append("\n");
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzPLZ: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzPLZ)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzOrt: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzOrt)).append("\n");
    sb.append("    heimatortPLZ: ").append(toIndentedString(heimatortPLZ)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
    sb.append("    wohnsitzAnteilVater: ").append(toIndentedString(wohnsitzAnteilVater)).append("\n");
    sb.append("    wohnsitzAnteilMutter: ").append(toIndentedString(wohnsitzAnteilMutter)).append("\n");
    sb.append("    vormundschaft: ").append(toIndentedString(vormundschaft)).append("\n");
    sb.append("    zustaendigeKESB: ").append(toIndentedString(zustaendigeKESB)).append("\n");
    sb.append("    sozialhilfebeitraege: ").append(toIndentedString(sozialhilfebeitraege)).append("\n");
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


  public static DemoPersonInAusbildungDtoBuilder<?, ?> builder() {
    return new DemoPersonInAusbildungDtoBuilderImpl();
  }

  private static final class DemoPersonInAusbildungDtoBuilderImpl extends DemoPersonInAusbildungDtoBuilder<DemoPersonInAusbildungDto, DemoPersonInAusbildungDtoBuilderImpl> {

    @Override
    protected DemoPersonInAusbildungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoPersonInAusbildungDto build() {
      return new DemoPersonInAusbildungDto(this);
    }
  }

  public static abstract class DemoPersonInAusbildungDtoBuilder<C extends DemoPersonInAusbildungDto, B extends DemoPersonInAusbildungDtoBuilder<C, B>>  {
    private String sozialversicherungsnummer;
    private ch.dvbern.stip.api.common.type.Anrede anrede;
    private String nachname;
    private String vorname;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;
    private String land;
    private Boolean identischerZivilrechtlicherWohnsitz;
    private String email;
    private String telefonnummer;
    private String geburtsdatum;
    private Integer alter;
    private ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand;
    private String nationalitaet;
    private ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus;
    private LocalDate einreisedatum;
    private String heimatort;
    private String coAdresse;
    private String identischerZivilrechtlicherWohnsitzPLZ;
    private String identischerZivilrechtlicherWohnsitzOrt;
    private String heimatortPLZ;
    private ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz;
    private Integer wohnsitzAnteilVater;
    private Integer wohnsitzAnteilMutter;
    private Boolean vormundschaft;
    private ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB;
    private Boolean sozialhilfebeitraege;
    protected abstract B self();

    public abstract C build();

    public B sozialversicherungsnummer(String sozialversicherungsnummer) {
      this.sozialversicherungsnummer = sozialversicherungsnummer;
      return self();
    }
    public B anrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
      this.anrede = anrede;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B strasse(String strasse) {
      this.strasse = strasse;
      return self();
    }
    public B hausnummer(String hausnummer) {
      this.hausnummer = hausnummer;
      return self();
    }
    public B plz(String plz) {
      this.plz = plz;
      return self();
    }
    public B ort(String ort) {
      this.ort = ort;
      return self();
    }
    public B land(String land) {
      this.land = land;
      return self();
    }
    public B identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
      this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
      return self();
    }
    public B email(String email) {
      this.email = email;
      return self();
    }
    public B telefonnummer(String telefonnummer) {
      this.telefonnummer = telefonnummer;
      return self();
    }
    public B geburtsdatum(String geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B alter(Integer alter) {
      this.alter = alter;
      return self();
    }
    public B zivilstand(ch.dvbern.stip.api.personinausbildung.type.Zivilstand zivilstand) {
      this.zivilstand = zivilstand;
      return self();
    }
    public B nationalitaet(String nationalitaet) {
      this.nationalitaet = nationalitaet;
      return self();
    }
    public B niederlassungsstatus(ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus niederlassungsstatus) {
      this.niederlassungsstatus = niederlassungsstatus;
      return self();
    }
    public B einreisedatum(LocalDate einreisedatum) {
      this.einreisedatum = einreisedatum;
      return self();
    }
    public B heimatort(String heimatort) {
      this.heimatort = heimatort;
      return self();
    }
    public B coAdresse(String coAdresse) {
      this.coAdresse = coAdresse;
      return self();
    }
    public B identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
      this.identischerZivilrechtlicherWohnsitzPLZ = identischerZivilrechtlicherWohnsitzPLZ;
      return self();
    }
    public B identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
      this.identischerZivilrechtlicherWohnsitzOrt = identischerZivilrechtlicherWohnsitzOrt;
      return self();
    }
    public B heimatortPLZ(String heimatortPLZ) {
      this.heimatortPLZ = heimatortPLZ;
      return self();
    }
    public B wohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
      this.wohnsitz = wohnsitz;
      return self();
    }
    public B wohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
      this.wohnsitzAnteilVater = wohnsitzAnteilVater;
      return self();
    }
    public B wohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
      this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
      return self();
    }
    public B vormundschaft(Boolean vormundschaft) {
      this.vormundschaft = vormundschaft;
      return self();
    }
    public B zustaendigeKESB(ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB zustaendigeKESB) {
      this.zustaendigeKESB = zustaendigeKESB;
      return self();
    }
    public B sozialhilfebeitraege(Boolean sozialhilfebeitraege) {
      this.sozialhilfebeitraege = sozialhilfebeitraege;
      return self();
    }
  }
}

