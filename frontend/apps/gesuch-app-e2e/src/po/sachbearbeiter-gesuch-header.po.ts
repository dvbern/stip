import { Locator, Page } from '@playwright/test';

import { StatusUebergang } from '@dv/shared/util/gesuch';

export class SachbearbeiterGesuchHeaderPO {
  public elems: {
    page: Page;
    trancheMenu: Locator;
    trancheMenuItems: Locator;
    verfuegungLink: Locator;
    aenderungenMenu: Locator;
    aenderungenMenuItems: Locator;
    infosPageLink: Locator;
    aktionMenu: Locator;
    aktionTrancheErstellen: Locator;
    getAktionStatusUebergangItem: (status: StatusUebergang) => Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      trancheMenu: page.getByTestId('sb-gesuch-header-tranche-nav-menu'),
      trancheMenuItems: page.getByTestId('tranche-nav-menu-item'),
      verfuegungLink: page.getByTestId('sb-gesuch-header-verfuegung-link'),
      aenderungenMenu: page.getByTestId(
        'sb-gesuch-header-aenderungen-nav-menu',
      ),
      aenderungenMenuItems: page.getByTestId('aenderungen-nav-menu-item'),
      infosPageLink: page.getByTestId('sb-gesuch-header-infos-link'),
      aktionMenu: page.getByTestId('sb-gesuch-header-aktion-menu'),
      aktionTrancheErstellen: page.getByTestId('aktion-tranche-erstellen'),
      getAktionStatusUebergangItem: (status: StatusUebergang) =>
        page.getByTestId(`sb-gesuch-header-aktion-status-uebergang-${status}`),
    };
  }
}
