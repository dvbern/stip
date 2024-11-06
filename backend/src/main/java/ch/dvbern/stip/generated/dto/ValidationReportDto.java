package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.ValidationErrorDto;
import ch.dvbern.stip.generated.dto.ValidationWarningDto;
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



@JsonTypeName("ValidationReport")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ValidationReportDto  implements Serializable {
  private @Valid List<ValidationErrorDto> validationErrors = new ArrayList<>();
  private @Valid List<ValidationWarningDto> validationWarnings = new ArrayList<>();
  private @Valid Boolean hasDocuments;

  /**
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
  /**
   **/
  public ValidationReportDto validationWarnings(List<ValidationWarningDto> validationWarnings) {
    this.validationWarnings = validationWarnings;
    return this;
  }

  
  @JsonProperty("validationWarnings")
  @NotNull
  public List<ValidationWarningDto> getValidationWarnings() {
    return validationWarnings;
  }

  @JsonProperty("validationWarnings")
  public void setValidationWarnings(List<ValidationWarningDto> validationWarnings) {
    this.validationWarnings = validationWarnings;
  }

  public ValidationReportDto addValidationWarningsItem(ValidationWarningDto validationWarningsItem) {
    if (this.validationWarnings == null) {
      this.validationWarnings = new ArrayList<>();
    }

    this.validationWarnings.add(validationWarningsItem);
    return this;
  }

  public ValidationReportDto removeValidationWarningsItem(ValidationWarningDto validationWarningsItem) {
    if (validationWarningsItem != null && this.validationWarnings != null) {
      this.validationWarnings.remove(validationWarningsItem);
    }

    return this;
  }
  /**
   * Whether or not the Gesuch validated has one or more GesuchDokument attached
   **/
  public ValidationReportDto hasDocuments(Boolean hasDocuments) {
    this.hasDocuments = hasDocuments;
    return this;
  }

  
  @JsonProperty("hasDocuments")
  public Boolean getHasDocuments() {
    return hasDocuments;
  }

  @JsonProperty("hasDocuments")
  public void setHasDocuments(Boolean hasDocuments) {
    this.hasDocuments = hasDocuments;
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
    return Objects.equals(this.validationErrors, validationReport.validationErrors) &&
        Objects.equals(this.validationWarnings, validationReport.validationWarnings) &&
        Objects.equals(this.hasDocuments, validationReport.hasDocuments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(validationErrors, validationWarnings, hasDocuments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationReportDto {\n");
    
    sb.append("    validationErrors: ").append(toIndentedString(validationErrors)).append("\n");
    sb.append("    validationWarnings: ").append(toIndentedString(validationWarnings)).append("\n");
    sb.append("    hasDocuments: ").append(toIndentedString(hasDocuments)).append("\n");
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

