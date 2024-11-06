package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("CreateGesuchTrancheRequest")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class CreateGesuchTrancheRequestDto  implements Serializable {
  private @Valid LocalDate start;
  private @Valid String comment;
  private @Valid LocalDate end;

  /**
   **/
  public CreateGesuchTrancheRequestDto start(LocalDate start) {
    this.start = start;
    return this;
  }


  @JsonProperty("start")
  @NotNull
  public LocalDate getStart() {
    return start;
  }

  @JsonProperty("start")
  public void setStart(LocalDate start) {
    this.start = start;
  }

  /**
   **/
  public CreateGesuchTrancheRequestDto comment(String comment) {
    this.comment = comment;
    return this;
  }


  @JsonProperty("comment")
  @NotNull
  public String getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   **/
  public CreateGesuchTrancheRequestDto end(LocalDate end) {
    this.end = end;
    return this;
  }


  @JsonProperty("end")
  public LocalDate getEnd() {
    return end;
  }

  @JsonProperty("end")
  public void setEnd(LocalDate end) {
    this.end = end;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateGesuchTrancheRequestDto createGesuchTrancheRequest = (CreateGesuchTrancheRequestDto) o;
    return Objects.equals(this.start, createGesuchTrancheRequest.start) &&
        Objects.equals(this.comment, createGesuchTrancheRequest.comment) &&
        Objects.equals(this.end, createGesuchTrancheRequest.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, comment, end);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateGesuchTrancheRequestDto {\n");

    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
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

