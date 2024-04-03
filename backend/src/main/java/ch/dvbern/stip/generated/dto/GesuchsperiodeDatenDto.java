package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchsperiodeDaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeDatenDto  implements Serializable {
  private @Valid String fiskaljahr;
  private @Valid String gesuchsjahr;
  private @Valid LocalDate gesuchsperiodeStart;
  private @Valid LocalDate gesuchsperiodeStopp;
  private @Valid LocalDate aufschaltterminStart;
  private @Valid LocalDate aufschaltterminStopp;
  private @Valid LocalDate einreichefristNormal;
  private @Valid LocalDate einreichefristReduziert;
  private @Valid BigDecimal ausbKostenSekII;
  private @Valid BigDecimal ausbKostenTertiaer;
  private @Valid BigDecimal freibetragErwerbseinkommen;
  private @Valid BigDecimal einkommensfreibetrag;
  private @Valid BigDecimal elternbeteiligungssatz;
  private @Valid BigDecimal fEinkommensfreibetrag;
  private @Valid BigDecimal fVermoegensfreibetrag;
  private @Valid BigDecimal fVermogenSatzAngerechnet;
  private @Valid BigDecimal integrationszulage;
  private @Valid BigDecimal limiteEkFreibetragIntegrationszulag;
  private @Valid BigDecimal stipLimiteMinimalstipendium;
  private @Valid BigDecimal person1;
  private @Valid BigDecimal personen2;
  private @Valid BigDecimal personen3;
  private @Valid BigDecimal personen4;
  private @Valid BigDecimal personen5;
  private @Valid BigDecimal personen6;
  private @Valid BigDecimal personen7;
  private @Valid BigDecimal proWeiterePerson;
  private @Valid BigDecimal kinder0018;
  private @Valid BigDecimal jugendlicheErwachsene1925;
  private @Valid BigDecimal erwachsene2699;
  private @Valid BigDecimal wohnkostenFam1pers;
  private @Valid BigDecimal wohnkostenFam2pers;
  private @Valid BigDecimal wohnkostenFam3pers;
  private @Valid BigDecimal wohnkostenFam4pers;
  private @Valid BigDecimal wohnkostenFam5pluspers;
  private @Valid BigDecimal wohnkostenPersoenlich1pers;
  private @Valid BigDecimal wohnkostenPersoenlich2pers;
  private @Valid BigDecimal wohnkostenPersoenlich3pers;
  private @Valid BigDecimal wohnkostenPersoenlich4pers;
  private @Valid BigDecimal wohnkostenPersoenlich5pluspers;
  private @Valid BigDecimal freibetragVermögen;

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fiskaljahr(String fiskaljahr) {
    this.fiskaljahr = fiskaljahr;
    return this;
  }

  
  @JsonProperty("fiskaljahr")
  @NotNull
  public String getFiskaljahr() {
    return fiskaljahr;
  }

  @JsonProperty("fiskaljahr")
  public void setFiskaljahr(String fiskaljahr) {
    this.fiskaljahr = fiskaljahr;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto gesuchsjahr(String gesuchsjahr) {
    this.gesuchsjahr = gesuchsjahr;
    return this;
  }

  
  @JsonProperty("gesuchsjahr")
  @NotNull
  public String getGesuchsjahr() {
    return gesuchsjahr;
  }

  @JsonProperty("gesuchsjahr")
  public void setGesuchsjahr(String gesuchsjahr) {
    this.gesuchsjahr = gesuchsjahr;
  }

  /**
   * dd.MM.YYYY
   **/
  public GesuchsperiodeDatenDto gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
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
   * dd.MM.YYYY
   **/
  public GesuchsperiodeDatenDto gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
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
   * dd.MM.YYYY
   **/
  public GesuchsperiodeDatenDto aufschaltterminStart(LocalDate aufschaltterminStart) {
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
   * dd.MM.YYYY
   **/
  public GesuchsperiodeDatenDto aufschaltterminStopp(LocalDate aufschaltterminStopp) {
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
   * dd.MM.YYYY
   **/
  public GesuchsperiodeDatenDto einreichefristNormal(LocalDate einreichefristNormal) {
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
   * dd.MM.YYYY
   **/
  public GesuchsperiodeDatenDto einreichefristReduziert(LocalDate einreichefristReduziert) {
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
   * 
   **/
  public GesuchsperiodeDatenDto ausbKostenSekII(BigDecimal ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
    return this;
  }

  
  @JsonProperty("ausbKosten_SekII")
  @NotNull
  public BigDecimal getAusbKostenSekII() {
    return ausbKostenSekII;
  }

  @JsonProperty("ausbKosten_SekII")
  public void setAusbKostenSekII(BigDecimal ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto ausbKostenTertiaer(BigDecimal ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
    return this;
  }

  
  @JsonProperty("ausbKosten_Tertiaer")
  @NotNull
  public BigDecimal getAusbKostenTertiaer() {
    return ausbKostenTertiaer;
  }

  @JsonProperty("ausbKosten_Tertiaer")
  public void setAusbKostenTertiaer(BigDecimal ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto freibetragErwerbseinkommen(BigDecimal freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
    return this;
  }

  
  @JsonProperty("freibetrag_erwerbseinkommen")
  @NotNull
  public BigDecimal getFreibetragErwerbseinkommen() {
    return freibetragErwerbseinkommen;
  }

  @JsonProperty("freibetrag_erwerbseinkommen")
  public void setFreibetragErwerbseinkommen(BigDecimal freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto einkommensfreibetrag(BigDecimal einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty("einkommensfreibetrag")
  @NotNull
  public BigDecimal getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty("einkommensfreibetrag")
  public void setEinkommensfreibetrag(BigDecimal einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto elternbeteiligungssatz(BigDecimal elternbeteiligungssatz) {
    this.elternbeteiligungssatz = elternbeteiligungssatz;
    return this;
  }

  
  @JsonProperty("elternbeteiligungssatz")
  @NotNull
  public BigDecimal getElternbeteiligungssatz() {
    return elternbeteiligungssatz;
  }

  @JsonProperty("elternbeteiligungssatz")
  public void setElternbeteiligungssatz(BigDecimal elternbeteiligungssatz) {
    this.elternbeteiligungssatz = elternbeteiligungssatz;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fEinkommensfreibetrag(BigDecimal fEinkommensfreibetrag) {
    this.fEinkommensfreibetrag = fEinkommensfreibetrag;
    return this;
  }

  
  @JsonProperty("f_Einkommensfreibetrag")
  @NotNull
  public BigDecimal getfEinkommensfreibetrag() {
    return fEinkommensfreibetrag;
  }

  @JsonProperty("f_Einkommensfreibetrag")
  public void setfEinkommensfreibetrag(BigDecimal fEinkommensfreibetrag) {
    this.fEinkommensfreibetrag = fEinkommensfreibetrag;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fVermoegensfreibetrag(BigDecimal fVermoegensfreibetrag) {
    this.fVermoegensfreibetrag = fVermoegensfreibetrag;
    return this;
  }

  
  @JsonProperty("f_Vermoegensfreibetrag")
  @NotNull
  public BigDecimal getfVermoegensfreibetrag() {
    return fVermoegensfreibetrag;
  }

  @JsonProperty("f_Vermoegensfreibetrag")
  public void setfVermoegensfreibetrag(BigDecimal fVermoegensfreibetrag) {
    this.fVermoegensfreibetrag = fVermoegensfreibetrag;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fVermogenSatzAngerechnet(BigDecimal fVermogenSatzAngerechnet) {
    this.fVermogenSatzAngerechnet = fVermogenSatzAngerechnet;
    return this;
  }

  
  @JsonProperty("f_VermogenSatzAngerechnet")
  @NotNull
  public BigDecimal getfVermogenSatzAngerechnet() {
    return fVermogenSatzAngerechnet;
  }

  @JsonProperty("f_VermogenSatzAngerechnet")
  public void setfVermogenSatzAngerechnet(BigDecimal fVermogenSatzAngerechnet) {
    this.fVermogenSatzAngerechnet = fVermogenSatzAngerechnet;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto integrationszulage(BigDecimal integrationszulage) {
    this.integrationszulage = integrationszulage;
    return this;
  }

  
  @JsonProperty("integrationszulage")
  @NotNull
  public BigDecimal getIntegrationszulage() {
    return integrationszulage;
  }

  @JsonProperty("integrationszulage")
  public void setIntegrationszulage(BigDecimal integrationszulage) {
    this.integrationszulage = integrationszulage;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto limiteEkFreibetragIntegrationszulag(BigDecimal limiteEkFreibetragIntegrationszulag) {
    this.limiteEkFreibetragIntegrationszulag = limiteEkFreibetragIntegrationszulag;
    return this;
  }

  
  @JsonProperty("limite_EkFreibetrag_Integrationszulag")
  @NotNull
  public BigDecimal getLimiteEkFreibetragIntegrationszulag() {
    return limiteEkFreibetragIntegrationszulag;
  }

  @JsonProperty("limite_EkFreibetrag_Integrationszulag")
  public void setLimiteEkFreibetragIntegrationszulag(BigDecimal limiteEkFreibetragIntegrationszulag) {
    this.limiteEkFreibetragIntegrationszulag = limiteEkFreibetragIntegrationszulag;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto stipLimiteMinimalstipendium(BigDecimal stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
    return this;
  }

  
  @JsonProperty("stipLimite_Minimalstipendium")
  @NotNull
  public BigDecimal getStipLimiteMinimalstipendium() {
    return stipLimiteMinimalstipendium;
  }

  @JsonProperty("stipLimite_Minimalstipendium")
  public void setStipLimiteMinimalstipendium(BigDecimal stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto person1(BigDecimal person1) {
    this.person1 = person1;
    return this;
  }

  
  @JsonProperty("person_1")
  @NotNull
  public BigDecimal getPerson1() {
    return person1;
  }

  @JsonProperty("person_1")
  public void setPerson1(BigDecimal person1) {
    this.person1 = person1;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto personen2(BigDecimal personen2) {
    this.personen2 = personen2;
    return this;
  }

  
  @JsonProperty("personen_2")
  @NotNull
  public BigDecimal getPersonen2() {
    return personen2;
  }

  @JsonProperty("personen_2")
  public void setPersonen2(BigDecimal personen2) {
    this.personen2 = personen2;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto personen3(BigDecimal personen3) {
    this.personen3 = personen3;
    return this;
  }

  
  @JsonProperty("personen_3")
  @NotNull
  public BigDecimal getPersonen3() {
    return personen3;
  }

  @JsonProperty("personen_3")
  public void setPersonen3(BigDecimal personen3) {
    this.personen3 = personen3;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto personen4(BigDecimal personen4) {
    this.personen4 = personen4;
    return this;
  }

  
  @JsonProperty("personen_4")
  @NotNull
  public BigDecimal getPersonen4() {
    return personen4;
  }

  @JsonProperty("personen_4")
  public void setPersonen4(BigDecimal personen4) {
    this.personen4 = personen4;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto personen5(BigDecimal personen5) {
    this.personen5 = personen5;
    return this;
  }

  
  @JsonProperty("personen_5")
  @NotNull
  public BigDecimal getPersonen5() {
    return personen5;
  }

  @JsonProperty("personen_5")
  public void setPersonen5(BigDecimal personen5) {
    this.personen5 = personen5;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto personen6(BigDecimal personen6) {
    this.personen6 = personen6;
    return this;
  }

  
  @JsonProperty("personen_6")
  @NotNull
  public BigDecimal getPersonen6() {
    return personen6;
  }

  @JsonProperty("personen_6")
  public void setPersonen6(BigDecimal personen6) {
    this.personen6 = personen6;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto personen7(BigDecimal personen7) {
    this.personen7 = personen7;
    return this;
  }

  
  @JsonProperty("personen_7")
  @NotNull
  public BigDecimal getPersonen7() {
    return personen7;
  }

  @JsonProperty("personen_7")
  public void setPersonen7(BigDecimal personen7) {
    this.personen7 = personen7;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto proWeiterePerson(BigDecimal proWeiterePerson) {
    this.proWeiterePerson = proWeiterePerson;
    return this;
  }

  
  @JsonProperty("proWeiterePerson")
  @NotNull
  public BigDecimal getProWeiterePerson() {
    return proWeiterePerson;
  }

  @JsonProperty("proWeiterePerson")
  public void setProWeiterePerson(BigDecimal proWeiterePerson) {
    this.proWeiterePerson = proWeiterePerson;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto kinder0018(BigDecimal kinder0018) {
    this.kinder0018 = kinder0018;
    return this;
  }

  
  @JsonProperty("kinder_00_18")
  @NotNull
  public BigDecimal getKinder0018() {
    return kinder0018;
  }

  @JsonProperty("kinder_00_18")
  public void setKinder0018(BigDecimal kinder0018) {
    this.kinder0018 = kinder0018;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto jugendlicheErwachsene1925(BigDecimal jugendlicheErwachsene1925) {
    this.jugendlicheErwachsene1925 = jugendlicheErwachsene1925;
    return this;
  }

  
  @JsonProperty("jugendliche_erwachsene_19_25")
  @NotNull
  public BigDecimal getJugendlicheErwachsene1925() {
    return jugendlicheErwachsene1925;
  }

  @JsonProperty("jugendliche_erwachsene_19_25")
  public void setJugendlicheErwachsene1925(BigDecimal jugendlicheErwachsene1925) {
    this.jugendlicheErwachsene1925 = jugendlicheErwachsene1925;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto erwachsene2699(BigDecimal erwachsene2699) {
    this.erwachsene2699 = erwachsene2699;
    return this;
  }

  
  @JsonProperty("erwachsene_26_99")
  @NotNull
  public BigDecimal getErwachsene2699() {
    return erwachsene2699;
  }

  @JsonProperty("erwachsene_26_99")
  public void setErwachsene2699(BigDecimal erwachsene2699) {
    this.erwachsene2699 = erwachsene2699;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenFam1pers(BigDecimal wohnkostenFam1pers) {
    this.wohnkostenFam1pers = wohnkostenFam1pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_1pers")
  @NotNull
  public BigDecimal getWohnkostenFam1pers() {
    return wohnkostenFam1pers;
  }

  @JsonProperty("wohnkosten_fam_1pers")
  public void setWohnkostenFam1pers(BigDecimal wohnkostenFam1pers) {
    this.wohnkostenFam1pers = wohnkostenFam1pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenFam2pers(BigDecimal wohnkostenFam2pers) {
    this.wohnkostenFam2pers = wohnkostenFam2pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_2pers")
  @NotNull
  public BigDecimal getWohnkostenFam2pers() {
    return wohnkostenFam2pers;
  }

  @JsonProperty("wohnkosten_fam_2pers")
  public void setWohnkostenFam2pers(BigDecimal wohnkostenFam2pers) {
    this.wohnkostenFam2pers = wohnkostenFam2pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenFam3pers(BigDecimal wohnkostenFam3pers) {
    this.wohnkostenFam3pers = wohnkostenFam3pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_3pers")
  @NotNull
  public BigDecimal getWohnkostenFam3pers() {
    return wohnkostenFam3pers;
  }

  @JsonProperty("wohnkosten_fam_3pers")
  public void setWohnkostenFam3pers(BigDecimal wohnkostenFam3pers) {
    this.wohnkostenFam3pers = wohnkostenFam3pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenFam4pers(BigDecimal wohnkostenFam4pers) {
    this.wohnkostenFam4pers = wohnkostenFam4pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_4pers")
  @NotNull
  public BigDecimal getWohnkostenFam4pers() {
    return wohnkostenFam4pers;
  }

  @JsonProperty("wohnkosten_fam_4pers")
  public void setWohnkostenFam4pers(BigDecimal wohnkostenFam4pers) {
    this.wohnkostenFam4pers = wohnkostenFam4pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenFam5pluspers(BigDecimal wohnkostenFam5pluspers) {
    this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_5pluspers")
  @NotNull
  public BigDecimal getWohnkostenFam5pluspers() {
    return wohnkostenFam5pluspers;
  }

  @JsonProperty("wohnkosten_fam_5pluspers")
  public void setWohnkostenFam5pluspers(BigDecimal wohnkostenFam5pluspers) {
    this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenPersoenlich1pers(BigDecimal wohnkostenPersoenlich1pers) {
    this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_1pers")
  @NotNull
  public BigDecimal getWohnkostenPersoenlich1pers() {
    return wohnkostenPersoenlich1pers;
  }

  @JsonProperty("wohnkosten_persoenlich_1pers")
  public void setWohnkostenPersoenlich1pers(BigDecimal wohnkostenPersoenlich1pers) {
    this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenPersoenlich2pers(BigDecimal wohnkostenPersoenlich2pers) {
    this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_2pers")
  @NotNull
  public BigDecimal getWohnkostenPersoenlich2pers() {
    return wohnkostenPersoenlich2pers;
  }

  @JsonProperty("wohnkosten_persoenlich_2pers")
  public void setWohnkostenPersoenlich2pers(BigDecimal wohnkostenPersoenlich2pers) {
    this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenPersoenlich3pers(BigDecimal wohnkostenPersoenlich3pers) {
    this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_3pers")
  @NotNull
  public BigDecimal getWohnkostenPersoenlich3pers() {
    return wohnkostenPersoenlich3pers;
  }

  @JsonProperty("wohnkosten_persoenlich_3pers")
  public void setWohnkostenPersoenlich3pers(BigDecimal wohnkostenPersoenlich3pers) {
    this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenPersoenlich4pers(BigDecimal wohnkostenPersoenlich4pers) {
    this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_4pers")
  @NotNull
  public BigDecimal getWohnkostenPersoenlich4pers() {
    return wohnkostenPersoenlich4pers;
  }

  @JsonProperty("wohnkosten_persoenlich_4pers")
  public void setWohnkostenPersoenlich4pers(BigDecimal wohnkostenPersoenlich4pers) {
    this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto wohnkostenPersoenlich5pluspers(BigDecimal wohnkostenPersoenlich5pluspers) {
    this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_5pluspers")
  @NotNull
  public BigDecimal getWohnkostenPersoenlich5pluspers() {
    return wohnkostenPersoenlich5pluspers;
  }

  @JsonProperty("wohnkosten_persoenlich_5pluspers")
  public void setWohnkostenPersoenlich5pluspers(BigDecimal wohnkostenPersoenlich5pluspers) {
    this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto freibetragVermögen(BigDecimal freibetragVermögen) {
    this.freibetragVermögen = freibetragVermögen;
    return this;
  }

  
  @JsonProperty("freibetrag_vermögen")
  public BigDecimal getFreibetragVermögen() {
    return freibetragVermögen;
  }

  @JsonProperty("freibetrag_vermögen")
  public void setFreibetragVermögen(BigDecimal freibetragVermögen) {
    this.freibetragVermögen = freibetragVermögen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeDatenDto gesuchsperiodeDaten = (GesuchsperiodeDatenDto) o;
    return Objects.equals(this.fiskaljahr, gesuchsperiodeDaten.fiskaljahr) &&
        Objects.equals(this.gesuchsjahr, gesuchsperiodeDaten.gesuchsjahr) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiodeDaten.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiodeDaten.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiodeDaten.aufschaltterminStart) &&
        Objects.equals(this.aufschaltterminStopp, gesuchsperiodeDaten.aufschaltterminStopp) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiodeDaten.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiodeDaten.einreichefristReduziert) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiodeDaten.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiodeDaten.ausbKostenTertiaer) &&
        Objects.equals(this.freibetragErwerbseinkommen, gesuchsperiodeDaten.freibetragErwerbseinkommen) &&
        Objects.equals(this.einkommensfreibetrag, gesuchsperiodeDaten.einkommensfreibetrag) &&
        Objects.equals(this.elternbeteiligungssatz, gesuchsperiodeDaten.elternbeteiligungssatz) &&
        Objects.equals(this.fEinkommensfreibetrag, gesuchsperiodeDaten.fEinkommensfreibetrag) &&
        Objects.equals(this.fVermoegensfreibetrag, gesuchsperiodeDaten.fVermoegensfreibetrag) &&
        Objects.equals(this.fVermogenSatzAngerechnet, gesuchsperiodeDaten.fVermogenSatzAngerechnet) &&
        Objects.equals(this.integrationszulage, gesuchsperiodeDaten.integrationszulage) &&
        Objects.equals(this.limiteEkFreibetragIntegrationszulag, gesuchsperiodeDaten.limiteEkFreibetragIntegrationszulag) &&
        Objects.equals(this.stipLimiteMinimalstipendium, gesuchsperiodeDaten.stipLimiteMinimalstipendium) &&
        Objects.equals(this.person1, gesuchsperiodeDaten.person1) &&
        Objects.equals(this.personen2, gesuchsperiodeDaten.personen2) &&
        Objects.equals(this.personen3, gesuchsperiodeDaten.personen3) &&
        Objects.equals(this.personen4, gesuchsperiodeDaten.personen4) &&
        Objects.equals(this.personen5, gesuchsperiodeDaten.personen5) &&
        Objects.equals(this.personen6, gesuchsperiodeDaten.personen6) &&
        Objects.equals(this.personen7, gesuchsperiodeDaten.personen7) &&
        Objects.equals(this.proWeiterePerson, gesuchsperiodeDaten.proWeiterePerson) &&
        Objects.equals(this.kinder0018, gesuchsperiodeDaten.kinder0018) &&
        Objects.equals(this.jugendlicheErwachsene1925, gesuchsperiodeDaten.jugendlicheErwachsene1925) &&
        Objects.equals(this.erwachsene2699, gesuchsperiodeDaten.erwachsene2699) &&
        Objects.equals(this.wohnkostenFam1pers, gesuchsperiodeDaten.wohnkostenFam1pers) &&
        Objects.equals(this.wohnkostenFam2pers, gesuchsperiodeDaten.wohnkostenFam2pers) &&
        Objects.equals(this.wohnkostenFam3pers, gesuchsperiodeDaten.wohnkostenFam3pers) &&
        Objects.equals(this.wohnkostenFam4pers, gesuchsperiodeDaten.wohnkostenFam4pers) &&
        Objects.equals(this.wohnkostenFam5pluspers, gesuchsperiodeDaten.wohnkostenFam5pluspers) &&
        Objects.equals(this.wohnkostenPersoenlich1pers, gesuchsperiodeDaten.wohnkostenPersoenlich1pers) &&
        Objects.equals(this.wohnkostenPersoenlich2pers, gesuchsperiodeDaten.wohnkostenPersoenlich2pers) &&
        Objects.equals(this.wohnkostenPersoenlich3pers, gesuchsperiodeDaten.wohnkostenPersoenlich3pers) &&
        Objects.equals(this.wohnkostenPersoenlich4pers, gesuchsperiodeDaten.wohnkostenPersoenlich4pers) &&
        Objects.equals(this.wohnkostenPersoenlich5pluspers, gesuchsperiodeDaten.wohnkostenPersoenlich5pluspers) &&
        Objects.equals(this.freibetragVermögen, gesuchsperiodeDaten.freibetragVermögen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fiskaljahr, gesuchsjahr, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, aufschaltterminStopp, einreichefristNormal, einreichefristReduziert, ausbKostenSekII, ausbKostenTertiaer, freibetragErwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz, fEinkommensfreibetrag, fVermoegensfreibetrag, fVermogenSatzAngerechnet, integrationszulage, limiteEkFreibetragIntegrationszulag, stipLimiteMinimalstipendium, person1, personen2, personen3, personen4, personen5, personen6, personen7, proWeiterePerson, kinder0018, jugendlicheErwachsene1925, erwachsene2699, wohnkostenFam1pers, wohnkostenFam2pers, wohnkostenFam3pers, wohnkostenFam4pers, wohnkostenFam5pluspers, wohnkostenPersoenlich1pers, wohnkostenPersoenlich2pers, wohnkostenPersoenlich3pers, wohnkostenPersoenlich4pers, wohnkostenPersoenlich5pluspers, freibetragVermögen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeDatenDto {\n");
    
    sb.append("    fiskaljahr: ").append(toIndentedString(fiskaljahr)).append("\n");
    sb.append("    gesuchsjahr: ").append(toIndentedString(gesuchsjahr)).append("\n");
    sb.append("    gesuchsperiodeStart: ").append(toIndentedString(gesuchsperiodeStart)).append("\n");
    sb.append("    gesuchsperiodeStopp: ").append(toIndentedString(gesuchsperiodeStopp)).append("\n");
    sb.append("    aufschaltterminStart: ").append(toIndentedString(aufschaltterminStart)).append("\n");
    sb.append("    aufschaltterminStopp: ").append(toIndentedString(aufschaltterminStopp)).append("\n");
    sb.append("    einreichefristNormal: ").append(toIndentedString(einreichefristNormal)).append("\n");
    sb.append("    einreichefristReduziert: ").append(toIndentedString(einreichefristReduziert)).append("\n");
    sb.append("    ausbKostenSekII: ").append(toIndentedString(ausbKostenSekII)).append("\n");
    sb.append("    ausbKostenTertiaer: ").append(toIndentedString(ausbKostenTertiaer)).append("\n");
    sb.append("    freibetragErwerbseinkommen: ").append(toIndentedString(freibetragErwerbseinkommen)).append("\n");
    sb.append("    einkommensfreibetrag: ").append(toIndentedString(einkommensfreibetrag)).append("\n");
    sb.append("    elternbeteiligungssatz: ").append(toIndentedString(elternbeteiligungssatz)).append("\n");
    sb.append("    fEinkommensfreibetrag: ").append(toIndentedString(fEinkommensfreibetrag)).append("\n");
    sb.append("    fVermoegensfreibetrag: ").append(toIndentedString(fVermoegensfreibetrag)).append("\n");
    sb.append("    fVermogenSatzAngerechnet: ").append(toIndentedString(fVermogenSatzAngerechnet)).append("\n");
    sb.append("    integrationszulage: ").append(toIndentedString(integrationszulage)).append("\n");
    sb.append("    limiteEkFreibetragIntegrationszulag: ").append(toIndentedString(limiteEkFreibetragIntegrationszulag)).append("\n");
    sb.append("    stipLimiteMinimalstipendium: ").append(toIndentedString(stipLimiteMinimalstipendium)).append("\n");
    sb.append("    person1: ").append(toIndentedString(person1)).append("\n");
    sb.append("    personen2: ").append(toIndentedString(personen2)).append("\n");
    sb.append("    personen3: ").append(toIndentedString(personen3)).append("\n");
    sb.append("    personen4: ").append(toIndentedString(personen4)).append("\n");
    sb.append("    personen5: ").append(toIndentedString(personen5)).append("\n");
    sb.append("    personen6: ").append(toIndentedString(personen6)).append("\n");
    sb.append("    personen7: ").append(toIndentedString(personen7)).append("\n");
    sb.append("    proWeiterePerson: ").append(toIndentedString(proWeiterePerson)).append("\n");
    sb.append("    kinder0018: ").append(toIndentedString(kinder0018)).append("\n");
    sb.append("    jugendlicheErwachsene1925: ").append(toIndentedString(jugendlicheErwachsene1925)).append("\n");
    sb.append("    erwachsene2699: ").append(toIndentedString(erwachsene2699)).append("\n");
    sb.append("    wohnkostenFam1pers: ").append(toIndentedString(wohnkostenFam1pers)).append("\n");
    sb.append("    wohnkostenFam2pers: ").append(toIndentedString(wohnkostenFam2pers)).append("\n");
    sb.append("    wohnkostenFam3pers: ").append(toIndentedString(wohnkostenFam3pers)).append("\n");
    sb.append("    wohnkostenFam4pers: ").append(toIndentedString(wohnkostenFam4pers)).append("\n");
    sb.append("    wohnkostenFam5pluspers: ").append(toIndentedString(wohnkostenFam5pluspers)).append("\n");
    sb.append("    wohnkostenPersoenlich1pers: ").append(toIndentedString(wohnkostenPersoenlich1pers)).append("\n");
    sb.append("    wohnkostenPersoenlich2pers: ").append(toIndentedString(wohnkostenPersoenlich2pers)).append("\n");
    sb.append("    wohnkostenPersoenlich3pers: ").append(toIndentedString(wohnkostenPersoenlich3pers)).append("\n");
    sb.append("    wohnkostenPersoenlich4pers: ").append(toIndentedString(wohnkostenPersoenlich4pers)).append("\n");
    sb.append("    wohnkostenPersoenlich5pluspers: ").append(toIndentedString(wohnkostenPersoenlich5pluspers)).append("\n");
    sb.append("    freibetragVermögen: ").append(toIndentedString(freibetragVermögen)).append("\n");
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

