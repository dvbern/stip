import {
  Elternschaftsteilung,
  GesuchFormularUpdate,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { type } from '@dv/shared/model/type-util';

import {
  AUSZAHLUNG,
  EINNAHMEN_KOSTEN,
  ELTERN,
  FAMILIENSITUATION,
  GESCHWISTER,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  isStepDisabled,
  isStepValid,
} from './gesuch-form-steps';
import { SharedModelGesuchFormStep } from './shared-model-gesuch-form';

const partnerCases = [
  ['disable', 'LEDIG', true],
  ['disable', 'VERWITWET', true],
  ['disable', 'AUFGELOESTE_PARTNERSCHAFT', true],
  ['disable', 'GESCHIEDEN_GERICHTLICH', true],
  ['enable', 'KONKUBINAT', false],
  ['enable', 'VERHEIRATET', false],
  ['enable', 'EINGETRAGENE_PARTNERSCHAFT', false],
] as const;

const alimentAufteilungCases = [
  ['enable', Elternschaftsteilung.MUTTER, false],
  ['enable', Elternschaftsteilung.VATER, false],
  ['disable', Elternschaftsteilung.GEMEINSAM, true],
] as const;

const validationCases = type<
  [SharedModelGesuchFormStep, keyof GesuchFormularUpdate][]
>([
  [PERSON, 'personInAusbildung'],
  [FAMILIENSITUATION, 'familiensituation'],
  [ELTERN, 'elterns'],
  [GESCHWISTER, 'geschwisters'],
  [PARTNER, 'partner'],
  [KINDER, 'kinds'],
  [AUSZAHLUNG, 'auszahlung'],
  [EINNAHMEN_KOSTEN, 'einnahmenKosten'],
]).map(
  ([step, ...rest]) =>
    // Add toString to make jest output more readable
    [{ ...step, toString: () => step.route }, ...rest] as const,
);

describe('GesuchFormSteps', () => {
  it.each(partnerCases)(
    'should %s Partner Step if GS is %s',
    (_, zivilstand, state) => {
      expect(
        isStepDisabled(
          PARTNER,
          {
            gesuchStatus: 'IN_BEARBEITUNG_GS',
            gesuchTrancheToWorkWith: {
              status: 'IN_BEARBEITUNG_GS',
              gesuchFormular: {
                personInAusbildung: {
                  zivilstand,
                },
              },
            },
          } as SharedModelGesuch,
          {},
        ),
      ).toBe(state);
    },
  );

  it.each(alimentAufteilungCases)(
    'should %s Eltern Step if GS is %s',
    (_, werZahltAlimente, state) => {
      expect(
        isStepDisabled(
          ELTERN,
          {
            gesuchStatus: 'IN_BEARBEITUNG_GS',
            gesuchTrancheToWorkWith: {
              status: 'IN_BEARBEITUNG_GS',
              gesuchFormular: {
                familiensituation: {
                  werZahltAlimente,
                },
              },
            },
          } as SharedModelGesuch,
          {},
        ),
      ).toBe(state);
    },
  );

  it.each(validationCases)(
    'route %s should be valid if %s is set',
    (step, field) => {
      expect(
        isStepValid(step, { [field]: {} } as any, {
          errors: [],
          hasDocuments: null,
        }),
      ).toBe('VALID');
    },
  );

  it('route lebenslaufItems should be valid if personInAusbildung and ausbildung are set', () => {
    expect(
      isStepValid(
        LEBENSLAUF,
        { personInAusbildung: {} as any, ausbildung: {} as any },
        { errors: [], hasDocuments: null },
      ),
    ).toBe('VALID');
  });

  it.each(validationCases)(
    'route %s validity should be undefined if %s is not set',
    (step, field) => {
      expect(
        isStepValid(step, { [field]: null } as any, {
          errors: [],
          hasDocuments: null,
        }),
      ).toBe(undefined);
    },
  );

  it('route lebenslaufItems should be undefined if personInAusbildung is not set', () => {
    expect(
      isStepValid(
        LEBENSLAUF,
        { personInAusbildung: undefined, ausbildung: {} as any },
        { errors: [], hasDocuments: null },
      ),
    ).toBe(undefined);
  });
});
