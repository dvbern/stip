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
  private @Valid List<PersonValueItemDto> nettoerwerbseinkommen = new ArrayList<>();
  private @Valid Integer nettoerwerbseinkommenTotal;
  private @Valid List<PersonValueItemDto> einnahmenBGSA = new ArrayList<>();
  private @Valid Integer einnahmenBGSATotal;
  private @Valid List<PersonValueItemDto> kinderAusbildungszulagen = new ArrayList<>();
  private @Valid Integer kinderAusbildungszulagenTotal;
  private @Valid List<PersonValueItemDto> unterhaltsbeitraege = new ArrayList<>();
  private @Valid Integer unterhaltsbeitraegeTotal;
  private @Valid List<PersonValueItemDto> eoLeistungen = new ArrayList<>();
  private @Valid Integer eoLeistungenTotal;
  private @Valid List<PersonValueItemDto> taggelderAHVIV = new ArrayList<>();
  private @Valid Integer taggelderAHVIVTotal;
  private @Valid List<PersonValueItemDto> renten = new ArrayList<>();
  private @Valid Integer rentenTotal;
  private @Valid List<PersonValueItemDto> ergaenzungsleistungen = new ArrayList<>();
  private @Valid Integer ergaenzungsleistungenTotal;
  private @Valid Integer beitraegeGemeindeInstitutionen;
  private @Valid List<PersonValueItemDto> andereEinnahmen = new ArrayList<>();
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
  public PersoenlichesBudgetresultatEinnahmenDto nettoerwerbseinkommen(List<PersonValueItemDto> nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommen")
  @NotNull
  public List<PersonValueItemDto> getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }

  @JsonProperty("nettoerwerbseinkommen")
  public void setNettoerwerbseinkommen(List<PersonValueItemDto> nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addNettoerwerbseinkommenItem(PersonValueItemDto nettoerwerbseinkommenItem) {
    if (this.nettoerwerbseinkommen == null) {
      this.nettoerwerbseinkommen = new ArrayList<>();
    }

    this.nettoerwerbseinkommen.add(nettoerwerbseinkommenItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeNettoerwerbseinkommenItem(PersonValueItemDto nettoerwerbseinkommenItem) {
    if (nettoerwerbseinkommenItem != null && this.nettoerwerbseinkommen != null) {
      this.nettoerwerbseinkommen.remove(nettoerwerbseinkommenItem);
    }

    return this;
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
  public PersoenlichesBudgetresultatEinnahmenDto einnahmenBGSA(List<PersonValueItemDto> einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  @NotNull
  public List<PersonValueItemDto> getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(List<PersonValueItemDto> einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addEinnahmenBGSAItem(PersonValueItemDto einnahmenBGSAItem) {
    if (this.einnahmenBGSA == null) {
      this.einnahmenBGSA = new ArrayList<>();
    }

    this.einnahmenBGSA.add(einnahmenBGSAItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeEinnahmenBGSAItem(PersonValueItemDto einnahmenBGSAItem) {
    if (einnahmenBGSAItem != null && this.einnahmenBGSA != null) {
      this.einnahmenBGSA.remove(einnahmenBGSAItem);
    }

    return this;
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
  public PersoenlichesBudgetresultatEinnahmenDto kinderAusbildungszulagen(List<PersonValueItemDto> kinderAusbildungszulagen) {
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
    return this;
  }

  
  @JsonProperty("kinderAusbildungszulagen")
  @NotNull
  public List<PersonValueItemDto> getKinderAusbildungszulagen() {
    return kinderAusbildungszulagen;
  }

  @JsonProperty("kinderAusbildungszulagen")
  public void setKinderAusbildungszulagen(List<PersonValueItemDto> kinderAusbildungszulagen) {
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addKinderAusbildungszulagenItem(PersonValueItemDto kinderAusbildungszulagenItem) {
    if (this.kinderAusbildungszulagen == null) {
      this.kinderAusbildungszulagen = new ArrayList<>();
    }

    this.kinderAusbildungszulagen.add(kinderAusbildungszulagenItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeKinderAusbildungszulagenItem(PersonValueItemDto kinderAusbildungszulagenItem) {
    if (kinderAusbildungszulagenItem != null && this.kinderAusbildungszulagen != null) {
      this.kinderAusbildungszulagen.remove(kinderAusbildungszulagenItem);
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
  public PersoenlichesBudgetresultatEinnahmenDto unterhaltsbeitraege(List<PersonValueItemDto> unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  @NotNull
  public List<PersonValueItemDto> getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(List<PersonValueItemDto> unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addUnterhaltsbeitraegeItem(PersonValueItemDto unterhaltsbeitraegeItem) {
    if (this.unterhaltsbeitraege == null) {
      this.unterhaltsbeitraege = new ArrayList<>();
    }

    this.unterhaltsbeitraege.add(unterhaltsbeitraegeItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeUnterhaltsbeitraegeItem(PersonValueItemDto unterhaltsbeitraegeItem) {
    if (unterhaltsbeitraegeItem != null && this.unterhaltsbeitraege != null) {
      this.unterhaltsbeitraege.remove(unterhaltsbeitraegeItem);
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
  public PersoenlichesBudgetresultatEinnahmenDto eoLeistungen(List<PersonValueItemDto> eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  @NotNull
  public List<PersonValueItemDto> getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(List<PersonValueItemDto> eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addEoLeistungenItem(PersonValueItemDto eoLeistungenItem) {
    if (this.eoLeistungen == null) {
      this.eoLeistungen = new ArrayList<>();
    }

    this.eoLeistungen.add(eoLeistungenItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeEoLeistungenItem(PersonValueItemDto eoLeistungenItem) {
    if (eoLeistungenItem != null && this.eoLeistungen != null) {
      this.eoLeistungen.remove(eoLeistungenItem);
    }

    return this;
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
  public PersoenlichesBudgetresultatEinnahmenDto taggelderAHVIV(List<PersonValueItemDto> taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty("taggelderAHVIV")
  @NotNull
  public List<PersonValueItemDto> getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty("taggelderAHVIV")
  public void setTaggelderAHVIV(List<PersonValueItemDto> taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addTaggelderAHVIVItem(PersonValueItemDto taggelderAHVIVItem) {
    if (this.taggelderAHVIV == null) {
      this.taggelderAHVIV = new ArrayList<>();
    }

    this.taggelderAHVIV.add(taggelderAHVIVItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeTaggelderAHVIVItem(PersonValueItemDto taggelderAHVIVItem) {
    if (taggelderAHVIVItem != null && this.taggelderAHVIV != null) {
      this.taggelderAHVIV.remove(taggelderAHVIVItem);
    }

    return this;
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
  public PersoenlichesBudgetresultatEinnahmenDto renten(List<PersonValueItemDto> renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  @NotNull
  public List<PersonValueItemDto> getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(List<PersonValueItemDto> renten) {
    this.renten = renten;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addRentenItem(PersonValueItemDto rentenItem) {
    if (this.renten == null) {
      this.renten = new ArrayList<>();
    }

    this.renten.add(rentenItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeRentenItem(PersonValueItemDto rentenItem) {
    if (rentenItem != null && this.renten != null) {
      this.renten.remove(rentenItem);
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
  public PersoenlichesBudgetresultatEinnahmenDto ergaenzungsleistungen(List<PersonValueItemDto> ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public List<PersonValueItemDto> getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(List<PersonValueItemDto> ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addErgaenzungsleistungenItem(PersonValueItemDto ergaenzungsleistungenItem) {
    if (this.ergaenzungsleistungen == null) {
      this.ergaenzungsleistungen = new ArrayList<>();
    }

    this.ergaenzungsleistungen.add(ergaenzungsleistungenItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeErgaenzungsleistungenItem(PersonValueItemDto ergaenzungsleistungenItem) {
    if (ergaenzungsleistungenItem != null && this.ergaenzungsleistungen != null) {
      this.ergaenzungsleistungen.remove(ergaenzungsleistungenItem);
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
  public PersoenlichesBudgetresultatEinnahmenDto andereEinnahmen(List<PersonValueItemDto> andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  @NotNull
  public List<PersonValueItemDto> getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(List<PersonValueItemDto> andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  public PersoenlichesBudgetresultatEinnahmenDto addAndereEinnahmenItem(PersonValueItemDto andereEinnahmenItem) {
    if (this.andereEinnahmen == null) {
      this.andereEinnahmen = new ArrayList<>();
    }

    this.andereEinnahmen.add(andereEinnahmenItem);
    return this;
  }

  public PersoenlichesBudgetresultatEinnahmenDto removeAndereEinnahmenItem(PersonValueItemDto andereEinnahmenItem) {
    if (andereEinnahmenItem != null && this.andereEinnahmen != null) {
      this.andereEinnahmen.remove(andereEinnahmenItem);
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
        Objects.equals(this.nettoerwerbseinkommenTotal, persoenlichesBudgetresultatEinnahmen.nettoerwerbseinkommenTotal) &&
        Objects.equals(this.einnahmenBGSA, persoenlichesBudgetresultatEinnahmen.einnahmenBGSA) &&
        Objects.equals(this.einnahmenBGSATotal, persoenlichesBudgetresultatEinnahmen.einnahmenBGSATotal) &&
        Objects.equals(this.kinderAusbildungszulagen, persoenlichesBudgetresultatEinnahmen.kinderAusbildungszulagen) &&
        Objects.equals(this.kinderAusbildungszulagenTotal, persoenlichesBudgetresultatEinnahmen.kinderAusbildungszulagenTotal) &&
        Objects.equals(this.unterhaltsbeitraege, persoenlichesBudgetresultatEinnahmen.unterhaltsbeitraege) &&
        Objects.equals(this.unterhaltsbeitraegeTotal, persoenlichesBudgetresultatEinnahmen.unterhaltsbeitraegeTotal) &&
        Objects.equals(this.eoLeistungen, persoenlichesBudgetresultatEinnahmen.eoLeistungen) &&
        Objects.equals(this.eoLeistungenTotal, persoenlichesBudgetresultatEinnahmen.eoLeistungenTotal) &&
        Objects.equals(this.taggelderAHVIV, persoenlichesBudgetresultatEinnahmen.taggelderAHVIV) &&
        Objects.equals(this.taggelderAHVIVTotal, persoenlichesBudgetresultatEinnahmen.taggelderAHVIVTotal) &&
        Objects.equals(this.renten, persoenlichesBudgetresultatEinnahmen.renten) &&
        Objects.equals(this.rentenTotal, persoenlichesBudgetresultatEinnahmen.rentenTotal) &&
        Objects.equals(this.ergaenzungsleistungen, persoenlichesBudgetresultatEinnahmen.ergaenzungsleistungen) &&
        Objects.equals(this.ergaenzungsleistungenTotal, persoenlichesBudgetresultatEinnahmen.ergaenzungsleistungenTotal) &&
        Objects.equals(this.beitraegeGemeindeInstitutionen, persoenlichesBudgetresultatEinnahmen.beitraegeGemeindeInstitutionen) &&
        Objects.equals(this.andereEinnahmen, persoenlichesBudgetresultatEinnahmen.andereEinnahmen) &&
        Objects.equals(this.andereEinnahmenTotal, persoenlichesBudgetresultatEinnahmen.andereEinnahmenTotal) &&
        Objects.equals(this.anrechenbaresVermoegen, persoenlichesBudgetresultatEinnahmen.anrechenbaresVermoegen) &&
        Objects.equals(this.steuerbaresVermoegen, persoenlichesBudgetresultatEinnahmen.steuerbaresVermoegen) &&
        Objects.equals(this.elterlicheLeistung, persoenlichesBudgetresultatEinnahmen.elterlicheLeistung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, nettoerwerbseinkommen, nettoerwerbseinkommenTotal, einnahmenBGSA, einnahmenBGSATotal, kinderAusbildungszulagen, kinderAusbildungszulagenTotal, unterhaltsbeitraege, unterhaltsbeitraegeTotal, eoLeistungen, eoLeistungenTotal, taggelderAHVIV, taggelderAHVIVTotal, renten, rentenTotal, ergaenzungsleistungen, ergaenzungsleistungenTotal, beitraegeGemeindeInstitutionen, andereEinnahmen, andereEinnahmenTotal, anrechenbaresVermoegen, steuerbaresVermoegen, elterlicheLeistung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatEinnahmenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    nettoerwerbseinkommenTotal: ").append(toIndentedString(nettoerwerbseinkommenTotal)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    einnahmenBGSATotal: ").append(toIndentedString(einnahmenBGSATotal)).append("\n");
    sb.append("    kinderAusbildungszulagen: ").append(toIndentedString(kinderAusbildungszulagen)).append("\n");
    sb.append("    kinderAusbildungszulagenTotal: ").append(toIndentedString(kinderAusbildungszulagenTotal)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    unterhaltsbeitraegeTotal: ").append(toIndentedString(unterhaltsbeitraegeTotal)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    eoLeistungenTotal: ").append(toIndentedString(eoLeistungenTotal)).append("\n");
    sb.append("    taggelderAHVIV: ").append(toIndentedString(taggelderAHVIV)).append("\n");
    sb.append("    taggelderAHVIVTotal: ").append(toIndentedString(taggelderAHVIVTotal)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    rentenTotal: ").append(toIndentedString(rentenTotal)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    ergaenzungsleistungenTotal: ").append(toIndentedString(ergaenzungsleistungenTotal)).append("\n");
    sb.append("    beitraegeGemeindeInstitutionen: ").append(toIndentedString(beitraegeGemeindeInstitutionen)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
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

