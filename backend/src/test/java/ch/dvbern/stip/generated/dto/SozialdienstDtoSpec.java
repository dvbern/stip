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
import ch.dvbern.stip.generated.dto.SozialdienstAdminDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * SozialdienstDtoSpec
 */
@JsonPropertyOrder({
  SozialdienstDtoSpec.JSON_PROPERTY_ID,
  SozialdienstDtoSpec.JSON_PROPERTY_NAME,
  SozialdienstDtoSpec.JSON_PROPERTY_ADRESSE,
  SozialdienstDtoSpec.JSON_PROPERTY_IBAN,
  SozialdienstDtoSpec.JSON_PROPERTY_SOZIALDIENST_ADMIN
})
@JsonTypeName("Sozialdienst")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class SozialdienstDtoSpec {
  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_ADRESSE = "adresse";
  private AdresseDtoSpec adresse;

  public static final String JSON_PROPERTY_IBAN = "iban";
  private String iban;

  public static final String JSON_PROPERTY_SOZIALDIENST_ADMIN = "sozialdienstAdmin";
  private SozialdienstAdminDtoSpec sozialdienstAdmin;

  public SozialdienstDtoSpec() {
  }

  public SozialdienstDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setId(UUID id) {
    this.id = id;
  }


  public SozialdienstDtoSpec name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getName() {
    return name;
  }


  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setName(String name) {
    this.name = name;
  }


  public SozialdienstDtoSpec adresse(AdresseDtoSpec adresse) {
    
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


  public SozialdienstDtoSpec iban(String iban) {
    
    this.iban = iban;
    return this;
  }

   /**
   * Get iban
   * @return iban
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_IBAN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getIban() {
    return iban;
  }


  @JsonProperty(JSON_PROPERTY_IBAN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIban(String iban) {
    this.iban = iban;
  }


  public SozialdienstDtoSpec sozialdienstAdmin(SozialdienstAdminDtoSpec sozialdienstAdmin) {
    
    this.sozialdienstAdmin = sozialdienstAdmin;
    return this;
  }

   /**
   * Get sozialdienstAdmin
   * @return sozialdienstAdmin
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SOZIALDIENST_ADMIN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public SozialdienstAdminDtoSpec getSozialdienstAdmin() {
    return sozialdienstAdmin;
  }


  @JsonProperty(JSON_PROPERTY_SOZIALDIENST_ADMIN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSozialdienstAdmin(SozialdienstAdminDtoSpec sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstDtoSpec sozialdienst = (SozialdienstDtoSpec) o;
    return Objects.equals(this.id, sozialdienst.id) &&
        Objects.equals(this.name, sozialdienst.name) &&
        Objects.equals(this.adresse, sozialdienst.adresse) &&
        Objects.equals(this.iban, sozialdienst.iban) &&
        Objects.equals(this.sozialdienstAdmin, sozialdienst.sozialdienstAdmin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, adresse, iban, sozialdienstAdmin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstDtoSpec {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    sozialdienstAdmin: ").append(toIndentedString(sozialdienstAdmin)).append("\n");
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
