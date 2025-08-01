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
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * AdresseDtoSpec
 */
@JsonPropertyOrder({
  AdresseDtoSpec.JSON_PROPERTY_ID,
  AdresseDtoSpec.JSON_PROPERTY_LAND_ID,
  AdresseDtoSpec.JSON_PROPERTY_CO_ADRESSE,
  AdresseDtoSpec.JSON_PROPERTY_STRASSE,
  AdresseDtoSpec.JSON_PROPERTY_HAUSNUMMER,
  AdresseDtoSpec.JSON_PROPERTY_PLZ,
  AdresseDtoSpec.JSON_PROPERTY_ORT
})
@JsonTypeName("Adresse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class AdresseDtoSpec {
  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_LAND_ID = "landId";
  private UUID landId;

  public static final String JSON_PROPERTY_CO_ADRESSE = "coAdresse";
  private String coAdresse;

  public static final String JSON_PROPERTY_STRASSE = "strasse";
  private String strasse;

  public static final String JSON_PROPERTY_HAUSNUMMER = "hausnummer";
  private String hausnummer;

  public static final String JSON_PROPERTY_PLZ = "plz";
  private String plz;

  public static final String JSON_PROPERTY_ORT = "ort";
  private String ort;

  public AdresseDtoSpec() {
  }

  public AdresseDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setId(UUID id) {
    this.id = id;
  }


  public AdresseDtoSpec landId(UUID landId) {
    
    this.landId = landId;
    return this;
  }

   /**
   * Get landId
   * @return landId
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_LAND_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getLandId() {
    return landId;
  }


  @JsonProperty(JSON_PROPERTY_LAND_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setLandId(UUID landId) {
    this.landId = landId;
  }


  public AdresseDtoSpec coAdresse(String coAdresse) {
    
    this.coAdresse = coAdresse;
    return this;
  }

   /**
   * Get coAdresse
   * @return coAdresse
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CO_ADRESSE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCoAdresse() {
    return coAdresse;
  }


  @JsonProperty(JSON_PROPERTY_CO_ADRESSE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCoAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
  }


  public AdresseDtoSpec strasse(String strasse) {
    
    this.strasse = strasse;
    return this;
  }

   /**
   * Get strasse
   * @return strasse
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STRASSE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getStrasse() {
    return strasse;
  }


  @JsonProperty(JSON_PROPERTY_STRASSE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }


  public AdresseDtoSpec hausnummer(String hausnummer) {
    
    this.hausnummer = hausnummer;
    return this;
  }

   /**
   * Get hausnummer
   * @return hausnummer
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_HAUSNUMMER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getHausnummer() {
    return hausnummer;
  }


  @JsonProperty(JSON_PROPERTY_HAUSNUMMER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setHausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
  }


  public AdresseDtoSpec plz(String plz) {
    
    this.plz = plz;
    return this;
  }

   /**
   * Get plz
   * @return plz
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PLZ)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getPlz() {
    return plz;
  }


  @JsonProperty(JSON_PROPERTY_PLZ)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPlz(String plz) {
    this.plz = plz;
  }


  public AdresseDtoSpec ort(String ort) {
    
    this.ort = ort;
    return this;
  }

   /**
   * Get ort
   * @return ort
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ORT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getOrt() {
    return ort;
  }


  @JsonProperty(JSON_PROPERTY_ORT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setOrt(String ort) {
    this.ort = ort;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdresseDtoSpec adresse = (AdresseDtoSpec) o;
    return Objects.equals(this.id, adresse.id) &&
        Objects.equals(this.landId, adresse.landId) &&
        Objects.equals(this.coAdresse, adresse.coAdresse) &&
        Objects.equals(this.strasse, adresse.strasse) &&
        Objects.equals(this.hausnummer, adresse.hausnummer) &&
        Objects.equals(this.plz, adresse.plz) &&
        Objects.equals(this.ort, adresse.ort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, landId, coAdresse, strasse, hausnummer, plz, ort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdresseDtoSpec {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    landId: ").append(toIndentedString(landId)).append("\n");
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
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

