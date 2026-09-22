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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SbDashboardGesuchDto  implements Serializable {
  private UUID id;
  private UUID gesuchTrancheId;
  private String fallNummer;
  private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ;
  private String piaNachname;
  private String piaVorname;
  private LocalDate piaGeburtsdatum;
  private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus trancheStatus;
  private String bearbeiter;
  private LocalDate letzteAktivitaet;

  protected SbDashboardGesuchDto(SbDashboardGesuchDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.gesuchTrancheId = b.gesuchTrancheId;
    this.fallNummer = b.fallNummer;
    this.typ = b.typ;
    this.piaNachname = b.piaNachname;
    this.piaVorname = b.piaVorname;
    this.piaGeburtsdatum = b.piaGeburtsdatum;
    this.gesuchStatus = b.gesuchStatus;
    this.trancheStatus = b.trancheStatus;
    this.bearbeiter = b.bearbeiter;
    this.letzteAktivitaet = b.letzteAktivitaet;
  }

  public SbDashboardGesuchDto() {
  }

  /**
   **/
  public SbDashboardGesuchDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty(required = true, value = "id")
  @NotNull public UUID getId() {
    return id;
  }

  @JsonProperty(required = true, value = "id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public SbDashboardGesuchDto gesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchTrancheId")
  @NotNull public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }

  @JsonProperty(required = true, value = "gesuchTrancheId")
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }

  /**
   **/
  public SbDashboardGesuchDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty(required = true, value = "fallNummer")
  @NotNull public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty(required = true, value = "fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public SbDashboardGesuchDto typ(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty(required = true, value = "typ")
  @NotNull public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp getTyp() {
    return typ;
  }

  @JsonProperty(required = true, value = "typ")
  public void setTyp(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
    this.typ = typ;
  }

  /**
   **/
  public SbDashboardGesuchDto piaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
    return this;
  }

  
  @JsonProperty(required = true, value = "piaNachname")
  @NotNull public String getPiaNachname() {
    return piaNachname;
  }

  @JsonProperty(required = true, value = "piaNachname")
  public void setPiaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
  }

  /**
   **/
  public SbDashboardGesuchDto piaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
    return this;
  }

  
  @JsonProperty(required = true, value = "piaVorname")
  @NotNull public String getPiaVorname() {
    return piaVorname;
  }

  @JsonProperty(required = true, value = "piaVorname")
  public void setPiaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
  }

  /**
   **/
  public SbDashboardGesuchDto piaGeburtsdatum(LocalDate piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
    return this;
  }

  
  @JsonProperty(required = true, value = "piaGeburtsdatum")
  @NotNull public LocalDate getPiaGeburtsdatum() {
    return piaGeburtsdatum;
  }

  @JsonProperty(required = true, value = "piaGeburtsdatum")
  public void setPiaGeburtsdatum(LocalDate piaGeburtsdatum) {
    this.piaGeburtsdatum = piaGeburtsdatum;
  }

  /**
   **/
  public SbDashboardGesuchDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchStatus")
  @NotNull public ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty(required = true, value = "gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public SbDashboardGesuchDto trancheStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus trancheStatus) {
    this.trancheStatus = trancheStatus;
    return this;
  }

  
  @JsonProperty(required = true, value = "trancheStatus")
  @NotNull public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus getTrancheStatus() {
    return trancheStatus;
  }

  @JsonProperty(required = true, value = "trancheStatus")
  public void setTrancheStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus trancheStatus) {
    this.trancheStatus = trancheStatus;
  }

  /**
   **/
  public SbDashboardGesuchDto bearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
    return this;
  }

  
  @JsonProperty(required = true, value = "bearbeiter")
  @NotNull public String getBearbeiter() {
    return bearbeiter;
  }

  @JsonProperty(required = true, value = "bearbeiter")
  public void setBearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
  }

  /**
   **/
  public SbDashboardGesuchDto letzteAktivitaet(LocalDate letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
    return this;
  }

  
  @JsonProperty(required = true, value = "letzteAktivitaet")
  @NotNull public LocalDate getLetzteAktivitaet() {
    return letzteAktivitaet;
  }

  @JsonProperty(required = true, value = "letzteAktivitaet")
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
        Objects.equals(this.gesuchStatus, sbDashboardGesuch.gesuchStatus) &&
        Objects.equals(this.trancheStatus, sbDashboardGesuch.trancheStatus) &&
        Objects.equals(this.bearbeiter, sbDashboardGesuch.bearbeiter) &&
        Objects.equals(this.letzteAktivitaet, sbDashboardGesuch.letzteAktivitaet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gesuchTrancheId, fallNummer, typ, piaNachname, piaVorname, piaGeburtsdatum, gesuchStatus, trancheStatus, bearbeiter, letzteAktivitaet);
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
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    trancheStatus: ").append(toIndentedString(trancheStatus)).append("\n");
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
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static SbDashboardGesuchDtoBuilder<?, ?> builder() {
    return new SbDashboardGesuchDtoBuilderImpl();
  }

  private static final class SbDashboardGesuchDtoBuilderImpl extends SbDashboardGesuchDtoBuilder<SbDashboardGesuchDto, SbDashboardGesuchDtoBuilderImpl> {

    @Override
    protected SbDashboardGesuchDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SbDashboardGesuchDto build() {
      return new SbDashboardGesuchDto(this);
    }
  }

  public static abstract class SbDashboardGesuchDtoBuilder<C extends SbDashboardGesuchDto, B extends SbDashboardGesuchDtoBuilder<C, B>>  {
    private UUID id;
    private UUID gesuchTrancheId;
    private String fallNummer;
    private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ;
    private String piaNachname;
    private String piaVorname;
    private LocalDate piaGeburtsdatum;
    private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
    private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus trancheStatus;
    private String bearbeiter;
    private LocalDate letzteAktivitaet;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B gesuchTrancheId(UUID gesuchTrancheId) {
      this.gesuchTrancheId = gesuchTrancheId;
      return self();
    }
    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B typ(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
      this.typ = typ;
      return self();
    }
    public B piaNachname(String piaNachname) {
      this.piaNachname = piaNachname;
      return self();
    }
    public B piaVorname(String piaVorname) {
      this.piaVorname = piaVorname;
      return self();
    }
    public B piaGeburtsdatum(LocalDate piaGeburtsdatum) {
      this.piaGeburtsdatum = piaGeburtsdatum;
      return self();
    }
    public B gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
      this.gesuchStatus = gesuchStatus;
      return self();
    }
    public B trancheStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus trancheStatus) {
      this.trancheStatus = trancheStatus;
      return self();
    }
    public B bearbeiter(String bearbeiter) {
      this.bearbeiter = bearbeiter;
      return self();
    }
    public B letzteAktivitaet(LocalDate letzteAktivitaet) {
      this.letzteAktivitaet = letzteAktivitaet;
      return self();
    }
  }
}
