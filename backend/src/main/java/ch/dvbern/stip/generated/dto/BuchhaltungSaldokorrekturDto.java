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



@JsonTypeName("BuchhaltungSaldokorrektur")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BuchhaltungSaldokorrekturDto  implements Serializable {
  private @Valid Integer betrag;
  private @Valid String comment;

  /**
   **/
  public BuchhaltungSaldokorrekturDto betrag(Integer betrag) {
    this.betrag = betrag;
    return this;
  }

  
  @JsonProperty("betrag")
  @NotNull
  public Integer getBetrag() {
    return betrag;
  }

  @JsonProperty("betrag")
  public void setBetrag(Integer betrag) {
    this.betrag = betrag;
  }

  /**
   **/
  public BuchhaltungSaldokorrekturDto comment(String comment) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuchhaltungSaldokorrekturDto buchhaltungSaldokorrektur = (BuchhaltungSaldokorrekturDto) o;
    return Objects.equals(this.betrag, buchhaltungSaldokorrektur.betrag) &&
        Objects.equals(this.comment, buchhaltungSaldokorrektur.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(betrag, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuchhaltungSaldokorrekturDto {\n");
    
    sb.append("    betrag: ").append(toIndentedString(betrag)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

