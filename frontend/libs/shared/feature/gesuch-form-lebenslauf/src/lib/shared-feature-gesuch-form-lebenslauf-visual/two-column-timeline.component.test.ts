import { provideMockStore } from '@ngrx/store/testing';
import { render } from '@testing-library/angular';
import { TranslateTestingModule } from 'ngx-translate-testing';

import {
  Ausbildung,
  LebenslaufAusbildungsArt,
  LebenslaufItemUpdate,
} from '@dv/shared/model/gesuch';

import { TwoColumnTimelineComponent } from './two-column-timeline.component';

const translations = {
  de: {
    'shared.form.lebenslauf.item.subtype.bildungsart.FACHMATURITAET':
      'Fachmaturität',
  },
};

async function setup(props: Partial<TwoColumnTimelineComponent> = {}) {
  return await render(TwoColumnTimelineComponent, {
    imports: [TranslateTestingModule.withTranslations(translations)],
    providers: [
      provideMockStore({ initialState: { language: { language: 'de' } } }),
    ],
    componentProperties: {
      lebenslaufItems: [],
      ausbildung: {
        ausbildungBegin: '01.2022',
        ausbildungEnd: '02.2022',
      } as Ausbildung,
      startDate: new Date(),
      ausbildungsstaettes: [],
      ...props,
    },
  });
}

describe(TwoColumnTimelineComponent.name, () => {
  it('should have berufsbezeichnung as label for item with berufsbezeichnung', async () => {
    const berufsbezeichnung = 'Mein Beruf';
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
        berufsbezeichnung: berufsbezeichnung,
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

  it('should have titelDesAbschlusses as label for item with titelDesAbschlusses', async () => {
    const titelDesAbschlusses = 'Mein Abschluss';
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS,
        titelDesAbschlusses,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    const { getAllByTestId } = await setup({ lebenslaufItems: items });

    const labels = getAllByTestId('two-column-timeline-label');
    const oneHasTitelDesAbschlusses = labels.some((label) =>
      label.textContent?.includes(titelDesAbschlusses),
    );
    expect(oneHasTitelDesAbschlusses).toBeTruthy();
  });

  it('should have fachrichtung as label for item with fachrichtung', async () => {
    const fachrichtung = 'Meine Fachbezeichnung';
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.MASTER,
        fachrichtung,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    const { getAllByTestId } = await setup({ lebenslaufItems: items });

    const labels = getAllByTestId('two-column-timeline-sub-label');
    const oneHasFachrichtung = labels.some((label) =>
      label.textContent?.includes(fachrichtung),
    );
    expect(oneHasFachrichtung).toBeTruthy();
  });

  it('should have translated bildungsart as label for item with no näherer Bezeichnung', async () => {
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.FACHMATURITAET,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    const { getAllByTestId } = await setup({ lebenslaufItems: items });

    const labels = getAllByTestId('two-column-timeline-label');

    const oneHasBildungsart = labels.some((label) =>
      label.textContent?.includes(
        translations.de[
          'shared.form.lebenslauf.item.subtype.bildungsart.FACHMATURITAET'
        ],
      ),
    );
    expect(oneHasBildungsart).toBeTruthy();
  });
});
