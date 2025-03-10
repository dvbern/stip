import { IChange, diff } from 'json-diff-ts';

import {
  AppTrancheChange,
  ElternTyp,
  ElternUpdate,
  FamiliensituationUpdate,
  GesuchFormular,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
} from '@dv/shared/model/gesuch';
import {
  GesuchFormStepView,
  gesuchFormStepsFieldMap,
} from '@dv/shared/model/gesuch-form';
import { lowercased } from '@dv/shared/model/type-util';

export interface ElternSituation {
  expectVater: boolean;
  expectMutter: boolean;
  vater?: ElternUpdate;
  mutter?: ElternUpdate;
}

export function calculateElternSituationGesuch(
  gesuch: SharedModelGesuchFormular | null,
): ElternSituation {
  return calculateElternSituation(gesuch?.familiensituation, gesuch?.elterns);
}

function calculateElternSituation(
  familienSituation: FamiliensituationUpdate | undefined,
  elterns: ElternUpdate[] | undefined,
): ElternSituation {
  return {
    expectVater: calculateExpectElternteil(ElternTyp.VATER, familienSituation),
    expectMutter: calculateExpectElternteil(
      ElternTyp.MUTTER,
      familienSituation,
    ),
    vater: findElternteil(ElternTyp.VATER, elterns),
    mutter: findElternteil(ElternTyp.MUTTER, elterns),
  };
}

export function calculateExpectElternteil(
  type: ElternTyp,
  familienSituation: FamiliensituationUpdate | undefined,
): boolean {
  if (familienSituation) {
    const elternteilLebt = familienSituation.elternteilUnbekanntVerstorben
      ? familienSituation?.[`${lowercased(type)}UnbekanntVerstorben`] ===
        'WEDER_NOCH'
      : true;
    const elternteilZahltKeineAlimente =
      familienSituation.werZahltAlimente !== type &&
      familienSituation.werZahltAlimente !== 'GEMEINSAM';
    return elternteilLebt && elternteilZahltKeineAlimente;
  }
  return false;
}

export function findElternteil(
  elternTyp: ElternTyp,
  elterns: ElternUpdate[] | undefined,
): ElternUpdate | undefined {
  return elterns?.find((eltern) => eltern.elternTyp === elternTyp);
}

/**
 * Flatten the given type to a non-array type.
 */
type Unpack<T> = T extends Array<infer U> ? U : T;
/**
 * Extract the changeable properties of a GesuchFormular.
 */
type ChangeableProperties = Exclude<
  keyof SharedModelGesuchFormular,
  'steuerdatenTabs'
>;
/**
 * Defines the values of a GesuchFormular which are not arrays.
 */
type NonArrayForm = Exclude<
  Unpack<SharedModelGesuchFormular[ChangeableProperties]>,
  unknown[] | undefined
>;
/**
 * Defines the values of a GesuchFormular which are arrays.
 */
type ArrayForms = Extract<
  SharedModelGesuchFormular[ChangeableProperties],
  unknown[]
>;

/**
 * Keys to skip when calculating the changes between two versions of a form.
 */
const keysToSkip = ['id'];

/**
 * This function checks which previous changes should be displayed in the view.
 * If the sachbearbeiter has made changes on the current formular view, the those changes will be displayed and not the previous changes from the gesuchsteller.
 * If the sachbearbeiter has not made changes on the current formular view, the previous changes from the gesuchsteller will be displayed.
 * @param view The view containing the GesuchFormular and the changes.
 * @param key The key of the GesuchFormular property.
 */
export const selectChangeForView = <K extends SharedModelGesuchFormularProps>(
  view: {
    gesuchFormular: GesuchFormular | null;
    tranchenChanges: AppTrancheChange | null;
  },
  key: K,
): {
  current: SharedModelGesuchFormular[K] | undefined;
  previous: SharedModelGesuchFormular[K];
} => {
  const changes = view.tranchenChanges;
  const currentFormular = view.gesuchFormular?.[key];

  const sachbearbeiterHasChangesOnView =
    changes?.sb?.affectedSteps.includes(key) ?? false;

  const current = currentFormular;

  const previous = (
    sachbearbeiterHasChangesOnView
      ? changes?.sb?.tranche?.gesuchFormular?.[key]
      : changes?.gs?.tranche?.gesuchFormular?.[key]
  )!;

  return { current, previous };
};

