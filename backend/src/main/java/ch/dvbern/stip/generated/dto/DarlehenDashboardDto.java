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



@JsonTypeName("DarlehenDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenDashboardDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String fallNummer;
  private @Valid String piaVorname;
  private @Valid String piaNachname;
  private @Valid String piaGeburtsdatum;
  private @Valid ch.dvbern.stip.api.darlehen.type.DarlehenStatus status;
  private @Valid String bearbeiter;
  private @Valid LocalDate letzteAktivitaet;

  /**
   **/
  public DarlehenDashboardDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public DarlehenDashboardDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty("fallNummer")
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public DarlehenDashboardDto piaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
    return this;
  }

  
  @JsonProperty("piaVorname")
  public String getPiaVorname() {
    return piaVorname;
  }

  @JsonProperty("piaVorname")
  public void setPiaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
  }

  /**
   **/
  public DarlehenDashboardDto piaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
    return this;
  }

  
  @JsonProperty("piaNachname")
  public String getPiaNachname() {
    return piaNachname;
  }

  @JsonProperty("piaNachname")
  public void setPiaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
  }

  /**
   **/
  public DarlehenDashboardDto piaGeburtsdatum(String piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
    return this;
  }

  
  @JsonProperty("piaGeburtsdatum")
  public String getPiaGeburtsdatum() {
    return piaGeburtsdatum;
  }

  @JsonProperty("piaGeburtsdatum")
  public void setPiaGeburtsdatum(String piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
  }

  /**
   **/
  public DarlehenDashboardDto status(ch.dvbern.stip.api.darlehen.type.DarlehenStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public ch.dvbern.stip.api.darlehen.type.DarlehenStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.darlehen.type.DarlehenStatus status) {
    this.status = status;
  }

  /**
   **/
  public DarlehenDashboardDto bearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
    return this;
  }

  
  @JsonProperty("bearbeiter")
  public String getBearbeiter() {
    return bearbeiter;
  }

  @JsonProperty("bearbeiter")
  public void setBearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
  }

  /**
   **/
  public DarlehenDashboardDto letzteAktivitaet(LocalDate letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
    return this;
  }

  
  @JsonProperty("letzteAktivitaet")
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
    DarlehenDashboardDto darlehenDashboard = (DarlehenDashboardDto) o;
    return Objects.equals(this.id, darlehenDashboard.id) &&
        Objects.equals(this.fallNummer, darlehenDashboard.fallNummer) &&
        Objects.equals(this.piaVorname, darlehenDashboard.piaVorname) &&
        Objects.equals(this.piaNachname, darlehenDashboard.piaNachname) &&
        Objects.equals(this.piaGeburtsdatum, darlehenDashboard.piaGeburtsdatum) &&
        Objects.equals(this.status, darlehenDashboard.status) &&
        Objects.equals(this.bearbeiter, darlehenDashboard.bearbeiter) &&
        Objects.equals(this.letzteAktivitaet, darlehenDashboard.letzteAktivitaet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallNummer, piaVorname, piaNachname, piaGeburtsdatum, status, bearbeiter, letzteAktivitaet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenDashboardDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    piaVorname: ").append(toIndentedString(piaVorname)).append("\n");
    sb.append("    piaNachname: ").append(toIndentedString(piaNachname)).append("\n");
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

