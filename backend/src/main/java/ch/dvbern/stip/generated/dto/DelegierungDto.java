package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.PersoenlicheAngabenDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
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



@JsonTypeName("Delegierung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DelegierungDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String fallNummer;
  private @Valid SozialdienstSlimDto sozialdienst;
  private @Valid PersoenlicheAngabenDto persoenlicheAngaben;
  private @Valid ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
  private @Valid SozialdienstBenutzerDto delegierterMitarbeiter;
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;

  protected DelegierungDto(DelegierungDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.fallNummer = b.fallNummer;
    this.sozialdienst = b.sozialdienst;
    this.persoenlicheAngaben = b.persoenlicheAngaben;
    this.status = b.status;
    this.delegierterMitarbeiter = b.delegierterMitarbeiter;
    this.startDate = b.startDate;
    this.endDate = b.endDate;
  }

  public DelegierungDto() {
  }

  /**
   **/
  public DelegierungDto id(UUID id) {
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
  public DelegierungDto fallNummer(String fallNummer) {
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
  public DelegierungDto sozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
    return this;
  }

  
  @JsonProperty("sozialdienst")
  @NotNull
  public SozialdienstSlimDto getSozialdienst() {
    return sozialdienst;
  }

  @JsonProperty("sozialdienst")
  public void setSozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
  }

  /**
   **/
  public DelegierungDto persoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
    this.persoenlicheAngaben = persoenlicheAngaben;
    return this;
  }

  
  @JsonProperty("persoenlicheAngaben")
  @NotNull
  public PersoenlicheAngabenDto getPersoenlicheAngaben() {
    return persoenlicheAngaben;
  }

  @JsonProperty("persoenlicheAngaben")
  public void setPersoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
    this.persoenlicheAngaben = persoenlicheAngaben;
  }

  /**
   **/
  public DelegierungDto status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
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
  public DelegierungDto delegierterMitarbeiter(SozialdienstBenutzerDto delegierterMitarbeiter) {
    this.delegierterMitarbeiter = delegierterMitarbeiter;
    return this;
  }

  
  @JsonProperty("delegierterMitarbeiter")
  public SozialdienstBenutzerDto getDelegierterMitarbeiter() {
    return delegierterMitarbeiter;
  }

  @JsonProperty("delegierterMitarbeiter")
  public void setDelegierterMitarbeiter(SozialdienstBenutzerDto delegierterMitarbeiter) {
    this.delegierterMitarbeiter = delegierterMitarbeiter;
  }

  /**
   **/
  public DelegierungDto startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  
  @JsonProperty("startDate")
  public LocalDate getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  public DelegierungDto endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  
  @JsonProperty("endDate")
  public LocalDate getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierungDto delegierung = (DelegierungDto) o;
    return Objects.equals(this.id, delegierung.id) &&
        Objects.equals(this.fallNummer, delegierung.fallNummer) &&
        Objects.equals(this.sozialdienst, delegierung.sozialdienst) &&
        Objects.equals(this.persoenlicheAngaben, delegierung.persoenlicheAngaben) &&
        Objects.equals(this.status, delegierung.status) &&
        Objects.equals(this.delegierterMitarbeiter, delegierung.delegierterMitarbeiter) &&
        Objects.equals(this.startDate, delegierung.startDate) &&
        Objects.equals(this.endDate, delegierung.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallNummer, sozialdienst, persoenlicheAngaben, status, delegierterMitarbeiter, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierungDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    sozialdienst: ").append(toIndentedString(sozialdienst)).append("\n");
    sb.append("    persoenlicheAngaben: ").append(toIndentedString(persoenlicheAngaben)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    delegierterMitarbeiter: ").append(toIndentedString(delegierterMitarbeiter)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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


  public static DelegierungDtoBuilder<?, ?> builder() {
    return new DelegierungDtoBuilderImpl();
  }

  private static final class DelegierungDtoBuilderImpl extends DelegierungDtoBuilder<DelegierungDto, DelegierungDtoBuilderImpl> {

    @Override
    protected DelegierungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DelegierungDto build() {
      return new DelegierungDto(this);
    }
  }

  public static abstract class DelegierungDtoBuilder<C extends DelegierungDto, B extends DelegierungDtoBuilder<C, B>>  {
    private UUID id;
    private String fallNummer;
    private SozialdienstSlimDto sozialdienst;
    private PersoenlicheAngabenDto persoenlicheAngaben;
    private ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
    private SozialdienstBenutzerDto delegierterMitarbeiter;
    private LocalDate startDate;
    private LocalDate endDate;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B sozialdienst(SozialdienstSlimDto sozialdienst) {
      this.sozialdienst = sozialdienst;
      return self();
    }
    public B persoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
      this.persoenlicheAngaben = persoenlicheAngaben;
      return self();
    }
    public B status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
      this.status = status;
      return self();
    }
    public B delegierterMitarbeiter(SozialdienstBenutzerDto delegierterMitarbeiter) {
      this.delegierterMitarbeiter = delegierterMitarbeiter;
      return self();
    }
    public B startDate(LocalDate startDate) {
      this.startDate = startDate;
      return self();
    }
    public B endDate(LocalDate endDate) {
      this.endDate = endDate;
      return self();
    }
  }
}