export const stepHasChanges = (
  tranchenChanges: AppTrancheChange | null,
  step: GesuchFormStepView,
) => {
  return (
    tranchenChanges?.gs?.affectedSteps.includes(
      gesuchFormStepsFieldMap[step.route] ?? -1,
    ) ||
    tranchenChanges?.sb?.affectedSteps.includes(
      gesuchFormStepsFieldMap[step.route] ?? -1,
    )
  );
};

/**
 * Calculate the changes between two versions of a GesuchFormular property.
 */
export function getChangesForForm<T extends NonArrayForm, K extends keyof T>(
  changed?: T,
  original?: T,
) {
  if (!original || !changed) {
    return null;
  }
  const rawDiff = diff(original, changed, { keysToSkip });
  const addChange = <T>(record: T, change: IChange) => {
    // Skip if the record is not an object or the key is already present
    if (!record || typeof record !== 'object' || change.key in record) {
      return record;
    }
    record[change.key as keyof T] =
      // Changes to undefined are marked with 2 entries, one REMOVE and one ADD
      // ADD/REMOVE operations don't have an oldValue
      change.type !== 'UPDATE' ? change.value : change.oldValue;
    return record;
  };

  const difference = rawDiff.reduce(
    (acc, c) => {
      if (c.changes) {
        acc[c.key as K] = c.changes.reduce(
          (sub, s) => addChange(sub, s),
          {} as T[K],
        );
      } else {
        addChange(acc, c);
      }
      return acc;
    },
    {} as {
      [key in K]?: T[key];
    },
  );

  if (Object.keys(difference).length === 0) {
    return null;
  }
  return difference;
}

/**
 * Calculate the changes between two lists of GesuchFormular properties using a custom identifier.
 */
export function getChangesForList<
  const T extends ArrayForms[number],
  const R extends string,
>(
  changed: T[] | undefined,
  original: T[] | undefined,
  getIdentifier?: (value: T) => R | undefined,
) {
  if (!original || !changed) {
    return null;
  }
  const _changes = diff(original, changed, {
    keysToSkip,
  }); // We only care about the first change because the dataset is just a list
  const changes = _changes[0];

  if (!changes) {
    return null;
  }

  // Identify new entries
  let newEntriesByIdentifier: Partial<Record<R, boolean>> = {};
  if (changes.type === 'UPDATE') {
    newEntriesByIdentifier =
      changes.changes
        ?.filter((c) => c.type === 'ADD')
        .reduce<Partial<Record<R, boolean>>>((entries, c) => {
          const identifier = getIdentifier?.(c.value);
          return { ...entries, [identifier ?? +c.key]: true };
        }, {}) ?? {};
  }

  // Collect all changes and associate them with the identifier
  const collectedChanges = original.map((c, i) => ({
    identifier: getIdentifier?.(c),
    values: getChangesForForm(
      // Compare using the identifier
      getIdentifier
        ? changed.find((cc) => getIdentifier(cc) === getIdentifier(c))
        : // Otherwise fallback to index comparison
          changed[i],
      c,
    ),
  }));

  if (
    collectedChanges.length === 0 &&
    Object.keys(newEntriesByIdentifier).length === 0
  ) {
    return null;
  }

  return {
    // Group changes by identifier
    changesByIndex: collectedChanges.map((c) => c.values),
    changesByIdentifier: collectedChanges.reduce(
      (acc, c) => ({
        ...acc,
        ...(c.identifier ? { [c.identifier]: c.values } : {}),
      }),
      {} as Record<R, T>,
    ),
    newEntriesByIdentifier,
  };
}
