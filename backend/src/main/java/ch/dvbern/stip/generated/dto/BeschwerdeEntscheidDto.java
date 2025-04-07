package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("BeschwerdeEntscheid")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BeschwerdeEntscheidDto  implements Serializable {
  private @Valid String kommentar;
  private @Valid Boolean isBeschwerdeErfolgreich;
  private @Valid org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;

  /**
   **/
  public BeschwerdeEntscheidDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  @NotNull
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public BeschwerdeEntscheidDto isBeschwerdeErfolgreich(Boolean isBeschwerdeErfolgreich) {
    this.isBeschwerdeErfolgreich = isBeschwerdeErfolgreich;
    return this;
  }

  
  @JsonProperty("isBeschwerdeErfolgreich")
  @NotNull
  public Boolean getIsBeschwerdeErfolgreich() {
    return isBeschwerdeErfolgreich;
  }

  @JsonProperty("isBeschwerdeErfolgreich")
  public void setIsBeschwerdeErfolgreich(Boolean isBeschwerdeErfolgreich) {
    this.isBeschwerdeErfolgreich = isBeschwerdeErfolgreich;
  }

  /**
   **/
  public BeschwerdeEntscheidDto fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
    this.fileUpload = fileUpload;
    return this;
  }

  
  @JsonProperty("fileUpload")
  public org.jboss.resteasy.reactive.multipart.FileUpload getFileUpload() {
    return fileUpload;
  }

  @JsonProperty("fileUpload")
  public void setFileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
    this.fileUpload = fileUpload;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeschwerdeEntscheidDto beschwerdeEntscheid = (BeschwerdeEntscheidDto) o;
    return Objects.equals(this.kommentar, beschwerdeEntscheid.kommentar) &&
        Objects.equals(this.isBeschwerdeErfolgreich, beschwerdeEntscheid.isBeschwerdeErfolgreich) &&
        Objects.equals(this.fileUpload, beschwerdeEntscheid.fileUpload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, isBeschwerdeErfolgreich, fileUpload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeEntscheidDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    isBeschwerdeErfolgreich: ").append(toIndentedString(isBeschwerdeErfolgreich)).append("\n");
    sb.append("    fileUpload: ").append(toIndentedString(fileUpload)).append("\n");
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

