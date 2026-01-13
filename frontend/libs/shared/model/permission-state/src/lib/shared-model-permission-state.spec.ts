import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';

import {
  getGesuchPermissions,
  isNotReadonly,
} from './shared-model-permission-state';

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

describe('isNotReadonly', () => {
  describe('when appType is sachbearbeitung-app', () => {
    it('should return true if user has V0_Sachbearbeiter role', () => {
      const rolesMap = { V0_Sachbearbeiter: true } as const;
      expect(isNotReadonly('sachbearbeitung-app', rolesMap, undefined)).toBe(
        true,
      );
    });

    it('should return true if user has V0_Jurist role', () => {
      const rolesMap = { V0_Jurist: true } as const;
      expect(isNotReadonly('sachbearbeitung-app', rolesMap, undefined)).toBe(
        true,
      );
    });

    it('should return false if user has neither V0_Sachbearbeiter nor V0_Jurist role', () => {
      const rolesMap = { V0_Gesuchsteller: true } as const;
      expect(isNotReadonly('sachbearbeitung-app', rolesMap, undefined)).toBe(
        false,
      );
    });
  });

  describe('when appType is gesuch-app', () => {
    it('should return true if not delegated (delegierung is undefined)', () => {
      const rolesMap = {};
      expect(isNotReadonly('gesuch-app', rolesMap, undefined)).toBe(true);
    });

    it('should return true if delegated (delegierung is boolean false)', () => {
      const rolesMap = {};
      const delegierung = false;
      expect(isNotReadonly('gesuch-app', rolesMap, delegierung)).toBe(true);
    });

    it('should return false if delegated (delegierung is boolean true)', () => {
      const rolesMap = {};
      const delegierung = true;
      expect(isNotReadonly('gesuch-app', rolesMap, delegierung)).toBe(false);
    });

    it('should return true if delegated but not angenommen', () => {
      const rolesMap = {};
      const delegierung = { delegierungAngenommen: false };
      expect(isNotReadonly('gesuch-app', rolesMap, delegierung)).toBe(true);
    });

    it('should return true if delegated and user has V0_Sozialdienst-Mitarbeiter role', () => {
      const rolesMap = { 'V0_Sozialdienst-Mitarbeiter': true } as const;
      const delegierung = { delegierungAngenommen: true };
      expect(isNotReadonly('gesuch-app', rolesMap, delegierung)).toBe(true);
    });

    it('should return true if delegated but not angenommen and user has V0_Sozialdienst-Mitarbeiter role', () => {
      const rolesMap = { 'V0_Sozialdienst-Mitarbeiter': true } as const;
      const delegierung = { delegierungAngenommen: false };
      expect(isNotReadonly('gesuch-app', rolesMap, delegierung)).toBe(true);
    });

    it('should return false if delegated and user does not have V0_Sozialdienst-Mitarbeiter role', () => {
      const rolesMap = { V0_Gesuchsteller: true } as const;
      const delegierung = { delegierungAngenommen: true };
      expect(isNotReadonly('gesuch-app', rolesMap, delegierung)).toBe(false);
    });
  });
});
