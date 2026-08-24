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



@JsonTypeName("CreateAusbildungUnterbruchSB")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class CreateAusbildungUnterbruchSBDto  implements Serializable {
  private @Valid String kommentarGS;
  private @Valid org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status;
  private @Valid String kommentarSB;
  private @Valid Integer monateOhneAnspruch;

  protected CreateAusbildungUnterbruchSBDto(CreateAusbildungUnterbruchSBDtoBuilder<?, ?> b) {
    this.kommentarGS = b.kommentarGS;
    this.fileUpload = b.fileUpload;
    this.startDate = b.startDate;
    this.endDate = b.endDate;
    this.status = b.status;
    this.kommentarSB = b.kommentarSB;
    this.monateOhneAnspruch = b.monateOhneAnspruch;
  }

  public CreateAusbildungUnterbruchSBDto() {
  }

  /**
   **/
  public CreateAusbildungUnterbruchSBDto kommentarGS(String kommentarGS) {
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
  public CreateAusbildungUnterbruchSBDto fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
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
  public CreateAusbildungUnterbruchSBDto startDate(LocalDate startDate) {
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
  public CreateAusbildungUnterbruchSBDto endDate(LocalDate endDate) {
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

  /**
   **/
  public CreateAusbildungUnterbruchSBDto status(ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status) {
    this.status = status;
  }

  /**
   **/
  public CreateAusbildungUnterbruchSBDto kommentarSB(String kommentarSB) {
    this.kommentarSB = kommentarSB;
    return this;
  }

  
  @JsonProperty("kommentarSB")
  @NotNull
  public String getKommentarSB() {
    return kommentarSB;
  }

  @JsonProperty("kommentarSB")
  public void setKommentarSB(String kommentarSB) {
    this.kommentarSB = kommentarSB;
  }

  /**
   **/
  public CreateAusbildungUnterbruchSBDto monateOhneAnspruch(Integer monateOhneAnspruch) {
    this.monateOhneAnspruch = monateOhneAnspruch;
    return this;
  }

  
  @JsonProperty("monateOhneAnspruch")
  public Integer getMonateOhneAnspruch() {
    return monateOhneAnspruch;
  }

  @JsonProperty("monateOhneAnspruch")
  public void setMonateOhneAnspruch(Integer monateOhneAnspruch) {
    this.monateOhneAnspruch = monateOhneAnspruch;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateAusbildungUnterbruchSBDto createAusbildungUnterbruchSB = (CreateAusbildungUnterbruchSBDto) o;
    return Objects.equals(this.kommentarGS, createAusbildungUnterbruchSB.kommentarGS) &&
        Objects.equals(this.fileUpload, createAusbildungUnterbruchSB.fileUpload) &&
        Objects.equals(this.startDate, createAusbildungUnterbruchSB.startDate) &&
        Objects.equals(this.endDate, createAusbildungUnterbruchSB.endDate) &&
        Objects.equals(this.status, createAusbildungUnterbruchSB.status) &&
        Objects.equals(this.kommentarSB, createAusbildungUnterbruchSB.kommentarSB) &&
        Objects.equals(this.monateOhneAnspruch, createAusbildungUnterbruchSB.monateOhneAnspruch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentarGS, fileUpload, startDate, endDate, status, kommentarSB, monateOhneAnspruch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAusbildungUnterbruchSBDto {\n");
    
    sb.append("    kommentarGS: ").append(toIndentedString(kommentarGS)).append("\n");
    sb.append("    fileUpload: ").append(toIndentedString(fileUpload)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    kommentarSB: ").append(toIndentedString(kommentarSB)).append("\n");
    sb.append("    monateOhneAnspruch: ").append(toIndentedString(monateOhneAnspruch)).append("\n");
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


  public static CreateAusbildungUnterbruchSBDtoBuilder<?, ?> builder() {
    return new CreateAusbildungUnterbruchSBDtoBuilderImpl();
  }

  private static final class CreateAusbildungUnterbruchSBDtoBuilderImpl extends CreateAusbildungUnterbruchSBDtoBuilder<CreateAusbildungUnterbruchSBDto, CreateAusbildungUnterbruchSBDtoBuilderImpl> {

    @Override
    protected CreateAusbildungUnterbruchSBDtoBuilderImpl self() {
      return this;
    }

    @Override
    public CreateAusbildungUnterbruchSBDto build() {
      return new CreateAusbildungUnterbruchSBDto(this);
    }
  }

  public static abstract class CreateAusbildungUnterbruchSBDtoBuilder<C extends CreateAusbildungUnterbruchSBDto, B extends CreateAusbildungUnterbruchSBDtoBuilder<C, B>>  {
    private String kommentarGS;
    private org.jboss.resteasy.reactive.multipart.FileUpload fileUpload;
    private LocalDate startDate;
    private LocalDate endDate;
    private ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status;
    private String kommentarSB;
    private Integer monateOhneAnspruch;
    protected abstract B self();

    public abstract C build();

    public B kommentarGS(String kommentarGS) {
      this.kommentarGS = kommentarGS;
      return self();
    }
    public B fileUpload(org.jboss.resteasy.reactive.multipart.FileUpload fileUpload) {
      this.fileUpload = fileUpload;
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
    public B status(ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status) {
      this.status = status;
      return self();
    }
    public B kommentarSB(String kommentarSB) {
      this.kommentarSB = kommentarSB;
      return self();
    }
    public B monateOhneAnspruch(Integer monateOhneAnspruch) {
      this.monateOhneAnspruch = monateOhneAnspruch;
      return self();
    }
  }
}

