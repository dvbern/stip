package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GueltigkeitStatusDto;
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



@JsonTypeName("GesuchsperiodeWithDaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeWithDatenDto  implements Serializable {
  private @Valid UUID id;
  private @Valid GueltigkeitStatusDto gueltigkeitStatus;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid Integer fiskaljahr;
  private @Valid UUID gesuchsjahrId;
  private @Valid LocalDate gesuchsperiodeStart;
  private @Valid LocalDate gesuchsperiodeStopp;
  private @Valid LocalDate aufschaltterminStart;
  private @Valid LocalDate aufschaltterminStopp;
  private @Valid LocalDate einreichefristNormal;
  private @Valid LocalDate einreichefristReduziert;
  private @Valid Integer ausbKostenSekII;
  private @Valid Integer ausbKostenTertiaer;
  private @Valid Integer freibetragVermoegen;
  private @Valid Integer freibetragErwerbseinkommen;
  private @Valid Integer einkommensfreibetrag;
  private @Valid Integer elternbeteiligungssatz;
  private @Valid Integer vermogenSatzAngerechnet;
  private @Valid Integer integrationszulage;
  private @Valid Integer limiteEkFreibetragIntegrationszulag;
  private @Valid Integer stipLimiteMinimalstipendium;
  private @Valid Integer person1;
  private @Valid Integer personen2;
  private @Valid Integer personen3;
  private @Valid Integer personen4;
  private @Valid Integer personen5;
  private @Valid Integer personen6;
  private @Valid Integer personen7;
  private @Valid Integer proWeiterePerson;
  private @Valid Integer kinder0018;
  private @Valid Integer jugendlicheErwachsene1925;
  private @Valid Integer erwachsene2699;
  private @Valid Integer wohnkostenFam1pers;
  private @Valid Integer wohnkostenFam2pers;
  private @Valid Integer wohnkostenFam3pers;
  private @Valid Integer wohnkostenFam4pers;
  private @Valid Integer wohnkostenFam5pluspers;
  private @Valid Integer wohnkostenPersoenlich1pers;
  private @Valid Integer wohnkostenPersoenlich2pers;
  private @Valid Integer wohnkostenPersoenlich3pers;
  private @Valid Integer wohnkostenPersoenlich4pers;
  private @Valid Integer wohnkostenPersoenlich5pluspers;
  private @Valid Integer preisProMahlzeit;
  private @Valid Integer maxSaeule3a;
  private @Valid Integer anzahlWochenLehre;
  private @Valid Integer anzahlWochenSchule;
  private @Valid LocalDate einreichfrist;

  /**
   **/
  public GesuchsperiodeWithDatenDto id(UUID id) {
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
  public GesuchsperiodeWithDatenDto gueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
    return this;
  }

  
  @JsonProperty("gueltigkeitStatus")
  @NotNull
  public GueltigkeitStatusDto getGueltigkeitStatus() {
    return gueltigkeitStatus;
  }

  @JsonProperty("gueltigkeitStatus")
  public void setGueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto bezeichnungDe(String bezeichnungDe) {
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
   **/
  public GesuchsperiodeWithDatenDto bezeichnungFr(String bezeichnungFr) {
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
   **/
  public GesuchsperiodeWithDatenDto fiskaljahr(Integer fiskaljahr) {
    this.fiskaljahr = fiskaljahr;
    return this;
  }

  
  @JsonProperty("fiskaljahr")
  @NotNull
  public Integer getFiskaljahr() {
    return fiskaljahr;
  }

  @JsonProperty("fiskaljahr")
  public void setFiskaljahr(Integer fiskaljahr) {
    this.fiskaljahr = fiskaljahr;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto gesuchsjahrId(UUID gesuchsjahrId) {
    this.gesuchsjahrId = gesuchsjahrId;
    return this;
  }

  
  @JsonProperty("gesuchsjahrId")
  @NotNull
  public UUID getGesuchsjahrId() {
    return gesuchsjahrId;
  }

  @JsonProperty("gesuchsjahrId")
  public void setGesuchsjahrId(UUID gesuchsjahrId) {
    this.gesuchsjahrId = gesuchsjahrId;
  }

  /**
   * dd.MM.YYYY
   **/
  public GesuchsperiodeWithDatenDto gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
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
  public GesuchsperiodeWithDatenDto gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
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
  public GesuchsperiodeWithDatenDto aufschaltterminStart(LocalDate aufschaltterminStart) {
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
  public GesuchsperiodeWithDatenDto aufschaltterminStopp(LocalDate aufschaltterminStopp) {
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
  public GesuchsperiodeWithDatenDto einreichefristNormal(LocalDate einreichefristNormal) {
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
  public GesuchsperiodeWithDatenDto einreichefristReduziert(LocalDate einreichefristReduziert) {
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
   **/
  public GesuchsperiodeWithDatenDto ausbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
    return this;
  }

  
  @JsonProperty("ausbKosten_SekII")
  @NotNull
  public Integer getAusbKostenSekII() {
    return ausbKostenSekII;
  }

  @JsonProperty("ausbKosten_SekII")
  public void setAusbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto ausbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
    return this;
  }

  
  @JsonProperty("ausbKosten_Tertiaer")
  @NotNull
  public Integer getAusbKostenTertiaer() {
    return ausbKostenTertiaer;
  }

  @JsonProperty("ausbKosten_Tertiaer")
  public void setAusbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto freibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
    return this;
  }

  
  @JsonProperty("freibetrag_vermoegen")
  @NotNull
  public Integer getFreibetragVermoegen() {
    return freibetragVermoegen;
  }

  @JsonProperty("freibetrag_vermoegen")
  public void setFreibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
    return this;
  }

  
  @JsonProperty("freibetrag_erwerbseinkommen")
  @NotNull
  public Integer getFreibetragErwerbseinkommen() {
    return freibetragErwerbseinkommen;
  }

  @JsonProperty("freibetrag_erwerbseinkommen")
  public void setFreibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto einkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty("einkommensfreibetrag")
  @NotNull
  public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty("einkommensfreibetrag")
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto elternbeteiligungssatz(Integer elternbeteiligungssatz) {
    this.elternbeteiligungssatz = elternbeteiligungssatz;
    return this;
  }

  
  @JsonProperty("elternbeteiligungssatz")
  @NotNull
  public Integer getElternbeteiligungssatz() {
    return elternbeteiligungssatz;
  }

  @JsonProperty("elternbeteiligungssatz")
  public void setElternbeteiligungssatz(Integer elternbeteiligungssatz) {
    this.elternbeteiligungssatz = elternbeteiligungssatz;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto vermogenSatzAngerechnet(Integer vermogenSatzAngerechnet) {
    this.vermogenSatzAngerechnet = vermogenSatzAngerechnet;
    return this;
  }

  
  @JsonProperty("vermogenSatzAngerechnet")
  @NotNull
  public Integer getVermogenSatzAngerechnet() {
    return vermogenSatzAngerechnet;
  }

  @JsonProperty("vermogenSatzAngerechnet")
  public void setVermogenSatzAngerechnet(Integer vermogenSatzAngerechnet) {
    this.vermogenSatzAngerechnet = vermogenSatzAngerechnet;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto integrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
    return this;
  }

  
  @JsonProperty("integrationszulage")
  @NotNull
  public Integer getIntegrationszulage() {
    return integrationszulage;
  }

  @JsonProperty("integrationszulage")
  public void setIntegrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto limiteEkFreibetragIntegrationszulag(Integer limiteEkFreibetragIntegrationszulag) {
    this.limiteEkFreibetragIntegrationszulag = limiteEkFreibetragIntegrationszulag;
    return this;
  }

  
  @JsonProperty("limite_EkFreibetrag_Integrationszulag")
  @NotNull
  public Integer getLimiteEkFreibetragIntegrationszulag() {
    return limiteEkFreibetragIntegrationszulag;
  }

  @JsonProperty("limite_EkFreibetrag_Integrationszulag")
  public void setLimiteEkFreibetragIntegrationszulag(Integer limiteEkFreibetragIntegrationszulag) {
    this.limiteEkFreibetragIntegrationszulag = limiteEkFreibetragIntegrationszulag;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
    return this;
  }

  
  @JsonProperty("stipLimite_Minimalstipendium")
  @NotNull
  public Integer getStipLimiteMinimalstipendium() {
    return stipLimiteMinimalstipendium;
  }

  @JsonProperty("stipLimite_Minimalstipendium")
  public void setStipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto person1(Integer person1) {
    this.person1 = person1;
    return this;
  }

  
  @JsonProperty("person_1")
  @NotNull
  public Integer getPerson1() {
    return person1;
  }

  @JsonProperty("person_1")
  public void setPerson1(Integer person1) {
    this.person1 = person1;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto personen2(Integer personen2) {
    this.personen2 = personen2;
    return this;
  }

  
  @JsonProperty("personen_2")
  @NotNull
  public Integer getPersonen2() {
    return personen2;
  }

  @JsonProperty("personen_2")
  public void setPersonen2(Integer personen2) {
    this.personen2 = personen2;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto personen3(Integer personen3) {
    this.personen3 = personen3;
    return this;
  }

  
  @JsonProperty("personen_3")
  @NotNull
  public Integer getPersonen3() {
    return personen3;
  }

  @JsonProperty("personen_3")
  public void setPersonen3(Integer personen3) {
    this.personen3 = personen3;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto personen4(Integer personen4) {
    this.personen4 = personen4;
    return this;
  }

  
  @JsonProperty("personen_4")
  @NotNull
  public Integer getPersonen4() {
    return personen4;
  }

  @JsonProperty("personen_4")
  public void setPersonen4(Integer personen4) {
    this.personen4 = personen4;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto personen5(Integer personen5) {
    this.personen5 = personen5;
    return this;
  }

  
  @JsonProperty("personen_5")
  @NotNull
  public Integer getPersonen5() {
    return personen5;
  }

  @JsonProperty("personen_5")
  public void setPersonen5(Integer personen5) {
    this.personen5 = personen5;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto personen6(Integer personen6) {
    this.personen6 = personen6;
    return this;
  }

  
  @JsonProperty("personen_6")
  @NotNull
  public Integer getPersonen6() {
    return personen6;
  }

  @JsonProperty("personen_6")
  public void setPersonen6(Integer personen6) {
    this.personen6 = personen6;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto personen7(Integer personen7) {
    this.personen7 = personen7;
    return this;
  }

  
  @JsonProperty("personen_7")
  @NotNull
  public Integer getPersonen7() {
    return personen7;
  }

  @JsonProperty("personen_7")
  public void setPersonen7(Integer personen7) {
    this.personen7 = personen7;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto proWeiterePerson(Integer proWeiterePerson) {
    this.proWeiterePerson = proWeiterePerson;
    return this;
  }

  
  @JsonProperty("proWeiterePerson")
  @NotNull
  public Integer getProWeiterePerson() {
    return proWeiterePerson;
  }

  @JsonProperty("proWeiterePerson")
  public void setProWeiterePerson(Integer proWeiterePerson) {
    this.proWeiterePerson = proWeiterePerson;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto kinder0018(Integer kinder0018) {
    this.kinder0018 = kinder0018;
    return this;
  }

  
  @JsonProperty("kinder_00_18")
  @NotNull
  public Integer getKinder0018() {
    return kinder0018;
  }

  @JsonProperty("kinder_00_18")
  public void setKinder0018(Integer kinder0018) {
    this.kinder0018 = kinder0018;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto jugendlicheErwachsene1925(Integer jugendlicheErwachsene1925) {
    this.jugendlicheErwachsene1925 = jugendlicheErwachsene1925;
    return this;
  }

  
  @JsonProperty("jugendliche_erwachsene_19_25")
  @NotNull
  public Integer getJugendlicheErwachsene1925() {
    return jugendlicheErwachsene1925;
  }

  @JsonProperty("jugendliche_erwachsene_19_25")
  public void setJugendlicheErwachsene1925(Integer jugendlicheErwachsene1925) {
    this.jugendlicheErwachsene1925 = jugendlicheErwachsene1925;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto erwachsene2699(Integer erwachsene2699) {
    this.erwachsene2699 = erwachsene2699;
    return this;
  }

  
  @JsonProperty("erwachsene_26_99")
  @NotNull
  public Integer getErwachsene2699() {
    return erwachsene2699;
  }

  @JsonProperty("erwachsene_26_99")
  public void setErwachsene2699(Integer erwachsene2699) {
    this.erwachsene2699 = erwachsene2699;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenFam1pers(Integer wohnkostenFam1pers) {
    this.wohnkostenFam1pers = wohnkostenFam1pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_1pers")
  @NotNull
  public Integer getWohnkostenFam1pers() {
    return wohnkostenFam1pers;
  }

  @JsonProperty("wohnkosten_fam_1pers")
  public void setWohnkostenFam1pers(Integer wohnkostenFam1pers) {
    this.wohnkostenFam1pers = wohnkostenFam1pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenFam2pers(Integer wohnkostenFam2pers) {
    this.wohnkostenFam2pers = wohnkostenFam2pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_2pers")
  @NotNull
  public Integer getWohnkostenFam2pers() {
    return wohnkostenFam2pers;
  }

  @JsonProperty("wohnkosten_fam_2pers")
  public void setWohnkostenFam2pers(Integer wohnkostenFam2pers) {
    this.wohnkostenFam2pers = wohnkostenFam2pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenFam3pers(Integer wohnkostenFam3pers) {
    this.wohnkostenFam3pers = wohnkostenFam3pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_3pers")
  @NotNull
  public Integer getWohnkostenFam3pers() {
    return wohnkostenFam3pers;
  }

  @JsonProperty("wohnkosten_fam_3pers")
  public void setWohnkostenFam3pers(Integer wohnkostenFam3pers) {
    this.wohnkostenFam3pers = wohnkostenFam3pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenFam4pers(Integer wohnkostenFam4pers) {
    this.wohnkostenFam4pers = wohnkostenFam4pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_4pers")
  @NotNull
  public Integer getWohnkostenFam4pers() {
    return wohnkostenFam4pers;
  }

  @JsonProperty("wohnkosten_fam_4pers")
  public void setWohnkostenFam4pers(Integer wohnkostenFam4pers) {
    this.wohnkostenFam4pers = wohnkostenFam4pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenFam5pluspers(Integer wohnkostenFam5pluspers) {
    this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
    return this;
  }

  
  @JsonProperty("wohnkosten_fam_5pluspers")
  @NotNull
  public Integer getWohnkostenFam5pluspers() {
    return wohnkostenFam5pluspers;
  }

  @JsonProperty("wohnkosten_fam_5pluspers")
  public void setWohnkostenFam5pluspers(Integer wohnkostenFam5pluspers) {
    this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenPersoenlich1pers(Integer wohnkostenPersoenlich1pers) {
    this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_1pers")
  @NotNull
  public Integer getWohnkostenPersoenlich1pers() {
    return wohnkostenPersoenlich1pers;
  }

  @JsonProperty("wohnkosten_persoenlich_1pers")
  public void setWohnkostenPersoenlich1pers(Integer wohnkostenPersoenlich1pers) {
    this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenPersoenlich2pers(Integer wohnkostenPersoenlich2pers) {
    this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_2pers")
  @NotNull
  public Integer getWohnkostenPersoenlich2pers() {
    return wohnkostenPersoenlich2pers;
  }

  @JsonProperty("wohnkosten_persoenlich_2pers")
  public void setWohnkostenPersoenlich2pers(Integer wohnkostenPersoenlich2pers) {
    this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenPersoenlich3pers(Integer wohnkostenPersoenlich3pers) {
    this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_3pers")
  @NotNull
  public Integer getWohnkostenPersoenlich3pers() {
    return wohnkostenPersoenlich3pers;
  }

  @JsonProperty("wohnkosten_persoenlich_3pers")
  public void setWohnkostenPersoenlich3pers(Integer wohnkostenPersoenlich3pers) {
    this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenPersoenlich4pers(Integer wohnkostenPersoenlich4pers) {
    this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_4pers")
  @NotNull
  public Integer getWohnkostenPersoenlich4pers() {
    return wohnkostenPersoenlich4pers;
  }

  @JsonProperty("wohnkosten_persoenlich_4pers")
  public void setWohnkostenPersoenlich4pers(Integer wohnkostenPersoenlich4pers) {
    this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto wohnkostenPersoenlich5pluspers(Integer wohnkostenPersoenlich5pluspers) {
    this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
    return this;
  }

  
  @JsonProperty("wohnkosten_persoenlich_5pluspers")
  @NotNull
  public Integer getWohnkostenPersoenlich5pluspers() {
    return wohnkostenPersoenlich5pluspers;
  }

  @JsonProperty("wohnkosten_persoenlich_5pluspers")
  public void setWohnkostenPersoenlich5pluspers(Integer wohnkostenPersoenlich5pluspers) {
    this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto preisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
    return this;
  }

  
  @JsonProperty("preisProMahlzeit")
  @NotNull
  public Integer getPreisProMahlzeit() {
    return preisProMahlzeit;
  }

  @JsonProperty("preisProMahlzeit")
  public void setPreisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto maxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
    return this;
  }

  
  @JsonProperty("maxSaeule3a")
  @NotNull
  public Integer getMaxSaeule3a() {
    return maxSaeule3a;
  }

  @JsonProperty("maxSaeule3a")
  public void setMaxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto anzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
    return this;
  }

  
  @JsonProperty("anzahlWochenLehre")
  @NotNull
  public Integer getAnzahlWochenLehre() {
    return anzahlWochenLehre;
  }

  @JsonProperty("anzahlWochenLehre")
  public void setAnzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto anzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
    return this;
  }

  
  @JsonProperty("anzahlWochenSchule")
  @NotNull
  public Integer getAnzahlWochenSchule() {
    return anzahlWochenSchule;
  }

  @JsonProperty("anzahlWochenSchule")
  public void setAnzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
  }

  /**
   **/
  public GesuchsperiodeWithDatenDto einreichfrist(LocalDate einreichfrist) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeWithDatenDto gesuchsperiodeWithDaten = (GesuchsperiodeWithDatenDto) o;
    return Objects.equals(this.id, gesuchsperiodeWithDaten.id) &&
        Objects.equals(this.gueltigkeitStatus, gesuchsperiodeWithDaten.gueltigkeitStatus) &&
        Objects.equals(this.bezeichnungDe, gesuchsperiodeWithDaten.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiodeWithDaten.bezeichnungFr) &&
        Objects.equals(this.fiskaljahr, gesuchsperiodeWithDaten.fiskaljahr) &&
        Objects.equals(this.gesuchsjahrId, gesuchsperiodeWithDaten.gesuchsjahrId) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiodeWithDaten.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiodeWithDaten.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiodeWithDaten.aufschaltterminStart) &&
        Objects.equals(this.aufschaltterminStopp, gesuchsperiodeWithDaten.aufschaltterminStopp) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiodeWithDaten.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiodeWithDaten.einreichefristReduziert) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiodeWithDaten.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiodeWithDaten.ausbKostenTertiaer) &&
        Objects.equals(this.freibetragVermoegen, gesuchsperiodeWithDaten.freibetragVermoegen) &&
        Objects.equals(this.freibetragErwerbseinkommen, gesuchsperiodeWithDaten.freibetragErwerbseinkommen) &&
        Objects.equals(this.einkommensfreibetrag, gesuchsperiodeWithDaten.einkommensfreibetrag) &&
        Objects.equals(this.elternbeteiligungssatz, gesuchsperiodeWithDaten.elternbeteiligungssatz) &&
        Objects.equals(this.vermogenSatzAngerechnet, gesuchsperiodeWithDaten.vermogenSatzAngerechnet) &&
        Objects.equals(this.integrationszulage, gesuchsperiodeWithDaten.integrationszulage) &&
        Objects.equals(this.limiteEkFreibetragIntegrationszulag, gesuchsperiodeWithDaten.limiteEkFreibetragIntegrationszulag) &&
        Objects.equals(this.stipLimiteMinimalstipendium, gesuchsperiodeWithDaten.stipLimiteMinimalstipendium) &&
        Objects.equals(this.person1, gesuchsperiodeWithDaten.person1) &&
        Objects.equals(this.personen2, gesuchsperiodeWithDaten.personen2) &&
        Objects.equals(this.personen3, gesuchsperiodeWithDaten.personen3) &&
        Objects.equals(this.personen4, gesuchsperiodeWithDaten.personen4) &&
        Objects.equals(this.personen5, gesuchsperiodeWithDaten.personen5) &&
        Objects.equals(this.personen6, gesuchsperiodeWithDaten.personen6) &&
        Objects.equals(this.personen7, gesuchsperiodeWithDaten.personen7) &&
        Objects.equals(this.proWeiterePerson, gesuchsperiodeWithDaten.proWeiterePerson) &&
        Objects.equals(this.kinder0018, gesuchsperiodeWithDaten.kinder0018) &&
        Objects.equals(this.jugendlicheErwachsene1925, gesuchsperiodeWithDaten.jugendlicheErwachsene1925) &&
        Objects.equals(this.erwachsene2699, gesuchsperiodeWithDaten.erwachsene2699) &&
        Objects.equals(this.wohnkostenFam1pers, gesuchsperiodeWithDaten.wohnkostenFam1pers) &&
        Objects.equals(this.wohnkostenFam2pers, gesuchsperiodeWithDaten.wohnkostenFam2pers) &&
        Objects.equals(this.wohnkostenFam3pers, gesuchsperiodeWithDaten.wohnkostenFam3pers) &&
        Objects.equals(this.wohnkostenFam4pers, gesuchsperiodeWithDaten.wohnkostenFam4pers) &&
        Objects.equals(this.wohnkostenFam5pluspers, gesuchsperiodeWithDaten.wohnkostenFam5pluspers) &&
        Objects.equals(this.wohnkostenPersoenlich1pers, gesuchsperiodeWithDaten.wohnkostenPersoenlich1pers) &&
        Objects.equals(this.wohnkostenPersoenlich2pers, gesuchsperiodeWithDaten.wohnkostenPersoenlich2pers) &&
        Objects.equals(this.wohnkostenPersoenlich3pers, gesuchsperiodeWithDaten.wohnkostenPersoenlich3pers) &&
        Objects.equals(this.wohnkostenPersoenlich4pers, gesuchsperiodeWithDaten.wohnkostenPersoenlich4pers) &&
        Objects.equals(this.wohnkostenPersoenlich5pluspers, gesuchsperiodeWithDaten.wohnkostenPersoenlich5pluspers) &&
        Objects.equals(this.preisProMahlzeit, gesuchsperiodeWithDaten.preisProMahlzeit) &&
        Objects.equals(this.maxSaeule3a, gesuchsperiodeWithDaten.maxSaeule3a) &&
        Objects.equals(this.anzahlWochenLehre, gesuchsperiodeWithDaten.anzahlWochenLehre) &&
        Objects.equals(this.anzahlWochenSchule, gesuchsperiodeWithDaten.anzahlWochenSchule) &&
        Objects.equals(this.einreichfrist, gesuchsperiodeWithDaten.einreichfrist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gueltigkeitStatus, bezeichnungDe, bezeichnungFr, fiskaljahr, gesuchsjahrId, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, aufschaltterminStopp, einreichefristNormal, einreichefristReduziert, ausbKostenSekII, ausbKostenTertiaer, freibetragVermoegen, freibetragErwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz, vermogenSatzAngerechnet, integrationszulage, limiteEkFreibetragIntegrationszulag, stipLimiteMinimalstipendium, person1, personen2, personen3, personen4, personen5, personen6, personen7, proWeiterePerson, kinder0018, jugendlicheErwachsene1925, erwachsene2699, wohnkostenFam1pers, wohnkostenFam2pers, wohnkostenFam3pers, wohnkostenFam4pers, wohnkostenFam5pluspers, wohnkostenPersoenlich1pers, wohnkostenPersoenlich2pers, wohnkostenPersoenlich3pers, wohnkostenPersoenlich4pers, wohnkostenPersoenlich5pluspers, preisProMahlzeit, maxSaeule3a, anzahlWochenLehre, anzahlWochenSchule, einreichfrist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeWithDatenDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gueltigkeitStatus: ").append(toIndentedString(gueltigkeitStatus)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    fiskaljahr: ").append(toIndentedString(fiskaljahr)).append("\n");
    sb.append("    gesuchsjahrId: ").append(toIndentedString(gesuchsjahrId)).append("\n");
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
    sb.append("    vermogenSatzAngerechnet: ").append(toIndentedString(vermogenSatzAngerechnet)).append("\n");
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
    sb.append("    preisProMahlzeit: ").append(toIndentedString(preisProMahlzeit)).append("\n");
    sb.append("    maxSaeule3a: ").append(toIndentedString(maxSaeule3a)).append("\n");
    sb.append("    anzahlWochenLehre: ").append(toIndentedString(anzahlWochenLehre)).append("\n");
    sb.append("    anzahlWochenSchule: ").append(toIndentedString(anzahlWochenSchule)).append("\n");
    sb.append("    einreichfrist: ").append(toIndentedString(einreichfrist)).append("\n");
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

