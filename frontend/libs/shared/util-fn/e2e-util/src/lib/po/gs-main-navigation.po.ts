import { Locator, Page } from '@playwright/test';

export class GSMainNavigationPO {
  public elems: {
    page: Page;
    dashboardLink: Locator;
    verfueungLink: Locator;
    auszahlungenLink: Locator;
    nachrichtenLink: Locator;
    delegierenLink: Locator;

    buttonUserMenu: Locator;
    buttonLogout: Locator;
    buttonProfile: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      dashboardLink: page.getByTestId('dashboard-nav-item'),
      verfueungLink: page.getByTestId('verfueung-nav-item'),
      auszahlungenLink: page.getByTestId('auszahlungen-nav-item'),
      nachrichtenLink: page.getByTestId('nachrichten-nav-item'),
      delegierenLink: page.getByTestId('sozialdienst-delegieren-nav-item'),
      buttonUserMenu: page.getByTestId('button-user-menu'),
      buttonLogout: page.getByTestId('button-logout'),
      buttonProfile: page.getByTestId('button-profile'),
    };
  }
}
