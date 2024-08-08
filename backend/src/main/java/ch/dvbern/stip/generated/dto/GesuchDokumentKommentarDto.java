package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Kommentar zu einem (abgelehnten) GesuchDokument
 **/

@JsonTypeName("GesuchDokumentKommentar")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDokumentKommentarDto  implements Serializable {
  private @Valid UUID gesuchDokumentId;
  private @Valid UUID gesuchId;
  private @Valid ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
  private @Valid BenutzerDto benutzer;
  private @Valid LocalDate datum;
  private @Valid ch.dvbern.stip.api.dokument.type.Dokumentstatus dokumentStatus;
  private @Valid String kommentar;

  /**
   **/
  public GesuchDokumentKommentarDto gesuchDokumentId(UUID gesuchDokumentId) {
    this.gesuchDokumentId = gesuchDokumentId;
    return this;
  }


  @JsonProperty("gesuchDokumentId")
  @NotNull
  public UUID getGesuchDokumentId() {
    return gesuchDokumentId;
  }

  @JsonProperty("gesuchDokumentId")
  public void setGesuchDokumentId(UUID gesuchDokumentId) {
    this.gesuchDokumentId = gesuchDokumentId;
  }

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
  public GesuchDokumentKommentarDto benutzer(BenutzerDto benutzer) {
    this.benutzer = benutzer;
    return this;
  }


  @JsonProperty("benutzer")
  @NotNull
  public BenutzerDto getBenutzer() {
    return benutzer;
  }

  @JsonProperty("benutzer")
  public void setBenutzer(BenutzerDto benutzer) {
    this.benutzer = benutzer;
  }

  /**
   **/
  public GesuchDokumentKommentarDto datum(LocalDate datum) {
    this.datum = datum;
    return this;
  }


  @JsonProperty("datum")
  @NotNull
  public LocalDate getDatum() {
    return datum;
  }

  @JsonProperty("datum")
  public void setDatum(LocalDate datum) {
    this.datum = datum;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDokumentKommentarDto gesuchDokumentKommentar = (GesuchDokumentKommentarDto) o;
    return Objects.equals(this.gesuchDokumentId, gesuchDokumentKommentar.gesuchDokumentId) &&
        Objects.equals(this.gesuchId, gesuchDokumentKommentar.gesuchId) &&
        Objects.equals(this.dokumentTyp, gesuchDokumentKommentar.dokumentTyp) &&
        Objects.equals(this.benutzer, gesuchDokumentKommentar.benutzer) &&
        Objects.equals(this.datum, gesuchDokumentKommentar.datum) &&
        Objects.equals(this.dokumentStatus, gesuchDokumentKommentar.dokumentStatus) &&
        Objects.equals(this.kommentar, gesuchDokumentKommentar.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchDokumentId, gesuchId, dokumentTyp, benutzer, datum, dokumentStatus, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentKommentarDto {\n");

    sb.append("    gesuchDokumentId: ").append(toIndentedString(gesuchDokumentId)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    dokumentTyp: ").append(toIndentedString(dokumentTyp)).append("\n");
    sb.append("    benutzer: ").append(toIndentedString(benutzer)).append("\n");
    sb.append("    datum: ").append(toIndentedString(datum)).append("\n");
    sb.append("    dokumentStatus: ").append(toIndentedString(dokumentStatus)).append("\n");
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

