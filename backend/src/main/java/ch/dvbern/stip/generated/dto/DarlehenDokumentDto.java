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



@JsonTypeName("DarlehenDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenDokumentDto  implements Serializable {
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType dokumentType;
  private @Valid List<DokumentDto> dokumente = new ArrayList<>();

  /**
   **/
  public DarlehenDokumentDto id(UUID id) {
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
  public DarlehenDokumentDto dokumentType(ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType dokumentType) {
    this.dokumentType = dokumentType;
    return this;
  }

  
  @JsonProperty("dokumentType")
  @NotNull
  public ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType getDokumentType() {
    return dokumentType;
  }

  @JsonProperty("dokumentType")
  public void setDokumentType(ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType dokumentType) {
    this.dokumentType = dokumentType;
  }

  /**
   **/
  public DarlehenDokumentDto dokumente(List<DokumentDto> dokumente) {
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

  public DarlehenDokumentDto addDokumenteItem(DokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public DarlehenDokumentDto removeDokumenteItem(DokumentDto dokumenteItem) {
    if (dokumenteItem != null && this.dokumente != null) {
      this.dokumente.remove(dokumenteItem);
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
    DarlehenDokumentDto darlehenDokument = (DarlehenDokumentDto) o;
    return Objects.equals(this.id, darlehenDokument.id) &&
        Objects.equals(this.dokumentType, darlehenDokument.dokumentType) &&
        Objects.equals(this.dokumente, darlehenDokument.dokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokumentType, dokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenDokumentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokumentType: ").append(toIndentedString(dokumentType)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
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

