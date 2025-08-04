package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("GesuchsperiodeCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeCreateDto  implements Serializable {
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid Integer fiskaljahr;
  private @Valid UUID gesuchsjahrId;
  private @Valid LocalDate gesuchsperiodeStart;
  private @Valid LocalDate gesuchsperiodeStopp;
  private @Valid LocalDate aufschaltterminStart;
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
  private @Valid Integer limiteEkFreibetragIntegrationszulage;
  private @Valid Integer stipLimiteMinimalstipendium;
  private @Valid Integer person1;
  private @Valid Integer personen2;
  private @Valid Integer personen3;
  private @Valid Integer personen4;
  private @Valid Integer personen5;
  private @Valid Integer personen6;
  private @Valid Integer personen7;
  private @Valid Integer proWeiterePerson;
  private @Valid Integer kinder0017;
  private @Valid Integer jugendlicheErwachsene1824;
  private @Valid Integer erwachsene2599;
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
  private @Valid Integer vermoegensanteilInProzent;
  private @Valid Integer reduzierungDesGrundbedarfs;
  private @Valid Integer limiteAlterAntragsstellerHalbierungElternbeitrag;
  private @Valid Integer zweiterAuszahlungsterminMonat;
  private @Valid Integer zweiterAuszahlungsterminTag;
  private @Valid Integer fristNachreichenDokumente;
  private @Valid LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung;
  private @Valid LocalDate einreichfrist;

  /**
   **/
  public GesuchsperiodeCreateDto bezeichnungDe(String bezeichnungDe) {
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
  public GesuchsperiodeCreateDto bezeichnungFr(String bezeichnungFr) {
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
  public GesuchsperiodeCreateDto fiskaljahr(Integer fiskaljahr) {
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
  public GesuchsperiodeCreateDto gesuchsjahrId(UUID gesuchsjahrId) {
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
   * dd.MM.yyyy
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
   * dd.MM.yyyy
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
   * dd.MM.yyyy
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
   * dd.MM.yyyy
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
   * dd.MM.yyyy
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
   **/
  public GesuchsperiodeCreateDto ausbKostenSekII(Integer ausbKostenSekII) {
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
  public GesuchsperiodeCreateDto ausbKostenTertiaer(Integer ausbKostenTertiaer) {
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
  public GesuchsperiodeCreateDto freibetragVermoegen(Integer freibetragVermoegen) {
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
  public GesuchsperiodeCreateDto freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
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
  public GesuchsperiodeCreateDto einkommensfreibetrag(Integer einkommensfreibetrag) {
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
  public GesuchsperiodeCreateDto elternbeteiligungssatz(Integer elternbeteiligungssatz) {
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
  public GesuchsperiodeCreateDto vermogenSatzAngerechnet(Integer vermogenSatzAngerechnet) {
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
  public GesuchsperiodeCreateDto integrationszulage(Integer integrationszulage) {
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
  public GesuchsperiodeCreateDto limiteEkFreibetragIntegrationszulage(Integer limiteEkFreibetragIntegrationszulage) {
    this.limiteEkFreibetragIntegrationszulage = limiteEkFreibetragIntegrationszulage;
    return this;
  }

  
  @JsonProperty("limite_EkFreibetrag_Integrationszulage")
  @NotNull
  public Integer getLimiteEkFreibetragIntegrationszulage() {
    return limiteEkFreibetragIntegrationszulage;
  }

  @JsonProperty("limite_EkFreibetrag_Integrationszulage")
  public void setLimiteEkFreibetragIntegrationszulage(Integer limiteEkFreibetragIntegrationszulage) {
    this.limiteEkFreibetragIntegrationszulage = limiteEkFreibetragIntegrationszulage;
  }

  /**
   **/
  public GesuchsperiodeCreateDto stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
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
  public GesuchsperiodeCreateDto person1(Integer person1) {
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
  public GesuchsperiodeCreateDto personen2(Integer personen2) {
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
  public GesuchsperiodeCreateDto personen3(Integer personen3) {
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
  public GesuchsperiodeCreateDto personen4(Integer personen4) {
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
  public GesuchsperiodeCreateDto personen5(Integer personen5) {
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
  public GesuchsperiodeCreateDto personen6(Integer personen6) {
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
  public GesuchsperiodeCreateDto personen7(Integer personen7) {
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
  public GesuchsperiodeCreateDto proWeiterePerson(Integer proWeiterePerson) {
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
  public GesuchsperiodeCreateDto kinder0017(Integer kinder0017) {
    this.kinder0017 = kinder0017;
    return this;
  }

  
  @JsonProperty("kinder_00_17")
  @NotNull
  public Integer getKinder0017() {
    return kinder0017;
  }

  @JsonProperty("kinder_00_17")
  public void setKinder0017(Integer kinder0017) {
    this.kinder0017 = kinder0017;
  }

  /**
   **/
  public GesuchsperiodeCreateDto jugendlicheErwachsene1824(Integer jugendlicheErwachsene1824) {
    this.jugendlicheErwachsene1824 = jugendlicheErwachsene1824;
    return this;
  }

  
  @JsonProperty("jugendliche_erwachsene_18_24")
  @NotNull
  public Integer getJugendlicheErwachsene1824() {
    return jugendlicheErwachsene1824;
  }

  @JsonProperty("jugendliche_erwachsene_18_24")
  public void setJugendlicheErwachsene1824(Integer jugendlicheErwachsene1824) {
    this.jugendlicheErwachsene1824 = jugendlicheErwachsene1824;
  }

  /**
   **/
  public GesuchsperiodeCreateDto erwachsene2599(Integer erwachsene2599) {
    this.erwachsene2599 = erwachsene2599;
    return this;
  }

  
  @JsonProperty("erwachsene_25_99")
  @NotNull
  public Integer getErwachsene2599() {
    return erwachsene2599;
  }

  @JsonProperty("erwachsene_25_99")
  public void setErwachsene2599(Integer erwachsene2599) {
    this.erwachsene2599 = erwachsene2599;
  }

  /**
   **/
  public GesuchsperiodeCreateDto wohnkostenFam1pers(Integer wohnkostenFam1pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam2pers(Integer wohnkostenFam2pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam3pers(Integer wohnkostenFam3pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam4pers(Integer wohnkostenFam4pers) {
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
  public GesuchsperiodeCreateDto wohnkostenFam5pluspers(Integer wohnkostenFam5pluspers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich1pers(Integer wohnkostenPersoenlich1pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich2pers(Integer wohnkostenPersoenlich2pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich3pers(Integer wohnkostenPersoenlich3pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich4pers(Integer wohnkostenPersoenlich4pers) {
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
  public GesuchsperiodeCreateDto wohnkostenPersoenlich5pluspers(Integer wohnkostenPersoenlich5pluspers) {
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
  public GesuchsperiodeCreateDto preisProMahlzeit(Integer preisProMahlzeit) {
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
  public GesuchsperiodeCreateDto maxSaeule3a(Integer maxSaeule3a) {
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
  public GesuchsperiodeCreateDto anzahlWochenLehre(Integer anzahlWochenLehre) {
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
  public GesuchsperiodeCreateDto anzahlWochenSchule(Integer anzahlWochenSchule) {
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
  public GesuchsperiodeCreateDto vermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
    return this;
  }

  
  @JsonProperty("vermoegensanteilInProzent")
  @NotNull
  public Integer getVermoegensanteilInProzent() {
    return vermoegensanteilInProzent;
  }

  @JsonProperty("vermoegensanteilInProzent")
  public void setVermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
  }

  /**
   **/
  public GesuchsperiodeCreateDto reduzierungDesGrundbedarfs(Integer reduzierungDesGrundbedarfs) {
    this.reduzierungDesGrundbedarfs = reduzierungDesGrundbedarfs;
    return this;
  }

  
  @JsonProperty("reduzierungDesGrundbedarfs")
  @NotNull
  public Integer getReduzierungDesGrundbedarfs() {
    return reduzierungDesGrundbedarfs;
  }

  @JsonProperty("reduzierungDesGrundbedarfs")
  public void setReduzierungDesGrundbedarfs(Integer reduzierungDesGrundbedarfs) {
    this.reduzierungDesGrundbedarfs = reduzierungDesGrundbedarfs;
  }

  /**
   **/
  public GesuchsperiodeCreateDto limiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
    return this;
  }

  
  @JsonProperty("limiteAlterAntragsstellerHalbierungElternbeitrag")
  @NotNull
  public Integer getLimiteAlterAntragsstellerHalbierungElternbeitrag() {
    return limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  @JsonProperty("limiteAlterAntragsstellerHalbierungElternbeitrag")
  public void setLimiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  /**
   **/
  public GesuchsperiodeCreateDto zweiterAuszahlungsterminMonat(Integer zweiterAuszahlungsterminMonat) {
    this.zweiterAuszahlungsterminMonat = zweiterAuszahlungsterminMonat;
    return this;
  }

  
  @JsonProperty("zweiterAuszahlungsterminMonat")
  @NotNull
  public Integer getZweiterAuszahlungsterminMonat() {
    return zweiterAuszahlungsterminMonat;
  }

  @JsonProperty("zweiterAuszahlungsterminMonat")
  public void setZweiterAuszahlungsterminMonat(Integer zweiterAuszahlungsterminMonat) {
    this.zweiterAuszahlungsterminMonat = zweiterAuszahlungsterminMonat;
  }

  /**
   **/
  public GesuchsperiodeCreateDto zweiterAuszahlungsterminTag(Integer zweiterAuszahlungsterminTag) {
    this.zweiterAuszahlungsterminTag = zweiterAuszahlungsterminTag;
    return this;
  }

  
  @JsonProperty("zweiterAuszahlungsterminTag")
  @NotNull
  public Integer getZweiterAuszahlungsterminTag() {
    return zweiterAuszahlungsterminTag;
  }

  @JsonProperty("zweiterAuszahlungsterminTag")
  public void setZweiterAuszahlungsterminTag(Integer zweiterAuszahlungsterminTag) {
    this.zweiterAuszahlungsterminTag = zweiterAuszahlungsterminTag;
  }

  /**
   **/
  public GesuchsperiodeCreateDto fristNachreichenDokumente(Integer fristNachreichenDokumente) {
    this.fristNachreichenDokumente = fristNachreichenDokumente;
    return this;
  }

  
  @JsonProperty("fristNachreichenDokumente")
  @NotNull
  public Integer getFristNachreichenDokumente() {
    return fristNachreichenDokumente;
  }

  @JsonProperty("fristNachreichenDokumente")
  public void setFristNachreichenDokumente(Integer fristNachreichenDokumente) {
    this.fristNachreichenDokumente = fristNachreichenDokumente;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeCreateDto stichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung) {
    this.stichtagVolljaehrigkeitMedizinischeGrundversorgung = stichtagVolljaehrigkeitMedizinischeGrundversorgung;
    return this;
  }

  
  @JsonProperty("stichtagVolljaehrigkeitMedizinischeGrundversorgung")
  @NotNull
  public LocalDate getStichtagVolljaehrigkeitMedizinischeGrundversorgung() {
    return stichtagVolljaehrigkeitMedizinischeGrundversorgung;
  }

  @JsonProperty("stichtagVolljaehrigkeitMedizinischeGrundversorgung")
  public void setStichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung) {
    this.stichtagVolljaehrigkeitMedizinischeGrundversorgung = stichtagVolljaehrigkeitMedizinischeGrundversorgung;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeCreateDto gesuchsperiodeCreate = (GesuchsperiodeCreateDto) o;
    return Objects.equals(this.bezeichnungDe, gesuchsperiodeCreate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiodeCreate.bezeichnungFr) &&
        Objects.equals(this.fiskaljahr, gesuchsperiodeCreate.fiskaljahr) &&
        Objects.equals(this.gesuchsjahrId, gesuchsperiodeCreate.gesuchsjahrId) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiodeCreate.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiodeCreate.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiodeCreate.aufschaltterminStart) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiodeCreate.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiodeCreate.einreichefristReduziert) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiodeCreate.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiodeCreate.ausbKostenTertiaer) &&
        Objects.equals(this.freibetragVermoegen, gesuchsperiodeCreate.freibetragVermoegen) &&
        Objects.equals(this.freibetragErwerbseinkommen, gesuchsperiodeCreate.freibetragErwerbseinkommen) &&
        Objects.equals(this.einkommensfreibetrag, gesuchsperiodeCreate.einkommensfreibetrag) &&
        Objects.equals(this.elternbeteiligungssatz, gesuchsperiodeCreate.elternbeteiligungssatz) &&
        Objects.equals(this.vermogenSatzAngerechnet, gesuchsperiodeCreate.vermogenSatzAngerechnet) &&
        Objects.equals(this.integrationszulage, gesuchsperiodeCreate.integrationszulage) &&
        Objects.equals(this.limiteEkFreibetragIntegrationszulage, gesuchsperiodeCreate.limiteEkFreibetragIntegrationszulage) &&
        Objects.equals(this.stipLimiteMinimalstipendium, gesuchsperiodeCreate.stipLimiteMinimalstipendium) &&
        Objects.equals(this.person1, gesuchsperiodeCreate.person1) &&
        Objects.equals(this.personen2, gesuchsperiodeCreate.personen2) &&
        Objects.equals(this.personen3, gesuchsperiodeCreate.personen3) &&
        Objects.equals(this.personen4, gesuchsperiodeCreate.personen4) &&
        Objects.equals(this.personen5, gesuchsperiodeCreate.personen5) &&
        Objects.equals(this.personen6, gesuchsperiodeCreate.personen6) &&
        Objects.equals(this.personen7, gesuchsperiodeCreate.personen7) &&
        Objects.equals(this.proWeiterePerson, gesuchsperiodeCreate.proWeiterePerson) &&
        Objects.equals(this.kinder0017, gesuchsperiodeCreate.kinder0017) &&
        Objects.equals(this.jugendlicheErwachsene1824, gesuchsperiodeCreate.jugendlicheErwachsene1824) &&
        Objects.equals(this.erwachsene2599, gesuchsperiodeCreate.erwachsene2599) &&
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
        Objects.equals(this.preisProMahlzeit, gesuchsperiodeCreate.preisProMahlzeit) &&
        Objects.equals(this.maxSaeule3a, gesuchsperiodeCreate.maxSaeule3a) &&
        Objects.equals(this.anzahlWochenLehre, gesuchsperiodeCreate.anzahlWochenLehre) &&
        Objects.equals(this.anzahlWochenSchule, gesuchsperiodeCreate.anzahlWochenSchule) &&
        Objects.equals(this.vermoegensanteilInProzent, gesuchsperiodeCreate.vermoegensanteilInProzent) &&
        Objects.equals(this.reduzierungDesGrundbedarfs, gesuchsperiodeCreate.reduzierungDesGrundbedarfs) &&
        Objects.equals(this.limiteAlterAntragsstellerHalbierungElternbeitrag, gesuchsperiodeCreate.limiteAlterAntragsstellerHalbierungElternbeitrag) &&
        Objects.equals(this.zweiterAuszahlungsterminMonat, gesuchsperiodeCreate.zweiterAuszahlungsterminMonat) &&
        Objects.equals(this.zweiterAuszahlungsterminTag, gesuchsperiodeCreate.zweiterAuszahlungsterminTag) &&
        Objects.equals(this.fristNachreichenDokumente, gesuchsperiodeCreate.fristNachreichenDokumente) &&
        Objects.equals(this.stichtagVolljaehrigkeitMedizinischeGrundversorgung, gesuchsperiodeCreate.stichtagVolljaehrigkeitMedizinischeGrundversorgung) &&
        Objects.equals(this.einreichfrist, gesuchsperiodeCreate.einreichfrist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, fiskaljahr, gesuchsjahrId, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, einreichefristNormal, einreichefristReduziert, ausbKostenSekII, ausbKostenTertiaer, freibetragVermoegen, freibetragErwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz, vermogenSatzAngerechnet, integrationszulage, limiteEkFreibetragIntegrationszulage, stipLimiteMinimalstipendium, person1, personen2, personen3, personen4, personen5, personen6, personen7, proWeiterePerson, kinder0017, jugendlicheErwachsene1824, erwachsene2599, wohnkostenFam1pers, wohnkostenFam2pers, wohnkostenFam3pers, wohnkostenFam4pers, wohnkostenFam5pluspers, wohnkostenPersoenlich1pers, wohnkostenPersoenlich2pers, wohnkostenPersoenlich3pers, wohnkostenPersoenlich4pers, wohnkostenPersoenlich5pluspers, preisProMahlzeit, maxSaeule3a, anzahlWochenLehre, anzahlWochenSchule, vermoegensanteilInProzent, reduzierungDesGrundbedarfs, limiteAlterAntragsstellerHalbierungElternbeitrag, zweiterAuszahlungsterminMonat, zweiterAuszahlungsterminTag, fristNachreichenDokumente, stichtagVolljaehrigkeitMedizinischeGrundversorgung, einreichfrist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeCreateDto {\n");
    
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    fiskaljahr: ").append(toIndentedString(fiskaljahr)).append("\n");
    sb.append("    gesuchsjahrId: ").append(toIndentedString(gesuchsjahrId)).append("\n");
    sb.append("    gesuchsperiodeStart: ").append(toIndentedString(gesuchsperiodeStart)).append("\n");
    sb.append("    gesuchsperiodeStopp: ").append(toIndentedString(gesuchsperiodeStopp)).append("\n");
    sb.append("    aufschaltterminStart: ").append(toIndentedString(aufschaltterminStart)).append("\n");
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
    sb.append("    limiteEkFreibetragIntegrationszulage: ").append(toIndentedString(limiteEkFreibetragIntegrationszulage)).append("\n");
    sb.append("    stipLimiteMinimalstipendium: ").append(toIndentedString(stipLimiteMinimalstipendium)).append("\n");
    sb.append("    person1: ").append(toIndentedString(person1)).append("\n");
    sb.append("    personen2: ").append(toIndentedString(personen2)).append("\n");
    sb.append("    personen3: ").append(toIndentedString(personen3)).append("\n");
    sb.append("    personen4: ").append(toIndentedString(personen4)).append("\n");
    sb.append("    personen5: ").append(toIndentedString(personen5)).append("\n");
    sb.append("    personen6: ").append(toIndentedString(personen6)).append("\n");
    sb.append("    personen7: ").append(toIndentedString(personen7)).append("\n");
    sb.append("    proWeiterePerson: ").append(toIndentedString(proWeiterePerson)).append("\n");
    sb.append("    kinder0017: ").append(toIndentedString(kinder0017)).append("\n");
    sb.append("    jugendlicheErwachsene1824: ").append(toIndentedString(jugendlicheErwachsene1824)).append("\n");
    sb.append("    erwachsene2599: ").append(toIndentedString(erwachsene2599)).append("\n");
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
    sb.append("    vermoegensanteilInProzent: ").append(toIndentedString(vermoegensanteilInProzent)).append("\n");
    sb.append("    reduzierungDesGrundbedarfs: ").append(toIndentedString(reduzierungDesGrundbedarfs)).append("\n");
    sb.append("    limiteAlterAntragsstellerHalbierungElternbeitrag: ").append(toIndentedString(limiteAlterAntragsstellerHalbierungElternbeitrag)).append("\n");
    sb.append("    zweiterAuszahlungsterminMonat: ").append(toIndentedString(zweiterAuszahlungsterminMonat)).append("\n");
    sb.append("    zweiterAuszahlungsterminTag: ").append(toIndentedString(zweiterAuszahlungsterminTag)).append("\n");
    sb.append("    fristNachreichenDokumente: ").append(toIndentedString(fristNachreichenDokumente)).append("\n");
    sb.append("    stichtagVolljaehrigkeitMedizinischeGrundversorgung: ").append(toIndentedString(stichtagVolljaehrigkeitMedizinischeGrundversorgung)).append("\n");
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

