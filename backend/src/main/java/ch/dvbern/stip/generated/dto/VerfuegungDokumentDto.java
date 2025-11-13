package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.VerfuegungDokumentTypDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("VerfuegungDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class VerfuegungDokumentDto  implements Serializable {
  private @Valid UUID id;
  private @Valid VerfuegungDokumentTypDto typ;
  private @Valid String filename;

  /**
   **/
  public VerfuegungDokumentDto id(UUID id) {
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
  public VerfuegungDokumentDto typ(VerfuegungDokumentTypDto typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  @NotNull
  public VerfuegungDokumentTypDto getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(VerfuegungDokumentTypDto typ) {
    this.typ = typ;
  }

  /**
   **/
  public VerfuegungDokumentDto filename(String filename) {
    this.filename = filename;
    return this;
  }

  
  @JsonProperty("filename")
  @NotNull
  public String getFilename() {
    return filename;
  }

  @JsonProperty("filename")
  public void setFilename(String filename) {
    this.filename = filename;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerfuegungDokumentDto verfuegungDokument = (VerfuegungDokumentDto) o;
    return Objects.equals(this.id, verfuegungDokument.id) &&
        Objects.equals(this.typ, verfuegungDokument.typ) &&
        Objects.equals(this.filename, verfuegungDokument.filename);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, typ, filename);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerfuegungDokumentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
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

