package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.PersonValueItemDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("PersoenlichesBudgetresultatEinnahmen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PersoenlichesBudgetresultatEinnahmenDto  implements Serializable {
  private @Valid Integer total;
  private @Valid Integer nettoerwerbseinkommen;
  private @Valid Integer nettoerwerbseinkommenPartner;
  private @Valid Integer nettoerwerbseinkommenTotal;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer einnahmenBGSAPartner;
  private @Valid Integer einnahmenBGSATotal;
  private @Valid Integer kinderAusbildungszulagen;
  private @Valid Integer kinderAusbildungszulagenPartner;
  private @Valid List<PersonValueItemDto> kinderAusbildungszulagenKinder = new ArrayList<>();
  private @Valid Integer kinderAusbildungszulagenTotal;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer unterhaltsbeitraegePartner;
  private @Valid List<PersonValueItemDto> unterhaltsbeitraegeKinder = new ArrayList<>();
  private @Valid Integer unterhaltsbeitraegeTotal;
  private @Valid Integer eoLeistungen;
  private @Valid Integer eoLeistungenPartner;
  private @Valid Integer eoLeistungenTotal;
  private @Valid Integer taggelderAHVIV;
  private @Valid Integer taggelderAHVIVPartner;
  private @Valid Integer taggelderAHVIVTotal;
  private @Valid Integer renten;
  private @Valid Integer rentenPartner;
  private @Valid List<PersonValueItemDto> rentenKinder = new ArrayList<>();
  private @Valid Integer rentenTotal;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer ergaenzungsleistungenPartner;
  private @Valid List<PersonValueItemDto> ergaenzungsleistungenKinder = new ArrayList<>();
  private @Valid Integer ergaenzungsleistungenTotal;
  private @Valid Integer beitraegeGemeindeInstitutionen;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer andereEinnahmenPartner;
  private @Valid List<PersonValueItemDto> andereEinnahmenKinder = new ArrayList<>();
  private @Valid Integer andereEinnahmenTotal;
  private @Valid Integer anrechenbaresVermoegen;
  private @Valid Integer steuerbaresVermoegen;
  private @Valid Integer elterlicheLeistung;

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty("total")
  @NotNull
  public Integer getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommen")
  @NotNull
  public Integer getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }

  @JsonProperty("nettoerwerbseinkommen")
  public void setNettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto nettoerwerbseinkommenPartner(Integer nettoerwerbseinkommenPartner) {
    this.nettoerwerbseinkommenPartner = nettoerwerbseinkommenPartner;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommenPartner")
  @NotNull
  public Integer getNettoerwerbseinkommenPartner() {
    return nettoerwerbseinkommenPartner;
  }

  @JsonProperty("nettoerwerbseinkommenPartner")
  public void setNettoerwerbseinkommenPartner(Integer nettoerwerbseinkommenPartner) {
    this.nettoerwerbseinkommenPartner = nettoerwerbseinkommenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto nettoerwerbseinkommenTotal(Integer nettoerwerbseinkommenTotal) {
    this.nettoerwerbseinkommenTotal = nettoerwerbseinkommenTotal;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommenTotal")
  @NotNull
  public Integer getNettoerwerbseinkommenTotal() {
    return nettoerwerbseinkommenTotal;
  }

  @JsonProperty("nettoerwerbseinkommenTotal")
  public void setNettoerwerbseinkommenTotal(Integer nettoerwerbseinkommenTotal) {
    this.nettoerwerbseinkommenTotal = nettoerwerbseinkommenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  @NotNull
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto einnahmenBGSAPartner(Integer einnahmenBGSAPartner) {
    this.einnahmenBGSAPartner = einnahmenBGSAPartner;
    return this;
  }

  
  @JsonProperty("einnahmenBGSAPartner")
  @NotNull
  public Integer getEinnahmenBGSAPartner() {
    return einnahmenBGSAPartner;
  }

  @JsonProperty("einnahmenBGSAPartner")
  public void setEinnahmenBGSAPartner(Integer einnahmenBGSAPartner) {
    this.einnahmenBGSAPartner = einnahmenBGSAPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto einnahmenBGSATotal(Integer einnahmenBGSATotal) {
    this.einnahmenBGSATotal = einnahmenBGSATotal;
    return this;
  }

  
  @JsonProperty("einnahmenBGSATotal")
  @NotNull
  public Integer getEinnahmenBGSATotal() {
    return einnahmenBGSATotal;
  }

  @JsonProperty("einnahmenBGSATotal")
  public void setEinnahmenBGSATotal(Integer einnahmenBGSATotal) {
    this.einnahmenBGSATotal = einnahmenBGSATotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto kinderAusbildungszulagen(Integer kinderAusbildungszulagen) {
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
    return this;
  }

  
  @JsonProperty("kinderAusbildungszulagen")
  @NotNull
  public Integer getKinderAusbildungszulagen() {
    return kinderAusbildungszulagen;
  }

  @JsonProperty("kinderAusbildungszulagen")
  public void setKinderAusbildungszulagen(Integer kinderAusbildungszulagen) {
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto kinderAusbildungszulagenPartner(Integer kinderAusbildungszulagenPartner) {
    this.kinderAusbildungszulagenPartner = kinderAusbildungszulagenPartner;
    return this;
  }

  
  @JsonProperty("kinderAusbildungszulagenPartner")
  @NotNull
  public Integer getKinderAusbildungszulagenPartner() {
    return kinderAusbildungszulagenPartner;
  }

  @JsonProperty("kinderAusbildungszulagenPartner")
  public void setKinderAusbildungszulagenPartner(Integer kinderAusbildungszulagenPartner) {
    this.kinderAusbildungszulagenPartner = kinderAusbildungszulagenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto kinderAusbildungszulagenKinder(List<PersonValueItemDto> kinderAusbildungszulagenKinder) {
    this.kinderAusbildungszulagenKinder = kinderAusbildungszulagenKinder;
    return this;
  }

  
  @JsonProperty("kinderAusbildungszulagenKinder")
  @NotNull
  public List<PersonValueItemDto> getKinderAusbildungszulagenKinder() {
    return kinderAusbildungszulagenKinder;
  }

  @JsonProperty("kinderAusbildungszulagenKinder")
  public void setKinderAusbildungszulagenKinder(List<PersonValueItemDto> kinderAusbildungszulagenKinder) {
    this.kinderAusbildungszulagenKinder = kinderAusbildungszulagenKinder;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addKinderAusbildungszulagenKinderItem(PersonValueItemDto kinderAusbildungszulagenKinderItem) {
    if (this.kinderAusbildungszulagenKinder == null) {
      this.kinderAusbildungszulagenKinder = new ArrayList<>();
    }

    this.kinderAusbildungszulagenKinder.add(kinderAusbildungszulagenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeKinderAusbildungszulagenKinderItem(PersonValueItemDto kinderAusbildungszulagenKinderItem) {
    if (kinderAusbildungszulagenKinderItem != null && this.kinderAusbildungszulagenKinder != null) {
      this.kinderAusbildungszulagenKinder.remove(kinderAusbildungszulagenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto kinderAusbildungszulagenTotal(Integer kinderAusbildungszulagenTotal) {
    this.kinderAusbildungszulagenTotal = kinderAusbildungszulagenTotal;
    return this;
  }

  
  @JsonProperty("kinderAusbildungszulagenTotal")
  @NotNull
  public Integer getKinderAusbildungszulagenTotal() {
    return kinderAusbildungszulagenTotal;
  }

  @JsonProperty("kinderAusbildungszulagenTotal")
  public void setKinderAusbildungszulagenTotal(Integer kinderAusbildungszulagenTotal) {
    this.kinderAusbildungszulagenTotal = kinderAusbildungszulagenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  @NotNull
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto unterhaltsbeitraegePartner(Integer unterhaltsbeitraegePartner) {
    this.unterhaltsbeitraegePartner = unterhaltsbeitraegePartner;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraegePartner")
  @NotNull
  public Integer getUnterhaltsbeitraegePartner() {
    return unterhaltsbeitraegePartner;
  }

  @JsonProperty("unterhaltsbeitraegePartner")
  public void setUnterhaltsbeitraegePartner(Integer unterhaltsbeitraegePartner) {
    this.unterhaltsbeitraegePartner = unterhaltsbeitraegePartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto unterhaltsbeitraegeKinder(List<PersonValueItemDto> unterhaltsbeitraegeKinder) {
    this.unterhaltsbeitraegeKinder = unterhaltsbeitraegeKinder;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraegeKinder")
  @NotNull
  public List<PersonValueItemDto> getUnterhaltsbeitraegeKinder() {
    return unterhaltsbeitraegeKinder;
  }

  @JsonProperty("unterhaltsbeitraegeKinder")
  public void setUnterhaltsbeitraegeKinder(List<PersonValueItemDto> unterhaltsbeitraegeKinder) {
    this.unterhaltsbeitraegeKinder = unterhaltsbeitraegeKinder;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addUnterhaltsbeitraegeKinderItem(PersonValueItemDto unterhaltsbeitraegeKinderItem) {
    if (this.unterhaltsbeitraegeKinder == null) {
      this.unterhaltsbeitraegeKinder = new ArrayList<>();
    }

    this.unterhaltsbeitraegeKinder.add(unterhaltsbeitraegeKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeUnterhaltsbeitraegeKinderItem(PersonValueItemDto unterhaltsbeitraegeKinderItem) {
    if (unterhaltsbeitraegeKinderItem != null && this.unterhaltsbeitraegeKinder != null) {
      this.unterhaltsbeitraegeKinder.remove(unterhaltsbeitraegeKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto unterhaltsbeitraegeTotal(Integer unterhaltsbeitraegeTotal) {
    this.unterhaltsbeitraegeTotal = unterhaltsbeitraegeTotal;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraegeTotal")
  @NotNull
  public Integer getUnterhaltsbeitraegeTotal() {
    return unterhaltsbeitraegeTotal;
  }

  @JsonProperty("unterhaltsbeitraegeTotal")
  public void setUnterhaltsbeitraegeTotal(Integer unterhaltsbeitraegeTotal) {
    this.unterhaltsbeitraegeTotal = unterhaltsbeitraegeTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto eoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  @NotNull
  public Integer getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto eoLeistungenPartner(Integer eoLeistungenPartner) {
    this.eoLeistungenPartner = eoLeistungenPartner;
    return this;
  }

  
  @JsonProperty("eoLeistungenPartner")
  @NotNull
  public Integer getEoLeistungenPartner() {
    return eoLeistungenPartner;
  }

  @JsonProperty("eoLeistungenPartner")
  public void setEoLeistungenPartner(Integer eoLeistungenPartner) {
    this.eoLeistungenPartner = eoLeistungenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto eoLeistungenTotal(Integer eoLeistungenTotal) {
    this.eoLeistungenTotal = eoLeistungenTotal;
    return this;
  }

  
  @JsonProperty("eoLeistungenTotal")
  @NotNull
  public Integer getEoLeistungenTotal() {
    return eoLeistungenTotal;
  }

  @JsonProperty("eoLeistungenTotal")
  public void setEoLeistungenTotal(Integer eoLeistungenTotal) {
    this.eoLeistungenTotal = eoLeistungenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto taggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty("taggelderAHVIV")
  @NotNull
  public Integer getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty("taggelderAHVIV")
  public void setTaggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto taggelderAHVIVPartner(Integer taggelderAHVIVPartner) {
    this.taggelderAHVIVPartner = taggelderAHVIVPartner;
    return this;
  }

  
  @JsonProperty("taggelderAHVIVPartner")
  @NotNull
  public Integer getTaggelderAHVIVPartner() {
    return taggelderAHVIVPartner;
  }

  @JsonProperty("taggelderAHVIVPartner")
  public void setTaggelderAHVIVPartner(Integer taggelderAHVIVPartner) {
    this.taggelderAHVIVPartner = taggelderAHVIVPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto taggelderAHVIVTotal(Integer taggelderAHVIVTotal) {
    this.taggelderAHVIVTotal = taggelderAHVIVTotal;
    return this;
  }

  
  @JsonProperty("taggelderAHVIVTotal")
  @NotNull
  public Integer getTaggelderAHVIVTotal() {
    return taggelderAHVIVTotal;
  }

  @JsonProperty("taggelderAHVIVTotal")
  public void setTaggelderAHVIVTotal(Integer taggelderAHVIVTotal) {
    this.taggelderAHVIVTotal = taggelderAHVIVTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  @NotNull
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto rentenPartner(Integer rentenPartner) {
    this.rentenPartner = rentenPartner;
    return this;
  }

  
  @JsonProperty("rentenPartner")
  @NotNull
  public Integer getRentenPartner() {
    return rentenPartner;
  }

  @JsonProperty("rentenPartner")
  public void setRentenPartner(Integer rentenPartner) {
    this.rentenPartner = rentenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto rentenKinder(List<PersonValueItemDto> rentenKinder) {
    this.rentenKinder = rentenKinder;
    return this;
  }

  
  @JsonProperty("rentenKinder")
  @NotNull
  public List<PersonValueItemDto> getRentenKinder() {
    return rentenKinder;
  }

  @JsonProperty("rentenKinder")
  public void setRentenKinder(List<PersonValueItemDto> rentenKinder) {
    this.rentenKinder = rentenKinder;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addRentenKinderItem(PersonValueItemDto rentenKinderItem) {
    if (this.rentenKinder == null) {
      this.rentenKinder = new ArrayList<>();
    }

    this.rentenKinder.add(rentenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeRentenKinderItem(PersonValueItemDto rentenKinderItem) {
    if (rentenKinderItem != null && this.rentenKinder != null) {
      this.rentenKinder.remove(rentenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto rentenTotal(Integer rentenTotal) {
    this.rentenTotal = rentenTotal;
    return this;
  }

  
  @JsonProperty("rentenTotal")
  @NotNull
  public Integer getRentenTotal() {
    return rentenTotal;
  }

  @JsonProperty("rentenTotal")
  public void setRentenTotal(Integer rentenTotal) {
    this.rentenTotal = rentenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto ergaenzungsleistungenPartner(Integer ergaenzungsleistungenPartner) {
    this.ergaenzungsleistungenPartner = ergaenzungsleistungenPartner;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungenPartner")
  @NotNull
  public Integer getErgaenzungsleistungenPartner() {
    return ergaenzungsleistungenPartner;
  }

  @JsonProperty("ergaenzungsleistungenPartner")
  public void setErgaenzungsleistungenPartner(Integer ergaenzungsleistungenPartner) {
    this.ergaenzungsleistungenPartner = ergaenzungsleistungenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto ergaenzungsleistungenKinder(List<PersonValueItemDto> ergaenzungsleistungenKinder) {
    this.ergaenzungsleistungenKinder = ergaenzungsleistungenKinder;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungenKinder")
  @NotNull
  public List<PersonValueItemDto> getErgaenzungsleistungenKinder() {
    return ergaenzungsleistungenKinder;
  }

  @JsonProperty("ergaenzungsleistungenKinder")
  public void setErgaenzungsleistungenKinder(List<PersonValueItemDto> ergaenzungsleistungenKinder) {
    this.ergaenzungsleistungenKinder = ergaenzungsleistungenKinder;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addErgaenzungsleistungenKinderItem(PersonValueItemDto ergaenzungsleistungenKinderItem) {
    if (this.ergaenzungsleistungenKinder == null) {
      this.ergaenzungsleistungenKinder = new ArrayList<>();
    }

    this.ergaenzungsleistungenKinder.add(ergaenzungsleistungenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeErgaenzungsleistungenKinderItem(PersonValueItemDto ergaenzungsleistungenKinderItem) {
    if (ergaenzungsleistungenKinderItem != null && this.ergaenzungsleistungenKinder != null) {
      this.ergaenzungsleistungenKinder.remove(ergaenzungsleistungenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto ergaenzungsleistungenTotal(Integer ergaenzungsleistungenTotal) {
    this.ergaenzungsleistungenTotal = ergaenzungsleistungenTotal;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungenTotal")
  @NotNull
  public Integer getErgaenzungsleistungenTotal() {
    return ergaenzungsleistungenTotal;
  }

  @JsonProperty("ergaenzungsleistungenTotal")
  public void setErgaenzungsleistungenTotal(Integer ergaenzungsleistungenTotal) {
    this.ergaenzungsleistungenTotal = ergaenzungsleistungenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto beitraegeGemeindeInstitutionen(Integer beitraegeGemeindeInstitutionen) {
    this.beitraegeGemeindeInstitutionen = beitraegeGemeindeInstitutionen;
    return this;
  }

  
  @JsonProperty("beitraegeGemeindeInstitutionen")
  @NotNull
  public Integer getBeitraegeGemeindeInstitutionen() {
    return beitraegeGemeindeInstitutionen;
  }

  @JsonProperty("beitraegeGemeindeInstitutionen")
  public void setBeitraegeGemeindeInstitutionen(Integer beitraegeGemeindeInstitutionen) {
    this.beitraegeGemeindeInstitutionen = beitraegeGemeindeInstitutionen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  @NotNull
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto andereEinnahmenPartner(Integer andereEinnahmenPartner) {
    this.andereEinnahmenPartner = andereEinnahmenPartner;
    return this;
  }

  
  @JsonProperty("andereEinnahmenPartner")
  @NotNull
  public Integer getAndereEinnahmenPartner() {
    return andereEinnahmenPartner;
  }

  @JsonProperty("andereEinnahmenPartner")
  public void setAndereEinnahmenPartner(Integer andereEinnahmenPartner) {
    this.andereEinnahmenPartner = andereEinnahmenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto andereEinnahmenKinder(List<PersonValueItemDto> andereEinnahmenKinder) {
    this.andereEinnahmenKinder = andereEinnahmenKinder;
    return this;
  }

  
  @JsonProperty("andereEinnahmenKinder")
  @NotNull
  public List<PersonValueItemDto> getAndereEinnahmenKinder() {
    return andereEinnahmenKinder;
  }

  @JsonProperty("andereEinnahmenKinder")
  public void setAndereEinnahmenKinder(List<PersonValueItemDto> andereEinnahmenKinder) {
    this.andereEinnahmenKinder = andereEinnahmenKinder;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addAndereEinnahmenKinderItem(PersonValueItemDto andereEinnahmenKinderItem) {
    if (this.andereEinnahmenKinder == null) {
      this.andereEinnahmenKinder = new ArrayList<>();
    }

    this.andereEinnahmenKinder.add(andereEinnahmenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeAndereEinnahmenKinderItem(PersonValueItemDto andereEinnahmenKinderItem) {
    if (andereEinnahmenKinderItem != null && this.andereEinnahmenKinder != null) {
      this.andereEinnahmenKinder.remove(andereEinnahmenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto andereEinnahmenTotal(Integer andereEinnahmenTotal) {
    this.andereEinnahmenTotal = andereEinnahmenTotal;
    return this;
  }

  
  @JsonProperty("andereEinnahmenTotal")
  @NotNull
  public Integer getAndereEinnahmenTotal() {
    return andereEinnahmenTotal;
  }

  @JsonProperty("andereEinnahmenTotal")
  public void setAndereEinnahmenTotal(Integer andereEinnahmenTotal) {
    this.andereEinnahmenTotal = andereEinnahmenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
    return this;
  }

  
  @JsonProperty("anrechenbaresVermoegen")
  @NotNull
  public Integer getAnrechenbaresVermoegen() {
    return anrechenbaresVermoegen;
  }

  @JsonProperty("anrechenbaresVermoegen")
  public void setAnrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto steuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
    return this;
  }

  
  @JsonProperty("steuerbaresVermoegen")
  @NotNull
  public Integer getSteuerbaresVermoegen() {
    return steuerbaresVermoegen;
  }

  @JsonProperty("steuerbaresVermoegen")
  public void setSteuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto elterlicheLeistung(Integer elterlicheLeistung) {
    this.elterlicheLeistung = elterlicheLeistung;
    return this;
  }

  
  @JsonProperty("elterlicheLeistung")
  @NotNull
  public Integer getElterlicheLeistung() {
    return elterlicheLeistung;
  }

  @JsonProperty("elterlicheLeistung")
  public void setElterlicheLeistung(Integer elterlicheLeistung) {
    this.elterlicheLeistung = elterlicheLeistung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersoenlichesBudgetresultatEinnahmenDto persoenlichesBudgetresultatEinnahmen = (PersoenlichesBudgetresultatEinnahmenDto) o;
    return Objects.equals(this.total, persoenlichesBudgetresultatEinnahmen.total) &&
        Objects.equals(this.nettoerwerbseinkommen, persoenlichesBudgetresultatEinnahmen.nettoerwerbseinkommen) &&
        Objects.equals(this.nettoerwerbseinkommenPartner, persoenlichesBudgetresultatEinnahmen.nettoerwerbseinkommenPartner) &&
        Objects.equals(this.nettoerwerbseinkommenTotal, persoenlichesBudgetresultatEinnahmen.nettoerwerbseinkommenTotal) &&
        Objects.equals(this.einnahmenBGSA, persoenlichesBudgetresultatEinnahmen.einnahmenBGSA) &&
        Objects.equals(this.einnahmenBGSAPartner, persoenlichesBudgetresultatEinnahmen.einnahmenBGSAPartner) &&
        Objects.equals(this.einnahmenBGSATotal, persoenlichesBudgetresultatEinnahmen.einnahmenBGSATotal) &&
        Objects.equals(this.kinderAusbildungszulagen, persoenlichesBudgetresultatEinnahmen.kinderAusbildungszulagen) &&
        Objects.equals(this.kinderAusbildungszulagenPartner, persoenlichesBudgetresultatEinnahmen.kinderAusbildungszulagenPartner) &&
        Objects.equals(this.kinderAusbildungszulagenKinder, persoenlichesBudgetresultatEinnahmen.kinderAusbildungszulagenKinder) &&
        Objects.equals(this.kinderAusbildungszulagenTotal, persoenlichesBudgetresultatEinnahmen.kinderAusbildungszulagenTotal) &&
        Objects.equals(this.unterhaltsbeitraege, persoenlichesBudgetresultatEinnahmen.unterhaltsbeitraege) &&
        Objects.equals(this.unterhaltsbeitraegePartner, persoenlichesBudgetresultatEinnahmen.unterhaltsbeitraegePartner) &&
        Objects.equals(this.unterhaltsbeitraegeKinder, persoenlichesBudgetresultatEinnahmen.unterhaltsbeitraegeKinder) &&
        Objects.equals(this.unterhaltsbeitraegeTotal, persoenlichesBudgetresultatEinnahmen.unterhaltsbeitraegeTotal) &&
        Objects.equals(this.eoLeistungen, persoenlichesBudgetresultatEinnahmen.eoLeistungen) &&
        Objects.equals(this.eoLeistungenPartner, persoenlichesBudgetresultatEinnahmen.eoLeistungenPartner) &&
        Objects.equals(this.eoLeistungenTotal, persoenlichesBudgetresultatEinnahmen.eoLeistungenTotal) &&
        Objects.equals(this.taggelderAHVIV, persoenlichesBudgetresultatEinnahmen.taggelderAHVIV) &&
        Objects.equals(this.taggelderAHVIVPartner, persoenlichesBudgetresultatEinnahmen.taggelderAHVIVPartner) &&
        Objects.equals(this.taggelderAHVIVTotal, persoenlichesBudgetresultatEinnahmen.taggelderAHVIVTotal) &&
        Objects.equals(this.renten, persoenlichesBudgetresultatEinnahmen.renten) &&
        Objects.equals(this.rentenPartner, persoenlichesBudgetresultatEinnahmen.rentenPartner) &&
        Objects.equals(this.rentenKinder, persoenlichesBudgetresultatEinnahmen.rentenKinder) &&
        Objects.equals(this.rentenTotal, persoenlichesBudgetresultatEinnahmen.rentenTotal) &&
        Objects.equals(this.ergaenzungsleistungen, persoenlichesBudgetresultatEinnahmen.ergaenzungsleistungen) &&
        Objects.equals(this.ergaenzungsleistungenPartner, persoenlichesBudgetresultatEinnahmen.ergaenzungsleistungenPartner) &&
        Objects.equals(this.ergaenzungsleistungenKinder, persoenlichesBudgetresultatEinnahmen.ergaenzungsleistungenKinder) &&
        Objects.equals(this.ergaenzungsleistungenTotal, persoenlichesBudgetresultatEinnahmen.ergaenzungsleistungenTotal) &&
        Objects.equals(this.beitraegeGemeindeInstitutionen, persoenlichesBudgetresultatEinnahmen.beitraegeGemeindeInstitutionen) &&
        Objects.equals(this.andereEinnahmen, persoenlichesBudgetresultatEinnahmen.andereEinnahmen) &&
        Objects.equals(this.andereEinnahmenPartner, persoenlichesBudgetresultatEinnahmen.andereEinnahmenPartner) &&
        Objects.equals(this.andereEinnahmenKinder, persoenlichesBudgetresultatEinnahmen.andereEinnahmenKinder) &&
        Objects.equals(this.andereEinnahmenTotal, persoenlichesBudgetresultatEinnahmen.andereEinnahmenTotal) &&
        Objects.equals(this.anrechenbaresVermoegen, persoenlichesBudgetresultatEinnahmen.anrechenbaresVermoegen) &&
        Objects.equals(this.steuerbaresVermoegen, persoenlichesBudgetresultatEinnahmen.steuerbaresVermoegen) &&
        Objects.equals(this.elterlicheLeistung, persoenlichesBudgetresultatEinnahmen.elterlicheLeistung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, nettoerwerbseinkommen, nettoerwerbseinkommenPartner, nettoerwerbseinkommenTotal, einnahmenBGSA, einnahmenBGSAPartner, einnahmenBGSATotal, kinderAusbildungszulagen, kinderAusbildungszulagenPartner, kinderAusbildungszulagenKinder, kinderAusbildungszulagenTotal, unterhaltsbeitraege, unterhaltsbeitraegePartner, unterhaltsbeitraegeKinder, unterhaltsbeitraegeTotal, eoLeistungen, eoLeistungenPartner, eoLeistungenTotal, taggelderAHVIV, taggelderAHVIVPartner, taggelderAHVIVTotal, renten, rentenPartner, rentenKinder, rentenTotal, ergaenzungsleistungen, ergaenzungsleistungenPartner, ergaenzungsleistungenKinder, ergaenzungsleistungenTotal, beitraegeGemeindeInstitutionen, andereEinnahmen, andereEinnahmenPartner, andereEinnahmenKinder, andereEinnahmenTotal, anrechenbaresVermoegen, steuerbaresVermoegen, elterlicheLeistung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatEinnahmenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    nettoerwerbseinkommenPartner: ").append(toIndentedString(nettoerwerbseinkommenPartner)).append("\n");
    sb.append("    nettoerwerbseinkommenTotal: ").append(toIndentedString(nettoerwerbseinkommenTotal)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    einnahmenBGSAPartner: ").append(toIndentedString(einnahmenBGSAPartner)).append("\n");
    sb.append("    einnahmenBGSATotal: ").append(toIndentedString(einnahmenBGSATotal)).append("\n");
    sb.append("    kinderAusbildungszulagen: ").append(toIndentedString(kinderAusbildungszulagen)).append("\n");
    sb.append("    kinderAusbildungszulagenPartner: ").append(toIndentedString(kinderAusbildungszulagenPartner)).append("\n");
    sb.append("    kinderAusbildungszulagenKinder: ").append(toIndentedString(kinderAusbildungszulagenKinder)).append("\n");
    sb.append("    kinderAusbildungszulagenTotal: ").append(toIndentedString(kinderAusbildungszulagenTotal)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    unterhaltsbeitraegePartner: ").append(toIndentedString(unterhaltsbeitraegePartner)).append("\n");
    sb.append("    unterhaltsbeitraegeKinder: ").append(toIndentedString(unterhaltsbeitraegeKinder)).append("\n");
    sb.append("    unterhaltsbeitraegeTotal: ").append(toIndentedString(unterhaltsbeitraegeTotal)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    eoLeistungenPartner: ").append(toIndentedString(eoLeistungenPartner)).append("\n");
    sb.append("    eoLeistungenTotal: ").append(toIndentedString(eoLeistungenTotal)).append("\n");
    sb.append("    taggelderAHVIV: ").append(toIndentedString(taggelderAHVIV)).append("\n");
    sb.append("    taggelderAHVIVPartner: ").append(toIndentedString(taggelderAHVIVPartner)).append("\n");
    sb.append("    taggelderAHVIVTotal: ").append(toIndentedString(taggelderAHVIVTotal)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    rentenPartner: ").append(toIndentedString(rentenPartner)).append("\n");
    sb.append("    rentenKinder: ").append(toIndentedString(rentenKinder)).append("\n");
    sb.append("    rentenTotal: ").append(toIndentedString(rentenTotal)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    ergaenzungsleistungenPartner: ").append(toIndentedString(ergaenzungsleistungenPartner)).append("\n");
    sb.append("    ergaenzungsleistungenKinder: ").append(toIndentedString(ergaenzungsleistungenKinder)).append("\n");
    sb.append("    ergaenzungsleistungenTotal: ").append(toIndentedString(ergaenzungsleistungenTotal)).append("\n");
    sb.append("    beitraegeGemeindeInstitutionen: ").append(toIndentedString(beitraegeGemeindeInstitutionen)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    andereEinnahmenPartner: ").append(toIndentedString(andereEinnahmenPartner)).append("\n");
    sb.append("    andereEinnahmenKinder: ").append(toIndentedString(andereEinnahmenKinder)).append("\n");
    sb.append("    andereEinnahmenTotal: ").append(toIndentedString(andereEinnahmenTotal)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
    sb.append("    elterlicheLeistung: ").append(toIndentedString(elterlicheLeistung)).append("\n");
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

