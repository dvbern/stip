import { screen, waitFor, within } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';

const dvUserEvent = userEvent.setup();
const dvUserEventForMockedTimers = userEvent.setup({
  advanceTimers: () => {
    jest.runOnlyPendingTimers();
  },
});

/**
 * Return the correct user event depending on whether fake timers are used or not
 */
export const prepareEvent = () => {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  if ((global.Date as any).isFake) {
    return dvUserEventForMockedTimers;
  }
  return dvUserEvent;
};

export function mockElementScrollIntoView() {
  window.HTMLElement.prototype.scrollIntoView = jest.fn();
}

export async function clickMatSelectOption(
  selectTestId: string,
  optionText: string,
) {
  await prepareEvent().click(screen.getByTestId(selectTestId));
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  const listbox = screen.getByRole('listbox');
  const option = within(listbox).getByTestId(optionText);
  await prepareEvent().click(option);
}

export async function clickAutocompleteOption(
  autocompleteTestId: string,
  searchText: string,
  optionText: string,
) {
  const autocomplete = screen.getByTestId(autocompleteTestId);

  (autocomplete as HTMLInputElement).value = '';
  await prepareEvent().type(autocomplete, searchText);
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  const listbox = screen.getByRole('listbox');
  const option = within(listbox).getByText(optionText);
  await prepareEvent().click(option);

  return autocomplete;
}

export async function clickFirstMatSelectOption(selectTestId: string) {
  await clickMatSelectOptionByIndex(selectTestId, 0);
}

export async function clickMatSelectOptionByIndex(
  selectTestId: string,
  index: number,
) {
  await prepareEvent().click(screen.getByTestId(selectTestId));
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  const listbox = screen.getByRole('listbox');
  const options = within(listbox).getAllByRole('option');
  await prepareEvent().click(options[index]);
}

export async function checkMatCheckbox(checkboxTestId: string) {
  const matCheckbox = screen.getByTestId(checkboxTestId);
  const checkbox = within(matCheckbox).getByRole('checkbox');

  expect(checkbox).not.toBeChecked();

  await prepareEvent().click(checkbox);

  expect(checkbox).toBeChecked();

  return checkbox;
}

export async function uncheckMatCheckbox(checkboxTestId: string) {
  const matCheckbox = screen.getByTestId(checkboxTestId);
  const checkbox = within(matCheckbox).getByRole('checkbox');

  expect(checkbox).toBeChecked();

  await prepareEvent().click(checkbox);

  expect(checkbox).not.toBeChecked();

  return checkbox;
}
