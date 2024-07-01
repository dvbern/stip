package ch.dvbern.stip.berechnung.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kie.dmn.api.core.DMNDecisionResult;

@RequiredArgsConstructor
@Builder
@Getter
public class PersonenImHaushaltResult {
    private final int noBudgetsRequired;
    private final int kinderImHaushalt1;
    private final int kinderImHaushalt2;
    private final int personenImHaushalt1;
    private final int personenImHaushalt2;
    private final List<DMNDecisionResult> decisionResults;

    public static PersonenImHaushaltResultBuilder builder() {
        return new PersonenImHaushaltResultBuilder();
    }
}
