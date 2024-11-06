package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
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



@JsonTypeName("GesuchFormularUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchFormularUpdateDto  implements Serializable {
  private @Valid PersonInAusbildungUpdateDto personInAusbildung;
  private @Valid FamiliensituationUpdateDto familiensituation;
  private @Valid PartnerUpdateDto partner;
  private @Valid AuszahlungUpdateDto auszahlung;
  private @Valid List<ElternUpdateDto> elterns;
  private @Valid List<GeschwisterUpdateDto> geschwisters;
  private @Valid List<LebenslaufItemUpdateDto> lebenslaufItems;
  private @Valid List<KindUpdateDto> kinds;
  private @Valid EinnahmenKostenUpdateDto einnahmenKosten;
  private @Valid List<SteuerdatenUpdateDto> steuerdaten;

  /**
   **/
  public GesuchFormularUpdateDto personInAusbildung(PersonInAusbildungUpdateDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
    return this;
  }

  
  @JsonProperty("personInAusbildung")
  public PersonInAusbildungUpdateDto getPersonInAusbildung() {
    return personInAusbildung;
  }

  @JsonProperty("personInAusbildung")
  public void setPersonInAusbildung(PersonInAusbildungUpdateDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
  }

  /**
   **/
  public GesuchFormularUpdateDto familiensituation(FamiliensituationUpdateDto familiensituation) {
    this.familiensituation = familiensituation;
    return this;
  }

  
  @JsonProperty("familiensituation")
  public FamiliensituationUpdateDto getFamiliensituation() {
    return familiensituation;
  }

  @JsonProperty("familiensituation")
  public void setFamiliensituation(FamiliensituationUpdateDto familiensituation) {
    this.familiensituation = familiensituation;
  }

  /**
   **/
  public GesuchFormularUpdateDto partner(PartnerUpdateDto partner) {
    this.partner = partner;
    return this;
  }

  
  @JsonProperty("partner")
  public PartnerUpdateDto getPartner() {
    return partner;
  }

  @JsonProperty("partner")
  public void setPartner(PartnerUpdateDto partner) {
    this.partner = partner;
  }

  /**
   **/
  public GesuchFormularUpdateDto auszahlung(AuszahlungUpdateDto auszahlung) {
    this.auszahlung = auszahlung;
    return this;
  }

  
  @JsonProperty("auszahlung")
  public AuszahlungUpdateDto getAuszahlung() {
    return auszahlung;
  }

  @JsonProperty("auszahlung")
  public void setAuszahlung(AuszahlungUpdateDto auszahlung) {
    this.auszahlung = auszahlung;
  }

  /**
   **/
  public GesuchFormularUpdateDto elterns(List<ElternUpdateDto> elterns) {
    this.elterns = elterns;
    return this;
  }

  
  @JsonProperty("elterns")
  public List<ElternUpdateDto> getElterns() {
    return elterns;
  }

  @JsonProperty("elterns")
  public void setElterns(List<ElternUpdateDto> elterns) {
    this.elterns = elterns;
  }

  public GesuchFormularUpdateDto addElternsItem(ElternUpdateDto elternsItem) {
    if (this.elterns == null) {
      this.elterns = new ArrayList<>();
    }

    this.elterns.add(elternsItem);
    return this;
  }

  public GesuchFormularUpdateDto removeElternsItem(ElternUpdateDto elternsItem) {
    if (elternsItem != null && this.elterns != null) {
      this.elterns.remove(elternsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularUpdateDto geschwisters(List<GeschwisterUpdateDto> geschwisters) {
    this.geschwisters = geschwisters;
    return this;
  }

  
  @JsonProperty("geschwisters")
  public List<GeschwisterUpdateDto> getGeschwisters() {
    return geschwisters;
  }

  @JsonProperty("geschwisters")
  public void setGeschwisters(List<GeschwisterUpdateDto> geschwisters) {
    this.geschwisters = geschwisters;
  }

  public GesuchFormularUpdateDto addGeschwistersItem(GeschwisterUpdateDto geschwistersItem) {
    if (this.geschwisters == null) {
      this.geschwisters = new ArrayList<>();
    }

    this.geschwisters.add(geschwistersItem);
    return this;
  }

  public GesuchFormularUpdateDto removeGeschwistersItem(GeschwisterUpdateDto geschwistersItem) {
    if (geschwistersItem != null && this.geschwisters != null) {
      this.geschwisters.remove(geschwistersItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularUpdateDto lebenslaufItems(List<LebenslaufItemUpdateDto> lebenslaufItems) {
    this.lebenslaufItems = lebenslaufItems;
    return this;
  }

  
  @JsonProperty("lebenslaufItems")
  public List<LebenslaufItemUpdateDto> getLebenslaufItems() {
    return lebenslaufItems;
  }

  @JsonProperty("lebenslaufItems")
  public void setLebenslaufItems(List<LebenslaufItemUpdateDto> lebenslaufItems) {
    this.lebenslaufItems = lebenslaufItems;
  }

  public GesuchFormularUpdateDto addLebenslaufItemsItem(LebenslaufItemUpdateDto lebenslaufItemsItem) {
    if (this.lebenslaufItems == null) {
      this.lebenslaufItems = new ArrayList<>();
    }

    this.lebenslaufItems.add(lebenslaufItemsItem);
    return this;
  }

  public GesuchFormularUpdateDto removeLebenslaufItemsItem(LebenslaufItemUpdateDto lebenslaufItemsItem) {
    if (lebenslaufItemsItem != null && this.lebenslaufItems != null) {
      this.lebenslaufItems.remove(lebenslaufItemsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularUpdateDto kinds(List<KindUpdateDto> kinds) {
    this.kinds = kinds;
    return this;
  }

  
  @JsonProperty("kinds")
  public List<KindUpdateDto> getKinds() {
    return kinds;
  }

  @JsonProperty("kinds")
  public void setKinds(List<KindUpdateDto> kinds) {
    this.kinds = kinds;
  }

  public GesuchFormularUpdateDto addKindsItem(KindUpdateDto kindsItem) {
    if (this.kinds == null) {
      this.kinds = new ArrayList<>();
    }

    this.kinds.add(kindsItem);
    return this;
  }

  public GesuchFormularUpdateDto removeKindsItem(KindUpdateDto kindsItem) {
    if (kindsItem != null && this.kinds != null) {
      this.kinds.remove(kindsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularUpdateDto einnahmenKosten(EinnahmenKostenUpdateDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
    return this;
  }

  
  @JsonProperty("einnahmenKosten")
  public EinnahmenKostenUpdateDto getEinnahmenKosten() {
    return einnahmenKosten;
  }

  @JsonProperty("einnahmenKosten")
  public void setEinnahmenKosten(EinnahmenKostenUpdateDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
  }

  /**
   **/
  public GesuchFormularUpdateDto steuerdaten(List<SteuerdatenUpdateDto> steuerdaten) {
    this.steuerdaten = steuerdaten;
    return this;
  }

  
  @JsonProperty("steuerdaten")
  public List<SteuerdatenUpdateDto> getSteuerdaten() {
    return steuerdaten;
  }

  @JsonProperty("steuerdaten")
  public void setSteuerdaten(List<SteuerdatenUpdateDto> steuerdaten) {
    this.steuerdaten = steuerdaten;
  }

  public GesuchFormularUpdateDto addSteuerdatenItem(SteuerdatenUpdateDto steuerdatenItem) {
    if (this.steuerdaten == null) {
      this.steuerdaten = new ArrayList<>();
    }

    this.steuerdaten.add(steuerdatenItem);
    return this;
  }

  public GesuchFormularUpdateDto removeSteuerdatenItem(SteuerdatenUpdateDto steuerdatenItem) {
    if (steuerdatenItem != null && this.steuerdaten != null) {
      this.steuerdaten.remove(steuerdatenItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchFormularUpdateDto gesuchFormularUpdate = (GesuchFormularUpdateDto) o;
    return Objects.equals(this.personInAusbildung, gesuchFormularUpdate.personInAusbildung) &&
        Objects.equals(this.familiensituation, gesuchFormularUpdate.familiensituation) &&
        Objects.equals(this.partner, gesuchFormularUpdate.partner) &&
        Objects.equals(this.auszahlung, gesuchFormularUpdate.auszahlung) &&
        Objects.equals(this.elterns, gesuchFormularUpdate.elterns) &&
        Objects.equals(this.geschwisters, gesuchFormularUpdate.geschwisters) &&
        Objects.equals(this.lebenslaufItems, gesuchFormularUpdate.lebenslaufItems) &&
        Objects.equals(this.kinds, gesuchFormularUpdate.kinds) &&
        Objects.equals(this.einnahmenKosten, gesuchFormularUpdate.einnahmenKosten) &&
        Objects.equals(this.steuerdaten, gesuchFormularUpdate.steuerdaten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personInAusbildung, familiensituation, partner, auszahlung, elterns, geschwisters, lebenslaufItems, kinds, einnahmenKosten, steuerdaten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchFormularUpdateDto {\n");
    
    sb.append("    personInAusbildung: ").append(toIndentedString(personInAusbildung)).append("\n");
    sb.append("    familiensituation: ").append(toIndentedString(familiensituation)).append("\n");
    sb.append("    partner: ").append(toIndentedString(partner)).append("\n");
    sb.append("    auszahlung: ").append(toIndentedString(auszahlung)).append("\n");
    sb.append("    elterns: ").append(toIndentedString(elterns)).append("\n");
    sb.append("    geschwisters: ").append(toIndentedString(geschwisters)).append("\n");
    sb.append("    lebenslaufItems: ").append(toIndentedString(lebenslaufItems)).append("\n");
    sb.append("    kinds: ").append(toIndentedString(kinds)).append("\n");
    sb.append("    einnahmenKosten: ").append(toIndentedString(einnahmenKosten)).append("\n");
    sb.append("    steuerdaten: ").append(toIndentedString(steuerdaten)).append("\n");
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

