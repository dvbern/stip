package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("AusbildungUnterbruchLimits")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungUnterbruchLimitsDto  implements Serializable {
  private @Valid LocalDate unterbruchLatestEndDate;
  private @Valid LocalDate unterbruchEarliestStartDate;

  protected AusbildungUnterbruchLimitsDto(AusbildungUnterbruchLimitsDtoBuilder<?, ?> b) {
    this.unterbruchLatestEndDate = b.unterbruchLatestEndDate;
    this.unterbruchEarliestStartDate = b.unterbruchEarliestStartDate;
  }

  public AusbildungUnterbruchLimitsDto() {
  }

  /**
   **/
  public AusbildungUnterbruchLimitsDto unterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
    this.unterbruchLatestEndDate = unterbruchLatestEndDate;
    return this;
  }

  
  @JsonProperty("unterbruchLatestEndDate")
  @NotNull
  public LocalDate getUnterbruchLatestEndDate() {
    return unterbruchLatestEndDate;
  }

  @JsonProperty("unterbruchLatestEndDate")
  public void setUnterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
    this.unterbruchLatestEndDate = unterbruchLatestEndDate;
  }

  /**
   **/
  public AusbildungUnterbruchLimitsDto unterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
    this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
    return this;
  }

  
  @JsonProperty("unterbruchEarliestStartDate")
  @NotNull
  public LocalDate getUnterbruchEarliestStartDate() {
    return unterbruchEarliestStartDate;
  }

  @JsonProperty("unterbruchEarliestStartDate")
  public void setUnterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
    this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungUnterbruchLimitsDto ausbildungUnterbruchLimits = (AusbildungUnterbruchLimitsDto) o;
    return Objects.equals(this.unterbruchLatestEndDate, ausbildungUnterbruchLimits.unterbruchLatestEndDate) &&
        Objects.equals(this.unterbruchEarliestStartDate, ausbildungUnterbruchLimits.unterbruchEarliestStartDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unterbruchLatestEndDate, unterbruchEarliestStartDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungUnterbruchLimitsDto {\n");
    
    sb.append("    unterbruchLatestEndDate: ").append(toIndentedString(unterbruchLatestEndDate)).append("\n");
    sb.append("    unterbruchEarliestStartDate: ").append(toIndentedString(unterbruchEarliestStartDate)).append("\n");
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


  public static AusbildungUnterbruchLimitsDtoBuilder<?, ?> builder() {
    return new AusbildungUnterbruchLimitsDtoBuilderImpl();
  }

  private static final class AusbildungUnterbruchLimitsDtoBuilderImpl extends AusbildungUnterbruchLimitsDtoBuilder<AusbildungUnterbruchLimitsDto, AusbildungUnterbruchLimitsDtoBuilderImpl> {

    @Override
    protected AusbildungUnterbruchLimitsDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungUnterbruchLimitsDto build() {
      return new AusbildungUnterbruchLimitsDto(this);
    }
  }

  public static abstract class AusbildungUnterbruchLimitsDtoBuilder<C extends AusbildungUnterbruchLimitsDto, B extends AusbildungUnterbruchLimitsDtoBuilder<C, B>>  {
    private LocalDate unterbruchLatestEndDate;
    private LocalDate unterbruchEarliestStartDate;
    protected abstract B self();

    public abstract C build();

    public B unterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
      this.unterbruchLatestEndDate = unterbruchLatestEndDate;
      return self();
    }
    public B unterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
      this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
      return self();
    }
  }
}

