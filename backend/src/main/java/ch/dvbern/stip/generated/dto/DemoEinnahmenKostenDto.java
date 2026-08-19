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



@JsonTypeName("DemoEinnahmenKosten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoEinnahmenKostenDto  implements Serializable {
  private @Valid Integer nettoerwerbseinkommen;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer zulagen;
  private @Valid Integer renten;
  private @Valid Integer eoLeistungen;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer beitraege;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer taggelderAHVIV;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer fahrkosten;
  private @Valid Integer vermoegen;
  private @Valid Integer steuernKantonGemeinde;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer verpflegungskosten;
  private @Valid Integer auswaertigeMittagessenProWoche;
  private @Valid Integer wohnkosten;
  private @Valid Boolean wgWohnend;
  private @Valid Boolean alternativeWohnformWohnend;

  protected DemoEinnahmenKostenDto(DemoEinnahmenKostenDtoBuilder<?, ?> b) {
    this.nettoerwerbseinkommen = b.nettoerwerbseinkommen;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.zulagen = b.zulagen;
    this.renten = b.renten;
    this.eoLeistungen = b.eoLeistungen;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.beitraege = b.beitraege;
    this.einnahmenBGSA = b.einnahmenBGSA;
    this.taggelderAHVIV = b.taggelderAHVIV;
    this.andereEinnahmen = b.andereEinnahmen;
    this.fahrkosten = b.fahrkosten;
    this.vermoegen = b.vermoegen;
    this.steuernKantonGemeinde = b.steuernKantonGemeinde;
    this.ausbildungskosten = b.ausbildungskosten;
    this.verpflegungskosten = b.verpflegungskosten;
    this.auswaertigeMittagessenProWoche = b.auswaertigeMittagessenProWoche;
    this.wohnkosten = b.wohnkosten;
    this.wgWohnend = b.wgWohnend;
    this.alternativeWohnformWohnend = b.alternativeWohnformWohnend;
  }

  public DemoEinnahmenKostenDto() {
  }

  /**
   **/
  public DemoEinnahmenKostenDto nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
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
  public DemoEinnahmenKostenDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  @NotNull
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public DemoEinnahmenKostenDto zulagen(Integer zulagen) {
    this.zulagen = zulagen;
    return this;
  }

  
  @JsonProperty("zulagen")
  @NotNull
  public Integer getZulagen() {
    return zulagen;
  }

  @JsonProperty("zulagen")
  public void setZulagen(Integer zulagen) {
    this.zulagen = zulagen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  @NotNull
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public DemoEinnahmenKostenDto eoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  @NotNull
  public Integer getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto beitraege(Integer beitraege) {
    this.beitraege = beitraege;
    return this;
  }

  
  @JsonProperty("beitraege")
  @NotNull
  public Integer getBeitraege() {
    return beitraege;
  }

  @JsonProperty("beitraege")
  public void setBeitraege(Integer beitraege) {
    this.beitraege = beitraege;
  }

  /**
   **/
  public DemoEinnahmenKostenDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  @NotNull
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public DemoEinnahmenKostenDto taggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty("taggelderAHVIV")
  @NotNull
  public Integer getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty("taggelderAHVIV")
  public void setTaggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
  }

  /**
   **/
  public DemoEinnahmenKostenDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  @NotNull
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto fahrkosten(Integer fahrkosten) {
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
   **/
  public DemoEinnahmenKostenDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty("vermoegen")
  @NotNull
  public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty("vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeinde")
  @NotNull
  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty("steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }

  /**
   **/
  public DemoEinnahmenKostenDto ausbildungskosten(Integer ausbildungskosten) {
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
  public DemoEinnahmenKostenDto verpflegungskosten(Integer verpflegungskosten) {
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
   **/
  public DemoEinnahmenKostenDto auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
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
  public DemoEinnahmenKostenDto wohnkosten(Integer wohnkosten) {
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
   **/
  public DemoEinnahmenKostenDto wgWohnend(Boolean wgWohnend) {
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
  public DemoEinnahmenKostenDto alternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoEinnahmenKostenDto demoEinnahmenKosten = (DemoEinnahmenKostenDto) o;
    return Objects.equals(this.nettoerwerbseinkommen, demoEinnahmenKosten.nettoerwerbseinkommen) &&
        Objects.equals(this.unterhaltsbeitraege, demoEinnahmenKosten.unterhaltsbeitraege) &&
        Objects.equals(this.zulagen, demoEinnahmenKosten.zulagen) &&
        Objects.equals(this.renten, demoEinnahmenKosten.renten) &&
        Objects.equals(this.eoLeistungen, demoEinnahmenKosten.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, demoEinnahmenKosten.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, demoEinnahmenKosten.beitraege) &&
        Objects.equals(this.einnahmenBGSA, demoEinnahmenKosten.einnahmenBGSA) &&
        Objects.equals(this.taggelderAHVIV, demoEinnahmenKosten.taggelderAHVIV) &&
        Objects.equals(this.andereEinnahmen, demoEinnahmenKosten.andereEinnahmen) &&
        Objects.equals(this.fahrkosten, demoEinnahmenKosten.fahrkosten) &&
        Objects.equals(this.vermoegen, demoEinnahmenKosten.vermoegen) &&
        Objects.equals(this.steuernKantonGemeinde, demoEinnahmenKosten.steuernKantonGemeinde) &&
        Objects.equals(this.ausbildungskosten, demoEinnahmenKosten.ausbildungskosten) &&
        Objects.equals(this.verpflegungskosten, demoEinnahmenKosten.verpflegungskosten) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, demoEinnahmenKosten.auswaertigeMittagessenProWoche) &&
        Objects.equals(this.wohnkosten, demoEinnahmenKosten.wohnkosten) &&
        Objects.equals(this.wgWohnend, demoEinnahmenKosten.wgWohnend) &&
        Objects.equals(this.alternativeWohnformWohnend, demoEinnahmenKosten.alternativeWohnformWohnend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nettoerwerbseinkommen, unterhaltsbeitraege, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, einnahmenBGSA, taggelderAHVIV, andereEinnahmen, fahrkosten, vermoegen, steuernKantonGemeinde, ausbildungskosten, verpflegungskosten, auswaertigeMittagessenProWoche, wohnkosten, wgWohnend, alternativeWohnformWohnend);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoEinnahmenKostenDto {\n");
    
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    zulagen: ").append(toIndentedString(zulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    beitraege: ").append(toIndentedString(beitraege)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    taggelderAHVIV: ").append(toIndentedString(taggelderAHVIV)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    wgWohnend: ").append(toIndentedString(wgWohnend)).append("\n");
    sb.append("    alternativeWohnformWohnend: ").append(toIndentedString(alternativeWohnformWohnend)).append("\n");
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


  public static DemoEinnahmenKostenDtoBuilder<?, ?> builder() {
    return new DemoEinnahmenKostenDtoBuilderImpl();
  }

  private static final class DemoEinnahmenKostenDtoBuilderImpl extends DemoEinnahmenKostenDtoBuilder<DemoEinnahmenKostenDto, DemoEinnahmenKostenDtoBuilderImpl> {

    @Override
    protected DemoEinnahmenKostenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoEinnahmenKostenDto build() {
      return new DemoEinnahmenKostenDto(this);
    }
  }

  public static abstract class DemoEinnahmenKostenDtoBuilder<C extends DemoEinnahmenKostenDto, B extends DemoEinnahmenKostenDtoBuilder<C, B>>  {
    private Integer nettoerwerbseinkommen;
    private Integer unterhaltsbeitraege;
    private Integer zulagen;
    private Integer renten;
    private Integer eoLeistungen;
    private Integer ergaenzungsleistungen;
    private Integer beitraege;
    private Integer einnahmenBGSA;
    private Integer taggelderAHVIV;
    private Integer andereEinnahmen;
    private Integer fahrkosten;
    private Integer vermoegen;
    private Integer steuernKantonGemeinde;
    private Integer ausbildungskosten;
    private Integer verpflegungskosten;
    private Integer auswaertigeMittagessenProWoche;
    private Integer wohnkosten;
    private Boolean wgWohnend;
    private Boolean alternativeWohnformWohnend;
    protected abstract B self();

    public abstract C build();

    public B nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
      this.nettoerwerbseinkommen = nettoerwerbseinkommen;
      return self();
    }
    public B unterhaltsbeitraege(Integer unterhaltsbeitraege) {
      this.unterhaltsbeitraege = unterhaltsbeitraege;
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
    public B fahrkosten(Integer fahrkosten) {
      this.fahrkosten = fahrkosten;
      return self();
    }
    public B vermoegen(Integer vermoegen) {
      this.vermoegen = vermoegen;
      return self();
    }
    public B steuernKantonGemeinde(Integer steuernKantonGemeinde) {
      this.steuernKantonGemeinde = steuernKantonGemeinde;
      return self();
    }
    public B ausbildungskosten(Integer ausbildungskosten) {
      this.ausbildungskosten = ausbildungskosten;
      return self();
    }
    public B verpflegungskosten(Integer verpflegungskosten) {
      this.verpflegungskosten = verpflegungskosten;
      return self();
    }
    public B auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
      this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
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
    public B alternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
      this.alternativeWohnformWohnend = alternativeWohnformWohnend;
      return self();
    }
  }
}

