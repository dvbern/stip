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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PersoenlichesBudgetresultatEinnahmenDto  implements Serializable {
  private Integer total;
  private @Valid List<@Valid PersonValueItemDto> nettoerwerbseinkommen = new ArrayList<>();
  private Integer nettoerwerbseinkommenTotal;
  private @Valid List<@Valid PersonValueItemDto> einnahmenBGSA = new ArrayList<>();
  private Integer einnahmenBGSATotal;
  private @Valid List<@Valid PersonValueItemDto> kinderAusbildungszulagen = new ArrayList<>();
  private Integer kinderAusbildungszulagenTotal;
  private @Valid List<@Valid PersonValueItemDto> unterhaltsbeitraege = new ArrayList<>();
  private Integer unterhaltsbeitraegeTotal;
  private @Valid List<@Valid PersonValueItemDto> eoLeistungen = new ArrayList<>();
  private Integer eoLeistungenTotal;
  private @Valid List<@Valid PersonValueItemDto> taggelderAHVIV = new ArrayList<>();
  private Integer taggelderAHVIVTotal;
  private @Valid List<@Valid PersonValueItemDto> renten = new ArrayList<>();
  private Integer rentenTotal;
  private @Valid List<@Valid PersonValueItemDto> ergaenzungsleistungen = new ArrayList<>();
  private Integer ergaenzungsleistungenTotal;
  private Integer beitraegeGemeindeInstitutionen;
  private @Valid List<@Valid PersonValueItemDto> andereEinnahmen = new ArrayList<>();
  private Integer andereEinnahmenTotal;
  private Integer anrechenbaresVermoegen;
  private Integer steuerbaresVermoegen;
  private Integer elterlicheLeistung;

  protected PersoenlichesBudgetresultatEinnahmenDto(PersoenlichesBudgetresultatEinnahmenDtoBuilder<?, ?> b) {
    this.total = b.total;
    this.nettoerwerbseinkommen = b.nettoerwerbseinkommen;
    this.nettoerwerbseinkommenTotal = b.nettoerwerbseinkommenTotal;
    this.einnahmenBGSA = b.einnahmenBGSA;
    this.einnahmenBGSATotal = b.einnahmenBGSATotal;
    this.kinderAusbildungszulagen = b.kinderAusbildungszulagen;
    this.kinderAusbildungszulagenTotal = b.kinderAusbildungszulagenTotal;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.unterhaltsbeitraegeTotal = b.unterhaltsbeitraegeTotal;
    this.eoLeistungen = b.eoLeistungen;
    this.eoLeistungenTotal = b.eoLeistungenTotal;
    this.taggelderAHVIV = b.taggelderAHVIV;
    this.taggelderAHVIVTotal = b.taggelderAHVIVTotal;
    this.renten = b.renten;
    this.rentenTotal = b.rentenTotal;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.ergaenzungsleistungenTotal = b.ergaenzungsleistungenTotal;
    this.beitraegeGemeindeInstitutionen = b.beitraegeGemeindeInstitutionen;
    this.andereEinnahmen = b.andereEinnahmen;
    this.andereEinnahmenTotal = b.andereEinnahmenTotal;
    this.anrechenbaresVermoegen = b.anrechenbaresVermoegen;
    this.steuerbaresVermoegen = b.steuerbaresVermoegen;
    this.elterlicheLeistung = b.elterlicheLeistung;
  }

  public PersoenlichesBudgetresultatEinnahmenDto() {
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty(required = true, value = "total")
  @NotNull public Integer getTotal() {
    return total;
  }

  @JsonProperty(required = true, value = "total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto nettoerwerbseinkommen(List<@Valid PersonValueItemDto> nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

  
  @JsonProperty(required = true, value = "nettoerwerbseinkommen")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }

  @JsonProperty(required = true, value = "nettoerwerbseinkommen")
  public void setNettoerwerbseinkommen(List<@Valid PersonValueItemDto> nettoerwerbseinkommen) {
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

  
  @JsonProperty(required = true, value = "nettoerwerbseinkommenTotal")
  @NotNull public Integer getNettoerwerbseinkommenTotal() {
    return nettoerwerbseinkommenTotal;
  }

  @JsonProperty(required = true, value = "nettoerwerbseinkommenTotal")
  public void setNettoerwerbseinkommenTotal(Integer nettoerwerbseinkommenTotal) {
    this.nettoerwerbseinkommenTotal = nettoerwerbseinkommenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto einnahmenBGSA(List<@Valid PersonValueItemDto> einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty(required = true, value = "einnahmenBGSA")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty(required = true, value = "einnahmenBGSA")
  public void setEinnahmenBGSA(List<@Valid PersonValueItemDto> einnahmenBGSA) {
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

  
  @JsonProperty(required = true, value = "einnahmenBGSATotal")
  @NotNull public Integer getEinnahmenBGSATotal() {
    return einnahmenBGSATotal;
  }

  @JsonProperty(required = true, value = "einnahmenBGSATotal")
  public void setEinnahmenBGSATotal(Integer einnahmenBGSATotal) {
    this.einnahmenBGSATotal = einnahmenBGSATotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto kinderAusbildungszulagen(List<@Valid PersonValueItemDto> kinderAusbildungszulagen) {
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
    return this;
  }

  
  @JsonProperty(required = true, value = "kinderAusbildungszulagen")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getKinderAusbildungszulagen() {
    return kinderAusbildungszulagen;
  }

  @JsonProperty(required = true, value = "kinderAusbildungszulagen")
  public void setKinderAusbildungszulagen(List<@Valid PersonValueItemDto> kinderAusbildungszulagen) {
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

  
  @JsonProperty(required = true, value = "kinderAusbildungszulagenTotal")
  @NotNull public Integer getKinderAusbildungszulagenTotal() {
    return kinderAusbildungszulagenTotal;
  }

  @JsonProperty(required = true, value = "kinderAusbildungszulagenTotal")
  public void setKinderAusbildungszulagenTotal(Integer kinderAusbildungszulagenTotal) {
    this.kinderAusbildungszulagenTotal = kinderAusbildungszulagenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto unterhaltsbeitraege(List<@Valid PersonValueItemDto> unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty(required = true, value = "unterhaltsbeitraege")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty(required = true, value = "unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(List<@Valid PersonValueItemDto> unterhaltsbeitraege) {
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

  
  @JsonProperty(required = true, value = "unterhaltsbeitraegeTotal")
  @NotNull public Integer getUnterhaltsbeitraegeTotal() {
    return unterhaltsbeitraegeTotal;
  }

  @JsonProperty(required = true, value = "unterhaltsbeitraegeTotal")
  public void setUnterhaltsbeitraegeTotal(Integer unterhaltsbeitraegeTotal) {
    this.unterhaltsbeitraegeTotal = unterhaltsbeitraegeTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto eoLeistungen(List<@Valid PersonValueItemDto> eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty(required = true, value = "eoLeistungen")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty(required = true, value = "eoLeistungen")
  public void setEoLeistungen(List<@Valid PersonValueItemDto> eoLeistungen) {
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

  
  @JsonProperty(required = true, value = "eoLeistungenTotal")
  @NotNull public Integer getEoLeistungenTotal() {
    return eoLeistungenTotal;
  }

  @JsonProperty(required = true, value = "eoLeistungenTotal")
  public void setEoLeistungenTotal(Integer eoLeistungenTotal) {
    this.eoLeistungenTotal = eoLeistungenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto taggelderAHVIV(List<@Valid PersonValueItemDto> taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty(required = true, value = "taggelderAHVIV")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty(required = true, value = "taggelderAHVIV")
  public void setTaggelderAHVIV(List<@Valid PersonValueItemDto> taggelderAHVIV) {
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

  
  @JsonProperty(required = true, value = "taggelderAHVIVTotal")
  @NotNull public Integer getTaggelderAHVIVTotal() {
    return taggelderAHVIVTotal;
  }

  @JsonProperty(required = true, value = "taggelderAHVIVTotal")
  public void setTaggelderAHVIVTotal(Integer taggelderAHVIVTotal) {
    this.taggelderAHVIVTotal = taggelderAHVIVTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto renten(List<@Valid PersonValueItemDto> renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty(required = true, value = "renten")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getRenten() {
    return renten;
  }

  @JsonProperty(required = true, value = "renten")
  public void setRenten(List<@Valid PersonValueItemDto> renten) {
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

  
  @JsonProperty(required = true, value = "rentenTotal")
  @NotNull public Integer getRentenTotal() {
    return rentenTotal;
  }

  @JsonProperty(required = true, value = "rentenTotal")
  public void setRentenTotal(Integer rentenTotal) {
    this.rentenTotal = rentenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto ergaenzungsleistungen(List<@Valid PersonValueItemDto> ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty(required = true, value = "ergaenzungsleistungen")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty(required = true, value = "ergaenzungsleistungen")
  public void setErgaenzungsleistungen(List<@Valid PersonValueItemDto> ergaenzungsleistungen) {
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

  
  @JsonProperty(required = true, value = "ergaenzungsleistungenTotal")
  @NotNull public Integer getErgaenzungsleistungenTotal() {
    return ergaenzungsleistungenTotal;
  }

  @JsonProperty(required = true, value = "ergaenzungsleistungenTotal")
  public void setErgaenzungsleistungenTotal(Integer ergaenzungsleistungenTotal) {
    this.ergaenzungsleistungenTotal = ergaenzungsleistungenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto beitraegeGemeindeInstitutionen(Integer beitraegeGemeindeInstitutionen) {
    this.beitraegeGemeindeInstitutionen = beitraegeGemeindeInstitutionen;
    return this;
  }

  
  @JsonProperty(required = true, value = "beitraegeGemeindeInstitutionen")
  @NotNull public Integer getBeitraegeGemeindeInstitutionen() {
    return beitraegeGemeindeInstitutionen;
  }

  @JsonProperty(required = true, value = "beitraegeGemeindeInstitutionen")
  public void setBeitraegeGemeindeInstitutionen(Integer beitraegeGemeindeInstitutionen) {
    this.beitraegeGemeindeInstitutionen = beitraegeGemeindeInstitutionen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto andereEinnahmen(List<@Valid PersonValueItemDto> andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty(required = true, value = "andereEinnahmen")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty(required = true, value = "andereEinnahmen")
  public void setAndereEinnahmen(List<@Valid PersonValueItemDto> andereEinnahmen) {
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

  
  @JsonProperty(required = true, value = "andereEinnahmenTotal")
  @NotNull public Integer getAndereEinnahmenTotal() {
    return andereEinnahmenTotal;
  }

  @JsonProperty(required = true, value = "andereEinnahmenTotal")
  public void setAndereEinnahmenTotal(Integer andereEinnahmenTotal) {
    this.andereEinnahmenTotal = andereEinnahmenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "anrechenbaresVermoegen")
  @NotNull public Integer getAnrechenbaresVermoegen() {
    return anrechenbaresVermoegen;
  }

  @JsonProperty(required = true, value = "anrechenbaresVermoegen")
  public void setAnrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto steuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "steuerbaresVermoegen")
  @NotNull public Integer getSteuerbaresVermoegen() {
    return steuerbaresVermoegen;
  }

  @JsonProperty(required = true, value = "steuerbaresVermoegen")
  public void setSteuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatEinnahmenDto elterlicheLeistung(Integer elterlicheLeistung) {
    this.elterlicheLeistung = elterlicheLeistung;
    return this;
  }

  
  @JsonProperty(required = true, value = "elterlicheLeistung")
  @NotNull public Integer getElterlicheLeistung() {
    return elterlicheLeistung;
  }

  @JsonProperty(required = true, value = "elterlicheLeistung")
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
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static PersoenlichesBudgetresultatEinnahmenDtoBuilder<?, ?> builder() {
    return new PersoenlichesBudgetresultatEinnahmenDtoBuilderImpl();
  }

  private static final class PersoenlichesBudgetresultatEinnahmenDtoBuilderImpl extends PersoenlichesBudgetresultatEinnahmenDtoBuilder<PersoenlichesBudgetresultatEinnahmenDto, PersoenlichesBudgetresultatEinnahmenDtoBuilderImpl> {

    @Override
    protected PersoenlichesBudgetresultatEinnahmenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PersoenlichesBudgetresultatEinnahmenDto build() {
      return new PersoenlichesBudgetresultatEinnahmenDto(this);
    }
  }

  public static abstract class PersoenlichesBudgetresultatEinnahmenDtoBuilder<C extends PersoenlichesBudgetresultatEinnahmenDto, B extends PersoenlichesBudgetresultatEinnahmenDtoBuilder<C, B>>  {
    private Integer total;
    private List<PersonValueItemDto> nettoerwerbseinkommen = new ArrayList<>();
    private Integer nettoerwerbseinkommenTotal;
    private List<PersonValueItemDto> einnahmenBGSA = new ArrayList<>();
    private Integer einnahmenBGSATotal;
    private List<PersonValueItemDto> kinderAusbildungszulagen = new ArrayList<>();
    private Integer kinderAusbildungszulagenTotal;
    private List<PersonValueItemDto> unterhaltsbeitraege = new ArrayList<>();
    private Integer unterhaltsbeitraegeTotal;
    private List<PersonValueItemDto> eoLeistungen = new ArrayList<>();
    private Integer eoLeistungenTotal;
    private List<PersonValueItemDto> taggelderAHVIV = new ArrayList<>();
    private Integer taggelderAHVIVTotal;
    private List<PersonValueItemDto> renten = new ArrayList<>();
    private Integer rentenTotal;
    private List<PersonValueItemDto> ergaenzungsleistungen = new ArrayList<>();
    private Integer ergaenzungsleistungenTotal;
    private Integer beitraegeGemeindeInstitutionen;
    private List<PersonValueItemDto> andereEinnahmen = new ArrayList<>();
    private Integer andereEinnahmenTotal;
    private Integer anrechenbaresVermoegen;
    private Integer steuerbaresVermoegen;
    private Integer elterlicheLeistung;
    protected abstract B self();

    public abstract C build();

    public B total(Integer total) {
      this.total = total;
      return self();
    }
    public B nettoerwerbseinkommen(List<PersonValueItemDto> nettoerwerbseinkommen) {
      this.nettoerwerbseinkommen = nettoerwerbseinkommen;
      return self();
    }
    public B nettoerwerbseinkommenTotal(Integer nettoerwerbseinkommenTotal) {
      this.nettoerwerbseinkommenTotal = nettoerwerbseinkommenTotal;
      return self();
    }
    public B einnahmenBGSA(List<PersonValueItemDto> einnahmenBGSA) {
      this.einnahmenBGSA = einnahmenBGSA;
      return self();
    }
    public B einnahmenBGSATotal(Integer einnahmenBGSATotal) {
      this.einnahmenBGSATotal = einnahmenBGSATotal;
      return self();
    }
    public B kinderAusbildungszulagen(List<PersonValueItemDto> kinderAusbildungszulagen) {
      this.kinderAusbildungszulagen = kinderAusbildungszulagen;
      return self();
    }
    public B kinderAusbildungszulagenTotal(Integer kinderAusbildungszulagenTotal) {
      this.kinderAusbildungszulagenTotal = kinderAusbildungszulagenTotal;
      return self();
    }
    public B unterhaltsbeitraege(List<PersonValueItemDto> unterhaltsbeitraege) {
      this.unterhaltsbeitraege = unterhaltsbeitraege;
      return self();
    }
    public B unterhaltsbeitraegeTotal(Integer unterhaltsbeitraegeTotal) {
      this.unterhaltsbeitraegeTotal = unterhaltsbeitraegeTotal;
      return self();
    }
    public B eoLeistungen(List<PersonValueItemDto> eoLeistungen) {
      this.eoLeistungen = eoLeistungen;
      return self();
    }
    public B eoLeistungenTotal(Integer eoLeistungenTotal) {
      this.eoLeistungenTotal = eoLeistungenTotal;
      return self();
    }
    public B taggelderAHVIV(List<PersonValueItemDto> taggelderAHVIV) {
      this.taggelderAHVIV = taggelderAHVIV;
      return self();
    }
    public B taggelderAHVIVTotal(Integer taggelderAHVIVTotal) {
      this.taggelderAHVIVTotal = taggelderAHVIVTotal;
      return self();
    }
    public B renten(List<PersonValueItemDto> renten) {
      this.renten = renten;
      return self();
    }
    public B rentenTotal(Integer rentenTotal) {
      this.rentenTotal = rentenTotal;
      return self();
    }
    public B ergaenzungsleistungen(List<PersonValueItemDto> ergaenzungsleistungen) {
      this.ergaenzungsleistungen = ergaenzungsleistungen;
      return self();
    }
    public B ergaenzungsleistungenTotal(Integer ergaenzungsleistungenTotal) {
      this.ergaenzungsleistungenTotal = ergaenzungsleistungenTotal;
      return self();
    }
    public B beitraegeGemeindeInstitutionen(Integer beitraegeGemeindeInstitutionen) {
      this.beitraegeGemeindeInstitutionen = beitraegeGemeindeInstitutionen;
      return self();
    }
    public B andereEinnahmen(List<PersonValueItemDto> andereEinnahmen) {
      this.andereEinnahmen = andereEinnahmen;
      return self();
    }
    public B andereEinnahmenTotal(Integer andereEinnahmenTotal) {
      this.andereEinnahmenTotal = andereEinnahmenTotal;
      return self();
    }
    public B anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
      this.anrechenbaresVermoegen = anrechenbaresVermoegen;
      return self();
    }
    public B steuerbaresVermoegen(Integer steuerbaresVermoegen) {
      this.steuerbaresVermoegen = steuerbaresVermoegen;
      return self();
    }
    public B elterlicheLeistung(Integer elterlicheLeistung) {
      this.elterlicheLeistung = elterlicheLeistung;
      return self();
    }
  }
}
