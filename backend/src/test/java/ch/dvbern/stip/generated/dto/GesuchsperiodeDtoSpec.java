/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import ch.dvbern.stip.generated.dto.GesuchsjahrDtoSpec;
import ch.dvbern.stip.generated.dto.GueltigkeitStatusDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * GesuchsperiodeDtoSpec
 */
@JsonPropertyOrder({
  GesuchsperiodeDtoSpec.JSON_PROPERTY_ID,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_BEZEICHNUNG_DE,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_BEZEICHNUNG_FR,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_GUELTIGKEIT_STATUS,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_GESUCHSPERIODE_START,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_GESUCHSPERIODE_STOPP,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_AUFSCHALTTERMIN_START,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_EINREICHEFRIST_NORMAL,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_EINREICHEFRIST_REDUZIERT,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_GESUCHSJAHR,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_AUSB_KOSTEN_SEK_I_I,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_AUSB_KOSTEN_TERTIAER,
  GesuchsperiodeDtoSpec.JSON_PROPERTY_FRIST_NACHREICHEN_DOKUMENTE
})
@JsonTypeName("Gesuchsperiode")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class GesuchsperiodeDtoSpec {
  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_BEZEICHNUNG_DE = "bezeichnungDe";
  private String bezeichnungDe;

  public static final String JSON_PROPERTY_BEZEICHNUNG_FR = "bezeichnungFr";
  private String bezeichnungFr;

  public static final String JSON_PROPERTY_GUELTIGKEIT_STATUS = "gueltigkeitStatus";
  private GueltigkeitStatusDtoSpec gueltigkeitStatus;

  public static final String JSON_PROPERTY_GESUCHSPERIODE_START = "gesuchsperiodeStart";
  private LocalDate gesuchsperiodeStart;

  public static final String JSON_PROPERTY_GESUCHSPERIODE_STOPP = "gesuchsperiodeStopp";
  private LocalDate gesuchsperiodeStopp;

  public static final String JSON_PROPERTY_AUFSCHALTTERMIN_START = "aufschaltterminStart";
  private LocalDate aufschaltterminStart;

  public static final String JSON_PROPERTY_EINREICHEFRIST_NORMAL = "einreichefristNormal";
  private LocalDate einreichefristNormal;

  public static final String JSON_PROPERTY_EINREICHEFRIST_REDUZIERT = "einreichefristReduziert";
  private LocalDate einreichefristReduziert;

  public static final String JSON_PROPERTY_GESUCHSJAHR = "gesuchsjahr";
  private GesuchsjahrDtoSpec gesuchsjahr;

  public static final String JSON_PROPERTY_AUSB_KOSTEN_SEK_I_I = "ausbKosten_SekII";
  private Integer ausbKostenSekII;

  public static final String JSON_PROPERTY_AUSB_KOSTEN_TERTIAER = "ausbKosten_Tertiaer";
  private Integer ausbKostenTertiaer;

  public static final String JSON_PROPERTY_FRIST_NACHREICHEN_DOKUMENTE = "fristNachreichenDokumente";
  private Integer fristNachreichenDokumente;

  public GesuchsperiodeDtoSpec() {
  }

  public GesuchsperiodeDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setId(UUID id) {
    this.id = id;
  }


  public GesuchsperiodeDtoSpec bezeichnungDe(String bezeichnungDe) {
    
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

   /**
   * Get bezeichnungDe
   * @return bezeichnungDe
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_DE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getBezeichnungDe() {
    return bezeichnungDe;
  }


  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_DE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }


  public GesuchsperiodeDtoSpec bezeichnungFr(String bezeichnungFr) {
    
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

   /**
   * Get bezeichnungFr
   * @return bezeichnungFr
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_FR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getBezeichnungFr() {
    return bezeichnungFr;
  }


  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_FR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }


  public GesuchsperiodeDtoSpec gueltigkeitStatus(GueltigkeitStatusDtoSpec gueltigkeitStatus) {
    
    this.gueltigkeitStatus = gueltigkeitStatus;
    return this;
  }

   /**
   * Get gueltigkeitStatus
   * @return gueltigkeitStatus
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GUELTIGKEIT_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GueltigkeitStatusDtoSpec getGueltigkeitStatus() {
    return gueltigkeitStatus;
  }


  @JsonProperty(JSON_PROPERTY_GUELTIGKEIT_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGueltigkeitStatus(GueltigkeitStatusDtoSpec gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
  }


  public GesuchsperiodeDtoSpec gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
    
    this.gesuchsperiodeStart = gesuchsperiodeStart;
    return this;
  }

   /**
   * Get gesuchsperiodeStart
   * @return gesuchsperiodeStart
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCHSPERIODE_START)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGesuchsperiodeStart() {
    return gesuchsperiodeStart;
  }


  @JsonProperty(JSON_PROPERTY_GESUCHSPERIODE_START)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
    this.gesuchsperiodeStart = gesuchsperiodeStart;
  }


  public GesuchsperiodeDtoSpec gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
    
    this.gesuchsperiodeStopp = gesuchsperiodeStopp;
    return this;
  }

   /**
   * Get gesuchsperiodeStopp
   * @return gesuchsperiodeStopp
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCHSPERIODE_STOPP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGesuchsperiodeStopp() {
    return gesuchsperiodeStopp;
  }


  @JsonProperty(JSON_PROPERTY_GESUCHSPERIODE_STOPP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
    this.gesuchsperiodeStopp = gesuchsperiodeStopp;
  }


  public GesuchsperiodeDtoSpec aufschaltterminStart(LocalDate aufschaltterminStart) {
    
    this.aufschaltterminStart = aufschaltterminStart;
    return this;
  }

   /**
   * Get aufschaltterminStart
   * @return aufschaltterminStart
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUFSCHALTTERMIN_START)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getAufschaltterminStart() {
    return aufschaltterminStart;
  }


  @JsonProperty(JSON_PROPERTY_AUFSCHALTTERMIN_START)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAufschaltterminStart(LocalDate aufschaltterminStart) {
    this.aufschaltterminStart = aufschaltterminStart;
  }


  public GesuchsperiodeDtoSpec einreichefristNormal(LocalDate einreichefristNormal) {
    
    this.einreichefristNormal = einreichefristNormal;
    return this;
  }

   /**
   * Get einreichefristNormal
   * @return einreichefristNormal
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINREICHEFRIST_NORMAL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getEinreichefristNormal() {
    return einreichefristNormal;
  }


  @JsonProperty(JSON_PROPERTY_EINREICHEFRIST_NORMAL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinreichefristNormal(LocalDate einreichefristNormal) {
    this.einreichefristNormal = einreichefristNormal;
  }


  public GesuchsperiodeDtoSpec einreichefristReduziert(LocalDate einreichefristReduziert) {
    
    this.einreichefristReduziert = einreichefristReduziert;
    return this;
  }

   /**
   * Get einreichefristReduziert
   * @return einreichefristReduziert
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINREICHEFRIST_REDUZIERT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getEinreichefristReduziert() {
    return einreichefristReduziert;
  }


  @JsonProperty(JSON_PROPERTY_EINREICHEFRIST_REDUZIERT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinreichefristReduziert(LocalDate einreichefristReduziert) {
    this.einreichefristReduziert = einreichefristReduziert;
  }


  public GesuchsperiodeDtoSpec gesuchsjahr(GesuchsjahrDtoSpec gesuchsjahr) {
    
    this.gesuchsjahr = gesuchsjahr;
    return this;
  }

   /**
   * Get gesuchsjahr
   * @return gesuchsjahr
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCHSJAHR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GesuchsjahrDtoSpec getGesuchsjahr() {
    return gesuchsjahr;
  }


  @JsonProperty(JSON_PROPERTY_GESUCHSJAHR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchsjahr(GesuchsjahrDtoSpec gesuchsjahr) {
    this.gesuchsjahr = gesuchsjahr;
  }


  public GesuchsperiodeDtoSpec ausbKostenSekII(Integer ausbKostenSekII) {
    
    this.ausbKostenSekII = ausbKostenSekII;
    return this;
  }

   /**
   * Get ausbKostenSekII
   * @return ausbKostenSekII
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSB_KOSTEN_SEK_I_I)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAusbKostenSekII() {
    return ausbKostenSekII;
  }


  @JsonProperty(JSON_PROPERTY_AUSB_KOSTEN_SEK_I_I)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
  }


  public GesuchsperiodeDtoSpec ausbKostenTertiaer(Integer ausbKostenTertiaer) {
    
    this.ausbKostenTertiaer = ausbKostenTertiaer;
    return this;
  }

   /**
   * Get ausbKostenTertiaer
   * @return ausbKostenTertiaer
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSB_KOSTEN_TERTIAER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAusbKostenTertiaer() {
    return ausbKostenTertiaer;
  }


  @JsonProperty(JSON_PROPERTY_AUSB_KOSTEN_TERTIAER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
  }


  public GesuchsperiodeDtoSpec fristNachreichenDokumente(Integer fristNachreichenDokumente) {
    
    this.fristNachreichenDokumente = fristNachreichenDokumente;
    return this;
  }

   /**
   * Get fristNachreichenDokumente
   * @return fristNachreichenDokumente
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FRIST_NACHREICHEN_DOKUMENTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFristNachreichenDokumente() {
    return fristNachreichenDokumente;
  }


  @JsonProperty(JSON_PROPERTY_FRIST_NACHREICHEN_DOKUMENTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFristNachreichenDokumente(Integer fristNachreichenDokumente) {
    this.fristNachreichenDokumente = fristNachreichenDokumente;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeDtoSpec gesuchsperiode = (GesuchsperiodeDtoSpec) o;
    return Objects.equals(this.id, gesuchsperiode.id) &&
        Objects.equals(this.bezeichnungDe, gesuchsperiode.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiode.bezeichnungFr) &&
        Objects.equals(this.gueltigkeitStatus, gesuchsperiode.gueltigkeitStatus) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiode.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiode.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiode.aufschaltterminStart) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiode.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiode.einreichefristReduziert) &&
        Objects.equals(this.gesuchsjahr, gesuchsperiode.gesuchsjahr) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiode.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiode.ausbKostenTertiaer) &&
        Objects.equals(this.fristNachreichenDokumente, gesuchsperiode.fristNachreichenDokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, gueltigkeitStatus, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, einreichefristNormal, einreichefristReduziert, gesuchsjahr, ausbKostenSekII, ausbKostenTertiaer, fristNachreichenDokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeDtoSpec {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    gueltigkeitStatus: ").append(toIndentedString(gueltigkeitStatus)).append("\n");
    sb.append("    gesuchsperiodeStart: ").append(toIndentedString(gesuchsperiodeStart)).append("\n");
    sb.append("    gesuchsperiodeStopp: ").append(toIndentedString(gesuchsperiodeStopp)).append("\n");
    sb.append("    aufschaltterminStart: ").append(toIndentedString(aufschaltterminStart)).append("\n");
    sb.append("    einreichefristNormal: ").append(toIndentedString(einreichefristNormal)).append("\n");
    sb.append("    einreichefristReduziert: ").append(toIndentedString(einreichefristReduziert)).append("\n");
    sb.append("    gesuchsjahr: ").append(toIndentedString(gesuchsjahr)).append("\n");
    sb.append("    ausbKostenSekII: ").append(toIndentedString(ausbKostenSekII)).append("\n");
    sb.append("    ausbKostenTertiaer: ").append(toIndentedString(ausbKostenTertiaer)).append("\n");
    sb.append("    fristNachreichenDokumente: ").append(toIndentedString(fristNachreichenDokumente)).append("\n");
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

