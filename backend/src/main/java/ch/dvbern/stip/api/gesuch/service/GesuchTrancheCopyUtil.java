package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.AenderungsantragCreateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchTrancheCopyUtil {
    public GesuchTranche createAenderungstranche(
        final GesuchTranche gesuchTranche,
        final AenderungsantragCreateDto createDto
    ) {
        final var gesuch = gesuchTranche.getGesuch();
        final var newTranche = new GesuchTranche();
        newTranche.setGesuch(gesuch);

        final var gesuchsperiodeStart = gesuch.getGesuchsperiode().getGesuchsperiodeStart();
        final var gesuchsperiodeStopp = gesuch.getGesuchsperiode().getGesuchsperiodeStopp();

        // TODO KSTIP-1111: unit test this
        final var startDate = DateUtil.clamp(
            createDto.getStart(),
            gesuchsperiodeStart,
            gesuchsperiodeStopp
        );
        final var endDate = DateUtil.clamp(
            createDto.getEnd() != null ? createDto.getEnd() : gesuchsperiodeStopp,
            gesuchsperiodeStart,
            gesuchsperiodeStopp
        );

//        final var startDate = DateUtil.after(
//            createDto.getStart(),
//            gesuch.getGesuchsperiode().getGesuchsperiodeStart()
//        );
//        final var endDate = DateUtil.before(
//            createDto.getEnd(),
//            gesuch.getGesuchsperiode().getGesuchsperiodeStopp()
//        );

        newTranche.setGueltigkeit(new DateRange(startDate, endDate));
        newTranche.setGesuchFormular(copy(gesuchTranche.getGesuchFormular()));
        return newTranche;
    }

    private GesuchFormular copy(final GesuchFormular gesuchFormular) {
        return new GesuchFormular();
    }
}
