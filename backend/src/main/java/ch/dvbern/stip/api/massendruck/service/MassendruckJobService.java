package ch.dvbern.stip.api.massendruck.service;

import java.time.LocalDate;

import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn;
import ch.dvbern.stip.generated.dto.GetMassendruckJobQueryTypeDto;
import ch.dvbern.stip.generated.dto.MassendruckJobStatusDto;
import ch.dvbern.stip.generated.dto.MassendruckJobTypDto;
import ch.dvbern.stip.generated.dto.PaginatedMassendruckJobDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobService {
    public PaginatedMassendruckJobDto getAllMassendruckJobs(
        GetMassendruckJobQueryTypeDto getMassendruckJobs,
        String massendruckJobNumber,
        String userErstellt,
        LocalDate timestampErstellt,
        MassendruckJobStatusDto massendruckJobStatus,
        MassendruckJobTypDto massendruckJobTyp,
        MassendruckJobSortColumn sortColumn,
        SortOrder sortOrder
    ) {

        return null;
    }
}
