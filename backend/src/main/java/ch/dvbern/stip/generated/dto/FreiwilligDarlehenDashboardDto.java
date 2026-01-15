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



@JsonTypeName("FreiwilligDarlehenDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FreiwilligDarlehenDashboardDto  implements Serializable {
  private @Valid UUID id;
  private @Valid UUID gesuchId;
  private @Valid UUID gesuchTrancheId;
  private @Valid UUID fallId;
  private @Valid String fallNummer;
  private @Valid String piaVorname;
  private @Valid String piaNachname;
  private @Valid String piaGeburtsdatum;
  private @Valid ch.dvbern.stip.api.darlehen.type.DarlehenStatus status;
  private @Valid String bearbeiter;
  private @Valid LocalDate letzteAktivitaet;

  /**
   **/
  public FreiwilligDarlehenDashboardDto id(UUID id) {
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
  public FreiwilligDarlehenDashboardDto gesuchId(UUID gesuchId) {
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
  public FreiwilligDarlehenDashboardDto gesuchTrancheId(UUID gesuchTrancheId) {
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
  public FreiwilligDarlehenDashboardDto fallId(UUID fallId) {
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
  public FreiwilligDarlehenDashboardDto fallNummer(String fallNummer) {
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
  public FreiwilligDarlehenDashboardDto piaVorname(String piaVorname) {
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
  public FreiwilligDarlehenDashboardDto piaNachname(String piaNachname) {
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
  public FreiwilligDarlehenDashboardDto piaGeburtsdatum(String piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
    return this;
  }

  
  @JsonProperty("piaGeburtsdatum")
  @NotNull
  public String getPiaGeburtsdatum() {
    return piaGeburtsdatum;
  }

  @JsonProperty("piaGeburtsdatum")
  public void setPiaGeburtsdatum(String piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
  }

  /**
   **/
  public FreiwilligDarlehenDashboardDto status(ch.dvbern.stip.api.darlehen.type.DarlehenStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.darlehen.type.DarlehenStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.darlehen.type.DarlehenStatus status) {
    this.status = status;
  }

  /**
   **/
  public FreiwilligDarlehenDashboardDto bearbeiter(String bearbeiter) {
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
  public FreiwilligDarlehenDashboardDto letzteAktivitaet(LocalDate letzteAktivitaet) {
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
    FreiwilligDarlehenDashboardDto freiwilligDarlehenDashboard = (FreiwilligDarlehenDashboardDto) o;
    return Objects.equals(this.id, freiwilligDarlehenDashboard.id) &&
        Objects.equals(this.gesuchId, freiwilligDarlehenDashboard.gesuchId) &&
        Objects.equals(this.gesuchTrancheId, freiwilligDarlehenDashboard.gesuchTrancheId) &&
        Objects.equals(this.fallId, freiwilligDarlehenDashboard.fallId) &&
        Objects.equals(this.fallNummer, freiwilligDarlehenDashboard.fallNummer) &&
        Objects.equals(this.piaVorname, freiwilligDarlehenDashboard.piaVorname) &&
        Objects.equals(this.piaNachname, freiwilligDarlehenDashboard.piaNachname) &&
        Objects.equals(this.piaGeburtsdatum, freiwilligDarlehenDashboard.piaGeburtsdatum) &&
        Objects.equals(this.status, freiwilligDarlehenDashboard.status) &&
        Objects.equals(this.bearbeiter, freiwilligDarlehenDashboard.bearbeiter) &&
        Objects.equals(this.letzteAktivitaet, freiwilligDarlehenDashboard.letzteAktivitaet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gesuchId, gesuchTrancheId, fallId, fallNummer, piaVorname, piaNachname, piaGeburtsdatum, status, bearbeiter, letzteAktivitaet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FreiwilligDarlehenDashboardDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
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

