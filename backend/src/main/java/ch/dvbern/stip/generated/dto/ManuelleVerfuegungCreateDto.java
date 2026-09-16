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



@JsonTypeName("ManuelleVerfuegungCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class ManuelleVerfuegungCreateDto  implements Serializable {
  private @Valid org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
  private @Valid String kommentar;

  protected ManuelleVerfuegungCreateDto(ManuelleVerfuegungCreateDtoBuilder<?, ?> b) {
    this.fileUpload = b.fileUpload;
    this.kommentar = b.kommentar;
  }

  public ManuelleVerfuegungCreateDto() {
  }

  /**
   **/
  public ManuelleVerfuegungCreateDto fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
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

  /**
   **/
  public ManuelleVerfuegungCreateDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
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
    ManuelleVerfuegungCreateDto manuelleVerfuegungCreate = (ManuelleVerfuegungCreateDto) o;
    return Objects.equals(this.fileUpload, manuelleVerfuegungCreate.fileUpload) &&
        Objects.equals(this.kommentar, manuelleVerfuegungCreate.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileUpload, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ManuelleVerfuegungCreateDto {\n");
    
    sb.append("    fileUpload: ").append(toIndentedString(fileUpload)).append("\n");
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


  public static ManuelleVerfuegungCreateDtoBuilder<?, ?> builder() {
    return new ManuelleVerfuegungCreateDtoBuilderImpl();
  }

  private static final class ManuelleVerfuegungCreateDtoBuilderImpl extends ManuelleVerfuegungCreateDtoBuilder<ManuelleVerfuegungCreateDto, ManuelleVerfuegungCreateDtoBuilderImpl> {

    @Override
    protected ManuelleVerfuegungCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public ManuelleVerfuegungCreateDto build() {
      return new ManuelleVerfuegungCreateDto(this);
    }
  }

  public static abstract class ManuelleVerfuegungCreateDtoBuilder<C extends ManuelleVerfuegungCreateDto, B extends ManuelleVerfuegungCreateDtoBuilder<C, B>>  {
    private org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
    private String kommentar;
    protected abstract B self();

    public abstract C build();

    public B fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
      this.fileUpload = fileUpload;
      return self();
    }
    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
  }
}

