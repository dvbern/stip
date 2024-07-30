package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Meta information um ein GesuchDokument abzulehnen
 **/

@JsonTypeName("GesuchDokumentAblehnenRequest")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDokumentAblehnenRequestDto  implements Serializable {
  private @Valid GesuchDokumentKommentarDto kommentar;

  /**
   **/
  public GesuchDokumentAblehnenRequestDto kommentar(GesuchDokumentKommentarDto kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  public GesuchDokumentKommentarDto getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(GesuchDokumentKommentarDto kommentar) {
    this.kommentar = kommentar;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequest = (GesuchDokumentAblehnenRequestDto) o;
    return Objects.equals(this.kommentar, gesuchDokumentAblehnenRequest.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentAblehnenRequestDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
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

