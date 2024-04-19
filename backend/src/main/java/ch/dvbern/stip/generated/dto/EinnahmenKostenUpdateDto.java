package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("EinnahmenKostenUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class EinnahmenKostenUpdateDto  implements Serializable {
  private @Valid BigDecimal nettoerwerbseinkommen;
  private @Valid BigDecimal fahrkosten;
  private @Valid Boolean verdienstRealisiert;
  private @Valid BigDecimal alimente;
  private @Valid BigDecimal zulagen;
  private @Valid BigDecimal renten;
  private @Valid BigDecimal eoLeistungen;
  private @Valid BigDecimal ergaenzungsleistungen;
  private @Valid BigDecimal beitraege;
  private @Valid BigDecimal ausbildungskostenSekundarstufeZwei;
  private @Valid BigDecimal ausbildungskostenTertiaerstufe;
  private @Valid BigDecimal wohnkosten;
  private @Valid Boolean wgWohnend;
  private @Valid Boolean willDarlehen;
  private @Valid Integer auswaertigeMittagessenProWoche;
  private @Valid BigDecimal betreuungskostenKinder;

  /**
   **/
  public EinnahmenKostenUpdateDto nettoerwerbseinkommen(BigDecimal nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommen")
  @NotNull
  public BigDecimal getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }

  @JsonProperty("nettoerwerbseinkommen")
  public void setNettoerwerbseinkommen(BigDecimal nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto fahrkosten(BigDecimal fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public BigDecimal getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(BigDecimal fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto verdienstRealisiert(Boolean verdienstRealisiert) {
    this.verdienstRealisiert = verdienstRealisiert;
    return this;
  }

  
  @JsonProperty("verdienstRealisiert")
  @NotNull
  public Boolean getVerdienstRealisiert() {
    return verdienstRealisiert;
  }

  @JsonProperty("verdienstRealisiert")
  public void setVerdienstRealisiert(Boolean verdienstRealisiert) {
    this.verdienstRealisiert = verdienstRealisiert;
  }

  /**
   * Required nur wenn mind. ein Elternteil Alimente zahlt
   **/
  public EinnahmenKostenUpdateDto alimente(BigDecimal alimente) {
    this.alimente = alimente;
    return this;
  }

  
  @JsonProperty("alimente")
  public BigDecimal getAlimente() {
    return alimente;
  }

  @JsonProperty("alimente")
  public void setAlimente(BigDecimal alimente) {
    this.alimente = alimente;
  }

  /**
   * Required nur wenn mind. ein Kind gibt
   **/
  public EinnahmenKostenUpdateDto zulagen(BigDecimal zulagen) {
    this.zulagen = zulagen;
    return this;
  }

  
  @JsonProperty("zulagen")
  public BigDecimal getZulagen() {
    return zulagen;
  }

  @JsonProperty("zulagen")
  public void setZulagen(BigDecimal zulagen) {
    this.zulagen = zulagen;
  }

  /**
   * Required nur wenn mind. ein Elternteil gestorben ist
   **/
  public EinnahmenKostenUpdateDto renten(BigDecimal renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  public BigDecimal getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(BigDecimal renten) {
    this.renten = renten;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto eoLeistungen(BigDecimal eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  public BigDecimal getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(BigDecimal eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto ergaenzungsleistungen(BigDecimal ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  public BigDecimal getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(BigDecimal ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto beitraege(BigDecimal beitraege) {
    this.beitraege = beitraege;
    return this;
  }

  
  @JsonProperty("beitraege")
  public BigDecimal getBeitraege() {
    return beitraege;
  }

  @JsonProperty("beitraege")
  public void setBeitraege(BigDecimal beitraege) {
    this.beitraege = beitraege;
  }

  /**
   * Required nur wenn die ausgewählte Ausbildung auf der Sekundarstuffe II ist
   **/
  public EinnahmenKostenUpdateDto ausbildungskostenSekundarstufeZwei(BigDecimal ausbildungskostenSekundarstufeZwei) {
    this.ausbildungskostenSekundarstufeZwei = ausbildungskostenSekundarstufeZwei;
    return this;
  }

  
  @JsonProperty("ausbildungskostenSekundarstufeZwei")
  public BigDecimal getAusbildungskostenSekundarstufeZwei() {
    return ausbildungskostenSekundarstufeZwei;
  }

  @JsonProperty("ausbildungskostenSekundarstufeZwei")
  public void setAusbildungskostenSekundarstufeZwei(BigDecimal ausbildungskostenSekundarstufeZwei) {
    this.ausbildungskostenSekundarstufeZwei = ausbildungskostenSekundarstufeZwei;
  }

  /**
   * Required nur wenn die ausgewählte Ausbildung auf der Tertiärstufe ist
   **/
  public EinnahmenKostenUpdateDto ausbildungskostenTertiaerstufe(BigDecimal ausbildungskostenTertiaerstufe) {
    this.ausbildungskostenTertiaerstufe = ausbildungskostenTertiaerstufe;
    return this;
  }

  
  @JsonProperty("ausbildungskostenTertiaerstufe")
  public BigDecimal getAusbildungskostenTertiaerstufe() {
    return ausbildungskostenTertiaerstufe;
  }

  @JsonProperty("ausbildungskostenTertiaerstufe")
  public void setAusbildungskostenTertiaerstufe(BigDecimal ausbildungskostenTertiaerstufe) {
    this.ausbildungskostenTertiaerstufe = ausbildungskostenTertiaerstufe;
  }

  /**
   **/
  public EinnahmenKostenUpdateDto wohnkosten(BigDecimal wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  public BigDecimal getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(BigDecimal wohnkosten) {
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
   * Required nur wenn volljährig
   **/
  public EinnahmenKostenUpdateDto willDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
    return this;
  }

  
  @JsonProperty("willDarlehen")
  public Boolean getWillDarlehen() {
    return willDarlehen;
  }

  @JsonProperty("willDarlehen")
  public void setWillDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
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
   * Betreuungskosten eigener Kinder
   **/
  public EinnahmenKostenUpdateDto betreuungskostenKinder(BigDecimal betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
    return this;
  }

  
  @JsonProperty("betreuungskostenKinder")
  public BigDecimal getBetreuungskostenKinder() {
    return betreuungskostenKinder;
  }

  @JsonProperty("betreuungskostenKinder")
  public void setBetreuungskostenKinder(BigDecimal betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
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
        Objects.equals(this.verdienstRealisiert, einnahmenKostenUpdate.verdienstRealisiert) &&
        Objects.equals(this.alimente, einnahmenKostenUpdate.alimente) &&
        Objects.equals(this.zulagen, einnahmenKostenUpdate.zulagen) &&
        Objects.equals(this.renten, einnahmenKostenUpdate.renten) &&
        Objects.equals(this.eoLeistungen, einnahmenKostenUpdate.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, einnahmenKostenUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, einnahmenKostenUpdate.beitraege) &&
        Objects.equals(this.ausbildungskostenSekundarstufeZwei, einnahmenKostenUpdate.ausbildungskostenSekundarstufeZwei) &&
        Objects.equals(this.ausbildungskostenTertiaerstufe, einnahmenKostenUpdate.ausbildungskostenTertiaerstufe) &&
        Objects.equals(this.wohnkosten, einnahmenKostenUpdate.wohnkosten) &&
        Objects.equals(this.wgWohnend, einnahmenKostenUpdate.wgWohnend) &&
        Objects.equals(this.willDarlehen, einnahmenKostenUpdate.willDarlehen) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, einnahmenKostenUpdate.auswaertigeMittagessenProWoche) &&
        Objects.equals(this.betreuungskostenKinder, einnahmenKostenUpdate.betreuungskostenKinder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nettoerwerbseinkommen, fahrkosten, verdienstRealisiert, alimente, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, ausbildungskostenSekundarstufeZwei, ausbildungskostenTertiaerstufe, wohnkosten, wgWohnend, willDarlehen, auswaertigeMittagessenProWoche, betreuungskostenKinder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinnahmenKostenUpdateDto {\n");
    
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    verdienstRealisiert: ").append(toIndentedString(verdienstRealisiert)).append("\n");
    sb.append("    alimente: ").append(toIndentedString(alimente)).append("\n");
    sb.append("    zulagen: ").append(toIndentedString(zulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    beitraege: ").append(toIndentedString(beitraege)).append("\n");
    sb.append("    ausbildungskostenSekundarstufeZwei: ").append(toIndentedString(ausbildungskostenSekundarstufeZwei)).append("\n");
    sb.append("    ausbildungskostenTertiaerstufe: ").append(toIndentedString(ausbildungskostenTertiaerstufe)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    wgWohnend: ").append(toIndentedString(wgWohnend)).append("\n");
    sb.append("    willDarlehen: ").append(toIndentedString(willDarlehen)).append("\n");
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
    sb.append("    betreuungskostenKinder: ").append(toIndentedString(betreuungskostenKinder)).append("\n");
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

