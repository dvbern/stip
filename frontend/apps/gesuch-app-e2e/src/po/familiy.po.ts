import { Locator, Page } from '@playwright/test';

import { Familiensituation } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

export class FamilyPO {
  public elems: {
    page: Page;
    loading: Locator;
    form: Locator;
    elternVerheiratetZusammenRadio: Locator;
    gerichtlicheAlimentenregelungRadio: Locator;
    werZahltAlimenteSelect: Locator;
    elternteilUnbekanntVerstorbenRadio: Locator;
    mutterUnbekanntVerstorbenRadio: Locator;
    mutterUnbekanntGrundRadio: Locator;
    vaterUnbekanntVerstorbenRadio: Locator;
    vaterUnbekanntGrundRadio: Locator;
    mutterWiederVerheiratetRadio: Locator;
    vaterWiederverheiratetRadio: Locator;
    sorgerechtSelect: Locator;
    obhutSelect: Locator;

    buttonNext: Locator;
    buttonPrevious: Locator;

    buttonSaveContinue: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-family-loading'),

      form: page.getByTestId('form-family-form'),

      elternVerheiratetZusammenRadio: page.getByTestId(
        'form-family-elternVerheiratetZusammen',
      ),
      gerichtlicheAlimentenregelungRadio: page.getByTestId(
        'form-family-gerichtlicheAlimentenregelung',
      ),
      werZahltAlimenteSelect: page.getByTestId('form-family-werZahltAlimente'),
      elternteilUnbekanntVerstorbenRadio: page.getByTestId(
        'form-family-elternteilUnbekanntVerstorben',
      ),
      mutterUnbekanntVerstorbenRadio: page.getByTestId(
        'form-family-mutterUnbekanntVerstorben',
      ),
      mutterUnbekanntGrundRadio: page.getByTestId(
        'form-family-mutterUnbekanntGrund',
      ),
      vaterUnbekanntVerstorbenRadio: page.getByTestId(
        'form-family-vaterUnbekanntVerstorben',
      ),
      vaterUnbekanntGrundRadio: page.getByTestId(
        'form-family-vaterUnbekanntGrund',
      ),
      mutterWiederVerheiratetRadio: page.getByTestId(
        'form-family-mutterWiederverheiratet',
      ),
      vaterWiederverheiratetRadio: page.getByTestId(
        'form-family-vaterWiederverheiratet',
      ),
      sorgerechtSelect: page.getByTestId('form-family-sorgerecht'),
      obhutSelect: page.getByTestId('form-family-obhut'),

      buttonNext: page.getByTestId('stepper-next'),
      buttonPrevious: page.getByTestId('stepper-previous'),
      buttonSaveContinue: page.getByTestId('button-save-continue'),
    };
  }

  async fillMinimalForm(item: Familiensituation) {
    await selectMatRadio(
      this.elems.elternVerheiratetZusammenRadio,
      item.elternVerheiratetZusammen,
    );

    await expectFormToBeValid(this.elems.form);
  }

  async fillUnbekanntOderVerstorben(item: Familiensituation) {
    await selectMatRadio(
      this.elems.elternVerheiratetZusammenRadio,
      item.elternVerheiratetZusammen,
    );
    await selectMatRadio(
      this.elems.gerichtlicheAlimentenregelungRadio,
      item.gerichtlicheAlimentenregelung ?? false,
    );
    await selectMatRadio(
      this.elems.elternteilUnbekanntVerstorbenRadio,
      item.elternteilUnbekanntVerstorben ?? true,
    );
    await selectMatRadio(
      this.elems.mutterUnbekanntVerstorbenRadio,
      item.mutterUnbekanntVerstorben ?? 'WEDER_NOCH',
    );
    await selectMatRadio(
      this.elems.vaterUnbekanntVerstorbenRadio,
      item.vaterUnbekanntVerstorben ?? 'WEDER_NOCH',
    );
    if (isDefined(item.mutterWiederverheiratet)) {
      await selectMatRadio(
        this.elems.mutterWiederVerheiratetRadio,
        item.mutterWiederverheiratet,
      );
    }
    if (isDefined(item.vaterWiederverheiratet)) {
      await selectMatRadio(
        this.elems.vaterWiederverheiratetRadio,
        item.vaterWiederverheiratet,
      );
    }

    await expectFormToBeValid(this.elems.form);
  }
}
