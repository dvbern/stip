package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.CustomDokumentTypDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentRefDto;
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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DokumenteToUploadDto  implements Serializable {
  private @Valid List<CustomDokumentTypDto> customDokumentTyps;
  private @Valid List<ch.dvbern.stip.api.dokument.type.DokumentTyp> required;
  private @Valid List<GesuchDokumentRefDto> requiredRefs;
  private @Valid List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> unterschriftenblaetter;
  private @Valid Boolean sbCanFehlendeDokumenteUebermitteln;
  private @Valid Boolean gsCanDokumenteUebermitteln;
  private @Valid Boolean sbCanUploadUnterschriftenblatt;

  protected DokumenteToUploadDto(DokumenteToUploadDtoBuilder<?, ?> b) {
    this.customDokumentTyps = b.customDokumentTyps;
    this.required = b.required;
    this.requiredRefs = b.requiredRefs;
    this.unterschriftenblaetter = b.unterschriftenblaetter;
    this.sbCanFehlendeDokumenteUebermitteln = b.sbCanFehlendeDokumenteUebermitteln;
    this.gsCanDokumenteUebermitteln = b.gsCanDokumenteUebermitteln;
    this.sbCanUploadUnterschriftenblatt = b.sbCanUploadUnterschriftenblatt;
  }

  public DokumenteToUploadDto() {
  }

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
  public DokumenteToUploadDto requiredRefs(List<GesuchDokumentRefDto> requiredRefs) {
    this.requiredRefs = requiredRefs;
    return this;
  }

  
  @JsonProperty("requiredRefs")
  public List<GesuchDokumentRefDto> getRequiredRefs() {
    return requiredRefs;
  }

  @JsonProperty("requiredRefs")
  public void setRequiredRefs(List<GesuchDokumentRefDto> requiredRefs) {
    this.requiredRefs = requiredRefs;
  }

  public DokumenteToUploadDto addRequiredRefsItem(GesuchDokumentRefDto requiredRefsItem) {
    if (this.requiredRefs == null) {
      this.requiredRefs = new ArrayList<>();
    }

    this.requiredRefs.add(requiredRefsItem);
    return this;
  }

  public DokumenteToUploadDto removeRequiredRefsItem(GesuchDokumentRefDto requiredRefsItem) {
    if (requiredRefsItem != null && this.requiredRefs != null) {
      this.requiredRefs.remove(requiredRefsItem);
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
        Objects.equals(this.requiredRefs, dokumenteToUpload.requiredRefs) &&
        Objects.equals(this.unterschriftenblaetter, dokumenteToUpload.unterschriftenblaetter) &&
        Objects.equals(this.sbCanFehlendeDokumenteUebermitteln, dokumenteToUpload.sbCanFehlendeDokumenteUebermitteln) &&
        Objects.equals(this.gsCanDokumenteUebermitteln, dokumenteToUpload.gsCanDokumenteUebermitteln) &&
        Objects.equals(this.sbCanUploadUnterschriftenblatt, dokumenteToUpload.sbCanUploadUnterschriftenblatt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customDokumentTyps, required, requiredRefs, unterschriftenblaetter, sbCanFehlendeDokumenteUebermitteln, gsCanDokumenteUebermitteln, sbCanUploadUnterschriftenblatt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DokumenteToUploadDto {\n");
    
    sb.append("    customDokumentTyps: ").append(toIndentedString(customDokumentTyps)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    requiredRefs: ").append(toIndentedString(requiredRefs)).append("\n");
    sb.append("    unterschriftenblaetter: ").append(toIndentedString(unterschriftenblaetter)).append("\n");
    sb.append("    sbCanFehlendeDokumenteUebermitteln: ").append(toIndentedString(sbCanFehlendeDokumenteUebermitteln)).append("\n");
    sb.append("    gsCanDokumenteUebermitteln: ").append(toIndentedString(gsCanDokumenteUebermitteln)).append("\n");
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


  public static DokumenteToUploadDtoBuilder<?, ?> builder() {
    return new DokumenteToUploadDtoBuilderImpl();
  }

  private static final class DokumenteToUploadDtoBuilderImpl extends DokumenteToUploadDtoBuilder<DokumenteToUploadDto, DokumenteToUploadDtoBuilderImpl> {

    @Override
    protected DokumenteToUploadDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DokumenteToUploadDto build() {
      return new DokumenteToUploadDto(this);
    }
  }

  public static abstract class DokumenteToUploadDtoBuilder<C extends DokumenteToUploadDto, B extends DokumenteToUploadDtoBuilder<C, B>>  {
    private List<CustomDokumentTypDto> customDokumentTyps;
    private List<ch.dvbern.stip.api.dokument.type.DokumentTyp> required;
    private List<GesuchDokumentRefDto> requiredRefs;
    private List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> unterschriftenblaetter;
    private Boolean sbCanFehlendeDokumenteUebermitteln;
    private Boolean gsCanDokumenteUebermitteln;
    private Boolean sbCanUploadUnterschriftenblatt;
    protected abstract B self();

    public abstract C build();

    public B customDokumentTyps(List<CustomDokumentTypDto> customDokumentTyps) {
      this.customDokumentTyps = customDokumentTyps;
      return self();
    }
    public B required(List<ch.dvbern.stip.api.dokument.type.DokumentTyp> required) {
      this.required = required;
      return self();
    }
    public B requiredRefs(List<GesuchDokumentRefDto> requiredRefs) {
      this.requiredRefs = requiredRefs;
      return self();
    }
    public B unterschriftenblaetter(List<ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp> unterschriftenblaetter) {
      this.unterschriftenblaetter = unterschriftenblaetter;
      return self();
    }
    public B sbCanFehlendeDokumenteUebermitteln(Boolean sbCanFehlendeDokumenteUebermitteln) {
      this.sbCanFehlendeDokumenteUebermitteln = sbCanFehlendeDokumenteUebermitteln;
      return self();
    }
    public B gsCanDokumenteUebermitteln(Boolean gsCanDokumenteUebermitteln) {
      this.gsCanDokumenteUebermitteln = gsCanDokumenteUebermitteln;
      return self();
    }
    public B sbCanUploadUnterschriftenblatt(Boolean sbCanUploadUnterschriftenblatt) {
      this.sbCanUploadUnterschriftenblatt = sbCanUploadUnterschriftenblatt;
      return self();
    }
  }
}

