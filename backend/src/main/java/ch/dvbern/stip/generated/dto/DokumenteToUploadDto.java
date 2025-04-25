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
  private @Valid Boolean sbCanFehlendeDokumenteUebermitteln;
  private @Valid Boolean gsCanDokumenteUebermitteln;
  private @Valid Boolean sbCanBearbeitungAbschliessen;
  private @Valid Boolean sbCanUploadUnterschriftenblatt;

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
  /**
   **/
  public DokumenteToUploadDto sbCanFehlendeDokumenteUebermitteln(Boolean sbCanFehlendeDokumenteUebermitteln) {
    this.sbCanFehlendeDokumenteUebermitteln = sbCanFehlendeDokumenteUebermitteln;
    return this;
  }

  
  @JsonProperty("sbCanFehlendeDokumenteUebermitteln")
  public Boolean getSbCanFehlendeDokumenteUebermitteln() {
    return sbCanFehlendeDokumenteUebermitteln;
  }

  @JsonProperty("sbCanFehlendeDokumenteUebermitteln")
  public void setSbCanFehlendeDokumenteUebermitteln(Boolean sbCanFehlendeDokumenteUebermitteln) {
    this.sbCanFehlendeDokumenteUebermitteln = sbCanFehlendeDokumenteUebermitteln;
  }

  /**
   **/
  public DokumenteToUploadDto gsCanDokumenteUebermitteln(Boolean gsCanDokumenteUebermitteln) {
    this.gsCanDokumenteUebermitteln = gsCanDokumenteUebermitteln;
    return this;
  }

  
  @JsonProperty("gsCanDokumenteUebermitteln")
  public Boolean getGsCanDokumenteUebermitteln() {
    return gsCanDokumenteUebermitteln;
  }

  @JsonProperty("gsCanDokumenteUebermitteln")
  public void setGsCanDokumenteUebermitteln(Boolean gsCanDokumenteUebermitteln) {
    this.gsCanDokumenteUebermitteln = gsCanDokumenteUebermitteln;
  }

  /**
   **/
  public DokumenteToUploadDto sbCanBearbeitungAbschliessen(Boolean sbCanBearbeitungAbschliessen) {
    this.sbCanBearbeitungAbschliessen = sbCanBearbeitungAbschliessen;
    return this;
  }

  
  @JsonProperty("sbCanBearbeitungAbschliessen")
  public Boolean getSbCanBearbeitungAbschliessen() {
    return sbCanBearbeitungAbschliessen;
  }

  @JsonProperty("sbCanBearbeitungAbschliessen")
  public void setSbCanBearbeitungAbschliessen(Boolean sbCanBearbeitungAbschliessen) {
    this.sbCanBearbeitungAbschliessen = sbCanBearbeitungAbschliessen;
  }

  /**
   **/
  public DokumenteToUploadDto sbCanUploadUnterschriftenblatt(Boolean sbCanUploadUnterschriftenblatt) {
    this.sbCanUploadUnterschriftenblatt = sbCanUploadUnterschriftenblatt;
    return this;
  }

  
  @JsonProperty("sbCanUploadUnterschriftenblatt")
  public Boolean getSbCanUploadUnterschriftenblatt() {
    return sbCanUploadUnterschriftenblatt;
  }

  @JsonProperty("sbCanUploadUnterschriftenblatt")
  public void setSbCanUploadUnterschriftenblatt(Boolean sbCanUploadUnterschriftenblatt) {
    this.sbCanUploadUnterschriftenblatt = sbCanUploadUnterschriftenblatt;
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
        Objects.equals(this.unterschriftenblaetter, dokumenteToUpload.unterschriftenblaetter) &&
        Objects.equals(this.sbCanFehlendeDokumenteUebermitteln, dokumenteToUpload.sbCanFehlendeDokumenteUebermitteln) &&
        Objects.equals(this.gsCanDokumenteUebermitteln, dokumenteToUpload.gsCanDokumenteUebermitteln) &&
        Objects.equals(this.sbCanBearbeitungAbschliessen, dokumenteToUpload.sbCanBearbeitungAbschliessen) &&
        Objects.equals(this.sbCanUploadUnterschriftenblatt, dokumenteToUpload.sbCanUploadUnterschriftenblatt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customDokumentTyps, required, unterschriftenblaetter, sbCanFehlendeDokumenteUebermitteln, gsCanDokumenteUebermitteln, sbCanBearbeitungAbschliessen, sbCanUploadUnterschriftenblatt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DokumenteToUploadDto {\n");
    
    sb.append("    customDokumentTyps: ").append(toIndentedString(customDokumentTyps)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    unterschriftenblaetter: ").append(toIndentedString(unterschriftenblaetter)).append("\n");
    sb.append("    sbCanFehlendeDokumenteUebermitteln: ").append(toIndentedString(sbCanFehlendeDokumenteUebermitteln)).append("\n");
    sb.append("    gsCanDokumenteUebermitteln: ").append(toIndentedString(gsCanDokumenteUebermitteln)).append("\n");
    sb.append("    sbCanBearbeitungAbschliessen: ").append(toIndentedString(sbCanBearbeitungAbschliessen)).append("\n");
    sb.append("    sbCanUploadUnterschriftenblatt: ").append(toIndentedString(sbCanUploadUnterschriftenblatt)).append("\n");
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

