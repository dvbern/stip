package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.VerfuegungDto;
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



@JsonTypeName("AdminDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AdminDokumentDto  implements Serializable {
  private @Valid VerfuegungDto verfuegungen;
  private @Valid UUID datenschutzbriefMassendruckJobId;

  /**
   **/
  public AdminDokumentDto verfuegungen(VerfuegungDto verfuegungen) {
    this.verfuegungen = verfuegungen;
    return this;
  }

  
  @JsonProperty("verfuegungen")
  @NotNull
  public VerfuegungDto getVerfuegungen() {
    return verfuegungen;
  }

  @JsonProperty("verfuegungen")
  public void setVerfuegungen(VerfuegungDto verfuegungen) {
    this.verfuegungen = verfuegungen;
  }

  /**
   **/
  public AdminDokumentDto datenschutzbriefMassendruckJobId(UUID datenschutzbriefMassendruckJobId) {
    this.datenschutzbriefMassendruckJobId = datenschutzbriefMassendruckJobId;
    return this;
  }

  
  @JsonProperty("datenschutzbriefMassendruckJobId")
  public UUID getDatenschutzbriefMassendruckJobId() {
    return datenschutzbriefMassendruckJobId;
  }

  @JsonProperty("datenschutzbriefMassendruckJobId")
  public void setDatenschutzbriefMassendruckJobId(UUID datenschutzbriefMassendruckJobId) {
    this.datenschutzbriefMassendruckJobId = datenschutzbriefMassendruckJobId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminDokumentDto adminDokument = (AdminDokumentDto) o;
    return Objects.equals(this.verfuegungen, adminDokument.verfuegungen) &&
        Objects.equals(this.datenschutzbriefMassendruckJobId, adminDokument.datenschutzbriefMassendruckJobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verfuegungen, datenschutzbriefMassendruckJobId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminDokumentDto {\n");
    
    sb.append("    verfuegungen: ").append(toIndentedString(verfuegungen)).append("\n");
    sb.append("    datenschutzbriefMassendruckJobId: ").append(toIndentedString(datenschutzbriefMassendruckJobId)).append("\n");
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

