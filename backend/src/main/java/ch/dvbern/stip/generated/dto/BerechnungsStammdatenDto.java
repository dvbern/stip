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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BerechnungsStammdatenDto  implements Serializable {
  private @Valid Integer maxSaeule3a;
  private @Valid Integer einkommensfreibetrag;
  private @Valid Integer abzugslimite;
  private @Valid Integer freibetragErwerbseinkommen;
  private @Valid Integer freibetragVermoegen;
  private @Valid Integer vermoegensanteilInProzent;
  private @Valid Integer anzahlWochenLehre;
  private @Valid Integer anzahlWochenSchule;
  private @Valid Integer preisProMahlzeit;
  private @Valid Integer stipLimiteMinimalstipendium;
  private @Valid Integer limiteAlterAntragsstellerHalbierungElternbeitrag;
  private @Valid Integer anzahlMonate;

  /**
   **/
  public BerechnungsStammdatenDto maxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
    return this;
  }

  
  @JsonProperty("maxSaeule3a")
  @NotNull
  public Integer getMaxSaeule3a() {
    return maxSaeule3a;
  }

  @JsonProperty("maxSaeule3a")
  public void setMaxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
  }

  /**
   **/
  public BerechnungsStammdatenDto einkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty("einkommensfreibetrag")
  @NotNull
  public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty("einkommensfreibetrag")
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   **/
  public BerechnungsStammdatenDto abzugslimite(Integer abzugslimite) {
    this.abzugslimite = abzugslimite;
    return this;
  }

  
  @JsonProperty("abzugslimite")
  @NotNull
  public Integer getAbzugslimite() {
    return abzugslimite;
  }

  @JsonProperty("abzugslimite")
  public void setAbzugslimite(Integer abzugslimite) {
    this.abzugslimite = abzugslimite;
  }

  /**
   **/
  public BerechnungsStammdatenDto freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
    return this;
  }

  
  @JsonProperty("freibetragErwerbseinkommen")
  @NotNull
  public Integer getFreibetragErwerbseinkommen() {
    return freibetragErwerbseinkommen;
  }

  @JsonProperty("freibetragErwerbseinkommen")
  public void setFreibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
  }

  /**
   **/
  public BerechnungsStammdatenDto freibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
    return this;
  }

  
  @JsonProperty("freibetragVermoegen")
  @NotNull
  public Integer getFreibetragVermoegen() {
    return freibetragVermoegen;
  }

  @JsonProperty("freibetragVermoegen")
  public void setFreibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
  }

  /**
   **/
  public BerechnungsStammdatenDto vermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
    return this;
  }

  
  @JsonProperty("vermoegensanteilInProzent")
  @NotNull
  public Integer getVermoegensanteilInProzent() {
    return vermoegensanteilInProzent;
  }

  @JsonProperty("vermoegensanteilInProzent")
  public void setVermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
  }

  /**
   **/
  public BerechnungsStammdatenDto anzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
    return this;
  }

  
  @JsonProperty("anzahlWochenLehre")
  @NotNull
  public Integer getAnzahlWochenLehre() {
    return anzahlWochenLehre;
  }

  @JsonProperty("anzahlWochenLehre")
  public void setAnzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
  }

  /**
   **/
  public BerechnungsStammdatenDto anzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
    return this;
  }

  
  @JsonProperty("anzahlWochenSchule")
  @NotNull
  public Integer getAnzahlWochenSchule() {
    return anzahlWochenSchule;
  }

  @JsonProperty("anzahlWochenSchule")
  public void setAnzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
  }

  /**
   **/
  public BerechnungsStammdatenDto preisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
    return this;
  }

  
  @JsonProperty("preisProMahlzeit")
  @NotNull
  public Integer getPreisProMahlzeit() {
    return preisProMahlzeit;
  }

  @JsonProperty("preisProMahlzeit")
  public void setPreisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
  }

  /**
   **/
  public BerechnungsStammdatenDto stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
    return this;
  }

  
  @JsonProperty("stipLimiteMinimalstipendium")
  @NotNull
  public Integer getStipLimiteMinimalstipendium() {
    return stipLimiteMinimalstipendium;
  }

  @JsonProperty("stipLimiteMinimalstipendium")
  public void setStipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
  }

  /**
   **/
  public BerechnungsStammdatenDto limiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
    return this;
  }

  
  @JsonProperty("limiteAlterAntragsstellerHalbierungElternbeitrag")
  @NotNull
  public Integer getLimiteAlterAntragsstellerHalbierungElternbeitrag() {
    return limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  @JsonProperty("limiteAlterAntragsstellerHalbierungElternbeitrag")
  public void setLimiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  /**
   **/
  public BerechnungsStammdatenDto anzahlMonate(Integer anzahlMonate) {
    this.anzahlMonate = anzahlMonate;
    return this;
  }

  
  @JsonProperty("anzahlMonate")
  @NotNull
  public Integer getAnzahlMonate() {
    return anzahlMonate;
  }

  @JsonProperty("anzahlMonate")
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
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

