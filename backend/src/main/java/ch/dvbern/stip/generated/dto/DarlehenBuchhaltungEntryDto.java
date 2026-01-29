package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DarlehenBuchhaltungEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenBuchhaltungEntryDto  implements Serializable {
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie;
  private @Valid Integer betrag;
  private @Valid DokumentDto verfuegung;
  private @Valid String userErstellt;
  private @Valid String kommentar;

  /**
   **/
  public DarlehenBuchhaltungEntryDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto kategorie(ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie) {
    this.kategorie = kategorie;
    return this;
  }

  
  @JsonProperty("kategorie")
  @NotNull
  public ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie getKategorie() {
    return kategorie;
  }

  @JsonProperty("kategorie")
  public void setKategorie(ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie) {
    this.kategorie = kategorie;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto betrag(Integer betrag) {
    this.betrag = betrag;
    return this;
  }

  
  @JsonProperty("betrag")
  @NotNull
  public Integer getBetrag() {
    return betrag;
  }

  @JsonProperty("betrag")
  public void setBetrag(Integer betrag) {
    this.betrag = betrag;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto verfuegung(DokumentDto verfuegung) {
    this.verfuegung = verfuegung;
    return this;
  }

  
  @JsonProperty("verfuegung")
  public DokumentDto getVerfuegung() {
    return verfuegung;
  }

  @JsonProperty("verfuegung")
  public void setVerfuegung(DokumentDto verfuegung) {
    this.verfuegung = verfuegung;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto kommentar(String kommentar) {
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
    DarlehenBuchhaltungEntryDto darlehenBuchhaltungEntry = (DarlehenBuchhaltungEntryDto) o;
    return Objects.equals(this.timestampErstellt, darlehenBuchhaltungEntry.timestampErstellt) &&
        Objects.equals(this.kategorie, darlehenBuchhaltungEntry.kategorie) &&
        Objects.equals(this.betrag, darlehenBuchhaltungEntry.betrag) &&
        Objects.equals(this.verfuegung, darlehenBuchhaltungEntry.verfuegung) &&
        Objects.equals(this.userErstellt, darlehenBuchhaltungEntry.userErstellt) &&
        Objects.equals(this.kommentar, darlehenBuchhaltungEntry.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestampErstellt, kategorie, betrag, verfuegung, userErstellt, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenBuchhaltungEntryDto {\n");
    
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    kategorie: ").append(toIndentedString(kategorie)).append("\n");
    sb.append("    betrag: ").append(toIndentedString(betrag)).append("\n");
    sb.append("    verfuegung: ").append(toIndentedString(verfuegung)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
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


}

