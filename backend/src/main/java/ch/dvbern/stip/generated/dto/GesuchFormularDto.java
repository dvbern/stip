package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("GesuchFormular")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchFormularDto  implements Serializable {
  private @Valid AusbildungDto ausbildung;
  private @Valid PersonInAusbildungDto personInAusbildung;
  private @Valid FamiliensituationDto familiensituation;
  private @Valid PartnerDto partner;
  private @Valid AuszahlungDto auszahlung;
  private @Valid List<ElternDto> elterns;
  private @Valid List<GeschwisterDto> geschwisters;
  private @Valid List<LebenslaufItemDto> lebenslaufItems;
  private @Valid List<KindDto> kinds;
  private @Valid EinnahmenKostenDto einnahmenKosten;
  private @Valid List<SteuerdatenDto> steuerdaten;
  private @Valid List<ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp> steuerdatenTabs;
  private @Valid DarlehenDto darlehen;

  /**
   **/
  public GesuchFormularDto ausbildung(AusbildungDto ausbildung) {
    this.ausbildung = ausbildung;
    return this;
  }


  @JsonProperty("ausbildung")
  @NotNull
  public AusbildungDto getAusbildung() {
    return ausbildung;
  }

  @JsonProperty("ausbildung")
  public void setAusbildung(AusbildungDto ausbildung) {
    this.ausbildung = ausbildung;
  }

  /**
   **/
  public GesuchFormularDto personInAusbildung(PersonInAusbildungDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
    return this;
  }


  @JsonProperty("personInAusbildung")
  public PersonInAusbildungDto getPersonInAusbildung() {
    return personInAusbildung;
  }

  @JsonProperty("personInAusbildung")
  public void setPersonInAusbildung(PersonInAusbildungDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
  }

  /**
   **/
  public GesuchFormularDto familiensituation(FamiliensituationDto familiensituation) {
    this.familiensituation = familiensituation;
    return this;
  }


  @JsonProperty("familiensituation")
  public FamiliensituationDto getFamiliensituation() {
    return familiensituation;
  }

  @JsonProperty("familiensituation")
  public void setFamiliensituation(FamiliensituationDto familiensituation) {
    this.familiensituation = familiensituation;
  }

  /**
   **/
  public GesuchFormularDto partner(PartnerDto partner) {
    this.partner = partner;
    return this;
  }


  @JsonProperty("partner")
  public PartnerDto getPartner() {
    return partner;
  }

  @JsonProperty("partner")
  public void setPartner(PartnerDto partner) {
    this.partner = partner;
  }

  /**
   **/
  public GesuchFormularDto auszahlung(AuszahlungDto auszahlung) {
    this.auszahlung = auszahlung;
    return this;
  }


  @JsonProperty("auszahlung")
  public AuszahlungDto getAuszahlung() {
    return auszahlung;
  }

  @JsonProperty("auszahlung")
  public void setAuszahlung(AuszahlungDto auszahlung) {
    this.auszahlung = auszahlung;
  }

  /**
   **/
  public GesuchFormularDto elterns(List<ElternDto> elterns) {
    this.elterns = elterns;
    return this;
  }


  @JsonProperty("elterns")
  public List<ElternDto> getElterns() {
    return elterns;
  }

  @JsonProperty("elterns")
  public void setElterns(List<ElternDto> elterns) {
    this.elterns = elterns;
  }

  public GesuchFormularDto addElternsItem(ElternDto elternsItem) {
    if (this.elterns == null) {
      this.elterns = new ArrayList<>();
    }

    this.elterns.add(elternsItem);
    return this;
  }

  public GesuchFormularDto removeElternsItem(ElternDto elternsItem) {
    if (elternsItem != null && this.elterns != null) {
      this.elterns.remove(elternsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularDto geschwisters(List<GeschwisterDto> geschwisters) {
    this.geschwisters = geschwisters;
    return this;
  }


  @JsonProperty("geschwisters")
  public List<GeschwisterDto> getGeschwisters() {
    return geschwisters;
  }

  @JsonProperty("geschwisters")
  public void setGeschwisters(List<GeschwisterDto> geschwisters) {
    this.geschwisters = geschwisters;
  }

  public GesuchFormularDto addGeschwistersItem(GeschwisterDto geschwistersItem) {
    if (this.geschwisters == null) {
      this.geschwisters = new ArrayList<>();
    }

    this.geschwisters.add(geschwistersItem);
    return this;
  }

  public GesuchFormularDto removeGeschwistersItem(GeschwisterDto geschwistersItem) {
    if (geschwistersItem != null && this.geschwisters != null) {
      this.geschwisters.remove(geschwistersItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularDto lebenslaufItems(List<LebenslaufItemDto> lebenslaufItems) {
    this.lebenslaufItems = lebenslaufItems;
    return this;
  }


  @JsonProperty("lebenslaufItems")
  public List<LebenslaufItemDto> getLebenslaufItems() {
    return lebenslaufItems;
  }

  @JsonProperty("lebenslaufItems")
  public void setLebenslaufItems(List<LebenslaufItemDto> lebenslaufItems) {
    this.lebenslaufItems = lebenslaufItems;
  }

  public GesuchFormularDto addLebenslaufItemsItem(LebenslaufItemDto lebenslaufItemsItem) {
    if (this.lebenslaufItems == null) {
      this.lebenslaufItems = new ArrayList<>();
    }

    this.lebenslaufItems.add(lebenslaufItemsItem);
    return this;
  }

  public GesuchFormularDto removeLebenslaufItemsItem(LebenslaufItemDto lebenslaufItemsItem) {
    if (lebenslaufItemsItem != null && this.lebenslaufItems != null) {
      this.lebenslaufItems.remove(lebenslaufItemsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularDto kinds(List<KindDto> kinds) {
    this.kinds = kinds;
    return this;
  }


  @JsonProperty("kinds")
  public List<KindDto> getKinds() {
    return kinds;
  }

  @JsonProperty("kinds")
  public void setKinds(List<KindDto> kinds) {
    this.kinds = kinds;
  }

  public GesuchFormularDto addKindsItem(KindDto kindsItem) {
    if (this.kinds == null) {
      this.kinds = new ArrayList<>();
    }

    this.kinds.add(kindsItem);
    return this;
  }

  public GesuchFormularDto removeKindsItem(KindDto kindsItem) {
    if (kindsItem != null && this.kinds != null) {
      this.kinds.remove(kindsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularDto einnahmenKosten(EinnahmenKostenDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
    return this;
  }


  @JsonProperty("einnahmenKosten")
  public EinnahmenKostenDto getEinnahmenKosten() {
    return einnahmenKosten;
  }

  @JsonProperty("einnahmenKosten")
  public void setEinnahmenKosten(EinnahmenKostenDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
  }

  /**
   **/
  public GesuchFormularDto steuerdaten(List<SteuerdatenDto> steuerdaten) {
    this.steuerdaten = steuerdaten;
    return this;
  }


  @JsonProperty("steuerdaten")
  public List<SteuerdatenDto> getSteuerdaten() {
    return steuerdaten;
  }

  @JsonProperty("steuerdaten")
  public void setSteuerdaten(List<SteuerdatenDto> steuerdaten) {
    this.steuerdaten = steuerdaten;
  }

  public GesuchFormularDto addSteuerdatenItem(SteuerdatenDto steuerdatenItem) {
    if (this.steuerdaten == null) {
      this.steuerdaten = new ArrayList<>();
    }

    this.steuerdaten.add(steuerdatenItem);
    return this;
  }

  public GesuchFormularDto removeSteuerdatenItem(SteuerdatenDto steuerdatenItem) {
    if (steuerdatenItem != null && this.steuerdaten != null) {
      this.steuerdaten.remove(steuerdatenItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularDto steuerdatenTabs(List<ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp> steuerdatenTabs) {
    this.steuerdatenTabs = steuerdatenTabs;
    return this;
  }


  @JsonProperty("steuerdatenTabs")
  public List<ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp> getSteuerdatenTabs() {
    return steuerdatenTabs;
  }

  @JsonProperty("steuerdatenTabs")
  public void setSteuerdatenTabs(List<ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp> steuerdatenTabs) {
    this.steuerdatenTabs = steuerdatenTabs;
  }

  public GesuchFormularDto addSteuerdatenTabsItem(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTabsItem) {
    if (this.steuerdatenTabs == null) {
      this.steuerdatenTabs = new ArrayList<>();
    }

    this.steuerdatenTabs.add(steuerdatenTabsItem);
    return this;
  }

  public GesuchFormularDto removeSteuerdatenTabsItem(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTabsItem) {
    if (steuerdatenTabsItem != null && this.steuerdatenTabs != null) {
      this.steuerdatenTabs.remove(steuerdatenTabsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchFormularDto darlehen(DarlehenDto darlehen) {
    this.darlehen = darlehen;
    return this;
  }


  @JsonProperty("darlehen")
  public DarlehenDto getDarlehen() {
    return darlehen;
  }

  @JsonProperty("darlehen")
  public void setDarlehen(DarlehenDto darlehen) {
    this.darlehen = darlehen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchFormularDto gesuchFormular = (GesuchFormularDto) o;
    return Objects.equals(this.ausbildung, gesuchFormular.ausbildung) &&
        Objects.equals(this.personInAusbildung, gesuchFormular.personInAusbildung) &&
        Objects.equals(this.familiensituation, gesuchFormular.familiensituation) &&
        Objects.equals(this.partner, gesuchFormular.partner) &&
        Objects.equals(this.auszahlung, gesuchFormular.auszahlung) &&
        Objects.equals(this.elterns, gesuchFormular.elterns) &&
        Objects.equals(this.geschwisters, gesuchFormular.geschwisters) &&
        Objects.equals(this.lebenslaufItems, gesuchFormular.lebenslaufItems) &&
        Objects.equals(this.kinds, gesuchFormular.kinds) &&
        Objects.equals(this.einnahmenKosten, gesuchFormular.einnahmenKosten) &&
        Objects.equals(this.steuerdaten, gesuchFormular.steuerdaten) &&
        Objects.equals(this.steuerdatenTabs, gesuchFormular.steuerdatenTabs) &&
        Objects.equals(this.darlehen, gesuchFormular.darlehen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildung, personInAusbildung, familiensituation, partner, auszahlung, elterns, geschwisters, lebenslaufItems, kinds, einnahmenKosten, steuerdaten, steuerdatenTabs, darlehen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchFormularDto {\n");

    sb.append("    ausbildung: ").append(toIndentedString(ausbildung)).append("\n");
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
    sb.append("    steuerdatenTabs: ").append(toIndentedString(steuerdatenTabs)).append("\n");
    sb.append("    darlehen: ").append(toIndentedString(darlehen)).append("\n");
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

