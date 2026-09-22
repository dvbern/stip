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



@JsonTypeName("BeschwerdeEntscheidCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BeschwerdeEntscheidCreateDto  implements Serializable {
  private String kommentar;
  private Boolean beschwerdeErfolgreich;
  private org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;

  protected BeschwerdeEntscheidCreateDto(BeschwerdeEntscheidCreateDtoBuilder<?, ?> b) {
    this.kommentar = b.kommentar;
    this.beschwerdeErfolgreich = b.beschwerdeErfolgreich;
    this.fileUpload = b.fileUpload;
  }

  public BeschwerdeEntscheidCreateDto() {
  }

  /**
   **/
  public BeschwerdeEntscheidCreateDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty(required = true, value = "kommentar")
  @NotNull public String getKommentar() {
    return kommentar;
  }

  @JsonProperty(required = true, value = "kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public BeschwerdeEntscheidCreateDto beschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
    this.beschwerdeErfolgreich = beschwerdeErfolgreich;
    return this;
  }

  
  @JsonProperty(required = true, value = "beschwerdeErfolgreich")
  @NotNull public Boolean getBeschwerdeErfolgreich() {
    return beschwerdeErfolgreich;
  }

  @JsonProperty(required = true, value = "beschwerdeErfolgreich")
  public void setBeschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
    this.beschwerdeErfolgreich = beschwerdeErfolgreich;
  }

  /**
   **/
  public BeschwerdeEntscheidCreateDto fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
    this.fileUpload = fileUpload;
    return this;
  }

  
  @JsonProperty(required = true, value = "fileUpload")
  @NotNull @Valid public org.jboss.resteasy.reactive.multipart.FileUpload getFileUpload() {
    return fileUpload;
  }

  @JsonProperty(required = true, value = "fileUpload")
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
    BeschwerdeEntscheidCreateDto beschwerdeEntscheidCreate = (BeschwerdeEntscheidCreateDto) o;
    return Objects.equals(this.kommentar, beschwerdeEntscheidCreate.kommentar) &&
        Objects.equals(this.beschwerdeErfolgreich, beschwerdeEntscheidCreate.beschwerdeErfolgreich) &&
        Objects.equals(this.fileUpload, beschwerdeEntscheidCreate.fileUpload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, beschwerdeErfolgreich, fileUpload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeEntscheidCreateDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    beschwerdeErfolgreich: ").append(toIndentedString(beschwerdeErfolgreich)).append("\n");
    sb.append("    fileUpload: ").append(toIndentedString(fileUpload)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static BeschwerdeEntscheidCreateDtoBuilder<?, ?> builder() {
    return new BeschwerdeEntscheidCreateDtoBuilderImpl();
  }

  private static final class BeschwerdeEntscheidCreateDtoBuilderImpl extends BeschwerdeEntscheidCreateDtoBuilder<BeschwerdeEntscheidCreateDto, BeschwerdeEntscheidCreateDtoBuilderImpl> {

    @Override
    protected BeschwerdeEntscheidCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BeschwerdeEntscheidCreateDto build() {
      return new BeschwerdeEntscheidCreateDto(this);
    }
  }

  public static abstract class BeschwerdeEntscheidCreateDtoBuilder<C extends BeschwerdeEntscheidCreateDto, B extends BeschwerdeEntscheidCreateDtoBuilder<C, B>>  {
    private String kommentar;
    private Boolean beschwerdeErfolgreich;
    private org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
    protected abstract B self();

    public abstract C build();

    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
    public B beschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
      this.beschwerdeErfolgreich = beschwerdeErfolgreich;
      return self();
    }
    public B fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
      this.fileUpload = fileUpload;
      return self();
    }
  }
}
