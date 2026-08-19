package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
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



@JsonTypeName("CreateAusbildungUnterbruchAntragGS")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class CreateAusbildungUnterbruchAntragGSDto  implements Serializable {
  private @Valid String kommentarGS;
  private @Valid List<org.jboss.resteasy.reactive.multipart.FileUpload> fileUploads = new ArrayList<>();
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;

  protected CreateAusbildungUnterbruchAntragGSDto(CreateAusbildungUnterbruchAntragGSDtoBuilder<?, ?> b) {
    this.kommentarGS = b.kommentarGS;
    this.fileUploads = b.fileUploads;
    this.startDate = b.startDate;
    this.endDate = b.endDate;
  }

  public CreateAusbildungUnterbruchAntragGSDto() {
  }

  /**
   **/
  public CreateAusbildungUnterbruchAntragGSDto kommentarGS(String kommentarGS) {
    this.kommentarGS = kommentarGS;
    return this;
  }

  
  @JsonProperty("kommentarGS")
  @NotNull
  public String getKommentarGS() {
    return kommentarGS;
  }

  @JsonProperty("kommentarGS")
  public void setKommentarGS(String kommentarGS) {
    this.kommentarGS = kommentarGS;
  }

  /**
   **/
  public CreateAusbildungUnterbruchAntragGSDto fileUploads(List<org.jboss.resteasy.reactive.multipart.FileUpload> fileUploads) {
    this.fileUploads = fileUploads;
    return this;
  }

  
  @JsonProperty("fileUploads")
  @NotNull
  public List<org.jboss.resteasy.reactive.multipart.FileUpload> getFileUploads() {
    return fileUploads;
  }

  @JsonProperty("fileUploads")
  public void setFileUploads(List<org.jboss.resteasy.reactive.multipart.FileUpload> fileUploads) {
    this.fileUploads = fileUploads;
  }

  public CreateAusbildungUnterbruchAntragGSDto addFileUploadsItem(org.jboss.resteasy.reactive.multipart.FileUpload fileUploadsItem) {
    if (this.fileUploads == null) {
      this.fileUploads = new ArrayList<>();
    }

    this.fileUploads.add(fileUploadsItem);
    return this;
  }

  public CreateAusbildungUnterbruchAntragGSDto removeFileUploadsItem(org.jboss.resteasy.reactive.multipart.FileUpload fileUploadsItem) {
    if (fileUploadsItem != null && this.fileUploads != null) {
      this.fileUploads.remove(fileUploadsItem);
    }

    return this;
  }
  /**
   **/
  public CreateAusbildungUnterbruchAntragGSDto startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  
  @JsonProperty("startDate")
  @NotNull
  public LocalDate getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  public CreateAusbildungUnterbruchAntragGSDto endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  
  @JsonProperty("endDate")
  @NotNull
  public LocalDate getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateAusbildungUnterbruchAntragGSDto createAusbildungUnterbruchAntragGS = (CreateAusbildungUnterbruchAntragGSDto) o;
    return Objects.equals(this.kommentarGS, createAusbildungUnterbruchAntragGS.kommentarGS) &&
        Objects.equals(this.fileUploads, createAusbildungUnterbruchAntragGS.fileUploads) &&
        Objects.equals(this.startDate, createAusbildungUnterbruchAntragGS.startDate) &&
        Objects.equals(this.endDate, createAusbildungUnterbruchAntragGS.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentarGS, fileUploads, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAusbildungUnterbruchAntragGSDto {\n");
    
    sb.append("    kommentarGS: ").append(toIndentedString(kommentarGS)).append("\n");
    sb.append("    fileUploads: ").append(toIndentedString(fileUploads)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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


  public static CreateAusbildungUnterbruchAntragGSDtoBuilder<?, ?> builder() {
    return new CreateAusbildungUnterbruchAntragGSDtoBuilderImpl();
  }

  private static final class CreateAusbildungUnterbruchAntragGSDtoBuilderImpl extends CreateAusbildungUnterbruchAntragGSDtoBuilder<CreateAusbildungUnterbruchAntragGSDto, CreateAusbildungUnterbruchAntragGSDtoBuilderImpl> {

    @Override
    protected CreateAusbildungUnterbruchAntragGSDtoBuilderImpl self() {
      return this;
    }

    @Override
    public CreateAusbildungUnterbruchAntragGSDto build() {
      return new CreateAusbildungUnterbruchAntragGSDto(this);
    }
  }

  public static abstract class CreateAusbildungUnterbruchAntragGSDtoBuilder<C extends CreateAusbildungUnterbruchAntragGSDto, B extends CreateAusbildungUnterbruchAntragGSDtoBuilder<C, B>>  {
    private String kommentarGS;
    private List<org.jboss.resteasy.reactive.multipart.FileUpload> fileUploads = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    protected abstract B self();

    public abstract C build();

    public B kommentarGS(String kommentarGS) {
      this.kommentarGS = kommentarGS;
      return self();
    }
    public B fileUploads(List<org.jboss.resteasy.reactive.multipart.FileUpload> fileUploads) {
      this.fileUploads = fileUploads;
      return self();
    }
    public B startDate(LocalDate startDate) {
      this.startDate = startDate;
      return self();
    }
    public B endDate(LocalDate endDate) {
      this.endDate = endDate;
      return self();
    }
  }
}

