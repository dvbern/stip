import { Operation, diff } from 'json-diff-ts';

import {
  ElternTyp,
  ElternUpdate,
  FamiliensituationUpdate,
  SharedModelGesuchFormular,
} from '@dv/shared/model/gesuch';
import { lowercased } from '@dv/shared/util-fn/string-helper';
import { isDefined } from '@dv/shared/util-fn/type-guards';

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

type Unpack<T> = T extends Array<infer U> ? U : T;
type ChangeableProperties = Exclude<
  keyof SharedModelGesuchFormular,
  'steuerdatenTabs'
>;
type NonArrayForm = Exclude<
  Unpack<SharedModelGesuchFormular[ChangeableProperties]>,
  unknown[] | undefined
>;
type ArrayForms = Extract<
  SharedModelGesuchFormular[ChangeableProperties],
  unknown[]
>;

const handledChangeTypes: Operation[] = [Operation.ADD, Operation.UPDATE];

export function getChangesForForm<T extends NonArrayForm, K extends keyof T>(
  original?: T,
  changed?: T,
) {
  if (!original || !changed) {
    return null;
  }
  const dif = diff(changed, original, { keysToSkip: ['id'] });
  const difference = dif.reduce(
    (acc, c) => {
      if (handledChangeTypes.includes(c.type)) {
        if (c.changes) {
          acc[c.key as K] = c.changes
            .filter((s) => handledChangeTypes.includes(s.type))
            .reduce(
              (sub, s) => ({
                ...sub,
                [s.key]: s.oldValue ?? '',
              }),
              {} as T[K],
            );
        } else {
          acc[c.key as K] = c.oldValue ?? '';
        }
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

export function getChangesForList<
  const T extends ArrayForms[number],
  const R extends string | undefined,
>(
  original: T[] | undefined,
  changed: T[] | undefined,
  getIdentifier: (value: T) => R,
) {
  if (!original || !changed) {
    return null;
  }
  const changes = diff(changed, original, { keysToSkip: ['id'] })[0];
  if (!changes) {
    return null;
  }
  let newIndexes: number[] = [];
  if (changes.type === 'UPDATE') {
    newIndexes =
      changes.changes?.filter((c) => c.type === 'ADD').map((c) => +c.key) ?? [];
  }
  const collectedChanges = original
    .map((c, i) => ({
      identifier: getIdentifier(c),
      values: getChangesForForm(
        c,
        // TODO: Check if diff library has a better way to compare arrays
        // Compare using the identifier
        changed.find((cc) => getIdentifier(cc) === getIdentifier(c)) ??
          // Otherwise fallback to index comparison
          changed[i],
      ),
    }))
    .filter((c) => isDefined(c.values));
  if (collectedChanges.length === 0) {
    return null;
  }
  return {
    changes: collectedChanges.filter((c) => isDefined(c.values)),
    changesByIdentifier: collectedChanges.reduce(
      (acc, c) => ({
        ...acc,
        ...(c.identifier ? { [c.identifier]: c.values } : {}),
      }),
      {} as Record<Exclude<R, undefined>, T>,
    ),
    newIndexes,
  };
}
