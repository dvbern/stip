package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 */
public enum AusbildungsortDto {
  
  AUSLAND("AUSLAND"),
  
  BERN("BERN"),
  
  ILANZ("ILANZ"),
  
  SOLOTHURN("SOLOTHURN"),
  
  AARAU("AARAU"),
  
  ALTDORF("ALTDORF"),
  
  APPENZELL("APPENZELL"),
  
  ARLESHEIM("ARLESHEIM"),
  
  BADEN("BADEN"),
  
  BASEL("BASEL"),
  
  BELP("BELP"),
  
  BEROMUENSTER("BEROMUENSTER"),
  
  BIEL("BIEL"),
  
  BRIENZ("BRIENZ"),
  
  BRIG("BRIG"),
  
  BRUGG("BRUGG"),
  
  BURGDORF("BURGDORF"),
  
  BUERGENSTOCK("BUERGENSTOCK"),
  
  CHENE_BOUGERIES("CHENE_BOUGERIES"),
  
  CHUR("CHUR"),
  
  CORCELLES("CORCELLES"),
  
  COURTETELLE("COURTETELLE"),
  
  DAVOS("DAVOS"),
  
  DELEMONT("DELEMONT"),
  
  DICKEN("DICKEN"),
  
  DIETIKON("DIETIKON"),
  
  DORNACH("DORNACH"),
  
  DUEBENDORF("DUEBENDORF"),
  
  EBIKON("EBIKON"),
  
  ENGELBERG("ENGELBERG"),
  
  EPALINGES("EPALINGES"),
  
  FRIBOURG("FRIBOURG"),
  
  FRUTIGEN("FRUTIGEN"),
  
  GENEVE("GENEVE"),
  
  GISWIL("GISWIL"),
  
  GRENCHEN("GRENCHEN"),
  
  GRUYERES("GRUYERES"),
  
  GSTEIGWILER("GSTEIGWILER"),
  
  HERZOGENBUCHSEE("HERZOGENBUCHSEE"),
  
  HUENENBERG("HUENENBERG"),
  
  HUNIBACH("HUNIBACH"),
  
  HUTTWIL("HUTTWIL"),
  
  IMMENSEE("IMMENSEE"),
  
  INTERLAKEN("INTERLAKEN"),
  
  ITTIGEN("ITTIGEN"),
  
  KIENTAL("KIENTAL"),
  
  KLOTEN("KLOTEN"),
  
  KOENIZ("KOENIZ"),
  
  KONOLFINGEN("KONOLFINGEN"),
  
  KOPPIGEN("KOPPIGEN"),
  
  LA_CHAUX_DE_FONDS("LA_CHAUX_DE_FONDS"),
  
  LA_NEUVEVILLE("LA_NEUVEVILLE"),
  
  LANDQUART("LANDQUART"),
  
  LANGENTHAL("LANGENTHAL"),
  
  LANGNAU_IM_EMMENTAL("LANGNAU_IM_EMMENTAL"),
  
  LAUFEN("LAUFEN"),
  
  LAUSANNE("LAUSANNE"),
  
  LE_LOCLE("LE_LOCLE"),
  
  LIESTAL("LIESTAL"),
  
  LINDAU("LINDAU"),
  
  LUGANO("LUGANO"),
  
  LUZERN("LUZERN"),
  
  LYSS("LYSS"),
  
  MANNO("MANNO"),
  
  MARLY("MARLY"),
  
  MORGES("MORGES"),
  
  MOERSCHWIL("MOERSCHWIL"),
  
  MOUTIER("MOUTIER"),
  
  MUENCHENBUCHSEE("MUENCHENBUCHSEE"),
  
  MUENCHENSTEIN("MUENCHENSTEIN"),
  
  MUENSINGEN("MUENSINGEN"),
  
  NEUCHATEL("NEUCHATEL"),
  
  NOTTWIL("NOTTWIL"),
  
  OLTEN("OLTEN"),
  
  OSTERMUNDIGEN("OSTERMUNDIGEN"),
  
  PASSUGG("PASSUGG"),
  
  PAYERNE("PAYERNE"),
  
  PETIT_LANCY("PETIT_LANCY"),
  
  PORRENTRUY("PORRENTRUY"),
  
  POSIEUX("POSIEUX"),
  
  RAPPERSWIL_SG("RAPPERSWIL_SG"),
  
  RHEINAU("RHEINAU"),
  
  RORSCHACH("RORSCHACH"),
  
  SAMEDAN("SAMEDAN"),
  
  SARNEN("SARNEN"),
  
  SAXON("SAXON"),
  
  SCHAFFHAUSEN("SCHAFFHAUSEN"),
  
  SCHIERS("SCHIERS"),
  
  SCHUEPFHEIM("SCHUEPFHEIM"),
  
  SCHWADERLOCH("SCHWADERLOCH"),
  
  SCHWYZ("SCHWYZ"),
  
  SIERRE("SIERRE"),
  
  SION("SION"),
  
  SISSACH("SISSACH"),
  
  SPIEZ("SPIEZ"),
  
  ST_GALLEN("ST_GALLEN"),
  
  STEFFISBURG("STEFFISBURG"),
  
  ST_IMIER("ST_IMIER"),
  
  SURSEE("SURSEE"),
  
  THUN("THUN"),
  
  TOLOCHENAZ("TOLOCHENAZ"),
  
  TRAMELAN("TRAMELAN"),
  
  VADUZ("VADUZ"),
  
  VERSCIO("VERSCIO"),
  
  VEVEY("VEVEY"),
  
  VILLARS_BOZON("VILLARS_BOZON"),
  
  VISP("VISP"),
  
  WEINFELDEN("WEINFELDEN"),
  
  WIL("WIL"),
  
  WINTERTHUR("WINTERTHUR"),
  
  WISEN("WISEN"),
  
  WORB("WORB"),
  
  YVERDON_LES_BAINS("YVERDON_LES_BAINS"),
  
  ZIZERS("ZIZERS"),
  
  ZOFINGEN("ZOFINGEN"),
  
  ZOLLIKOFEN("ZOLLIKOFEN"),
  
  ZUG("ZUG"),
  
  ZUOZ("ZUOZ"),
  
  ZUERICH("ZUERICH"),
  
  ZWEISIMMEN("ZWEISIMMEN"),
  
  PULLY("PULLY"),
  
  CERNIER("CERNIER"),
  
  HONDRICH("HONDRICH"),
  
  LE_SENTIER("LE_SENTIER");

  private String value;

  AusbildungsortDto(String value) {
    this.value = value;
  }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static AusbildungsortDto fromString(String s) {
      for (AusbildungsortDto b : AusbildungsortDto.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
	}
	
  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static AusbildungsortDto fromValue(String value) {
    for (AusbildungsortDto b : AusbildungsortDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

