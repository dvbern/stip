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



@JsonTypeName("AusbildungUnterbruchAntragGS")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungUnterbruchAntragGSDto  implements Serializable {
  private @Valid UUID id;
  private @Valid List<DokumentDto> dokuments = new ArrayList<>();

  /**
   **/
  public AusbildungUnterbruchAntragGSDto id(UUID id) {
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
  public AusbildungUnterbruchAntragGSDto dokuments(List<DokumentDto> dokuments) {
    this.dokuments = dokuments;
    return this;
  }

  
  @JsonProperty("dokuments")
  @NotNull
  public List<DokumentDto> getDokuments() {
    return dokuments;
  }

  @JsonProperty("dokuments")
  public void setDokuments(List<DokumentDto> dokuments) {
    this.dokuments = dokuments;
  }

  public AusbildungUnterbruchAntragGSDto addDokumentsItem(DokumentDto dokumentsItem) {
    if (this.dokuments == null) {
      this.dokuments = new ArrayList<>();
    }

    this.dokuments.add(dokumentsItem);
    return this;
  }

  public AusbildungUnterbruchAntragGSDto removeDokumentsItem(DokumentDto dokumentsItem) {
    if (dokumentsItem != null && this.dokuments != null) {
      this.dokuments.remove(dokumentsItem);
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
    AusbildungUnterbruchAntragGSDto ausbildungUnterbruchAntragGS = (AusbildungUnterbruchAntragGSDto) o;
    return Objects.equals(this.id, ausbildungUnterbruchAntragGS.id) &&
        Objects.equals(this.dokuments, ausbildungUnterbruchAntragGS.dokuments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokuments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungUnterbruchAntragGSDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokuments: ").append(toIndentedString(dokuments)).append("\n");
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

