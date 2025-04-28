package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
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



@JsonTypeName("BeschwerdeEntscheid")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BeschwerdeEntscheidDto  implements Serializable {
  private @Valid String kommentar;
  private @Valid Boolean beschwerdeErfolgreich;
  private @Valid List<DokumentDto> dokumente = new ArrayList<>();

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
  public BeschwerdeEntscheidDto beschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
    this.beschwerdeErfolgreich = beschwerdeErfolgreich;
    return this;
  }

  
  @JsonProperty("beschwerdeErfolgreich")
  @NotNull
  public Boolean getBeschwerdeErfolgreich() {
    return beschwerdeErfolgreich;
  }

  @JsonProperty("beschwerdeErfolgreich")
  public void setBeschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
    this.beschwerdeErfolgreich = beschwerdeErfolgreich;
  }

  /**
   **/
  public BeschwerdeEntscheidDto dokumente(List<DokumentDto> dokumente) {
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

  public BeschwerdeEntscheidDto addDokumenteItem(DokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public BeschwerdeEntscheidDto removeDokumenteItem(DokumentDto dokumenteItem) {
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
    BeschwerdeEntscheidDto beschwerdeEntscheid = (BeschwerdeEntscheidDto) o;
    return Objects.equals(this.kommentar, beschwerdeEntscheid.kommentar) &&
        Objects.equals(this.beschwerdeErfolgreich, beschwerdeEntscheid.beschwerdeErfolgreich) &&
        Objects.equals(this.dokumente, beschwerdeEntscheid.dokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, beschwerdeErfolgreich, dokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeEntscheidDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    beschwerdeErfolgreich: ").append(toIndentedString(beschwerdeErfolgreich)).append("\n");
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

