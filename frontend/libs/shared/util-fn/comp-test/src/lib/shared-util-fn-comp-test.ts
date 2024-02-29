import { screen, waitFor, within } from '@testing-library/angular';
import { userEvent } from '@testing-library/user-event';

export const LONG_RUNNING_TEST_TIMEOUT = 10000;
export const dvUserEvent = userEvent.setup({
  advanceTimers: () => jest.runOnlyPendingTimers(),
});

export function mockElementScrollIntoView() {
  window.HTMLElement.prototype.scrollIntoView = jest.fn();
}

export async function clickMatSelectOption(
  selectTestId: string,
  optionText: string,
) {
  await dvUserEvent.click(screen.getByTestId(selectTestId));
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  const listbox = screen.getByRole('listbox');
  const option = within(listbox).getByTestId(optionText);
  await dvUserEvent.click(option);
}

export async function clickFirstMatSelectOption(selectTestId: string) {
  await dvUserEvent.click(screen.getByTestId(selectTestId));
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  const listbox = screen.getByRole('listbox');
  const options = within(listbox).getAllByRole('option');
  await dvUserEvent.click(options[0]);
}

export async function clickMatSelectOptionByIndex(
  selectTestId: string,
  index: number,
) {
  await dvUserEvent.click(screen.getByTestId(selectTestId));
  await waitFor(() =>
    expect(screen.queryByRole('listbox')).toBeInTheDocument(),
  );
  const listbox = screen.getByRole('listbox');
  const options = within(listbox).getAllByRole('option');
  await dvUserEvent.click(options[index]);
}

export async function checkMatCheckbox(checkboxTestId: string) {
  const matCheckbox = screen.getByTestId(checkboxTestId);
  const checkbox = within(matCheckbox).getByRole('checkbox');

  expect(checkbox).not.toBeChecked();

  await dvUserEvent.click(checkbox);

  expect(checkbox).toBeChecked();

  return checkbox;
}

export async function uncheckMatCheckbox(checkboxTestId: string) {
  const matCheckbox = screen.getByTestId(checkboxTestId);
  const checkbox = within(matCheckbox).getByRole('checkbox');

  expect(checkbox).toBeChecked();

  await dvUserEvent.click(checkbox);

  expect(checkbox).not.toBeChecked();

  return checkbox;
}
