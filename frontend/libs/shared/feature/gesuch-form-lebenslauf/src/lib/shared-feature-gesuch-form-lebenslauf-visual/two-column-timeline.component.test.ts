import { InputSignal } from '@angular/core';
import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { Ausbildung, LebenslaufItemUpdate } from '@dv/shared/model/gesuch';
import {
  TEST_ABSCHLUESSE,
  getTranslocoModule,
  provideSharedPatternVitestTestAusbildungstaetten,
} from '@dv/shared/pattern/vitest-test-setup';

import { TwoColumnTimelineComponent } from './two-column-timeline.component';

const translations = {
  de: {
    'shared.form.lebenslauf.item.name.zusatzfrage.FACHRICHTUNG': 'Fachrichtung',
  },
};
type InputsFrom<T> = {
  [K in keyof T]: T[K] extends InputSignal<infer U> ? U : never;
};

async function setup(
  props: Partial<InputsFrom<TwoColumnTimelineComponent>> = {},
) {
  const ref = await render(TwoColumnTimelineComponent, {
    imports: [getTranslocoModule(translations)],
    providers: [
      provideMockStore({ initialState: { language: { language: 'de' } } }),
      provideSharedPatternVitestTestAusbildungstaetten(),
      AusbildungsstaetteStore,
    ],
    inputs: {
      language: 'de',
      lebenslaufItems: [],
      startDate: new Date(),
      ausbildungsstaettes: [],
      ausbildung: {
        ausbildungBegin: '01.2022',
        ausbildungEnd: '02.2022',
      } as Ausbildung,
      ...props,
    },
  });
  return ref;
}

describe(TwoColumnTimelineComponent.name, () => {
  it('should have berufsbezeichnung as label for item with berufsbezeichnung', async () => {
    const berufsbezeichnung = 'Mein Beruf';
    const items = [
      {
        abschlussId: TEST_ABSCHLUESSE.abschlussBerufsbezeichnung1.id,
        fachrichtungBerufsbezeichnung: berufsbezeichnung,
        wohnsitz: 'BE',
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    const { getAllByTestId } = await setup({ lebenslaufItems: items });

    const labels = getAllByTestId('two-column-timeline-sub-label');
    const oneHasBerufsbezeichnung = labels.some((label) =>
      label.textContent?.includes(berufsbezeichnung),
    );
    expect(oneHasBerufsbezeichnung).toBeTruthy();
  });

  it('should have fachrichtung as label for item with fachrichtung', async () => {
    const fachrichtungBerufsbezeichnung = 'Meine Fachbezeichnung';
    const items = [
      {
        abschlussId: TEST_ABSCHLUESSE.abschlussFachrichtung1.id,
        fachrichtungBerufsbezeichnung,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    const { getAllByTestId } = await setup({ lebenslaufItems: items });

    const labels = getAllByTestId('two-column-timeline-sub-label');
    const oneHasFachrichtung = labels.some((label) =>
      label.textContent?.includes(fachrichtungBerufsbezeichnung),
    );
    expect(oneHasFachrichtung).toBeTruthy();
  });
});
