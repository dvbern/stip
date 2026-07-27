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



@JsonTypeName("EinnahmenKostenUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class EinnahmenKostenUpdateDto  implements Serializable {
  private @Valid Integer nettoerwerbseinkommen;
  private @Valid Integer fahrkosten;
  private @Valid Integer arbeitspensumProzent;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer taggelderAHVIV;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer zulagen;
  private @Valid Integer renten;
  private @Valid Integer eoLeistungen;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer beitraege;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer wohnkosten;
  private @Valid Boolean wgWohnend;
  private @Valid Integer wgAnzahlPersonen;
  private @Valid Boolean alternativeWohnformWohnend;
  private @Valid Integer auswaertigeMittagessenProWoche;
  private @Valid Integer verpflegungskosten;
  private @Valid Integer betreuungskostenKinder;
  private @Valid String veranlagungsStatus;
  private @Valid Integer steuerjahr;
  private @Valid Integer vermoegen;
  private @Valid Integer steuern;

  protected EinnahmenKostenUpdateDto(EinnahmenKostenUpdateDtoBuilder<?, ?> b) {
    this.nettoerwerbseinkommen = b.nettoerwerbseinkommen;
    this.fahrkosten = b.fahrkosten;
    this.arbeitspensumProzent = b.arbeitspensumProzent;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.einnahmenBGSA = b.einnahmenBGSA;
    this.taggelderAHVIV = b.taggelderAHVIV;
    this.andereEinnahmen = b.andereEinnahmen;
    this.zulagen = b.zulagen;
    this.renten = b.renten;
    this.eoLeistungen = b.eoLeistungen;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.beitraege = b.beitraege;
    this.ausbildungskosten = b.ausbildungskosten;
    this.wohnkosten = b.wohnkosten;
    this.wgWohnend = b.wgWohnend;
    this.wgAnzahlPersonen = b.wgAnzahlPersonen;
    this.alternativeWohnformWohnend = b.alternativeWohnformWohnend;
    this.auswaertigeMittagessenProWoche = b.auswaertigeMittagessenProWoche;
    this.verpflegungskosten = b.verpflegungskosten;
    this.betreuungskostenKinder = b.betreuungskostenKinder;
    this.veranlagungsStatus = b.veranlagungsStatus;
    this.steuerjahr = b.steuerjahr;
    this.vermoegen = b.vermoegen;
    this.steuern = b.steuern;
  }

  public EinnahmenKostenUpdateDto() {
  }

  /**
   **/
  public EinnahmenKostenUpdateDto nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommen")
  @NotNull
  public Integer getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }

  @JsonProperty("nettoerwerbseinkommen")
  public void setNettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   * Required nur wenn nettoerwerbseinkommen &gt; 0
   **/
  public EinnahmenKostenUpdateDto arbeitspensumProzent(Integer arbeitspensumProzent) {
    this.arbeitspensumProzent = arbeitspensumProzent;
    return this;
  }

  
  @JsonProperty("arbeitspensumProzent")
  public Integer getArbeitspensumProzent() {
    return arbeitspensumProzent;
  }

  @JsonProperty("arbeitspensumProzent")
  public void setArbeitspensumProzent(Integer arbeitspensumProzent) {
    this.arbeitspensumProzent = arbeitspensumProzent;
  }

  /**
   * Wird immer angezeigt, ist aber optional
   **/
  public EinnahmenKostenUpdateDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   * Mit Dokument wenn &gt; 0
   **/
  public EinnahmenKostenUpdateDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   * Mit Dokument wenn &gt; 0
   **/
  public EinnahmenKostenUpdateDto taggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty("taggelderAHVIV")
  public Integer getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty("taggelderAHVIV")
  public void setTaggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
  }

  /**
   * Mit Dokument wenn &gt; 0
   **/
  public EinnahmenKostenUpdateDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   * Required nur wenn mind. ein Kind gibt
   **/
  public EinnahmenKostenUpdateDto zulagen(Integer zulagen) {
    this.zulagen = zulagen;
    return this;
  }

  
  @JsonProperty("zulagen")
  public Integer getZulagen() {
    return zulagen;
  }

  @JsonProperty("zulagen")
  public void setZulagen(Integer zulagen) {
    this.zulagen = zulagen;
  }

  /**
   * Required nur wenn mind. ein Elternteil gestorben ist
   **/
  public EinnahmenKostenUpdateDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto eoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  public Integer getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto beitraege(Integer beitraege) {
    this.beitraege = beitraege;
    return this;
  }

  
  @JsonProperty("beitraege")
  public Integer getBeitraege() {
    return beitraege;
  }

  @JsonProperty("beitraege")
  public void setBeitraege(Integer beitraege) {
    this.beitraege = beitraege;
  }

  /**
   * The cost for the education calculated over the whole year
   **/
  public EinnahmenKostenUpdateDto ausbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
    return this;
  }

  
  @JsonProperty("ausbildungskosten")
  public Integer getAusbildungskosten() {
    return ausbildungskosten;
  }

  @JsonProperty("ausbildungskosten")
  public void setAusbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   * Required nur wenn Person eigener Haushalt hat
   **/
  public EinnahmenKostenUpdateDto wgWohnend(Boolean wgWohnend) {
    this.wgWohnend = wgWohnend;
    return this;
  }

  
  @JsonProperty("wgWohnend")
  public Boolean getWgWohnend() {
    return wgWohnend;
  }

  @JsonProperty("wgWohnend")
  public void setWgWohnend(Boolean wgWohnend) {
    this.wgWohnend = wgWohnend;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto wgAnzahlPersonen(Integer wgAnzahlPersonen) {
    this.wgAnzahlPersonen = wgAnzahlPersonen;
    return this;
  }

  
  @JsonProperty("wgAnzahlPersonen")
  public Integer getWgAnzahlPersonen() {
    return wgAnzahlPersonen;
  }

  @JsonProperty("wgAnzahlPersonen")
  public void setWgAnzahlPersonen(Integer wgAnzahlPersonen) {
    this.wgAnzahlPersonen = wgAnzahlPersonen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto alternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
    this.alternativeWohnformWohnend = alternativeWohnformWohnend;
    return this;
  }

  
  @JsonProperty("alternativeWohnformWohnend")
  public Boolean getAlternativeWohnformWohnend() {
    return alternativeWohnformWohnend;
  }

  @JsonProperty("alternativeWohnformWohnend")
  public void setAlternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
    this.alternativeWohnformWohnend = alternativeWohnformWohnend;
  }

  /**
   * Required nur wenn die Person keinen eigenen Haushalt führt
   **/
  public EinnahmenKostenUpdateDto auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
    this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
    return this;
  }

  
  @JsonProperty("auswaertigeMittagessenProWoche")
  public Integer getAuswaertigeMittagessenProWoche() {
    return auswaertigeMittagessenProWoche;
  }

  @JsonProperty("auswaertigeMittagessenProWoche")
  public void setAuswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
    this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto verpflegungskosten(Integer verpflegungskosten) {
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

  /**
   * Betreuungskosten eigener Kinder
   **/
  public EinnahmenKostenUpdateDto betreuungskostenKinder(Integer betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
    return this;
  }

  
  @JsonProperty("betreuungskostenKinder")
  public Integer getBetreuungskostenKinder() {
    return betreuungskostenKinder;
  }

  @JsonProperty("betreuungskostenKinder")
  public void setBetreuungskostenKinder(Integer betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
  }

  /**
   * Veranlagungsstatus
   **/
  public EinnahmenKostenUpdateDto veranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
    return this;
  }

  
  @JsonProperty("veranlagungsStatus")
  public String getVeranlagungsStatus() {
    return veranlagungsStatus;
  }

  @JsonProperty("veranlagungsStatus")
  public void setVeranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
  }

  /**
   * Aktuelles oder ein vergangenes Steuerjahr als 4-stellige Zahl. Default ist Vorjahr des Gesuchsjahrs
   * minimum: 0
   * maximum: 99999
   **/
  public EinnahmenKostenUpdateDto steuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
    return this;
  }

  
  @JsonProperty("steuerjahr")
 @Min(0) @Max(99999)  public Integer getSteuerjahr() {
    return steuerjahr;
  }

  @JsonProperty("steuerjahr")
  public void setSteuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
  }

  /**
   * Ganze Zahl, ohne Kommastellen
   * minimum: 0
   * maximum: 2147483647
   **/
  public EinnahmenKostenUpdateDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty("vermoegen")
 @Min(0) @Max(2147483647)  public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty("vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   * only visible by SB
   * minimum: 0
   * maximum: 2147483647
   **/
  public EinnahmenKostenUpdateDto steuern(Integer steuern) {
    this.steuern = steuern;
    return this;
  }

  
  @JsonProperty("steuern")
 @Min(0) @Max(2147483647)  public Integer getSteuern() {
    return steuern;
  }

  @JsonProperty("steuern")
  public void setSteuern(Integer steuern) {
    this.steuern = steuern;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EinnahmenKostenUpdateDto einnahmenKostenUpdate = (EinnahmenKostenUpdateDto) o;
    return Objects.equals(this.nettoerwerbseinkommen, einnahmenKostenUpdate.nettoerwerbseinkommen) &&
        Objects.equals(this.fahrkosten, einnahmenKostenUpdate.fahrkosten) &&
        Objects.equals(this.arbeitspensumProzent, einnahmenKostenUpdate.arbeitspensumProzent) &&
        Objects.equals(this.unterhaltsbeitraege, einnahmenKostenUpdate.unterhaltsbeitraege) &&
        Objects.equals(this.einnahmenBGSA, einnahmenKostenUpdate.einnahmenBGSA) &&
        Objects.equals(this.taggelderAHVIV, einnahmenKostenUpdate.taggelderAHVIV) &&
        Objects.equals(this.andereEinnahmen, einnahmenKostenUpdate.andereEinnahmen) &&
        Objects.equals(this.zulagen, einnahmenKostenUpdate.zulagen) &&
        Objects.equals(this.renten, einnahmenKostenUpdate.renten) &&
        Objects.equals(this.eoLeistungen, einnahmenKostenUpdate.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, einnahmenKostenUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, einnahmenKostenUpdate.beitraege) &&
        Objects.equals(this.ausbildungskosten, einnahmenKostenUpdate.ausbildungskosten) &&
        Objects.equals(this.wohnkosten, einnahmenKostenUpdate.wohnkosten) &&
        Objects.equals(this.wgWohnend, einnahmenKostenUpdate.wgWohnend) &&
        Objects.equals(this.wgAnzahlPersonen, einnahmenKostenUpdate.wgAnzahlPersonen) &&
        Objects.equals(this.alternativeWohnformWohnend, einnahmenKostenUpdate.alternativeWohnformWohnend) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, einnahmenKostenUpdate.auswaertigeMittagessenProWoche) &&
        Objects.equals(this.verpflegungskosten, einnahmenKostenUpdate.verpflegungskosten) &&
        Objects.equals(this.betreuungskostenKinder, einnahmenKostenUpdate.betreuungskostenKinder) &&
        Objects.equals(this.veranlagungsStatus, einnahmenKostenUpdate.veranlagungsStatus) &&
        Objects.equals(this.steuerjahr, einnahmenKostenUpdate.steuerjahr) &&
        Objects.equals(this.vermoegen, einnahmenKostenUpdate.vermoegen) &&
        Objects.equals(this.steuern, einnahmenKostenUpdate.steuern);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nettoerwerbseinkommen, fahrkosten, arbeitspensumProzent, unterhaltsbeitraege, einnahmenBGSA, taggelderAHVIV, andereEinnahmen, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, ausbildungskosten, wohnkosten, wgWohnend, wgAnzahlPersonen, alternativeWohnformWohnend, auswaertigeMittagessenProWoche, verpflegungskosten, betreuungskostenKinder, veranlagungsStatus, steuerjahr, vermoegen, steuern);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinnahmenKostenUpdateDto {\n");
    
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    arbeitspensumProzent: ").append(toIndentedString(arbeitspensumProzent)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    taggelderAHVIV: ").append(toIndentedString(taggelderAHVIV)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    zulagen: ").append(toIndentedString(zulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    beitraege: ").append(toIndentedString(beitraege)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    wgWohnend: ").append(toIndentedString(wgWohnend)).append("\n");
    sb.append("    wgAnzahlPersonen: ").append(toIndentedString(wgAnzahlPersonen)).append("\n");
    sb.append("    alternativeWohnformWohnend: ").append(toIndentedString(alternativeWohnformWohnend)).append("\n");
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
    sb.append("    betreuungskostenKinder: ").append(toIndentedString(betreuungskostenKinder)).append("\n");
    sb.append("    veranlagungsStatus: ").append(toIndentedString(veranlagungsStatus)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    steuern: ").append(toIndentedString(steuern)).append("\n");
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


  public static EinnahmenKostenUpdateDtoBuilder<?, ?> builder() {
    return new EinnahmenKostenUpdateDtoBuilderImpl();
  }

  private static final class EinnahmenKostenUpdateDtoBuilderImpl extends EinnahmenKostenUpdateDtoBuilder<EinnahmenKostenUpdateDto, EinnahmenKostenUpdateDtoBuilderImpl> {

    @Override
    protected EinnahmenKostenUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public EinnahmenKostenUpdateDto build() {
      return new EinnahmenKostenUpdateDto(this);
    }
  }

  public static abstract class EinnahmenKostenUpdateDtoBuilder<C extends EinnahmenKostenUpdateDto, B extends EinnahmenKostenUpdateDtoBuilder<C, B>>  {
    private Integer nettoerwerbseinkommen;
    private Integer fahrkosten;
    private Integer arbeitspensumProzent;
    private Integer unterhaltsbeitraege;
    private Integer einnahmenBGSA;
    private Integer taggelderAHVIV;
    private Integer andereEinnahmen;
    private Integer zulagen;
    private Integer renten;
    private Integer eoLeistungen;
    private Integer ergaenzungsleistungen;
    private Integer beitraege;
    private Integer ausbildungskosten;
    private Integer wohnkosten;
    private Boolean wgWohnend;
    private Integer wgAnzahlPersonen;
    private Boolean alternativeWohnformWohnend;
    private Integer auswaertigeMittagessenProWoche;
    private Integer verpflegungskosten;
    private Integer betreuungskostenKinder;
    private String veranlagungsStatus;
    private Integer steuerjahr;
    private Integer vermoegen;
    private Integer steuern;
    protected abstract B self();

    public abstract C build();

    public B nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
      this.nettoerwerbseinkommen = nettoerwerbseinkommen;
      return self();
    }
    public B fahrkosten(Integer fahrkosten) {
      this.fahrkosten = fahrkosten;
      return self();
    }
    public B arbeitspensumProzent(Integer arbeitspensumProzent) {
      this.arbeitspensumProzent = arbeitspensumProzent;
      return self();
    }
    public B unterhaltsbeitraege(Integer unterhaltsbeitraege) {
      this.unterhaltsbeitraege = unterhaltsbeitraege;
      return self();
    }
    public B einnahmenBGSA(Integer einnahmenBGSA) {
      this.einnahmenBGSA = einnahmenBGSA;
      return self();
    }
    public B taggelderAHVIV(Integer taggelderAHVIV) {
      this.taggelderAHVIV = taggelderAHVIV;
      return self();
    }
    public B andereEinnahmen(Integer andereEinnahmen) {
      this.andereEinnahmen = andereEinnahmen;
      return self();
    }
    public B zulagen(Integer zulagen) {
      this.zulagen = zulagen;
      return self();
    }
    public B renten(Integer renten) {
      this.renten = renten;
      return self();
    }
    public B eoLeistungen(Integer eoLeistungen) {
      this.eoLeistungen = eoLeistungen;
      return self();
    }
    public B ergaenzungsleistungen(Integer ergaenzungsleistungen) {
      this.ergaenzungsleistungen = ergaenzungsleistungen;
      return self();
    }
    public B beitraege(Integer beitraege) {
      this.beitraege = beitraege;
      return self();
    }
    public B ausbildungskosten(Integer ausbildungskosten) {
      this.ausbildungskosten = ausbildungskosten;
      return self();
    }
    public B wohnkosten(Integer wohnkosten) {
      this.wohnkosten = wohnkosten;
      return self();
    }
    public B wgWohnend(Boolean wgWohnend) {
      this.wgWohnend = wgWohnend;
      return self();
    }
    public B wgAnzahlPersonen(Integer wgAnzahlPersonen) {
      this.wgAnzahlPersonen = wgAnzahlPersonen;
      return self();
    }
    public B alternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
      this.alternativeWohnformWohnend = alternativeWohnformWohnend;
      return self();
    }
    public B auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
      this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
      return self();
    }
    public B verpflegungskosten(Integer verpflegungskosten) {
      this.verpflegungskosten = verpflegungskosten;
      return self();
    }
    public B betreuungskostenKinder(Integer betreuungskostenKinder) {
      this.betreuungskostenKinder = betreuungskostenKinder;
      return self();
    }
    public B veranlagungsStatus(String veranlagungsStatus) {
      this.veranlagungsStatus = veranlagungsStatus;
      return self();
    }
    public B steuerjahr(Integer steuerjahr) {
      this.steuerjahr = steuerjahr;
      return self();
    }
    public B vermoegen(Integer vermoegen) {
      this.vermoegen = vermoegen;
      return self();
    }
    public B steuern(Integer steuern) {
      this.steuern = steuern;
      return self();
    }
  }
}

