package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
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
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer gemeindeInstitutionen;
  private @Valid Integer steuerbaresVermoegen;
  private @Valid Integer anrechenbaresVermoegen;
  private @Valid Integer einkommenPartner;
  private @Valid Integer einnahmenPersoenlichesBudget;
  private @Valid Integer anteilLebenshaltungskosten;
  private @Valid Integer grundbedarf;
  private @Valid Integer wohnkosten;
  private @Valid Integer medizinischeGrundversorgung;
  private @Valid Integer steuernKantonGemeinde;
  private @Valid Integer steuernKantonGemeindePartner;
  private @Valid Integer fahrkosten;
  private @Valid Integer fahrkostenPartner;
  private @Valid Integer verpflegung;
  private @Valid Integer verpflegungPartner;
  private @Valid Integer fremdbetreuung;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer ausgabenPersoenlichesBudget;
  private @Valid Integer persoenlichesbudgetBerechnet;
  private @Valid Integer totalVorTeilung;
  private @Valid Integer elternbeitrag1;
  private @Valid Integer elternbeitrag2;

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
  public PersoenlichesBudgetresultatDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeinde")
  @NotNull
  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty("steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto steuernKantonGemeindePartner(Integer steuernKantonGemeindePartner) {
    this.steuernKantonGemeindePartner = steuernKantonGemeindePartner;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeindePartner")
  @NotNull
  public Integer getSteuernKantonGemeindePartner() {
    return steuernKantonGemeindePartner;
  }

  @JsonProperty("steuernKantonGemeindePartner")
  public void setSteuernKantonGemeindePartner(Integer steuernKantonGemeindePartner) {
    this.steuernKantonGemeindePartner = steuernKantonGemeindePartner;
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
        Objects.equals(this.ergaenzungsleistungen, persoenlichesBudgetresultat.ergaenzungsleistungen) &&
        Objects.equals(this.gemeindeInstitutionen, persoenlichesBudgetresultat.gemeindeInstitutionen) &&
        Objects.equals(this.steuerbaresVermoegen, persoenlichesBudgetresultat.steuerbaresVermoegen) &&
        Objects.equals(this.anrechenbaresVermoegen, persoenlichesBudgetresultat.anrechenbaresVermoegen) &&
        Objects.equals(this.einkommenPartner, persoenlichesBudgetresultat.einkommenPartner) &&
        Objects.equals(this.einnahmenPersoenlichesBudget, persoenlichesBudgetresultat.einnahmenPersoenlichesBudget) &&
        Objects.equals(this.anteilLebenshaltungskosten, persoenlichesBudgetresultat.anteilLebenshaltungskosten) &&
        Objects.equals(this.grundbedarf, persoenlichesBudgetresultat.grundbedarf) &&
        Objects.equals(this.wohnkosten, persoenlichesBudgetresultat.wohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, persoenlichesBudgetresultat.medizinischeGrundversorgung) &&
        Objects.equals(this.steuernKantonGemeinde, persoenlichesBudgetresultat.steuernKantonGemeinde) &&
        Objects.equals(this.steuernKantonGemeindePartner, persoenlichesBudgetresultat.steuernKantonGemeindePartner) &&
        Objects.equals(this.fahrkosten, persoenlichesBudgetresultat.fahrkosten) &&
        Objects.equals(this.fahrkostenPartner, persoenlichesBudgetresultat.fahrkostenPartner) &&
        Objects.equals(this.verpflegung, persoenlichesBudgetresultat.verpflegung) &&
        Objects.equals(this.verpflegungPartner, persoenlichesBudgetresultat.verpflegungPartner) &&
        Objects.equals(this.fremdbetreuung, persoenlichesBudgetresultat.fremdbetreuung) &&
        Objects.equals(this.ausbildungskosten, persoenlichesBudgetresultat.ausbildungskosten) &&
        Objects.equals(this.ausgabenPersoenlichesBudget, persoenlichesBudgetresultat.ausgabenPersoenlichesBudget) &&
        Objects.equals(this.persoenlichesbudgetBerechnet, persoenlichesBudgetresultat.persoenlichesbudgetBerechnet) &&
        Objects.equals(this.totalVorTeilung, persoenlichesBudgetresultat.totalVorTeilung) &&
        Objects.equals(this.elternbeitrag1, persoenlichesBudgetresultat.elternbeitrag1) &&
        Objects.equals(this.elternbeitrag2, persoenlichesBudgetresultat.elternbeitrag2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anzahlPersonenImHaushalt, eigenerHaushalt, anteilFamilienbudget, einkommen, alimente, leistungenEO, rente, kinderAusbildungszulagen, ergaenzungsleistungen, gemeindeInstitutionen, steuerbaresVermoegen, anrechenbaresVermoegen, einkommenPartner, einnahmenPersoenlichesBudget, anteilLebenshaltungskosten, grundbedarf, wohnkosten, medizinischeGrundversorgung, steuernKantonGemeinde, steuernKantonGemeindePartner, fahrkosten, fahrkostenPartner, verpflegung, verpflegungPartner, fremdbetreuung, ausbildungskosten, ausgabenPersoenlichesBudget, persoenlichesbudgetBerechnet, totalVorTeilung, elternbeitrag1, elternbeitrag2);
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
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    gemeindeInstitutionen: ").append(toIndentedString(gemeindeInstitutionen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    einkommenPartner: ").append(toIndentedString(einkommenPartner)).append("\n");
    sb.append("    einnahmenPersoenlichesBudget: ").append(toIndentedString(einnahmenPersoenlichesBudget)).append("\n");
    sb.append("    anteilLebenshaltungskosten: ").append(toIndentedString(anteilLebenshaltungskosten)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    steuernKantonGemeindePartner: ").append(toIndentedString(steuernKantonGemeindePartner)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    fremdbetreuung: ").append(toIndentedString(fremdbetreuung)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    ausgabenPersoenlichesBudget: ").append(toIndentedString(ausgabenPersoenlichesBudget)).append("\n");
    sb.append("    persoenlichesbudgetBerechnet: ").append(toIndentedString(persoenlichesbudgetBerechnet)).append("\n");
    sb.append("    totalVorTeilung: ").append(toIndentedString(totalVorTeilung)).append("\n");
    sb.append("    elternbeitrag1: ").append(toIndentedString(elternbeitrag1)).append("\n");
    sb.append("    elternbeitrag2: ").append(toIndentedString(elternbeitrag2)).append("\n");
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

