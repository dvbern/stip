package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.KindIntegerValueItemDto;
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

/**
 * Persoenliche Budget daten fuer und von der Berechnung
 **/

@JsonTypeName("PersoenlichesBudgetresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PersoenlichesBudgetresultatDto  implements Serializable {
  private @Valid Integer anzahlPersonenImHaushalt;
  private @Valid Boolean eigenerHaushalt;
  private @Valid Integer anteilFamilienbudget;
  private @Valid Integer einkommen;
  private @Valid Integer alimente;
  private @Valid Integer leistungenEO;
  private @Valid Integer rente;
  private @Valid Integer kinderAusbildungszulagen;
  private @Valid Integer kinderAusbildungszulagenPartner;
  private @Valid List<Integer> kinderAusbildungszulagenKinder = new ArrayList<>();
  private @Valid Integer kinderAusbildungszulagenTotal;
  private @Valid List<Integer> kinderUnterhaltsbeitraege = new ArrayList<>();
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer gemeindeInstitutionen;
  private @Valid Integer steuerbaresVermoegen;
  private @Valid Integer anrechenbaresVermoegen;
  private @Valid Integer einkommenPartner;
  private @Valid Integer einkommenTotal;
  private @Valid Integer einnahmenPersoenlichesBudget;
  private @Valid Integer anteilLebenshaltungskosten;
  private @Valid Integer grundbedarf;
  private @Valid Integer wohnkosten;
  private @Valid Integer medizinischeGrundversorgung;
  private @Valid Integer steuern;
  private @Valid Integer steuernPartner;
  private @Valid Integer fahrkosten;
  private @Valid Integer fahrkostenPartner;
  private @Valid Integer verpflegung;
  private @Valid Integer verpflegungPartner;
  private @Valid Integer fremdbetreuung;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer ausgabenPersoenlichesBudget;
  private @Valid Integer persoenlichesbudgetBerechnet;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer totalVorTeilung;
  private @Valid Integer alimentePartner;
  private @Valid Integer alimenteTotal;
  private @Valid Integer leistungenEOPartner;
  private @Valid Integer leistungenEOTotal;
  private @Valid Integer rentePartner;
  private @Valid Integer renteTotal;
  private @Valid Integer kinderUnterhaltsbeitraegeTotal;
  private @Valid List<Integer> kinderRenten;
  private @Valid Integer kinderRentenTotal;
  private @Valid List<KindIntegerValueItemDto> ausbildungszulagenKinder;
  private @Valid Integer ausbildungszulagenKinderTotal;
  private @Valid List<KindIntegerValueItemDto> unterhaltsbeitraegeKinder;
  private @Valid Integer unterhaltsbeitraegeKinderTotal;
  private @Valid List<KindIntegerValueItemDto> rentenKinder;
  private @Valid Integer rentenKinderTotal;
  private @Valid Integer ergaenzungsleistungenPartner;
  private @Valid List<KindIntegerValueItemDto> ergaenzungsleistungenKinder;
  private @Valid Integer ergaenzungsleistungenTotal;
  private @Valid Integer steuernTotal;
  private @Valid Integer fahrkostenTotal;
  private @Valid Integer verpflegungTotal;
  private @Valid Integer elternbeitrag1;
  private @Valid Integer elternbeitrag2;
  private @Valid Integer einnahmenBGSAPartner;
  private @Valid Integer einnahmenBGSATotal;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer andereEinnahmenPartner;
  private @Valid List<KindIntegerValueItemDto> andereEinnahmenKinder;
  private @Valid Integer andereEinnahmenTotal;
  private @Valid Integer taggelder;
  private @Valid Integer taggelderPartner;
  private @Valid Integer taggelderTotal;

  /**
   **/
  public PersoenlichesBudgetresultatDto anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
    return this;
  }

  
  @JsonProperty("anzahlPersonenImHaushalt")
  @NotNull
  public Integer getAnzahlPersonenImHaushalt() {
    return anzahlPersonenImHaushalt;
  }

  @JsonProperty("anzahlPersonenImHaushalt")
  public void setAnzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto eigenerHaushalt(Boolean eigenerHaushalt) {
    this.eigenerHaushalt = eigenerHaushalt;
    return this;
  }

  
  @JsonProperty("eigenerHaushalt")
  @NotNull
  public Boolean getEigenerHaushalt() {
    return eigenerHaushalt;
  }

  @JsonProperty("eigenerHaushalt")
  public void setEigenerHaushalt(Boolean eigenerHaushalt) {
    this.eigenerHaushalt = eigenerHaushalt;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto anteilFamilienbudget(Integer anteilFamilienbudget) {
    this.anteilFamilienbudget = anteilFamilienbudget;
    return this;
  }

  
  @JsonProperty("anteilFamilienbudget")
  @NotNull
  public Integer getAnteilFamilienbudget() {
    return anteilFamilienbudget;
  }

  @JsonProperty("anteilFamilienbudget")
  public void setAnteilFamilienbudget(Integer anteilFamilienbudget) {
    this.anteilFamilienbudget = anteilFamilienbudget;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto einkommen(Integer einkommen) {
    this.einkommen = einkommen;
    return this;
  }

  
  @JsonProperty("einkommen")
  @NotNull
  public Integer getEinkommen() {
    return einkommen;
  }

  @JsonProperty("einkommen")
  public void setEinkommen(Integer einkommen) {
    this.einkommen = einkommen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto alimente(Integer alimente) {
    this.alimente = alimente;
    return this;
  }

  
  @JsonProperty("alimente")
  @NotNull
  public Integer getAlimente() {
    return alimente;
  }

  @JsonProperty("alimente")
  public void setAlimente(Integer alimente) {
    this.alimente = alimente;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto leistungenEO(Integer leistungenEO) {
    this.leistungenEO = leistungenEO;
    return this;
  }

  
  @JsonProperty("leistungenEO")
  @NotNull
  public Integer getLeistungenEO() {
    return leistungenEO;
  }

  @JsonProperty("leistungenEO")
  public void setLeistungenEO(Integer leistungenEO) {
    this.leistungenEO = leistungenEO;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto rente(Integer rente) {
    this.rente = rente;
    return this;
  }

  
  @JsonProperty("rente")
  @NotNull
  public Integer getRente() {
    return rente;
  }

  @JsonProperty("rente")
  public void setRente(Integer rente) {
    this.rente = rente;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto kinderAusbildungszulagen(Integer kinderAusbildungszulagen) {
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
  public PersoenlichesBudgetresultatDto kinderAusbildungszulagenPartner(Integer kinderAusbildungszulagenPartner) {
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
  public PersoenlichesBudgetresultatDto kinderAusbildungszulagenKinder(List<Integer> kinderAusbildungszulagenKinder) {
    this.kinderAusbildungszulagenKinder = kinderAusbildungszulagenKinder;
    return this;
  }

  
  @JsonProperty("kinderAusbildungszulagenKinder")
  @NotNull
  public List<Integer> getKinderAusbildungszulagenKinder() {
    return kinderAusbildungszulagenKinder;
  }

  @JsonProperty("kinderAusbildungszulagenKinder")
  public void setKinderAusbildungszulagenKinder(List<Integer> kinderAusbildungszulagenKinder) {
    this.kinderAusbildungszulagenKinder = kinderAusbildungszulagenKinder;
  }

  public PersoenlichesBudgetresultatDto addKinderAusbildungszulagenKinderItem(Integer kinderAusbildungszulagenKinderItem) {
    if (this.kinderAusbildungszulagenKinder == null) {
      this.kinderAusbildungszulagenKinder = new ArrayList<>();
    }

    this.kinderAusbildungszulagenKinder.add(kinderAusbildungszulagenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeKinderAusbildungszulagenKinderItem(Integer kinderAusbildungszulagenKinderItem) {
    if (kinderAusbildungszulagenKinderItem != null && this.kinderAusbildungszulagenKinder != null) {
      this.kinderAusbildungszulagenKinder.remove(kinderAusbildungszulagenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto kinderAusbildungszulagenTotal(Integer kinderAusbildungszulagenTotal) {
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
  public PersoenlichesBudgetresultatDto kinderUnterhaltsbeitraege(List<Integer> kinderUnterhaltsbeitraege) {
    this.kinderUnterhaltsbeitraege = kinderUnterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("kinderUnterhaltsbeitraege")
  @NotNull
  public List<Integer> getKinderUnterhaltsbeitraege() {
    return kinderUnterhaltsbeitraege;
  }

  @JsonProperty("kinderUnterhaltsbeitraege")
  public void setKinderUnterhaltsbeitraege(List<Integer> kinderUnterhaltsbeitraege) {
    this.kinderUnterhaltsbeitraege = kinderUnterhaltsbeitraege;
  }

  public PersoenlichesBudgetresultatDto addKinderUnterhaltsbeitraegeItem(Integer kinderUnterhaltsbeitraegeItem) {
    if (this.kinderUnterhaltsbeitraege == null) {
      this.kinderUnterhaltsbeitraege = new ArrayList<>();
    }

    this.kinderUnterhaltsbeitraege.add(kinderUnterhaltsbeitraegeItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeKinderUnterhaltsbeitraegeItem(Integer kinderUnterhaltsbeitraegeItem) {
    if (kinderUnterhaltsbeitraegeItem != null && this.kinderUnterhaltsbeitraege != null) {
      this.kinderUnterhaltsbeitraege.remove(kinderUnterhaltsbeitraegeItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
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
  public PersoenlichesBudgetresultatDto gemeindeInstitutionen(Integer gemeindeInstitutionen) {
    this.gemeindeInstitutionen = gemeindeInstitutionen;
    return this;
  }

  
  @JsonProperty("gemeindeInstitutionen")
  @NotNull
  public Integer getGemeindeInstitutionen() {
    return gemeindeInstitutionen;
  }

  @JsonProperty("gemeindeInstitutionen")
  public void setGemeindeInstitutionen(Integer gemeindeInstitutionen) {
    this.gemeindeInstitutionen = gemeindeInstitutionen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto steuerbaresVermoegen(Integer steuerbaresVermoegen) {
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
  public PersoenlichesBudgetresultatDto anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
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
  public PersoenlichesBudgetresultatDto einkommenPartner(Integer einkommenPartner) {
    this.einkommenPartner = einkommenPartner;
    return this;
  }

  
  @JsonProperty("einkommenPartner")
  @NotNull
  public Integer getEinkommenPartner() {
    return einkommenPartner;
  }

  @JsonProperty("einkommenPartner")
  public void setEinkommenPartner(Integer einkommenPartner) {
    this.einkommenPartner = einkommenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto einkommenTotal(Integer einkommenTotal) {
    this.einkommenTotal = einkommenTotal;
    return this;
  }

  
  @JsonProperty("einkommenTotal")
  @NotNull
  public Integer getEinkommenTotal() {
    return einkommenTotal;
  }

  @JsonProperty("einkommenTotal")
  public void setEinkommenTotal(Integer einkommenTotal) {
    this.einkommenTotal = einkommenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto einnahmenPersoenlichesBudget(Integer einnahmenPersoenlichesBudget) {
    this.einnahmenPersoenlichesBudget = einnahmenPersoenlichesBudget;
    return this;
  }

  
  @JsonProperty("einnahmenPersoenlichesBudget")
  @NotNull
  public Integer getEinnahmenPersoenlichesBudget() {
    return einnahmenPersoenlichesBudget;
  }

  @JsonProperty("einnahmenPersoenlichesBudget")
  public void setEinnahmenPersoenlichesBudget(Integer einnahmenPersoenlichesBudget) {
    this.einnahmenPersoenlichesBudget = einnahmenPersoenlichesBudget;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto anteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
    return this;
  }

  
  @JsonProperty("anteilLebenshaltungskosten")
  @NotNull
  public Integer getAnteilLebenshaltungskosten() {
    return anteilLebenshaltungskosten;
  }

  @JsonProperty("anteilLebenshaltungskosten")
  public void setAnteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto grundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
    return this;
  }

  
  @JsonProperty("grundbedarf")
  @NotNull
  public Integer getGrundbedarf() {
    return grundbedarf;
  }

  @JsonProperty("grundbedarf")
  public void setGrundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  @NotNull
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
    return this;
  }

  
  @JsonProperty("medizinischeGrundversorgung")
  @NotNull
  public Integer getMedizinischeGrundversorgung() {
    return medizinischeGrundversorgung;
  }

  @JsonProperty("medizinischeGrundversorgung")
  public void setMedizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto steuern(Integer steuern) {
    this.steuern = steuern;
    return this;
  }

  
  @JsonProperty("steuern")
  @NotNull
  public Integer getSteuern() {
    return steuern;
  }

  @JsonProperty("steuern")
  public void setSteuern(Integer steuern) {
    this.steuern = steuern;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto steuernPartner(Integer steuernPartner) {
    this.steuernPartner = steuernPartner;
    return this;
  }

  
  @JsonProperty("steuernPartner")
  @NotNull
  public Integer getSteuernPartner() {
    return steuernPartner;
  }

  @JsonProperty("steuernPartner")
  public void setSteuernPartner(Integer steuernPartner) {
    this.steuernPartner = steuernPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto fahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

  
  @JsonProperty("fahrkostenPartner")
  @NotNull
  public Integer getFahrkostenPartner() {
    return fahrkostenPartner;
  }

  @JsonProperty("fahrkostenPartner")
  public void setFahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto verpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
    return this;
  }

  
  @JsonProperty("verpflegung")
  @NotNull
  public Integer getVerpflegung() {
    return verpflegung;
  }

  @JsonProperty("verpflegung")
  public void setVerpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto verpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
    return this;
  }

  
  @JsonProperty("verpflegungPartner")
  @NotNull
  public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }

  @JsonProperty("verpflegungPartner")
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto fremdbetreuung(Integer fremdbetreuung) {
    this.fremdbetreuung = fremdbetreuung;
    return this;
  }

  
  @JsonProperty("fremdbetreuung")
  @NotNull
  public Integer getFremdbetreuung() {
    return fremdbetreuung;
  }

  @JsonProperty("fremdbetreuung")
  public void setFremdbetreuung(Integer fremdbetreuung) {
    this.fremdbetreuung = fremdbetreuung;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto ausbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
    return this;
  }

  
  @JsonProperty("ausbildungskosten")
  @NotNull
  public Integer getAusbildungskosten() {
    return ausbildungskosten;
  }

  @JsonProperty("ausbildungskosten")
  public void setAusbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto ausgabenPersoenlichesBudget(Integer ausgabenPersoenlichesBudget) {
    this.ausgabenPersoenlichesBudget = ausgabenPersoenlichesBudget;
    return this;
  }

  
  @JsonProperty("ausgabenPersoenlichesBudget")
  @NotNull
  public Integer getAusgabenPersoenlichesBudget() {
    return ausgabenPersoenlichesBudget;
  }

  @JsonProperty("ausgabenPersoenlichesBudget")
  public void setAusgabenPersoenlichesBudget(Integer ausgabenPersoenlichesBudget) {
    this.ausgabenPersoenlichesBudget = ausgabenPersoenlichesBudget;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto persoenlichesbudgetBerechnet(Integer persoenlichesbudgetBerechnet) {
    this.persoenlichesbudgetBerechnet = persoenlichesbudgetBerechnet;
    return this;
  }

  
  @JsonProperty("persoenlichesbudgetBerechnet")
  @NotNull
  public Integer getPersoenlichesbudgetBerechnet() {
    return persoenlichesbudgetBerechnet;
  }

  @JsonProperty("persoenlichesbudgetBerechnet")
  public void setPersoenlichesbudgetBerechnet(Integer persoenlichesbudgetBerechnet) {
    this.persoenlichesbudgetBerechnet = persoenlichesbudgetBerechnet;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto einnahmenBGSA(Integer einnahmenBGSA) {
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
  public PersoenlichesBudgetresultatDto totalVorTeilung(Integer totalVorTeilung) {
    this.totalVorTeilung = totalVorTeilung;
    return this;
  }

  
  @JsonProperty("totalVorTeilung")
  public Integer getTotalVorTeilung() {
    return totalVorTeilung;
  }

  @JsonProperty("totalVorTeilung")
  public void setTotalVorTeilung(Integer totalVorTeilung) {
    this.totalVorTeilung = totalVorTeilung;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto alimentePartner(Integer alimentePartner) {
    this.alimentePartner = alimentePartner;
    return this;
  }

  
  @JsonProperty("alimentePartner")
  public Integer getAlimentePartner() {
    return alimentePartner;
  }

  @JsonProperty("alimentePartner")
  public void setAlimentePartner(Integer alimentePartner) {
    this.alimentePartner = alimentePartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto alimenteTotal(Integer alimenteTotal) {
    this.alimenteTotal = alimenteTotal;
    return this;
  }

  
  @JsonProperty("alimenteTotal")
  public Integer getAlimenteTotal() {
    return alimenteTotal;
  }

  @JsonProperty("alimenteTotal")
  public void setAlimenteTotal(Integer alimenteTotal) {
    this.alimenteTotal = alimenteTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto leistungenEOPartner(Integer leistungenEOPartner) {
    this.leistungenEOPartner = leistungenEOPartner;
    return this;
  }

  
  @JsonProperty("leistungenEOPartner")
  public Integer getLeistungenEOPartner() {
    return leistungenEOPartner;
  }

  @JsonProperty("leistungenEOPartner")
  public void setLeistungenEOPartner(Integer leistungenEOPartner) {
    this.leistungenEOPartner = leistungenEOPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto leistungenEOTotal(Integer leistungenEOTotal) {
    this.leistungenEOTotal = leistungenEOTotal;
    return this;
  }

  
  @JsonProperty("leistungenEOTotal")
  public Integer getLeistungenEOTotal() {
    return leistungenEOTotal;
  }

  @JsonProperty("leistungenEOTotal")
  public void setLeistungenEOTotal(Integer leistungenEOTotal) {
    this.leistungenEOTotal = leistungenEOTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto rentePartner(Integer rentePartner) {
    this.rentePartner = rentePartner;
    return this;
  }

  
  @JsonProperty("rentePartner")
  public Integer getRentePartner() {
    return rentePartner;
  }

  @JsonProperty("rentePartner")
  public void setRentePartner(Integer rentePartner) {
    this.rentePartner = rentePartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto renteTotal(Integer renteTotal) {
    this.renteTotal = renteTotal;
    return this;
  }

  
  @JsonProperty("renteTotal")
  public Integer getRenteTotal() {
    return renteTotal;
  }

  @JsonProperty("renteTotal")
  public void setRenteTotal(Integer renteTotal) {
    this.renteTotal = renteTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto kinderUnterhaltsbeitraegeTotal(Integer kinderUnterhaltsbeitraegeTotal) {
    this.kinderUnterhaltsbeitraegeTotal = kinderUnterhaltsbeitraegeTotal;
    return this;
  }

  
  @JsonProperty("kinderUnterhaltsbeitraegeTotal")
  public Integer getKinderUnterhaltsbeitraegeTotal() {
    return kinderUnterhaltsbeitraegeTotal;
  }

  @JsonProperty("kinderUnterhaltsbeitraegeTotal")
  public void setKinderUnterhaltsbeitraegeTotal(Integer kinderUnterhaltsbeitraegeTotal) {
    this.kinderUnterhaltsbeitraegeTotal = kinderUnterhaltsbeitraegeTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto kinderRenten(List<Integer> kinderRenten) {
    this.kinderRenten = kinderRenten;
    return this;
  }

  
  @JsonProperty("kinderRenten")
  public List<Integer> getKinderRenten() {
    return kinderRenten;
  }

  @JsonProperty("kinderRenten")
  public void setKinderRenten(List<Integer> kinderRenten) {
    this.kinderRenten = kinderRenten;
  }

  public PersoenlichesBudgetresultatDto addKinderRentenItem(Integer kinderRentenItem) {
    if (this.kinderRenten == null) {
      this.kinderRenten = new ArrayList<>();
    }

    this.kinderRenten.add(kinderRentenItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeKinderRentenItem(Integer kinderRentenItem) {
    if (kinderRentenItem != null && this.kinderRenten != null) {
      this.kinderRenten.remove(kinderRentenItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto kinderRentenTotal(Integer kinderRentenTotal) {
    this.kinderRentenTotal = kinderRentenTotal;
    return this;
  }

  
  @JsonProperty("kinderRentenTotal")
  public Integer getKinderRentenTotal() {
    return kinderRentenTotal;
  }

  @JsonProperty("kinderRentenTotal")
  public void setKinderRentenTotal(Integer kinderRentenTotal) {
    this.kinderRentenTotal = kinderRentenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto ausbildungszulagenKinder(List<KindIntegerValueItemDto> ausbildungszulagenKinder) {
    this.ausbildungszulagenKinder = ausbildungszulagenKinder;
    return this;
  }

  
  @JsonProperty("ausbildungszulagenKinder")
  public List<KindIntegerValueItemDto> getAusbildungszulagenKinder() {
    return ausbildungszulagenKinder;
  }

  @JsonProperty("ausbildungszulagenKinder")
  public void setAusbildungszulagenKinder(List<KindIntegerValueItemDto> ausbildungszulagenKinder) {
    this.ausbildungszulagenKinder = ausbildungszulagenKinder;
  }

  public PersoenlichesBudgetresultatDto addAusbildungszulagenKinderItem(KindIntegerValueItemDto ausbildungszulagenKinderItem) {
    if (this.ausbildungszulagenKinder == null) {
      this.ausbildungszulagenKinder = new ArrayList<>();
    }

    this.ausbildungszulagenKinder.add(ausbildungszulagenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeAusbildungszulagenKinderItem(KindIntegerValueItemDto ausbildungszulagenKinderItem) {
    if (ausbildungszulagenKinderItem != null && this.ausbildungszulagenKinder != null) {
      this.ausbildungszulagenKinder.remove(ausbildungszulagenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto ausbildungszulagenKinderTotal(Integer ausbildungszulagenKinderTotal) {
    this.ausbildungszulagenKinderTotal = ausbildungszulagenKinderTotal;
    return this;
  }

  
  @JsonProperty("ausbildungszulagenKinderTotal")
  public Integer getAusbildungszulagenKinderTotal() {
    return ausbildungszulagenKinderTotal;
  }

  @JsonProperty("ausbildungszulagenKinderTotal")
  public void setAusbildungszulagenKinderTotal(Integer ausbildungszulagenKinderTotal) {
    this.ausbildungszulagenKinderTotal = ausbildungszulagenKinderTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto unterhaltsbeitraegeKinder(List<KindIntegerValueItemDto> unterhaltsbeitraegeKinder) {
    this.unterhaltsbeitraegeKinder = unterhaltsbeitraegeKinder;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraegeKinder")
  public List<KindIntegerValueItemDto> getUnterhaltsbeitraegeKinder() {
    return unterhaltsbeitraegeKinder;
  }

  @JsonProperty("unterhaltsbeitraegeKinder")
  public void setUnterhaltsbeitraegeKinder(List<KindIntegerValueItemDto> unterhaltsbeitraegeKinder) {
    this.unterhaltsbeitraegeKinder = unterhaltsbeitraegeKinder;
  }

  public PersoenlichesBudgetresultatDto addUnterhaltsbeitraegeKinderItem(KindIntegerValueItemDto unterhaltsbeitraegeKinderItem) {
    if (this.unterhaltsbeitraegeKinder == null) {
      this.unterhaltsbeitraegeKinder = new ArrayList<>();
    }

    this.unterhaltsbeitraegeKinder.add(unterhaltsbeitraegeKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeUnterhaltsbeitraegeKinderItem(KindIntegerValueItemDto unterhaltsbeitraegeKinderItem) {
    if (unterhaltsbeitraegeKinderItem != null && this.unterhaltsbeitraegeKinder != null) {
      this.unterhaltsbeitraegeKinder.remove(unterhaltsbeitraegeKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto unterhaltsbeitraegeKinderTotal(Integer unterhaltsbeitraegeKinderTotal) {
    this.unterhaltsbeitraegeKinderTotal = unterhaltsbeitraegeKinderTotal;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraegeKinderTotal")
  public Integer getUnterhaltsbeitraegeKinderTotal() {
    return unterhaltsbeitraegeKinderTotal;
  }

  @JsonProperty("unterhaltsbeitraegeKinderTotal")
  public void setUnterhaltsbeitraegeKinderTotal(Integer unterhaltsbeitraegeKinderTotal) {
    this.unterhaltsbeitraegeKinderTotal = unterhaltsbeitraegeKinderTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto rentenKinder(List<KindIntegerValueItemDto> rentenKinder) {
    this.rentenKinder = rentenKinder;
    return this;
  }

  
  @JsonProperty("rentenKinder")
  public List<KindIntegerValueItemDto> getRentenKinder() {
    return rentenKinder;
  }

  @JsonProperty("rentenKinder")
  public void setRentenKinder(List<KindIntegerValueItemDto> rentenKinder) {
    this.rentenKinder = rentenKinder;
  }

  public PersoenlichesBudgetresultatDto addRentenKinderItem(KindIntegerValueItemDto rentenKinderItem) {
    if (this.rentenKinder == null) {
      this.rentenKinder = new ArrayList<>();
    }

    this.rentenKinder.add(rentenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeRentenKinderItem(KindIntegerValueItemDto rentenKinderItem) {
    if (rentenKinderItem != null && this.rentenKinder != null) {
      this.rentenKinder.remove(rentenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto rentenKinderTotal(Integer rentenKinderTotal) {
    this.rentenKinderTotal = rentenKinderTotal;
    return this;
  }

  
  @JsonProperty("rentenKinderTotal")
  public Integer getRentenKinderTotal() {
    return rentenKinderTotal;
  }

  @JsonProperty("rentenKinderTotal")
  public void setRentenKinderTotal(Integer rentenKinderTotal) {
    this.rentenKinderTotal = rentenKinderTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto ergaenzungsleistungenPartner(Integer ergaenzungsleistungenPartner) {
    this.ergaenzungsleistungenPartner = ergaenzungsleistungenPartner;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungenPartner")
  public Integer getErgaenzungsleistungenPartner() {
    return ergaenzungsleistungenPartner;
  }

  @JsonProperty("ergaenzungsleistungenPartner")
  public void setErgaenzungsleistungenPartner(Integer ergaenzungsleistungenPartner) {
    this.ergaenzungsleistungenPartner = ergaenzungsleistungenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto ergaenzungsleistungenKinder(List<KindIntegerValueItemDto> ergaenzungsleistungenKinder) {
    this.ergaenzungsleistungenKinder = ergaenzungsleistungenKinder;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungenKinder")
  public List<KindIntegerValueItemDto> getErgaenzungsleistungenKinder() {
    return ergaenzungsleistungenKinder;
  }

  @JsonProperty("ergaenzungsleistungenKinder")
  public void setErgaenzungsleistungenKinder(List<KindIntegerValueItemDto> ergaenzungsleistungenKinder) {
    this.ergaenzungsleistungenKinder = ergaenzungsleistungenKinder;
  }

  public PersoenlichesBudgetresultatDto addErgaenzungsleistungenKinderItem(KindIntegerValueItemDto ergaenzungsleistungenKinderItem) {
    if (this.ergaenzungsleistungenKinder == null) {
      this.ergaenzungsleistungenKinder = new ArrayList<>();
    }

    this.ergaenzungsleistungenKinder.add(ergaenzungsleistungenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeErgaenzungsleistungenKinderItem(KindIntegerValueItemDto ergaenzungsleistungenKinderItem) {
    if (ergaenzungsleistungenKinderItem != null && this.ergaenzungsleistungenKinder != null) {
      this.ergaenzungsleistungenKinder.remove(ergaenzungsleistungenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto ergaenzungsleistungenTotal(Integer ergaenzungsleistungenTotal) {
    this.ergaenzungsleistungenTotal = ergaenzungsleistungenTotal;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungenTotal")
  public Integer getErgaenzungsleistungenTotal() {
    return ergaenzungsleistungenTotal;
  }

  @JsonProperty("ergaenzungsleistungenTotal")
  public void setErgaenzungsleistungenTotal(Integer ergaenzungsleistungenTotal) {
    this.ergaenzungsleistungenTotal = ergaenzungsleistungenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto steuernTotal(Integer steuernTotal) {
    this.steuernTotal = steuernTotal;
    return this;
  }

  
  @JsonProperty("steuernTotal")
  public Integer getSteuernTotal() {
    return steuernTotal;
  }

  @JsonProperty("steuernTotal")
  public void setSteuernTotal(Integer steuernTotal) {
    this.steuernTotal = steuernTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto fahrkostenTotal(Integer fahrkostenTotal) {
    this.fahrkostenTotal = fahrkostenTotal;
    return this;
  }

  
  @JsonProperty("fahrkostenTotal")
  public Integer getFahrkostenTotal() {
    return fahrkostenTotal;
  }

  @JsonProperty("fahrkostenTotal")
  public void setFahrkostenTotal(Integer fahrkostenTotal) {
    this.fahrkostenTotal = fahrkostenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto verpflegungTotal(Integer verpflegungTotal) {
    this.verpflegungTotal = verpflegungTotal;
    return this;
  }

  
  @JsonProperty("verpflegungTotal")
  public Integer getVerpflegungTotal() {
    return verpflegungTotal;
  }

  @JsonProperty("verpflegungTotal")
  public void setVerpflegungTotal(Integer verpflegungTotal) {
    this.verpflegungTotal = verpflegungTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto elternbeitrag1(Integer elternbeitrag1) {
    this.elternbeitrag1 = elternbeitrag1;
    return this;
  }

  
  @JsonProperty("elternbeitrag1")
  public Integer getElternbeitrag1() {
    return elternbeitrag1;
  }

  @JsonProperty("elternbeitrag1")
  public void setElternbeitrag1(Integer elternbeitrag1) {
    this.elternbeitrag1 = elternbeitrag1;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto elternbeitrag2(Integer elternbeitrag2) {
    this.elternbeitrag2 = elternbeitrag2;
    return this;
  }

  
  @JsonProperty("elternbeitrag2")
  public Integer getElternbeitrag2() {
    return elternbeitrag2;
  }

  @JsonProperty("elternbeitrag2")
  public void setElternbeitrag2(Integer elternbeitrag2) {
    this.elternbeitrag2 = elternbeitrag2;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto einnahmenBGSAPartner(Integer einnahmenBGSAPartner) {
    this.einnahmenBGSAPartner = einnahmenBGSAPartner;
    return this;
  }

  
  @JsonProperty("einnahmenBGSAPartner")
  public Integer getEinnahmenBGSAPartner() {
    return einnahmenBGSAPartner;
  }

  @JsonProperty("einnahmenBGSAPartner")
  public void setEinnahmenBGSAPartner(Integer einnahmenBGSAPartner) {
    this.einnahmenBGSAPartner = einnahmenBGSAPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto einnahmenBGSATotal(Integer einnahmenBGSATotal) {
    this.einnahmenBGSATotal = einnahmenBGSATotal;
    return this;
  }

  
  @JsonProperty("einnahmenBGSATotal")
  public Integer getEinnahmenBGSATotal() {
    return einnahmenBGSATotal;
  }

  @JsonProperty("einnahmenBGSATotal")
  public void setEinnahmenBGSATotal(Integer einnahmenBGSATotal) {
    this.einnahmenBGSATotal = einnahmenBGSATotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto andereEinnahmenPartner(Integer andereEinnahmenPartner) {
    this.andereEinnahmenPartner = andereEinnahmenPartner;
    return this;
  }

  
  @JsonProperty("andereEinnahmenPartner")
  public Integer getAndereEinnahmenPartner() {
    return andereEinnahmenPartner;
  }

  @JsonProperty("andereEinnahmenPartner")
  public void setAndereEinnahmenPartner(Integer andereEinnahmenPartner) {
    this.andereEinnahmenPartner = andereEinnahmenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto andereEinnahmenKinder(List<KindIntegerValueItemDto> andereEinnahmenKinder) {
    this.andereEinnahmenKinder = andereEinnahmenKinder;
    return this;
  }

  
  @JsonProperty("andereEinnahmenKinder")
  public List<KindIntegerValueItemDto> getAndereEinnahmenKinder() {
    return andereEinnahmenKinder;
  }

  @JsonProperty("andereEinnahmenKinder")
  public void setAndereEinnahmenKinder(List<KindIntegerValueItemDto> andereEinnahmenKinder) {
    this.andereEinnahmenKinder = andereEinnahmenKinder;
  }

  public PersoenlichesBudgetresultatDto addAndereEinnahmenKinderItem(KindIntegerValueItemDto andereEinnahmenKinderItem) {
    if (this.andereEinnahmenKinder == null) {
      this.andereEinnahmenKinder = new ArrayList<>();
    }

    this.andereEinnahmenKinder.add(andereEinnahmenKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatDto removeAndereEinnahmenKinderItem(KindIntegerValueItemDto andereEinnahmenKinderItem) {
    if (andereEinnahmenKinderItem != null && this.andereEinnahmenKinder != null) {
      this.andereEinnahmenKinder.remove(andereEinnahmenKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatDto andereEinnahmenTotal(Integer andereEinnahmenTotal) {
    this.andereEinnahmenTotal = andereEinnahmenTotal;
    return this;
  }

  
  @JsonProperty("andereEinnahmenTotal")
  public Integer getAndereEinnahmenTotal() {
    return andereEinnahmenTotal;
  }

  @JsonProperty("andereEinnahmenTotal")
  public void setAndereEinnahmenTotal(Integer andereEinnahmenTotal) {
    this.andereEinnahmenTotal = andereEinnahmenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto taggelder(Integer taggelder) {
    this.taggelder = taggelder;
    return this;
  }

  
  @JsonProperty("taggelder")
  public Integer getTaggelder() {
    return taggelder;
  }

  @JsonProperty("taggelder")
  public void setTaggelder(Integer taggelder) {
    this.taggelder = taggelder;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto taggelderPartner(Integer taggelderPartner) {
    this.taggelderPartner = taggelderPartner;
    return this;
  }

  
  @JsonProperty("taggelderPartner")
  public Integer getTaggelderPartner() {
    return taggelderPartner;
  }

  @JsonProperty("taggelderPartner")
  public void setTaggelderPartner(Integer taggelderPartner) {
    this.taggelderPartner = taggelderPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto taggelderTotal(Integer taggelderTotal) {
    this.taggelderTotal = taggelderTotal;
    return this;
  }

  
  @JsonProperty("taggelderTotal")
  public Integer getTaggelderTotal() {
    return taggelderTotal;
  }

  @JsonProperty("taggelderTotal")
  public void setTaggelderTotal(Integer taggelderTotal) {
    this.taggelderTotal = taggelderTotal;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersoenlichesBudgetresultatDto persoenlichesBudgetresultat = (PersoenlichesBudgetresultatDto) o;
    return Objects.equals(this.anzahlPersonenImHaushalt, persoenlichesBudgetresultat.anzahlPersonenImHaushalt) &&
        Objects.equals(this.eigenerHaushalt, persoenlichesBudgetresultat.eigenerHaushalt) &&
        Objects.equals(this.anteilFamilienbudget, persoenlichesBudgetresultat.anteilFamilienbudget) &&
        Objects.equals(this.einkommen, persoenlichesBudgetresultat.einkommen) &&
        Objects.equals(this.alimente, persoenlichesBudgetresultat.alimente) &&
        Objects.equals(this.leistungenEO, persoenlichesBudgetresultat.leistungenEO) &&
        Objects.equals(this.rente, persoenlichesBudgetresultat.rente) &&
        Objects.equals(this.kinderAusbildungszulagen, persoenlichesBudgetresultat.kinderAusbildungszulagen) &&
        Objects.equals(this.kinderAusbildungszulagenPartner, persoenlichesBudgetresultat.kinderAusbildungszulagenPartner) &&
        Objects.equals(this.kinderAusbildungszulagenKinder, persoenlichesBudgetresultat.kinderAusbildungszulagenKinder) &&
        Objects.equals(this.kinderAusbildungszulagenTotal, persoenlichesBudgetresultat.kinderAusbildungszulagenTotal) &&
        Objects.equals(this.kinderUnterhaltsbeitraege, persoenlichesBudgetresultat.kinderUnterhaltsbeitraege) &&
        Objects.equals(this.ergaenzungsleistungen, persoenlichesBudgetresultat.ergaenzungsleistungen) &&
        Objects.equals(this.gemeindeInstitutionen, persoenlichesBudgetresultat.gemeindeInstitutionen) &&
        Objects.equals(this.steuerbaresVermoegen, persoenlichesBudgetresultat.steuerbaresVermoegen) &&
        Objects.equals(this.anrechenbaresVermoegen, persoenlichesBudgetresultat.anrechenbaresVermoegen) &&
        Objects.equals(this.einkommenPartner, persoenlichesBudgetresultat.einkommenPartner) &&
        Objects.equals(this.einkommenTotal, persoenlichesBudgetresultat.einkommenTotal) &&
        Objects.equals(this.einnahmenPersoenlichesBudget, persoenlichesBudgetresultat.einnahmenPersoenlichesBudget) &&
        Objects.equals(this.anteilLebenshaltungskosten, persoenlichesBudgetresultat.anteilLebenshaltungskosten) &&
        Objects.equals(this.grundbedarf, persoenlichesBudgetresultat.grundbedarf) &&
        Objects.equals(this.wohnkosten, persoenlichesBudgetresultat.wohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, persoenlichesBudgetresultat.medizinischeGrundversorgung) &&
        Objects.equals(this.steuern, persoenlichesBudgetresultat.steuern) &&
        Objects.equals(this.steuernPartner, persoenlichesBudgetresultat.steuernPartner) &&
        Objects.equals(this.fahrkosten, persoenlichesBudgetresultat.fahrkosten) &&
        Objects.equals(this.fahrkostenPartner, persoenlichesBudgetresultat.fahrkostenPartner) &&
        Objects.equals(this.verpflegung, persoenlichesBudgetresultat.verpflegung) &&
        Objects.equals(this.verpflegungPartner, persoenlichesBudgetresultat.verpflegungPartner) &&
        Objects.equals(this.fremdbetreuung, persoenlichesBudgetresultat.fremdbetreuung) &&
        Objects.equals(this.ausbildungskosten, persoenlichesBudgetresultat.ausbildungskosten) &&
        Objects.equals(this.ausgabenPersoenlichesBudget, persoenlichesBudgetresultat.ausgabenPersoenlichesBudget) &&
        Objects.equals(this.persoenlichesbudgetBerechnet, persoenlichesBudgetresultat.persoenlichesbudgetBerechnet) &&
        Objects.equals(this.einnahmenBGSA, persoenlichesBudgetresultat.einnahmenBGSA) &&
        Objects.equals(this.totalVorTeilung, persoenlichesBudgetresultat.totalVorTeilung) &&
        Objects.equals(this.alimentePartner, persoenlichesBudgetresultat.alimentePartner) &&
        Objects.equals(this.alimenteTotal, persoenlichesBudgetresultat.alimenteTotal) &&
        Objects.equals(this.leistungenEOPartner, persoenlichesBudgetresultat.leistungenEOPartner) &&
        Objects.equals(this.leistungenEOTotal, persoenlichesBudgetresultat.leistungenEOTotal) &&
        Objects.equals(this.rentePartner, persoenlichesBudgetresultat.rentePartner) &&
        Objects.equals(this.renteTotal, persoenlichesBudgetresultat.renteTotal) &&
        Objects.equals(this.kinderUnterhaltsbeitraegeTotal, persoenlichesBudgetresultat.kinderUnterhaltsbeitraegeTotal) &&
        Objects.equals(this.kinderRenten, persoenlichesBudgetresultat.kinderRenten) &&
        Objects.equals(this.kinderRentenTotal, persoenlichesBudgetresultat.kinderRentenTotal) &&
        Objects.equals(this.ausbildungszulagenKinder, persoenlichesBudgetresultat.ausbildungszulagenKinder) &&
        Objects.equals(this.ausbildungszulagenKinderTotal, persoenlichesBudgetresultat.ausbildungszulagenKinderTotal) &&
        Objects.equals(this.unterhaltsbeitraegeKinder, persoenlichesBudgetresultat.unterhaltsbeitraegeKinder) &&
        Objects.equals(this.unterhaltsbeitraegeKinderTotal, persoenlichesBudgetresultat.unterhaltsbeitraegeKinderTotal) &&
        Objects.equals(this.rentenKinder, persoenlichesBudgetresultat.rentenKinder) &&
        Objects.equals(this.rentenKinderTotal, persoenlichesBudgetresultat.rentenKinderTotal) &&
        Objects.equals(this.ergaenzungsleistungenPartner, persoenlichesBudgetresultat.ergaenzungsleistungenPartner) &&
        Objects.equals(this.ergaenzungsleistungenKinder, persoenlichesBudgetresultat.ergaenzungsleistungenKinder) &&
        Objects.equals(this.ergaenzungsleistungenTotal, persoenlichesBudgetresultat.ergaenzungsleistungenTotal) &&
        Objects.equals(this.steuernTotal, persoenlichesBudgetresultat.steuernTotal) &&
        Objects.equals(this.fahrkostenTotal, persoenlichesBudgetresultat.fahrkostenTotal) &&
        Objects.equals(this.verpflegungTotal, persoenlichesBudgetresultat.verpflegungTotal) &&
        Objects.equals(this.elternbeitrag1, persoenlichesBudgetresultat.elternbeitrag1) &&
        Objects.equals(this.elternbeitrag2, persoenlichesBudgetresultat.elternbeitrag2) &&
        Objects.equals(this.einnahmenBGSAPartner, persoenlichesBudgetresultat.einnahmenBGSAPartner) &&
        Objects.equals(this.einnahmenBGSATotal, persoenlichesBudgetresultat.einnahmenBGSATotal) &&
        Objects.equals(this.andereEinnahmen, persoenlichesBudgetresultat.andereEinnahmen) &&
        Objects.equals(this.andereEinnahmenPartner, persoenlichesBudgetresultat.andereEinnahmenPartner) &&
        Objects.equals(this.andereEinnahmenKinder, persoenlichesBudgetresultat.andereEinnahmenKinder) &&
        Objects.equals(this.andereEinnahmenTotal, persoenlichesBudgetresultat.andereEinnahmenTotal) &&
        Objects.equals(this.taggelder, persoenlichesBudgetresultat.taggelder) &&
        Objects.equals(this.taggelderPartner, persoenlichesBudgetresultat.taggelderPartner) &&
        Objects.equals(this.taggelderTotal, persoenlichesBudgetresultat.taggelderTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anzahlPersonenImHaushalt, eigenerHaushalt, anteilFamilienbudget, einkommen, alimente, leistungenEO, rente, kinderAusbildungszulagen, kinderAusbildungszulagenPartner, kinderAusbildungszulagenKinder, kinderAusbildungszulagenTotal, kinderUnterhaltsbeitraege, ergaenzungsleistungen, gemeindeInstitutionen, steuerbaresVermoegen, anrechenbaresVermoegen, einkommenPartner, einkommenTotal, einnahmenPersoenlichesBudget, anteilLebenshaltungskosten, grundbedarf, wohnkosten, medizinischeGrundversorgung, steuern, steuernPartner, fahrkosten, fahrkostenPartner, verpflegung, verpflegungPartner, fremdbetreuung, ausbildungskosten, ausgabenPersoenlichesBudget, persoenlichesbudgetBerechnet, einnahmenBGSA, totalVorTeilung, alimentePartner, alimenteTotal, leistungenEOPartner, leistungenEOTotal, rentePartner, renteTotal, kinderUnterhaltsbeitraegeTotal, kinderRenten, kinderRentenTotal, ausbildungszulagenKinder, ausbildungszulagenKinderTotal, unterhaltsbeitraegeKinder, unterhaltsbeitraegeKinderTotal, rentenKinder, rentenKinderTotal, ergaenzungsleistungenPartner, ergaenzungsleistungenKinder, ergaenzungsleistungenTotal, steuernTotal, fahrkostenTotal, verpflegungTotal, elternbeitrag1, elternbeitrag2, einnahmenBGSAPartner, einnahmenBGSATotal, andereEinnahmen, andereEinnahmenPartner, andereEinnahmenKinder, andereEinnahmenTotal, taggelder, taggelderPartner, taggelderTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatDto {\n");
    
    sb.append("    anzahlPersonenImHaushalt: ").append(toIndentedString(anzahlPersonenImHaushalt)).append("\n");
    sb.append("    eigenerHaushalt: ").append(toIndentedString(eigenerHaushalt)).append("\n");
    sb.append("    anteilFamilienbudget: ").append(toIndentedString(anteilFamilienbudget)).append("\n");
    sb.append("    einkommen: ").append(toIndentedString(einkommen)).append("\n");
    sb.append("    alimente: ").append(toIndentedString(alimente)).append("\n");
    sb.append("    leistungenEO: ").append(toIndentedString(leistungenEO)).append("\n");
    sb.append("    rente: ").append(toIndentedString(rente)).append("\n");
    sb.append("    kinderAusbildungszulagen: ").append(toIndentedString(kinderAusbildungszulagen)).append("\n");
    sb.append("    kinderAusbildungszulagenPartner: ").append(toIndentedString(kinderAusbildungszulagenPartner)).append("\n");
    sb.append("    kinderAusbildungszulagenKinder: ").append(toIndentedString(kinderAusbildungszulagenKinder)).append("\n");
    sb.append("    kinderAusbildungszulagenTotal: ").append(toIndentedString(kinderAusbildungszulagenTotal)).append("\n");
    sb.append("    kinderUnterhaltsbeitraege: ").append(toIndentedString(kinderUnterhaltsbeitraege)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    gemeindeInstitutionen: ").append(toIndentedString(gemeindeInstitutionen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    einkommenPartner: ").append(toIndentedString(einkommenPartner)).append("\n");
    sb.append("    einkommenTotal: ").append(toIndentedString(einkommenTotal)).append("\n");
    sb.append("    einnahmenPersoenlichesBudget: ").append(toIndentedString(einnahmenPersoenlichesBudget)).append("\n");
    sb.append("    anteilLebenshaltungskosten: ").append(toIndentedString(anteilLebenshaltungskosten)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    steuern: ").append(toIndentedString(steuern)).append("\n");
    sb.append("    steuernPartner: ").append(toIndentedString(steuernPartner)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    fremdbetreuung: ").append(toIndentedString(fremdbetreuung)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    ausgabenPersoenlichesBudget: ").append(toIndentedString(ausgabenPersoenlichesBudget)).append("\n");
    sb.append("    persoenlichesbudgetBerechnet: ").append(toIndentedString(persoenlichesbudgetBerechnet)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    totalVorTeilung: ").append(toIndentedString(totalVorTeilung)).append("\n");
    sb.append("    alimentePartner: ").append(toIndentedString(alimentePartner)).append("\n");
    sb.append("    alimenteTotal: ").append(toIndentedString(alimenteTotal)).append("\n");
    sb.append("    leistungenEOPartner: ").append(toIndentedString(leistungenEOPartner)).append("\n");
    sb.append("    leistungenEOTotal: ").append(toIndentedString(leistungenEOTotal)).append("\n");
    sb.append("    rentePartner: ").append(toIndentedString(rentePartner)).append("\n");
    sb.append("    renteTotal: ").append(toIndentedString(renteTotal)).append("\n");
    sb.append("    kinderUnterhaltsbeitraegeTotal: ").append(toIndentedString(kinderUnterhaltsbeitraegeTotal)).append("\n");
    sb.append("    kinderRenten: ").append(toIndentedString(kinderRenten)).append("\n");
    sb.append("    kinderRentenTotal: ").append(toIndentedString(kinderRentenTotal)).append("\n");
    sb.append("    ausbildungszulagenKinder: ").append(toIndentedString(ausbildungszulagenKinder)).append("\n");
    sb.append("    ausbildungszulagenKinderTotal: ").append(toIndentedString(ausbildungszulagenKinderTotal)).append("\n");
    sb.append("    unterhaltsbeitraegeKinder: ").append(toIndentedString(unterhaltsbeitraegeKinder)).append("\n");
    sb.append("    unterhaltsbeitraegeKinderTotal: ").append(toIndentedString(unterhaltsbeitraegeKinderTotal)).append("\n");
    sb.append("    rentenKinder: ").append(toIndentedString(rentenKinder)).append("\n");
    sb.append("    rentenKinderTotal: ").append(toIndentedString(rentenKinderTotal)).append("\n");
    sb.append("    ergaenzungsleistungenPartner: ").append(toIndentedString(ergaenzungsleistungenPartner)).append("\n");
    sb.append("    ergaenzungsleistungenKinder: ").append(toIndentedString(ergaenzungsleistungenKinder)).append("\n");
    sb.append("    ergaenzungsleistungenTotal: ").append(toIndentedString(ergaenzungsleistungenTotal)).append("\n");
    sb.append("    steuernTotal: ").append(toIndentedString(steuernTotal)).append("\n");
    sb.append("    fahrkostenTotal: ").append(toIndentedString(fahrkostenTotal)).append("\n");
    sb.append("    verpflegungTotal: ").append(toIndentedString(verpflegungTotal)).append("\n");
    sb.append("    elternbeitrag1: ").append(toIndentedString(elternbeitrag1)).append("\n");
    sb.append("    elternbeitrag2: ").append(toIndentedString(elternbeitrag2)).append("\n");
    sb.append("    einnahmenBGSAPartner: ").append(toIndentedString(einnahmenBGSAPartner)).append("\n");
    sb.append("    einnahmenBGSATotal: ").append(toIndentedString(einnahmenBGSATotal)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    andereEinnahmenPartner: ").append(toIndentedString(andereEinnahmenPartner)).append("\n");
    sb.append("    andereEinnahmenKinder: ").append(toIndentedString(andereEinnahmenKinder)).append("\n");
    sb.append("    andereEinnahmenTotal: ").append(toIndentedString(andereEinnahmenTotal)).append("\n");
    sb.append("    taggelder: ").append(toIndentedString(taggelder)).append("\n");
    sb.append("    taggelderPartner: ").append(toIndentedString(taggelderPartner)).append("\n");
    sb.append("    taggelderTotal: ").append(toIndentedString(taggelderTotal)).append("\n");
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

