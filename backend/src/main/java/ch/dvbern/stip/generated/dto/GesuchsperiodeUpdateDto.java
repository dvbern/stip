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



@JsonTypeName("GesuchsperiodeUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchsperiodeUpdateDto  implements Serializable {
  private String bezeichnungDe;
  private String bezeichnungFr;
  private Integer fiskaljahr;
  private UUID gesuchsjahrId;
  private LocalDate gesuchsperiodeStart;
  private LocalDate gesuchsperiodeStopp;
  private LocalDate aufschaltterminStart;
  private LocalDate einreichefristNormal;
  private LocalDate einreichefristReduziert;
  private Integer ausbKostenSekII;
  private Integer ausbKostenTertiaer;
  private Integer freibetragVermoegen;
  private Integer freibetragErwerbseinkommen;
  private Integer einkommensfreibetrag;
  private Integer elternbeteiligungssatz;
  private Integer vermogenSatzAngerechnet;
  private Integer integrationszulage;
  private Integer limiteEkFreibetragIntegrationszulage;
  private Integer stipLimiteMinimalstipendium;
  private Integer person1;
  private Integer personen2;
  private Integer personen3;
  private Integer personen4;
  private Integer personen5;
  private Integer personen6;
  private Integer personen7;
  private Integer proWeiterePerson;
  private Integer kinder0017;
  private Integer jugendlicheErwachsene1824;
  private Integer erwachsene2599;
  private Integer wohnkostenFam1pers;
  private Integer wohnkostenFam2pers;
  private Integer wohnkostenFam3pers;
  private Integer wohnkostenFam4pers;
  private Integer wohnkostenFam5pluspers;
  private Integer wohnkostenPersoenlich1pers;
  private Integer wohnkostenPersoenlich2pers;
  private Integer wohnkostenPersoenlich3pers;
  private Integer wohnkostenPersoenlich4pers;
  private Integer wohnkostenPersoenlich5pluspers;
  private Integer preisProMahlzeit;
  private Integer maxSaeule3a;
  private Integer anzahlWochenLehre;
  private Integer anzahlWochenSchule;
  private Integer vermoegensanteilInProzent;
  private Integer reduzierungDesGrundbedarfs;
  private Integer limiteAlterAntragsstellerHalbierungElternbeitrag;
  private Integer zweiterAuszahlungsterminMonat;
  private Integer zweiterAuszahlungsterminTag;
  private Integer fristNachreichenDokumente;
  private Integer fristUploadUnterschriftenblatt;
  private LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung;
  private LocalDate einreichfrist;

  protected GesuchsperiodeUpdateDto(GesuchsperiodeUpdateDtoBuilder<?, ?> b) {
    this.bezeichnungDe = b.bezeichnungDe;
    this.bezeichnungFr = b.bezeichnungFr;
    this.fiskaljahr = b.fiskaljahr;
    this.gesuchsjahrId = b.gesuchsjahrId;
    this.gesuchsperiodeStart = b.gesuchsperiodeStart;
    this.gesuchsperiodeStopp = b.gesuchsperiodeStopp;
    this.aufschaltterminStart = b.aufschaltterminStart;
    this.einreichefristNormal = b.einreichefristNormal;
    this.einreichefristReduziert = b.einreichefristReduziert;
    this.ausbKostenSekII = b.ausbKostenSekII;
    this.ausbKostenTertiaer = b.ausbKostenTertiaer;
    this.freibetragVermoegen = b.freibetragVermoegen;
    this.freibetragErwerbseinkommen = b.freibetragErwerbseinkommen;
    this.einkommensfreibetrag = b.einkommensfreibetrag;
    this.elternbeteiligungssatz = b.elternbeteiligungssatz;
    this.vermogenSatzAngerechnet = b.vermogenSatzAngerechnet;
    this.integrationszulage = b.integrationszulage;
    this.limiteEkFreibetragIntegrationszulage = b.limiteEkFreibetragIntegrationszulage;
    this.stipLimiteMinimalstipendium = b.stipLimiteMinimalstipendium;
    this.person1 = b.person1;
    this.personen2 = b.personen2;
    this.personen3 = b.personen3;
    this.personen4 = b.personen4;
    this.personen5 = b.personen5;
    this.personen6 = b.personen6;
    this.personen7 = b.personen7;
    this.proWeiterePerson = b.proWeiterePerson;
    this.kinder0017 = b.kinder0017;
    this.jugendlicheErwachsene1824 = b.jugendlicheErwachsene1824;
    this.erwachsene2599 = b.erwachsene2599;
    this.wohnkostenFam1pers = b.wohnkostenFam1pers;
    this.wohnkostenFam2pers = b.wohnkostenFam2pers;
    this.wohnkostenFam3pers = b.wohnkostenFam3pers;
    this.wohnkostenFam4pers = b.wohnkostenFam4pers;
    this.wohnkostenFam5pluspers = b.wohnkostenFam5pluspers;
    this.wohnkostenPersoenlich1pers = b.wohnkostenPersoenlich1pers;
    this.wohnkostenPersoenlich2pers = b.wohnkostenPersoenlich2pers;
    this.wohnkostenPersoenlich3pers = b.wohnkostenPersoenlich3pers;
    this.wohnkostenPersoenlich4pers = b.wohnkostenPersoenlich4pers;
    this.wohnkostenPersoenlich5pluspers = b.wohnkostenPersoenlich5pluspers;
    this.preisProMahlzeit = b.preisProMahlzeit;
    this.maxSaeule3a = b.maxSaeule3a;
    this.anzahlWochenLehre = b.anzahlWochenLehre;
    this.anzahlWochenSchule = b.anzahlWochenSchule;
    this.vermoegensanteilInProzent = b.vermoegensanteilInProzent;
    this.reduzierungDesGrundbedarfs = b.reduzierungDesGrundbedarfs;
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = b.limiteAlterAntragsstellerHalbierungElternbeitrag;
    this.zweiterAuszahlungsterminMonat = b.zweiterAuszahlungsterminMonat;
    this.zweiterAuszahlungsterminTag = b.zweiterAuszahlungsterminTag;
    this.fristNachreichenDokumente = b.fristNachreichenDokumente;
    this.fristUploadUnterschriftenblatt = b.fristUploadUnterschriftenblatt;
    this.stichtagVolljaehrigkeitMedizinischeGrundversorgung = b.stichtagVolljaehrigkeitMedizinischeGrundversorgung;
    this.einreichfrist = b.einreichfrist;
  }

  public GesuchsperiodeUpdateDto() {
  }

  /**
   **/
  public GesuchsperiodeUpdateDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty(required = true, value = "bezeichnungDe")
  @NotNull public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty(required = true, value = "bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty(required = true, value = "bezeichnungFr")
  @NotNull public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty(required = true, value = "bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto fiskaljahr(Integer fiskaljahr) {
    this.fiskaljahr = fiskaljahr;
    return this;
  }

  
  @JsonProperty(required = true, value = "fiskaljahr")
  @NotNull public Integer getFiskaljahr() {
    return fiskaljahr;
  }

  @JsonProperty(required = true, value = "fiskaljahr")
  public void setFiskaljahr(Integer fiskaljahr) {
    this.fiskaljahr = fiskaljahr;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto gesuchsjahrId(UUID gesuchsjahrId) {
    this.gesuchsjahrId = gesuchsjahrId;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchsjahrId")
  @NotNull public UUID getGesuchsjahrId() {
    return gesuchsjahrId;
  }

  @JsonProperty(required = true, value = "gesuchsjahrId")
  public void setGesuchsjahrId(UUID gesuchsjahrId) {
    this.gesuchsjahrId = gesuchsjahrId;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeUpdateDto gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
    this.gesuchsperiodeStart = gesuchsperiodeStart;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchsperiodeStart")
  @NotNull public LocalDate getGesuchsperiodeStart() {
    return gesuchsperiodeStart;
  }

  @JsonProperty(required = true, value = "gesuchsperiodeStart")
  public void setGesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
    this.gesuchsperiodeStart = gesuchsperiodeStart;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeUpdateDto gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
    this.gesuchsperiodeStopp = gesuchsperiodeStopp;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchsperiodeStopp")
  @NotNull public LocalDate getGesuchsperiodeStopp() {
    return gesuchsperiodeStopp;
  }

  @JsonProperty(required = true, value = "gesuchsperiodeStopp")
  public void setGesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
    this.gesuchsperiodeStopp = gesuchsperiodeStopp;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeUpdateDto aufschaltterminStart(LocalDate aufschaltterminStart) {
    this.aufschaltterminStart = aufschaltterminStart;
    return this;
  }

  
  @JsonProperty(required = true, value = "aufschaltterminStart")
  @NotNull public LocalDate getAufschaltterminStart() {
    return aufschaltterminStart;
  }

  @JsonProperty(required = true, value = "aufschaltterminStart")
  public void setAufschaltterminStart(LocalDate aufschaltterminStart) {
    this.aufschaltterminStart = aufschaltterminStart;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeUpdateDto einreichefristNormal(LocalDate einreichefristNormal) {
    this.einreichefristNormal = einreichefristNormal;
    return this;
  }

  
  @JsonProperty(required = true, value = "einreichefristNormal")
  @NotNull public LocalDate getEinreichefristNormal() {
    return einreichefristNormal;
  }

  @JsonProperty(required = true, value = "einreichefristNormal")
  public void setEinreichefristNormal(LocalDate einreichefristNormal) {
    this.einreichefristNormal = einreichefristNormal;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeUpdateDto einreichefristReduziert(LocalDate einreichefristReduziert) {
    this.einreichefristReduziert = einreichefristReduziert;
    return this;
  }

  
  @JsonProperty(required = true, value = "einreichefristReduziert")
  @NotNull public LocalDate getEinreichefristReduziert() {
    return einreichefristReduziert;
  }

  @JsonProperty(required = true, value = "einreichefristReduziert")
  public void setEinreichefristReduziert(LocalDate einreichefristReduziert) {
    this.einreichefristReduziert = einreichefristReduziert;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto ausbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbKosten_SekII")
  @NotNull public Integer getAusbKostenSekII() {
    return ausbKostenSekII;
  }

  @JsonProperty(required = true, value = "ausbKosten_SekII")
  public void setAusbKostenSekII(Integer ausbKostenSekII) {
    this.ausbKostenSekII = ausbKostenSekII;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto ausbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbKosten_Tertiaer")
  @NotNull public Integer getAusbKostenTertiaer() {
    return ausbKostenTertiaer;
  }

  @JsonProperty(required = true, value = "ausbKosten_Tertiaer")
  public void setAusbKostenTertiaer(Integer ausbKostenTertiaer) {
    this.ausbKostenTertiaer = ausbKostenTertiaer;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto freibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
    return this;
  }

  
  @JsonProperty(required = true, value = "freibetrag_vermoegen")
  @NotNull public Integer getFreibetragVermoegen() {
    return freibetragVermoegen;
  }

  @JsonProperty(required = true, value = "freibetrag_vermoegen")
  public void setFreibetragVermoegen(Integer freibetragVermoegen) {
    this.freibetragVermoegen = freibetragVermoegen;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
    return this;
  }

  
  @JsonProperty(required = true, value = "freibetrag_erwerbseinkommen")
  @NotNull public Integer getFreibetragErwerbseinkommen() {
    return freibetragErwerbseinkommen;
  }

  @JsonProperty(required = true, value = "freibetrag_erwerbseinkommen")
  public void setFreibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
    this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto einkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty(required = true, value = "einkommensfreibetrag")
  @NotNull public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty(required = true, value = "einkommensfreibetrag")
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto elternbeteiligungssatz(Integer elternbeteiligungssatz) {
    this.elternbeteiligungssatz = elternbeteiligungssatz;
    return this;
  }

  
  @JsonProperty(required = true, value = "elternbeteiligungssatz")
  @NotNull public Integer getElternbeteiligungssatz() {
    return elternbeteiligungssatz;
  }

  @JsonProperty(required = true, value = "elternbeteiligungssatz")
  public void setElternbeteiligungssatz(Integer elternbeteiligungssatz) {
    this.elternbeteiligungssatz = elternbeteiligungssatz;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto vermogenSatzAngerechnet(Integer vermogenSatzAngerechnet) {
    this.vermogenSatzAngerechnet = vermogenSatzAngerechnet;
    return this;
  }

  
  @JsonProperty(required = true, value = "vermogenSatzAngerechnet")
  @NotNull public Integer getVermogenSatzAngerechnet() {
    return vermogenSatzAngerechnet;
  }

  @JsonProperty(required = true, value = "vermogenSatzAngerechnet")
  public void setVermogenSatzAngerechnet(Integer vermogenSatzAngerechnet) {
    this.vermogenSatzAngerechnet = vermogenSatzAngerechnet;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto integrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
    return this;
  }

  
  @JsonProperty(required = true, value = "integrationszulage")
  @NotNull public Integer getIntegrationszulage() {
    return integrationszulage;
  }

  @JsonProperty(required = true, value = "integrationszulage")
  public void setIntegrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto limiteEkFreibetragIntegrationszulage(Integer limiteEkFreibetragIntegrationszulage) {
    this.limiteEkFreibetragIntegrationszulage = limiteEkFreibetragIntegrationszulage;
    return this;
  }

  
  @JsonProperty(required = true, value = "limite_EkFreibetrag_Integrationszulage")
  @NotNull public Integer getLimiteEkFreibetragIntegrationszulage() {
    return limiteEkFreibetragIntegrationszulage;
  }

  @JsonProperty(required = true, value = "limite_EkFreibetrag_Integrationszulage")
  public void setLimiteEkFreibetragIntegrationszulage(Integer limiteEkFreibetragIntegrationszulage) {
    this.limiteEkFreibetragIntegrationszulage = limiteEkFreibetragIntegrationszulage;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
    return this;
  }

  
  @JsonProperty(required = true, value = "stipLimite_Minimalstipendium")
  @NotNull public Integer getStipLimiteMinimalstipendium() {
    return stipLimiteMinimalstipendium;
  }

  @JsonProperty(required = true, value = "stipLimite_Minimalstipendium")
  public void setStipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
    this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto person1(Integer person1) {
    this.person1 = person1;
    return this;
  }

  
  @JsonProperty(required = true, value = "person_1")
  @NotNull public Integer getPerson1() {
    return person1;
  }

  @JsonProperty(required = true, value = "person_1")
  public void setPerson1(Integer person1) {
    this.person1 = person1;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto personen2(Integer personen2) {
    this.personen2 = personen2;
    return this;
  }

  
  @JsonProperty(required = true, value = "personen_2")
  @NotNull public Integer getPersonen2() {
    return personen2;
  }

  @JsonProperty(required = true, value = "personen_2")
  public void setPersonen2(Integer personen2) {
    this.personen2 = personen2;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto personen3(Integer personen3) {
    this.personen3 = personen3;
    return this;
  }

  
  @JsonProperty(required = true, value = "personen_3")
  @NotNull public Integer getPersonen3() {
    return personen3;
  }

  @JsonProperty(required = true, value = "personen_3")
  public void setPersonen3(Integer personen3) {
    this.personen3 = personen3;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto personen4(Integer personen4) {
    this.personen4 = personen4;
    return this;
  }

  
  @JsonProperty(required = true, value = "personen_4")
  @NotNull public Integer getPersonen4() {
    return personen4;
  }

  @JsonProperty(required = true, value = "personen_4")
  public void setPersonen4(Integer personen4) {
    this.personen4 = personen4;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto personen5(Integer personen5) {
    this.personen5 = personen5;
    return this;
  }

  
  @JsonProperty(required = true, value = "personen_5")
  @NotNull public Integer getPersonen5() {
    return personen5;
  }

  @JsonProperty(required = true, value = "personen_5")
  public void setPersonen5(Integer personen5) {
    this.personen5 = personen5;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto personen6(Integer personen6) {
    this.personen6 = personen6;
    return this;
  }

  
  @JsonProperty(required = true, value = "personen_6")
  @NotNull public Integer getPersonen6() {
    return personen6;
  }

  @JsonProperty(required = true, value = "personen_6")
  public void setPersonen6(Integer personen6) {
    this.personen6 = personen6;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto personen7(Integer personen7) {
    this.personen7 = personen7;
    return this;
  }

  
  @JsonProperty(required = true, value = "personen_7")
  @NotNull public Integer getPersonen7() {
    return personen7;
  }

  @JsonProperty(required = true, value = "personen_7")
  public void setPersonen7(Integer personen7) {
    this.personen7 = personen7;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto proWeiterePerson(Integer proWeiterePerson) {
    this.proWeiterePerson = proWeiterePerson;
    return this;
  }

  
  @JsonProperty(required = true, value = "proWeiterePerson")
  @NotNull public Integer getProWeiterePerson() {
    return proWeiterePerson;
  }

  @JsonProperty(required = true, value = "proWeiterePerson")
  public void setProWeiterePerson(Integer proWeiterePerson) {
    this.proWeiterePerson = proWeiterePerson;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto kinder0017(Integer kinder0017) {
    this.kinder0017 = kinder0017;
    return this;
  }

  
  @JsonProperty(required = true, value = "kinder_00_17")
  @NotNull public Integer getKinder0017() {
    return kinder0017;
  }

  @JsonProperty(required = true, value = "kinder_00_17")
  public void setKinder0017(Integer kinder0017) {
    this.kinder0017 = kinder0017;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto jugendlicheErwachsene1824(Integer jugendlicheErwachsene1824) {
    this.jugendlicheErwachsene1824 = jugendlicheErwachsene1824;
    return this;
  }

  
  @JsonProperty(required = true, value = "jugendliche_erwachsene_18_24")
  @NotNull public Integer getJugendlicheErwachsene1824() {
    return jugendlicheErwachsene1824;
  }

  @JsonProperty(required = true, value = "jugendliche_erwachsene_18_24")
  public void setJugendlicheErwachsene1824(Integer jugendlicheErwachsene1824) {
    this.jugendlicheErwachsene1824 = jugendlicheErwachsene1824;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto erwachsene2599(Integer erwachsene2599) {
    this.erwachsene2599 = erwachsene2599;
    return this;
  }

  
  @JsonProperty(required = true, value = "erwachsene_25_99")
  @NotNull public Integer getErwachsene2599() {
    return erwachsene2599;
  }

  @JsonProperty(required = true, value = "erwachsene_25_99")
  public void setErwachsene2599(Integer erwachsene2599) {
    this.erwachsene2599 = erwachsene2599;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenFam1pers(Integer wohnkostenFam1pers) {
    this.wohnkostenFam1pers = wohnkostenFam1pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_fam_1pers")
  @NotNull public Integer getWohnkostenFam1pers() {
    return wohnkostenFam1pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_fam_1pers")
  public void setWohnkostenFam1pers(Integer wohnkostenFam1pers) {
    this.wohnkostenFam1pers = wohnkostenFam1pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenFam2pers(Integer wohnkostenFam2pers) {
    this.wohnkostenFam2pers = wohnkostenFam2pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_fam_2pers")
  @NotNull public Integer getWohnkostenFam2pers() {
    return wohnkostenFam2pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_fam_2pers")
  public void setWohnkostenFam2pers(Integer wohnkostenFam2pers) {
    this.wohnkostenFam2pers = wohnkostenFam2pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenFam3pers(Integer wohnkostenFam3pers) {
    this.wohnkostenFam3pers = wohnkostenFam3pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_fam_3pers")
  @NotNull public Integer getWohnkostenFam3pers() {
    return wohnkostenFam3pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_fam_3pers")
  public void setWohnkostenFam3pers(Integer wohnkostenFam3pers) {
    this.wohnkostenFam3pers = wohnkostenFam3pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenFam4pers(Integer wohnkostenFam4pers) {
    this.wohnkostenFam4pers = wohnkostenFam4pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_fam_4pers")
  @NotNull public Integer getWohnkostenFam4pers() {
    return wohnkostenFam4pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_fam_4pers")
  public void setWohnkostenFam4pers(Integer wohnkostenFam4pers) {
    this.wohnkostenFam4pers = wohnkostenFam4pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenFam5pluspers(Integer wohnkostenFam5pluspers) {
    this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_fam_5pluspers")
  @NotNull public Integer getWohnkostenFam5pluspers() {
    return wohnkostenFam5pluspers;
  }

  @JsonProperty(required = true, value = "wohnkosten_fam_5pluspers")
  public void setWohnkostenFam5pluspers(Integer wohnkostenFam5pluspers) {
    this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenPersoenlich1pers(Integer wohnkostenPersoenlich1pers) {
    this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_persoenlich_1pers")
  @NotNull public Integer getWohnkostenPersoenlich1pers() {
    return wohnkostenPersoenlich1pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_persoenlich_1pers")
  public void setWohnkostenPersoenlich1pers(Integer wohnkostenPersoenlich1pers) {
    this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenPersoenlich2pers(Integer wohnkostenPersoenlich2pers) {
    this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_persoenlich_2pers")
  @NotNull public Integer getWohnkostenPersoenlich2pers() {
    return wohnkostenPersoenlich2pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_persoenlich_2pers")
  public void setWohnkostenPersoenlich2pers(Integer wohnkostenPersoenlich2pers) {
    this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenPersoenlich3pers(Integer wohnkostenPersoenlich3pers) {
    this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_persoenlich_3pers")
  @NotNull public Integer getWohnkostenPersoenlich3pers() {
    return wohnkostenPersoenlich3pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_persoenlich_3pers")
  public void setWohnkostenPersoenlich3pers(Integer wohnkostenPersoenlich3pers) {
    this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenPersoenlich4pers(Integer wohnkostenPersoenlich4pers) {
    this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_persoenlich_4pers")
  @NotNull public Integer getWohnkostenPersoenlich4pers() {
    return wohnkostenPersoenlich4pers;
  }

  @JsonProperty(required = true, value = "wohnkosten_persoenlich_4pers")
  public void setWohnkostenPersoenlich4pers(Integer wohnkostenPersoenlich4pers) {
    this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto wohnkostenPersoenlich5pluspers(Integer wohnkostenPersoenlich5pluspers) {
    this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten_persoenlich_5pluspers")
  @NotNull public Integer getWohnkostenPersoenlich5pluspers() {
    return wohnkostenPersoenlich5pluspers;
  }

  @JsonProperty(required = true, value = "wohnkosten_persoenlich_5pluspers")
  public void setWohnkostenPersoenlich5pluspers(Integer wohnkostenPersoenlich5pluspers) {
    this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto preisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
    return this;
  }

  
  @JsonProperty(required = true, value = "preisProMahlzeit")
  @NotNull public Integer getPreisProMahlzeit() {
    return preisProMahlzeit;
  }

  @JsonProperty(required = true, value = "preisProMahlzeit")
  public void setPreisProMahlzeit(Integer preisProMahlzeit) {
    this.preisProMahlzeit = preisProMahlzeit;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto maxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
    return this;
  }

  
  @JsonProperty(required = true, value = "maxSaeule3a")
  @NotNull public Integer getMaxSaeule3a() {
    return maxSaeule3a;
  }

  @JsonProperty(required = true, value = "maxSaeule3a")
  public void setMaxSaeule3a(Integer maxSaeule3a) {
    this.maxSaeule3a = maxSaeule3a;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto anzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
    return this;
  }

  
  @JsonProperty(required = true, value = "anzahlWochenLehre")
  @NotNull public Integer getAnzahlWochenLehre() {
    return anzahlWochenLehre;
  }

  @JsonProperty(required = true, value = "anzahlWochenLehre")
  public void setAnzahlWochenLehre(Integer anzahlWochenLehre) {
    this.anzahlWochenLehre = anzahlWochenLehre;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto anzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
    return this;
  }

  
  @JsonProperty(required = true, value = "anzahlWochenSchule")
  @NotNull public Integer getAnzahlWochenSchule() {
    return anzahlWochenSchule;
  }

  @JsonProperty(required = true, value = "anzahlWochenSchule")
  public void setAnzahlWochenSchule(Integer anzahlWochenSchule) {
    this.anzahlWochenSchule = anzahlWochenSchule;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto vermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
    return this;
  }

  
  @JsonProperty(required = true, value = "vermoegensanteilInProzent")
  @NotNull public Integer getVermoegensanteilInProzent() {
    return vermoegensanteilInProzent;
  }

  @JsonProperty(required = true, value = "vermoegensanteilInProzent")
  public void setVermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
    this.vermoegensanteilInProzent = vermoegensanteilInProzent;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto reduzierungDesGrundbedarfs(Integer reduzierungDesGrundbedarfs) {
    this.reduzierungDesGrundbedarfs = reduzierungDesGrundbedarfs;
    return this;
  }

  
  @JsonProperty(required = true, value = "reduzierungDesGrundbedarfs")
  @NotNull public Integer getReduzierungDesGrundbedarfs() {
    return reduzierungDesGrundbedarfs;
  }

  @JsonProperty(required = true, value = "reduzierungDesGrundbedarfs")
  public void setReduzierungDesGrundbedarfs(Integer reduzierungDesGrundbedarfs) {
    this.reduzierungDesGrundbedarfs = reduzierungDesGrundbedarfs;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto limiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
    return this;
  }

  
  @JsonProperty(required = true, value = "limiteAlterAntragsstellerHalbierungElternbeitrag")
  @NotNull public Integer getLimiteAlterAntragsstellerHalbierungElternbeitrag() {
    return limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  @JsonProperty(required = true, value = "limiteAlterAntragsstellerHalbierungElternbeitrag")
  public void setLimiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
    this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto zweiterAuszahlungsterminMonat(Integer zweiterAuszahlungsterminMonat) {
    this.zweiterAuszahlungsterminMonat = zweiterAuszahlungsterminMonat;
    return this;
  }

  
  @JsonProperty(required = true, value = "zweiterAuszahlungsterminMonat")
  @NotNull public Integer getZweiterAuszahlungsterminMonat() {
    return zweiterAuszahlungsterminMonat;
  }

  @JsonProperty(required = true, value = "zweiterAuszahlungsterminMonat")
  public void setZweiterAuszahlungsterminMonat(Integer zweiterAuszahlungsterminMonat) {
    this.zweiterAuszahlungsterminMonat = zweiterAuszahlungsterminMonat;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto zweiterAuszahlungsterminTag(Integer zweiterAuszahlungsterminTag) {
    this.zweiterAuszahlungsterminTag = zweiterAuszahlungsterminTag;
    return this;
  }

  
  @JsonProperty(required = true, value = "zweiterAuszahlungsterminTag")
  @NotNull public Integer getZweiterAuszahlungsterminTag() {
    return zweiterAuszahlungsterminTag;
  }

  @JsonProperty(required = true, value = "zweiterAuszahlungsterminTag")
  public void setZweiterAuszahlungsterminTag(Integer zweiterAuszahlungsterminTag) {
    this.zweiterAuszahlungsterminTag = zweiterAuszahlungsterminTag;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto fristNachreichenDokumente(Integer fristNachreichenDokumente) {
    this.fristNachreichenDokumente = fristNachreichenDokumente;
    return this;
  }

  
  @JsonProperty(required = true, value = "fristNachreichenDokumente")
  @NotNull public Integer getFristNachreichenDokumente() {
    return fristNachreichenDokumente;
  }

  @JsonProperty(required = true, value = "fristNachreichenDokumente")
  public void setFristNachreichenDokumente(Integer fristNachreichenDokumente) {
    this.fristNachreichenDokumente = fristNachreichenDokumente;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto fristUploadUnterschriftenblatt(Integer fristUploadUnterschriftenblatt) {
    this.fristUploadUnterschriftenblatt = fristUploadUnterschriftenblatt;
    return this;
  }

  
  @JsonProperty(required = true, value = "fristUploadUnterschriftenblatt")
  @NotNull public Integer getFristUploadUnterschriftenblatt() {
    return fristUploadUnterschriftenblatt;
  }

  @JsonProperty(required = true, value = "fristUploadUnterschriftenblatt")
  public void setFristUploadUnterschriftenblatt(Integer fristUploadUnterschriftenblatt) {
    this.fristUploadUnterschriftenblatt = fristUploadUnterschriftenblatt;
  }

  /**
   * dd.MM.yyyy
   **/
  public GesuchsperiodeUpdateDto stichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung) {
    this.stichtagVolljaehrigkeitMedizinischeGrundversorgung = stichtagVolljaehrigkeitMedizinischeGrundversorgung;
    return this;
  }

  
  @JsonProperty(required = true, value = "stichtagVolljaehrigkeitMedizinischeGrundversorgung")
  @NotNull public LocalDate getStichtagVolljaehrigkeitMedizinischeGrundversorgung() {
    return stichtagVolljaehrigkeitMedizinischeGrundversorgung;
  }

  @JsonProperty(required = true, value = "stichtagVolljaehrigkeitMedizinischeGrundversorgung")
  public void setStichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung) {
    this.stichtagVolljaehrigkeitMedizinischeGrundversorgung = stichtagVolljaehrigkeitMedizinischeGrundversorgung;
  }

  /**
   **/
  public GesuchsperiodeUpdateDto einreichfrist(LocalDate einreichfrist) {
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
    GesuchsperiodeUpdateDto gesuchsperiodeUpdate = (GesuchsperiodeUpdateDto) o;
    return Objects.equals(this.bezeichnungDe, gesuchsperiodeUpdate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiodeUpdate.bezeichnungFr) &&
        Objects.equals(this.fiskaljahr, gesuchsperiodeUpdate.fiskaljahr) &&
        Objects.equals(this.gesuchsjahrId, gesuchsperiodeUpdate.gesuchsjahrId) &&
        Objects.equals(this.gesuchsperiodeStart, gesuchsperiodeUpdate.gesuchsperiodeStart) &&
        Objects.equals(this.gesuchsperiodeStopp, gesuchsperiodeUpdate.gesuchsperiodeStopp) &&
        Objects.equals(this.aufschaltterminStart, gesuchsperiodeUpdate.aufschaltterminStart) &&
        Objects.equals(this.einreichefristNormal, gesuchsperiodeUpdate.einreichefristNormal) &&
        Objects.equals(this.einreichefristReduziert, gesuchsperiodeUpdate.einreichefristReduziert) &&
        Objects.equals(this.ausbKostenSekII, gesuchsperiodeUpdate.ausbKostenSekII) &&
        Objects.equals(this.ausbKostenTertiaer, gesuchsperiodeUpdate.ausbKostenTertiaer) &&
        Objects.equals(this.freibetragVermoegen, gesuchsperiodeUpdate.freibetragVermoegen) &&
        Objects.equals(this.freibetragErwerbseinkommen, gesuchsperiodeUpdate.freibetragErwerbseinkommen) &&
        Objects.equals(this.einkommensfreibetrag, gesuchsperiodeUpdate.einkommensfreibetrag) &&
        Objects.equals(this.elternbeteiligungssatz, gesuchsperiodeUpdate.elternbeteiligungssatz) &&
        Objects.equals(this.vermogenSatzAngerechnet, gesuchsperiodeUpdate.vermogenSatzAngerechnet) &&
        Objects.equals(this.integrationszulage, gesuchsperiodeUpdate.integrationszulage) &&
        Objects.equals(this.limiteEkFreibetragIntegrationszulage, gesuchsperiodeUpdate.limiteEkFreibetragIntegrationszulage) &&
        Objects.equals(this.stipLimiteMinimalstipendium, gesuchsperiodeUpdate.stipLimiteMinimalstipendium) &&
        Objects.equals(this.person1, gesuchsperiodeUpdate.person1) &&
        Objects.equals(this.personen2, gesuchsperiodeUpdate.personen2) &&
        Objects.equals(this.personen3, gesuchsperiodeUpdate.personen3) &&
        Objects.equals(this.personen4, gesuchsperiodeUpdate.personen4) &&
        Objects.equals(this.personen5, gesuchsperiodeUpdate.personen5) &&
        Objects.equals(this.personen6, gesuchsperiodeUpdate.personen6) &&
        Objects.equals(this.personen7, gesuchsperiodeUpdate.personen7) &&
        Objects.equals(this.proWeiterePerson, gesuchsperiodeUpdate.proWeiterePerson) &&
        Objects.equals(this.kinder0017, gesuchsperiodeUpdate.kinder0017) &&
        Objects.equals(this.jugendlicheErwachsene1824, gesuchsperiodeUpdate.jugendlicheErwachsene1824) &&
        Objects.equals(this.erwachsene2599, gesuchsperiodeUpdate.erwachsene2599) &&
        Objects.equals(this.wohnkostenFam1pers, gesuchsperiodeUpdate.wohnkostenFam1pers) &&
        Objects.equals(this.wohnkostenFam2pers, gesuchsperiodeUpdate.wohnkostenFam2pers) &&
        Objects.equals(this.wohnkostenFam3pers, gesuchsperiodeUpdate.wohnkostenFam3pers) &&
        Objects.equals(this.wohnkostenFam4pers, gesuchsperiodeUpdate.wohnkostenFam4pers) &&
        Objects.equals(this.wohnkostenFam5pluspers, gesuchsperiodeUpdate.wohnkostenFam5pluspers) &&
        Objects.equals(this.wohnkostenPersoenlich1pers, gesuchsperiodeUpdate.wohnkostenPersoenlich1pers) &&
        Objects.equals(this.wohnkostenPersoenlich2pers, gesuchsperiodeUpdate.wohnkostenPersoenlich2pers) &&
        Objects.equals(this.wohnkostenPersoenlich3pers, gesuchsperiodeUpdate.wohnkostenPersoenlich3pers) &&
        Objects.equals(this.wohnkostenPersoenlich4pers, gesuchsperiodeUpdate.wohnkostenPersoenlich4pers) &&
        Objects.equals(this.wohnkostenPersoenlich5pluspers, gesuchsperiodeUpdate.wohnkostenPersoenlich5pluspers) &&
        Objects.equals(this.preisProMahlzeit, gesuchsperiodeUpdate.preisProMahlzeit) &&
        Objects.equals(this.maxSaeule3a, gesuchsperiodeUpdate.maxSaeule3a) &&
        Objects.equals(this.anzahlWochenLehre, gesuchsperiodeUpdate.anzahlWochenLehre) &&
        Objects.equals(this.anzahlWochenSchule, gesuchsperiodeUpdate.anzahlWochenSchule) &&
        Objects.equals(this.vermoegensanteilInProzent, gesuchsperiodeUpdate.vermoegensanteilInProzent) &&
        Objects.equals(this.reduzierungDesGrundbedarfs, gesuchsperiodeUpdate.reduzierungDesGrundbedarfs) &&
        Objects.equals(this.limiteAlterAntragsstellerHalbierungElternbeitrag, gesuchsperiodeUpdate.limiteAlterAntragsstellerHalbierungElternbeitrag) &&
        Objects.equals(this.zweiterAuszahlungsterminMonat, gesuchsperiodeUpdate.zweiterAuszahlungsterminMonat) &&
        Objects.equals(this.zweiterAuszahlungsterminTag, gesuchsperiodeUpdate.zweiterAuszahlungsterminTag) &&
        Objects.equals(this.fristNachreichenDokumente, gesuchsperiodeUpdate.fristNachreichenDokumente) &&
        Objects.equals(this.fristUploadUnterschriftenblatt, gesuchsperiodeUpdate.fristUploadUnterschriftenblatt) &&
        Objects.equals(this.stichtagVolljaehrigkeitMedizinischeGrundversorgung, gesuchsperiodeUpdate.stichtagVolljaehrigkeitMedizinischeGrundversorgung) &&
        Objects.equals(this.einreichfrist, gesuchsperiodeUpdate.einreichfrist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, fiskaljahr, gesuchsjahrId, gesuchsperiodeStart, gesuchsperiodeStopp, aufschaltterminStart, einreichefristNormal, einreichefristReduziert, ausbKostenSekII, ausbKostenTertiaer, freibetragVermoegen, freibetragErwerbseinkommen, einkommensfreibetrag, elternbeteiligungssatz, vermogenSatzAngerechnet, integrationszulage, limiteEkFreibetragIntegrationszulage, stipLimiteMinimalstipendium, person1, personen2, personen3, personen4, personen5, personen6, personen7, proWeiterePerson, kinder0017, jugendlicheErwachsene1824, erwachsene2599, wohnkostenFam1pers, wohnkostenFam2pers, wohnkostenFam3pers, wohnkostenFam4pers, wohnkostenFam5pluspers, wohnkostenPersoenlich1pers, wohnkostenPersoenlich2pers, wohnkostenPersoenlich3pers, wohnkostenPersoenlich4pers, wohnkostenPersoenlich5pluspers, preisProMahlzeit, maxSaeule3a, anzahlWochenLehre, anzahlWochenSchule, vermoegensanteilInProzent, reduzierungDesGrundbedarfs, limiteAlterAntragsstellerHalbierungElternbeitrag, zweiterAuszahlungsterminMonat, zweiterAuszahlungsterminTag, fristNachreichenDokumente, fristUploadUnterschriftenblatt, stichtagVolljaehrigkeitMedizinischeGrundversorgung, einreichfrist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeUpdateDto {\n");
    
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
    sb.append("    fristUploadUnterschriftenblatt: ").append(toIndentedString(fristUploadUnterschriftenblatt)).append("\n");
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
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static GesuchsperiodeUpdateDtoBuilder<?, ?> builder() {
    return new GesuchsperiodeUpdateDtoBuilderImpl();
  }

  private static final class GesuchsperiodeUpdateDtoBuilderImpl extends GesuchsperiodeUpdateDtoBuilder<GesuchsperiodeUpdateDto, GesuchsperiodeUpdateDtoBuilderImpl> {

    @Override
    protected GesuchsperiodeUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchsperiodeUpdateDto build() {
      return new GesuchsperiodeUpdateDto(this);
    }
  }

  public static abstract class GesuchsperiodeUpdateDtoBuilder<C extends GesuchsperiodeUpdateDto, B extends GesuchsperiodeUpdateDtoBuilder<C, B>>  {
    private String bezeichnungDe;
    private String bezeichnungFr;
    private Integer fiskaljahr;
    private UUID gesuchsjahrId;
    private LocalDate gesuchsperiodeStart;
    private LocalDate gesuchsperiodeStopp;
    private LocalDate aufschaltterminStart;
    private LocalDate einreichefristNormal;
    private LocalDate einreichefristReduziert;
    private Integer ausbKostenSekII;
    private Integer ausbKostenTertiaer;
    private Integer freibetragVermoegen;
    private Integer freibetragErwerbseinkommen;
    private Integer einkommensfreibetrag;
    private Integer elternbeteiligungssatz;
    private Integer vermogenSatzAngerechnet;
    private Integer integrationszulage;
    private Integer limiteEkFreibetragIntegrationszulage;
    private Integer stipLimiteMinimalstipendium;
    private Integer person1;
    private Integer personen2;
    private Integer personen3;
    private Integer personen4;
    private Integer personen5;
    private Integer personen6;
    private Integer personen7;
    private Integer proWeiterePerson;
    private Integer kinder0017;
    private Integer jugendlicheErwachsene1824;
    private Integer erwachsene2599;
    private Integer wohnkostenFam1pers;
    private Integer wohnkostenFam2pers;
    private Integer wohnkostenFam3pers;
    private Integer wohnkostenFam4pers;
    private Integer wohnkostenFam5pluspers;
    private Integer wohnkostenPersoenlich1pers;
    private Integer wohnkostenPersoenlich2pers;
    private Integer wohnkostenPersoenlich3pers;
    private Integer wohnkostenPersoenlich4pers;
    private Integer wohnkostenPersoenlich5pluspers;
    private Integer preisProMahlzeit;
    private Integer maxSaeule3a;
    private Integer anzahlWochenLehre;
    private Integer anzahlWochenSchule;
    private Integer vermoegensanteilInProzent;
    private Integer reduzierungDesGrundbedarfs;
    private Integer limiteAlterAntragsstellerHalbierungElternbeitrag;
    private Integer zweiterAuszahlungsterminMonat;
    private Integer zweiterAuszahlungsterminTag;
    private Integer fristNachreichenDokumente;
    private Integer fristUploadUnterschriftenblatt;
    private LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung;
    private LocalDate einreichfrist;
    protected abstract B self();

    public abstract C build();

    public B bezeichnungDe(String bezeichnungDe) {
      this.bezeichnungDe = bezeichnungDe;
      return self();
    }
    public B bezeichnungFr(String bezeichnungFr) {
      this.bezeichnungFr = bezeichnungFr;
      return self();
    }
    public B fiskaljahr(Integer fiskaljahr) {
      this.fiskaljahr = fiskaljahr;
      return self();
    }
    public B gesuchsjahrId(UUID gesuchsjahrId) {
      this.gesuchsjahrId = gesuchsjahrId;
      return self();
    }
    public B gesuchsperiodeStart(LocalDate gesuchsperiodeStart) {
      this.gesuchsperiodeStart = gesuchsperiodeStart;
      return self();
    }
    public B gesuchsperiodeStopp(LocalDate gesuchsperiodeStopp) {
      this.gesuchsperiodeStopp = gesuchsperiodeStopp;
      return self();
    }
    public B aufschaltterminStart(LocalDate aufschaltterminStart) {
      this.aufschaltterminStart = aufschaltterminStart;
      return self();
    }
    public B einreichefristNormal(LocalDate einreichefristNormal) {
      this.einreichefristNormal = einreichefristNormal;
      return self();
    }
    public B einreichefristReduziert(LocalDate einreichefristReduziert) {
      this.einreichefristReduziert = einreichefristReduziert;
      return self();
    }
    public B ausbKostenSekII(Integer ausbKostenSekII) {
      this.ausbKostenSekII = ausbKostenSekII;
      return self();
    }
    public B ausbKostenTertiaer(Integer ausbKostenTertiaer) {
      this.ausbKostenTertiaer = ausbKostenTertiaer;
      return self();
    }
    public B freibetragVermoegen(Integer freibetragVermoegen) {
      this.freibetragVermoegen = freibetragVermoegen;
      return self();
    }
    public B freibetragErwerbseinkommen(Integer freibetragErwerbseinkommen) {
      this.freibetragErwerbseinkommen = freibetragErwerbseinkommen;
      return self();
    }
    public B einkommensfreibetrag(Integer einkommensfreibetrag) {
      this.einkommensfreibetrag = einkommensfreibetrag;
      return self();
    }
    public B elternbeteiligungssatz(Integer elternbeteiligungssatz) {
      this.elternbeteiligungssatz = elternbeteiligungssatz;
      return self();
    }
    public B vermogenSatzAngerechnet(Integer vermogenSatzAngerechnet) {
      this.vermogenSatzAngerechnet = vermogenSatzAngerechnet;
      return self();
    }
    public B integrationszulage(Integer integrationszulage) {
      this.integrationszulage = integrationszulage;
      return self();
    }
    public B limiteEkFreibetragIntegrationszulage(Integer limiteEkFreibetragIntegrationszulage) {
      this.limiteEkFreibetragIntegrationszulage = limiteEkFreibetragIntegrationszulage;
      return self();
    }
    public B stipLimiteMinimalstipendium(Integer stipLimiteMinimalstipendium) {
      this.stipLimiteMinimalstipendium = stipLimiteMinimalstipendium;
      return self();
    }
    public B person1(Integer person1) {
      this.person1 = person1;
      return self();
    }
    public B personen2(Integer personen2) {
      this.personen2 = personen2;
      return self();
    }
    public B personen3(Integer personen3) {
      this.personen3 = personen3;
      return self();
    }
    public B personen4(Integer personen4) {
      this.personen4 = personen4;
      return self();
    }
    public B personen5(Integer personen5) {
      this.personen5 = personen5;
      return self();
    }
    public B personen6(Integer personen6) {
      this.personen6 = personen6;
      return self();
    }
    public B personen7(Integer personen7) {
      this.personen7 = personen7;
      return self();
    }
    public B proWeiterePerson(Integer proWeiterePerson) {
      this.proWeiterePerson = proWeiterePerson;
      return self();
    }
    public B kinder0017(Integer kinder0017) {
      this.kinder0017 = kinder0017;
      return self();
    }
    public B jugendlicheErwachsene1824(Integer jugendlicheErwachsene1824) {
      this.jugendlicheErwachsene1824 = jugendlicheErwachsene1824;
      return self();
    }
    public B erwachsene2599(Integer erwachsene2599) {
      this.erwachsene2599 = erwachsene2599;
      return self();
    }
    public B wohnkostenFam1pers(Integer wohnkostenFam1pers) {
      this.wohnkostenFam1pers = wohnkostenFam1pers;
      return self();
    }
    public B wohnkostenFam2pers(Integer wohnkostenFam2pers) {
      this.wohnkostenFam2pers = wohnkostenFam2pers;
      return self();
    }
    public B wohnkostenFam3pers(Integer wohnkostenFam3pers) {
      this.wohnkostenFam3pers = wohnkostenFam3pers;
      return self();
    }
    public B wohnkostenFam4pers(Integer wohnkostenFam4pers) {
      this.wohnkostenFam4pers = wohnkostenFam4pers;
      return self();
    }
    public B wohnkostenFam5pluspers(Integer wohnkostenFam5pluspers) {
      this.wohnkostenFam5pluspers = wohnkostenFam5pluspers;
      return self();
    }
    public B wohnkostenPersoenlich1pers(Integer wohnkostenPersoenlich1pers) {
      this.wohnkostenPersoenlich1pers = wohnkostenPersoenlich1pers;
      return self();
    }
    public B wohnkostenPersoenlich2pers(Integer wohnkostenPersoenlich2pers) {
      this.wohnkostenPersoenlich2pers = wohnkostenPersoenlich2pers;
      return self();
    }
    public B wohnkostenPersoenlich3pers(Integer wohnkostenPersoenlich3pers) {
      this.wohnkostenPersoenlich3pers = wohnkostenPersoenlich3pers;
      return self();
    }
    public B wohnkostenPersoenlich4pers(Integer wohnkostenPersoenlich4pers) {
      this.wohnkostenPersoenlich4pers = wohnkostenPersoenlich4pers;
      return self();
    }
    public B wohnkostenPersoenlich5pluspers(Integer wohnkostenPersoenlich5pluspers) {
      this.wohnkostenPersoenlich5pluspers = wohnkostenPersoenlich5pluspers;
      return self();
    }
    public B preisProMahlzeit(Integer preisProMahlzeit) {
      this.preisProMahlzeit = preisProMahlzeit;
      return self();
    }
    public B maxSaeule3a(Integer maxSaeule3a) {
      this.maxSaeule3a = maxSaeule3a;
      return self();
    }
    public B anzahlWochenLehre(Integer anzahlWochenLehre) {
      this.anzahlWochenLehre = anzahlWochenLehre;
      return self();
    }
    public B anzahlWochenSchule(Integer anzahlWochenSchule) {
      this.anzahlWochenSchule = anzahlWochenSchule;
      return self();
    }
    public B vermoegensanteilInProzent(Integer vermoegensanteilInProzent) {
      this.vermoegensanteilInProzent = vermoegensanteilInProzent;
      return self();
    }
    public B reduzierungDesGrundbedarfs(Integer reduzierungDesGrundbedarfs) {
      this.reduzierungDesGrundbedarfs = reduzierungDesGrundbedarfs;
      return self();
    }
    public B limiteAlterAntragsstellerHalbierungElternbeitrag(Integer limiteAlterAntragsstellerHalbierungElternbeitrag) {
      this.limiteAlterAntragsstellerHalbierungElternbeitrag = limiteAlterAntragsstellerHalbierungElternbeitrag;
      return self();
    }
    public B zweiterAuszahlungsterminMonat(Integer zweiterAuszahlungsterminMonat) {
      this.zweiterAuszahlungsterminMonat = zweiterAuszahlungsterminMonat;
      return self();
    }
    public B zweiterAuszahlungsterminTag(Integer zweiterAuszahlungsterminTag) {
      this.zweiterAuszahlungsterminTag = zweiterAuszahlungsterminTag;
      return self();
    }
    public B fristNachreichenDokumente(Integer fristNachreichenDokumente) {
      this.fristNachreichenDokumente = fristNachreichenDokumente;
      return self();
    }
    public B fristUploadUnterschriftenblatt(Integer fristUploadUnterschriftenblatt) {
      this.fristUploadUnterschriftenblatt = fristUploadUnterschriftenblatt;
      return self();
    }
    public B stichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung) {
      this.stichtagVolljaehrigkeitMedizinischeGrundversorgung = stichtagVolljaehrigkeitMedizinischeGrundversorgung;
      return self();
    }
    public B einreichfrist(LocalDate einreichfrist) {
      this.einreichfrist = einreichfrist;
      return self();
    }
  }
}
