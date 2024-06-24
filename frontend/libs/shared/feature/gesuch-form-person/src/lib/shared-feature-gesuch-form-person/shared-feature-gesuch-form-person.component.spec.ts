import { Niederlassungsstatus } from '@dv/shared/model/gesuch';

import { isFluechtlingOrHasAusweisB } from './shared-feature-gesuch-form-person.component';

describe('SharedFeatureGesuchFormPersonComponent', () => {
  it.each([
    [Niederlassungsstatus.FLUECHTLING, true],
    [Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B, true],
    [Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C, false],
  ])(
    'should check if niederlassung is fluechtling or Ausweis B, with [%s] it should be [%s]',
    (niederlassung, expected) => {
      expect(isFluechtlingOrHasAusweisB(niederlassung)).toBe(expected);
    },
  );
});
