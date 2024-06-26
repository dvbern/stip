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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * WelcomeMailDtoSpec
 */
@JsonPropertyOrder({
  WelcomeMailDtoSpec.JSON_PROPERTY_NAME,
  WelcomeMailDtoSpec.JSON_PROPERTY_VORNAME,
  WelcomeMailDtoSpec.JSON_PROPERTY_EMAIL,
  WelcomeMailDtoSpec.JSON_PROPERTY_REDIRECT_URI
})
@JsonTypeName("WelcomeMail")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class WelcomeMailDtoSpec {
  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_VORNAME = "vorname";
  private String vorname;

  public static final String JSON_PROPERTY_EMAIL = "email";
  private String email;

  public static final String JSON_PROPERTY_REDIRECT_URI = "redirectUri";
  private String redirectUri;

  public WelcomeMailDtoSpec() {
  }

  public WelcomeMailDtoSpec name(String name) {
    
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


  public WelcomeMailDtoSpec vorname(String vorname) {
    
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


  public WelcomeMailDtoSpec email(String email) {
    
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EMAIL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getEmail() {
    return email;
  }


  @JsonProperty(JSON_PROPERTY_EMAIL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEmail(String email) {
    this.email = email;
  }


  public WelcomeMailDtoSpec redirectUri(String redirectUri) {
    
    this.redirectUri = redirectUri;
    return this;
  }

   /**
   * Get redirectUri
   * @return redirectUri
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_REDIRECT_URI)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getRedirectUri() {
    return redirectUri;
  }


  @JsonProperty(JSON_PROPERTY_REDIRECT_URI)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WelcomeMailDtoSpec welcomeMail = (WelcomeMailDtoSpec) o;
    return Objects.equals(this.name, welcomeMail.name) &&
        Objects.equals(this.vorname, welcomeMail.vorname) &&
        Objects.equals(this.email, welcomeMail.email) &&
        Objects.equals(this.redirectUri, welcomeMail.redirectUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, vorname, email, redirectUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WelcomeMailDtoSpec {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    redirectUri: ").append(toIndentedString(redirectUri)).append("\n");
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

