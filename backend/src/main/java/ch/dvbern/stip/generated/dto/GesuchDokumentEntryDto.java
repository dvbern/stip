package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("GesuchDokumentEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDokumentEntryDto  implements Serializable {
  private @Valid UUID entryId;
  private @Valid String name;
  private @Valid List<ch.dvbern.stip.api.dokument.type.DokumentTyp> dokumentTyps = new ArrayList<>();

  /**
   **/
  public GesuchDokumentEntryDto entryId(UUID entryId) {
    this.entryId = entryId;
    return this;
  }

  
  @JsonProperty("entryId")
  @NotNull
  public UUID getEntryId() {
    return entryId;
  }

  @JsonProperty("entryId")
  public void setEntryId(UUID entryId) {
    this.entryId = entryId;
  }

  /**
   **/
  public GesuchDokumentEntryDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public GesuchDokumentEntryDto dokumentTyps(List<ch.dvbern.stip.api.dokument.type.DokumentTyp> dokumentTyps) {
    this.dokumentTyps = dokumentTyps;
    return this;
  }

  
  @JsonProperty("dokumentTyps")
  @NotNull
  public List<ch.dvbern.stip.api.dokument.type.DokumentTyp> getDokumentTyps() {
    return dokumentTyps;
  }

  @JsonProperty("dokumentTyps")
  public void setDokumentTyps(List<ch.dvbern.stip.api.dokument.type.DokumentTyp> dokumentTyps) {
    this.dokumentTyps = dokumentTyps;
  }

  public GesuchDokumentEntryDto addDokumentTypsItem(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTypsItem) {
    if (this.dokumentTyps == null) {
      this.dokumentTyps = new ArrayList<>();
    }

    this.dokumentTyps.add(dokumentTypsItem);
    return this;
  }

  public GesuchDokumentEntryDto removeDokumentTypsItem(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTypsItem) {
    if (dokumentTypsItem != null && this.dokumentTyps != null) {
      this.dokumentTyps.remove(dokumentTypsItem);
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
    GesuchDokumentEntryDto gesuchDokumentEntry = (GesuchDokumentEntryDto) o;
    return Objects.equals(this.entryId, gesuchDokumentEntry.entryId) &&
        Objects.equals(this.name, gesuchDokumentEntry.name) &&
        Objects.equals(this.dokumentTyps, gesuchDokumentEntry.dokumentTyps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, name, dokumentTyps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentEntryDto {\n");
    
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dokumentTyps: ").append(toIndentedString(dokumentTyps)).append("\n");
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

