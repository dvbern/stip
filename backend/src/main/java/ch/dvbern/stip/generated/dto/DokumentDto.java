package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("Dokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DokumentDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String filename;
  private @Valid String filepfad;
  private @Valid String filesize;
  private @Valid String objectId;

  /**
   **/
  public DokumentDto id(UUID id) {
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
  public DokumentDto filename(String filename) {
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

  /**
   **/
  public DokumentDto filepfad(String filepfad) {
    this.filepfad = filepfad;
    return this;
  }

  
  @JsonProperty("filepfad")
  @NotNull
  public String getFilepfad() {
    return filepfad;
  }

  @JsonProperty("filepfad")
  public void setFilepfad(String filepfad) {
    this.filepfad = filepfad;
  }

  /**
   **/
  public DokumentDto filesize(String filesize) {
    this.filesize = filesize;
    return this;
  }

  
  @JsonProperty("filesize")
  @NotNull
  public String getFilesize() {
    return filesize;
  }

  @JsonProperty("filesize")
  public void setFilesize(String filesize) {
    this.filesize = filesize;
  }

  /**
   * 
   **/
  public DokumentDto objectId(String objectId) {
    this.objectId = objectId;
    return this;
  }

  
  @JsonProperty("objectId")
  @NotNull
  public String getObjectId() {
    return objectId;
  }

  @JsonProperty("objectId")
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DokumentDto dokument = (DokumentDto) o;
    return Objects.equals(this.id, dokument.id) &&
        Objects.equals(this.filename, dokument.filename) &&
        Objects.equals(this.filepfad, dokument.filepfad) &&
        Objects.equals(this.filesize, dokument.filesize) &&
        Objects.equals(this.objectId, dokument.objectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, filename, filepfad, filesize, objectId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DokumentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    filepfad: ").append(toIndentedString(filepfad)).append("\n");
    sb.append("    filesize: ").append(toIndentedString(filesize)).append("\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
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

