package ch.dvbern.stip.api.plz.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GeoCollectionItem {
//    String type;
    List<GeoCollectionItemFeature> features;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class GeoCollectionItemFeature {
        JsonNode assets;
    }
}
