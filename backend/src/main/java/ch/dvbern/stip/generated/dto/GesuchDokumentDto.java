package ch.dvbern.stip.generated.dto;

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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDokumentDto  implements Serializable {
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
  private @Valid List<DokumentDto> dokumente = new ArrayList<>();
  private @Valid ch.dvbern.stip.api.dokument.type.Dokumentstatus status;

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
  public GesuchDokumentDto dokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
    return this;
  }

  
  @JsonProperty("dokumentTyp")
  @NotNull
  public ch.dvbern.stip.api.dokument.type.DokumentTyp getDokumentTyp() {
    return dokumentTyp;
  }

  @JsonProperty("dokumentTyp")
  public void setDokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
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
  public GesuchDokumentDto status(ch.dvbern.stip.api.dokument.type.Dokumentstatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.dokument.type.Dokumentstatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.dokument.type.Dokumentstatus status) {
    this.status = status;
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
        Objects.equals(this.dokumentTyp, gesuchDokument.dokumentTyp) &&
        Objects.equals(this.dokumente, gesuchDokument.dokumente) &&
        Objects.equals(this.status, gesuchDokument.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokumentTyp, dokumente, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokumentTyp: ").append(toIndentedString(dokumentTyp)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

