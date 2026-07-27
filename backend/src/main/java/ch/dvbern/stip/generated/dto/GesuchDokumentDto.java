package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.CustomDokumentTypDto;
import ch.dvbern.stip.generated.dto.DokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchDokumentDto  implements Serializable {
  private @Valid UUID id;
  private @Valid List<DokumentDto> dokumente = new ArrayList<>();
  private @Valid ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus status;
  private @Valid UUID entryId;
  private @Valid ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
  private @Valid CustomDokumentTypDto customDokumentTyp;

  protected GesuchDokumentDto(GesuchDokumentDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.dokumente = b.dokumente;
    this.status = b.status;
    this.entryId = b.entryId;
    this.dokumentTyp = b.dokumentTyp;
    this.customDokumentTyp = b.customDokumentTyp;
  }

  public GesuchDokumentDto() {
  }

  /**
   **/
  public GesuchDokumentDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public GesuchDokumentDto dokumente(List<DokumentDto> dokumente) {
    this.dokumente = dokumente;
    return this;
  }

  
  @JsonProperty("dokumente")
  @NotNull
  public List<DokumentDto> getDokumente() {
    return dokumente;
  }

  @JsonProperty("dokumente")
  public void setDokumente(List<DokumentDto> dokumente) {
    this.dokumente = dokumente;
  }

  public GesuchDokumentDto addDokumenteItem(DokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public GesuchDokumentDto removeDokumenteItem(DokumentDto dokumenteItem) {
    if (dokumenteItem != null && this.dokumente != null) {
      this.dokumente.remove(dokumenteItem);
    }

    return this;
  }
  /**
   **/
  public GesuchDokumentDto status(ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus status) {
    this.status = status;
  }

  /**
   **/
  public GesuchDokumentDto entryId(UUID entryId) {
    this.entryId = entryId;
    return this;
  }

  
  @JsonProperty("entryId")
  public UUID getEntryId() {
    return entryId;
  }

  @JsonProperty("entryId")
  public void setEntryId(UUID entryId) {
    this.entryId = entryId;
  }

  /**
   **/
  public GesuchDokumentDto dokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
    return this;
  }

  
  @JsonProperty("dokumentTyp")
  public ch.dvbern.stip.api.dokument.type.DokumentTyp getDokumentTyp() {
    return dokumentTyp;
  }

  @JsonProperty("dokumentTyp")
  public void setDokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
  }

  /**
   **/
  public GesuchDokumentDto customDokumentTyp(CustomDokumentTypDto customDokumentTyp) {
    this.customDokumentTyp = customDokumentTyp;
    return this;
  }

  
  @JsonProperty("customDokumentTyp")
  public CustomDokumentTypDto getCustomDokumentTyp() {
    return customDokumentTyp;
  }

  @JsonProperty("customDokumentTyp")
  public void setCustomDokumentTyp(CustomDokumentTypDto customDokumentTyp) {
    this.customDokumentTyp = customDokumentTyp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDokumentDto gesuchDokument = (GesuchDokumentDto) o;
    return Objects.equals(this.id, gesuchDokument.id) &&
        Objects.equals(this.dokumente, gesuchDokument.dokumente) &&
        Objects.equals(this.status, gesuchDokument.status) &&
        Objects.equals(this.entryId, gesuchDokument.entryId) &&
        Objects.equals(this.dokumentTyp, gesuchDokument.dokumentTyp) &&
        Objects.equals(this.customDokumentTyp, gesuchDokument.customDokumentTyp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokumente, status, entryId, dokumentTyp, customDokumentTyp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    dokumentTyp: ").append(toIndentedString(dokumentTyp)).append("\n");
    sb.append("    customDokumentTyp: ").append(toIndentedString(customDokumentTyp)).append("\n");
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


  public static GesuchDokumentDtoBuilder<?, ?> builder() {
    return new GesuchDokumentDtoBuilderImpl();
  }

  private static final class GesuchDokumentDtoBuilderImpl extends GesuchDokumentDtoBuilder<GesuchDokumentDto, GesuchDokumentDtoBuilderImpl> {

    @Override
    protected GesuchDokumentDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchDokumentDto build() {
      return new GesuchDokumentDto(this);
    }
  }

  public static abstract class GesuchDokumentDtoBuilder<C extends GesuchDokumentDto, B extends GesuchDokumentDtoBuilder<C, B>>  {
    private UUID id;
    private List<DokumentDto> dokumente = new ArrayList<>();
    private ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus status;
    private UUID entryId;
    private ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
    private CustomDokumentTypDto customDokumentTyp;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B dokumente(List<DokumentDto> dokumente) {
      this.dokumente = dokumente;
      return self();
    }
    public B status(ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus status) {
      this.status = status;
      return self();
    }
    public B entryId(UUID entryId) {
      this.entryId = entryId;
      return self();
    }
    public B dokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
      this.dokumentTyp = dokumentTyp;
      return self();
    }
    public B customDokumentTyp(CustomDokumentTypDto customDokumentTyp) {
      this.customDokumentTyp = customDokumentTyp;
      return self();
    }
  }
}

