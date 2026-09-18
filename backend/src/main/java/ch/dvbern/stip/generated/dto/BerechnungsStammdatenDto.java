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

/**
 * Stammdaten used for the calculation of the stipendium
 **/

@JsonTypeName("BerechnungsStammdaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BerechnungsStammdatenDto  implements Serializable {
  private Integer maxSaeule3a;
  private Integer einkommensfreibetrag;
  private Integer abzugslimite;
  private Integer freibetragErwerbseinkommen;
  private Integer freibetragVermoegen;
  private Integer vermoegensanteilInProzent;
  private Integer anzahlWochenLehre;
  private Integer anzahlWochenSchule;
  private Integer preisProMahlzeit;
  private Integer stipLimiteMinimalstipendium;
  private Integer limiteAlterAntragsstellerHalbierungElternbeitrag;
  private Integer anzahlMonate;

  protected BerechnungsStammdatenDto(BerechnungsStammdatenDtoBuilder<?, ?> b) {
    this.maxSaeule3a = b.maxSaeule3a;
    this.einkommensfreibetrag = b.einkommensfreibetrag;
    this.abzugslimite = b.abzugslimite;
    this.freibetragErwerbseinkommen = b.freibetragErwerbseinkommen;
    this.freibetragVermoegen = b.freibetragVermoegen;
    this.vermoegensanteilInProzent = b.vermoegensanteilInProzent;
    this.anzahlWochenLehre = b.anzahlWochenLehre;
    this.anzahlWochenSchule = b.anzahlWochenSchule;
    this.preisProMahlzeit = b.preisProMahlzeit;
    this.stipLimiteMinimalstipendium = b.stipLimiteMinimalstipendium;
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = b.limiteAlterAntragsstellerHalbierungElternbeitrag;
    this.anzahlMonate = b.anzahlMonate;
  }

  public BerechnungsStammdatenDto() {
  }

  /**
   **/
  public BerechnungsStammdatenDto maxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
    return this;
  }

  
  @JsonProperty(required = true, value = "maxSaeule3a")
  @NotNull public Integer getMaxSaeule3a() {
    return maxSaeule3a;
  }

  @JsonProperty(required = true, value = "maxSaeule3a")
  public void setMaxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
  }

  /**
   **/
  public BerechnungsStammdatenDto einkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty(required = true, value = "einkommensfreibetrag")
  @NotNull public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty(required = true, value = "einkommensfreibetrag")
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   **/
  public BerechnungsStammdatenDto abzugslimite(Integer abzugslimite) {
    this.abzugslimite = abzugslimite;
    return this;
  }

  
  @JsonProperty(required = true, value = "abzugslimite")
  @NotNull public Integer getAbzugslimite() {
    return abzugslimite;
  }

  @JsonProperty(required = true, value = "abzugslimite")
  public void setAbzugslimite(Integer abzugslimite) {
    this.abzugslimite = abzugslimite;
  }

  /**
   **/
  public BerechnungsStammdatenDto freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
    return this;
  }

  
  @JsonProperty(required = true, value = "freibetragErwerbseinkommen")
  @NotNull public Integer getFreibetragErwerbseinkommen() {
    return freibetragErwerbseinkommen;
  }

  @JsonProperty(required = true, value = "freibetragErwerbseinkommen")
  public void setFreibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
  }

  /**
   **/
  public BerechnungsStammdatenDto freibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "freibetragVermoegen")
  @NotNull public Integer getFreibetragVermoegen() {
    return freibetragVermoegen;
  }

  @JsonProperty(required = true, value = "freibetragVermoegen")
  public void setFreibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
  }

  /**
   **/
  public BerechnungsStammdatenDto vermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
    return this;
  }

  
  @JsonProperty(required = true, value = "vermoegensanteilInProzent")
  @NotNull public Integer getVermoegensanteilInProzent() {
    return vermoegensanteilInProzent;
  }

  @JsonProperty(required = true, value = "vermoegensanteilInProzent")
  public void setVermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
  }

  /**
   **/
  public BerechnungsStammdatenDto anzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
    return this;
  }

  
  @JsonProperty(required = true, value = "anzahlWochenLehre")
  @NotNull public Integer getAnzahlWochenLehre() {
    return anzahlWochenLehre;
  }

  @JsonProperty(required = true, value = "anzahlWochenLehre")
  public void setAnzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
  }

  /**
   **/
  public BerechnungsStammdatenDto anzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
    return this;
  }

  
  @JsonProperty(required = true, value = "anzahlWochenSchule")
  @NotNull public Integer getAnzahlWochenSchule() {
    return anzahlWochenSchule;
  }

  @JsonProperty(required = true, value = "anzahlWochenSchule")
  public void setAnzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
  }

  /**
   **/
  public BerechnungsStammdatenDto preisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
    return this;
  }

  
  @JsonProperty(required = true, value = "preisProMahlzeit")
  @NotNull public Integer getPreisProMahlzeit() {
    return preisProMahlzeit;
  }

  @JsonProperty(required = true, value = "preisProMahlzeit")
  public void setPreisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
  }

  /**
   **/
  public BerechnungsStammdatenDto stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
    return this;
  }

  
  @JsonProperty(required = true, value = "stipLimiteMinimalstipendium")
  @NotNull public Integer getStipLimiteMinimalstipendium() {
    return stipLimiteMinimalstipendium;
  }

  @JsonProperty(required = true, value = "stipLimiteMinimalstipendium")
  public void setStipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
  }

  /**
   **/
  public BerechnungsStammdatenDto limiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
    return this;
  }

  
  @JsonProperty(required = true, value = "limiteAlterAntragsstellerHalbierungElternbeitrag")
  @NotNull public Integer getLimiteAlterAntragsstellerHalbierungElternbeitrag() {
    return limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  @JsonProperty(required = true, value = "limiteAlterAntragsstellerHalbierungElternbeitrag")
  public void setLimiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  /**
   **/
  public BerechnungsStammdatenDto anzahlMonate(Integer anzahlMonate) {
    this.anzahlMonate = anzahlMonate;
    return this;
  }

  
  @JsonProperty(required = true, value = "anzahlMonate")
  @NotNull public Integer getAnzahlMonate() {
    return anzahlMonate;
  }

  @JsonProperty(required = true, value = "anzahlMonate")
  public void setAnzahlMonate(Integer anzahlMonate) {
    this.anzahlMonate = anzahlMonate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BerechnungsStammdatenDto berechnungsStammdaten = (BerechnungsStammdatenDto) o;
    return Objects.equals(this.maxSaeule3a, berechnungsStammdaten.maxSaeule3a) &&
        Objects.equals(this.einkommensfreibetrag, berechnungsStammdaten.einkommensfreibetrag) &&
        Objects.equals(this.abzugslimite, berechnungsStammdaten.abzugslimite) &&
        Objects.equals(this.freibetragErwerbseinkommen, berechnungsStammdaten.freibetragErwerbseinkommen) &&
        Objects.equals(this.freibetragVermoegen, berechnungsStammdaten.freibetragVermoegen) &&
        Objects.equals(this.vermoegensanteilInProzent, berechnungsStammdaten.vermoegensanteilInProzent) &&
        Objects.equals(this.anzahlWochenLehre, berechnungsStammdaten.anzahlWochenLehre) &&
        Objects.equals(this.anzahlWochenSchule, berechnungsStammdaten.anzahlWochenSchule) &&
        Objects.equals(this.preisProMahlzeit, berechnungsStammdaten.preisProMahlzeit) &&
        Objects.equals(this.stipLimiteMinimalstipendium, berechnungsStammdaten.stipLimiteMinimalstipendium) &&
        Objects.equals(this.limiteAlterAntragsstellerHalbierungElternbeitrag, berechnungsStammdaten.limiteAlterAntragsstellerHalbierungElternbeitrag) &&
        Objects.equals(this.anzahlMonate, berechnungsStammdaten.anzahlMonate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxSaeule3a, einkommensfreibetrag, abzugslimite, freibetragErwerbseinkommen, freibetragVermoegen, vermoegensanteilInProzent, anzahlWochenLehre, anzahlWochenSchule, preisProMahlzeit, stipLimiteMinimalstipendium, limiteAlterAntragsstellerHalbierungElternbeitrag, anzahlMonate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BerechnungsStammdatenDto {\n");
    
    sb.append("    maxSaeule3a: ").append(toIndentedString(maxSaeule3a)).append("\n");
    sb.append("    einkommensfreibetrag: ").append(toIndentedString(einkommensfreibetrag)).append("\n");
    sb.append("    abzugslimite: ").append(toIndentedString(abzugslimite)).append("\n");
    sb.append("    freibetragErwerbseinkommen: ").append(toIndentedString(freibetragErwerbseinkommen)).append("\n");
    sb.append("    freibetragVermoegen: ").append(toIndentedString(freibetragVermoegen)).append("\n");
    sb.append("    vermoegensanteilInProzent: ").append(toIndentedString(vermoegensanteilInProzent)).append("\n");
    sb.append("    anzahlWochenLehre: ").append(toIndentedString(anzahlWochenLehre)).append("\n");
    sb.append("    anzahlWochenSchule: ").append(toIndentedString(anzahlWochenSchule)).append("\n");
    sb.append("    preisProMahlzeit: ").append(toIndentedString(preisProMahlzeit)).append("\n");
    sb.append("    stipLimiteMinimalstipendium: ").append(toIndentedString(stipLimiteMinimalstipendium)).append("\n");
    sb.append("    limiteAlterAntragsstellerHalbierungElternbeitrag: ").append(toIndentedString(limiteAlterAntragsstellerHalbierungElternbeitrag)).append("\n");
    sb.append("    anzahlMonate: ").append(toIndentedString(anzahlMonate)).append("\n");
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


  public static BerechnungsStammdatenDtoBuilder<?, ?> builder() {
    return new BerechnungsStammdatenDtoBuilderImpl();
  }

  private static final class BerechnungsStammdatenDtoBuilderImpl extends BerechnungsStammdatenDtoBuilder<BerechnungsStammdatenDto, BerechnungsStammdatenDtoBuilderImpl> {

    @Override
    protected BerechnungsStammdatenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BerechnungsStammdatenDto build() {
      return new BerechnungsStammdatenDto(this);
    }
  }

  public static abstract class BerechnungsStammdatenDtoBuilder<C extends BerechnungsStammdatenDto, B extends BerechnungsStammdatenDtoBuilder<C, B>>  {
    private Integer maxSaeule3a;
    private Integer einkommensfreibetrag;
    private Integer abzugslimite;
    private Integer freibetragErwerbseinkommen;
    private Integer freibetragVermoegen;
    private Integer vermoegensanteilInProzent;
    private Integer anzahlWochenLehre;
    private Integer anzahlWochenSchule;
    private Integer preisProMahlzeit;
    private Integer stipLimiteMinimalstipendium;
    private Integer limiteAlterAntragsstellerHalbierungElternbeitrag;
    private Integer anzahlMonate;
    protected abstract B self();

    public abstract C build();

    public B maxSaeule3a(Integer maxSaeule3a) {
      this.maxSaeule3a = maxSaeule3a;
      return self();
    }
    public B einkommensfreibetrag(Integer einkommensfreibetrag) {
      this.einkommensfreibetrag = einkommensfreibetrag;
      return self();
    }
    public B abzugslimite(Integer abzugslimite) {
      this.abzugslimite = abzugslimite;
      return self();
    }
    public B freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
      this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
      return self();
    }
    public B freibetragVermoegen(Integer freibetragVermoegen) {
      this.freibetragVermoegen = freibetragVermoegen;
      return self();
    }
    public B vermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
      this.vermoegensanteilInProzent = vermoegensanteilInProzent;
      return self();
    }
    public B anzahlWochenLehre(Integer anzahlWochenLehre) {
      this.anzahlWochenLehre = anzahlWochenLehre;
      return self();
    }
    public B anzahlWochenSchule(Integer anzahlWochenSchule) {
      this.anzahlWochenSchule = anzahlWochenSchule;
      return self();
    }
    public B preisProMahlzeit(Integer preisProMahlzeit) {
      this.preisProMahlzeit = preisProMahlzeit;
      return self();
    }
    public B stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
      this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
      return self();
    }
    public B limiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
      this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
      return self();
    }
    public B anzahlMonate(Integer anzahlMonate) {
      this.anzahlMonate = anzahlMonate;
      return self();
    }
  }
}
