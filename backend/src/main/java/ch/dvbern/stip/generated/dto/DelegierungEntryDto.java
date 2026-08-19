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



@JsonTypeName("DelegierungEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DelegierungEntryDto  implements Serializable {
  private @Valid String fallNummer;
  private @Valid UUID fallId;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid String wohnort;
  private @Valid ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
  private @Valid Integer totalCount;
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private @Valid ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus aenderungStatus;

  protected DelegierungEntryDto(DelegierungEntryDtoBuilder<?, ?> b) {
    this.fallNummer = b.fallNummer;
    this.fallId = b.fallId;
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.geburtsdatum = b.geburtsdatum;
    this.wohnort = b.wohnort;
    this.status = b.status;
    this.totalCount = b.totalCount;
    this.id = b.id;
    this.gesuchStatus = b.gesuchStatus;
    this.aenderungStatus = b.aenderungStatus;
  }

  public DelegierungEntryDto() {
  }

  /**
   **/
  public DelegierungEntryDto fallNummer(String fallNummer) {
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
  public DelegierungEntryDto fallId(UUID fallId) {
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
  public DelegierungEntryDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  @NotNull
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public DelegierungEntryDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public DelegierungEntryDto geburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DelegierungEntryDto wohnort(String wohnort) {
    this.wohnort = wohnort;
    return this;
  }

  
  @JsonProperty("wohnort")
  @NotNull
  public String getWohnort() {
    return wohnort;
  }

  @JsonProperty("wohnort")
  public void setWohnort(String wohnort) {
    this.wohnort = wohnort;
  }

  /**
   **/
  public DelegierungEntryDto status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.delegieren.type.DelegierungStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
    this.status = status;
  }

  /**
   **/
  public DelegierungEntryDto totalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  
  @JsonProperty("totalCount")
  @NotNull
  public Integer getTotalCount() {
    return totalCount;
  }

  @JsonProperty("totalCount")
  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  /**
   **/
  public DelegierungEntryDto id(UUID id) {
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
  public DelegierungEntryDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  public ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public DelegierungEntryDto aenderungStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus aenderungStatus) {
    this.aenderungStatus = aenderungStatus;
    return this;
  }

  
  @JsonProperty("aenderungStatus")
  public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus getAenderungStatus() {
    return aenderungStatus;
  }

  @JsonProperty("aenderungStatus")
  public void setAenderungStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus aenderungStatus) {
    this.aenderungStatus = aenderungStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierungEntryDto delegierungEntry = (DelegierungEntryDto) o;
    return Objects.equals(this.fallNummer, delegierungEntry.fallNummer) &&
        Objects.equals(this.fallId, delegierungEntry.fallId) &&
        Objects.equals(this.nachname, delegierungEntry.nachname) &&
        Objects.equals(this.vorname, delegierungEntry.vorname) &&
        Objects.equals(this.geburtsdatum, delegierungEntry.geburtsdatum) &&
        Objects.equals(this.wohnort, delegierungEntry.wohnort) &&
        Objects.equals(this.status, delegierungEntry.status) &&
        Objects.equals(this.totalCount, delegierungEntry.totalCount) &&
        Objects.equals(this.id, delegierungEntry.id) &&
        Objects.equals(this.gesuchStatus, delegierungEntry.gesuchStatus) &&
        Objects.equals(this.aenderungStatus, delegierungEntry.aenderungStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallNummer, fallId, nachname, vorname, geburtsdatum, wohnort, status, totalCount, id, gesuchStatus, aenderungStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierungEntryDto {\n");
    
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    wohnort: ").append(toIndentedString(wohnort)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    aenderungStatus: ").append(toIndentedString(aenderungStatus)).append("\n");
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


  public static DelegierungEntryDtoBuilder<?, ?> builder() {
    return new DelegierungEntryDtoBuilderImpl();
  }

  private static final class DelegierungEntryDtoBuilderImpl extends DelegierungEntryDtoBuilder<DelegierungEntryDto, DelegierungEntryDtoBuilderImpl> {

    @Override
    protected DelegierungEntryDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DelegierungEntryDto build() {
      return new DelegierungEntryDto(this);
    }
  }

  public static abstract class DelegierungEntryDtoBuilder<C extends DelegierungEntryDto, B extends DelegierungEntryDtoBuilder<C, B>>  {
    private String fallNummer;
    private UUID fallId;
    private String nachname;
    private String vorname;
    private LocalDate geburtsdatum;
    private String wohnort;
    private ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
    private Integer totalCount;
    private UUID id;
    private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
    private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus aenderungStatus;
    protected abstract B self();

    public abstract C build();

    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B fallId(UUID fallId) {
      this.fallId = fallId;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B geburtsdatum(LocalDate geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B wohnort(String wohnort) {
      this.wohnort = wohnort;
      return self();
    }
    public B status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
      this.status = status;
      return self();
    }
    public B totalCount(Integer totalCount) {
      this.totalCount = totalCount;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
      this.gesuchStatus = gesuchStatus;
      return self();
    }
    public B aenderungStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus aenderungStatus) {
      this.aenderungStatus = aenderungStatus;
      return self();
    }
  }
}

