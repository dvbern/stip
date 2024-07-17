import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';

import { setGesuchFromularReadonly } from './shared-util-readonly-state';

const gesuch: SharedModelGesuch = {
  fall: {
    id: '',
    fallNummer: 0,
    mandant: '',
  },
  gesuchsperiode: {
    id: '',
    bezeichnungDe: '',
    bezeichnungFr: '',
    gueltigkeitStatus: 'ENTWURF',
    gesuchsperiodeStart: '',
    gesuchsperiodeStopp: '',
    aufschaltterminStart: '',
    gesuchsjahr: {
      id: '',
      bezeichnungDe: '',
      bezeichnungFr: '',
      technischesJahr: 0,
      gueltigkeitStatus: 'ENTWURF',
    },
    ausbKosten_SekII: 0,
    ausbKosten_Tertiaer: 0,
  },
  gesuchStatus: Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
  gesuchNummer: 0,
  id: '',
  aenderungsdatum: '',
  gesuchTrancheToWorkWith: {
    id: '',
    gueltigAb: '',
    gueltigBis: '',
  },
};

describe('when App Gesuchsteller', () => {
  const isGesuchApp = true;
  const isSachbearbeitungApp = false;

  it('should be readonly if in bearbeitung Sachbearbeiter', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_SB;

    expect(
      setGesuchFromularReadonly(gesuch, isGesuchApp, isSachbearbeitungApp),
    ).toBeTruthy();
  });
});

describe('when App Sachbearbeitung', () => {
  const isGesuchApp = false;
  const isSachbearbeitungApp = true;

  it('should be readonly if in bearbeitung Gesuchsteller', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_GS;

    expect(
      setGesuchFromularReadonly(gesuch, isGesuchApp, isSachbearbeitungApp),
    ).toBeTruthy();
  });
});
