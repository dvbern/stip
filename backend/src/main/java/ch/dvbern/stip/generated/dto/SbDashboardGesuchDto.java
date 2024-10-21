package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SbDashboardGesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SbDashboardGesuchDto  implements Serializable {
  private @Valid UUID id;
  private @Valid UUID gesuchTrancheId;
  private @Valid String fallNummer;
  private @Valid ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp typ;
  private @Valid String piaNachname;
  private @Valid String piaVorname;
  private @Valid LocalDate piaGeburtsdatum;
  private @Valid ch.dvbern.stip.api.gesuch.type.Gesuchstatus status;
  private @Valid String bearbeiter;
  private @Valid LocalDate letzteAktivitaet;

  /**
   **/
  public SbDashboardGesuchDto id(UUID id) {
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
  public SbDashboardGesuchDto gesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

  
  @JsonProperty("gesuchTrancheId")
  @NotNull
  public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }

  @JsonProperty("gesuchTrancheId")
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }

  /**
   **/
  public SbDashboardGesuchDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty("fallNummer")
  @NotNull
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public SbDashboardGesuchDto typ(ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  @NotNull
  public ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp typ) {
    this.typ = typ;
  }

  /**
   **/
  public SbDashboardGesuchDto piaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
    return this;
  }

  
  @JsonProperty("piaNachname")
  @NotNull
  public String getPiaNachname() {
    return piaNachname;
  }

  @JsonProperty("piaNachname")
  public void setPiaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
  }

  /**
   **/
  public SbDashboardGesuchDto piaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
    return this;
  }

  
  @JsonProperty("piaVorname")
  @NotNull
  public String getPiaVorname() {
    return piaVorname;
  }

  @JsonProperty("piaVorname")
  public void setPiaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
  }

  /**
   **/
  public SbDashboardGesuchDto piaGeburtsdatum(LocalDate piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
    return this;
  }

  
  @JsonProperty("piaGeburtsdatum")
  @NotNull
  public LocalDate getPiaGeburtsdatum() {
    return piaGeburtsdatum;
  }

  @JsonProperty("piaGeburtsdatum")
  public void setPiaGeburtsdatum(LocalDate piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
  }

  /**
   **/
  public SbDashboardGesuchDto status(ch.dvbern.stip.api.gesuch.type.Gesuchstatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.gesuch.type.Gesuchstatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.gesuch.type.Gesuchstatus status) {
    this.status = status;
  }

  /**
   **/
  public SbDashboardGesuchDto bearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
    return this;
  }

  
  @JsonProperty("bearbeiter")
  @NotNull
  public String getBearbeiter() {
    return bearbeiter;
  }

  @JsonProperty("bearbeiter")
  public void setBearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
  }

  /**
   **/
  public SbDashboardGesuchDto letzteAktivitaet(LocalDate letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
    return this;
  }

  
  @JsonProperty("letzteAktivitaet")
  @NotNull
  public LocalDate getLetzteAktivitaet() {
    return letzteAktivitaet;
  }

  @JsonProperty("letzteAktivitaet")
  public void setLetzteAktivitaet(LocalDate letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SbDashboardGesuchDto sbDashboardGesuch = (SbDashboardGesuchDto) o;
    return Objects.equals(this.id, sbDashboardGesuch.id) &&
        Objects.equals(this.gesuchTrancheId, sbDashboardGesuch.gesuchTrancheId) &&
        Objects.equals(this.fallNummer, sbDashboardGesuch.fallNummer) &&
        Objects.equals(this.typ, sbDashboardGesuch.typ) &&
        Objects.equals(this.piaNachname, sbDashboardGesuch.piaNachname) &&
        Objects.equals(this.piaVorname, sbDashboardGesuch.piaVorname) &&
        Objects.equals(this.piaGeburtsdatum, sbDashboardGesuch.piaGeburtsdatum) &&
        Objects.equals(this.status, sbDashboardGesuch.status) &&
        Objects.equals(this.bearbeiter, sbDashboardGesuch.bearbeiter) &&
        Objects.equals(this.letzteAktivitaet, sbDashboardGesuch.letzteAktivitaet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gesuchTrancheId, fallNummer, typ, piaNachname, piaVorname, piaGeburtsdatum, status, bearbeiter, letzteAktivitaet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SbDashboardGesuchDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    piaNachname: ").append(toIndentedString(piaNachname)).append("\n");
    sb.append("    piaVorname: ").append(toIndentedString(piaVorname)).append("\n");
    sb.append("    piaGeburtsdatum: ").append(toIndentedString(piaGeburtsdatum)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
    sb.append("    letzteAktivitaet: ").append(toIndentedString(letzteAktivitaet)).append("\n");
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

