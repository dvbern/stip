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
    einreichefristDays: number | null;
    reduzierterBeitrag: boolean;
    einreichefristAbgelaufen: boolean;
    erfassbar: boolean;
  }) => ({
    ...options,
    gesuchsperiodeStart: '2024-07-01',
    gesuchsperiodeStopp: '2025-06-30',
    aufschaltterminStart: '2024-07-01',
    aufschaltterminStopp: '2025-06-30',
    einreichefristNormal: '2024-12-31',
    einreichefristReduziert: '2025-02-28',
    semester: 'HERBST',
    yearsLabel: '24/25',
  });
  const createFruehlingData = (options: {
    currentDate: string;
    einreichefristDays: number | null;
    reduzierterBeitrag: boolean;
    einreichefristAbgelaufen: boolean;
    erfassbar: boolean;
  }) => ({
    ...options,
    gesuchsperiodeStart: '2024-01-01',
    gesuchsperiodeStopp: '2024-12-31',
    aufschaltterminStart: '2024-01-01',
    aufschaltterminStopp: '2024-12-31',
    einreichefristNormal: '2024-05-31',
    einreichefristReduziert: '2024-08-30',
    semester: 'FRUEHLING',
    yearsLabel: '24/24',
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
      einreichefristDays: 120,
      reduzierterBeitrag: false,
      einreichefristAbgelaufen: false,
      erfassbar: true,
    }),
    createFruehlingData({
      currentDate: '2024-03-01',
      einreichefristDays: 91,
      reduzierterBeitrag: false,
      einreichefristAbgelaufen: false,
      erfassbar: true,
    }),
    createHerbstData({
      currentDate: '2025-01-01',
      einreichefristDays: 58,
      reduzierterBeitrag: true,
      einreichefristAbgelaufen: false,
      erfassbar: true,
    }),
    createFruehlingData({
      currentDate: '2024-10-01',
      einreichefristDays: null,
      reduzierterBeitrag: true,
      einreichefristAbgelaufen: true,
      erfassbar: true,
    }),
    createHerbstData({
      currentDate: '2025-08-01',
      einreichefristDays: null,
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
