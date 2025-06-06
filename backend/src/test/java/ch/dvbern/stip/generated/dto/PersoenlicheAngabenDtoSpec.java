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
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.AnredeDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * PersoenlicheAngabenDtoSpec
 */
@JsonPropertyOrder({
  PersoenlicheAngabenDtoSpec.JSON_PROPERTY_ANREDE,
  PersoenlicheAngabenDtoSpec.JSON_PROPERTY_NACHNAME,
  PersoenlicheAngabenDtoSpec.JSON_PROPERTY_VORNAME,
  PersoenlicheAngabenDtoSpec.JSON_PROPERTY_GEBURTSDATUM,
  PersoenlicheAngabenDtoSpec.JSON_PROPERTY_ADRESSE
})
@JsonTypeName("PersoenlicheAngaben")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class PersoenlicheAngabenDtoSpec {
  public static final String JSON_PROPERTY_ANREDE = "anrede";
  private AnredeDtoSpec anrede;

  public static final String JSON_PROPERTY_NACHNAME = "nachname";
  private String nachname;

  public static final String JSON_PROPERTY_VORNAME = "vorname";
  private String vorname;

  public static final String JSON_PROPERTY_GEBURTSDATUM = "geburtsdatum";
  private LocalDate geburtsdatum;

  public static final String JSON_PROPERTY_ADRESSE = "adresse";
  private AdresseDtoSpec adresse;

  public PersoenlicheAngabenDtoSpec() {
  }

  public PersoenlicheAngabenDtoSpec anrede(AnredeDtoSpec anrede) {
    
    this.anrede = anrede;
    return this;
  }

   /**
   * Get anrede
   * @return anrede
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ANREDE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public AnredeDtoSpec getAnrede() {
    return anrede;
  }


  @JsonProperty(JSON_PROPERTY_ANREDE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAnrede(AnredeDtoSpec anrede) {
    this.anrede = anrede;
  }


  public PersoenlicheAngabenDtoSpec nachname(String nachname) {
    
    this.nachname = nachname;
    return this;
  }

   /**
   * Get nachname
   * @return nachname
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NACHNAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getNachname() {
    return nachname;
  }


  @JsonProperty(JSON_PROPERTY_NACHNAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }


  public PersoenlicheAngabenDtoSpec vorname(String vorname) {
    
    this.vorname = vorname;
    return this;
  }

   /**
   * Get vorname
   * @return vorname
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VORNAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getVorname() {
    return vorname;
  }


  @JsonProperty(JSON_PROPERTY_VORNAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }


  public PersoenlicheAngabenDtoSpec geburtsdatum(LocalDate geburtsdatum) {
    
    this.geburtsdatum = geburtsdatum;
    return this;
  }

   /**
   * Get geburtsdatum
   * @return geburtsdatum
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GEBURTSDATUM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }


  @JsonProperty(JSON_PROPERTY_GEBURTSDATUM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }


  public PersoenlicheAngabenDtoSpec adresse(AdresseDtoSpec adresse) {
    
    this.adresse = adresse;
    return this;
  }

   /**
   * Get adresse
   * @return adresse
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ADRESSE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public AdresseDtoSpec getAdresse() {
    return adresse;
  }


  @JsonProperty(JSON_PROPERTY_ADRESSE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAdresse(AdresseDtoSpec adresse) {
    this.adresse = adresse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersoenlicheAngabenDtoSpec persoenlicheAngaben = (PersoenlicheAngabenDtoSpec) o;
    return Objects.equals(this.anrede, persoenlicheAngaben.anrede) &&
        Objects.equals(this.nachname, persoenlicheAngaben.nachname) &&
        Objects.equals(this.vorname, persoenlicheAngaben.vorname) &&
        Objects.equals(this.geburtsdatum, persoenlicheAngaben.geburtsdatum) &&
        Objects.equals(this.adresse, persoenlicheAngaben.adresse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anrede, nachname, vorname, geburtsdatum, adresse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlicheAngabenDtoSpec {\n");
    sb.append("    anrede: ").append(toIndentedString(anrede)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
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

