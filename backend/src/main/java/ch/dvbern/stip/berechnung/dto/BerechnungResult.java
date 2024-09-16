package ch.dvbern.stip.berechnung.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionEvent;
@RequiredArgsConstructor

@Getter
public class BerechnungResult {
    private final Integer stipendien;
    private final List<DMNDecisionResult> decisionResults;
    private final List<AfterEvaluateDecisionEvent> decisionEventList;
}
