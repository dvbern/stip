import { Locator, Page } from '@playwright/test';

import { SteuererklaerungUpdate } from '@dv/shared/model/gesuch';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

export class SteruererklaerungPO {
  public elems: {
    page: Page;
    form: Locator;
    steuererklaerungInBernRadio: Locator;
    unterhaltsbeitraege: Locator;
    renten: Locator;
    ergaenzungsleistungen: Locator;
    einnahmenBGSA: Locator;
    andereEinnahmen: Locator;

    loading: Locator;
    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      form: page.getByTestId('form-eltern-steuererklaerung-form'),
      steuererklaerungInBernRadio: page.getByTestId(
        'form-eltern-steuererklaerung-steuererklaerungInBern',
      ),

      unterhaltsbeitraege: page.getByTestId(
        'form-eltern-steuererklaerung-unterhaltsbeitraege',
      ),
      renten: page.getByTestId('form-eltern-steuererklaerung-renten'),
      ergaenzungsleistungen: page.getByTestId(
        'form-eltern-steuererklaerung-ergaenzungsleistungen',
      ),
      einnahmenBGSA: page.getByTestId(
        'form-eltern-steuererklaerung-einnahmenBGSA',
      ),
      andereEinnahmen: page.getByTestId(
        'form-eltern-steuererklaerung-andereEinnahmen',
      ),

      loading: page.getByTestId('loading'),
      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  async fillSteuererklaerung(
    item: Omit<SteuererklaerungUpdate, 'steuerdatenTyp'>,
  ) {
    await selectMatRadio(
      this.elems.steuererklaerungInBernRadio,
      item.steuererklaerungInBern,
    );

    if (item.unterhaltsbeitraege) {
      await this.elems.unterhaltsbeitraege.fill(`${item.unterhaltsbeitraege}`);
    }
    if (item.renten) {
      await this.elems.renten.fill(`${item.renten}`);
    }
    if (item.ergaenzungsleistungen) {
      await this.elems.ergaenzungsleistungen.fill(
        `${item.ergaenzungsleistungen}`,
      );
    }
    if (item.einnahmenBGSA) {
      await this.elems.einnahmenBGSA.fill(`${item.einnahmenBGSA}`);
    }
    if (item.andereEinnahmen) {
      await this.elems.andereEinnahmen.fill(`${item.andereEinnahmen}`);
    }

    await expectFormToBeValid(this.elems.form);
  }
}
