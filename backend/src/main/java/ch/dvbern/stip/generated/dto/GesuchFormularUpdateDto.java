package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GeschwisterUpdateDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.SteuererklaerungUpdateDto;
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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchFormularUpdateDto  implements Serializable {
  private PersonInAusbildungUpdateDto personInAusbildung;
  private FamiliensituationUpdateDto familiensituation;
  private PartnerUpdateDto partner;
  private @Valid List<@Valid ElternUpdateDto> elterns = new ArrayList<>();
  private @Valid List<@Valid GeschwisterUpdateDto> geschwisters = new ArrayList<>();
  private @Valid List<@Valid LebenslaufItemUpdateDto> lebenslaufItems = new ArrayList<>();
  private @Valid List<@Valid KindUpdateDto> kinds = new ArrayList<>();
  private EinnahmenKostenUpdateDto einnahmenKosten;
  private EinnahmenKostenUpdateDto einnahmenKostenPartner;
  private @Valid List<@Valid SteuererklaerungUpdateDto> steuererklaerung = new ArrayList<>();

  protected GesuchFormularUpdateDto(GesuchFormularUpdateDtoBuilder<?, ?> b) {
    this.personInAusbildung = b.personInAusbildung;
    this.familiensituation = b.familiensituation;
    this.partner = b.partner;
    this.elterns = b.elterns;
    this.geschwisters = b.geschwisters;
    this.lebenslaufItems = b.lebenslaufItems;
    this.kinds = b.kinds;
    this.einnahmenKosten = b.einnahmenKosten;
    this.einnahmenKostenPartner = b.einnahmenKostenPartner;
    this.steuererklaerung = b.steuererklaerung;
  }

  public GesuchFormularUpdateDto() {
  }

  /**
   **/
  public GesuchFormularUpdateDto personInAusbildung(PersonInAusbildungUpdateDto personInAusbildung) {
    this.personInAusbildung = personInAusbildung;
    return this;
  }

  
  @JsonProperty("personInAusbildung")
  @Valid public PersonInAusbildungUpdateDto getPersonInAusbildung() {
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
  @Valid public FamiliensituationUpdateDto getFamiliensituation() {
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
  @Valid public PartnerUpdateDto getPartner() {
    return partner;
  }

  @JsonProperty("partner")
  public void setPartner(PartnerUpdateDto partner) {
    this.partner = partner;
  }

  /**
   **/
  public GesuchFormularUpdateDto elterns(List<@Valid ElternUpdateDto> elterns) {
    this.elterns = elterns;
    return this;
  }

  
  @JsonProperty("elterns")
  @Valid public List<@Valid ElternUpdateDto> getElterns() {
    return elterns;
  }

  @JsonProperty("elterns")
  public void setElterns(List<@Valid ElternUpdateDto> elterns) {
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
  public GesuchFormularUpdateDto geschwisters(List<@Valid GeschwisterUpdateDto> geschwisters) {
    this.geschwisters = geschwisters;
    return this;
  }

  
  @JsonProperty("geschwisters")
  @Valid public List<@Valid GeschwisterUpdateDto> getGeschwisters() {
    return geschwisters;
  }

  @JsonProperty("geschwisters")
  public void setGeschwisters(List<@Valid GeschwisterUpdateDto> geschwisters) {
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
  public GesuchFormularUpdateDto lebenslaufItems(List<@Valid LebenslaufItemUpdateDto> lebenslaufItems) {
    this.lebenslaufItems = lebenslaufItems;
    return this;
  }

  
  @JsonProperty("lebenslaufItems")
  @Valid public List<@Valid LebenslaufItemUpdateDto> getLebenslaufItems() {
    return lebenslaufItems;
  }

  @JsonProperty("lebenslaufItems")
  public void setLebenslaufItems(List<@Valid LebenslaufItemUpdateDto> lebenslaufItems) {
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
  public GesuchFormularUpdateDto kinds(List<@Valid KindUpdateDto> kinds) {
    this.kinds = kinds;
    return this;
  }

  
  @JsonProperty("kinds")
  @Valid public List<@Valid KindUpdateDto> getKinds() {
    return kinds;
  }

  @JsonProperty("kinds")
  public void setKinds(List<@Valid KindUpdateDto> kinds) {
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
  @Valid public EinnahmenKostenUpdateDto getEinnahmenKosten() {
    return einnahmenKosten;
  }

  @JsonProperty("einnahmenKosten")
  public void setEinnahmenKosten(EinnahmenKostenUpdateDto einnahmenKosten) {
    this.einnahmenKosten = einnahmenKosten;
  }

  /**
   **/
  public GesuchFormularUpdateDto einnahmenKostenPartner(EinnahmenKostenUpdateDto einnahmenKostenPartner) {
    this.einnahmenKostenPartner = einnahmenKostenPartner;
    return this;
  }

  
  @JsonProperty("einnahmenKostenPartner")
  @Valid public EinnahmenKostenUpdateDto getEinnahmenKostenPartner() {
    return einnahmenKostenPartner;
  }

  @JsonProperty("einnahmenKostenPartner")
  public void setEinnahmenKostenPartner(EinnahmenKostenUpdateDto einnahmenKostenPartner) {
    this.einnahmenKostenPartner = einnahmenKostenPartner;
  }

  /**
   **/
  public GesuchFormularUpdateDto steuererklaerung(List<@Valid SteuererklaerungUpdateDto> steuererklaerung) {
    this.steuererklaerung = steuererklaerung;
    return this;
  }

  
  @JsonProperty("steuererklaerung")
  @Valid public List<@Valid SteuererklaerungUpdateDto> getSteuererklaerung() {
    return steuererklaerung;
  }

  @JsonProperty("steuererklaerung")
  public void setSteuererklaerung(List<@Valid SteuererklaerungUpdateDto> steuererklaerung) {
    this.steuererklaerung = steuererklaerung;
  }

  public GesuchFormularUpdateDto addSteuererklaerungItem(SteuererklaerungUpdateDto steuererklaerungItem) {
    if (this.steuererklaerung == null) {
      this.steuererklaerung = new ArrayList<>();
    }

    this.steuererklaerung.add(steuererklaerungItem);
    return this;
  }

  public GesuchFormularUpdateDto removeSteuererklaerungItem(SteuererklaerungUpdateDto steuererklaerungItem) {
    if (steuererklaerungItem != null && this.steuererklaerung != null) {
      this.steuererklaerung.remove(steuererklaerungItem);
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
        Objects.equals(this.elterns, gesuchFormularUpdate.elterns) &&
        Objects.equals(this.geschwisters, gesuchFormularUpdate.geschwisters) &&
        Objects.equals(this.lebenslaufItems, gesuchFormularUpdate.lebenslaufItems) &&
        Objects.equals(this.kinds, gesuchFormularUpdate.kinds) &&
        Objects.equals(this.einnahmenKosten, gesuchFormularUpdate.einnahmenKosten) &&
        Objects.equals(this.einnahmenKostenPartner, gesuchFormularUpdate.einnahmenKostenPartner) &&
        Objects.equals(this.steuererklaerung, gesuchFormularUpdate.steuererklaerung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personInAusbildung, familiensituation, partner, elterns, geschwisters, lebenslaufItems, kinds, einnahmenKosten, einnahmenKostenPartner, steuererklaerung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchFormularUpdateDto {\n");
    
    sb.append("    personInAusbildung: ").append(toIndentedString(personInAusbildung)).append("\n");
    sb.append("    familiensituation: ").append(toIndentedString(familiensituation)).append("\n");
    sb.append("    partner: ").append(toIndentedString(partner)).append("\n");
    sb.append("    elterns: ").append(toIndentedString(elterns)).append("\n");
    sb.append("    geschwisters: ").append(toIndentedString(geschwisters)).append("\n");
    sb.append("    lebenslaufItems: ").append(toIndentedString(lebenslaufItems)).append("\n");
    sb.append("    kinds: ").append(toIndentedString(kinds)).append("\n");
    sb.append("    einnahmenKosten: ").append(toIndentedString(einnahmenKosten)).append("\n");
    sb.append("    einnahmenKostenPartner: ").append(toIndentedString(einnahmenKostenPartner)).append("\n");
    sb.append("    steuererklaerung: ").append(toIndentedString(steuererklaerung)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static GesuchFormularUpdateDtoBuilder<?, ?> builder() {
    return new GesuchFormularUpdateDtoBuilderImpl();
  }

  private static final class GesuchFormularUpdateDtoBuilderImpl extends GesuchFormularUpdateDtoBuilder<GesuchFormularUpdateDto, GesuchFormularUpdateDtoBuilderImpl> {

    @Override
    protected GesuchFormularUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchFormularUpdateDto build() {
      return new GesuchFormularUpdateDto(this);
    }
  }

  public static abstract class GesuchFormularUpdateDtoBuilder<C extends GesuchFormularUpdateDto, B extends GesuchFormularUpdateDtoBuilder<C, B>>  {
    private PersonInAusbildungUpdateDto personInAusbildung;
    private FamiliensituationUpdateDto familiensituation;
    private PartnerUpdateDto partner;
    private List<ElternUpdateDto> elterns = new ArrayList<>();
    private List<GeschwisterUpdateDto> geschwisters = new ArrayList<>();
    private List<LebenslaufItemUpdateDto> lebenslaufItems = new ArrayList<>();
    private List<KindUpdateDto> kinds = new ArrayList<>();
    private EinnahmenKostenUpdateDto einnahmenKosten;
    private EinnahmenKostenUpdateDto einnahmenKostenPartner;
    private List<SteuererklaerungUpdateDto> steuererklaerung = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B personInAusbildung(PersonInAusbildungUpdateDto personInAusbildung) {
      this.personInAusbildung = personInAusbildung;
      return self();
    }
    public B familiensituation(FamiliensituationUpdateDto familiensituation) {
      this.familiensituation = familiensituation;
      return self();
    }
    public B partner(PartnerUpdateDto partner) {
      this.partner = partner;
      return self();
    }
    public B elterns(List<ElternUpdateDto> elterns) {
      this.elterns = elterns;
      return self();
    }
    public B geschwisters(List<GeschwisterUpdateDto> geschwisters) {
      this.geschwisters = geschwisters;
      return self();
    }
    public B lebenslaufItems(List<LebenslaufItemUpdateDto> lebenslaufItems) {
      this.lebenslaufItems = lebenslaufItems;
      return self();
    }
    public B kinds(List<KindUpdateDto> kinds) {
      this.kinds = kinds;
      return self();
    }
    public B einnahmenKosten(EinnahmenKostenUpdateDto einnahmenKosten) {
      this.einnahmenKosten = einnahmenKosten;
      return self();
    }
    public B einnahmenKostenPartner(EinnahmenKostenUpdateDto einnahmenKostenPartner) {
      this.einnahmenKostenPartner = einnahmenKostenPartner;
      return self();
    }
    public B steuererklaerung(List<SteuererklaerungUpdateDto> steuererklaerung) {
      this.steuererklaerung = steuererklaerung;
      return self();
    }
  }
}
