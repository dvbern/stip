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



@JsonTypeName("GesuchsperiodeCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeCreateDto  implements Serializable {
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
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
  private @Valid BigDecimal freibetragVermoegen;
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
  private @Valid LocalDate einreichfrist;
  private @Valid LocalDate aufschaltdatum;

  /**
   **/
  public GesuchsperiodeCreateDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  @NotNull
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public GesuchsperiodeCreateDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  @NotNull
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   * 
   **/
  public GesuchsperiodeCreateDto fiskaljahr(String fiskaljahr) {
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
  public GesuchsperiodeCreateDto gesuchsjahr(String gesuchsjahr) {
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
  public GesuchsperiodeCreateDto gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
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
  public GesuchsperiodeCreateDto gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
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
  public GesuchsperiodeCreateDto aufschaltterminStart(LocalDate aufschaltterminStart) {
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
  public GesuchsperiodeCreateDto aufschaltterminStopp(LocalDate aufschaltterminStopp) {
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
  public GesuchsperiodeCreateDto einreichefristNormal(LocalDate einreichefristNormal) {
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
  public GesuchsperiodeCreateDto einreichefristReduziert(LocalDate einreichefristReduziert) {
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
  public GesuchsperiodeCreateDto ausbKostenSekII(BigDecimal ausbKostenSekII) {
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
  public GesuchsperiodeCreateDto ausbKostenTertiaer(BigDecimal ausbKostenTertiaer) {
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
  public GesuchsperiodeCreateDto freibetragVermoegen(BigDecimal freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
    return this;
  }

  
  @JsonProperty("freibetrag_vermoegen")
  @NotNull
  public BigDecimal getFreibetragVermoegen() {
    return freibetragVermoegen;
  }

  @JsonProperty("freibetrag_vermoegen")
  public void setFreibetragVermoegen(BigDecimal freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
  }

  /**
   * 
   **/
  public GesuchsperiodeCreateDto freibetragErwerbseinkommen(BigDecimal freibetragErwerbseinkommen) {
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
  public GesuchsperiodeCreateDto einkommensfreibetrag(BigDecimal einkommensfreibetrag) {
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
  public GesuchsperiodeCreateDto elternbeteiligungssatz(BigDecimal elternbeteiligungssatz) {
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
  public GesuchsperiodeCreateDto fEinkommensfreibetrag(BigDecimal fEinkommensfreibetrag) {
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
  public GesuchsperiodeCreateDto fVermoegensfreibetrag(BigDecimal fVermoegensfreibetrag) {
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
  public GesuchsperiodeCreateDto fVermogenSatzAngerechnet(BigDecimal fVermogenSatzAngerechnet) {
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
  public GesuchsperiodeCreateDto integrationszulage(BigDecimal integrationszulage) {
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
  public GesuchsperiodeCreateDto limiteEkFreibetragIntegrationszulag(BigDecimal limiteEkFreibetragIntegrationszulag) {
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
  public GesuchsperiodeCreateDto stipLimiteMinimalstipendium(BigDecimal stipLimiteMinimalstipendium) {
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
  public GesuchsperiodeCreateDto person1(BigDecimal person1) {
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
  public GesuchsperiodeCreateDto personen2(BigDecimal personen2) {
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
  public GesuchsperiodeCreateDto personen3(BigDecimal personen3) {
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
  public GesuchsperiodeCreateDto personen4(BigDecimal personen4) {
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
  public GesuchsperiodeCreateDto personen5(BigDecimal personen5) {
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
  public GesuchsperiodeCreateDto personen6(BigDecimal personen6) {
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
  public GesuchsperiodeCreateDto personen7(BigDecimal personen7) {
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
  public GesuchsperiodeCreateDto proWeiterePerson(BigDecimal proWeiterePerson) {
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
  public GesuchsperiodeCreateDto kinder0018(BigDecimal kinder0018) {
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
  public GesuchsperiodeCreateDto jugendlicheErwachsene1925(BigDecimal jugendlicheErwachsene1925) {
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
  public GesuchsperiodeCreateDto erwachsene2699(BigDecimal erwachsene2699) {
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
  public GesuchsperiodeCreateDto wohnkostenFam1pers(BigDecimal wohnkostenFam1pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam2pers(BigDecimal wohnkostenFam2pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam3pers(BigDecimal wohnkostenFam3pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam4pers(BigDecimal wohnkostenFam4pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam5pluspers(BigDecimal wohnkostenFam5pluspers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich1pers(BigDecimal wohnkostenPersoenlich1pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich2pers(BigDecimal wohnkostenPersoenlich2pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich3pers(BigDecimal wohnkostenPersoenlich3pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich4pers(BigDecimal wohnkostenPersoenlich4pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich5pluspers(BigDecimal wohnkostenPersoenlich5pluspers) {
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
   **/
  public GesuchsperiodeCreateDto einreichfrist(LocalDate einreichfrist) {
    this.einreichfrist = einreichfrist;
    return this;
  }

  
  @JsonProperty("einreichfrist")
  public LocalDate getEinreichfrist() {
    return einreichfrist;
  }

  @JsonProperty("einreichfrist")
  public void setEinreichfrist(LocalDate einreichfrist) {
    this.einreichfrist = einreichfrist;
  }

  /**
   **/
  public GesuchsperiodeCreateDto aufschaltdatum(LocalDate aufschaltdatum) {
    this.aufschaltdatum = aufschaltdatum;
    return this;
  }

  
  @JsonProperty("aufschaltdatum")
  public LocalDate getAufschaltdatum() {
    return aufschaltdatum;
  }

  @JsonProperty("aufschaltdatum")
  public void setAufschaltdatum(LocalDate aufschaltdatum) {
    this.aufschaltdatum = aufschaltdatum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeCreateDto gesuchsperiodeCreate = (GesuchsperiodeCreateDto) o;
    return Objects.equals(this.gueltigAb, gesuchsperiodeCreate.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchsperiodeCreate.gueltigBis) &&
        Objects.equals(this.fiskaljahr, gesuchsperiodeCreate.fiskaljahr) &&
        Objects.equals(this.gesuchsjahr, gesuchsperiodeCreate.gesuchsjahr) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiodeCreate.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiodeCreate.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiodeCreate.aufschaltterminStart) &&
        Objects.equals(this.aufschaltterminStopp, gesuchsperiodeCreate.aufschaltterminStopp) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiodeCreate.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiodeCreate.einreichefristReduziert) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiodeCreate.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiodeCreate.ausbKostenTertiaer) &&
        Objects.equals(this.freibetragVermoegen, gesuchsperiodeCreate.freibetragVermoegen) &&
        Objects.equals(this.freibetragErwerbseinkommen, gesuchsperiodeCreate.freibetragErwerbseinkommen) &&
        Objects.equals(this.einkommensfreibetrag, gesuchsperiodeCreate.einkommensfreibetrag) &&
        Objects.equals(this.elternbeteiligungssatz, gesuchsperiodeCreate.elternbeteiligungssatz) &&
        Objects.equals(this.fEinkommensfreibetrag, gesuchsperiodeCreate.fEinkommensfreibetrag) &&
        Objects.equals(this.fVermoegensfreibetrag, gesuchsperiodeCreate.fVermoegensfreibetrag) &&
        Objects.equals(this.fVermogenSatzAngerechnet, gesuchsperiodeCreate.fVermogenSatzAngerechnet) &&
        Objects.equals(this.integrationszulage, gesuchsperiodeCreate.integrationszulage) &&
        Objects.equals(this.limiteEkFreibetragIntegrationszulag, gesuchsperiodeCreate.limiteEkFreibetragIntegrationszulag) &&
        Objects.equals(this.stipLimiteMinimalstipendium, gesuchsperiodeCreate.stipLimiteMinimalstipendium) &&
        Objects.equals(this.person1, gesuchsperiodeCreate.person1) &&
        Objects.equals(this.personen2, gesuchsperiodeCreate.personen2) &&
        Objects.equals(this.personen3, gesuchsperiodeCreate.personen3) &&
        Objects.equals(this.personen4, gesuchsperiodeCreate.personen4) &&
        Objects.equals(this.personen5, gesuchsperiodeCreate.personen5) &&
        Objects.equals(this.personen6, gesuchsperiodeCreate.personen6) &&
        Objects.equals(this.personen7, gesuchsperiodeCreate.personen7) &&
        Objects.equals(this.proWeiterePerson, gesuchsperiodeCreate.proWeiterePerson) &&
        Objects.equals(this.kinder0018, gesuchsperiodeCreate.kinder0018) &&
        Objects.equals(this.jugendlicheErwachsene1925, gesuchsperiodeCreate.jugendlicheErwachsene1925) &&
        Objects.equals(this.erwachsene2699, gesuchsperiodeCreate.erwachsene2699) &&
        Objects.equals(this.wohnkostenFam1pers, gesuchsperiodeCreate.wohnkostenFam1pers) &&
        Objects.equals(this.wohnkostenFam2pers, gesuchsperiodeCreate.wohnkostenFam2pers) &&
        Objects.equals(this.wohnkostenFam3pers, gesuchsperiodeCreate.wohnkostenFam3pers) &&
        Objects.equals(this.wohnkostenFam4pers, gesuchsperiodeCreate.wohnkostenFam4pers) &&
        Objects.equals(this.wohnkostenFam5pluspers, gesuchsperiodeCreate.wohnkostenFam5pluspers) &&
        Objects.equals(this.wohnkostenPersoenlich1pers, gesuchsperiodeCreate.wohnkostenPersoenlich1pers) &&
        Objects.equals(this.wohnkostenPersoenlich2pers, gesuchsperiodeCreate.wohnkostenPersoenlich2pers) &&
        Objects.equals(this.wohnkostenPersoenlich3pers, gesuchsperiodeCreate.wohnkostenPersoenlich3pers) &&
        Objects.equals(this.wohnkostenPersoenlich4pers, gesuchsperiodeCreate.wohnkostenPersoenlich4pers) &&
        Objects.equals(this.wohnkostenPersoenlich5pluspers, gesuchsperiodeCreate.wohnkostenPersoenlich5pluspers) &&
        Objects.equals(this.einreichfrist, gesuchsperiodeCreate.einreichfrist) &&
        Objects.equals(this.aufschaltdatum, gesuchsperiodeCreate.aufschaltdatum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gueltigAb, gueltigBis, fiskaljahr, gesuchsjahr, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, aufschaltterminStopp, einreichefristNormal, einreichefristReduziert, ausbKostenSekII, ausbKostenTertiaer, freibetragVermoegen, freibetragErwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz, fEinkommensfreibetrag, fVermoegensfreibetrag, fVermogenSatzAngerechnet, integrationszulage, limiteEkFreibetragIntegrationszulag, stipLimiteMinimalstipendium, person1, personen2, personen3, personen4, personen5, personen6, personen7, proWeiterePerson, kinder0018, jugendlicheErwachsene1925, erwachsene2699, wohnkostenFam1pers, wohnkostenFam2pers, wohnkostenFam3pers, wohnkostenFam4pers, wohnkostenFam5pluspers, wohnkostenPersoenlich1pers, wohnkostenPersoenlich2pers, wohnkostenPersoenlich3pers, wohnkostenPersoenlich4pers, wohnkostenPersoenlich5pluspers, einreichfrist, aufschaltdatum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeCreateDto {\n");
    
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
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
    sb.append("    freibetragVermoegen: ").append(toIndentedString(freibetragVermoegen)).append("\n");
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
    sb.append("    einreichfrist: ").append(toIndentedString(einreichfrist)).append("\n");
    sb.append("    aufschaltdatum: ").append(toIndentedString(aufschaltdatum)).append("\n");
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

