/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * EinnahmenKostenDtoSpec
 */
@JsonPropertyOrder({
  EinnahmenKostenDtoSpec.JSON_PROPERTY_NETTOERWERBSEINKOMMEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_ALIMENTE,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_ZULAGEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_RENTEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_EO_LEISTUNGEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_BEITRAEGE,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_AUSBILDUNGSKOSTEN_SEKUNDARSTUFE_ZWEI,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_AUSBILDUNGSKOSTEN_TERTIAERSTUFE,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_FAHRKOSTEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_WOHNKOSTEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_PERSONEN_IM_HAUSHALT,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_VERDIENST_REALISIERT,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_WILL_DARLEHEN,
  EinnahmenKostenDtoSpec.JSON_PROPERTY_AUSWAERTIGE_MITTAGESSEN_PRO_WOCHE
})
@JsonTypeName("EinnahmenKosten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class EinnahmenKostenDtoSpec {
  public static final String JSON_PROPERTY_NETTOERWERBSEINKOMMEN = "nettoerwerbseinkommen";
  private BigDecimal nettoerwerbseinkommen;

  public static final String JSON_PROPERTY_ALIMENTE = "alimente";
  private BigDecimal alimente;

  public static final String JSON_PROPERTY_ZULAGEN = "zulagen";
  private BigDecimal zulagen;

  public static final String JSON_PROPERTY_RENTEN = "renten";
  private BigDecimal renten;

  public static final String JSON_PROPERTY_EO_LEISTUNGEN = "eoLeistungen";
  private BigDecimal eoLeistungen;

  public static final String JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN = "ergaenzungsleistungen";
  private BigDecimal ergaenzungsleistungen;

  public static final String JSON_PROPERTY_BEITRAEGE = "beitraege";
  private BigDecimal beitraege;

  public static final String JSON_PROPERTY_AUSBILDUNGSKOSTEN_SEKUNDARSTUFE_ZWEI = "ausbildungskostenSekundarstufeZwei";
  private BigDecimal ausbildungskostenSekundarstufeZwei;

  public static final String JSON_PROPERTY_AUSBILDUNGSKOSTEN_TERTIAERSTUFE = "ausbildungskostenTertiaerstufe";
  private BigDecimal ausbildungskostenTertiaerstufe;

  public static final String JSON_PROPERTY_FAHRKOSTEN = "fahrkosten";
  private BigDecimal fahrkosten;

  public static final String JSON_PROPERTY_WOHNKOSTEN = "wohnkosten";
  private BigDecimal wohnkosten;

  public static final String JSON_PROPERTY_PERSONEN_IM_HAUSHALT = "personenImHaushalt";
  private BigDecimal personenImHaushalt;

  public static final String JSON_PROPERTY_VERDIENST_REALISIERT = "verdienstRealisiert";
  private Boolean verdienstRealisiert;

  public static final String JSON_PROPERTY_WILL_DARLEHEN = "willDarlehen";
  private Boolean willDarlehen;

  public static final String JSON_PROPERTY_AUSWAERTIGE_MITTAGESSEN_PRO_WOCHE = "auswaertigeMittagessenProWoche";
  private Integer auswaertigeMittagessenProWoche;

  public EinnahmenKostenDtoSpec() {
  }

  public EinnahmenKostenDtoSpec nettoerwerbseinkommen(BigDecimal nettoerwerbseinkommen) {
    
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

   /**
   * Get nettoerwerbseinkommen
   * @return nettoerwerbseinkommen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NETTOERWERBSEINKOMMEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }


  @JsonProperty(JSON_PROPERTY_NETTOERWERBSEINKOMMEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNettoerwerbseinkommen(BigDecimal nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
  }


  public EinnahmenKostenDtoSpec alimente(BigDecimal alimente) {
    
    this.alimente = alimente;
    return this;
  }

   /**
   * Required nur wenn mind. ein Elternteil Alimente zahlt
   * @return alimente
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALIMENTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getAlimente() {
    return alimente;
  }


  @JsonProperty(JSON_PROPERTY_ALIMENTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAlimente(BigDecimal alimente) {
    this.alimente = alimente;
  }


  public EinnahmenKostenDtoSpec zulagen(BigDecimal zulagen) {
    
    this.zulagen = zulagen;
    return this;
  }

   /**
   * Required nur wenn mind. ein Kind gibt
   * @return zulagen
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ZULAGEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getZulagen() {
    return zulagen;
  }


  @JsonProperty(JSON_PROPERTY_ZULAGEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setZulagen(BigDecimal zulagen) {
    this.zulagen = zulagen;
  }


  public EinnahmenKostenDtoSpec renten(BigDecimal renten) {
    
    this.renten = renten;
    return this;
  }

   /**
   * Required nur wenn mind. ein Elternteil gestorben ist
   * @return renten
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RENTEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getRenten() {
    return renten;
  }


  @JsonProperty(JSON_PROPERTY_RENTEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRenten(BigDecimal renten) {
    this.renten = renten;
  }


  public EinnahmenKostenDtoSpec eoLeistungen(BigDecimal eoLeistungen) {
    
    this.eoLeistungen = eoLeistungen;
    return this;
  }

   /**
   * Get eoLeistungen
   * @return eoLeistungen
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EO_LEISTUNGEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getEoLeistungen() {
    return eoLeistungen;
  }


  @JsonProperty(JSON_PROPERTY_EO_LEISTUNGEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEoLeistungen(BigDecimal eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }


  public EinnahmenKostenDtoSpec ergaenzungsleistungen(BigDecimal ergaenzungsleistungen) {
    
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

   /**
   * Get ergaenzungsleistungen
   * @return ergaenzungsleistungen
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }


  @JsonProperty(JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setErgaenzungsleistungen(BigDecimal ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }


  public EinnahmenKostenDtoSpec beitraege(BigDecimal beitraege) {
    
    this.beitraege = beitraege;
    return this;
  }

   /**
   * Get beitraege
   * @return beitraege
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BEITRAEGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getBeitraege() {
    return beitraege;
  }


  @JsonProperty(JSON_PROPERTY_BEITRAEGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBeitraege(BigDecimal beitraege) {
    this.beitraege = beitraege;
  }


  public EinnahmenKostenDtoSpec ausbildungskostenSekundarstufeZwei(BigDecimal ausbildungskostenSekundarstufeZwei) {
    
    this.ausbildungskostenSekundarstufeZwei = ausbildungskostenSekundarstufeZwei;
    return this;
  }

   /**
   * Required nur wenn die ausgewählte Ausbildung auf der Sekundarstuffe II ist
   * @return ausbildungskostenSekundarstufeZwei
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSKOSTEN_SEKUNDARSTUFE_ZWEI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getAusbildungskostenSekundarstufeZwei() {
    return ausbildungskostenSekundarstufeZwei;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSKOSTEN_SEKUNDARSTUFE_ZWEI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAusbildungskostenSekundarstufeZwei(BigDecimal ausbildungskostenSekundarstufeZwei) {
    this.ausbildungskostenSekundarstufeZwei = ausbildungskostenSekundarstufeZwei;
  }


  public EinnahmenKostenDtoSpec ausbildungskostenTertiaerstufe(BigDecimal ausbildungskostenTertiaerstufe) {
    
    this.ausbildungskostenTertiaerstufe = ausbildungskostenTertiaerstufe;
    return this;
  }

   /**
   * Required nur wenn die ausgewählte Ausbildung auf der Tertiärstufe ist
   * @return ausbildungskostenTertiaerstufe
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSKOSTEN_TERTIAERSTUFE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getAusbildungskostenTertiaerstufe() {
    return ausbildungskostenTertiaerstufe;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSKOSTEN_TERTIAERSTUFE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAusbildungskostenTertiaerstufe(BigDecimal ausbildungskostenTertiaerstufe) {
    this.ausbildungskostenTertiaerstufe = ausbildungskostenTertiaerstufe;
  }


  public EinnahmenKostenDtoSpec fahrkosten(BigDecimal fahrkosten) {
    
    this.fahrkosten = fahrkosten;
    return this;
  }

   /**
   * Get fahrkosten
   * @return fahrkosten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getFahrkosten() {
    return fahrkosten;
  }


  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFahrkosten(BigDecimal fahrkosten) {
    this.fahrkosten = fahrkosten;
  }


  public EinnahmenKostenDtoSpec wohnkosten(BigDecimal wohnkosten) {
    
    this.wohnkosten = wohnkosten;
    return this;
  }

   /**
   * Get wohnkosten
   * @return wohnkosten
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WOHNKOSTEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getWohnkosten() {
    return wohnkosten;
  }


  @JsonProperty(JSON_PROPERTY_WOHNKOSTEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWohnkosten(BigDecimal wohnkosten) {
    this.wohnkosten = wohnkosten;
  }


  public EinnahmenKostenDtoSpec personenImHaushalt(BigDecimal personenImHaushalt) {
    
    this.personenImHaushalt = personenImHaushalt;
    return this;
  }

   /**
   * Get personenImHaushalt
   * @return personenImHaushalt
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PERSONEN_IM_HAUSHALT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getPersonenImHaushalt() {
    return personenImHaushalt;
  }


  @JsonProperty(JSON_PROPERTY_PERSONEN_IM_HAUSHALT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPersonenImHaushalt(BigDecimal personenImHaushalt) {
    this.personenImHaushalt = personenImHaushalt;
  }


  public EinnahmenKostenDtoSpec verdienstRealisiert(Boolean verdienstRealisiert) {
    
    this.verdienstRealisiert = verdienstRealisiert;
    return this;
  }

   /**
   * Get verdienstRealisiert
   * @return verdienstRealisiert
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VERDIENST_REALISIERT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getVerdienstRealisiert() {
    return verdienstRealisiert;
  }


  @JsonProperty(JSON_PROPERTY_VERDIENST_REALISIERT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVerdienstRealisiert(Boolean verdienstRealisiert) {
    this.verdienstRealisiert = verdienstRealisiert;
  }


  public EinnahmenKostenDtoSpec willDarlehen(Boolean willDarlehen) {
    
    this.willDarlehen = willDarlehen;
    return this;
  }

   /**
   * Required nur wenn volljährig
   * @return willDarlehen
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WILL_DARLEHEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getWillDarlehen() {
    return willDarlehen;
  }


  @JsonProperty(JSON_PROPERTY_WILL_DARLEHEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWillDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
  }


  public EinnahmenKostenDtoSpec auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
    
    this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
    return this;
  }

   /**
   * Required nur wenn die Person keinen eigenen Haushalt führt
   * @return auswaertigeMittagessenProWoche
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUSWAERTIGE_MITTAGESSEN_PRO_WOCHE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getAuswaertigeMittagessenProWoche() {
    return auswaertigeMittagessenProWoche;
  }


  @JsonProperty(JSON_PROPERTY_AUSWAERTIGE_MITTAGESSEN_PRO_WOCHE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
    this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EinnahmenKostenDtoSpec einnahmenKosten = (EinnahmenKostenDtoSpec) o;
    return Objects.equals(this.nettoerwerbseinkommen, einnahmenKosten.nettoerwerbseinkommen) &&
        Objects.equals(this.alimente, einnahmenKosten.alimente) &&
        Objects.equals(this.zulagen, einnahmenKosten.zulagen) &&
        Objects.equals(this.renten, einnahmenKosten.renten) &&
        Objects.equals(this.eoLeistungen, einnahmenKosten.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, einnahmenKosten.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, einnahmenKosten.beitraege) &&
        Objects.equals(this.ausbildungskostenSekundarstufeZwei, einnahmenKosten.ausbildungskostenSekundarstufeZwei) &&
        Objects.equals(this.ausbildungskostenTertiaerstufe, einnahmenKosten.ausbildungskostenTertiaerstufe) &&
        Objects.equals(this.fahrkosten, einnahmenKosten.fahrkosten) &&
        Objects.equals(this.wohnkosten, einnahmenKosten.wohnkosten) &&
        Objects.equals(this.personenImHaushalt, einnahmenKosten.personenImHaushalt) &&
        Objects.equals(this.verdienstRealisiert, einnahmenKosten.verdienstRealisiert) &&
        Objects.equals(this.willDarlehen, einnahmenKosten.willDarlehen) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, einnahmenKosten.auswaertigeMittagessenProWoche);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nettoerwerbseinkommen, alimente, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, ausbildungskostenSekundarstufeZwei, ausbildungskostenTertiaerstufe, fahrkosten, wohnkosten, personenImHaushalt, verdienstRealisiert, willDarlehen, auswaertigeMittagessenProWoche);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinnahmenKostenDtoSpec {\n");
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    alimente: ").append(toIndentedString(alimente)).append("\n");
    sb.append("    zulagen: ").append(toIndentedString(zulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    beitraege: ").append(toIndentedString(beitraege)).append("\n");
    sb.append("    ausbildungskostenSekundarstufeZwei: ").append(toIndentedString(ausbildungskostenSekundarstufeZwei)).append("\n");
    sb.append("    ausbildungskostenTertiaerstufe: ").append(toIndentedString(ausbildungskostenTertiaerstufe)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    personenImHaushalt: ").append(toIndentedString(personenImHaushalt)).append("\n");
    sb.append("    verdienstRealisiert: ").append(toIndentedString(verdienstRealisiert)).append("\n");
    sb.append("    willDarlehen: ").append(toIndentedString(willDarlehen)).append("\n");
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
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
