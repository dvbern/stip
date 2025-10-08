package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.VerfuegungDto;
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



@JsonTypeName("AdminDokumente")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AdminDokumenteDto  implements Serializable {
  private @Valid List<VerfuegungDto> verfuegungen = new ArrayList<>();
  private @Valid UUID datenschutzbriefMassendruckJobId;

  /**
   **/
  public AdminDokumenteDto verfuegungen(List<VerfuegungDto> verfuegungen) {
    this.verfuegungen = verfuegungen;
    return this;
  }

  
  @JsonProperty("verfuegungen")
  @NotNull
  public List<VerfuegungDto> getVerfuegungen() {
    return verfuegungen;
  }

  @JsonProperty("verfuegungen")
  public void setVerfuegungen(List<VerfuegungDto> verfuegungen) {
    this.verfuegungen = verfuegungen;
  }

  public AdminDokumenteDto addVerfuegungenItem(VerfuegungDto verfuegungenItem) {
    if (this.verfuegungen == null) {
      this.verfuegungen = new ArrayList<>();
    }

    this.verfuegungen.add(verfuegungenItem);
    return this;
  }

  public AdminDokumenteDto removeVerfuegungenItem(VerfuegungDto verfuegungenItem) {
    if (verfuegungenItem != null && this.verfuegungen != null) {
      this.verfuegungen.remove(verfuegungenItem);
    }

    return this;
  }
  /**
   **/
  public AdminDokumenteDto datenschutzbriefMassendruckJobId(UUID datenschutzbriefMassendruckJobId) {
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
    AdminDokumenteDto adminDokumente = (AdminDokumenteDto) o;
    return Objects.equals(this.verfuegungen, adminDokumente.verfuegungen) &&
        Objects.equals(this.datenschutzbriefMassendruckJobId, adminDokumente.datenschutzbriefMassendruckJobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verfuegungen, datenschutzbriefMassendruckJobId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminDokumenteDto {\n");
    
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

