package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoDataSlimDto;
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



@JsonTypeName("DemoDataList")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataListDto  implements Serializable {
  private @Valid String importiertVon;
  private @Valid java.time.LocalDateTime letzteAktivitaet;
  private @Valid String kommentar;
  private @Valid String documentId;
  private @Valid String filename;
  private @Valid String filepath;
  private @Valid Integer filesize;
  private @Valid List<DemoDataSlimDto> demoDatas = new ArrayList<>();

  /**
   **/
  public DemoDataListDto importiertVon(String importiertVon) {
    this.importiertVon = importiertVon;
    return this;
  }

  
  @JsonProperty("importiertVon")
  @NotNull
  public String getImportiertVon() {
    return importiertVon;
  }

  @JsonProperty("importiertVon")
  public void setImportiertVon(String importiertVon) {
    this.importiertVon = importiertVon;
  }

  /**
   **/
  public DemoDataListDto letzteAktivitaet(java.time.LocalDateTime letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
    return this;
  }

  
  @JsonProperty("letzteAktivitaet")
  @NotNull
  public java.time.LocalDateTime getLetzteAktivitaet() {
    return letzteAktivitaet;
  }

  @JsonProperty("letzteAktivitaet")
  public void setLetzteAktivitaet(java.time.LocalDateTime letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
  }

  /**
   **/
  public DemoDataListDto kommentar(String kommentar) {
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
  public DemoDataListDto documentId(String documentId) {
    this.documentId = documentId;
    return this;
  }

  
  @JsonProperty("documentId")
  @NotNull
  public String getDocumentId() {
    return documentId;
  }

  @JsonProperty("documentId")
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  /**
   **/
  public DemoDataListDto filename(String filename) {
    this.filename = filename;
    return this;
  }

  
  @JsonProperty("filename")
  @NotNull
  public String getFilename() {
    return filename;
  }

  @JsonProperty("filename")
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   **/
  public DemoDataListDto filepath(String filepath) {
    this.filepath = filepath;
    return this;
  }

  
  @JsonProperty("filepath")
  @NotNull
  public String getFilepath() {
    return filepath;
  }

  @JsonProperty("filepath")
  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  /**
   **/
  public DemoDataListDto filesize(Integer filesize) {
    this.filesize = filesize;
    return this;
  }

  
  @JsonProperty("filesize")
  @NotNull
  public Integer getFilesize() {
    return filesize;
  }

  @JsonProperty("filesize")
  public void setFilesize(Integer filesize) {
    this.filesize = filesize;
  }

  /**
   **/
  public DemoDataListDto demoDatas(List<DemoDataSlimDto> demoDatas) {
    this.demoDatas = demoDatas;
    return this;
  }

  
  @JsonProperty("demoDatas")
  @NotNull
  public List<DemoDataSlimDto> getDemoDatas() {
    return demoDatas;
  }

  @JsonProperty("demoDatas")
  public void setDemoDatas(List<DemoDataSlimDto> demoDatas) {
    this.demoDatas = demoDatas;
  }

  public DemoDataListDto addDemoDatasItem(DemoDataSlimDto demoDatasItem) {
    if (this.demoDatas == null) {
      this.demoDatas = new ArrayList<>();
    }

    this.demoDatas.add(demoDatasItem);
    return this;
  }

  public DemoDataListDto removeDemoDatasItem(DemoDataSlimDto demoDatasItem) {
    if (demoDatasItem != null && this.demoDatas != null) {
      this.demoDatas.remove(demoDatasItem);
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
    DemoDataListDto demoDataList = (DemoDataListDto) o;
    return Objects.equals(this.importiertVon, demoDataList.importiertVon) &&
        Objects.equals(this.letzteAktivitaet, demoDataList.letzteAktivitaet) &&
        Objects.equals(this.kommentar, demoDataList.kommentar) &&
        Objects.equals(this.documentId, demoDataList.documentId) &&
        Objects.equals(this.filename, demoDataList.filename) &&
        Objects.equals(this.filepath, demoDataList.filepath) &&
        Objects.equals(this.filesize, demoDataList.filesize) &&
        Objects.equals(this.demoDatas, demoDataList.demoDatas);
  }

  @Override
  public int hashCode() {
    return Objects.hash(importiertVon, letzteAktivitaet, kommentar, documentId, filename, filepath, filesize, demoDatas);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataListDto {\n");
    
    sb.append("    importiertVon: ").append(toIndentedString(importiertVon)).append("\n");
    sb.append("    letzteAktivitaet: ").append(toIndentedString(letzteAktivitaet)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    documentId: ").append(toIndentedString(documentId)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    filepath: ").append(toIndentedString(filepath)).append("\n");
    sb.append("    filesize: ").append(toIndentedString(filesize)).append("\n");
    sb.append("    demoDatas: ").append(toIndentedString(demoDatas)).append("\n");
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

