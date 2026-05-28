package ch.dvbern.stip.integration.plzfetch.adapter.dummy.service;

import java.util.Optional;
import java.util.Set;


import ch.dvbern.stip.integration.plzfetch.domain.model.PlzFetchData;
import ch.dvbern.stip.integration.plzfetch.domain.port.PlzFetchPort;

public class DummyPlzFetchAdapter implements PlzFetchPort {

	@Override
	public Optional<Set<PlzFetchData>> fetchData() {
        return Optional.of(Set.of(new PlzFetchData("3000", "Bern", "be")));
	}


}
