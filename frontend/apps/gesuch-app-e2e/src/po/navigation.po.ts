import { Locator, Page } from '@playwright/test';

export class NavigationPO {
  public elems: {
    page: Page;
    navLinkDashboard: Locator;
    buttonUserMenu: Locator;
    buttonLogout: Locator;
    buttonProfile: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      navLinkDashboard: page.getByTestId('gesuch-step-nav-dashboard'),
      buttonUserMenu: page.getByTestId('button-user-menu'),
      buttonLogout: page.getByTestId('button-logout'),
      buttonProfile: page.getByTestId('button-profile'),
    };
  }
}
