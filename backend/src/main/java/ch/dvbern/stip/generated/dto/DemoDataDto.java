package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoAuszahlungDto;
import ch.dvbern.stip.generated.dto.DemoDarlehenDto;
import ch.dvbern.stip.generated.dto.DemoDataStipendienanspruchDto;
import ch.dvbern.stip.generated.dto.DemoEinnahmenKostenDto;
import ch.dvbern.stip.generated.dto.DemoElternteilDto;
import ch.dvbern.stip.generated.dto.DemoFamiliensituationDto;
import ch.dvbern.stip.generated.dto.DemoGeschwisterDto;
import ch.dvbern.stip.generated.dto.DemoKindDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufDto;
import ch.dvbern.stip.generated.dto.DemoPartnerDto;
import ch.dvbern.stip.generated.dto.DemoPersonInAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoSteuerdatenDto;
import ch.dvbern.stip.generated.dto.DemoSteuererklaerungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoData")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataDto  implements Serializable {
  private @Valid DemoAusbildungDto ausbildung;
  private @Valid DemoPersonInAusbildungDto personInAusbildung;
  private @Valid DemoLebenslaufDto lebenslauf;
  private @Valid List<DemoKindDto> kinder = new ArrayList<>();
  private @Valid DemoEinnahmenKostenDto einnahmenKosten;
  private @Valid DemoFamiliensituationDto familiensituation;
  private @Valid List<DemoElternteilDto> elterns = new ArrayList<>();
  private @Valid List<DemoSteuererklaerungDto> steuererklaerung = new ArrayList<>();
  private @Valid List<DemoSteuerdatenDto> steuerdaten = new ArrayList<>();
  private @Valid List<DemoGeschwisterDto> geschwister = new ArrayList<>();
  private @Valid DemoAuszahlungDto auszahlung;
  private @Valid DemoDarlehenDto darlehen;
  private @Valid DemoPartnerDto partner;
  private @Valid DemoEinnahmenKostenDto einnahmenKostenPartner;
  private @Valid DemoDataStipendienanspruchDto stipendienanspruch;

  /**
   **/
  public DemoDataDto ausbildung(DemoAusbildungDto ausbildung) {
    this.ausbildung = ausbildung;
    return this;
  }

  
  @JsonProperty("ausbildung")
  @NotNull
  public DemoAusbildungDto getAusbildung() {
    return ausbildung;
  }

  @JsonProperty("ausbildung")
  public void setAusbildung(DemoAusbildungDto ausbildung) {
    this.ausbildung = ausbildung;
  }

  /**
   **/
  public DemoDataDto personInAusbildung(DemoPersonInAusbildungDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
    return this;
  }

  
  @JsonProperty("personInAusbildung")
  @NotNull
  public DemoPersonInAusbildungDto getPersonInAusbildung() {
    return personInAusbildung;
  }

  @JsonProperty("personInAusbildung")
  public void setPersonInAusbildung(DemoPersonInAusbildungDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
  }

  /**
   **/
  public DemoDataDto lebenslauf(DemoLebenslaufDto lebenslauf) {
    this.lebenslauf = lebenslauf;
    return this;
  }

  
  @JsonProperty("lebenslauf")
  @NotNull
  public DemoLebenslaufDto getLebenslauf() {
    return lebenslauf;
  }

  @JsonProperty("lebenslauf")
  public void setLebenslauf(DemoLebenslaufDto lebenslauf) {
    this.lebenslauf = lebenslauf;
  }

  /**
   **/
  public DemoDataDto kinder(List<DemoKindDto> kinder) {
    this.kinder = kinder;
    return this;
  }

  
  @JsonProperty("kinder")
  @NotNull
  public List<DemoKindDto> getKinder() {
    return kinder;
  }

  @JsonProperty("kinder")
  public void setKinder(List<DemoKindDto> kinder) {
    this.kinder = kinder;
  }

  public DemoDataDto addKinderItem(DemoKindDto kinderItem) {
    if (this.kinder == null) {
      this.kinder = new ArrayList<>();
    }

    this.kinder.add(kinderItem);
    return this;
  }

  public DemoDataDto removeKinderItem(DemoKindDto kinderItem) {
    if (kinderItem != null && this.kinder != null) {
      this.kinder.remove(kinderItem);
    }

    return this;
  }
  /**
   **/
  public DemoDataDto einnahmenKosten(DemoEinnahmenKostenDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
    return this;
  }

  
  @JsonProperty("einnahmenKosten")
  @NotNull
  public DemoEinnahmenKostenDto getEinnahmenKosten() {
    return einnahmenKosten;
  }

  @JsonProperty("einnahmenKosten")
  public void setEinnahmenKosten(DemoEinnahmenKostenDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
  }

  /**
   **/
  public DemoDataDto familiensituation(DemoFamiliensituationDto familiensituation) {
    this.familiensituation = familiensituation;
    return this;
  }

  
  @JsonProperty("familiensituation")
  @NotNull
  public DemoFamiliensituationDto getFamiliensituation() {
    return familiensituation;
  }

  @JsonProperty("familiensituation")
  public void setFamiliensituation(DemoFamiliensituationDto familiensituation) {
    this.familiensituation = familiensituation;
  }

  /**
   **/
  public DemoDataDto elterns(List<DemoElternteilDto> elterns) {
    this.elterns = elterns;
    return this;
  }

  
  @JsonProperty("elterns")
  @NotNull
  public List<DemoElternteilDto> getElterns() {
    return elterns;
  }

  @JsonProperty("elterns")
  public void setElterns(List<DemoElternteilDto> elterns) {
    this.elterns = elterns;
  }

  public DemoDataDto addElternsItem(DemoElternteilDto elternsItem) {
    if (this.elterns == null) {
      this.elterns = new ArrayList<>();
    }

    this.elterns.add(elternsItem);
    return this;
  }

  public DemoDataDto removeElternsItem(DemoElternteilDto elternsItem) {
    if (elternsItem != null && this.elterns != null) {
      this.elterns.remove(elternsItem);
    }

    return this;
  }
  /**
   **/
  public DemoDataDto steuererklaerung(List<DemoSteuererklaerungDto> steuererklaerung) {
    this.steuererklaerung = steuererklaerung;
    return this;
  }

  
  @JsonProperty("steuererklaerung")
  @NotNull
  public List<DemoSteuererklaerungDto> getSteuererklaerung() {
    return steuererklaerung;
  }

  @JsonProperty("steuererklaerung")
  public void setSteuererklaerung(List<DemoSteuererklaerungDto> steuererklaerung) {
    this.steuererklaerung = steuererklaerung;
  }

  public DemoDataDto addSteuererklaerungItem(DemoSteuererklaerungDto steuererklaerungItem) {
    if (this.steuererklaerung == null) {
      this.steuererklaerung = new ArrayList<>();
    }

    this.steuererklaerung.add(steuererklaerungItem);
    return this;
  }

  public DemoDataDto removeSteuererklaerungItem(DemoSteuererklaerungDto steuererklaerungItem) {
    if (steuererklaerungItem != null && this.steuererklaerung != null) {
      this.steuererklaerung.remove(steuererklaerungItem);
    }

    return this;
  }
  /**
   **/
  public DemoDataDto steuerdaten(List<DemoSteuerdatenDto> steuerdaten) {
    this.steuerdaten = steuerdaten;
    return this;
  }

  
  @JsonProperty("steuerdaten")
  @NotNull
  public List<DemoSteuerdatenDto> getSteuerdaten() {
    return steuerdaten;
  }

  @JsonProperty("steuerdaten")
  public void setSteuerdaten(List<DemoSteuerdatenDto> steuerdaten) {
    this.steuerdaten = steuerdaten;
  }

  public DemoDataDto addSteuerdatenItem(DemoSteuerdatenDto steuerdatenItem) {
    if (this.steuerdaten == null) {
      this.steuerdaten = new ArrayList<>();
    }

    this.steuerdaten.add(steuerdatenItem);
    return this;
  }

  public DemoDataDto removeSteuerdatenItem(DemoSteuerdatenDto steuerdatenItem) {
    if (steuerdatenItem != null && this.steuerdaten != null) {
      this.steuerdaten.remove(steuerdatenItem);
    }

    return this;
  }
  /**
   **/
  public DemoDataDto geschwister(List<DemoGeschwisterDto> geschwister) {
    this.geschwister = geschwister;
    return this;
  }

  
  @JsonProperty("geschwister")
  @NotNull
  public List<DemoGeschwisterDto> getGeschwister() {
    return geschwister;
  }

  @JsonProperty("geschwister")
  public void setGeschwister(List<DemoGeschwisterDto> geschwister) {
    this.geschwister = geschwister;
  }

  public DemoDataDto addGeschwisterItem(DemoGeschwisterDto geschwisterItem) {
    if (this.geschwister == null) {
      this.geschwister = new ArrayList<>();
    }

    this.geschwister.add(geschwisterItem);
    return this;
  }

  public DemoDataDto removeGeschwisterItem(DemoGeschwisterDto geschwisterItem) {
    if (geschwisterItem != null && this.geschwister != null) {
      this.geschwister.remove(geschwisterItem);
    }

    return this;
  }
  /**
   **/
  public DemoDataDto auszahlung(DemoAuszahlungDto auszahlung) {
    this.auszahlung = auszahlung;
    return this;
  }

  
  @JsonProperty("auszahlung")
  @NotNull
  public DemoAuszahlungDto getAuszahlung() {
    return auszahlung;
  }

  @JsonProperty("auszahlung")
  public void setAuszahlung(DemoAuszahlungDto auszahlung) {
    this.auszahlung = auszahlung;
  }

  /**
   **/
  public DemoDataDto darlehen(DemoDarlehenDto darlehen) {
    this.darlehen = darlehen;
    return this;
  }

  
  @JsonProperty("darlehen")
  @NotNull
  public DemoDarlehenDto getDarlehen() {
    return darlehen;
  }

  @JsonProperty("darlehen")
  public void setDarlehen(DemoDarlehenDto darlehen) {
    this.darlehen = darlehen;
  }

  /**
   **/
  public DemoDataDto partner(DemoPartnerDto partner) {
    this.partner = partner;
    return this;
  }

  
  @JsonProperty("partner")
  public DemoPartnerDto getPartner() {
    return partner;
  }

  @JsonProperty("partner")
  public void setPartner(DemoPartnerDto partner) {
    this.partner = partner;
  }

  /**
   **/
  public DemoDataDto einnahmenKostenPartner(DemoEinnahmenKostenDto einnahmenKostenPartner) {
    this.einnahmenKostenPartner = einnahmenKostenPartner;
    return this;
  }

  
  @JsonProperty("einnahmenKostenPartner")
  public DemoEinnahmenKostenDto getEinnahmenKostenPartner() {
    return einnahmenKostenPartner;
  }

  @JsonProperty("einnahmenKostenPartner")
  public void setEinnahmenKostenPartner(DemoEinnahmenKostenDto einnahmenKostenPartner) {
    this.einnahmenKostenPartner = einnahmenKostenPartner;
  }

  /**
   **/
  public DemoDataDto stipendienanspruch(DemoDataStipendienanspruchDto stipendienanspruch) {
    this.stipendienanspruch = stipendienanspruch;
    return this;
  }

  
  @JsonProperty("stipendienanspruch")
  public DemoDataStipendienanspruchDto getStipendienanspruch() {
    return stipendienanspruch;
  }

  @JsonProperty("stipendienanspruch")
  public void setStipendienanspruch(DemoDataStipendienanspruchDto stipendienanspruch) {
    this.stipendienanspruch = stipendienanspruch;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataDto demoData = (DemoDataDto) o;
    return Objects.equals(this.ausbildung, demoData.ausbildung) &&
        Objects.equals(this.personInAusbildung, demoData.personInAusbildung) &&
        Objects.equals(this.lebenslauf, demoData.lebenslauf) &&
        Objects.equals(this.kinder, demoData.kinder) &&
        Objects.equals(this.einnahmenKosten, demoData.einnahmenKosten) &&
        Objects.equals(this.familiensituation, demoData.familiensituation) &&
        Objects.equals(this.elterns, demoData.elterns) &&
        Objects.equals(this.steuererklaerung, demoData.steuererklaerung) &&
        Objects.equals(this.steuerdaten, demoData.steuerdaten) &&
        Objects.equals(this.geschwister, demoData.geschwister) &&
        Objects.equals(this.auszahlung, demoData.auszahlung) &&
        Objects.equals(this.darlehen, demoData.darlehen) &&
        Objects.equals(this.partner, demoData.partner) &&
        Objects.equals(this.einnahmenKostenPartner, demoData.einnahmenKostenPartner) &&
        Objects.equals(this.stipendienanspruch, demoData.stipendienanspruch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildung, personInAusbildung, lebenslauf, kinder, einnahmenKosten, familiensituation, elterns, steuererklaerung, steuerdaten, geschwister, auszahlung, darlehen, partner, einnahmenKostenPartner, stipendienanspruch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataDto {\n");
    
    sb.append("    ausbildung: ").append(toIndentedString(ausbildung)).append("\n");
    sb.append("    personInAusbildung: ").append(toIndentedString(personInAusbildung)).append("\n");
    sb.append("    lebenslauf: ").append(toIndentedString(lebenslauf)).append("\n");
    sb.append("    kinder: ").append(toIndentedString(kinder)).append("\n");
    sb.append("    einnahmenKosten: ").append(toIndentedString(einnahmenKosten)).append("\n");
    sb.append("    familiensituation: ").append(toIndentedString(familiensituation)).append("\n");
    sb.append("    elterns: ").append(toIndentedString(elterns)).append("\n");
    sb.append("    steuererklaerung: ").append(toIndentedString(steuererklaerung)).append("\n");
    sb.append("    steuerdaten: ").append(toIndentedString(steuerdaten)).append("\n");
    sb.append("    geschwister: ").append(toIndentedString(geschwister)).append("\n");
    sb.append("    auszahlung: ").append(toIndentedString(auszahlung)).append("\n");
    sb.append("    darlehen: ").append(toIndentedString(darlehen)).append("\n");
    sb.append("    partner: ").append(toIndentedString(partner)).append("\n");
    sb.append("    einnahmenKostenPartner: ").append(toIndentedString(einnahmenKostenPartner)).append("\n");
    sb.append("    stipendienanspruch: ").append(toIndentedString(stipendienanspruch)).append("\n");
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

