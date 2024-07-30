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
  private @Valid String kommentar;
  private @Valid String benutzer;
  private @Valid LocalDate datum;

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
  public GesuchDokumentKommentarDto kommentar(String kommentar) {
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
   * Der Benutzer welcher das Dokument abgelehnt hat
   **/
  public GesuchDokumentKommentarDto benutzer(String benutzer) {
    this.benutzer = benutzer;
    return this;
  }


  @JsonProperty("benutzer")
  @NotNull
  public String getBenutzer() {
    return benutzer;
  }

  @JsonProperty("benutzer")
  public void setBenutzer(String benutzer) {
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
        Objects.equals(this.kommentar, gesuchDokumentKommentar.kommentar) &&
        Objects.equals(this.benutzer, gesuchDokumentKommentar.benutzer) &&
        Objects.equals(this.datum, gesuchDokumentKommentar.datum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchDokumentId, kommentar, benutzer, datum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentKommentarDto {\n");

    sb.append("    gesuchDokumentId: ").append(toIndentedString(gesuchDokumentId)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    benutzer: ").append(toIndentedString(benutzer)).append("\n");
    sb.append("    datum: ").append(toIndentedString(datum)).append("\n");
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

