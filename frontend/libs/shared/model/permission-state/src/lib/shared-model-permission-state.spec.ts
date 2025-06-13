import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';

import { getGesuchPermissions } from './shared-model-permission-state';

const gesuch: SharedModelGesuch = {
  fallId: '',
  fallNummer: '',
  ausbildungId: '',
  gesuchsperiode: {
    id: '',
    bezeichnungDe: '',
    bezeichnungFr: '',
    gueltigkeitStatus: 'ENTWURF',
    gesuchsperiodeStart: '',
    gesuchsperiodeStopp: '',
    aufschaltterminStart: '',
    einreichefristNormal: '',
    einreichefristReduziert: '',
    fristNachreichenDokumente: 0,
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
  gesuchNummer: '',
  id: '',
  aenderungsdatum: '',
  gesuchTrancheToWorkWith: {
    id: '',
    gueltigAb: '',
    gueltigBis: '',
    status: 'UEBERPRUEFEN',
    typ: 'TRANCHE',
  },
  verfuegt: false,
};

describe('when App Gesuchsteller', () => {
  it('should be readonly if in bearbeitung Sachbearbeiter', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_SB;

    expect(
      getGesuchPermissions(gesuch, 'gesuch-app', {
        V0_Gesuchsteller: true,
      }).permissions.canWrite,
    ).toBe(false);
  });
});

describe('when App Sachbearbeitung', () => {
  it('should be readonly if in bearbeitung Gesuchsteller', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_GS;

    expect(
      getGesuchPermissions(gesuch, 'sachbearbeitung-app', {
        V0_Sachbearbeiter: true,
      }).permissions.canWrite,
    ).toBe(false);
  });
});
