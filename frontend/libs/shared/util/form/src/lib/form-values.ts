import { AbstractControl, FormGroup, Validators } from '@angular/forms';

import { ElternAbwesenheitsGrund } from '@dv/shared/model/gesuch';

type NonNullableRecord<T extends Record<string, unknown>> = {
  [K in keyof T]: NonNullable<T[K]>;
};

/**
 * Mark given properties as not null, only design time
 */
const fromRequired = <
  T extends { [k: string]: unknown },
  K extends keyof T,
  Keys extends K[] = K[],
>(
  values: T,
  // Keys are only used for the type information during design time
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  _keys: Keys,
) => {
  return values as unknown as Omit<T, Keys[number]> &
    NonNullableRecord<Pick<T, Keys[number]>>;
};

type OnlyString<T> = T extends string ? T : never;

/**
 * This helper function can be used to obtain the non nullable required form values.
 *
 * **required**
 * During form editing some values are nullable but in reality they are required, this methods should ensure
 * that these Values are also reflecting the reality during design-time
 *
 * Sadly it is requiered to specify again which form keys should be handled as **required** because there is
 * no information available about the used validators in a form during design-time.
 *
 * @throws Error if given properties have no `Validators.required` set
 * @param form The form used to obtain the values
 * @param required a list of properties that should be handled as non-nullable
 *                 if none given, all properties are treated as non-nullable
 */
export function convertTempFormToRealValues<
  T extends {
    [K: string]: AbstractControl<unknown>;
  },
  K extends keyof T,
>(form: FormGroup<T>, required?: OnlyString<K>[]) {
  const values = form.getRawValue();
  if (!required) {
    return values as NonNullableRecord<typeof values>;
  }

  const invalidFieldConfig = required.find(
    (f) => !form.get(f)?.hasValidator(Validators.required),
  );
  if (invalidFieldConfig) {
    throw new Error(
      `Form control '${invalidFieldConfig}' has no Validators.required`,
    );
  }

  return fromRequired(values, required);
}

/**
 * Convert a percent string to a number
 */
export function percentStringToNumber(value: string): number;
export function percentStringToNumber(
  value: string | undefined,
): number | undefined;
export function percentStringToNumber(
  value?: string | undefined,
): number | undefined {
  const parsed = parseInt(value ?? '');
  if (isNaN(parsed)) {
    return undefined;
  } else {
    return parsed;
  }
}

/**
 * Convert a number to a percent string or an empty string if the value is undefined or null
 */
export function numberToPercentString(value?: number): string {
  return value?.toString() ?? '';
}

/**
 * Check if the Eltern Abwesenheits Grund is either 'VERSTORBEN' or 'UNBEKANNT'
 */
export const isVerstorbenOrUnbekannt = (grund?: ElternAbwesenheitsGrund) => {
  return (['VERSTORBEN', 'UNBEKANNT'] satisfies ElternAbwesenheitsGrund[]).some(
    (s) => s === grund,
  );
};
