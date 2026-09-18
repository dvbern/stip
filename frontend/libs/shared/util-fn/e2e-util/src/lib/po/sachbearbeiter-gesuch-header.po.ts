import { Locator, Page } from '@playwright/test';

import { StatusUebergang } from '@dv/shared/model/gesuch';

export class SachbearbeiterGesuchHeaderPO {
  public elems: {
    page: Page;
    trancheMenu: Locator;
    trancheMenuItems: Locator;
    verfuegungLink: Locator;
    aenderungenLink: Locator;
    aenderungenMenu: Locator;
    aenderungenMenuItems: Locator;
    infosPageLink: Locator;
    aktionMenu: Locator;
    actionLoading: Locator;
    aktionTrancheErstellen: Locator;
    getAktionStatusUebergangItem: (status: StatusUebergang) => Locator;
    aenderungAccept: Locator;
    aenderungReject: Locator;
    aenderungManuallyChange: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      trancheMenu: page.getByTestId('sb-gesuch-header-tranche-nav-menu'),
      trancheMenuItems: page.getByTestId('tranche-nav-menu-item'),
      verfuegungLink: page.getByTestId('sb-gesuch-header-verfuegung-link'),
      aenderungenLink: page.getByTestId(
        'sb-gesuch-header-aenderungen-nav-link',
      ),
      aenderungenMenu: page.getByTestId(
        'sb-gesuch-header-aenderungen-nav-menu',
      ),
      aenderungenMenuItems: page.getByTestId('aenderungen-nav-menu-item'),
      infosPageLink: page.getByTestId('sb-gesuch-header-infos-nav-link'),
      aktionMenu: page.getByTestId('sb-gesuch-header-aktion-menu'),
      actionLoading: page.getByTestId('loading-action-menu'),
      aktionTrancheErstellen: page.getByTestId('aktion-tranche-erstellen'),
      getAktionStatusUebergangItem: (status: StatusUebergang) =>
        page.getByTestId(`sb-gesuch-header-aktion-status-uebergang-${status}`),
      aenderungAccept: page.getByTestId('aenderung-accept'),
      aenderungReject: page.getByTestId('aenderung-reject'),
      aenderungManuallyChange: page.getByTestId('aenderung-manually-change'),
    };
  }
}
