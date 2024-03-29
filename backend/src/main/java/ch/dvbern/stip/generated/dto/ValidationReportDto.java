package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.ValidationErrorDto;
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

/**
 * 
 **/

@JsonTypeName("ValidationReport")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ValidationReportDto  implements Serializable {
  private @Valid List<ValidationErrorDto> validationErrors = new ArrayList<>();

  /**
   * 
   **/
  public ValidationReportDto validationErrors(List<ValidationErrorDto> validationErrors) {
    this.validationErrors = validationErrors;
    return this;
  }

  
  @JsonProperty("validationErrors")
  @NotNull
  public List<ValidationErrorDto> getValidationErrors() {
    return validationErrors;
  }

  @JsonProperty("validationErrors")
  public void setValidationErrors(List<ValidationErrorDto> validationErrors) {
    this.validationErrors = validationErrors;
  }

  public ValidationReportDto addValidationErrorsItem(ValidationErrorDto validationErrorsItem) {
    if (this.validationErrors == null) {
      this.validationErrors = new ArrayList<>();
    }

    this.validationErrors.add(validationErrorsItem);
    return this;
  }

  public ValidationReportDto removeValidationErrorsItem(ValidationErrorDto validationErrorsItem) {
    if (validationErrorsItem != null && this.validationErrors != null) {
      this.validationErrors.remove(validationErrorsItem);
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
    ValidationReportDto validationReport = (ValidationReportDto) o;
    return Objects.equals(this.validationErrors, validationReport.validationErrors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(validationErrors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationReportDto {\n");
    
    sb.append("    validationErrors: ").append(toIndentedString(validationErrors)).append("\n");
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

