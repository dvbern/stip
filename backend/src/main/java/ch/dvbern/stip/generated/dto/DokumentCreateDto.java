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



@JsonTypeName("DokumentCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DokumentCreateDto  implements Serializable {
  private @Valid org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;

  protected DokumentCreateDto(DokumentCreateDtoBuilder<?, ?> b) {
    this.fileUpload = b.fileUpload;
  }

  public DokumentCreateDto() {
  }

  /**
   **/
  public DokumentCreateDto fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
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
    DokumentCreateDto dokumentCreate = (DokumentCreateDto) o;
    return Objects.equals(this.fileUpload, dokumentCreate.fileUpload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileUpload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DokumentCreateDto {\n");
    
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


  public static DokumentCreateDtoBuilder<?, ?> builder() {
    return new DokumentCreateDtoBuilderImpl();
  }

  private static final class DokumentCreateDtoBuilderImpl extends DokumentCreateDtoBuilder<DokumentCreateDto, DokumentCreateDtoBuilderImpl> {

    @Override
    protected DokumentCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DokumentCreateDto build() {
      return new DokumentCreateDto(this);
    }
  }

  public static abstract class DokumentCreateDtoBuilder<C extends DokumentCreateDto, B extends DokumentCreateDtoBuilder<C, B>>  {
    private org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
    protected abstract B self();

    public abstract C build();

    public B fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
      this.fileUpload = fileUpload;
      return self();
    }
  }
}

