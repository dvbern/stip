package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DarlehenGrundDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Darlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenDto  implements Serializable {
  private @Valid UUID id;
  private @Valid UUID fallId;
  private @Valid Boolean darlehenGewaehren;
  private @Valid Integer darlehenBetrag;
  private @Valid String kommentar;
  private @Valid Integer darlehenBetragGewuenscht;
  private @Valid Integer schulden;
  private @Valid Integer anzahlBetreibungen;
  private @Valid DarlehenGrundDto grund;

  /**
   **/
  public DarlehenDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public DarlehenDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public DarlehenDto darlehenGewaehren(Boolean darlehenGewaehren) {
    this.darlehenGewaehren = darlehenGewaehren;
    return this;
  }

  
  @JsonProperty("darlehenGewaehren")
  @NotNull
  public Boolean getDarlehenGewaehren() {
    return darlehenGewaehren;
  }

  @JsonProperty("darlehenGewaehren")
  public void setDarlehenGewaehren(Boolean darlehenGewaehren) {
    this.darlehenGewaehren = darlehenGewaehren;
  }

  /**
   * minimum: 0
   **/
  public DarlehenDto darlehenBetrag(Integer darlehenBetrag) {
    this.darlehenBetrag = darlehenBetrag;
    return this;
  }

  
  @JsonProperty("darlehenBetrag")
  @NotNull
 @Min(0)  public Integer getDarlehenBetrag() {
    return darlehenBetrag;
  }

  @JsonProperty("darlehenBetrag")
  public void setDarlehenBetrag(Integer darlehenBetrag) {
    this.darlehenBetrag = darlehenBetrag;
  }

  /**
   **/
  public DarlehenDto kommentar(String kommentar) {
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
   * minimum: 0
   **/
  public DarlehenDto darlehenBetragGewuenscht(Integer darlehenBetragGewuenscht) {
    this.darlehenBetragGewuenscht = darlehenBetragGewuenscht;
    return this;
  }

  
  @JsonProperty("darlehenBetragGewuenscht")
  @NotNull
 @Min(0)  public Integer getDarlehenBetragGewuenscht() {
    return darlehenBetragGewuenscht;
  }

  @JsonProperty("darlehenBetragGewuenscht")
  public void setDarlehenBetragGewuenscht(Integer darlehenBetragGewuenscht) {
    this.darlehenBetragGewuenscht = darlehenBetragGewuenscht;
  }

  /**
   * minimum: 0
   **/
  public DarlehenDto schulden(Integer schulden) {
    this.schulden = schulden;
    return this;
  }

  
  @JsonProperty("schulden")
  @NotNull
 @Min(0)  public Integer getSchulden() {
    return schulden;
  }

  @JsonProperty("schulden")
  public void setSchulden(Integer schulden) {
    this.schulden = schulden;
  }

  /**
   * minimum: 0
   **/
  public DarlehenDto anzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
    return this;
  }

  
  @JsonProperty("anzahlBetreibungen")
  @NotNull
 @Min(0)  public Integer getAnzahlBetreibungen() {
    return anzahlBetreibungen;
  }

  @JsonProperty("anzahlBetreibungen")
  public void setAnzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
  }

  /**
   **/
  public DarlehenDto grund(DarlehenGrundDto grund) {
    this.grund = grund;
    return this;
  }

  
  @JsonProperty("grund")
  @NotNull
  public DarlehenGrundDto getGrund() {
    return grund;
  }

  @JsonProperty("grund")
  public void setGrund(DarlehenGrundDto grund) {
    this.grund = grund;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DarlehenDto darlehen = (DarlehenDto) o;
    return Objects.equals(this.id, darlehen.id) &&
        Objects.equals(this.fallId, darlehen.fallId) &&
        Objects.equals(this.darlehenGewaehren, darlehen.darlehenGewaehren) &&
        Objects.equals(this.darlehenBetrag, darlehen.darlehenBetrag) &&
        Objects.equals(this.kommentar, darlehen.kommentar) &&
        Objects.equals(this.darlehenBetragGewuenscht, darlehen.darlehenBetragGewuenscht) &&
        Objects.equals(this.schulden, darlehen.schulden) &&
        Objects.equals(this.anzahlBetreibungen, darlehen.anzahlBetreibungen) &&
        Objects.equals(this.grund, darlehen.grund);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallId, darlehenGewaehren, darlehenBetrag, kommentar, darlehenBetragGewuenscht, schulden, anzahlBetreibungen, grund);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    darlehenGewaehren: ").append(toIndentedString(darlehenGewaehren)).append("\n");
    sb.append("    darlehenBetrag: ").append(toIndentedString(darlehenBetrag)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    darlehenBetragGewuenscht: ").append(toIndentedString(darlehenBetragGewuenscht)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    grund: ").append(toIndentedString(grund)).append("\n");
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

