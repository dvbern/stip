package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("Gesuchsperiode")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid GueltigkeitStatusDto gueltigkeitStatus;
  private @Valid LocalDate gesuchsperiodeStart;
  private @Valid LocalDate gesuchsperiodeStopp;
  private @Valid LocalDate aufschaltterminStart;
  private @Valid LocalDate aufschaltterminStopp;
  private @Valid LocalDate einreichefristNormal;
  private @Valid LocalDate einreichefristReduziert;
  private @Valid GesuchsjahrDto gesuchsjahr;
  private @Valid Integer ausbKostenSekII;
  private @Valid Integer ausbKostenTertiaer;

  /**
   **/
  public GesuchsperiodeDto id(UUID id) {
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
  public GesuchsperiodeDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }


  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public GesuchsperiodeDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }


  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public GesuchsperiodeDto gueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
    return this;
  }


  @JsonProperty("gueltigkeitStatus")
  @NotNull
  public GueltigkeitStatusDto getGueltigkeitStatus() {
    return gueltigkeitStatus;
  }

  @JsonProperty("gueltigkeitStatus")
  public void setGueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
  }

  /**
   **/
  public GesuchsperiodeDto gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
    this.gesuchsperiodeStart = gesuchsperiodeStart;
    return this;
  }


  @JsonProperty("gesuchsperiodeStart")
  @NotNull
  public LocalDate getGesuchsperiodeStart() {
    return gesuchsperiodeStart;
  }

  @JsonProperty("gesuchsperiodeStart")
  public void setGesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
    this.gesuchsperiodeStart = gesuchsperiodeStart;
  }

  /**
   **/
  public GesuchsperiodeDto gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
    this.gesuchsperiodeStopp = gesuchsperiodeStopp;
    return this;
  }


  @JsonProperty("gesuchsperiodeStopp")
  @NotNull
  public LocalDate getGesuchsperiodeStopp() {
    return gesuchsperiodeStopp;
  }

  @JsonProperty("gesuchsperiodeStopp")
  public void setGesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
    this.gesuchsperiodeStopp = gesuchsperiodeStopp;
  }

  /**
   **/
  public GesuchsperiodeDto aufschaltterminStart(LocalDate aufschaltterminStart) {
    this.aufschaltterminStart = aufschaltterminStart;
    return this;
  }


  @JsonProperty("aufschaltterminStart")
  @NotNull
  public LocalDate getAufschaltterminStart() {
    return aufschaltterminStart;
  }

  @JsonProperty("aufschaltterminStart")
  public void setAufschaltterminStart(LocalDate aufschaltterminStart) {
    this.aufschaltterminStart = aufschaltterminStart;
  }

  /**
   **/
  public GesuchsperiodeDto aufschaltterminStopp(LocalDate aufschaltterminStopp) {
    this.aufschaltterminStopp = aufschaltterminStopp;
    return this;
  }


  @JsonProperty("aufschaltterminStopp")
  @NotNull
  public LocalDate getAufschaltterminStopp() {
    return aufschaltterminStopp;
  }

  @JsonProperty("aufschaltterminStopp")
  public void setAufschaltterminStopp(LocalDate aufschaltterminStopp) {
    this.aufschaltterminStopp = aufschaltterminStopp;
  }

  /**
   **/
  public GesuchsperiodeDto einreichefristNormal(LocalDate einreichefristNormal) {
    this.einreichefristNormal = einreichefristNormal;
    return this;
  }


  @JsonProperty("einreichefristNormal")
  @NotNull
  public LocalDate getEinreichefristNormal() {
    return einreichefristNormal;
  }

  @JsonProperty("einreichefristNormal")
  public void setEinreichefristNormal(LocalDate einreichefristNormal) {
    this.einreichefristNormal = einreichefristNormal;
  }

  /**
   **/
  public GesuchsperiodeDto einreichefristReduziert(LocalDate einreichefristReduziert) {
    this.einreichefristReduziert = einreichefristReduziert;
    return this;
  }


  @JsonProperty("einreichefristReduziert")
  @NotNull
  public LocalDate getEinreichefristReduziert() {
    return einreichefristReduziert;
  }

  @JsonProperty("einreichefristReduziert")
  public void setEinreichefristReduziert(LocalDate einreichefristReduziert) {
    this.einreichefristReduziert = einreichefristReduziert;
  }

  /**
   **/
  public GesuchsperiodeDto gesuchsjahr(GesuchsjahrDto gesuchsjahr) {
    this.gesuchsjahr = gesuchsjahr;
    return this;
  }


  @JsonProperty("gesuchsjahr")
  @NotNull
  public GesuchsjahrDto getGesuchsjahr() {
    return gesuchsjahr;
  }

  @JsonProperty("gesuchsjahr")
  public void setGesuchsjahr(GesuchsjahrDto gesuchsjahr) {
    this.gesuchsjahr = gesuchsjahr;
  }

  /**
   **/
  public GesuchsperiodeDto ausbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
    return this;
  }


  @JsonProperty("ausbKosten_SekII")
  @NotNull
  public Integer getAusbKostenSekII() {
    return ausbKostenSekII;
  }

  @JsonProperty("ausbKosten_SekII")
  public void setAusbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
  }

  /**
   **/
  public GesuchsperiodeDto ausbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
    return this;
  }


  @JsonProperty("ausbKosten_Tertiaer")
  @NotNull
  public Integer getAusbKostenTertiaer() {
    return ausbKostenTertiaer;
  }

  @JsonProperty("ausbKosten_Tertiaer")
  public void setAusbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeDto gesuchsperiode = (GesuchsperiodeDto) o;
    return Objects.equals(this.id, gesuchsperiode.id) &&
        Objects.equals(this.bezeichnungDe, gesuchsperiode.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiode.bezeichnungFr) &&
        Objects.equals(this.gueltigkeitStatus, gesuchsperiode.gueltigkeitStatus) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiode.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiode.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiode.aufschaltterminStart) &&
        Objects.equals(this.aufschaltterminStopp, gesuchsperiode.aufschaltterminStopp) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiode.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiode.einreichefristReduziert) &&
        Objects.equals(this.gesuchsjahr, gesuchsperiode.gesuchsjahr) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiode.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiode.ausbKostenTertiaer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, gueltigkeitStatus, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, aufschaltterminStopp, einreichefristNormal, einreichefristReduziert, gesuchsjahr, ausbKostenSekII, ausbKostenTertiaer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    gueltigkeitStatus: ").append(toIndentedString(gueltigkeitStatus)).append("\n");
    sb.append("    gesuchsperiodeStart: ").append(toIndentedString(gesuchsperiodeStart)).append("\n");
    sb.append("    gesuchsperiodeStopp: ").append(toIndentedString(gesuchsperiodeStopp)).append("\n");
    sb.append("    aufschaltterminStart: ").append(toIndentedString(aufschaltterminStart)).append("\n");
    sb.append("    aufschaltterminStopp: ").append(toIndentedString(aufschaltterminStopp)).append("\n");
    sb.append("    einreichefristNormal: ").append(toIndentedString(einreichefristNormal)).append("\n");
    sb.append("    einreichefristReduziert: ").append(toIndentedString(einreichefristReduziert)).append("\n");
    sb.append("    gesuchsjahr: ").append(toIndentedString(gesuchsjahr)).append("\n");
    sb.append("    ausbKostenSekII: ").append(toIndentedString(ausbKostenSekII)).append("\n");
    sb.append("    ausbKostenTertiaer: ").append(toIndentedString(ausbKostenTertiaer)).append("\n");
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

