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



@JsonTypeName("DemoDataImport")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataImportDto  implements Serializable {
  private @Valid String kommentar;
  private @Valid Boolean ignoreBerechnungErrors;
  private @Valid org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;

  protected DemoDataImportDto(DemoDataImportDtoBuilder<?, ?> b) {
    this.kommentar = b.kommentar;
    this.ignoreBerechnungErrors = b.ignoreBerechnungErrors;
    this.fileUpload = b.fileUpload;
  }

  public DemoDataImportDto() {
  }

  /**
   **/
  public DemoDataImportDto kommentar(String kommentar) {
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
  public DemoDataImportDto ignoreBerechnungErrors(Boolean ignoreBerechnungErrors) {
    this.ignoreBerechnungErrors = ignoreBerechnungErrors;
    return this;
  }

  
  @JsonProperty("ignoreBerechnungErrors")
  @NotNull
  public Boolean getIgnoreBerechnungErrors() {
    return ignoreBerechnungErrors;
  }

  @JsonProperty("ignoreBerechnungErrors")
  public void setIgnoreBerechnungErrors(Boolean ignoreBerechnungErrors) {
    this.ignoreBerechnungErrors = ignoreBerechnungErrors;
  }

  /**
   **/
  public DemoDataImportDto fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
    this.fileUpload = fileUpload;
    return this;
  }

  
  @JsonProperty("fileUpload")
  @NotNull
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
    DemoDataImportDto demoDataImport = (DemoDataImportDto) o;
    return Objects.equals(this.kommentar, demoDataImport.kommentar) &&
        Objects.equals(this.ignoreBerechnungErrors, demoDataImport.ignoreBerechnungErrors) &&
        Objects.equals(this.fileUpload, demoDataImport.fileUpload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, ignoreBerechnungErrors, fileUpload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataImportDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    ignoreBerechnungErrors: ").append(toIndentedString(ignoreBerechnungErrors)).append("\n");
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


  public static DemoDataImportDtoBuilder<?, ?> builder() {
    return new DemoDataImportDtoBuilderImpl();
  }

  private static final class DemoDataImportDtoBuilderImpl extends DemoDataImportDtoBuilder<DemoDataImportDto, DemoDataImportDtoBuilderImpl> {

    @Override
    protected DemoDataImportDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataImportDto build() {
      return new DemoDataImportDto(this);
    }
  }

  public static abstract class DemoDataImportDtoBuilder<C extends DemoDataImportDto, B extends DemoDataImportDtoBuilder<C, B>>  {
    private String kommentar;
    private Boolean ignoreBerechnungErrors;
    private org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
    protected abstract B self();

    public abstract C build();

    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
    public B ignoreBerechnungErrors(Boolean ignoreBerechnungErrors) {
      this.ignoreBerechnungErrors = ignoreBerechnungErrors;
      return self();
    }
    public B fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
      this.fileUpload = fileUpload;
      return self();
    }
  }
}

