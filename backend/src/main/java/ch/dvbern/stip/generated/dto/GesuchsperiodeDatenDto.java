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
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid String fiskaljahr;
  private @Valid LocalDate gesuchsperiodeStart;
  private @Valid LocalDate gesuchsperiodeStopp;
  private @Valid LocalDate aufschaltterminStart;
  private @Valid LocalDate aufschaltterminStopp;
  private @Valid LocalDate einreichefristNormal;
  private @Valid LocalDate einreichefristReduziert;
  private @Valid BigDecimal ausbKostenSekII;
  private @Valid BigDecimal ausbKostenTertiaer;
  private @Valid BigDecimal bEinkommenfreibetrag;
  private @Valid BigDecimal bVermogenSatzAngerechnet;
  private @Valid BigDecimal bVerpfAuswaertsTagessatz;
  private @Valid BigDecimal elternbeteiligungssatz;
  private @Valid BigDecimal fEinkommensfreibetrag;
  private @Valid BigDecimal fVermoegensfreibetrag;
  private @Valid BigDecimal fVermogenSatzAngerechnet;
  private @Valid BigDecimal integrationszulage;
  private @Valid BigDecimal limiteEkFreibetragIntegrationszulag;
  private @Valid BigDecimal stipLimiteMinimalstipendium;
  private @Valid BigDecimal person1;
  private @Valid BigDecimal person2;
  private @Valid BigDecimal person3;
  private @Valid BigDecimal person4;
  private @Valid BigDecimal person5;
  private @Valid BigDecimal person6;
  private @Valid BigDecimal person7;
  private @Valid BigDecimal ppP8;
  private @Valid BigDecimal _00_18;
  private @Valid BigDecimal _19_25;
  private @Valid BigDecimal _26_99;
  private @Valid BigDecimal bB1Pers;
  private @Valid BigDecimal bB2Pers;
  private @Valid BigDecimal bB3Pers;
  private @Valid BigDecimal bB4Pers;
  private @Valid BigDecimal bB5Pers;
  private @Valid BigDecimal fB1Pers;
  private @Valid BigDecimal fB2Pers;
  private @Valid BigDecimal fB3Pers;
  private @Valid BigDecimal fB4Pers;
  private @Valid BigDecimal fB5Pers;

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

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
  public GesuchsperiodeDatenDto bEinkommenfreibetrag(BigDecimal bEinkommenfreibetrag) {
    this.bEinkommenfreibetrag = bEinkommenfreibetrag;
    return this;
  }

  
  @JsonProperty("b_Einkommenfreibetrag")
  @NotNull
  public BigDecimal getbEinkommenfreibetrag() {
    return bEinkommenfreibetrag;
  }

  @JsonProperty("b_Einkommenfreibetrag")
  public void setbEinkommenfreibetrag(BigDecimal bEinkommenfreibetrag) {
    this.bEinkommenfreibetrag = bEinkommenfreibetrag;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bVermogenSatzAngerechnet(BigDecimal bVermogenSatzAngerechnet) {
    this.bVermogenSatzAngerechnet = bVermogenSatzAngerechnet;
    return this;
  }

  
  @JsonProperty("b_VermogenSatzAngerechnet")
  @NotNull
  public BigDecimal getbVermogenSatzAngerechnet() {
    return bVermogenSatzAngerechnet;
  }

  @JsonProperty("b_VermogenSatzAngerechnet")
  public void setbVermogenSatzAngerechnet(BigDecimal bVermogenSatzAngerechnet) {
    this.bVermogenSatzAngerechnet = bVermogenSatzAngerechnet;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bVerpfAuswaertsTagessatz(BigDecimal bVerpfAuswaertsTagessatz) {
    this.bVerpfAuswaertsTagessatz = bVerpfAuswaertsTagessatz;
    return this;
  }

  
  @JsonProperty("b_Verpf_Auswaerts_Tagessatz")
  @NotNull
  public BigDecimal getbVerpfAuswaertsTagessatz() {
    return bVerpfAuswaertsTagessatz;
  }

  @JsonProperty("b_Verpf_Auswaerts_Tagessatz")
  public void setbVerpfAuswaertsTagessatz(BigDecimal bVerpfAuswaertsTagessatz) {
    this.bVerpfAuswaertsTagessatz = bVerpfAuswaertsTagessatz;
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
  public GesuchsperiodeDatenDto person2(BigDecimal person2) {
    this.person2 = person2;
    return this;
  }

  
  @JsonProperty("person_2")
  @NotNull
  public BigDecimal getPerson2() {
    return person2;
  }

  @JsonProperty("person_2")
  public void setPerson2(BigDecimal person2) {
    this.person2 = person2;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto person3(BigDecimal person3) {
    this.person3 = person3;
    return this;
  }

  
  @JsonProperty("person_3")
  @NotNull
  public BigDecimal getPerson3() {
    return person3;
  }

  @JsonProperty("person_3")
  public void setPerson3(BigDecimal person3) {
    this.person3 = person3;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto person4(BigDecimal person4) {
    this.person4 = person4;
    return this;
  }

  
  @JsonProperty("person_4")
  @NotNull
  public BigDecimal getPerson4() {
    return person4;
  }

  @JsonProperty("person_4")
  public void setPerson4(BigDecimal person4) {
    this.person4 = person4;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto person5(BigDecimal person5) {
    this.person5 = person5;
    return this;
  }

  
  @JsonProperty("person_5")
  @NotNull
  public BigDecimal getPerson5() {
    return person5;
  }

  @JsonProperty("person_5")
  public void setPerson5(BigDecimal person5) {
    this.person5 = person5;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto person6(BigDecimal person6) {
    this.person6 = person6;
    return this;
  }

  
  @JsonProperty("person_6")
  @NotNull
  public BigDecimal getPerson6() {
    return person6;
  }

  @JsonProperty("person_6")
  public void setPerson6(BigDecimal person6) {
    this.person6 = person6;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto person7(BigDecimal person7) {
    this.person7 = person7;
    return this;
  }

  
  @JsonProperty("person_7")
  @NotNull
  public BigDecimal getPerson7() {
    return person7;
  }

  @JsonProperty("person_7")
  public void setPerson7(BigDecimal person7) {
    this.person7 = person7;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto ppP8(BigDecimal ppP8) {
    this.ppP8 = ppP8;
    return this;
  }

  
  @JsonProperty("ppP_8")
  @NotNull
  public BigDecimal getPpP8() {
    return ppP8;
  }

  @JsonProperty("ppP_8")
  public void setPpP8(BigDecimal ppP8) {
    this.ppP8 = ppP8;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto _00_18(BigDecimal _00_18) {
    this._00_18 = _00_18;
    return this;
  }

  
  @JsonProperty("_00_18")
  @NotNull
  public BigDecimal get0018() {
    return _00_18;
  }

  @JsonProperty("_00_18")
  public void set0018(BigDecimal _00_18) {
    this._00_18 = _00_18;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto _19_25(BigDecimal _19_25) {
    this._19_25 = _19_25;
    return this;
  }

  
  @JsonProperty("_19_25")
  @NotNull
  public BigDecimal get1925() {
    return _19_25;
  }

  @JsonProperty("_19_25")
  public void set1925(BigDecimal _19_25) {
    this._19_25 = _19_25;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto _26_99(BigDecimal _26_99) {
    this._26_99 = _26_99;
    return this;
  }

  
  @JsonProperty("_26_99")
  @NotNull
  public BigDecimal get2699() {
    return _26_99;
  }

  @JsonProperty("_26_99")
  public void set2699(BigDecimal _26_99) {
    this._26_99 = _26_99;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bB1Pers(BigDecimal bB1Pers) {
    this.bB1Pers = bB1Pers;
    return this;
  }

  
  @JsonProperty("bB_1Pers")
  @NotNull
  public BigDecimal getbB1Pers() {
    return bB1Pers;
  }

  @JsonProperty("bB_1Pers")
  public void setbB1Pers(BigDecimal bB1Pers) {
    this.bB1Pers = bB1Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bB2Pers(BigDecimal bB2Pers) {
    this.bB2Pers = bB2Pers;
    return this;
  }

  
  @JsonProperty("bB_2Pers")
  @NotNull
  public BigDecimal getbB2Pers() {
    return bB2Pers;
  }

  @JsonProperty("bB_2Pers")
  public void setbB2Pers(BigDecimal bB2Pers) {
    this.bB2Pers = bB2Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bB3Pers(BigDecimal bB3Pers) {
    this.bB3Pers = bB3Pers;
    return this;
  }

  
  @JsonProperty("bB_3Pers")
  @NotNull
  public BigDecimal getbB3Pers() {
    return bB3Pers;
  }

  @JsonProperty("bB_3Pers")
  public void setbB3Pers(BigDecimal bB3Pers) {
    this.bB3Pers = bB3Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bB4Pers(BigDecimal bB4Pers) {
    this.bB4Pers = bB4Pers;
    return this;
  }

  
  @JsonProperty("bB_4Pers")
  @NotNull
  public BigDecimal getbB4Pers() {
    return bB4Pers;
  }

  @JsonProperty("bB_4Pers")
  public void setbB4Pers(BigDecimal bB4Pers) {
    this.bB4Pers = bB4Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto bB5Pers(BigDecimal bB5Pers) {
    this.bB5Pers = bB5Pers;
    return this;
  }

  
  @JsonProperty("bB_5Pers")
  @NotNull
  public BigDecimal getbB5Pers() {
    return bB5Pers;
  }

  @JsonProperty("bB_5Pers")
  public void setbB5Pers(BigDecimal bB5Pers) {
    this.bB5Pers = bB5Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fB1Pers(BigDecimal fB1Pers) {
    this.fB1Pers = fB1Pers;
    return this;
  }

  
  @JsonProperty("fB_1Pers")
  @NotNull
  public BigDecimal getfB1Pers() {
    return fB1Pers;
  }

  @JsonProperty("fB_1Pers")
  public void setfB1Pers(BigDecimal fB1Pers) {
    this.fB1Pers = fB1Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fB2Pers(BigDecimal fB2Pers) {
    this.fB2Pers = fB2Pers;
    return this;
  }

  
  @JsonProperty("fB_2Pers")
  @NotNull
  public BigDecimal getfB2Pers() {
    return fB2Pers;
  }

  @JsonProperty("fB_2Pers")
  public void setfB2Pers(BigDecimal fB2Pers) {
    this.fB2Pers = fB2Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fB3Pers(BigDecimal fB3Pers) {
    this.fB3Pers = fB3Pers;
    return this;
  }

  
  @JsonProperty("fB_3Pers")
  @NotNull
  public BigDecimal getfB3Pers() {
    return fB3Pers;
  }

  @JsonProperty("fB_3Pers")
  public void setfB3Pers(BigDecimal fB3Pers) {
    this.fB3Pers = fB3Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fB4Pers(BigDecimal fB4Pers) {
    this.fB4Pers = fB4Pers;
    return this;
  }

  
  @JsonProperty("fB_4Pers")
  @NotNull
  public BigDecimal getfB4Pers() {
    return fB4Pers;
  }

  @JsonProperty("fB_4Pers")
  public void setfB4Pers(BigDecimal fB4Pers) {
    this.fB4Pers = fB4Pers;
  }

  /**
   * 
   **/
  public GesuchsperiodeDatenDto fB5Pers(BigDecimal fB5Pers) {
    this.fB5Pers = fB5Pers;
    return this;
  }

  
  @JsonProperty("fB_5Pers")
  @NotNull
  public BigDecimal getfB5Pers() {
    return fB5Pers;
  }

  @JsonProperty("fB_5Pers")
  public void setfB5Pers(BigDecimal fB5Pers) {
    this.fB5Pers = fB5Pers;
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
    return Objects.equals(this.bezeichnungDe, gesuchsperiodeDaten.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiodeDaten.bezeichnungFr) &&
        Objects.equals(this.fiskaljahr, gesuchsperiodeDaten.fiskaljahr) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiodeDaten.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiodeDaten.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiodeDaten.aufschaltterminStart) &&
        Objects.equals(this.aufschaltterminStopp, gesuchsperiodeDaten.aufschaltterminStopp) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiodeDaten.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiodeDaten.einreichefristReduziert) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiodeDaten.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiodeDaten.ausbKostenTertiaer) &&
        Objects.equals(this.bEinkommenfreibetrag, gesuchsperiodeDaten.bEinkommenfreibetrag) &&
        Objects.equals(this.bVermogenSatzAngerechnet, gesuchsperiodeDaten.bVermogenSatzAngerechnet) &&
        Objects.equals(this.bVerpfAuswaertsTagessatz, gesuchsperiodeDaten.bVerpfAuswaertsTagessatz) &&
        Objects.equals(this.elternbeteiligungssatz, gesuchsperiodeDaten.elternbeteiligungssatz) &&
        Objects.equals(this.fEinkommensfreibetrag, gesuchsperiodeDaten.fEinkommensfreibetrag) &&
        Objects.equals(this.fVermoegensfreibetrag, gesuchsperiodeDaten.fVermoegensfreibetrag) &&
        Objects.equals(this.fVermogenSatzAngerechnet, gesuchsperiodeDaten.fVermogenSatzAngerechnet) &&
        Objects.equals(this.integrationszulage, gesuchsperiodeDaten.integrationszulage) &&
        Objects.equals(this.limiteEkFreibetragIntegrationszulag, gesuchsperiodeDaten.limiteEkFreibetragIntegrationszulag) &&
        Objects.equals(this.stipLimiteMinimalstipendium, gesuchsperiodeDaten.stipLimiteMinimalstipendium) &&
        Objects.equals(this.person1, gesuchsperiodeDaten.person1) &&
        Objects.equals(this.person2, gesuchsperiodeDaten.person2) &&
        Objects.equals(this.person3, gesuchsperiodeDaten.person3) &&
        Objects.equals(this.person4, gesuchsperiodeDaten.person4) &&
        Objects.equals(this.person5, gesuchsperiodeDaten.person5) &&
        Objects.equals(this.person6, gesuchsperiodeDaten.person6) &&
        Objects.equals(this.person7, gesuchsperiodeDaten.person7) &&
        Objects.equals(this.ppP8, gesuchsperiodeDaten.ppP8) &&
        Objects.equals(this._00_18, gesuchsperiodeDaten._00_18) &&
        Objects.equals(this._19_25, gesuchsperiodeDaten._19_25) &&
        Objects.equals(this._26_99, gesuchsperiodeDaten._26_99) &&
        Objects.equals(this.bB1Pers, gesuchsperiodeDaten.bB1Pers) &&
        Objects.equals(this.bB2Pers, gesuchsperiodeDaten.bB2Pers) &&
        Objects.equals(this.bB3Pers, gesuchsperiodeDaten.bB3Pers) &&
        Objects.equals(this.bB4Pers, gesuchsperiodeDaten.bB4Pers) &&
        Objects.equals(this.bB5Pers, gesuchsperiodeDaten.bB5Pers) &&
        Objects.equals(this.fB1Pers, gesuchsperiodeDaten.fB1Pers) &&
        Objects.equals(this.fB2Pers, gesuchsperiodeDaten.fB2Pers) &&
        Objects.equals(this.fB3Pers, gesuchsperiodeDaten.fB3Pers) &&
        Objects.equals(this.fB4Pers, gesuchsperiodeDaten.fB4Pers) &&
        Objects.equals(this.fB5Pers, gesuchsperiodeDaten.fB5Pers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, fiskaljahr, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, aufschaltterminStopp, einreichefristNormal, einreichefristReduziert, ausbKostenSekII, ausbKostenTertiaer, bEinkommenfreibetrag, bVermogenSatzAngerechnet, bVerpfAuswaertsTagessatz, elternbeteiligungssatz, fEinkommensfreibetrag, fVermoegensfreibetrag, fVermogenSatzAngerechnet, integrationszulage, limiteEkFreibetragIntegrationszulag, stipLimiteMinimalstipendium, person1, person2, person3, person4, person5, person6, person7, ppP8, _00_18, _19_25, _26_99, bB1Pers, bB2Pers, bB3Pers, bB4Pers, bB5Pers, fB1Pers, fB2Pers, fB3Pers, fB4Pers, fB5Pers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeDatenDto {\n");
    
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    fiskaljahr: ").append(toIndentedString(fiskaljahr)).append("\n");
    sb.append("    gesuchsperiodeStart: ").append(toIndentedString(gesuchsperiodeStart)).append("\n");
    sb.append("    gesuchsperiodeStopp: ").append(toIndentedString(gesuchsperiodeStopp)).append("\n");
    sb.append("    aufschaltterminStart: ").append(toIndentedString(aufschaltterminStart)).append("\n");
    sb.append("    aufschaltterminStopp: ").append(toIndentedString(aufschaltterminStopp)).append("\n");
    sb.append("    einreichefristNormal: ").append(toIndentedString(einreichefristNormal)).append("\n");
    sb.append("    einreichefristReduziert: ").append(toIndentedString(einreichefristReduziert)).append("\n");
    sb.append("    ausbKostenSekII: ").append(toIndentedString(ausbKostenSekII)).append("\n");
    sb.append("    ausbKostenTertiaer: ").append(toIndentedString(ausbKostenTertiaer)).append("\n");
    sb.append("    bEinkommenfreibetrag: ").append(toIndentedString(bEinkommenfreibetrag)).append("\n");
    sb.append("    bVermogenSatzAngerechnet: ").append(toIndentedString(bVermogenSatzAngerechnet)).append("\n");
    sb.append("    bVerpfAuswaertsTagessatz: ").append(toIndentedString(bVerpfAuswaertsTagessatz)).append("\n");
    sb.append("    elternbeteiligungssatz: ").append(toIndentedString(elternbeteiligungssatz)).append("\n");
    sb.append("    fEinkommensfreibetrag: ").append(toIndentedString(fEinkommensfreibetrag)).append("\n");
    sb.append("    fVermoegensfreibetrag: ").append(toIndentedString(fVermoegensfreibetrag)).append("\n");
    sb.append("    fVermogenSatzAngerechnet: ").append(toIndentedString(fVermogenSatzAngerechnet)).append("\n");
    sb.append("    integrationszulage: ").append(toIndentedString(integrationszulage)).append("\n");
    sb.append("    limiteEkFreibetragIntegrationszulag: ").append(toIndentedString(limiteEkFreibetragIntegrationszulag)).append("\n");
    sb.append("    stipLimiteMinimalstipendium: ").append(toIndentedString(stipLimiteMinimalstipendium)).append("\n");
    sb.append("    person1: ").append(toIndentedString(person1)).append("\n");
    sb.append("    person2: ").append(toIndentedString(person2)).append("\n");
    sb.append("    person3: ").append(toIndentedString(person3)).append("\n");
    sb.append("    person4: ").append(toIndentedString(person4)).append("\n");
    sb.append("    person5: ").append(toIndentedString(person5)).append("\n");
    sb.append("    person6: ").append(toIndentedString(person6)).append("\n");
    sb.append("    person7: ").append(toIndentedString(person7)).append("\n");
    sb.append("    ppP8: ").append(toIndentedString(ppP8)).append("\n");
    sb.append("    _00_18: ").append(toIndentedString(_00_18)).append("\n");
    sb.append("    _19_25: ").append(toIndentedString(_19_25)).append("\n");
    sb.append("    _26_99: ").append(toIndentedString(_26_99)).append("\n");
    sb.append("    bB1Pers: ").append(toIndentedString(bB1Pers)).append("\n");
    sb.append("    bB2Pers: ").append(toIndentedString(bB2Pers)).append("\n");
    sb.append("    bB3Pers: ").append(toIndentedString(bB3Pers)).append("\n");
    sb.append("    bB4Pers: ").append(toIndentedString(bB4Pers)).append("\n");
    sb.append("    bB5Pers: ").append(toIndentedString(bB5Pers)).append("\n");
    sb.append("    fB1Pers: ").append(toIndentedString(fB1Pers)).append("\n");
    sb.append("    fB2Pers: ").append(toIndentedString(fB2Pers)).append("\n");
    sb.append("    fB3Pers: ").append(toIndentedString(fB3Pers)).append("\n");
    sb.append("    fB4Pers: ").append(toIndentedString(fB4Pers)).append("\n");
    sb.append("    fB5Pers: ").append(toIndentedString(fB5Pers)).append("\n");
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

