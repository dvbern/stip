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
import ch.dvbern.stip.generated.dto.CustomDokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * DokumenteToUploadDtoSpec
 */
@JsonPropertyOrder({
  DokumenteToUploadDtoSpec.JSON_PROPERTY_CUSTOM_DOKUMENT_TYPS,
  DokumenteToUploadDtoSpec.JSON_PROPERTY_REQUIRED,
  DokumenteToUploadDtoSpec.JSON_PROPERTY_UNTERSCHRIFTENBLAETTER
})
@JsonTypeName("DokumenteToUpload")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class DokumenteToUploadDtoSpec {
  public static final String JSON_PROPERTY_CUSTOM_DOKUMENT_TYPS = "customDokumentTyps";
  private List<CustomDokumentTypDtoSpec> customDokumentTyps;

  public static final String JSON_PROPERTY_REQUIRED = "required";
  private List<DokumentTypDtoSpec> required;

  public static final String JSON_PROPERTY_UNTERSCHRIFTENBLAETTER = "unterschriftenblaetter";
  private List<UnterschriftenblattDokumentTypDtoSpec> unterschriftenblaetter;

  public DokumenteToUploadDtoSpec() {
  }

  public DokumenteToUploadDtoSpec customDokumentTyps(List<CustomDokumentTypDtoSpec> customDokumentTyps) {
    
    this.customDokumentTyps = customDokumentTyps;
    return this;
  }

  public DokumenteToUploadDtoSpec addCustomDokumentTypsItem(CustomDokumentTypDtoSpec customDokumentTypsItem) {
    if (this.customDokumentTyps == null) {
      this.customDokumentTyps = new ArrayList<>();
    }
    this.customDokumentTyps.add(customDokumentTypsItem);
    return this;
  }

   /**
   * Get customDokumentTyps
   * @return customDokumentTyps
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CUSTOM_DOKUMENT_TYPS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<CustomDokumentTypDtoSpec> getCustomDokumentTyps() {
    return customDokumentTyps;
  }


  @JsonProperty(JSON_PROPERTY_CUSTOM_DOKUMENT_TYPS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCustomDokumentTyps(List<CustomDokumentTypDtoSpec> customDokumentTyps) {
    this.customDokumentTyps = customDokumentTyps;
  }


  public DokumenteToUploadDtoSpec required(List<DokumentTypDtoSpec> required) {
    
    this.required = required;
    return this;
  }

  public DokumenteToUploadDtoSpec addRequiredItem(DokumentTypDtoSpec requiredItem) {
    if (this.required == null) {
      this.required = new ArrayList<>();
    }
    this.required.add(requiredItem);
    return this;
  }

   /**
   * Get required
   * @return required
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REQUIRED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<DokumentTypDtoSpec> getRequired() {
    return required;
  }


  @JsonProperty(JSON_PROPERTY_REQUIRED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRequired(List<DokumentTypDtoSpec> required) {
    this.required = required;
  }


  public DokumenteToUploadDtoSpec unterschriftenblaetter(List<UnterschriftenblattDokumentTypDtoSpec> unterschriftenblaetter) {
    
    this.unterschriftenblaetter = unterschriftenblaetter;
    return this;
  }

  public DokumenteToUploadDtoSpec addUnterschriftenblaetterItem(UnterschriftenblattDokumentTypDtoSpec unterschriftenblaetterItem) {
    if (this.unterschriftenblaetter == null) {
      this.unterschriftenblaetter = new ArrayList<>();
    }
    this.unterschriftenblaetter.add(unterschriftenblaetterItem);
    return this;
  }

   /**
   * Get unterschriftenblaetter
   * @return unterschriftenblaetter
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_UNTERSCHRIFTENBLAETTER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<UnterschriftenblattDokumentTypDtoSpec> getUnterschriftenblaetter() {
    return unterschriftenblaetter;
  }


  @JsonProperty(JSON_PROPERTY_UNTERSCHRIFTENBLAETTER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUnterschriftenblaetter(List<UnterschriftenblattDokumentTypDtoSpec> unterschriftenblaetter) {
    this.unterschriftenblaetter = unterschriftenblaetter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DokumenteToUploadDtoSpec dokumenteToUpload = (DokumenteToUploadDtoSpec) o;
    return Objects.equals(this.customDokumentTyps, dokumenteToUpload.customDokumentTyps) &&
        Objects.equals(this.required, dokumenteToUpload.required) &&
        Objects.equals(this.unterschriftenblaetter, dokumenteToUpload.unterschriftenblaetter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customDokumentTyps, required, unterschriftenblaetter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DokumenteToUploadDtoSpec {\n");
    sb.append("    customDokumentTyps: ").append(toIndentedString(customDokumentTyps)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    unterschriftenblaetter: ").append(toIndentedString(unterschriftenblaetter)).append("\n");
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

