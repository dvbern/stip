package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.CustomDokumentTypDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DokumenteToUpload")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DokumenteToUploadDto  implements Serializable {
  private @Valid List<CustomDokumentTypDto> customDokumentTyps;
  private @Valid List<ch.dvbern.stip.api.dokument.type.DokumentTyp> required;
  private @Valid List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> unterschriftenblaetter;

  /**
   **/
  public DokumenteToUploadDto customDokumentTyps(List<CustomDokumentTypDto> customDokumentTyps) {
    this.customDokumentTyps = customDokumentTyps;
    return this;
  }

  
  @JsonProperty("customDokumentTyps")
  public List<CustomDokumentTypDto> getCustomDokumentTyps() {
    return customDokumentTyps;
  }

  @JsonProperty("customDokumentTyps")
  public void setCustomDokumentTyps(List<CustomDokumentTypDto> customDokumentTyps) {
    this.customDokumentTyps = customDokumentTyps;
  }

  public DokumenteToUploadDto addCustomDokumentTypsItem(CustomDokumentTypDto customDokumentTypsItem) {
    if (this.customDokumentTyps == null) {
      this.customDokumentTyps = new ArrayList<>();
    }

    this.customDokumentTyps.add(customDokumentTypsItem);
    return this;
  }

  public DokumenteToUploadDto removeCustomDokumentTypsItem(CustomDokumentTypDto customDokumentTypsItem) {
    if (customDokumentTypsItem != null && this.customDokumentTyps != null) {
      this.customDokumentTyps.remove(customDokumentTypsItem);
    }

    return this;
  }
  /**
   **/
  public DokumenteToUploadDto required(List<ch.dvbern.stip.api.dokument.type.DokumentTyp> required) {
    this.required = required;
    return this;
  }

  
  @JsonProperty("required")
  public List<ch.dvbern.stip.api.dokument.type.DokumentTyp> getRequired() {
    return required;
  }

  @JsonProperty("required")
  public void setRequired(List<ch.dvbern.stip.api.dokument.type.DokumentTyp> required) {
    this.required = required;
  }

  public DokumenteToUploadDto addRequiredItem(ch.dvbern.stip.api.dokument.type.DokumentTyp requiredItem) {
    if (this.required == null) {
      this.required = new ArrayList<>();
    }

    this.required.add(requiredItem);
    return this;
  }

  public DokumenteToUploadDto removeRequiredItem(ch.dvbern.stip.api.dokument.type.DokumentTyp requiredItem) {
    if (requiredItem != null && this.required != null) {
      this.required.remove(requiredItem);
    }

    return this;
  }
  /**
   **/
  public DokumenteToUploadDto unterschriftenblaetter(List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> unterschriftenblaetter) {
    this.unterschriftenblaetter = unterschriftenblaetter;
    return this;
  }

  
  @JsonProperty("unterschriftenblaetter")
  public List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> getUnterschriftenblaetter() {
    return unterschriftenblaetter;
  }

  @JsonProperty("unterschriftenblaetter")
  public void setUnterschriftenblaetter(List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> unterschriftenblaetter) {
    this.unterschriftenblaetter = unterschriftenblaetter;
  }

  public DokumenteToUploadDto addUnterschriftenblaetterItem(ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp unterschriftenblaetterItem) {
    if (this.unterschriftenblaetter == null) {
      this.unterschriftenblaetter = new ArrayList<>();
    }

    this.unterschriftenblaetter.add(unterschriftenblaetterItem);
    return this;
  }

  public DokumenteToUploadDto removeUnterschriftenblaetterItem(ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp unterschriftenblaetterItem) {
    if (unterschriftenblaetterItem != null && this.unterschriftenblaetter != null) {
      this.unterschriftenblaetter.remove(unterschriftenblaetterItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DokumenteToUploadDto dokumenteToUpload = (DokumenteToUploadDto) o;
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
    sb.append("class DokumenteToUploadDto {\n");
    
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

