package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DelegierungSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchWithChanges")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchWithChangesDto  implements Serializable {
  private UUID fallId;
  private String fallNummer;
  private UUID ausbildungId;
  private Boolean hasPendingAusbildungUnterbruchAntrag;
  private GesuchsperiodeDto gesuchsperiode;
  private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private String gesuchNummer;
  private UUID id;
  private LocalDate aenderungsdatum;
  private GesuchTrancheDto gesuchTrancheToWorkWith;
  private Boolean verfuegt;
  private String bearbeiter;
  private LocalDate einreichedatum;
  private Boolean hadDelegierungs;
  private LocalDate minDateEigenerWohnsitz;
  private DelegierungSlimDto delegierung;
  private LocalDate nachfristDokumente;
  private @Valid List<@Valid GesuchTrancheDto> changes = new ArrayList<>();
  private Boolean isInitial;

  protected GesuchWithChangesDto(GesuchWithChangesDtoBuilder<?, ?> b) {
    this.fallId = b.fallId;
    this.fallNummer = b.fallNummer;
    this.ausbildungId = b.ausbildungId;
    this.hasPendingAusbildungUnterbruchAntrag = b.hasPendingAusbildungUnterbruchAntrag;
    this.gesuchsperiode = b.gesuchsperiode;
    this.gesuchStatus = b.gesuchStatus;
    this.gesuchNummer = b.gesuchNummer;
    this.id = b.id;
    this.aenderungsdatum = b.aenderungsdatum;
    this.gesuchTrancheToWorkWith = b.gesuchTrancheToWorkWith;
    this.verfuegt = b.verfuegt;
    this.bearbeiter = b.bearbeiter;
    this.einreichedatum = b.einreichedatum;
    this.hadDelegierungs = b.hadDelegierungs;
    this.minDateEigenerWohnsitz = b.minDateEigenerWohnsitz;
    this.delegierung = b.delegierung;
    this.nachfristDokumente = b.nachfristDokumente;
    this.changes = b.changes;
    this.isInitial = b.isInitial;
  }

  public GesuchWithChangesDto() {
  }

  /**
   **/
  public GesuchWithChangesDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty(required = true, value = "fallId")
  @NotNull public UUID getFallId() {
    return fallId;
  }

  @JsonProperty(required = true, value = "fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public GesuchWithChangesDto fallNummer(String fallNummer) {
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
  public GesuchWithChangesDto ausbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungId")
  @NotNull public UUID getAusbildungId() {
    return ausbildungId;
  }

  @JsonProperty(required = true, value = "ausbildungId")
  public void setAusbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
  }

  /**
   **/
  public GesuchWithChangesDto hasPendingAusbildungUnterbruchAntrag(Boolean hasPendingAusbildungUnterbruchAntrag) {
    this.hasPendingAusbildungUnterbruchAntrag = hasPendingAusbildungUnterbruchAntrag;
    return this;
  }

  
  @JsonProperty(required = true, value = "hasPendingAusbildungUnterbruchAntrag")
  @NotNull public Boolean getHasPendingAusbildungUnterbruchAntrag() {
    return hasPendingAusbildungUnterbruchAntrag;
  }

  @JsonProperty(required = true, value = "hasPendingAusbildungUnterbruchAntrag")
  public void setHasPendingAusbildungUnterbruchAntrag(Boolean hasPendingAusbildungUnterbruchAntrag) {
    this.hasPendingAusbildungUnterbruchAntrag = hasPendingAusbildungUnterbruchAntrag;
  }

  /**
   **/
  public GesuchWithChangesDto gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchsperiode")
  @NotNull @Valid public GesuchsperiodeDto getGesuchsperiode() {
    return gesuchsperiode;
  }

  @JsonProperty(required = true, value = "gesuchsperiode")
  public void setGesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
  }

  /**
   **/
  public GesuchWithChangesDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
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
  public GesuchWithChangesDto gesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchNummer")
  @NotNull public String getGesuchNummer() {
    return gesuchNummer;
  }

  @JsonProperty(required = true, value = "gesuchNummer")
  public void setGesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }

  /**
   **/
  public GesuchWithChangesDto id(UUID id) {
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
  public GesuchWithChangesDto aenderungsdatum(LocalDate aenderungsdatum) {
    this.aenderungsdatum = aenderungsdatum;
    return this;
  }

  
  @JsonProperty(required = true, value = "aenderungsdatum")
  @NotNull public LocalDate getAenderungsdatum() {
    return aenderungsdatum;
  }

  @JsonProperty(required = true, value = "aenderungsdatum")
  public void setAenderungsdatum(LocalDate aenderungsdatum) {
    this.aenderungsdatum = aenderungsdatum;
  }

  /**
   **/
  public GesuchWithChangesDto gesuchTrancheToWorkWith(GesuchTrancheDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchTrancheToWorkWith")
  @NotNull @Valid public GesuchTrancheDto getGesuchTrancheToWorkWith() {
    return gesuchTrancheToWorkWith;
  }

  @JsonProperty(required = true, value = "gesuchTrancheToWorkWith")
  public void setGesuchTrancheToWorkWith(GesuchTrancheDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
  }

  /**
   **/
  public GesuchWithChangesDto verfuegt(Boolean verfuegt) {
    this.verfuegt = verfuegt;
    return this;
  }

  
  @JsonProperty(required = true, value = "verfuegt")
  @NotNull public Boolean getVerfuegt() {
    return verfuegt;
  }

  @JsonProperty(required = true, value = "verfuegt")
  public void setVerfuegt(Boolean verfuegt) {
    this.verfuegt = verfuegt;
  }

  /**
   * Zuständiger Sachbearbeiter des Gesuchs
   **/
  public GesuchWithChangesDto bearbeiter(String bearbeiter) {
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
  public GesuchWithChangesDto einreichedatum(LocalDate einreichedatum) {
    this.einreichedatum = einreichedatum;
    return this;
  }

  
  @JsonProperty("einreichedatum")
  public LocalDate getEinreichedatum() {
    return einreichedatum;
  }

  @JsonProperty("einreichedatum")
  public void setEinreichedatum(LocalDate einreichedatum) {
    this.einreichedatum = einreichedatum;
  }

  /**
   **/
  public GesuchWithChangesDto hadDelegierungs(Boolean hadDelegierungs) {
    this.hadDelegierungs = hadDelegierungs;
    return this;
  }

  
  @JsonProperty("hadDelegierungs")
  public Boolean getHadDelegierungs() {
    return hadDelegierungs;
  }

  @JsonProperty("hadDelegierungs")
  public void setHadDelegierungs(Boolean hadDelegierungs) {
    this.hadDelegierungs = hadDelegierungs;
  }

  /**
   **/
  public GesuchWithChangesDto minDateEigenerWohnsitz(LocalDate minDateEigenerWohnsitz) {
    this.minDateEigenerWohnsitz = minDateEigenerWohnsitz;
    return this;
  }

  
  @JsonProperty("minDateEigenerWohnsitz")
  public LocalDate getMinDateEigenerWohnsitz() {
    return minDateEigenerWohnsitz;
  }

  @JsonProperty("minDateEigenerWohnsitz")
  public void setMinDateEigenerWohnsitz(LocalDate minDateEigenerWohnsitz) {
    this.minDateEigenerWohnsitz = minDateEigenerWohnsitz;
  }

  /**
   **/
  public GesuchWithChangesDto delegierung(DelegierungSlimDto delegierung) {
    this.delegierung = delegierung;
    return this;
  }

  
  @JsonProperty("delegierung")
  @Valid public DelegierungSlimDto getDelegierung() {
    return delegierung;
  }

  @JsonProperty("delegierung")
  public void setDelegierung(DelegierungSlimDto delegierung) {
    this.delegierung = delegierung;
  }

  /**
   **/
  public GesuchWithChangesDto nachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
    return this;
  }

  
  @JsonProperty("nachfristDokumente")
  public LocalDate getNachfristDokumente() {
    return nachfristDokumente;
  }

  @JsonProperty("nachfristDokumente")
  public void setNachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
  }

  /**
   **/
  public GesuchWithChangesDto changes(List<@Valid GesuchTrancheDto> changes) {
    this.changes = changes;
    return this;
  }

  
  @JsonProperty("changes")
  @Valid public List<@Valid GesuchTrancheDto> getChanges() {
    return changes;
  }

  @JsonProperty("changes")
  public void setChanges(List<@Valid GesuchTrancheDto> changes) {
    this.changes = changes;
  }

  public GesuchWithChangesDto addChangesItem(GesuchTrancheDto changesItem) {
    if (this.changes == null) {
      this.changes = new ArrayList<>();
    }

    this.changes.add(changesItem);
    return this;
  }

  public GesuchWithChangesDto removeChangesItem(GesuchTrancheDto changesItem) {
    if (changesItem != null && this.changes != null) {
      this.changes.remove(changesItem);
    }

    return this;
  }
  /**
   **/
  public GesuchWithChangesDto isInitial(Boolean isInitial) {
    this.isInitial = isInitial;
    return this;
  }

  
  @JsonProperty("isInitial")
  public Boolean getIsInitial() {
    return isInitial;
  }

  @JsonProperty("isInitial")
  public void setIsInitial(Boolean isInitial) {
    this.isInitial = isInitial;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchWithChangesDto gesuchWithChanges = (GesuchWithChangesDto) o;
    return Objects.equals(this.fallId, gesuchWithChanges.fallId) &&
        Objects.equals(this.fallNummer, gesuchWithChanges.fallNummer) &&
        Objects.equals(this.ausbildungId, gesuchWithChanges.ausbildungId) &&
        Objects.equals(this.hasPendingAusbildungUnterbruchAntrag, gesuchWithChanges.hasPendingAusbildungUnterbruchAntrag) &&
        Objects.equals(this.gesuchsperiode, gesuchWithChanges.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gesuchWithChanges.gesuchStatus) &&
        Objects.equals(this.gesuchNummer, gesuchWithChanges.gesuchNummer) &&
        Objects.equals(this.id, gesuchWithChanges.id) &&
        Objects.equals(this.aenderungsdatum, gesuchWithChanges.aenderungsdatum) &&
        Objects.equals(this.gesuchTrancheToWorkWith, gesuchWithChanges.gesuchTrancheToWorkWith) &&
        Objects.equals(this.verfuegt, gesuchWithChanges.verfuegt) &&
        Objects.equals(this.bearbeiter, gesuchWithChanges.bearbeiter) &&
        Objects.equals(this.einreichedatum, gesuchWithChanges.einreichedatum) &&
        Objects.equals(this.hadDelegierungs, gesuchWithChanges.hadDelegierungs) &&
        Objects.equals(this.minDateEigenerWohnsitz, gesuchWithChanges.minDateEigenerWohnsitz) &&
        Objects.equals(this.delegierung, gesuchWithChanges.delegierung) &&
        Objects.equals(this.nachfristDokumente, gesuchWithChanges.nachfristDokumente) &&
        Objects.equals(this.changes, gesuchWithChanges.changes) &&
        Objects.equals(this.isInitial, gesuchWithChanges.isInitial);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, fallNummer, ausbildungId, hasPendingAusbildungUnterbruchAntrag, gesuchsperiode, gesuchStatus, gesuchNummer, id, aenderungsdatum, gesuchTrancheToWorkWith, verfuegt, bearbeiter, einreichedatum, hadDelegierungs, minDateEigenerWohnsitz, delegierung, nachfristDokumente, changes, isInitial);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchWithChangesDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    ausbildungId: ").append(toIndentedString(ausbildungId)).append("\n");
    sb.append("    hasPendingAusbildungUnterbruchAntrag: ").append(toIndentedString(hasPendingAusbildungUnterbruchAntrag)).append("\n");
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aenderungsdatum: ").append(toIndentedString(aenderungsdatum)).append("\n");
    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
    sb.append("    verfuegt: ").append(toIndentedString(verfuegt)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
    sb.append("    einreichedatum: ").append(toIndentedString(einreichedatum)).append("\n");
    sb.append("    hadDelegierungs: ").append(toIndentedString(hadDelegierungs)).append("\n");
    sb.append("    minDateEigenerWohnsitz: ").append(toIndentedString(minDateEigenerWohnsitz)).append("\n");
    sb.append("    delegierung: ").append(toIndentedString(delegierung)).append("\n");
    sb.append("    nachfristDokumente: ").append(toIndentedString(nachfristDokumente)).append("\n");
    sb.append("    changes: ").append(toIndentedString(changes)).append("\n");
    sb.append("    isInitial: ").append(toIndentedString(isInitial)).append("\n");
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


  public static GesuchWithChangesDtoBuilder<?, ?> builder() {
    return new GesuchWithChangesDtoBuilderImpl();
  }

  private static final class GesuchWithChangesDtoBuilderImpl extends GesuchWithChangesDtoBuilder<GesuchWithChangesDto, GesuchWithChangesDtoBuilderImpl> {

    @Override
    protected GesuchWithChangesDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchWithChangesDto build() {
      return new GesuchWithChangesDto(this);
    }
  }

  public static abstract class GesuchWithChangesDtoBuilder<C extends GesuchWithChangesDto, B extends GesuchWithChangesDtoBuilder<C, B>>  {
    private UUID fallId;
    private String fallNummer;
    private UUID ausbildungId;
    private Boolean hasPendingAusbildungUnterbruchAntrag;
    private GesuchsperiodeDto gesuchsperiode;
    private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
    private String gesuchNummer;
    private UUID id;
    private LocalDate aenderungsdatum;
    private GesuchTrancheDto gesuchTrancheToWorkWith;
    private Boolean verfuegt;
    private String bearbeiter;
    private LocalDate einreichedatum;
    private Boolean hadDelegierungs;
    private LocalDate minDateEigenerWohnsitz;
    private DelegierungSlimDto delegierung;
    private LocalDate nachfristDokumente;
    private List<GesuchTrancheDto> changes = new ArrayList<>();
    private Boolean isInitial;
    protected abstract B self();

    public abstract C build();

    public B fallId(UUID fallId) {
      this.fallId = fallId;
      return self();
    }
    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B ausbildungId(UUID ausbildungId) {
      this.ausbildungId = ausbildungId;
      return self();
    }
    public B hasPendingAusbildungUnterbruchAntrag(Boolean hasPendingAusbildungUnterbruchAntrag) {
      this.hasPendingAusbildungUnterbruchAntrag = hasPendingAusbildungUnterbruchAntrag;
      return self();
    }
    public B gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
      this.gesuchsperiode = gesuchsperiode;
      return self();
    }
    public B gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
      this.gesuchStatus = gesuchStatus;
      return self();
    }
    public B gesuchNummer(String gesuchNummer) {
      this.gesuchNummer = gesuchNummer;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B aenderungsdatum(LocalDate aenderungsdatum) {
      this.aenderungsdatum = aenderungsdatum;
      return self();
    }
    public B gesuchTrancheToWorkWith(GesuchTrancheDto gesuchTrancheToWorkWith) {
      this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
      return self();
    }
    public B verfuegt(Boolean verfuegt) {
      this.verfuegt = verfuegt;
      return self();
    }
    public B bearbeiter(String bearbeiter) {
      this.bearbeiter = bearbeiter;
      return self();
    }
    public B einreichedatum(LocalDate einreichedatum) {
      this.einreichedatum = einreichedatum;
      return self();
    }
    public B hadDelegierungs(Boolean hadDelegierungs) {
      this.hadDelegierungs = hadDelegierungs;
      return self();
    }
    public B minDateEigenerWohnsitz(LocalDate minDateEigenerWohnsitz) {
      this.minDateEigenerWohnsitz = minDateEigenerWohnsitz;
      return self();
    }
    public B delegierung(DelegierungSlimDto delegierung) {
      this.delegierung = delegierung;
      return self();
    }
    public B nachfristDokumente(LocalDate nachfristDokumente) {
      this.nachfristDokumente = nachfristDokumente;
      return self();
    }
    public B changes(List<GesuchTrancheDto> changes) {
      this.changes = changes;
      return self();
    }
    public B isInitial(Boolean isInitial) {
      this.isInitial = isInitial;
      return self();
    }
  }
}
