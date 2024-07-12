import {
  prepareGesuchsperiode,
  selectSharedDataAccessGesuchsperiodesView,
} from './shared-data-access-gesuchsperiode.selectors';

describe('selectSharedDataAccessGesuchsperiodesView', () => {
  it('selects view', () => {
    const state = {
      gesuchsperiodes: [],
      loading: false,
      error: undefined,
    };
    const result = selectSharedDataAccessGesuchsperiodesView.projector(state);
    expect(result).toEqual(state);
  });
});

describe('prepareGesuchsperiode', () => {
  const createHerbstData = (options: {
    currentDate: string;
    reduzierterBeitrag: boolean;
    einreichefristAbgelaufen: boolean;
    erfassbar: boolean;
  }) => ({
    ...options,
    gesuchsperiodeStart: '2024-08-01',
    gesuchsperiodeStopp: '2025-07-31',
    aufschaltterminStart: '2024-08-01',
    aufschaltterminStopp: '2025-07-31',
    einreichefristNormal: '2024-12-31',
    einreichefristReduziert: '2025-03-31',
    semester: 'HERBST',
    yearsLabel: '24/25',
  });
  const createFruehlingData = (options: {
    currentDate: string;
    reduzierterBeitrag: boolean;
    einreichefristAbgelaufen: boolean;
    erfassbar: boolean;
  }) => ({
    ...options,
    gesuchsperiodeStart: '2024-02-01',
    gesuchsperiodeStopp: '2025-01-31',
    aufschaltterminStart: '2024-02-01',
    aufschaltterminStopp: '2025-01-31',
    einreichefristNormal: '2024-06-30',
    einreichefristReduziert: '2024-09-30',
    semester: 'FRUEHLING',
    yearsLabel: '24/25',
  });

  beforeAll(() => {
    jest.useFakeTimers();
  });

  afterAll(() => {
    jest.useRealTimers();
  });

  it.each([
    createHerbstData({
      currentDate: '2024-09-01',
      reduzierterBeitrag: false,
      einreichefristAbgelaufen: false,
      erfassbar: true,
    }),
    createFruehlingData({
      currentDate: '2024-03-01',
      reduzierterBeitrag: false,
      einreichefristAbgelaufen: false,
      erfassbar: true,
    }),
    createHerbstData({
      currentDate: '2025-01-01',
      reduzierterBeitrag: true,
      einreichefristAbgelaufen: false,
      erfassbar: true,
    }),
    createFruehlingData({
      currentDate: '2024-10-01',
      reduzierterBeitrag: true,
      einreichefristAbgelaufen: true,
      erfassbar: true,
    }),
    createHerbstData({
      currentDate: '2025-08-01',
      reduzierterBeitrag: true,
      einreichefristAbgelaufen: true,
      erfassbar: false,
    }),
  ])(
    'should prepare gesuchsperiode from $gesuchsperiodeStart - $gesuchsperiodeStopp\n' +
      '\twith Einrechefrist normal $einreichefristNormal, reduziert $einreichefristReduziert:\n' +
      '\t* semester: $semester\n' +
      '\t* yearsLabel: $yearsLabel\n' +
      '\t* reduzierterBeitrag: $reduzierterBeitrag\n' +
      '\t* einreichefristAbgelaufen: $einreichefristAbgelaufen\n' +
      '\t* erfassbar: $erfassbar',
    (periode) => {
      jest.setSystemTime(new Date(periode.currentDate));
      expect(prepareGesuchsperiode(periode)).toEqual(periode);
    },
  );
});
