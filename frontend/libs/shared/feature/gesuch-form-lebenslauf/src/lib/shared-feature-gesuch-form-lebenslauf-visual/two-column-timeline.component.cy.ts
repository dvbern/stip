import { TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslateTestingModule } from 'ngx-translate-testing';
import {
  LebenslaufItemUpdate,
  Ausbildung,
  LebenslaufAusbildungsArt,
} from '@dv/shared/model/gesuch';
import { LebenslaufPo } from '../../../cypress/support/lebenslauf.po';
import { TwoColumnTimelineComponent } from './two-column-timeline.component';

const translations = {
  de: {
    'shared.form.lebenslauf.item.subtype.bildungsart.FACHMATURITAET':
      'Fachmaturität',
  },
};

describe(TwoColumnTimelineComponent.name, () => {
  beforeEach(() => {
    TestBed.overrideComponent(TwoColumnTimelineComponent, {
      add: {
        imports: [],
        providers: [],
      },
    });
  });

  // TODO: Fix error while running in parallel
  it.skip('should have berufszeichnung as label for item with berufsbezeichnung', () => {
    const berufsbezeichnung = 'Mein Beruf';
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
        berufsbezeichnung: berufsbezeichnung,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    mountWith(items);
    LebenslaufPo.getWrapper()
      .find('[data-testid="two-column-timeline-sub-label"]')
      .contains(berufsbezeichnung)
      .should('exist');
  });

  it.skip('should have titelDesAbschlusses as label for item with titelDesAbschlusses', () => {
    const titelDesAbschlusses = 'Mein Abschluss';
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS,
        titelDesAbschlusses,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    mountWith(items);
    LebenslaufPo.getWrapper()
      .find('[data-testid="two-column-timeline-label"]')
      .contains(titelDesAbschlusses)
      .should('exist');
  });

  it.skip('should have fachrichtung as label for item with fachrichtung', () => {
    const fachrichtung = 'Meine Fachbezeichnung';
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.MASTER,
        fachrichtung,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    mountWith(items);
    LebenslaufPo.getWrapper()
      .find('[data-testid="two-column-timeline-sub-label"]')
      .contains(fachrichtung)
      .should('exist');
  });

  it.skip('should have translated bildungsart as label for item with no näherer Bezeichnung', () => {
    const items = [
      {
        bildungsart: LebenslaufAusbildungsArt.FACHMATURITAET,
        von: '02.2022',
        bis: '03.2022',
      } as LebenslaufItemUpdate,
    ];
    mountWith(items);
    LebenslaufPo.getWrapper()
      .find('[data-testid="two-column-timeline-label"]')
      .contains(
        translations.de[
          'shared.form.lebenslauf.item.subtype.bildungsart.FACHMATURITAET'
        ],
      )
      .should('exist');
  });
});

function mountWith(lebenslaufItems: LebenslaufItemUpdate[]): void {
  cy.mount(TwoColumnTimelineComponent, {
    imports: [
      TranslateTestingModule.withTranslations(translations),
      BrowserAnimationsModule,
    ],
    providers: [
      provideMockStore({ initialState: { language: { language: 'de' } } }),
    ],
    componentProperties: {
      lebenslaufItems,
      ausbildung: {
        ausbildungBegin: '01.2022',
        ausbildungEnd: '02.2022',
      } as Ausbildung,
      startDate: new Date(),
      ausbildungsstaettes: [],
    },
  });
}
