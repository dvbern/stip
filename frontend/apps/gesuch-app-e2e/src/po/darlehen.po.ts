import { Locator, Page } from '@playwright/test';

import { Darlehen } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  expectFormToBeValid,
  handleCheckbox,
} from '@dv/shared/util-fn/e2e-util';

export class DarlehenPO {
  public elems: {
    page: Page;
    loading: Locator;
    form: Locator;
    willDarlehenCheckbox: Locator;
    betragDarlehen: Locator;
    betragBezogenKanton: Locator;
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
      willDarlehenCheckbox: page.getByTestId('form-darlehen-willDarlehen'),
      betragDarlehen: page.getByTestId('form-darlehen-betragDarlehen'),
      betragBezogenKanton: page.getByTestId(
        'form-darlehen-betragBezogenKanton',
      ),
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
      buttonSaveContinue: page.getByTestId(
        'form-darlehen-button-save-continue',
      ),
      buttonNext: page.getByTestId('form-darlehen-button-next'),
    };
  }

  public async fillDarlehenForm(darlehen: Darlehen) {
    await handleCheckbox(
      this.elems.willDarlehenCheckbox,
      darlehen.willDarlehen,
    );

    if (isDefined(darlehen.betragDarlehen)) {
      await this.elems.betragDarlehen.fill(`${darlehen.betragDarlehen}`);
    }
    if (isDefined(darlehen.betragBezogenKanton)) {
      await this.elems.betragBezogenKanton.fill(
        `${darlehen.betragBezogenKanton}`,
      );
    }
    if (isDefined(darlehen.schulden)) {
      await this.elems.schulden.fill(`${darlehen.schulden}`);
    }
    if (isDefined(darlehen.anzahlBetreibungen)) {
      await this.elems.anzahlBetreibungen.fill(
        `${darlehen.anzahlBetreibungen}`,
      );
    }

    await handleCheckbox(
      this.elems.grundNichtBerechtigtCheckbox,
      darlehen.grundNichtBerechtigt,
    );
    await handleCheckbox(
      this.elems.grundAusbildungZwoelfJahreCheckbox,
      darlehen.grundAusbildungZwoelfJahre,
    );
    await handleCheckbox(
      this.elems.grundHoheGebuehrenCheckbox,
      darlehen.grundHoheGebuehren,
    );
    await handleCheckbox(
      this.elems.grundAnschaffungenFuerAusbildungCheckbox,
      darlehen.grundAnschaffungenFuerAusbildung,
    );
    await handleCheckbox(
      this.elems.grundZweitausbildungCheckbox,
      darlehen.grundZweitausbildung,
    );
    await expectFormToBeValid(this.elems.form);
  }
}
