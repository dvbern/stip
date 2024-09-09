import {
  Eltern,
  ElternAbwesenheitsGrund,
  ElternTyp,
  Familiensituation,
  PersonInAusbildung,
} from '@dv/shared/model/gesuch';

import {
  calculateExpectElternteil,
  getChangesForForm,
  getChangesForList,
} from './shared-util-fn-gesuch-util';

describe('calculateExpectElternteil', () => {
  it.each([
    // verstorben geschlecht  elternstatus     expectParent

    // niemand verstorben -> beide Eltern expected
    [false, ElternTyp.VATER, undefined, true],
    [false, ElternTyp.MUTTER, undefined, true],

    // jemand verstorben -> expected, wenn Grund "WEDER_NOCH"
    [true, ElternTyp.VATER, ElternAbwesenheitsGrund.VERSTORBEN, false],
    [true, ElternTyp.VATER, ElternAbwesenheitsGrund.UNBEKANNT, false],
    [true, ElternTyp.VATER, ElternAbwesenheitsGrund.WEDER_NOCH, true],
    [true, ElternTyp.MUTTER, ElternAbwesenheitsGrund.VERSTORBEN, false],
    [true, ElternTyp.MUTTER, ElternAbwesenheitsGrund.UNBEKANNT, false],
    [true, ElternTyp.MUTTER, ElternAbwesenheitsGrund.WEDER_NOCH, true],

    // elternteilUnbekanntVerstorben undefined -> beide expected
    [undefined, ElternTyp.VATER, ElternAbwesenheitsGrund.UNBEKANNT, true],
    [undefined, ElternTyp.MUTTER, ElternAbwesenheitsGrund.UNBEKANNT, true],
  ])(
    'jemand verstorben/unbekannt: %s, geschlecht %s mit status %s => expected Elternteil %s',
    (
      elternteilUnbekanntVerstorben: boolean | undefined,
      elternTyp: ElternTyp,
      grund: ElternAbwesenheitsGrund | undefined,
      expectElternteil: boolean,
    ) => {
      const familienSituation: Familiensituation = {
        elternteilUnbekanntVerstorben,
      } as Familiensituation;
      if (elternTyp === 'VATER') {
        familienSituation.vaterUnbekanntVerstorben = grund;
      }
      if (elternTyp === 'MUTTER') {
        familienSituation.mutterUnbekanntVerstorben = grund;
      }

      expect(calculateExpectElternteil(elternTyp, familienSituation)).toEqual(
        expectElternteil,
      );
    },
  );
});

describe('calculate differences', () => {
  it('should calculate the diff for PersonInAusbildung', () => {
    const [original, updated] = [
      {
        wohnsitz: 'EIGENER_HAUSHALT',
        adresse: {
          strasse: 'Musterstrasse',
          plz: '1234',
          ort: 'Musterort',
          land: 'CH',
        },
      },
      {
        wohnsitz: 'MUTTER_VATER',
        wohnsitzAnteilMutter: 50,
        wohnsitzAnteilVater: 50,
        adresse: {
          strasse: 'Musterstrasse',
          plz: '1234',
          ort: 'Musterort',
          land: 'CH',
        },
      },
    ] satisfies Partial<PersonInAusbildung>[] as PersonInAusbildung[];

    const changes = getChangesForForm(original, updated);
    expect(changes).toEqual({
      wohnsitz: 'MUTTER_VATER',
      wohnsitzAnteilMutter: 50,
      wohnsitzAnteilVater: 50,
    });
  });

  it('should calculate the diff of Eltern (MUTTER/-VATER)', () => {
    const [original, updated] = [
      [
        {
          id: '123',
          elternTyp: ElternTyp.MUTTER,
          nachname: 'Sanchez',
          vorname: 'Laura',
          geburtsdatum: '2000-01-01',
          identischerZivilrechtlicherWohnsitz: true,
        },
        {
          id: '321',
          elternTyp: ElternTyp.VATER,
          nachname: 'Sanchez',
          vorname: 'Alejandro',
          geburtsdatum: '2000-01-02',
          identischerZivilrechtlicherWohnsitz: true,
        },
      ],
      [
        {
          id: '123',
          elternTyp: ElternTyp.MUTTER,
          nachname: 'Alvarez',
          vorname: 'Elvira',
          geburtsdatum: '2000-01-03',
          identischerZivilrechtlicherWohnsitz: false,
          identischerZivilrechtlicherWohnsitzPLZ: '1234',
          identischerZivilrechtlicherWohnsitzOrt: 'Musterort',
        },
      ],
    ] satisfies Partial<Eltern>[][] as Eltern[][];

    const changes = getChangesForList(updated, original, (e) => e.elternTyp);
    expect(changes).toEqual({
      changesByIdentifier: {
        MUTTER: {
          geburtsdatum: '2000-01-01',
          identischerZivilrechtlicherWohnsitz: true,
          identischerZivilrechtlicherWohnsitzOrt: 'Musterort',
          identischerZivilrechtlicherWohnsitzPLZ: '1234',
          nachname: 'Sanchez',
          vorname: 'Laura',
        },
      },
      newEntries: {},
    });
  });

  it('should calculate the diff of Eltern (MUTTER/+VATER)', () => {
    const [original, changed] = [
      [{ id: '123', elternTyp: ElternTyp.MUTTER, nachname: 'Sanchez' }],
      [
        { id: '123', elternTyp: ElternTyp.MUTTER, nachname: 'Alvarez' },
        { id: '321', elternTyp: ElternTyp.VATER, nachname: 'Alvarez' },
      ],
    ] satisfies Partial<Eltern>[][] as Eltern[][];

    const changes = getChangesForList(changed, original, (e) => e.elternTyp);
    expect(changes).toEqual({
      changesByIdentifier: {
        MUTTER: { nachname: 'Sanchez' },
      },
      newEntries: { VATER: true },
    });
  });
});
