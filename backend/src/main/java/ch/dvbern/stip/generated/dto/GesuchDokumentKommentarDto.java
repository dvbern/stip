package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Kommentar zu einem (abgelehnten) GesuchDokument
 **/

@JsonTypeName("GesuchDokumentKommentar")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDokumentKommentarDto  implements Serializable {
  private @Valid UUID gesuchId;
  private @Valid ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
  private @Valid ch.dvbern.stip.api.dokument.type.Dokumentstatus dokumentStatus;
  private @Valid String kommentar;
  private @Valid String userErstellt;
  private @Valid LocalDate timestampErstellt;

  /**
   **/
  public GesuchDokumentKommentarDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }


  @JsonProperty("gesuchId")
  @NotNull
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public GesuchDokumentKommentarDto dokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
    return this;
  }


  @JsonProperty("dokumentTyp")
  @NotNull
  public ch.dvbern.stip.api.dokument.type.DokumentTyp getDokumentTyp() {
    return dokumentTyp;
  }

  @JsonProperty("dokumentTyp")
  public void setDokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
  }

  /**
   **/
  public GesuchDokumentKommentarDto dokumentStatus(ch.dvbern.stip.api.dokument.type.Dokumentstatus dokumentStatus) {
    this.dokumentStatus = dokumentStatus;
    return this;
  }


  @JsonProperty("dokumentStatus")
  public ch.dvbern.stip.api.dokument.type.Dokumentstatus getDokumentStatus() {
    return dokumentStatus;
  }

  @JsonProperty("dokumentStatus")
  public void setDokumentStatus(ch.dvbern.stip.api.dokument.type.Dokumentstatus dokumentStatus) {
    this.dokumentStatus = dokumentStatus;
  }

  /**
   **/
  public GesuchDokumentKommentarDto kommentar(String kommentar) {
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

  /**
   **/
  public GesuchDokumentKommentarDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }


  @JsonProperty("user_erstellt")
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("user_erstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public GesuchDokumentKommentarDto timestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }


  @JsonProperty("timestampErstellt")
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDokumentKommentarDto gesuchDokumentKommentar = (GesuchDokumentKommentarDto) o;
    return Objects.equals(this.gesuchId, gesuchDokumentKommentar.gesuchId) &&
        Objects.equals(this.dokumentTyp, gesuchDokumentKommentar.dokumentTyp) &&
        Objects.equals(this.dokumentStatus, gesuchDokumentKommentar.dokumentStatus) &&
        Objects.equals(this.kommentar, gesuchDokumentKommentar.kommentar) &&
        Objects.equals(this.userErstellt, gesuchDokumentKommentar.userErstellt) &&
        Objects.equals(this.timestampErstellt, gesuchDokumentKommentar.timestampErstellt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchId, dokumentTyp, dokumentStatus, kommentar, userErstellt, timestampErstellt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentKommentarDto {\n");

    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    dokumentTyp: ").append(toIndentedString(dokumentTyp)).append("\n");
    sb.append("    dokumentStatus: ").append(toIndentedString(dokumentStatus)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
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

