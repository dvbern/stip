import { Locator, Page } from '@playwright/test';

import { FreiwilligDarlehen } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';

import { expectFormToBeValid } from '../utils';

export class DarlehenPO {
  public elems: {
    page: Page;
    loading: Locator;
    form: Locator;
    betragDarlehen: Locator;
    schulden: Locator;
    fieldSetGrund: Locator;
    anzahlBetreibungen: Locator;
    grundNichtBerechtigtCheckbox: Locator;
    grundAusbildungZwoelfJahreCheckbox: Locator;
    grundHoheGebuehrenCheckbox: Locator;
    grundAnschaffungenFuerAusbildungCheckbox: Locator;
    grundZweitausbildungCheckbox: Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-darlehen-loading'),
      form: page.getByTestId('form-darlehen-form'),
      betragDarlehen: page.getByTestId('form-darlehen-betragDarlehen'),
      schulden: page.getByTestId('form-darlehen-schulden'),
      fieldSetGrund: page.getByTestId('form-darlehen-gruende'),
      anzahlBetreibungen: page.getByTestId('form-darlehen-anzahlBetreibungen'),
      grundNichtBerechtigtCheckbox: page.getByTestId(
        'form-darlehen-grundNichtBerechtigt',
      ),
      grundAusbildungZwoelfJahreCheckbox: page.getByTestId(
        'form-darlehen-grundAusbildungZwoelfJahre',
      ),
      grundHoheGebuehrenCheckbox: page.getByTestId(
        'form-darlehen-grundHoheGebuehren',
      ),
      grundAnschaffungenFuerAusbildungCheckbox: page.getByTestId(
        'form-darlehen-grundAnschaffungenFuerAusbildung',
      ),
      grundZweitausbildungCheckbox: page.getByTestId(
        'form-darlehen-grundZweitausbildung',
      ),
      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  public async fillDarlehenForm(darlehen: FreiwilligDarlehen) {
    if (isDefined(darlehen.betragGewuenscht)) {
      await this.elems.betragDarlehen.fill(`${darlehen.betragGewuenscht}`);
    }

    if (isDefined(darlehen.schulden)) {
      await this.elems.schulden.fill(`${darlehen.schulden}`);
    }
    if (isDefined(darlehen.anzahlBetreibungen)) {
      await this.elems.anzahlBetreibungen.fill(
        `${darlehen.anzahlBetreibungen}`,
      );
    }

    // todo: fix in within new darlehen E2E
    // await handleCheckbox(
    //   this.elems.grundNichtBerechtigtCheckbox,
    //   darlehen.gruende[0],
    // );
    // await handleCheckbox(
    //   this.elems.grundAusbildungZwoelfJahreCheckbox,
    //   darlehen.grundAusbildungZwoelfJahre,
    // );
    // await handleCheckbox(
    //   this.elems.grundHoheGebuehrenCheckbox,
    //   darlehen.grundHoheGebuehren,
    // );
    // await handleCheckbox(
    //   this.elems.grundAnschaffungenFuerAusbildungCheckbox,
    //   darlehen.grundAnschaffungenFuerAusbildung,
    // );
    // await handleCheckbox(
    //   this.elems.grundZweitausbildungCheckbox,
    //   darlehen.grundZweitausbildung,
    // );
    await expectFormToBeValid(this.elems.form);
  }
}
