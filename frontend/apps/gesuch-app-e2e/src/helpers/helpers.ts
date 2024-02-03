import { Locator, Page, expect } from '@playwright/test';

export const getStepTitleP = async (page: Page) => {
  return page.getByTestId('step-title');
};

export const expectStepTitleToContainText = async (
  text: string,
  page: Page,
) => {
  return expect(await getStepTitleP(page)).toContainText(text);
};

export const selectMatOption = async (locator: Locator, value: string) => {
  locator.click();

  return locator.page().getByTestId(value).first().click();
};

export const selectMatRadio = async (
  locator: Locator,
  value: string | boolean,
) => {
  if (typeof value === 'boolean') {
    value = value ? 'yes' : 'no';
  }

  return locator.getByTestId(value).getByRole('radio').click();
};

export const expectFormToBeValid = async (form: Locator) => {
  return expect(form).toHaveClass(/ng-valid/);
};
